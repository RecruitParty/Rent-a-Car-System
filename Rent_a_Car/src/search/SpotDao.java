package search;

import java.sql.*;
import java.util.*;
import db.DBConnector;
import model.Spot;

public class SpotDao {
	//존재하는 지점 조회
	public List<Spot> getAllSpots() {

	    List<Spot> spotList = new ArrayList<>();

	    String sql = "SELECT * FROM spot";

	    try(
	        Connection conn =
	            DBConnector.getConnection();

	        PreparedStatement pstmt =
	            conn.prepareStatement(sql);

	        ResultSet rs = pstmt.executeQuery()
	    ) {

	        while(rs.next()) {

	            Spot spot = new Spot();

	            spot.setSpot_no(rs.getInt("spot_no"));

	            spot.setSpot_name(rs.getString("spot_name"));
	            
	            spot.setSpot_location(rs.getString("spot_location"));

	            spotList.add(spot);
	        }

	    } catch(Exception e) {
	        e.printStackTrace();
	    }

	    return spotList;
	}

}
