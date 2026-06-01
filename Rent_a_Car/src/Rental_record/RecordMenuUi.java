package Rental_record;

import java.util.List;
import java.util.Scanner;

public class RecordMenuUi {
	private final RentalService service;
    private final Scanner sc = new Scanner(System.in);
    
    public RecordMenuUi(RentalService service) {
        this.service = service;
    }
    
    public void start() throws Exception {
    	System.out.print("Enter User ID: ");
        int userId = sc.nextInt();
        
        List<RecordDTO> list = service.getRentalRecord(userId);
        
        if (list.isEmpty()) {
            System.out.println("There is no Rental Record.");
            return;
        }
        
        System.out.println("\nRental Record");
        for (RecordDTO dto : list) {
            System.out.print("Rental ID: " + dto.getRental_id());
            System.out.print(", Car number: " + dto.getCar_no());
            System.out.print(", Rental date: " + dto.getRental_date());
            System.out.print(", Expected Return date: " + dto.getExpected_return_date());
            System.out.print(", Actual Return date: " + dto.getActual_return_date());
            System.out.print(", Rental state: " + dto.getRental_state());
            System.out.println(", total fee: " + dto.getTotal_fee());
        }
    }

}
