package reservation;

import java.sql.Connection;
import java.time.LocalDate;

import db.DBConnector;
import model.Rental;
import model.RentalRecord;

public class reservationCancelService {

    private static final CustomerRDAO customerDAO = new CustomerRDAO();
    private static final CarRDAO carDAO = new CarRDAO();
    private static final RentalRDAO rentalDAO = new RentalRDAO();
    private static final RentalRecordRDAO rentalRecordDAO = new RentalRecordRDAO();

    public static boolean reservationCancel(
            int cusID,
            String carNo) {

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

                System.out.println("대여되지 않은 차량입니다.");

                return false;
            }

            Rental rental = rentalDAO.findByUserAndCar(conn, cusID, carNo);

            if (rental == null) {

                conn.rollback();

                System.out.println("취소 가능한 예약이 없습니다.");

                return false;
            }

            RentalRecord rentalRecord = rentalRecordDAO.findByRentalId(conn, rental.getRental_id());

            if (rentalRecord == null) {

                conn.rollback();

                System.out.println("예약 정보를 찾을 수 없습니다.");

                return false;
            }

            LocalDate today = LocalDate.now();

            long dayDiff =
                    rentalRecord.getRental_date().toEpochDay() - today.toEpochDay();

            if (!"예약완료".equals(
                    rentalRecord.getRental_state())
                    || dayDiff < 1) {
                conn.rollback();
                System.out.println("취소 가능한 예약이 없습니다.");

                return false;
            }

            carDAO.updateAvailability(conn, carNo, true);

            rentalRecordDAO.updateState(conn, rental.getRental_id(), "예약취소");

            conn.commit();

            System.out.println("예약이 취소되었습니다.");
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