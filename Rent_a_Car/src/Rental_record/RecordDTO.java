package Rental_record;

import java.time.LocalDate;

public class RecordDTO {
	private int rental_id;
	private String car_no;
	private int rental_spot;
	private int return_spot;
	private LocalDate rental_date;
	private LocalDate expected_return_date;
	private LocalDate actual_return_date;
	private String rental_state;
	private int daily_rental_fee;
	private int total_fee;
	
	public RecordDTO() {}
	
	public RecordDTO(int rental_id, String car_no, int rental_spot, int return_spot, 
			LocalDate rental_date, LocalDate expected_return_date, LocalDate actual_return_date, 
			String rental_state, int daily_rental_fee) {
		this.rental_id = rental_id;
		this.car_no = car_no;
		this.rental_spot = rental_spot;
		this.return_spot = return_spot;
		this.rental_date = rental_date;
		this.expected_return_date = expected_return_date;
		this.actual_return_date = actual_return_date;
		this.rental_state = rental_state;
		this.daily_rental_fee = daily_rental_fee;
	}
	
	public int getRental_id() {
		return rental_id;
	}
	public void setRental_id(int rental_id) {
		this.rental_id = rental_id;
	}
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	public int getRental_spot() {
		return rental_spot;
	}
	public void setRental_spot(int rental_spot) {
		this.rental_spot = rental_spot;
	}
	public int getReturn_spot() {
		return return_spot;
	}
	public void setReturn_spot(int return_spot) {
		this.return_spot = return_spot;
	}
	public LocalDate getRental_date() {
		return rental_date;
	}
	public void setRental_date(LocalDate rental_date) {
		this.rental_date = rental_date;
	}
	public LocalDate getExpected_return_date() {
		return expected_return_date;
	}
	public void setExpected_return_date(LocalDate expected_return_date) {
		this.expected_return_date = expected_return_date;
	}
	public LocalDate getActual_return_date() {
		return actual_return_date;
	}
	public void setActual_return_date(LocalDate actual_return_date) {
		this.actual_return_date = actual_return_date;
	}
	public String getRental_state() {
		return rental_state;
	}
	public void setRental_state(String rental_state) {
		this.rental_state = rental_state;
	}
	public int getDaily_rental_fee() {
		return daily_rental_fee;
	}
	public void setDaily_rental_fee(int daily_rental_fee) {
		this.daily_rental_fee = daily_rental_fee;
	}
	public int getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}
	
	

}
