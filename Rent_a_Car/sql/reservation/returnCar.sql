DELIMITER //

CREATE PROCEDURE returncar(
    IN cusID INT,
    IN carNo VARCHAR(20),
    IN returnDest INT,
    IN actualReturnDate DATE
)

BEGIN
    DECLARE rentState BOOLEAN;
    DECLARE reserveNo INT;
    DECLARE dayPrice INT;
    DECLARE startDate DATE;
    DECLARE totalPrice INT;
    DECLARE rentalState VARCHAR(30);

    SELECT rental_availability
    INTO rentState
    FROM car
    WHERE car_no = carNo;

    SELECT r.rental_id
    INTO reserveNo
    FROM Rental r
    JOIN Rental_record rr
    ON r.rental_id = rr.rental_id
    WHERE r.user_id = cusID AND r.car_no = carNo AND rr.rental_state IN ('진행중', '연체');

    SELECT rental_date
    INTO startDate
    FROM Rental_record
    WHERE rental_id = reserveNo;
        
    SELECT rental_state
    INTO rentalState
    FROM Rental_record
    WHERE rental_id = reserveNo;

    IF rentState = FALSE THEN
		  IF rentalState = '연체' THEN
		  	UPDATE Rental_record
        	SET rental_state = '완료(연체됨)', return_spot = returnDest, actual_return_date = actualReturnDate
        	WHERE rental_id = reserveNo;
        ELSE 
      	UPDATE Rental_record
        	SET rental_state = '완료', return_spot = returnDest, actual_return_date = actualReturnDate
        	WHERE rental_id = reserveNo;
        END IF;

        SELECT daily_rental_fee
        INTO dayPrice
        FROM car
        WHERE car_no = carNo;

        SET totalPrice = dayPrice * DATEDIFF(actualReturnDate, startDate);

        SELECT 
            '반납이 완료되었습니다.' AS result,
            totalPrice AS total_price;

    ELSE
        SELECT '대여하지 않은 차량입니다.' AS result;
    END IF;

END //

DELIMITER ;