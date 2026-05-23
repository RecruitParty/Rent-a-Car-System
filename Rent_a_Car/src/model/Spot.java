package model;

public class Spot {

    private int spot_no;
    private String spot_name;
    private String spot_location;

    public Spot() {}
    public Spot(int spot_no, String spot_name, String spot_location) {
        this.spot_no = spot_no;
        this.spot_name = spot_name;
        this.spot_location = spot_location;
    }
	public int getSpot_no() {
		return spot_no;
	}
	public void setSpot_no(int spot_no) {
		this.spot_no = spot_no;
	}
	public String getSpot_name() {
		return spot_name;
	}
	public void setSpot_name(String spot_name) {
		this.spot_name = spot_name;
	}
	public String getSpot_location() {
		return spot_location;
	}
	public void setSpot_location(String spot_location) {
		this.spot_location = spot_location;
	}
    
}
