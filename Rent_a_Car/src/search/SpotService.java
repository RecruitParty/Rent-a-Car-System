package search;

import java.util.List;
import model.Car;
import model.Spot;

public class SpotService {
	private final SpotDao spotDAO;
    
    public SpotService(SpotDao spotDAO) {
        this.spotDAO = spotDAO;
    }
    
    // 전체 지점 조회
    public List<Spot> getAllSpots() {
        return spotDAO.getAllSpots();
    }
    
    // 지점 번호로 차량 조회
    public List<Car> getCarsBySpot(int spotNo) {
        return spotDAO.getCarsBySpot(spotNo);
    }
}
