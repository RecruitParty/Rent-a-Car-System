import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import db.DBConnector;

// 지점 관리 기능 - 보유 차량 목록 조회 (이 파일 하나로 실행됩니다)
public class CarView {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // 1) 어떤 지점들이 있는지 먼저 보여주기
        System.out.println("===== 지점 목록 =====");

        String spotSql = "SELECT spot_no, spot_name, spot_location FROM spot";

        try(Connection conn = DBConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(spotSql);
            ResultSet rs = pstmt.executeQuery()
        ) {
            while(rs.next()) {
                System.out.println(
                    rs.getInt("spot_no") + ". " +
                    rs.getString("spot_name") + " (" +
                    rs.getString("spot_location") + ")"
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        // 2) 조회할 지점 번호 입력받기
        System.out.print("\n조회할 지점 번호 입력 : ");
        int spotNo = sc.nextInt();

        // 3) 그 지점이 보유한 차량을 찾아서 출력
        System.out.println("\n===== 보유 차량 목록 =====");

        String carSql = "SELECT * FROM car WHERE spot_no = ? ORDER BY car_no";

        try(Connection conn = DBConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(carSql)
        ) {
            pstmt.setInt(1, spotNo);

            ResultSet rs = pstmt.executeQuery();

            boolean hasCar = false;

            while(rs.next()) {
                hasCar = true;

                String state =
                    rs.getBoolean("rental_availability") ? "대여 가능" : "대여 중";

                System.out.println(
                    "차량번호: " + rs.getString("car_no") +
                    " / 종류: "   + rs.getString("car_type") +
                    " / 상태: "   + state +
                    " / 일당요금: " + rs.getInt("daily_rental_fee") + "원"
                );
            }

            if(!hasCar) {
                System.out.println("해당 지점에 보유 중인 차량이 없습니다.");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
