DELIMITER //

CREATE PROCEDURE reservationQuit(
    IN cusID INT,
    IN carNo VARCHAR(20)
)
BEGIN
    DECLARE rentState BOOLEAN;
	 DECLARE reserveNo INTEGER;
	 
    SELECT rental_availability
    INTO rentState
    FROM car
    WHERE car_no = carNo;
    
    SELECT rental_no
    INTO reserveNo
    FROM Rental
    WHERE user_id = cusID AND car_no = carNo;
    
    IF rentState = FALSE THEN
    	  UPDATE car
        SET rental_availability = TRUE
        WHERE car_no = carNo;
        
        UPDATE Rental_record
        SET rental_state = '예약취소'
        WHERE car_no = carNo;
			
        SELECT '취소가 완료되었습니다.' AS result;
    ELSE
        SELECT '대여하지 않은 차량입니다.' AS result;
    END IF;

END //

DELIMITER ;