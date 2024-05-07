drop table if exists member;
CREATE TABLE member (
              id INT AUTO_INCREMENT PRIMARY KEY,
              name VARCHAR(255),
              member_status enum ('INUSE', 'LEAVE'),
              leave_at DATETIME NULL,
              is_deleted enum ('Y', 'N')
);
