package search;

import java.sql.Date;
import java.util.List;

import model.Car;

public interface CarDao {

    // 차량 종류 검색
    List<Car> searchByType(String carType);

    // 차량 종류 + 대여 기간으로 대여 가능 여부 확인
    List<Car> checkRentalAvailability(
            String carType,
            Date rentalDate,
            Date returnDate);
}