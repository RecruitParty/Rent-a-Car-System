package Rental_record;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalService {
	private final RecordDAO dao;
	
	public RentalService(RecordDAO dao) {
		this.dao = dao;
	}
	
	public List<RecordDTO> getRentalRecord(int userId) throws Exception {
		List<RecordDTO> list = dao.getRentalRecord(userId);
	   
	    for (RecordDTO dto : list) {
	        
	        LocalDate endDate = (dto.getActual_return_date() != null)
	                            ? dto.getActual_return_date()
	                            : dto.getExpected_return_date();
	        
	        long days = ChronoUnit.DAYS.between(dto.getRental_date(), endDate);
	        
	        dto.setTotal_fee((int) days * dto.getDaily_rental_fee());
	    }
	    
	    return list;
	}

}
