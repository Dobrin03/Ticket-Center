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

CREATE SEQUENCE EType_SEQ START WITH 100 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER EType_ID_AUTO
BEFORE INSERT ON Event_Type
FOR EACH ROW
WHEN (NEW.Event_Type_ID IS NULL)
BEGIN
    :NEW.Event_Type_ID:=EType_SEQ.NEXTVAL;
END;

CREATE OR REPLACE PROCEDURE EType_Ins
(v_event_type_name Event_Type.event_type_name%type) AS
BEGIN
    INSERT INTO Event_Type(event_type_name)
    VALUES(v_event_type_name);
END;

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

CREATE SEQUENCE City_SEQ START WITH 200 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER City_ID_AUTO
BEFORE INSERT ON City
FOR EACH ROW
WHEN (NEW.City_ID IS NULL)
BEGIN
    :NEW.City_ID:=City_SEQ.NEXTVAL;
END;

CREATE OR REPLACE PROCEDURE City_Ins
(v_city_name City.city_name%type) AS
BEGIN
    INSERT INTO City(city_name)
    VALUES(v_city_name);
END;

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

CREATE SEQUENCE Organiser_SEQ START WITH 300 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER Organiser_ID_AUTO
BEFORE INSERT ON Organiser_Data
FOR EACH ROW
WHEN (NEW.Organiser_ID IS NULL)
BEGIN
    :NEW.Organiser_ID:=Organiser_SEQ.NEXTVAL;
END;

CREATE OR REPLACE PROCEDURE Organiser_Ins
(v_organiser_name Organiser_Data.organiser_name%type,
v_organiser_user Organiser_Data.organiser_user%type,
v_organiser_pass Organiser_Data.organiser_pass%type) AS
BEGIN
    INSERT INTO Organiser_Data(organiser_name, organiser_user, organiser_pass)
    VALUES(v_organiser_name, v_organiser_user, v_organiser_pass);
END;

CREATE TABLE Distributor_Data(
Distributor_ID INTEGER NOT NULL,
Distributor_Name VARCHAR(50),
Distributor_User VARCHAR(30),
Distributor_Pass VARCHAR(20),
Distributor_Fee NUMBER(5,2));
ALTER TABLE Distributor_Data ADD CONSTRAINT PK_Distributor PRIMARY KEY(Distributor_ID);

CREATE SEQUENCE Distributor_SEQ START WITH 400 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER Distributor_ID_AUTO
BEFORE INSERT ON Distributor_Data
FOR EACH ROW
WHEN (NEW.Distributor_ID IS NULL)
BEGIN
    :NEW.Distributor_ID:=Distributor_SEQ.NEXTVAL;
END;

CREATE OR REPLACE PROCEDURE Distributor_Ins
(v_distributor_name Distributor_Data.distributor_name%type,
v_distributor_user Distributor_Data.distributor_user%type,
v_distributor_pass Distributor_Data.distributor_pass%type,
v_distributor_fee Distributor_Data.distributor_fee%type) AS
BEGIN
    INSERT INTO Distributor_Data(distributor_name, distributor_user, distributor_pass, distributor_fee)
    VALUES(v_distributor_name, v_distributor_user, v_distributor_pass, v_distributor_fee);
END;

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

CREATE SEQUENCE Client_SEQ START WITH 500 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER Client_ID_AUTO
BEFORE INSERT ON Client_Data
FOR EACH ROW
WHEN (NEW.Client_ID IS NULL)
BEGIN
    :NEW.Client_ID:=Distributor_SEQ.NEXTVAL;
END;

CREATE OR REPLACE PROCEDURE Client_Ins
(v_client_name Client_Data.client_name%type,
v_client_user Client_Data.client_user%type,
v_client_pass Client_Data.client_pass%type,
v_client_email Client_Data.client_email%type,
v_client_address Client_Data.client_address%type,
v_client_number Client_Data.client_number%type,
v_city_id Client_Data.city_id%type) AS
BEGIN
    INSERT INTO Client_Data(client_name, client_user, client_pass, client_email, client_address, client_number, city_id)
    VALUES(v_client_name, v_client_user, v_client_pass, v_client_email, v_client_address, v_client_number, v_city_id);
