package search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Car;
import model.Spot;

public class SpotDAOImpl implements SpotDao{
	
	private final Connection conn;
	
	public SpotDAOImpl(Connection conn) {
		this.conn = conn;
	}
	// 존재하는 지점 조회
	public List<Spot> getAllSpots() {

	    List<Spot> spotList = new ArrayList<>();

	    String sql = "SELECT * FROM spot";

	    try(PreparedStatement pstmt =conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery()) {
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
	
	
	@Override
	public List<Car> getCarsBySpot(int spotNo) {
		List<Car> carList = new ArrayList<>();
    
    String sql = "SELECT * FROM car WHERE spot_no = ?";
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, spotNo);
        ResultSet rs = pstmt.executeQuery();
        
        while(rs.next()) {
            Car car = new Car();
            car.setCar_no(rs.getString("car_no"));
            car.setCar_type(rs.getString("car_type"));
            car.setRental_availabiliy(rs.getBoolean("rental_availability"));
            car.setSpot_no(rs.getInt("spot_no"));
            car.setDaily_rental_fee(rs.getInt("daily_rental_fee"));
            carList.add(car);
        }
        
    } catch(Exception e) {
        e.printStackTrace();
    }
    
    return carList;
	}


}
