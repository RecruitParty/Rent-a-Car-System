package reservation;

import java.sql.Connection;
import java.sql.Date;

import db.DBConnector;
import model.Rental;
import model.RentalRecord;

public class returnCarService {

    private static final CustomerRDAO customerDAO = new CustomerRDAO();
    private static final CarRDAO carDAO = new CarRDAO();
    private static final RentalRDAO rentalDAO = new RentalRDAO();
    private static final RentalRecordRDAO rentalRecordDAO = new RentalRecordRDAO();

    public static boolean returnCar(
            int cusID,
            String carNo,
            int returnDest,
            Date actualReturnDate) {

        Connection conn = null;

        try {

            conn = DBConnector.getConnection();

            conn.setAutoCommit(false);

            if (!customerDAO.existsById(conn, cusID)) {

                conn.rollback();

                System.out.println("등록되지 않은 고객입니다.");

                return false;
            }

            if (carDAO.isAvailable(conn, carNo)) {

                conn.rollback();

                System.out.println("대여중인 차량이 아닙니다.");

                return false;
            }

            Rental rental =
                    rentalDAO.findActiveRental(conn, cusID, carNo);

            if (rental == null) {

                conn.rollback();

                System.out.println("반납 가능한 대여 내역이 없습니다.");

                return false;
            }

            RentalRecord rentalRecord = rentalRecordDAO.findByRentalId(conn, rental.getRental_id());

            if (rentalRecord == null) {

                conn.rollback();

                System.out.println("대여 정보를 찾을 수 없습니다.");

                return false;
            }

            if ("연체".equals(
                    rentalRecord.getRental_state())) {

                rentalRecord.setRental_state("완료(연체됨)");

            } else {

                rentalRecord.setRental_state("완료");
            }

            rentalRecord.setReturn_spot(returnDest);

            rentalRecord.setActual_return_date(actualReturnDate.toLocalDate());

            rentalRecordDAO.updateReturnInfo(conn, rentalRecord);

            carDAO.returnCar(conn, carNo, returnDest);

            int dailyFee = carDAO.getDailyFee(conn, carNo);

            long rentalDays = actualReturnDate.toLocalDate().toEpochDay() - rentalRecord.getRental_date().toEpochDay();

            int totalPrice = (int) rentalDays * dailyFee;

            conn.commit();

            System.out.println("반납이 완료되었습니다.");
            System.out.println("총 금액 : " + totalPrice);
            return true;

        } catch (Exception e) {

            try {

                if (conn != null) {

                    conn.rollback();
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
		return false;
    }
}