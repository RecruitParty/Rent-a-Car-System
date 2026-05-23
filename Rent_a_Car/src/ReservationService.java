import java.sql.*;

public class ReservationService {

    public void reservation(int cusID, String carNo, int startLocation, int endLocation, String startDate, String dueDate) {

        String sql = "{CALL reservation(?, ?, ?, ?, ?, ?)}";

        try (
                Connection conn = DBConnector.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)
        ) {

            cstmt.setInt(1, cusID);
            cstmt.setString(2, carNo);
            cstmt.setInt(3, startLocation);
            cstmt.setInt(4, endLocation);
            cstmt.setDate(5, Date.valueOf(startDate));
            cstmt.setDate(6, Date.valueOf(dueDate));

            ResultSet rs = cstmt.executeQuery();

            while(rs.next()) {
                System.out.println(rs.getString("result"));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void reservationCancel(int cusID, String carNo) {

        String sql = "{CALL reservationCancel(?, ?)}";

        try (
                Connection conn = DBConnector.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)
        ) {

            cstmt.setInt(1, cusID);
            cstmt.setString(2, carNo);

            ResultSet rs = cstmt.executeQuery();

            while(rs.next()) {
                System.out.println(rs.getString("result"));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void returnCar(
            int cusID,
            String carNo,
            int returnDest,
            String actualReturnDate
    ) {

        String sql = "{CALL returncar(?, ?, ?, ?)}";

        try (
                Connection conn = DBConnector.getConnection();
                CallableStatement cstmt = conn.prepareCall(sql)
        ) {

            cstmt.setInt(1, cusID);
            cstmt.setString(2, carNo);
            cstmt.setInt(3, returnDest);
            cstmt.setDate(4, Date.valueOf(actualReturnDate));

            ResultSet rs = cstmt.executeQuery();

            while(rs.next()) {

                System.out.println(rs.getString("result"));

                try {
                    System.out.println(
                            "총 금액 : " + rs.getInt("total_price")
                    );
                } catch(Exception e) {
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}