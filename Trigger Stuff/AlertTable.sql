CREATE TABLE Alert(
ID int(6) PRIMARY KEY,
ID_Client int(6),
Username varchar(45),
Email varchar(45),
TotalAmount decimal(10,0),
DateOfCreation date,
HourOfCreation time
)
