INSERT INTO authority
VALUES ('ROLE_ADMIN');
INSERT INTO authority
VALUES ('ROLE_USER');
INSERT into users
VALUES (1, 'admin', 'admin', '$2a$10$bAETRWVNlR1QD6xHHYPUFuFQ8s5ujohB.JYloKzNxVhAatqCVEG22', 'admin');
/* ADMIN ACCOUNT: credentials : admin admin*/
INSERT into users
VALUES (2, 'guest', 'guest', '$2a$10$eymbAGwTBLZZdCSmmFk3muUmFdJaUczJA4RivSDCB1ekUxuWzaW7O', 'guest');
/* ADMIN ACCOUNT: credentials : guest guest*/

INSERT INTO user_authority
VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_authority
VALUES (1, 'ROLE_USER');

INSERT INTO user_authority
VALUES (2, 'ROLE_USER');
