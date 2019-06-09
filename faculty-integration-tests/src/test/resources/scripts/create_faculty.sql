CREATE TABLE IF NOT EXISTS COURSE (
                                      COURSE_TID INT PRIMARY KEY AUTO_INCREMENT,
                                      CODE VARCHAR(8),
                                      DESCRIPTION VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS STUDENT (
                                       ID INT PRIMARY KEY AUTO_INCREMENT,
                                       CODE VARCHAR(8),
                                       DESCRIPTION VARCHAR(200)
);
