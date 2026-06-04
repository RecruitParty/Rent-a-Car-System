import java.sql.Connection;
import db.DBConnector;
import search.*;
import reservation.ReservationMenuUI;
import Rental_record.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection conn = DBConnector.getConnection();

        // Search 관련
        CarDao carDAO = new CarDAOImpl(conn);
        SpotDao spotDAO = new SpotDAOImpl(conn);
        CarService carService = new CarService(carDAO, spotDAO);

        // Rental_record 관련
        RecordDAO recordDAO = new RentalDAOImpl(conn);
        RentalService rentalService = new RentalService(recordDAO);
        
        //spot 관련
        spot_management.SpotMDAO spotMgmtDAO = new spot_management.SpotMDAOImpl(conn);
        spot_management.SpotService spotService = new spot_management.SpotService(spotMgmtDAO);
        spot_management.SpotMenuUI spotMenuUI = new spot_management.SpotMenuUI(spotService);


        // MenuUI 생성
        SearchMenuUI searchMenuUI = new SearchMenuUI(carService);
        ReservationMenuUI reservationMenuUI = new ReservationMenuUI();
        RecordMenuUi recordMenuUI = new RecordMenuUi(rentalService);
        
        // 실행
        MainMenuUI mainMenuUI = new MainMenuUI(
            searchMenuUI,
            reservationMenuUI,
            recordMenuUI,
            spotMenuUI
        );
        
        mainMenuUI.start();
        
        conn.close();
    }
}