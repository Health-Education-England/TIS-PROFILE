
CREATE TABLE IF NOT EXISTS user (
  user_name varchar(50) NOT NULL primary key,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  gmc_id varchar(7) NOT NULL,
  designated_body_code varchar(20) NOT NULL,
  phone_number varchar(10)
);
