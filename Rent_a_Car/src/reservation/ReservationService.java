package reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import db.DBConnector;
import model.Car;
import model.Rental;
import model.RentalRecord;

public class ReservationService {

    static Connection conn = null;

    public static void reservation(
            int cusID,
            String carNo,
            int startLocation,
            int endLocation,
            Date startDate,
            Date dueDate
    ) {

        try {

            conn = DBConnector.getConnection();

            System.out.println("DB 연결 성공");

            conn.setAutoCommit(false);
            
            String userSql =
                    "SELECT * FROM customer " +
                    "WHERE user_id = ?";

            PreparedStatement userStmt = conn.prepareStatement(userSql);

            userStmt.setInt(1, cusID);

            ResultSet userRs = userStmt.executeQuery();

            if(!userRs.next()) {
                conn.rollback();

                System.out.println("등록되지 않는 아이디입니다.");


                return;

            }

            String checkSql =
                    "SELECT rental_availability " +
                    "FROM car " +
                    "WHERE car_no = ? FOR UPDATE";

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);

            checkStmt.setString(1, carNo);

            ResultSet rs = checkStmt.executeQuery();

            boolean rentState = true;

            if(rs.next()) {

                rentState = rs.getBoolean("rental_availability");

            } else {

                conn.rollback();

                System.out.println("존재하지 않는 차량입니다.");

                return;
            }

            if(!rentState) {

                conn.rollback();

                System.out.println("이미 대여중인 차량입니다.");

                return;
            }

            String updateCarSql =
                    "UPDATE car " +
                    "SET rental_availability = FALSE " +
                    "WHERE car_no = ?";

            PreparedStatement updateStmt = conn.prepareStatement(updateCarSql);

            updateStmt.setString(1, carNo);

            int update = updateStmt.executeUpdate();

            if(update <= 0) {

                conn.rollback();

                System.out.println("차량 상태 변경에 실패하셨습니다.");

                return;
            }

            String countSql =
                    "SELECT COUNT(*) + 1 AS next_id " +
                    "FROM Rental_record";

            PreparedStatement countStmt = conn.prepareStatement(countSql);

            ResultSet countRs = countStmt.executeQuery();

            int reservationID = 0;

            if(countRs.next()) {

                reservationID = countRs.getInt("next_id");
            }
            
            String rentalSql =
                    "INSERT INTO Rental(" +
                    "rental_id, " +
                    "user_id, " +
                    "car_no" +
                    ") VALUES (?, ?, ?)";

            PreparedStatement rentalStmt = conn.prepareStatement(rentalSql);

            rentalStmt.setInt(1, reservationID);
            rentalStmt.setInt(2, cusID);
            rentalStmt.setString(3, carNo);

            int insertRental = rentalStmt.executeUpdate();

            if(insertRental <= 0) {

                conn.rollback();

                System.out.println("Rental에 저장에 실패하셨습니다.");

                return;
            }
            
            String recordSql =
                    "INSERT INTO Rental_record(" +
                    "rental_id, " +
                    "rental_dest, " +
                    "return_dest, " +
                    "rental_date, " +
                    "expected_return_date, " +
                    "rental_state" +
                    ") VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement recordStmt = conn.prepareStatement(recordSql);

            recordStmt.setInt(1, reservationID);
            recordStmt.setInt(2, startLocation);
            recordStmt.setInt(3, endLocation);
            recordStmt.setDate(4, startDate);
            recordStmt.setDate(5, dueDate);
            recordStmt.setString(6, "예약완료");

            int insertRecord = recordStmt.executeUpdate();

            if(insertRecord <= 0) {

                conn.rollback();

                System.out.println("Rental_record에 저장에 실패하셨습니다.");

                return;
            }
            conn.commit();
            
            System.out.println("예약이 완료되었습니다.");

        } catch(Exception e) {

            try {

                if(conn != null) {

                    conn.rollback();

                    System.out.println("트랜잭션 롤백");
                }

            } catch(Exception ex) {

                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {

            try {

                if(conn != null) {

                    conn.setAutoCommit(true);

                    conn.close();
                }

            } catch(Exception e) {

                e.printStackTrace();
            }
        }
    }
}