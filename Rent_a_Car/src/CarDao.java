import java.sql.*;
import java.util.*;

public class CarDao {
	//차량 종류 검색
	public List<Car> searchByType(String type) {

	    List<Car> carList = new ArrayList<>();

	    String sql =
	        "SELECT * FROM car WHERE car_type = ?";

	    try(Connection conn = DBConnector.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql)
	    ) {

	        pstmt.setString(1, type);

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
