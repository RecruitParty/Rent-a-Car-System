USE rental_car;

SELECT 	r.rental_id, c.car_no, 
			rr.rental_spot, rr.return_spot, 
			rr.rental_date, rr.expected_return_date, rr.actual_return_date, 
			rr.rental_state
FROM Rental r
	JOIN Rental_record rr ON r.rental_id = rr.rental_id
	JOIN Car c ON r.car_no = c.car_no
WHERE user_id = 1002;