public class CarDTO {

    private String carNo;
    private String carType;
    private boolean rentalAvailability;
    private int spotNo;
    private int dailyRentalFee;

    public CarDTO() {
    }

    public CarDTO(
            String carNo,
            String carType,
            boolean rentalAvailability,
            int spotNo,
            int dailyRentalFee
    ) {

        this.carNo = carNo;
        this.carType = carType;
        this.rentalAvailability = rentalAvailability;
        this.spotNo = spotNo;
        this.dailyRentalFee = dailyRentalFee;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public boolean isRentalAvailability() {
        return rentalAvailability;
    }

    public void setRentalAvailability(boolean rentalAvailability) {
        this.rentalAvailability = rentalAvailability;
    }

    public int getSpotNo() {
        return spotNo;
    }

    public void setSpotNo(int spotNo) {
        this.spotNo = spotNo;
    }

    public int getDailyRentalFee() {
        return dailyRentalFee;
    }

    public void setDailyRentalFee(int dailyRentalFee) {
        this.dailyRentalFee = dailyRentalFee;
    }
}