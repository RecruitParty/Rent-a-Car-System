package spot_management;

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
            System.out.println("1. 지점별 차량 조회");
            System.out.println("0. 뒤로가기");
            System.out.print("메뉴 선택 : ");

            int menu = sc.nextInt();

            if(menu == 1) {
                // 지점 목록 먼저 보여주기
                List<Spot> spotList = service.getAllSpots();
                System.out.println("\n===== 지점 목록 =====");
                for(Spot spot : spotList) {
                    System.out.println(
                        spot.getSpot_no() + ". " +
                        spot.getSpot_name() + " (" +
                        spot.getSpot_location() + ")"
                    );
                }

                // 지점 번호 입력받기
                System.out.print("\n조회할 지점 번호 입력 : ");
                int spotNo = sc.nextInt();

                // 해당 지점 차량 출력
                List<Car> carList = service.getCarsBySpot(spotNo);
                System.out.println("\n===== 보유 차량 목록 =====");
                if(carList.isEmpty()) {
                    System.out.println("해당 지점에 보유 중인 차량이 없습니다.");
                } else {
                    for(Car car : carList) {
                        String state = car.isRental_availabiliy() ? "대여 가능" : "대여 중";
                        System.out.println(
                            "차량번호: " + car.getCar_no() +
                            " / 종류: " + car.getCar_type() +
                            " / 상태: " + state +
                            " / 일당요금: " + car.getDaily_rental_fee() + "원"
                        );
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