END;

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

CREATE SEQUENCE Event_SEQ START WITH 600 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER Event_ID_AUTO
BEFORE INSERT ON Event
FOR EACH ROW
WHEN (NEW.Event_ID IS NULL)
BEGIN
    :NEW.Event_ID:=Event_SEQ.NEXTVAL;
END;

CREATE TABLE Event_Distributor(
Event_Distributor_ID INTEGER NOT NULL,
Event_Seats_ID INTEGER,
Distributor_ID INTEGER,
Is_Distributing NUMBER(1, 0));
ALTER TABLE Event_Distributor ADD CONSTRAINT PK_Event_Distributor PRIMARY KEY(Event_Distributor_ID);
ALTER TABLE Event_Distributor ADD CONSTRAINT FK_Seats FOREIGN KEY(Event_Seats_ID) REFERENCES Event_Seats(Event_Seats_ID);
ALTER TABLE Event_Distributor ADD CONSTRAINT FK_Distributor FOREIGN KEY(Distributor_ID) REFERENCES Distributor_Data(Distributor_ID);

CREATE SEQUENCE ED_SEQ START WITH 700 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER ED_ID_AUTO
BEFORE INSERT ON Event_Distributor
FOR EACH ROW
WHEN (NEW.Event_Distributor_ID IS NULL)
BEGIN
    :NEW.Event_Distributor_ID:=ED_SEQ.NEXTVAL;
END;

CREATE OR REPLACE PROCEDURE ED_Ins
(v_event_seats_id Event_Distributor.event_seats_id%type,
v_distributor_id Event_Distributor.distributor_id%type) AS
BEGIN
    INSERT INTO Event_Distributor(event_seats_id, distributor_id)
    VALUES(v_event_seats_id, v_distributor_id);
END;

CREATE TABLE Event_Seats(
Event_Seats_ID INTEGER NOT NULL,
Event_ID INTEGER,
Seat_Type_ID INTEGER, 
Seat_Quantity INTEGER,
Seat_Price NUMBER(5,2));
ALTER TABLE Event_Seats ADD CONSTRAINT PK_Event_Seats PRIMARY KEY(Event_Seats_ID);
ALTER TABLE Event_Seats ADD CONSTRAINT FK_Event_Seats_Event FOREIGN KEY(Event_ID) REFERENCES Event(Event_ID);
ALTER TABLE Event_Seats ADD CONSTRAINT FK_Seat_Type FOREIGN KEY(Seat_Type_ID) REFERENCES Seat_Type(Seat_Type_ID);

CREATE SEQUENCE ES_SEQ START WITH 800 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER ES_ID_AUTO
BEFORE INSERT ON Event_Seats
FOR EACH ROW
WHEN (NEW.Event_Seats_ID IS NULL)
BEGIN
    :NEW.Event_Seats_ID:=ES_SEQ.NEXTVAL;
END;

CREATE OR REPLACE PROCEDURE ES_Ins
(v_event_id Event_Seats.event_id%type,
v_seat_type_id Event_Seats.seat_type_id%type,
v_seat_quantity Event_Seats.seat_quantity%type,
v_seat_price Event_Seats.seat_price%type) AS
BEGIN
    INSERT INTO Event_Seats(event_id, seat_type_id, seat_quantity, seat_price)
    VALUES(v_event_id, v_seat_type_id, v_seat_quantity, v_seat_price);
END;

CREATE TABLE Ticket(
Ticket_ID INTEGER NOT NULL,
Client_ID INTEGER,
Event_Distributor_ID INTEGER);
ALTER TABLE Ticket ADD CONSTRAINT PK_Ticket PRIMARY KEY(Ticket_ID);
ALTER TABLE Ticket ADD CONSTRAINT FK_Client FOREIGN KEY(Client_ID) REFERENCES Client_Data(Client_ID);
ALTER TABLE Ticket ADD CONSTRAINT FK_Event_Distributor FOREIGN KEY(Event_Distributor_ID) REFERENCES Event_Distributor(Event_Distributor_ID);

