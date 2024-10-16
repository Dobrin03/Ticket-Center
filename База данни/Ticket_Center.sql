CREATE TABLE Administrator_Data(
Admin_ID INTEGER NOT NULL,
Admin_Name VARCHAR(50),
Admin_User VARCHAR(30),
Admin_Pass VARCHAR(20));
ALTER TABLE Administrator_Data ADD CONSTRAINT PK_Admin PRIMARY KEY(Admin_ID);

INSERT INTO Administrator_Data VALUES(1, 'Admin', 'project_admin', 'admin123');


CREATE TABLE Event_Type(
Event_Type_ID INTEGER NOT NULL,
Event_Type_Name VARCHAR(30));
ALTER TABLE Event_Type ADD CONSTRAINT PK_Event_Type PRIMARY KEY(Event_Type_ID);


CREATE TABLE Event_Status(
Event_Status_ID INTEGER NOT NULL,
Event_Status_Name VARCHAR(30));
ALTER TABLE Event_Status ADD CONSTRAINT PK_Event_Status PRIMARY KEY(Event_Status_ID);

INSERT INTO Event_Status VALUES(10, 'Ongoing');
INSERT INTO Event_Status VALUES(11, 'Cancelled');
INSERT INTO Event_Status VALUES(12, 'To be determined');


CREATE TABLE City(
City_ID INTEGER NOT NULL,
City_Name VARCHAR(30));
ALTER TABLE City ADD CONSTRAINT PK_City PRIMARY KEY(City_ID);


CREATE TABLE Seat_Type(
Seat_Type_ID INTEGER NOT NULL,
Seat_Type_Name VARCHAR(30));
ALTER TABLE Seat_Type ADD CONSTRAINT PK_Seat_Type PRIMARY KEY(Seat_Type_ID);


INSERT INTO Seat_Type VALUES(20, 'Regular');
INSERT INTO Seat_Type VALUES(21, 'Front Row');
INSERT INTO Seat_Type VALUES(22, 'Back Row');
INSERT INTO Seat_Type VALUES(23, 'Balcony');
INSERT INTO Seat_Type VALUES(24, 'Lounge');


CREATE TABLE Organiser_Data(
Organiser_ID INTEGER NOT NULL,
Organiser_Name VARCHAR(50),
Organiser_User VARCHAR(30),
Organiser_Pass VARCHAR(20));
ALTER TABLE Organiser_Data ADD CONSTRAINT PK_Organiser PRIMARY KEY(Organiser_ID);


CREATE TABLE Distributor_Data(
Distributor_ID INTEGER NOT NULL,
Distributor_Name VARCHAR(50),
Distributor_User VARCHAR(30),
Distributor_Pass VARCHAR(20),
Distributor_Fee NUMBER(5,2),
Rating NUMBER(2,2));
ALTER TABLE Distributor_Data ADD CONSTRAINT PK_Distributor PRIMARY KEY(Distributor_ID);


CREATE TABLE Client_Data(
Client_ID INTEGER NOT NULL,
Client_Name VARCHAR(50),
Client_User VARCHAR(30),
Client_Pass VARCHAR(20),
Client_Email VARCHAR(50),
Client_Address VARCHAR(100),
Client_Number VARCHAR(10),
City_ID INTEGER);
ALTER TABLE Client_Data ADD CONSTRAINT PK_Client PRIMARY KEY(Client_ID);
ALTER TABLE Client_Data ADD CONSTRAINT FK_City FOREIGN KEY(City_ID) REFERENCES City(City_ID);


CREATE TABLE Event(
Event_ID INTEGER NOT NULL,
Event_Name VARCHAR(50),
Ticket_Limit_per_Person INTEGER,
Event_Date DATE,
Event_Address VARCHAR(100),
City_ID INTEGER,
Event_Type_ID INTEGER,
Event_Status_ID INTEGER,
Organiser_ID INTEGER);
ALTER TABLE Event ADD CONSTRAINT PK_Event PRIMARY KEY(Event_ID);
ALTER TABLE Event ADD CONSTRAINT FK_Event_City FOREIGN KEY(City_ID) REFERENCES City(City_ID);
ALTER TABLE Event ADD CONSTRAINT FK_Type FOREIGN KEY(Event_Type_ID) REFERENCES Event_Type(Event_Type_ID);
ALTER TABLE Event ADD CONSTRAINT FK_Status FOREIGN KEY(Event_Status_ID) REFERENCES Event_Status(Event_Status_ID);
ALTER TABLE Event ADD CONSTRAINT FK_Organiser FOREIGN KEY(Organiser_ID) REFERENCES Organiser_Data(Organiser_ID);


CREATE TABLE Event_Distributor(
Event_Distributor_ID INTEGER NOT NULL,
Event_ID INTEGER,
Distributor_ID INTEGER);
ALTER TABLE Event_Distributor ADD CONSTRAINT PK_Event_Distributor PRIMARY KEY(Event_Distributor_ID);
ALTER TABLE Event_Distributor ADD CONSTRAINT FK_Event FOREIGN KEY(Event_ID) REFERENCES Event(Event_ID);
ALTER TABLE Event_Distributor ADD CONSTRAINT FK_Distributor FOREIGN KEY(Distributor_ID) REFERENCES Distributor_Data(Distributor_ID);


CREATE TABLE Event_Seats(
Event_Seats_ID INTEGER NOT NULL,
Event_ID INTEGER,
Seat_Type_ID INTEGER, 
Seat_Quantity INTEGER,
Seat_Price NUMBER(5,2));
ALTER TABLE Event_Seats ADD CONSTRAINT PK_Event_Seats PRIMARY KEY(Event_Seats_ID);
ALTER TABLE Event_Seats ADD CONSTRAINT FK_Event_Seats_Event FOREIGN KEY(Event_ID) REFERENCES Event(Event_ID);
ALTER TABLE Event_Seats ADD CONSTRAINT FK_Seat_Type FOREIGN KEY(Seat_Type_ID) REFERENCES Seat_Type(Seat_Type_ID);


CREATE TABLE Ticket(
Ticket_ID INTEGER NOT NULL,
Client_ID INTEGER,
Event_Seats_ID INTEGER, 
Event_Distributor_ID INTEGER);
ALTER TABLE Ticket ADD CONSTRAINT PK_Ticket PRIMARY KEY(Ticket_ID);
ALTER TABLE Ticket ADD CONSTRAINT FK_Client FOREIGN KEY(Client_ID) REFERENCES Client_Data(Client_ID);
ALTER TABLE Ticket ADD CONSTRAINT FK_Event_Seat FOREIGN KEY(Event_Seats_ID) REFERENCES Event_Seats(Event_Seats_ID);
ALTER TABLE Ticket ADD CONSTRAINT FK_Event_Distributor FOREIGN KEY(Event_Distributor_ID) REFERENCES Event_Distributor(Event_Distributor_ID);