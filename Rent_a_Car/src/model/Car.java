package model;

public class Car {
	private String car_no;
	private int daily_rental_fee;
	
	public Car() {}
	
	public Car(String car_no, int daily_rental_fee) {
		this.car_no = car_no;
		this.daily_rental_fee = daily_rental_fee;
	}
	
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	public int getDaily_rental_fee() {
		return daily_rental_fee;
	}
	public void setDaily_rental_fee(int daily_rental_fee) {
		this.daily_rental_fee = daily_rental_fee;
	}
	
	

}
