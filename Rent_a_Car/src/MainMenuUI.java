import java.util.Scanner;

import search.SearchMenuUI;
import spot_management.SpotMenuUI;
import reservation.ReservationMenuUI;
import Rental_record.RecordMenuUi;

public class MainMenuUI {
    private final Scanner sc = new Scanner(System.in);
    private final SearchMenuUI searchMenuUI;
    private final ReservationMenuUI reservationMenuUI;
    private final RecordMenuUi recordMenuUI;
    private final SpotMenuUI spotMenuUI;

    public MainMenuUI(
        SearchMenuUI searchMenuUI,
        ReservationMenuUI reservationMenuUI,
        RecordMenuUi recordMenuUI,
        SpotMenuUI spotMenuUI
    ) {
        this.searchMenuUI = searchMenuUI;
        this.reservationMenuUI = reservationMenuUI;
        this.recordMenuUI = recordMenuUI;
        this.spotMenuUI = spotMenuUI;
    }

    public void start() throws Exception {
        while(true) {
            System.out.println("\n===== 렌터카 시스템 =====");
            System.out.println("1. 차량 검색");
            System.out.println("2. 예약/취소/반납");
            System.out.println("3. 대여 이력 조회");
            System.out.println("4. 관리자 기능");
            System.out.println("0. 종료");
            System.out.print("메뉴 선택 : ");

            int menu = sc.nextInt();

            if(menu == 1) {
                searchMenuUI.start();
            }
            else if(menu == 2) {
                reservationMenuUI.start();
            }
            else if(menu == 3) {
                recordMenuUI.start();
            }
            else if(menu == 4) {
            	spotMenuUI.start();
            }
            else if(menu == 0) {
                System.out.println("프로그램 종료");
                break;
            }
            else {
                System.out.println("잘못된 메뉴입니다.");
            }
        }
        sc.close();
    }
}