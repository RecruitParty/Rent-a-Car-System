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

    SELECT rental_availability
    INTO rentState
    FROM car
    WHERE car_no = carNo;

    SELECT rental_id
    INTO reserveNo
    FROM Rental_record
    WHERE user_id = cusID AND car_no = carNo AND rental_state = '대여중';

    IF rentState = FALSE THEN
		  IF rental_state = '연체' THEN
		  	UPDATE Rental_record
        	SET rental_state = '반납완료(연체됨)', return_dest = returnDest, actual_return_date = actualReturnDate
        	WHERE rental_id = reserveNo;
        ELSE 
      	UPDATE Rental_record
        	SET rental_state = '반납완료', return_dest = returnDest, actual_return_date = actualReturnDate
        	WHERE rental_id = reserveNo;
        END IF;

        SELECT daily_rental_fee
        INTO dayPrice
        FROM car
        WHERE car_no = carNo;

        SELECT rental_date
        INTO startDate
        FROM Rental_record
        WHERE rental_id = reserveNo;

        SET totalPrice = dayPrice * DATEDIFF(actualReturnDate, startDate);

        SELECT 
            '반납이 완료되었습니다.' AS result,
            totalPrice AS total_price;

    ELSE
        SELECT '대여하지 않은 차량입니다.' AS result;
    END IF;

END //

DELIMITER ;