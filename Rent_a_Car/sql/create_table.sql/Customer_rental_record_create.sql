USE renatl_car;


CREATE TABLE Customer (

user_id INTEGER PRIMARY KEY,

user_name VARCHAR(50) NOT NULL,

user_phone VARCHAR(20) NOT NULL UNIQUE

);


CREATE TABLE Rental (

rental_id INTEGER PRIMARY KEY,

FOREIGN KEY (user_id) REFERENCES Customer(user_id)

##FOREIGN KEY (차량번호) REFERENCES 차량(차량번호)

);



CREATE TABLE Rental_record (

rental_id INTEGER PRIMARY KEY,

rental_dest INTEGER NOT NULL,

return_dest INTEGER,

rental_date DATE NOT NULL,

expected_return_date DATE NOT NULL,

actual_return_date DATE,

FOREIGN KEY (rental_id) REFERENCES Rental(rental_id)

##FOREIGN KEY (rental_dest) REFERENCES 지점(지점번호),

##FOREIGN KEY (return_dest) REFERENCES 지점(지점번호)

);