package search;
import java.sql.Date;
import java.util.List;

import model.Car;
import model.Spot;

public class CarService {
	private final CarDao carDAO;
	private final SpotDao spotDAO;
    
    public CarService(CarDao carDAO, SpotDao spotDAO) {
        this.carDAO = carDAO;
        this.spotDAO = spotDAO;
    }
    // 차량 종류로 검색
    public List<Car> searchByType(String carType) {
        return carDAO.searchByType(carType);
    }

    // 존재하는 지점 조회
    public List<Spot> getAllSpots() {
        return spotDAO.getAllSpots();
    }
    // 차량 종류 + 대여 기간으로 대여 가능 여부 확인
    public List<Car> checkRentalAvailability(
            String carType,
            Date rentalDate,
            Date returnDate) {

        // 반납일 검증
        if(returnDate.before(rentalDate)) {
            throw new IllegalArgumentException(
                    "반납일은 대여일보다 빠를 수 없습니다.");
        }

        return carDAO.checkRentalAvailability(carType, rentalDate, returnDate);
    }

}
