
public class Car {

    private String car_no;
    private String car_type;
    private boolean rental_availabiliy;
    private int spot_no;
    private int daily_rental_fee;

    public Car() {}
    public Car(String car_no, String car_type, boolean rental_availabiliy,
    		int spot_no, int daily_rental_fee) {

        this.car_no = car_no;
        this.car_type = car_type;
        this.rental_availabiliy = rental_availabiliy;
        this.spot_no = spot_no;
        this.daily_rental_fee = daily_rental_fee;
    }
	public String getCar_no() {
		return car_no;
	}
	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}
	public String getCar_type() {
		return car_type;
	}
	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}
	public boolean isRental_availabiliy() {
		return rental_availabiliy;
	}
	public void setRental_availabiliy(boolean rental_availabiliy) {
		this.rental_availabiliy = rental_availabiliy;
	}
	public int getSpot_no() {
		return spot_no;
	}
	public void setSpot_no(int spot_no) {
		this.spot_no = spot_no;
	}
	public int getDaily_rental_fee() {
		return daily_rental_fee;
	}
	public void setDaily_rental_fee(int daily_rental_fee) {
		this.daily_rental_fee = daily_rental_fee;
	}
    
    
}
    