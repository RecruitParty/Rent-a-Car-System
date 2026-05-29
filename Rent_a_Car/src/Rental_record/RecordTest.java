package Rental_record;

import java.sql.Connection;
import db.DBConnector;
//이 코드는 main말고 제가 실험해보려고 만든 class로, 추후 삭제 예정입니다.

public class RecordTest {
    public static void main(String[] args) throws Exception {
        Connection conn = DBConnector.getConnection();
        RecordDAO dao = new RentalDAOImpl(conn);
        RentalService service = new RentalService(dao);
        RecordMenuUi ui = new RecordMenuUi(service);
        
        ui.start();
        
        conn.close();
    }
}