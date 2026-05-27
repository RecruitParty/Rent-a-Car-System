import java.sql.Date;

public class RentalRecordDTO {

    private int rentalId;
    private int rentalDest;
    private Integer returnDest;

    private Date rentalDate;
    private Date expectedReturnDate;
    private Date actualReturnDate;

    private String rentalState;

    public RentalRecordDTO() {
    }

    public RentalRecordDTO(
            int rentalId,
            int rentalDest,
            Integer returnDest,
            Date rentalDate,
            Date expectedReturnDate,
            Date actualReturnDate,
            String rentalState
    ) {

        this.rentalId = rentalId;
        this.rentalDest = rentalDest;
        this.returnDest = returnDest;
        this.rentalDate = rentalDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.rentalState = rentalState;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getRentalDest() {
        return rentalDest;
    }

    public void setRentalDest(int rentalDest) {
        this.rentalDest = rentalDest;
    }

    public Integer getReturnDest() {
        return returnDest;
    }

    public void setReturnDest(Integer returnDest) {
        this.returnDest = returnDest;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getRentalState() {
        return rentalState;
    }

    public void setRentalState(String rentalState) {
        this.rentalState = rentalState;
    }
}