package search;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import model.Car;
import model.Spot;

public class SearchMenuUI {
    private final CarService service;
    private final Scanner sc = new Scanner(System.in);

    public SearchMenuUI(CarService service) {
        this.service = service;
    }

    public void start() {
        while(true) {
            System.out.println("\n===== Car Search system =====");
            System.out.println("1. Search car type");
            System.out.println("2. 존재하는 지점 조회");
            System.out.println("3. 대여 가능 여부 확인");
            System.out.println("0. 뒤로가기");
            System.out.print("메뉴 선택 : ");
            
            int menu;
            try {
            	menu = sc.nextInt();
            }
            catch (NumberFormatException e) {
        	    System.out.println("메뉴는 숫자만 입력 가능합니다.");
        	    continue;
            }

            if(menu == 1) {
                System.out.print("차량 종류 입력 : ");
                String carType = sc.next();
                List<Car> carList = service.searchByType(carType);
                System.out.println("\n=== 검색 결과 ===");
                for(Car car : carList) {
                    System.out.println("차량번호 : " + car.getCar_no());
                    System.out.println("차량종류 : " + car.getCar_type());
                    System.out.println("지점번호 : " + car.getSpot_no());
                    System.out.println("일일요금 : " + car.getDaily_rental_fee());
                    System.out.println();
                }
            }

            else if(menu == 2) {
                List<Spot> spotList = service.getAllSpots();
                System.out.println("\n=== 지점 목록 ===");
                for(Spot spot : spotList) {
                    System.out.println("지점번호 : " + spot.getSpot_no());
                    System.out.println("지점명 : " + spot.getSpot_name());
                    System.out.println("지점위치 : " + spot.getSpot_location());
                    System.out.println();
                }
            }

            else if(menu == 3) {
                System.out.print("차량 종류 입력 : ");
                String carType = sc.next();
                Date rentalDate;
                Date returnDate;
                try {
	                System.out.print("대여 날짜 입력(YYYY-MM-DD) : ");
	                rentalDate = Date.valueOf(sc.next());
	                System.out.print("반납 날짜 입력(YYYY-MM-DD) : ");
	                returnDate = Date.valueOf(sc.next());
                }
                catch (IllegalArgumentException e) {
                    System.out.println("날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력해주세요.");
                    continue;
                }
                List<Car> availableCars = service.checkRentalAvailability(carType, rentalDate, returnDate);
                System.out.println("\n=== 예약 가능 차량 ===");
                if(availableCars.isEmpty()) {
                    System.out.println("예약 가능한 차량이 없습니다.");
                } else {
                    for(Car car : availableCars) {
                        System.out.println("차량번호 : " + car.getCar_no());
                        System.out.println("차량종류 : " + car.getCar_type());
                        System.out.println("지점번호 : " + car.getSpot_no());
                        System.out.println("일일요금 : " + car.getDaily_rental_fee());
                        System.out.println();
                    }
                }
            }

            else if(menu == 0) {
                break;
            }

            else {
                System.out.println("잘못된 메뉴입니다.");
            }
        }
    }
}