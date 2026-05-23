package Rental_record;

import java.util.List;

public interface RecordDAO {
	List<RecordDTO> getRentalRecord(int user_id) throws Exception;
}
