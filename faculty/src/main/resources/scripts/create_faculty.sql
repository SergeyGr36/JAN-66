DROP TABLE IF EXISTS COURSE;
CREATE TABLE COURSE (
COURSE_TID INT PRIMARY KEY AUTO_INCREMENT,
CODE VARCHAR2(8),
DESCRIPTION VARCHAR2(200)
);
INSERT INTO COURSE(CODE,DESCRIPTION )
VALUES ('QA0','Quality assurance level 0');

INSERT INTO COURSE(CODE,DESCRIPTION )
VALUES ('QA1','Quality assurance level 1');


