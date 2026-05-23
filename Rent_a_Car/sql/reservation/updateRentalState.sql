SET GLOBAL event_scheduler = ON;

DELIMITER //

CREATE PROCEDURE updateRentalState()
BEGIN

    UPDATE Rental_record
    SET rental_state = '대여중'
    WHERE rental_state = '예약완료' AND rental_date = CURDATE();

    UPDATE Rental_record
    SET rental_state = '연체'
    WHERE rental_state = '대여중' AND due_date < CURDATE();

    UPDATE car c
    JOIN Rental r
      ON c.car_no = r.car_no
    JOIN rental_record rr
    	ON r.rental_id = rr.rental_id
    SET c.rental_availability = TRUE
    WHERE rr.rental_state IN ('반납완료', '반납완료(연체됨)') AND CURDATE() > DATE_ADD(rr.actual_return_date, INTERVAL 2 DAY);

END //

DELIMITER ;

CREATE EVENT rentalStateEvent
ON SCHEDULE EVERY 1 DAY
STARTS TIMESTAMP(CURRENT_DATE + INTERVAL 1 DAY)
DO
CALL updateRentalState();