CREATE SEQUENCE Ticket_SEQ START WITH 900 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER Ticket_ID_AUTO
BEFORE INSERT ON Ticket
FOR EACH ROW
WHEN (NEW.Ticket_ID IS NULL)
BEGIN
    :NEW.Ticket_ID:=Ticket_SEQ.NEXTVAL;
END;

CREATE OR REPLACE PROCEDURE Ticket_Ins
(v_client_id Ticket.client_id%type,
v_event_distributor_id Ticket.event_distributor_id%type) AS
BEGIN
    INSERT INTO Ticket(client_id, event_distributor_id)
    VALUES(v_client_id, v_event_distributor_id);
END;

CREATE TABLE Distributor_Rating(
Rating_ID INTEGER NOT NULL,
Rating_Value NUMBER(3,2),
Organiser_ID INTEGER,
Distributor_ID INTEGER,
Review NVARCHAR2(1000));
ALTER TABLE Distributor_Rating ADD CONSTRAINT PK_Rating PRIMARY KEY(Rating_ID);
ALTER TABLE Distributor_Rating ADD CONSTRAINT FK_Rater FOREIGN KEY(Organiser_ID) REFERENCES Organiser_Data(Organiser_ID);
ALTER TABLE Distributor_Rating ADD CONSTRAINT FK_Rated FOREIGN KEY(Distributor_ID) REFERENCES Distributor_Data(Distributor_ID);

CREATE SEQUENCE Rating_SEQ START WITH 1000 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER Rating_ID_AUTO
BEFORE INSERT ON Distributor_Rating
FOR EACH ROW
WHEN (NEW.Rating_ID IS NULL)
BEGIN
    :NEW.Rating_ID:=Rating_SEQ.NEXTVAL;
END;

CREATE OR REPLACE PROCEDURE Rating_Ins
(v_rating Distributor_Rating.Rating_Value%type,
v_organiser Distributor_Rating.Organiser_ID%type,
v_distributor Distributor_Rating.Distributor_ID%type,
v_review Distributor_Rating.Review%type) AS
BEGIN
    INSERT INTO Distributor_Rating(rating_value, organiser_id, distributor_id, review)
    VALUES(v_rating, v_organiser, v_distributor, v_review);
END;

CREATE OR REPLACE PROCEDURE ORGANISER_UPD
(v_name ORGANISER_DATA.organiser_name%type,
v_id ORGANISER_DATA.organiser_id%type,
v_user ORGANISER_DATA.organiser_user%type,
v_pass ORGANISER_DATA.organiser_pass%type) AS
BEGIN
    UPDATE ORGANISER_DATA
    SET ORGANISER_NAME=v_name, ORGANISER_USER=v_user,  ORGANISER_PASS=v_pass 
    WHERE ORGANISER_ID=v_id;
END;

CREATE OR REPLACE PROCEDURE ORGANISER_DEL
(v_id ORGANISER_DATA.organiser_id%type) AS
BEGIN
    DELETE FROM ORGANISER_DATA
    WHERE ORGANISER_ID=v_id;
END;

CREATE OR REPLACE PROCEDURE DISTRIBUTOR_UPD
(v_name DISTRIBUTOR_DATA.distributor_name%type,
v_id DISTRIBUTOR_DATA.distributor_id%type,
v_user DISTRIBUTOR_DATA.distributor_user%type,
v_pass DISTRIBUTOR_DATA.distributor_pass%type,
v_fee DISTRIBUTOR_DATA.distributor_fee%type) AS
BEGIN
    UPDATE DISTRIBUTOR_DATA
    SET DISTRIBUTOR_NAME=v_name, DISTRIBUTOR_USER=v_user,  DISTRIBUTOR_PASS=v_pass, DISTRIBUTOR_FEE=v_fee
    WHERE DISTRIBUTOR_ID=v_id;
