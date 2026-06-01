package reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import db.DBConnector;
import model.Rental;
import model.RentalRecord;

public class reservationCancelService {

    public static void reservationCancel(
            int cusID,
            String carNo) {

        Connection conn = null;

        try {

            conn = DBConnector.getConnection();

            conn.setAutoCommit(false);

            String userSql =
                    "SELECT 1 " +
                    "FROM customer " +
                    "WHERE user_id = ?";

            try (
                PreparedStatement userStmt = conn.prepareStatement(userSql)
            ) {

                userStmt.setInt(1, cusID);

                ResultSet userRs =
                        userStmt.executeQuery();

                if (!userRs.next()) {

                    conn.rollback();

                    System.out.println("등록되지 않은 고객입니다.");

                    return;
                }
            }

            String checkSql =
                    "SELECT rental_availability " +
                    "FROM car " +
                    "WHERE car_no = ? " +
                    "FOR UPDATE";

            boolean rentState;

            try (
                PreparedStatement checkStmt = conn.prepareStatement(checkSql)
            ) {

                checkStmt.setString(1, carNo);

                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {

                    conn.rollback();

                    System.out.println("존재하지 않는 차량입니다.");

                    return;
                }

                rentState = rs.getBoolean(
                                "rental_availability");
            }

            if (rentState) {

                conn.rollback();

                System.out.println("대여되지 않은 차량입니다.");

                return;
            }

            Rental rental = null;

            String rentalSql =
                    "SELECT * " +
                    "FROM rental " +
                    "WHERE user_id = ? " +
                    "AND car_no = ?";

            try (
                PreparedStatement rentalStmt = conn.prepareStatement(rentalSql)
            ) {

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

                System.out.println("취소 가능한 예약이 없습니다.");

                return;
            }

            RentalRecord rentalRecord = null;

            String recordSql =
                    "SELECT * " +
                    "FROM rental_record " +
                    "WHERE rental_id = ?";

            try (
                PreparedStatement recordStmt = conn.prepareStatement(recordSql)
            ) {

                recordStmt.setInt(
                        1,
                        rental.getRental_id());

                ResultSet rs = recordStmt.executeQuery();

                if (rs.next()) {

                    Date actualDate =
                            rs.getDate(
                                    "actual_return_date");

                    rentalRecord =
                            new RentalRecord(
                                    rs.getInt("rental_id"),
                                    rs.getInt("rental_spot"),
                                    rs.getInt("return_spot"),
                                    rs.getDate("rental_date")
                                            .toLocalDate(),
                                    rs.getDate("expected_return_date")
                                            .toLocalDate(),
                                    actualDate == null
                                            ? null
                                            : actualDate.toLocalDate(),
                                    rs.getString("rental_state")
                            );
                }
            }

            if (rentalRecord == null) {

                conn.rollback();

                System.out.println(
                        "예약 정보를 찾을 수 없습니다.");

                return;
            }

            LocalDate today = LocalDate.now();

            long dayDiff = rentalRecord.getRental_date() .toEpochDay() - today.toEpochDay();

            if (!"예약완료".equals(rentalRecord.getRental_state()) || dayDiff < 1) {

                conn.rollback();

                System.out.println("취소 가능한 예약이 없습니다.");

                return;
            }

            String updateCarSql =
                    "UPDATE car " +
                    "SET rental_availability = TRUE " +
                    "WHERE car_no = ?";

            try (
                PreparedStatement updateStmt = conn.prepareStatement(updateCarSql)
            ) {

                updateStmt.setString(
                        1,
                        rental.getCar_no());

                int result = updateStmt.executeUpdate();

                if (result == 0) {

                    conn.rollback();

                    System.out.println("차량 상태 변경 실패");

                    return;
                }
            }

            rentalRecord.setRental_state("예약취소");

            String cancelSql =
                    "UPDATE rental_record " +
                    "SET rental_state = ? " +
                    "WHERE rental_id = ?";

            try (
                PreparedStatement cancelStmt = conn.prepareStatement(cancelSql)
            ) {

                cancelStmt.setString(
                        1,
                        rentalRecord.getRental_state());

                cancelStmt.setInt(
                        2,
                        rentalRecord.getRental_id());

                int result = cancelStmt.executeUpdate();

                if (result == 0) {

                    conn.rollback();

                    System.out.println("예약 취소 실패");

                    return;
                }
            }

            conn.commit();

            System.out.println("예약이 취소되었습니다.");

        } catch (Exception e) {

            try {

                if (conn != null) {

                    conn.rollback();

                    System.out.println("트랜잭션 롤백");
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
    }
}