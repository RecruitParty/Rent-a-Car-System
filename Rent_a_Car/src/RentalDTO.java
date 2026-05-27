public class RentalDTO {

    private int rentalId;
    private int userId;
    private String carNo;

    public RentalDTO() {
    }

    public RentalDTO(
            int rentalId,
            int userId,
            String carNo
    ) {

        this.rentalId = rentalId;
        this.userId = userId;
        this.carNo = carNo;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}