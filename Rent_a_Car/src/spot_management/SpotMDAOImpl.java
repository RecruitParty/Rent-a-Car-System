package spot_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.DBConnector;
import model.Car;
import model.Spot;

public class SpotMDAOImpl implements SpotMDAO {

    // 전체 지점 조회
    public List<Spot> getAllSpots() {
        List<Spot> spotList = new ArrayList<>();
        String sql = "SELECT spot_no, spot_name, spot_location FROM spot";

        try(Connection conn = DBConnector.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement(sql);
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

    // 지점별 차량 조회
    public List<Car> getCarsBySpot(int spotNo) {
        List<Car> carList = new ArrayList<>();
        String sql = "SELECT * FROM car WHERE spot_no = ? ORDER BY car_no";

        try(Connection conn = DBConnector.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
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