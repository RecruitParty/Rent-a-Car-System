package search;

import java.util.List;

import model.Car;
import model.Spot;

public interface SpotDao {
	// 존재하는 지점 조회
    List<Spot> getAllSpots();
    List<Car> getCarsBySpot(int spotNo);
}