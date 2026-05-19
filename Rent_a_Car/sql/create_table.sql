USE rental_car;


CREATE TABLE manager(
	manager_id INTEGER PRIMARY KEY,
	manager_name VARCHAR(50) NOT NULL,
	manager_phno VARCHAR(20) NOT NULL UNIQUE
);
	
CREATE TABLE spot (
    spot_no INTEGER PRIMARY KEY,
    spot_name VARCHAR(50) NOT NULL UNIQUE,
    spot_location VARCHAR(100) NOT NULL
);

CREATE TABLE car (
    car_no VARCHAR(20) PRIMARY KEY,
    car_type VARCHAR(30) NOT NULL,
    rental_availability BOOLEAN NOT NULL,
    spot_no INTEGER,
    daily_rental_fee INTEGER NOT NULL,

    FOREIGN KEY (spot_no) REFERENCES spot(spot_no)
);