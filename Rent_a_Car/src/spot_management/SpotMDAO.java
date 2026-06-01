package spot_management;

import java.util.List;
import model.Car;
import model.Spot;

public interface SpotMDAO {
    List<Spot> getAllSpots();           // 전체 지점 조회
    List<Car> getCarsBySpot(int spotNo); // 지점별 차량 조회
}