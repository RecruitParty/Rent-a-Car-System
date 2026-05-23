package model;

public class Rental {
	private int rental_id;
	private int user_id;
	private String car_no;
	
	public Rental() {}
	public Rental(int rental_id, int user_id, String car_no) {
		this.rental_id = rental_id;
		this.user_id = user_id;
		this.car_no = car_no;
	}
	
	public int getRental_id() {
		return rental_id;
	}
	public void setRental_id(int rental_id) {
		this.rental_id = rental_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	
	
}
