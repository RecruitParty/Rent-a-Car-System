package reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.RentalRecord;

public class RentalRecordRDAO {

    public void insert(
            Connection conn,
            RentalRecord record)
            throws Exception {

        String sql =
                "INSERT INTO rental_record(" +
                "rental_id," +
                "rental_spot," +
                "return_spot," +
                "rental_date," +
                "expected_return_date," +
                "rental_state" +
                ") VALUES (?,?,?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(
                    1,
                    record.getRental_id());

            pstmt.setInt(
                    2,
                    record.getRental_spot());

            pstmt.setInt(
                    3,
                    record.getReturn_spot());

            pstmt.setDate(
                    4,
                    Date.valueOf(
                            record.getRental_date()));

            pstmt.setDate(
                    5,
                    Date.valueOf(
                            record.getExpected_return_date()));

            pstmt.setString(
                    6,
                    record.getRental_state());

            pstmt.executeUpdate();
        }
    }
    public RentalRecord findByRentalId(
            Connection conn,
            int rentalId) throws Exception {

    	String sql =
    	        "SELECT * " +
    	        "FROM rental_record " +
    	        "WHERE rental_id = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, rentalId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                Date actualDate = rs.getDate("actual_return_date");

                return new RentalRecord(
                        rs.getInt("rental_id"),
                        rs.getInt("rental_spot"),
                        rs.getInt("return_spot"),
                        rs.getDate("rental_date")
                                .toLocalDate(),
                        rs.getDate(
                                "expected_return_date")
                                .toLocalDate(),
                        actualDate == null
                                ? null
                                : actualDate.toLocalDate(),
                        rs.getString(
                                "rental_state")
                );
            }
        }

        return null;
    }
    public void updateReturnInfo(
            Connection conn,
            RentalRecord record)
            throws Exception {

        String sql =
                "UPDATE rental_record " +
                "SET rental_state=?, " +
                "return_spot=?, " +
                "actual_return_date=? " +
                "WHERE rental_id=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    record.getRental_state());

            pstmt.setInt(
                    2,
                    record.getReturn_spot());

            pstmt.setDate(
                    3,
                    Date.valueOf(
                            record.getActual_return_date()));

            pstmt.setInt(
                    4,
                    record.getRental_id());

            pstmt.executeUpdate();
        }
    }
    public void updateState(
            Connection conn,
            int rentalId,
            String state)
            throws Exception {

        String sql =
                "UPDATE rental_record " +
                "SET rental_state = ? " +
                "WHERE rental_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, state);
            pstmt.setInt(2, rentalId);

            pstmt.executeUpdate();
        }
    }
}