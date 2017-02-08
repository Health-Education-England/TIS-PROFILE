CREATE TABLE UserDesignatedBody (
  userName varchar(50) NOT NULL,
  designatedBodyCode varchar(50) NOT NULL,
  PRIMARY KEY (userName, designatedBodyCode),
  CONSTRAINT fk_dbc_heeuser FOREIGN KEY (userName) REFERENCES HeeUser (name) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO UserDesignatedBody (userName, designatedBodyCode ) SELECT u.name, u.designatedBodyCode FROM HeeUser u;

ALTER TABLE HeeUser DROP COLUMN designatedBodyCode; 