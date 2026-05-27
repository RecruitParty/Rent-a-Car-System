package reservation;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import db.DBConnector;

public class returnCarService {

    static Connection conn = null;

    public static void returnCar(
            int cusID,
            String carNo,
            int returnDest,
            Date actualReturnDate
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
                    "SELECT r.rental_id " +
                    "FROM Rental r " +
                    "JOIN Rental_record rr " +
                    "ON r.rental_id = rr.rental_id " +
                    "WHERE r.user_id = ? " +
                    "AND r.car_no = ? " +
                    "AND rr.rental_state IN ('진행중', '연체')";

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
                    "SELECT rental_date, rental_state " +
                    "FROM Rental_record " +
                    "WHERE rental_id = ?";

            PreparedStatement infoStmt = conn.prepareStatement(infoSql);

            infoStmt.setInt(1, reserveNo);

            ResultSet infoRs = infoStmt.executeQuery();

            Date startDate = null;
            String rentalState = "";

            if(infoRs.next()) {

                startDate = infoRs.getDate("rental_date");

                rentalState = infoRs.getString("rental_state");
            }

            String updateSql = "";

            if(rentalState.equals("연체")) {

                updateSql =
                        "UPDATE Rental_record " +
                        "SET rental_state = '완료(연체됨)', " +
                        "return_dest = ?, " +
                        "actual_return_date = ? " +
                        "WHERE rental_id = ?";

            } else {

                updateSql =
                        "UPDATE Rental_record " +
                        "SET rental_state = '완료', " +
                        "return_dest = ?, " +
                        "actual_return_date = ? " +
                        "WHERE rental_id = ?";
            }

            PreparedStatement updateStmt = conn.prepareStatement(updateSql);

            updateStmt.setInt(1, returnDest);
            updateStmt.setDate(2, actualReturnDate);
            updateStmt.setInt(3, reserveNo);

            int updated = updateStmt.executeUpdate();

            if(updated <= 0) {
                conn.rollback();

                System.out.println("반납 처리 실패");

                return;
            }

            String priceSql =
                    "SELECT daily_rental_fee " +
                    "FROM car " +
                    "WHERE car_no = ?";

            PreparedStatement priceStmt = conn.prepareStatement(priceSql);

            priceStmt.setString(1, carNo);

            ResultSet priceRs = priceStmt.executeQuery();

            int dayPrice = 0;

            if(priceRs.next()) {

                dayPrice = priceRs.getInt("daily_rental_fee");
            }
            long diff = (actualReturnDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);

            int totalPrice = (int)diff * dayPrice;
            
            conn.commit();

            System.out.println("반납이 완료되었습니다.");

            System.out.println("총 금액 : " + totalPrice);

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