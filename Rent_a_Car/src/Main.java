import java.sql.Date;
import java.util.Scanner;

import reservation.ReservationService;
import reservation.reservationCancelService;
import reservation.returnCarService;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\n===== 렌터카 프로그램 =====");
            System.out.println("1. 차량 예약");
            System.out.println("2. 예약 취소");
            System.out.println("3. 차량 반납");
            System.out.println("0. 종료");
            System.out.print("메뉴 선택 : ");

            int menu = sc.nextInt();

            if(menu == 1) {

                System.out.print("고객 ID 입력 : ");
                int cusID = sc.nextInt();

                System.out.print("차량 번호 입력 : ");
                String carNo = sc.next();

                System.out.print("대여 위치 입력 : ");
                int startLocation = sc.nextInt();

                System.out.print("반납 위치 입력 : ");
                int endLocation = sc.nextInt();

                System.out.print(
                        "대여 날짜 입력(YYYY-MM-DD) : "
                );

                Date startDate =
                        Date.valueOf(sc.next());

                System.out.print(
                        "반납 예정 날짜 입력(YYYY-MM-DD) : "
                );

                Date dueDate =
                        Date.valueOf(sc.next());

                ReservationService.reservation(
                        cusID,
                        carNo,
                        startLocation,
                        endLocation,
                        startDate,
                        dueDate
                );
            }

            else if(menu == 2) {

                System.out.print("고객 ID 입력 : ");
                int cusID = sc.nextInt();

                System.out.print("차량 번호 입력 : ");
                String carNo = sc.next();

                reservationCancelService.reservationCancel(
                        cusID,
                        carNo
                );
            }

            else if(menu == 3) {

                System.out.print("고객 ID 입력 : ");
                int cusID = sc.nextInt();

                System.out.print("차량 번호 입력 : ");
                String carNo = sc.next();

                System.out.print("반납 위치 입력 : ");
                int returnDest = sc.nextInt();

                System.out.print(
                        "반납 날짜 입력(YYYY-MM-DD) : "
                );

                Date actualReturnDate =
                        Date.valueOf(sc.next());

                returnCarService.returnCar(
                        cusID,
                        carNo,
                        returnDest,
                        actualReturnDate
                );
            }

            else if(menu == 0) {

                System.out.println("프로그램 종료");

                break;
            }

            else {

                System.out.println(
                        "잘못된 메뉴입니다."
                );
            }
        }

        sc.close();
    }
}