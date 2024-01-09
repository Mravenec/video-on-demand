CREATE DATABASE manual_video_access;

USE manual_video_access;

CREATE TABLE video_accesses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    admin_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DELIMITER //

CREATE PROCEDURE spInsertVideoAccess(IN p_email VARCHAR(255), IN p_admin_id INT)
BEGIN
    DECLARE isAdmin BOOLEAN;
    DECLARE existentAccessId INT;
    DECLARE isActiveAccess BOOLEAN;

    -- Verificar si el usuario es un administrador en la tabla admin_roles
    SELECT is_active INTO isAdmin FROM users.admin_roles WHERE user_id = p_admin_id;

    IF isAdmin THEN
        -- Verificar si ya existe un acceso de video con el mismo correo electrónico
        SELECT id, is_active INTO existentAccessId, isActiveAccess FROM video_accesses WHERE email = p_email;

        IF existentAccessId IS NOT NULL THEN
            -- Si ya existe, verificar si está activo o no
            IF NOT isActiveAccess THEN
                -- Si está inactivo, reactivarlo
                UPDATE video_accesses SET is_active = TRUE WHERE id = existentAccessId;
                SELECT 'Acceso de video reactivado correctamente.' AS Message;
            ELSE
                -- Si está activo, no hacer nada
                SELECT 'El acceso de video ya está activo.' AS Message;
            END IF;
        ELSE
            -- Si no existe, insertar el nuevo acceso de video
            INSERT INTO video_accesses (email, admin_id) VALUES (p_email, p_admin_id);
            SELECT 'Acceso de video insertado correctamente.' AS Message;
        END IF;
    ELSE
        -- Si el usuario no es administrador, mostrar un mensaje de error
        SELECT 'Error: El usuario no es administrador.' AS ErrorMessage;
    END IF;
END;

//
DELIMITER ;


DELIMITER //

CREATE PROCEDURE spDeactivateVideoAccess(IN p_access_id INT, IN p_admin_id INT)
BEGIN
    DECLARE isAdmin BOOLEAN;

    -- Verificar si el usuario es un administrador en la tabla admin_roles
    SELECT is_active INTO isAdmin FROM users.admin_roles WHERE user_id = p_admin_id;

    IF isAdmin THEN
        -- Si el usuario es administrador, actualizar el estado de is_active a FALSE
        UPDATE manual_video_access.video_accesses
        SET is_active = FALSE
        WHERE id = p_access_id;

        SELECT 'Acceso de video desactivado correctamente.' AS Message;
    ELSE
        -- Si el usuario no es administrador, mostrar un mensaje de error
        SELECT 'Error: El usuario no es administrador.' AS ErrorMessage;
    END IF;
END;

//
DELIMITER ;


DELIMITER //


CREATE PROCEDURE spGetActiveVideoAccessUsers(IN p_admin_id INT)
BEGIN
    DECLARE isAdmin BOOLEAN;
    DECLARE admin_email VARCHAR(255);

    -- Obtener el correo electrónico del administrador de la base de datos 'users'
    SELECT email INTO admin_email FROM users.users WHERE id = p_admin_id;

    -- Verificar si el usuario es un administrador en la tabla admin_roles
    SELECT is_active INTO isAdmin FROM users.admin_roles WHERE user_id = p_admin_id;

    IF isAdmin THEN
        -- Si el usuario es administrador, seleccionar todos los usuarios activos de video_accesses
        SELECT m.id,
		  m.email,
		  m.is_active, 
		  admin_email,
		  m.created_at,
		  m.updated_at 
        FROM manual_video_access.video_accesses m
        WHERE is_active = TRUE;
    ELSE
        -- Si el usuario no es administrador, mostrar un mensaje de error
        SELECT 'Error: El usuario no es administrador.' AS ErrorMessage;
    END IF;
END;

//
DELIMITER ;


DELIMITER //

CREATE PROCEDURE spUpdateVideoAccessEmail(IN p_access_id INT, IN p_new_email VARCHAR(255), IN p_admin_id INT)
BEGIN
    DECLARE isAdmin BOOLEAN;
    DECLARE emailExists BOOLEAN;

    -- Verificar si el usuario es un administrador en la tabla admin_roles
    SELECT is_active INTO isAdmin FROM users.admin_roles WHERE user_id = p_admin_id;

    IF isAdmin THEN
        -- Verificar si el nuevo correo electrónico ya está en uso
        SELECT EXISTS(SELECT 1 FROM video_accesses WHERE email = p_new_email AND id != p_access_id) INTO emailExists;

        IF emailExists THEN
            -- Si el correo electrónico ya está en uso, mostrar un mensaje de error
            SELECT 'Error: El correo electrónico ya está en uso.' AS ErrorMessage;
        ELSE
            -- Si el correo electrónico no está en uso, actualizar el registro
            UPDATE video_accesses
            SET email = p_new_email
            WHERE id = p_access_id;

            SELECT 'Correo electrónico de acceso de video actualizado correctamente.' AS Message;
        END IF;
    ELSE
        -- Si el usuario no es administrador, mostrar un mensaje de error
        SELECT 'Error: El usuario no es administrador.' AS ErrorMessage;
    END IF;
END;

//
DELIMITER ;

DELIMITER //

CREATE PROCEDURE spGetAllActiveVideoAccessUsers()
BEGIN
    -- Seleccionar todos los usuarios activos de video_accesses
    SELECT id, email, is_active, admin_id, created_at, updated_at
    FROM manual_video_access.video_accesses
    WHERE is_active = TRUE;
END;

//
DELIMITER ;

DELIMITER //

CREATE PROCEDURE spGetActiveVideoAccessEmails()
BEGIN
    -- Seleccionar los correos electrónicos de los usuarios activos en video_accesses
    SELECT email
    FROM manual_video_access.video_accesses
    WHERE is_active = TRUE;
END;

//
DELIMITER ;

CALL spGetActiveVideoAccessEmails();


-- CALL spInsertVideoAccess('correo@example.com', 3);
-- CALL spInsertVideoAccess('correo1@example.com', 3);

-- CALL spInsertVideoAccess('ejemplo1@email.com', 3);

-- CALL spDeactivateVideoAccess(1, 3);

-- CALL  spGetActiveVideoAccessUsers(3); -- Supongamos que el administrador es el usuario con ID 3

-- CALL spGetAllActiveVideoAccessUsers();


-- CALL spUpdateVideoAccessEmail(1, 'nuevo_correo@example.com', 3);

