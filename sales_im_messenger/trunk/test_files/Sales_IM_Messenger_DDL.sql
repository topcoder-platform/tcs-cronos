CREATE TABLE session_mode (
  session_mode_id INTEGER NOT NULL,
  name VARCHAR(64) NOT NULL,
  description VARCHAR(255) NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  PRIMARY KEY(session_mode_id)
);

CREATE TABLE all_user (
  user_id INTEGER NOT NULL,
  registered_flag VARCHAR(1) NOT NULL,
  username VARCHAR(64) NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  PRIMARY KEY(user_id)
);

CREATE TABLE session (
  session_id INTEGER NOT NULL,
  session_mode_id INTEGER NOT NULL,
  create_user_id INTEGER NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  PRIMARY KEY(session_id),
  FOREIGN KEY(session_mode_id)
    REFERENCES session_mode(session_mode_id),
  FOREIGN KEY(create_user_id)
    REFERENCES all_user(user_id)
);

CREATE TABLE group_session (
  session_id INTEGER NOT NULL,
  topic VARCHAR(255) NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  PRIMARY KEY(session_id),
  FOREIGN KEY(session_id)
    REFERENCES session(session_id)
);

CREATE TABLE session_user (
  session_user_id INTEGER NOT NULL,
  session_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  enter_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  exit_date DATETIME YEAR TO FRACTION(3),
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  PRIMARY KEY(session_user_id),
  FOREIGN KEY(session_id)
    REFERENCES session(session_id),
  FOREIGN KEY(user_id)
    REFERENCES all_user(user_id)
);

CREATE TABLE category (
  category_id INTEGER NOT NULL,
  name VARCHAR(64) NOT NULL,
  description VARCHAR(255) NOT NULL,
  chattable_flag VARCHAR(1) NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  PRIMARY KEY(category_id)
);


CREATE TABLE session_requested_user (
  session_id INTEGER NOT NULL,
  requested_user_id INTEGER NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  PRIMARY KEY(session_id, requested_user_id),
  FOREIGN KEY(session_id)
    REFERENCES session(session_id),
  FOREIGN KEY(requested_user_id)
    REFERENCES all_user(user_id)
);

CREATE TABLE sales_session (
  session_id INTEGER NOT NULL,
  category_id INTEGER NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  PRIMARY KEY(session_id),
  FOREIGN KEY(session_id)
    REFERENCES session(session_id),
  FOREIGN KEY(category_id)
    REFERENCES category(category_id)
);

CREATE TABLE session_user_message (
  session_user_id INTEGER NOT NULL,
  message_text VARCHAR(255) NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  create_user VARCHAR(64) NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) NOT NULL,
  modify_user VARCHAR(64) NOT NULL,
  FOREIGN KEY(session_user_id)
    REFERENCES session_user(session_user_id)
);


CREATE TABLE id_sequences (
    name                VARCHAR(255) NOT NULL  PRIMARY KEY,
    next_block_start    INT8 NOT NULL,
    block_size          INT NOT NULL,
    exhausted		int default 0
);

CREATE TABLE profile (
       profile_id DEC(18) NOT NULL,
       username       VARCHAR(64) NOT NULL UNIQUE,
       PRIMARY KEY(profile_id)	
);

CREATE TABLE property (
       property_id         DEC(18) NOT NULL,
       name                VARCHAR(64) NOT NULL,
       PRIMARY KEY(property_id) 
);

CREATE TABLE profile_property (
       profile_id          DEC(18) NOT NULL,
       property_id         DEC(18) NOT NULL,
       value               VARCHAR(64) NOT NULL,
       FOREIGN KEY (profile_id)
          REFERENCES profile(profile_id),
       FOREIGN KEY (property_id)
          REFERENCES property(property_id) 
          
);

CREATE TABLE user_profile (
       user_profile_id         DEC(18) NOT NULL,
       user_profile_name       VARCHAR(64) NOT NULL UNIQUE,
       PRIMARY KEY(user_profile_id)
);

CREATE TABLE user_property (
       user_property_id         DEC(18) NOT NULL,
       user_property_name                VARCHAR(64) NOT NULL,
       PRIMARY KEY(user_property_id)
);

CREATE TABLE user_profile_property (
       profile_property_profile_id          DEC(18) NOT NULL,
       profile_property_property_id         DEC(18) NOT NULL,
       user_value               VARCHAR(64) NOT NULL,
       FOREIGN KEY (profile_property_profile_id)
           REFERENCES user_profile(user_profile_id),
       FOREIGN KEY (profile_property_property_id)
           REFERENCES user_property(user_property_id)    
);

CREATE TABLE buddy_user (
  user_id INTEGER NOT NULL,
  buddy_user_id INTEGER NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) DEFAULT CURRENT NOT NULL,
  create_user VARCHAR(64) DEFAULT USER NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) DEFAULT CURRENT NOT NULL,
  modify_user VARCHAR(64) DEFAULT USER NOT NULL,
  PRIMARY KEY(user_id, buddy_user_id),
  FOREIGN KEY(user_id)
    REFERENCES all_user(user_id),
  FOREIGN KEY(buddy_user_id)
    REFERENCES all_user(user_id)
);

CREATE TABLE blocked_user (
  user_id INTEGER NOT NULL,
  blocked_user_id INTEGER NOT NULL,
  unblocked_flag VARCHAR(1) NOT NULL,
  create_date DATETIME YEAR TO FRACTION(3) DEFAULT CURRENT NOT NULL,
  create_user VARCHAR(64) DEFAULT USER NOT NULL,
  modify_date DATETIME YEAR TO FRACTION(3) DEFAULT CURRENT NOT NULL,
  modify_user VARCHAR(64) DEFAULT USER NOT NULL,
  PRIMARY KEY(user_id, blocked_user_id),
  FOREIGN KEY(user_id)
    REFERENCES all_user(user_id),
  FOREIGN KEY(blocked_user_id)
    REFERENCES all_user(user_id)
);

/* -----------------------     INSERT STATEMENTS -------------------------------------------------------------------*/

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) VALUES ('profileKeyGenerator', 0, 10, 0);
INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) VALUES ('propertyKeyGenerator', 0, 10, 0);
INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) VALUES ('profile', 0, 10, 0);
INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) VALUES ('property', 0, 10, 0);
INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) VALUES ('session_id', 500, 10, 0);
INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) VALUES ('session_user_id', 500, 10, 0);
