CREATE TABLE users (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    user_type_id INT NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active CHAR(1),
    sms_notification CHAR(1),
    email_notification CHAR(1),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_profile (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    date_of_birth date,
    gender CHAR(1),
    contact_number INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `jobs` (
  `jobid` int(20) NOT NULL AUTO_INCREMENT,
  `eid` int(20) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `jobdesc` varchar(700) NOT NULL,
  `vacno` int(20) DEFAULT NULL,
  `experience` varchar(100) DEFAULT NULL,
  `basicpay` varchar(100) DEFAULT NULL,
  `fnarea` varchar(100) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `industry` varchar(200) DEFAULT NULL,
  `ugqual` varchar(100) DEFAULT NULL,
  `pgqual` varchar(100) DEFAULT NULL,
  `profile` varchar(700) DEFAULT NULL,
  `postdate` varchar(20) NOT NULL,
  PRIMARY KEY (`jobid`),
  KEY `eid` (`eid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;