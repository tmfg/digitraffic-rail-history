CREATE TABLE `train` (
  `departure_date` date NOT NULL,
  `train_number` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `fetch_date` datetime NOT NULL,
  `jdoc` JSON NOT NULL,
  PRIMARY KEY (`departure_date`,`train_number`,`fetch_date`),
  KEY `train_version_IDX` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `composition` (
  `departure_date` date NOT NULL,
  `train_number` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `fetch_date` datetime NOT NULL,
  `jdoc` JSON NOT NULL,
  PRIMARY KEY (`departure_date`,`train_number`,`fetch_date`),
  KEY `train_version_IDX` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;