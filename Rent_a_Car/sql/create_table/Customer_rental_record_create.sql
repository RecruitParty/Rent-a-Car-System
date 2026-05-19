USE renatl_car;


CREATE TABLE Customer (

user_id INTEGER PRIMARY KEY,

user_name VARCHAR(50) NOT NULL,

user_phone VARCHAR(20) NOT NULL UNIQUE

);


CREATE TABLE Rental (

rental_id INTEGER PRIMARY KEY,

FOREIGN KEY (user_id) REFERENCES Customer(user_id),

FOREIGN KEY (car_no) REFERENCES Car(car_no)

);



CREATE TABLE Rental_record (

rental_id INTEGER PRIMARY KEY,

rental_spot INTEGER NOT NULL,

return_spot INTEGER,

rental_date DATE NOT NULL,

expected_return_date DATE NOT NULL,

actual_return_date DATE,

FOREIGN KEY (rental_id) REFERENCES Rental(rental_id),

FOREIGN KEY (rental_spot) REFERENCES Spot(spot_no),

FOREIGN KEY (return_spot) REFERENCES Spot(spot_no)

);