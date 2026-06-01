package search;

import java.util.List;
import java.util.Scanner;
import model.Car;
import model.Spot;

public class SpotMenuUI {
    private final SpotService service;
    private final Scanner sc = new Scanner(System.in);

    public SpotMenuUI(SpotService service) {
        this.service = service;
    }

    public void start() {
        while(true) {
            System.out.println("\n===== 지점 관리 =====");
            System.out.println("1. 전체 지점 조회");
            System.out.println("2. 지점별 차량 조회");
            System.out.println("0. 뒤로가기");
            System.out.print("메뉴 선택 : ");

            int menu = sc.nextInt();

            if(menu == 1) {
                List<Spot> spotList = service.getAllSpots();
                System.out.println("\n=== 지점 목록 ===");
                if(spotList.isEmpty()) {
                    System.out.println("지점이 없습니다.");
                } else {
                    for(Spot spot : spotList) {
                        System.out.println("지점번호 : " + spot.getSpot_no());
                        System.out.println("지점명 : " + spot.getSpot_name());
                        System.out.println("지점위치 : " + spot.getSpot_location());
                        System.out.println();
                    }
                }
            }

            else if(menu == 2) {
                System.out.print("지점 번호 입력 : ");
                int spotNo = sc.nextInt();
                List<Car> carList = service.getCarsBySpot(spotNo);
                System.out.println("\n=== 지점 차량 목록 ===");
                if(carList.isEmpty()) {
                    System.out.println("해당 지점에 차량이 없습니다.");
                } else {
                    for(Car car : carList) {
                        System.out.println("차량번호 : " + car.getCar_no());
                        System.out.println("차량종류 : " + car.getCar_type());
                        System.out.println("대여가능여부 : " + car.isRental_availabiliy());
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