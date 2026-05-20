DELIMITER //

CREATE PROCEDURE reservation(
    IN cusID INT,
    IN carNo VARCHAR(20),
    IN startLocation INT,
    IN endLocation INT,
    IN startDate DATE,
    IN dueDate DATE
)
BEGIN
    DECLARE rentState BOOLEAN;
    DECLARE reservationListCount INTEGER;
    DECLARE reservationCount INTEGER;

    SELECT rental_availability
    INTO rentState
    FROM car
    WHERE car_no = carNo;
    
    IF rentState = FALSE THEN
        SELECT '이미 대여중인 차량입니다.' AS result;
    ELSE
        UPDATE car
        SET rental_availability = FALSE
        WHERE car_no = carNo;
        
		  SELECT COUNT(*)+1
        INTO reservationListCount
        FROM Rental_record;
        
        SELECT COUNT(*)+1
        INTO reservationCount
        FROM Rental;
        
		  INSERT INTO Rental_record(rental_id, rental_dest, return_dest, rental_date, expected_rental_date, rental_state)
		  	VALUES(reservationListCount, startLocation, endLocation, startDate, dueDate, '예약확정');
		  	
		  INSERT INTO Rental(rental_id, user_id, car_no)
			VALUES(reservationCount, cusID, carNo);
		  
        SELECT '예약이 완료되었습니다.' AS result;
    END IF;

END //

DELIMITER ;