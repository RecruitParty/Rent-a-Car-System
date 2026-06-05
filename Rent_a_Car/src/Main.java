import java.sql.Connection;
import db.DBConnector;
import search.*;
import ui.MainFrame;
import Rental_record.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection conn = DBConnector.getConnection();

        // Search 관련
        CarDao carDAO = new CarDAOImpl();
        SpotDao spotDAO = new SpotDAOImpl();
        CarService carService = new CarService(carDAO, spotDAO);

        // Rental_record 관련
        RecordDAO recordDAO = new RentalDAOImpl();
        RentalService rentalService = new RentalService(recordDAO);
        
        //spot 관련
        spot_management.SpotMDAO spotMgmtDAO = new spot_management.SpotMDAOImpl();
        spot_management.SpotService spotService = new spot_management.SpotService(spotMgmtDAO);
        
        reservation.updateRentalState.updateRentalState();
        // 실행
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame(carService, null, rentalService, spotService);
        });
        
        conn.close();
    }
}