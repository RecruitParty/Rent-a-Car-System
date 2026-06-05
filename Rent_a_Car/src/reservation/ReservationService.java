package reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import db.DBConnector;
import model.Rental;
import model.RentalRecord;

public class ReservationService {

    public static boolean reservation(
            int cusID,
            String carNo,
            int startLocation,
            int endLocation,
            Date startDate,
            Date dueDate) {

        Connection conn = null;

        try {

            conn = DBConnector.getConnection();

            conn.setAutoCommit(false);

            LocalDate rentalDate =
                    startDate.toLocalDate();

            LocalDate expectedReturnDate =
                    dueDate.toLocalDate();

            if (expectedReturnDate.isBefore(rentalDate)) {

                System.out.println("반납 예정일은 대여일보다 빠를 수 없습니다.");

                return false;
            }
            if (!expectedReturnDate.isAfter(rentalDate)) {

                System.out.println("반납 예정일은 대여일 이후여야 합니다.");

                return false;
            }
            LocalDate today = LocalDate.now();

            if (rentalDate.isBefore(today)) {

                System.out.println("과거 날짜로 예약할 수 없습니다.");

                return false;
            }
            
            String userSql =
                    "SELECT 1 " +
                    "FROM customer " +
                    "WHERE user_id = ?";

            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {

                userStmt.setInt(1, cusID);

                ResultSet userRs = userStmt.executeQuery();

                if (!userRs.next()) {

                    conn.rollback();

                    System.out.println("등록되지 않은 고객입니다.");

                    return false;
                }
            }

            String checkSql =
                    "SELECT rental_availability " +
                    "FROM car " +
                    "WHERE car_no = ? " +
                    "FOR UPDATE";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

                checkStmt.setString(1, carNo);

                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {

                    conn.rollback();

                    System.out.println("존재하지 않는 차량입니다.");

                    return false;
                }

                if (!rs.getBoolean("rental_availability")) {

                    conn.rollback();

                    System.out.println("이미 대여중인 차량입니다.");

                    return false;
                }
            }

            String updateCarSql =
                    "UPDATE car " +
                    "SET rental_availability = FALSE " +
                    "WHERE car_no = ?";

            try (PreparedStatement updateStmt = conn.prepareStatement(updateCarSql)) {

                updateStmt.setString(1, carNo);

                int updateCount = updateStmt.executeUpdate();

                if (updateCount == 0) {

                    conn.rollback();

                    System.out.println("차량 상태 변경 실패");

                    return false;
                }
            }

            int rentalId = 0;

            String idSql =
                    "SELECT COALESCE(MAX(rental_id),0)+1 " +
                    "AS next_id " +
                    "FROM rental";

            try (PreparedStatement idStmt = conn.prepareStatement(idSql);

                 ResultSet rs = idStmt.executeQuery()) {

                if (rs.next()) {

                    rentalId = rs.getInt("next_id");
                }
            }

            Rental rental =
                    new Rental(
                            rentalId,
                            cusID,
                            carNo
                    );

            RentalRecord rentalRecord =
                    new RentalRecord(
                            rentalId,
                            startLocation,
                            endLocation,
                            startDate.toLocalDate(),
                            dueDate.toLocalDate(),
                            null,
                            "예약완료"
                    );

            String rentalSql =
                    "INSERT INTO rental(" +
                    "rental_id, " +
                    "user_id, " +
                    "car_no" +
                    ") VALUES (?, ?, ?)";

            try (PreparedStatement rentalStmt = conn.prepareStatement(rentalSql)) {

                rentalStmt.setInt(
                        1,
                        rental.getRental_id());

                rentalStmt.setInt(
                        2,
                        rental.getUser_id());

                rentalStmt.setString(
                        3,
                        rental.getCar_no());

                int result = rentalStmt.executeUpdate();

                if (result == 0) {

                    conn.rollback();

                    System.out.println("Rental 저장 실패");

                    return false;
                }
            }

            String recordSql =
                    "INSERT INTO rental_record(" +
                    "rental_id, " +
                    "rental_spot, " +
                    "return_spot, " +
                    "rental_date, " +
                    "expected_return_date, " +
                    "rental_state" +
                    ") VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement recordStmt =
                         conn.prepareStatement(recordSql)) {

                recordStmt.setInt(
                        1,
                        rentalRecord.getRental_id());

                recordStmt.setInt(
                        2,
                        rentalRecord.getRental_spot());

                recordStmt.setInt(
                        3,
                        rentalRecord.getReturn_spot());

                recordStmt.setDate(
                        4,
                        Date.valueOf(
                                rentalRecord.getRental_date()));

                recordStmt.setDate(
                        5,
                        Date.valueOf(
                                rentalRecord.getExpected_return_date()));

                recordStmt.setString(
                        6,
                        rentalRecord.getRental_state());

                int result = recordStmt.executeUpdate();

                if (result == 0) {

                    conn.rollback();

                    System.out.println("RentalRecord 저장 실패");

                    return false;
                }
            }

            conn.commit();

            System.out.println("예약이 완료되었습니다.");
            return true;

        } catch (Exception e) {

            try {

                if (conn != null) {

                    conn.rollback();
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {

            try {

                if (conn != null) {

                    conn.setAutoCommit(true);

                    conn.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
		return false;
    }
}