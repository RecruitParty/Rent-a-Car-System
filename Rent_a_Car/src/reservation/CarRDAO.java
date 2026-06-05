package reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarRDAO {

    public boolean isAvailable(
            Connection conn,
            String carNo) throws Exception {

        String sql =
                "SELECT rental_availability " +
                "FROM car " +
                "WHERE car_no = ? " +
                "FOR UPDATE";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, carNo);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            return rs.getBoolean("rental_availability");
        }
    }

    public void updateAvailability(
            Connection conn,
            String carNo,
            boolean state)
            throws Exception {

        String sql =
                "UPDATE car " +
                "SET rental_availability = ? " +
                "WHERE car_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, state);
            pstmt.setString(2, carNo);

            pstmt.executeUpdate();
        }
    }

    public int getDailyFee(
            Connection conn,
            String carNo)
            throws Exception {

        String sql =
                "SELECT daily_rental_fee " +
                "FROM car " +
                "WHERE car_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, carNo);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("daily_rental_fee");
            }

            return 0;
        }
    }
    public void returnCar(
            Connection conn,
            String carNo,
            int spotNo)
            throws Exception {

        String sql =
                "UPDATE car " +
                "SET rental_availability = TRUE," +
                "spot_no = ? " +
                "WHERE car_no = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, spotNo);
            pstmt.setString(2, carNo);

            pstmt.executeUpdate();
        }
    }
}