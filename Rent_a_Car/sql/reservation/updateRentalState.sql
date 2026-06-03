SET GLOBAL event_scheduler = ON;

DELIMITER //

CREATE PROCEDURE updateRentalState()
BEGIN

    -- 예약 시작일 도달
    UPDATE Rental_record
    SET rental_state = '진행중'
    WHERE rental_state = '예약완료'
      AND rental_date = CURDATE();

    -- 반납 예정일 초과
    UPDATE Rental_record
    SET rental_state = '연체'
    WHERE rental_state = '진행중'
      AND expected_return_date < CURDATE();

END //

DELIMITER ;

CREATE EVENT rentalStateEvent
ON SCHEDULE EVERY 1 HOUR
STARTS TIMESTAMP(CURRENT_DATE + INTERVAL 1 DAY)
DO
CALL updateRentalState();