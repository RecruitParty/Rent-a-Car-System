package reservation;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import db.DBConnector;

public class reservationCancelService {

    static Connection conn = null;

    public static void reservationCancel(int cusID, String carNo) {

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
                    "WHERE car_no = ?";

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);

            checkStmt.setString(1, carNo);

            ResultSet rs = checkStmt.executeQuery();

            boolean rentState = true;

            if(rs.next()) {

                rentState = rs.getBoolean("rental_availability");
            }
            else {

                conn.rollback();

                System.out.println("존재하지 않는 차량입니다.");

                return;
            }

            if(rentState) {

                conn.rollback();

                System.out.println("대여되지 않은 차량입니다.");

                return;
            }


            String reserveSql =
                    "SELECT rental_id " +
                    "FROM Rental " +
                    "WHERE user_id = ? " +
                    "AND car_no = ?";

            PreparedStatement reserveStmt = conn.prepareStatement(reserveSql);

            reserveStmt.setInt(1, cusID);
            reserveStmt.setString(2, carNo);

            ResultSet reserveRs = reserveStmt.executeQuery();

            int reserveNo = 0;

            if(reserveRs.next()) {

                reserveNo = reserveRs.getInt("rental_id");
            }
            else{
            	conn.rollback();

                System.out.println("취소가능한 예약이 없습니다.");
                
                return;
            }

            String infoSql =
                    "SELECT rental_state, rental_date " +
                    "FROM Rental_record " +
                    "WHERE rental_id = ?";

            PreparedStatement infoStmt = conn.prepareStatement(infoSql);

            infoStmt.setInt(1, reserveNo);

            ResultSet infoRs = infoStmt.executeQuery();

            String rentalState = "";
            Date rentalDate = null;

            if(infoRs.next()) {

                rentalState = infoRs.getString("rental_state");

                rentalDate = infoRs.getDate("rental_date");
            }

            Date currentDate =
                    new Date(
                            System.currentTimeMillis()
                    );

            long diff =
                    rentalDate.getTime()
                    - currentDate.getTime();

            long dayDiff =
                    diff / (1000 * 60 * 60 * 24);

            if(!rentState && rentalState.equals("예약완료") && dayDiff >= 1) {

                String updateCarSql =
                        "UPDATE car " +
                        "SET rental_availability = TRUE " +
                        "WHERE car_no = ?";

                PreparedStatement updateCarStmt = conn.prepareStatement(updateCarSql);

                updateCarStmt.setString(1, carNo);

                int updateCar = updateCarStmt.executeUpdate();

                if(updateCar <= 0) {

                    conn.rollback();

                    System.out.println("차량 상태 변경 실패");

                    return;
                }

                String cancelSql =
                        "UPDATE Rental_record " +
                        "SET rental_state = '예약취소' " +
                        "WHERE rental_id = ?";

                PreparedStatement cancelStmt = conn.prepareStatement(cancelSql);

                cancelStmt.setInt(1, reserveNo);

                int cancel = cancelStmt.executeUpdate();

                if(cancel <= 0) {

                    conn.rollback();

                    System.out.println("예약 취소에 실패하셨습니다.");

                    return;
                }

                conn.commit();

                System.out.println("예약이 취소되었습니다.");

            } else {

                conn.rollback();

                System.out.println("취소 가능한 예약이 없습니다.");
            }

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