END;

CREATE OR REPLACE PROCEDURE DISTRIBUTOR_DEL
(v_id DISTRIBUTOR_DATA.distributor_id%type) AS
BEGIN
    DELETE FROM DISTRIBUTOR_DATA
    WHERE DISTRIBUTOR_ID=v_id;
END;

CREATE OR REPLACE PROCEDURE CLIENT_UPD
(v_name CLIENT_DATA.client_name%type,
v_id CLIENT_DATA.client_id%type,
v_user CLIENT_DATA.client_user%type,
v_pass CLIENT_DATA.client_pass%type,
v_email CLIENT_DATA.client_email%type,
v_address CLIENT_DATA.client_address%type,
v_number CLIENT_DATA.client_number%type,
v_city_id CLIENT_DATA.city_id%type) AS
BEGIN
    UPDATE CLIENT_DATA
    SET CLIENT_NAME=v_name, CLIENT_USER=v_user,  CLIENT_PASS=v_pass, CLIENT_EMAIL=v_email,  CLIENT_ADDRESS=v_address, CLIENT_NUMBER=v_number, CITY_ID=v_city_id 
    WHERE CLIENT_ID=v_id;
END;

CREATE OR REPLACE PROCEDURE CLIENT_DEL
(v_id CLIENT_DATA.client_id%type) AS
BEGIN
    DELETE FROM CLIENT_DATA
    WHERE CLIENT_ID=v_id;
END;

