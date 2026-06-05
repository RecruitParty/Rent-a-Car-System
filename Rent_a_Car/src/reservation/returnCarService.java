package reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import db.DBConnector;
import model.Rental;
import model.RentalRecord;

public class returnCarService {

    public static boolean returnCar(
            int cusID,
            String carNo,
            int returnDest,
            Date actualReturnDate) {

        Connection conn = null;

        try {

            conn = DBConnector.getConnection();

            conn.setAutoCommit(false);

            String userSql =
                    "SELECT 1 " +
                    "FROM customer " +
                    "WHERE user_id = ?";

            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {

                userStmt.setInt(1, cusID);

                ResultSet rs = userStmt.executeQuery();

                if (!rs.next()) {

                    conn.rollback();

                    System.out.println("등록되지 않은 고객입니다.");

                    return false;
                }
            }

            boolean rentState;

            String carSql =
                    "SELECT rental_availability " +
                    "FROM car " +
                    "WHERE car_no = ? " +
                    "FOR UPDATE";

            try (PreparedStatement carStmt = conn.prepareStatement(carSql)) {

                carStmt.setString(1, carNo);

                ResultSet rs = carStmt.executeQuery();

                if (!rs.next()) {

                    conn.rollback();

                    System.out.println("존재하지 않는 차량입니다.");

                    return false;
                }

                rentState = rs.getBoolean("rental_availability");
            }

            if (rentState) {

                conn.rollback();

                System.out.println("대여중인 차량이 아닙니다.");

                return false;
            }

            Rental rental = null;

            String rentalSql =
                    "SELECT r.* " +
                    "FROM rental r " +
                    "JOIN rental_record rr " +
                    "ON r.rental_id = rr.rental_id " +
                    "WHERE r.user_id = ? " +
                    "AND r.car_no = ? " +
                    "AND rr.rental_state IN ('진행중','연체')";

            try (PreparedStatement rentalStmt = conn.prepareStatement(rentalSql)) {

                rentalStmt.setInt(1, cusID);
                rentalStmt.setString(2, carNo);

                ResultSet rs = rentalStmt.executeQuery();

                if (rs.next()) {

                    rental = 
                    		new Rental(
                                    rs.getInt("rental_id"),
                                    rs.getInt("user_id"),
                                    rs.getString("car_no")
                            );
                }
            }

            if (rental == null) {

                conn.rollback();

                System.out.println("반납 가능한 대여 내역이 없습니다.");

                return false;
            }

            RentalRecord rentalRecord = null;

            String recordSql =
                    "SELECT * " +
                    "FROM rental_record " +
                    "WHERE rental_id = ?";

            try (PreparedStatement recordStmt = conn.prepareStatement(recordSql)) {

                recordStmt.setInt(
                        1,
                        rental.getRental_id());

                ResultSet rs = recordStmt.executeQuery();

                if (rs.next()) {

                    Date actualDate = rs.getDate("actual_return_date");

                    rentalRecord =
                            new RentalRecord(
                                    rs.getInt("rental_id"),
                                    rs.getInt("rental_spot"),
                                    rs.getInt("return_spot"),
                                    rs.getDate("rental_date")
                                            .toLocalDate(),
                                    rs.getDate("expected_return_date").toLocalDate(),
                                    actualDate == null
                                            ? null
                                            : actualDate.toLocalDate(),
                                    rs.getString("rental_state")
                            );
                }
            }

            if (rentalRecord == null) {

                conn.rollback();

                System.out.println("대여 정보를 찾을 수 없습니다.");

                return false;
            }

            if ("연체".equals(rentalRecord.getRental_state())) {

                rentalRecord.setRental_state("완료(연체됨)");

            } else {

                rentalRecord.setRental_state("완료");
            }

            rentalRecord.setReturn_spot(returnDest);

            rentalRecord.setActual_return_date(actualReturnDate.toLocalDate());

            String updateRecordSql =
                    "UPDATE rental_record " +
                    "SET rental_state = ?, " +
                    "return_spot = ?, " +
                    "actual_return_date = ? " +
                    "WHERE rental_id = ?";

            try (PreparedStatement updateStmt =
                         conn.prepareStatement(
                                 updateRecordSql)) {

                updateStmt.setString(
                        1,
                        rentalRecord.getRental_state());

                updateStmt.setInt(
                        2,
                        rentalRecord.getReturn_spot());

                updateStmt.setDate(
                        3,
                        Date.valueOf(
                                rentalRecord
                                        .getActual_return_date()));

                updateStmt.setInt(
                        4,
                        rentalRecord.getRental_id());

                if (updateStmt.executeUpdate() == 0) {

                    conn.rollback();

                    System.out.println("반납 처리 실패");

                    return false;
                }
            }

            String availableSql =
                    "UPDATE car " +
                    "SET rental_availability = TRUE, " +
                    "spot_no = ? " +
                    "WHERE car_no = ?";

            try (PreparedStatement carUpdateStmt =
                         conn.prepareStatement(availableSql)) {

                carUpdateStmt.setInt(
                        1,
                        returnDest);

                carUpdateStmt.setString(
                        2,
                        carNo);

                carUpdateStmt.executeUpdate();
            }

            int dailyFee = 0;

            String feeSql =
                    "SELECT daily_rental_fee " +
                    "FROM car " +
                    "WHERE car_no = ?";

            try (PreparedStatement feeStmt = conn.prepareStatement(feeSql)) {

                feeStmt.setString(1, carNo);

                ResultSet rs = feeStmt.executeQuery();

                if (rs.next()) {

                    dailyFee = rs.getInt("daily_rental_fee");
                }
            }

            long rentalDays =
                    actualReturnDate.toLocalDate()
                                    .toEpochDay()
                    -
                    rentalRecord.getRental_date()
                                .toEpochDay();

            int totalPrice = (int) rentalDays * dailyFee;

            conn.commit();

            System.out.println("반납이 완료되었습니다.");
            System.out.println("총 금액 : " + totalPrice);
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