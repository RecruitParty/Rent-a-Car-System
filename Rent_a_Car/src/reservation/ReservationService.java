package reservation;

import java.sql.Connection;
import java.sql.Date;

import db.DBConnector;
import model.Rental;
import model.RentalRecord;

public class ReservationService {

    private static final CustomerRDAO customerDAO = new CustomerRDAO();
    private static final CarRDAO carDAO = new CarRDAO();
    private static final RentalRDAO rentalDAO = new RentalRDAO();
    private static final RentalRecordRDAO rentalRecordDAO = new RentalRecordRDAO();

    public static boolean reservation(
            int cusID,
            String carNo,
            int startLocation,
            int endLocation,
            Date startDate,
            Date dueDate) {

        Connection conn = null;

        try {

            conn = DBConnector.getConnection();

            conn.setAutoCommit(false);

            if (!customerDAO.existsById(conn, cusID)) {

                System.out.println("등록되지 않은 고객입니다.");

                return false;
            }

            if (!carDAO.isAvailable(conn, carNo)) {

                System.out.println("대여 불가능한 차량입니다.");

                return false;
            }

            carDAO.updateAvailability(conn, carNo, false);
            int rentalId = rentalDAO.getNextRentalId(conn);

            Rental rental = new Rental(rentalId, cusID, carNo);

            RentalRecord record =
                    new RentalRecord(
                            rentalId,
                            startLocation,
                            endLocation,
                            startDate.toLocalDate(),
                            dueDate.toLocalDate(),
                            null,
                            "예약완료");

            rentalDAO.insert(conn, rental);
            rentalRecordDAO.insert(conn, record);
            conn.commit();
            System.out.println("예약 완료");
            return true;
        } catch (Exception e) {

            try {
                conn.rollback();
            } catch (Exception ex) {
            }

            e.printStackTrace();

        } finally {

            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (Exception e) {
            }
        }
		return false;
    }
}