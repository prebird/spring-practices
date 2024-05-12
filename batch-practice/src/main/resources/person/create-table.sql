drop table if exists person_from;
CREATE TABLE person_from (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        last_name VARCHAR(40),
                        first_name VARCHAR(40),
                        age INT
);

drop table if exists person_to;
CREATE TABLE person_to (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(80),
                        age INT
);
