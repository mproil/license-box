INSERT INTO licenseboxdb.app_user (username, first_name, last_name, email, password) 
	VALUES ('admin', 'Admin', 'Admin', 'licenseboxproject@gmail.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');
INSERT INTO licenseboxdb.user_role (username, role_name) 
	VALUES ('admin', 'USER');
INSERT INTO licenseboxdb.user_role (username, role_name) 
	VALUES ('admin', 'ADMIN');