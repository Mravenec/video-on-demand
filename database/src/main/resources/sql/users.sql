CREATE DATABASE users;

USE users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

-- Tabla para usuarios regulares
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla para administradores
CREATE TABLE admin_roles (
    user_id INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla de datos personales
CREATE TABLE personal_data (
    user_id INT NOT NULL,
    full_name VARCHAR(255),
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE account_configuration
(
	id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`key` VARCHAR(60) NOT NULL,
	value VARCHAR(60) NOT NULL,
	type VARCHAR(60) NOT NULL,
	CONSTRAINT account_configuration_uq1 UNIQUE(`key`)
);

INSERT INTO account_configuration(`key`, value, type) VALUES('SALT', 'testValue', 'java.lang.String');

-- Address 
CREATE TABLE address (
    user_id INT NOT NULL PRIMARY KEY,
    address_line1 VARCHAR(255) ,
    address_line2 VARCHAR(255),
    province VARCHAR(255),
    canton VARCHAR(255),
    postal_code VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

--  Phones
CREATE TABLE phones (
    user_id INT NOT NULL PRIMARY KEY,
    whatsapp VARCHAR(20),
    other_numbers VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Profile Picture

CREATE TABLE user_profilePicture (
    user_id INT NOT NULL,
    profile_picture BLOB,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);


DELIMITER //
CREATE PROCEDURE spUpdatePhones(IN p_user_id INT, IN p_whatsapp VARCHAR(20), IN p_other_numbers VARCHAR(255))
BEGIN
    UPDATE phones
    SET whatsapp = p_whatsapp, 
        other_numbers = p_other_numbers
    WHERE user_id = p_user_id;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE spSelectPhones(IN p_user_id INT)
BEGIN
    SELECT user_id, whatsapp, other_numbers FROM phones WHERE user_id = p_user_id;
END;
//
DELIMITER ;



DELIMITER //
CREATE PROCEDURE spSelectAddress(IN p_user_id INT)
BEGIN
    SELECT * FROM address WHERE user_id = p_user_id;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE spUpdateAddress(IN p_user_id INT, IN p_address_line1 VARCHAR(255), IN p_address_line2 VARCHAR(255), 
                                 IN p_province VARCHAR(255), IN p_canton VARCHAR(255), IN p_postal_code VARCHAR(10))
BEGIN
    UPDATE address 
    SET address_line1 = p_address_line1, address_line2 = p_address_line2, 
        province = p_province, canton = p_canton, postal_code = p_postal_code
    WHERE user_id = p_user_id;
END;
//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE spDeleteAddress(IN p_user_id INT)
BEGIN
    UPDATE address 
    SET address_line1 = NULL, 
        address_line2 = NULL, 
        province = NULL, 
        canton = NULL, 
        postal_code = NULL
    WHERE user_id = p_user_id;
END;
//
DELIMITER ;


-- -------------------------------------------------------------


DELIMITER //
CREATE TRIGGER after_user_insert
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    INSERT INTO personal_data (user_id, full_name) VALUES (NEW.id, NULL);
    INSERT INTO user_roles (user_id) VALUES (NEW.id);
    INSERT INTO admin_roles (user_id) VALUES (NEW.id);
    
    -- Agregando inserción en la tabla address
    INSERT INTO address (user_id) VALUES (NEW.id);
    INSERT INTO phones (user_id) VALUES (NEW.id);
    
    
    INSERT INTO user_profilePicture (user_id) VALUES (NEW.id);
END;
//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE spInsertUser(IN p_email VARCHAR(255), IN p_password VARCHAR(255), IN p_full_name VARCHAR(255))
BEGIN
    DECLARE new_user_id INT;
    DECLARE salt VARCHAR(60);
    
    -- Obtener el salt de la tabla de configuración
    SELECT value INTO salt FROM account_configuration WHERE `key` = 'SALT';
    
    -- Insertar el nuevo usuario en la tabla 'users' usando el hash SHA2 junto con el salt
    INSERT INTO users (email, password_hash) VALUES (p_email, SHA2(CONCAT(p_password, salt), 256));
    
    -- Obtener el ID del nuevo usuario
    SET new_user_id = LAST_INSERT_ID();
    
    -- Actualizar el registro en 'personal_data' que fue creado automáticamente por el trigger
    UPDATE personal_data SET full_name = p_full_name WHERE user_id = new_user_id;
    
    -- Actualizar el registro en 'user_roles' que fue creado automáticamente por el trigger
    UPDATE user_roles SET is_active = TRUE WHERE user_id = new_user_id;
    
    -- Actualizar el registro en 'admin_roles' que fue creado automáticamente por el trigger
    UPDATE admin_roles SET is_active = FALSE WHERE user_id = new_user_id;
    
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE spInsertAdmin(IN p_email VARCHAR(255), IN p_password VARCHAR(255), IN p_full_name VARCHAR(255))
BEGIN
    DECLARE new_admin_id INT;
    DECLARE salt VARCHAR(60);
    
    -- Obtener el salt de la tabla de configuración
    SELECT value INTO salt FROM account_configuration WHERE `key` = 'SALT';
    
    -- Inserta el nuevo administrador en la tabla 'users' usando el hash SHA2 junto con el salt
    INSERT INTO users (email, password_hash) VALUES (p_email, SHA2(CONCAT(p_password, salt), 256));
    
    -- Obtiene el ID del nuevo administrador
    SET new_admin_id = LAST_INSERT_ID();
    
    -- Actualiza el registro en 'personal_data' que fue creado automáticamente por el trigger
    UPDATE personal_data SET full_name = p_full_name WHERE user_id = new_admin_id;
    
    -- Actualiza el registro en 'user_roles' que fue creado automáticamente por el trigger
    UPDATE user_roles SET is_active = FALSE WHERE user_id = new_admin_id;
    
    -- Actualiza el registro en 'admin_roles' que fue creado automáticamente por el trigger
    UPDATE admin_roles SET is_active = TRUE WHERE user_id = new_admin_id;
    
END;
//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE spValidateUser(IN p_email VARCHAR(255), IN p_password VARCHAR(255))
BEGIN
    DECLARE user_id INT;
    DECLARE salt VARCHAR(60);

    -- Obtener el salt de la tabla de configuración
    SELECT value INTO salt FROM account_configuration WHERE `key` = 'SALT';
    
    -- Comprueba si el correo electrónico y la contraseña coinciden (utilizando el salt)
    SELECT id INTO user_id FROM users WHERE email = p_email AND password_hash = SHA2(CONCAT(p_password, salt), 256);

    IF user_id IS NOT NULL THEN
        -- Si el usuario es válido, muestra los detalles desde todas las tablas, incluyendo la foto de perfil
        SELECT 
            u.id, 
            u.email, 
            u.password_hash, 
            pd.full_name, 
            ur.is_active AS user_is_active, 
            ar.is_active AS admin_is_active,
            a.address_line1, 
            a.address_line2, 
            a.province, 
            a.canton, 
            a.postal_code,
            p.whatsapp, 
            p.other_numbers,
            pp.profile_picture
        FROM users u
        LEFT JOIN personal_data pd ON u.id = pd.user_id
        LEFT JOIN user_roles ur ON u.id = ur.user_id
        LEFT JOIN admin_roles ar ON u.id = ar.user_id
        LEFT JOIN address a ON u.id = a.user_id
        LEFT JOIN phones p ON u.id = p.user_id
        LEFT JOIN user_profilePicture pp ON u.id = pp.user_id
        WHERE u.id = user_id;
    ELSE
        -- Si el usuario no es válido, muestra un mensaje de error
        SELECT 'Invalid email or password' AS error_message;
    END IF;
    
END;
//
DELIMITER ;





-- Convierte un usuario regular en administrador y muestra información
DELIMITER //
CREATE PROCEDURE spGrantAdminRole(IN p_user_id INT)
BEGIN
    UPDATE admin_roles SET is_active = TRUE WHERE user_id = p_user_id;

    SELECT u.id, u.email, u.password_hash, pd.full_name, ur.is_active AS user_is_active, ar.is_active AS admin_is_active
    FROM users u
    LEFT JOIN personal_data pd ON u.id = pd.user_id
    LEFT JOIN user_roles ur ON u.id = ur.user_id
    LEFT JOIN admin_roles ar ON u.id = ar.user_id
    WHERE u.id = p_user_id;
END;
//
DELIMITER ;

-- Elimina el rol de administrador de un usuario y muestra información
DELIMITER //
CREATE PROCEDURE spRevokeAdminRole(IN p_user_id INT)
BEGIN
    UPDATE admin_roles SET is_active = FALSE WHERE user_id = p_user_id;

    SELECT u.id, u.email, u.password_hash, pd.full_name, ur.is_active AS user_is_active, ar.is_active AS admin_is_active
    FROM users u
    LEFT JOIN personal_data pd ON u.id = pd.user_id
    LEFT JOIN user_roles ur ON u.id = ur.user_id
    LEFT JOIN admin_roles ar ON u.id = ar.user_id
    WHERE u.id = p_user_id;
END;
//
DELIMITER ;

-- Otorga a un administrador el rol de usuario y muestra información
DELIMITER //
CREATE PROCEDURE spGrantUserRole(IN p_user_id INT)
BEGIN
    UPDATE user_roles SET is_active = TRUE WHERE user_id = p_user_id;

    SELECT u.id, u.email, u.password_hash, pd.full_name, ur.is_active AS user_is_active, ar.is_active AS admin_is_active
    FROM users u
    LEFT JOIN personal_data pd ON u.id = pd.user_id
    LEFT JOIN user_roles ur ON u.id = ur.user_id
    LEFT JOIN admin_roles ar ON u.id = ar.user_id
    WHERE u.id = p_user_id;
END;
//
DELIMITER ;

-- Elimina el rol de usuario de un administrador y muestra información
DELIMITER //
CREATE PROCEDURE spRevokeUserRole(IN p_user_id INT)
BEGIN
    UPDATE user_roles SET is_active = FALSE WHERE user_id = p_user_id;

    SELECT u.id, u.email, u.password_hash, pd.full_name, ur.is_active AS user_is_active, ar.is_active AS admin_is_active
    FROM users u
    LEFT JOIN personal_data pd ON u.id = pd.user_id
    LEFT JOIN user_roles ur ON u.id = ur.user_id
    LEFT JOIN admin_roles ar ON u.id = ar.user_id
    WHERE u.id = p_user_id;
END;
//
DELIMITER ;

-- Buscar un usuario por correo electrónico
DELIMITER //
CREATE PROCEDURE spFindUserByEmail(IN p_email VARCHAR(255))
BEGIN
    SELECT 
        u.id, 
        u.email, 
        u.password_hash, 
        pd.full_name, 
        ur.is_active AS user_is_active, 
        ar.is_active AS admin_is_active,
        a.address_line1, 
        a.address_line2, 
        a.province, 
        a.canton, 
        a.postal_code,
        p.whatsapp, 
        p.other_numbers,
        pp.profile_picture
    FROM users u
    LEFT JOIN personal_data pd ON u.id = pd.user_id
    LEFT JOIN user_roles ur ON u.id = ur.user_id
    LEFT JOIN admin_roles ar ON u.id = ar.user_id
    LEFT JOIN address a ON u.id = a.user_id
    LEFT JOIN phones p ON u.id = p.user_id
    LEFT JOIN user_profilePicture pp ON u.id = pp.user_id
    WHERE u.email = p_email;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE spUpdateUserPassword(IN p_email VARCHAR(255), IN p_new_password VARCHAR(255))
BEGIN
    DECLARE user_id INT;
    DECLARE salt VARCHAR(60);
    DECLARE rows_affected INT;

    -- Obtener el salt de la tabla de configuración
    SELECT value INTO salt FROM account_configuration WHERE `key` = 'SALT';

    -- Encontrar el ID del usuario basado en el correo electrónico
    SELECT id INTO user_id FROM users WHERE email = p_email;

    IF user_id IS NOT NULL THEN
        -- Actualizar la contraseña del usuario
        UPDATE users 
        SET password_hash = SHA2(CONCAT(p_new_password, salt), 256)
        WHERE id = user_id;

        -- Obtener el número de filas afectadas por la actualización
        SET rows_affected = ROW_COUNT();

        -- Imprimir un mensaje en función del resultado
        IF rows_affected = 0 THEN
            SELECT 'No se actualizó ninguna contraseña. La contraseña es la misma.' AS message;
        ELSE
            SELECT 'Contraseña actualizada correctamente.' AS message;
        END IF;
    ELSE
        SELECT 'No se encontró ningún usuario con ese correo electrónico.' AS message;
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE spFindAllUsers()
BEGIN
    SELECT 
        u.id, 
        u.email, 

        pd.full_name, 
        ur.is_active AS user_is_active, 
        ar.is_active AS admin_is_active,
        a.address_line1, 
        a.address_line2, 
        a.province, 
        a.canton, 
        a.postal_code,
        p.whatsapp, 
        p.other_numbers,
        pp.profile_picture
    FROM users u
    LEFT JOIN personal_data pd ON u.id = pd.user_id
    LEFT JOIN user_roles ur ON u.id = ur.user_id
    LEFT JOIN admin_roles ar ON u.id = ar.user_id
    LEFT JOIN address a ON u.id = a.user_id
    LEFT JOIN phones p ON u.id = p.user_id
    LEFT JOIN user_profilePicture pp ON u.id = pp.user_id;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE spFindAllUsersByAdmin(admin_id INT)
BEGIN
    -- Verificar si el usuario es un administrador
    DECLARE is_admin BOOLEAN;
    SELECT is_active INTO is_admin FROM admin_roles WHERE user_id = admin_id;

    IF is_admin THEN
        -- Si es administrador, mostrar la lista de todos los usuarios
        SELECT 
            u.id, 
            u.email, 

            pd.full_name, 
            ur.is_active AS user_is_active, 
            ar.is_active AS admin_is_active,
            a.address_line1, 
            a.address_line2, 
            a.province, 
            a.canton, 
            a.postal_code,
            p.whatsapp, 
            p.other_numbers,
            pp.profile_picture
        FROM users u
        LEFT JOIN personal_data pd ON u.id = pd.user_id
        LEFT JOIN user_roles ur ON u.id = ur.user_id
        LEFT JOIN admin_roles ar ON u.id = ar.user_id
        LEFT JOIN address a ON u.id = a.user_id
        LEFT JOIN phones p ON u.id = p.user_id
        LEFT JOIN user_profilePicture pp ON u.id = pp.user_id;
    ELSE
        -- Si no es administrador, mostrar un mensaje de error
        SELECT 'Acceso denegado: usuario no es administrador' AS error_message;
    END IF;
END;
//
DELIMITER ;




-- Ejemplo 1
CALL spInsertUser('ejemplo1@email.com', 'contrasena_hash1', 'Nombre Completo 1');

-- Ejemplo 2
CALL spInsertUser('ejemplo2@email.com', 'contrasena_hash2', 'Nombre Completo 2');



CALL spValidateUser('ejemplo1@email.com', 'contrasena_hash1');
CALL spValidateUser('ejemplo2@email.com', 'contrasena_hash2');




CALL spInsertAdmin('rvenegass28@gmail.com', 'AdminPassword123', 'Roberto Venegas');
CALL spValidateUser('rvenegass28@gmail.com', 'AdminPassword123');


CALL spGrantAdminRole(1);
CALL spRevokeAdminRole(1);
CALL spGrantUserRole(3);
CALL spRevokeUserRole(2);

SELECT * FROM personal_data;

-- Prueba para spFindUserByEmail
CALL spFindUserByEmail('ejemplo1@email.com');
CALL spFindUserByEmail('ejemplo2@email.com');
CALL spFindUserByEmail('rvenegass28@gmail.com');
CALL spFindUserByEmail('correo_no_existente@email.com');


-- ADDRESS PRUEBAS
-- CALL spDeleteAddress(1);

CALL spUpdateAddress(1, '456 Elm St', 'Suite 8', 'Alajuela', 'Occidental', '20202');
CALL spSelectAddress(1);


CALL spUpdatePhones(1, '+1234567890', '9876543210');


CALL spSelectPhones(1);


-- Pruebas de Profile Picture
UPDATE user_profilePicture SET profile_picture = 'nueva_imagen_blob' WHERE user_id = 3;
SELECT profile_picture FROM user_profilePicture WHERE user_id = 3;

CALL spFindAllUsers();

CALL spFindAllUsersByAdmin(3);

-- Reiniciar contrasena

-- CALL spUpdateUserPassword('ejemplo1@email.com', 'nuevaContrasena123');


-- SELECT email, password_hash FROM users WHERE email = 'ejemplo1@email.com';


