package reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Rental;

public class RentalRDAO {

    public int getNextRentalId(
            Connection conn)
            throws Exception {

        String sql =
                "SELECT COALESCE(MAX(rental_id),0)+1 AS next_id " +
                "FROM rental";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);

             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("next_id");
            }
        }

        return 1;
    }

    public void insert(
            Connection conn,
            Rental rental)
            throws Exception {

        String sql =
                "INSERT INTO rental(" +
                "rental_id,user_id,car_no" +
                ") VALUES (?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(
                    1,
                    rental.getRental_id());

            pstmt.setInt(
                    2,
                    rental.getUser_id());

            pstmt.setString(
                    3,
                    rental.getCar_no());

            pstmt.executeUpdate();
        }
    }
    public Rental findActiveRental(
            Connection conn,
            int userId,
            String carNo) throws Exception {

        String sql =
                "SELECT r.* " +
                "FROM rental r " +
                "JOIN rental_record rr " +
                "ON r.rental_id = rr.rental_id " +
                "WHERE r.user_id = ? " +
                "AND r.car_no = ? " +
                "AND rr.rental_state IN ('진행중','연체')";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, carNo);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                return new Rental(
                        rs.getInt("rental_id"),
                        rs.getInt("user_id"),
                        rs.getString("car_no"));
            }
        }

        return null;
    }
    public Rental findByUserAndCar(
            Connection conn,
            int userId,
            String carNo)
            throws Exception {

    	String sql =
    	        "SELECT r.* " +
    	        "FROM rental r " +
    	        "JOIN rental_record rr " +
    	        "ON r.rental_id = rr.rental_id " +
    	        "WHERE r.user_id = ? " +
    	        "AND r.car_no = ? " +
    	        "AND rr.rental_state = '예약완료'";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, carNo);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                return new Rental(
                        rs.getInt("rental_id"),
                        rs.getInt("user_id"),
                        rs.getString("car_no"));
            }
        }

        return null;
    }
}