package Rental_record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import db.DBConnector;

public class RentalDAOImpl implements RecordDAO{

	@Override
	public List<RecordDTO> getRentalRecord(int user_id) throws Exception {
		List<RecordDTO> personalRecord = new ArrayList<>();
		
		String sql = "SELECT 	r.rental_id, c.car_no," +
				     "rr.rental_spot, rr.return_spot, \r\n" +
				     "rr.rental_date, rr.expected_return_date, rr.actual_return_date, \r\n" +
				     "rr.rental_state\r\n" +
				     "FROM Rental r\r\n" +
				     "JOIN Rental_record rr ON r.rental_id = rr.rental_id\r\n" +
				     "JOIN Car c ON r.car_no = c.car_no\r\n" +
				     "WHERE user_id = ?;";
		
		Connection conn = DBConnector.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, user_id);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			personalRecord.add(new RecordDTO(rs.getInt("rental_id"), rs.getString("car_no"), 
					rs.getInt("rental_spot"), rs.getInt("return_spot"), 
					rs.getDate("rental_date").toLocalDate(), rs.getDate("expected_return_date").toLocalDate(), 
					rs.getDate("actual_return_date").toLocalDate(), rs.getString("rental_state")));
		}
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return personalRecord;
	}

}
