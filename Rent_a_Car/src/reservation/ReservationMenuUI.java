package reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import db.DBConnector;

public class ReservationMenuUI {
    private final Scanner sc = new Scanner(System.in);

    public void start() {
        while(true) {
            System.out.println("\n===== 예약 시스템 =====");
            System.out.println("1. 차량 예약");
            System.out.println("2. 예약 취소");
            System.out.println("3. 차량 반납");
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
            	int cusID;

            	try {
            	    System.out.print("고객 ID 입력 : ");
            	    cusID = Integer.parseInt(sc.next());

            	} catch (NumberFormatException e) {
            	    System.out.println("고객 ID는 숫자만 입력 가능합니다.");
            	    continue;
            	}
                
                System.out.print("차량 번호 입력 : ");
                String carNo = sc.next();
                
                int startLocation;
                int endLocation;
                try {
	                System.out.print("대여 위치 입력 : ");
	                startLocation = sc.nextInt();
	                System.out.print("반납 위치 입력 : ");
	                endLocation = sc.nextInt();
            	} catch (NumberFormatException e) {
            	    System.out.println("위치는 숫자만 입력 가능합니다.");
            	    continue;
            	}
                
                Date startDate;
                Date dueDate;
             
                try {
                    System.out.print("대여 날짜 입력(YYYY-MM-DD) : ");
                    startDate = Date.valueOf(sc.next());

                    System.out.print("반납 예정 날짜 입력(YYYY-MM-DD) : ");
                    dueDate = Date.valueOf(sc.next());

                } catch (IllegalArgumentException e) {
                    System.out.println("날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력해주세요.");
                    continue;
                }
                
                ReservationService.reservation(cusID, carNo, startLocation, endLocation, startDate, dueDate);
            }

            else if(menu == 2) {
            	int cusID;
            	try {
            	    System.out.print("고객 ID 입력 : ");
            	    cusID = Integer.parseInt(sc.next());

            	} catch (NumberFormatException e) {
            	    System.out.println("고객 ID는 숫자만 입력 가능합니다.");
            	    continue;
            	}
                
                System.out.print("차량 번호 입력 : ");
                String carNo = sc.next();
                reservationCancelService.reservationCancel(cusID, carNo);
            }

            else if(menu == 3) {
            	int cusID;
            	try {
            	    System.out.print("고객 ID 입력 : ");
            	    cusID = Integer.parseInt(sc.next());

            	} catch (NumberFormatException e) {
            	    System.out.println("고객 ID는 숫자만 입력 가능합니다.");
            	    continue;
            	}
                
                System.out.print("차량 번호 입력 : ");
                String carNo = sc.next();
                
                int returnDest;
                try {
	                System.out.print("반납 위치 입력 : ");
	                returnDest = sc.nextInt();
            	} catch (NumberFormatException e) {
            	    System.out.println("위치는 숫자만 입력 가능합니다.");
            	    continue;
            	}
                Date actualReturnDate;
                try {
                    System.out.print("반납 날짜 입력(YYYY-MM-DD) : ");
                    actualReturnDate = Date.valueOf(sc.next());

                } catch (IllegalArgumentException e) {
                    System.out.println("날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력해주세요.");
                    continue;
                }
                returnCarService.returnCar(cusID, carNo, returnDest, actualReturnDate);
            }

            else if(menu == 0) {
            	System.out.println("시스템 종료");
                break;
            }

            else {
                System.out.println("잘못된 메뉴입니다.");
            }
        }
    }
}