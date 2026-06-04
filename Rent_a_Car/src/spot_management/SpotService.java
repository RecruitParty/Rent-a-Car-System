package spot_management;

import java.util.List;
import model.Car;
import model.Spot;

public class SpotService {
    private final SpotMDAO dao;

    public SpotService(SpotMDAO dao) {
        this.dao = dao;
    }

    // 전체 지점 조회
    public List<Spot> getAllSpots() {
        return dao.getAllSpots();
    }

    // 지점별 차량 조회
    public List<Car> getCarsBySpot(int spotNo) {
        return dao.getCarsBySpot(spotNo);
    }
}