CREATE OR REPLACE PROCEDURE FIND_ADMIN
(v_user Administrator_data.admin_user%type,
v_pass Administrator_data.admin_pass%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT * FROM ADMINISTRATOR_DATA
    WHERE Admin_User=v_user AND Admin_Pass=v_pass;
END;

CREATE OR REPLACE PROCEDURE FIND_ORGANISER
(v_user Organiser_data.organiser_user%type,
v_pass Organiser_data.organiser_pass%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT * FROM ORGANISER_DATA
    WHERE Organiser_User=v_user AND Organiser_Pass=v_pass;
END;

CREATE OR REPLACE PROCEDURE FIND_DISTRIBUTOR
(v_user Distributor_data.distributor_user%type,
v_pass Distributor_data.distributor_pass%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT * FROM DISTRIBUTOR_DATA
    WHERE Distributor_User=v_user AND Distributor_Pass=v_pass;
END;

CREATE OR REPLACE PROCEDURE FIND_CLIENT
(v_user Client_data.Client_user%type,
v_pass Client_data.Client_pass%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT c.Client_ID, c.Client_Name, c.Client_User, c.Client_Pass, c.Client_Email, c.Client_Address, c.Client_Number, City_Name FROM CLIENT_DATA c
    JOIN City ct ON c.city_id=ct.city_id
    WHERE Client_User=v_user AND Client_Pass=v_pass;
END;

CREATE OR REPLACE PROCEDURE FIND_ALL_ORGANISERS
(cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT * FROM ORGANISER_DATA;
END;

create or replace PROCEDURE FIND_ALL_DISTRIBUTORS
(cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT * FROM Distributor_Data;
END;

CREATE OR REPLACE PROCEDURE FIND_UNRATED_DISTRIBUTORS
(v_organiser Distributor_Rating.Organiser_ID%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT *
    FROM Distributor_Data
    WHERE Distributor_ID NOT IN(
    SELECT Distributor_ID
    FROM Distributor_Rating
    WHERE Organiser_ID=v_organiser);
END;

CREATE OR REPLACE PROCEDURE FIND_ALL_CLIENTS
(cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT c.Client_ID, c.Client_Name, c.Client_User, c.Client_Pass, c.Client_Email, c.Client_Address, c.Client_Number, City_Name FROM CLIENT_DATA c
    JOIN City ct ON c.city_id=ct.city_id;
END;

CREATE OR REPLACE PROCEDURE CHECK_CITY
(v_name City.city_name%type, 
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT City_ID FROM City WHERE City_Name = v_name;
END;

CREATE OR REPLACE PROCEDURE ET_INS
(v_name Event_Type.event_type_name%type) AS
BEGIN
    INSERT INTO Event_Type(event_type_name)
    VALUES(v_name);
END;

CREATE OR REPLACE PROCEDURE EVENT_INS
(v_name Event.event_name%type,
v_limit Event.ticket_limit_per_person%type, 
v_date Event.event_date%type, 
v_address Event.event_address%type, 
v_city Event.city_id%type, 
v_type Event.event_type_id%type, 
v_organiser Event.organiser_id%type,
v_id OUT Event.event_id%type) AS
BEGIN
    INSERT INTO Event(event_name, ticket_limit_per_person, event_date, event_address, city_id, event_type_id, organiser_id)
    VALUES(v_name, v_limit, v_date, v_address, v_city, v_type, v_organiser)
    RETURNING event_id INTO v_id;
END;

CREATE OR REPLACE PROCEDURE EVENT_UPD
(v_id Event.event_id%type,
v_name Event.event_name%type,
v_limit Event.ticket_limit_per_person%type, 
v_date Event.event_date%type, 
v_address Event.event_address%type, 
v_city Event.city_id%type, 
v_type Event.event_type_id%type, 
v_status Event.event_status_id%type) AS
BEGIN
    UPDATE Event
    SET Event_name = v_name, Ticket_limit_per_person = v_limit, Event_date = v_date, Event_address = v_address, City_id = v_city, Event_type_id = v_type, Event_status_id = v_status
    WHERE Event_id = v_id;
END;

CREATE OR REPLACE PROCEDURE ES_INS
(v_event Event_Seats.event_id%type,
v_seat Event_Seats.seat_type_id%type,
v_quantity Event_Seats.seat_quantity%type,
v_price Event_Seats.seat_price%type,
v_id OUT Event_Seats.event_seats_id%type)AS
BEGIN
    INSERT INTO Event_Seats(event_id, seat_type_id, seat_quantity, seat_price)
    VALUES(v_event, v_seat, v_quantity, v_price)
    RETURNING event_seats_id INTO v_id;
END;

CREATE OR REPLACE PROCEDURE ED_INS
(v_seats Event_Distributor.event_seats_id%type,
v_distributor Event_Distributor.distributor_id%type)AS
BEGIN
    INSERT INTO Event_Distributor(event_seats_id, distributor_id)
    VALUES(v_seats, v_distributor);
END;

CREATE OR REPLACE PROCEDURE FIND_SEAT_TYPE
(cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT Seat_Type_Name FROM Seat_Type;
END;

CREATE OR REPLACE PROCEDURE CHECK_ETYPE
(v_type Event_Type.event_type_name%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT * FROM Event_Type WHERE Event_Type_Name=v_type;
END;

CREATE OR REPLACE TRIGGER DEFINE_EVENT_STATUS
BEFORE INSERT OR UPDATE ON Event
FOR EACH ROW
BEGIN
    IF(:NEW.Event_Status_ID != 11 OR :NEW.Event_Status_ID IS NULL)
    THEN
        IF(:NEW.Event_Date IS NULL)
        THEN
            :NEW.Event_Status_ID:=12;
        ELSE
            :NEW.Event_Status_ID:=10;
        END IF;
    END IF;
END;

CREATE OR REPLACE PROCEDURE CHECK_STYPE
(v_type Seat_Type.seat_type_name%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT * FROM Seat_Type WHERE Seat_Type_Name=v_type;
END;

CREATE OR REPLACE PROCEDURE FIND_EVENTS
(v_organiser Event.Organiser_Id%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT E.Event_Id,E.Event_Name,E.Ticket_Limit_Per_Person,E.Event_Date,E.Event_Address,C.City_Name,T.Event_Type_Name,S.Event_Status_Name 
    FROM Event E
    JOIN City C ON C.City_Id=E.City_Id
    JOIN Event_Type T ON T.Event_Type_Id=E.Event_Type_Id
    JOIN Event_Status S ON S.Event_Status_Id=E.Event_Status_Id
    WHERE Organiser_Id=v_organiser;
END;

CREATE OR REPLACE PROCEDURE FIND_EVENT_SEATS
(v_event Event_Seats.event_id%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT S.SEAT_TYPE_ID,S.SEAT_TYPE_NAME,E.SEAT_QUANTITY,E.SEAT_PRICE 
    FROM Event_Seats E 
    JOIN Seat_Type S on S.SEAT_TYPE_ID = E.SEAT_TYPE_ID
    WHERE Event_Id=v_event;
END;

CREATE OR REPLACE PROCEDURE ES_DEL
(v_event Event_Seats.event_id%type,
v_seat Event_Seats.Seat_type_Id%type)
AS
BEGIN
    DELETE FROM Event_Seats
    WHERE Event_Id=v_event AND Seat_Type_ID=v_seat;
END;

CREATE OR REPLACE PROCEDURE FIND_STATUS
(cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT event_status_name FROM Event_Status;
END;

CREATE OR REPLACE PROCEDURE CHECK_STATUS
(v_name Event_Status.event_status_name%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT event_status_id FROM Event_Status
    WHERE event_status_name=v_name;
END;

CREATE OR REPLACE PROCEDURE ED_DEL
(v_event Event_Seats.Event_ID%type,
v_seat Event_Seats.Seat_Type_ID%type,
v_distributor Event_Distributor.Distributor_ID%type)
AS
BEGIN
    DELETE FROM Event_Distributor
    WHERE Event_Distributor_ID IN(
    SELECT Event_Distributor_ID 
    FROM Event_Distributor evd
    JOIN Event_Seats es ON es.event_seats_id=evd.event_seats_id
    WHERE es.Event_ID=v_event AND es.Seat_Type_ID=v_seat AND evd.Distributor_ID=v_distributor);
END;

CREATE OR REPLACE PROCEDURE FIND_REQUESTS
(v_distributor Event_Distributor.Distributor_ID%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR
    SELECT E.event_id,O.organiser_id FROM Event_Distributor ed
    JOIN Event_Seats es ON es.event_seats_id=ed.event_seats_id
    JOIN Event E ON E.Event_ID=es.event_id
    JOIN Organiser_data O ON O.Organiser_ID=E.Organiser_ID
    WHERE ed.distributor_id=v_distributor AND ed.is_distributing IS NULL
    GROUP BY E.event_id,O.organiser_id;
END;

CREATE OR REPLACE PROCEDURE ANSWER_REQUEST
(v_answer INTEGER,
v_distributor Event_Distributor.Distributor_ID%type)
AS 
BEGIN
    UPDATE Event_Distributor 
    SET Is_Distributing=v_answer
    WHERE Distributor_ID=v_distributor;
END;

CREATE OR REPLACE PROCEDURE FIND_ORGANISER_BY_ID
(v_id organiser_data.organiser_id%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR 
    SELECT *
    FROM Organiser_Data
    WHERE Organiser_ID=v_id;
END;

CREATE OR REPLACE PROCEDURE FIND_EVENT_BY_ID
(v_id event.event_id%type,
cur OUT SYS_REFCURSOR)
AS
BEGIN
    OPEN cur FOR 
SELECT E.Event_Id,E.Event_Name,E.Ticket_Limit_Per_Person,E.Event_Date,E.Event_Address,C.City_Name,T.Event_Type_Name,S.Event_Status_Name 
    FROM Event E
    JOIN City C ON C.City_Id=E.City_Id
    JOIN Event_Type T ON T.Event_Type_Id=E.Event_Type_Id
    JOIN Event_Status S ON S.Event_Status_Id=E.Event_Status_Id
    WHERE Event_ID=v_id;
END;

