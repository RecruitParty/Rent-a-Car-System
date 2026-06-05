package reservation;

import java.sql.CallableStatement;
import java.sql.Connection;
import db.DBConnector;

public class updateRentalState {

    static Connection conn = null;

    public static void updateRentalState() {

        try {

            conn = DBConnector.getConnection();

            System.out.println("DB 연결 성공");
            CallableStatement cstmt = conn.prepareCall("{CALL updateRentalState()}");

            cstmt.execute();

            System.out.println("대여 상태 업데이트 완료");

            cstmt.close();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}