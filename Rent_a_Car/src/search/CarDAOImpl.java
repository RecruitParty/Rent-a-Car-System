package search;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.DBConnector;
import model.Car;

public class CarDAOImpl implements CarDao {
	
	//차량 종류 검색
	public List<Car> searchByType(String type) {

	    List<Car> carList = new ArrayList<>();

	    String sql =
	        "SELECT * FROM car WHERE car_type = ?";
	    
	    try(Connection conn = DBConnector.getConnection();
	    		PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
		
		// 차량 종류 + 대여 기간으로 대여 가능 여부 확인
	public List<Car> checkRentalAvailability(
	        String carType,
	        Date rentalDate,
	        Date returnDate) {

	    List<Car> carList = new ArrayList<>();

	    String sql =
	    	    "SELECT * " +
	    	    "FROM car c " +
	    	    "WHERE c.car_type = ? " +
	    	    "AND c.car_no NOT IN ( " +
	    	    "   SELECT r.car_no " +
	    	    "   FROM rental r " +
	    	    "   JOIN rental_record rr " +
	    	    "   ON r.rental_id = rr.rental_id " +
	    	    "   WHERE rr.rental_state IN ('진행중', '완료') " +
	    	    "   AND rr.rental_date <= ? " +
	    	    "   AND rr.expected_return_date >= ? " +
	    	    ")";

	    try(Connection conn = DBConnector.getConnection();
	    		PreparedStatement pstmt =conn.prepareStatement(sql)) {
	        // 차량 종류
	        pstmt.setString(1, carType);
	        // 반납예정일
	        pstmt.setDate(2, returnDate);
	        // 대여시작일
	        pstmt.setDate(3, rentalDate);
	        ResultSet rs = pstmt.executeQuery();
	        while(rs.next()) {
	            Car car = new Car();
	            car.setCar_no(
	                rs.getString("car_no"));
	            car.setCar_type(
	                rs.getString("car_type"));
	            car.setRental_availabiliy(
	                rs.getBoolean(
	                    "rental_availability"));
	            car.setSpot_no(
	                rs.getInt("spot_no"));
	            car.setDaily_rental_fee(
	                rs.getInt("daily_rental_fee"));
	            carList.add(car);
	        }

	    } catch(Exception e) {
	        e.printStackTrace();
	    }

	    return carList;
	}
}
