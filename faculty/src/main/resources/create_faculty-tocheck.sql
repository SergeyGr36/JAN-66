CREATE TABLE MARK (
MARK_TID INT PRIMARY KEY AUTO_INCREMENT,
SCORE INT,
REFERENCE VARCHAR2(4000)
);
CREATE TABLE COURSE (
COURSE_TID INT PRIMARY KEY AUTO_INCREMENT,
CODE VARCHAR2(30),
DESCRIPTION VARCHAR2(4000)
);
