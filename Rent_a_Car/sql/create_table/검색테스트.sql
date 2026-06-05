-- rental_availibility 초기화
UPDATE car c
SET c.rental_availability=0
WHERE c.car_no IN (SELECT r.car_no
FROM rental r JOIN rental_record rr ON r.rental_id=rr.rental_id
WHERE rr.rental_state IN ('연체', '진행중'));

-- 쿼리 참고
SELECT *
FROM car c
WHERE c.car_type = 'SUV'
AND c.car_no NOT IN (
    SELECT r.car_no
    FROM rental r
    JOIN rental_record rr
    ON rcar.rental_id = rr.rental_id
    WHERE rr.rental_state = '연체'
)
AND c.car_no NOT IN (
    SELECT r.car_nocar
    FROM rental r
    JOIN rental_record rr
    ON r.rental_id = rr.rental_id
    WHERE rr.rental_state IN ('진행중', '완료', '완료(연체됨)')
    AND rr.rental_date <= '2026-05-30'
    AND rr.expected_return_date >= '2026-05-25'
);

