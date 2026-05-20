DELIMITER //

CREATE PROCEDURE reservationCancel(
    IN cusID INT,
    IN carNo VARCHAR(20)
)
BEGIN
    DECLARE rentState BOOLEAN;
	 DECLARE reserveNo INTEGER;
	 DECLARE rentalState VARCHAR(20);
	 DECLARE rentalDate date;
	 
    SELECT rental_availability
    INTO rentState
    FROM car
    WHERE car_no = carNo;
    
    SELECT rental_id
    INTO reserveNo
    FROM Rental
    WHERE user_id = cusID AND car_no = carNo;
    
    SELECT rental_state
    INTO rentalState
    FROM Rental_record
    WHERE rental_id = reserveNo;
    
    SELECT rental_date
    INTO rentalDate
    FROM Rental_record
    WHERE rental_id = reserveNo;
    
    IF rentState = FALSE AND rentalState = '예약완료' AND CURDATE() <= DATE_SUB(rentalDate, INTERVAL 1 DAY) THEN
    	  UPDATE car
        SET rental_availability = TRUE
        WHERE car_no = carNo;
        
        UPDATE Rental_record
        SET rental_state = '예약취소'
        WHERE rental_id = reserveNo;
			
        SELECT '예약이 취소되었습니다.' AS result;
    ELSE
        SELECT '취소 가능한 예약이 없습니다.' AS result;
    END IF;

END //

DELIMITER ;