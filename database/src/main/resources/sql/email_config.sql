CREATE DATABASE IF NOT EXISTS email_config;

USE email_config;

DROP TABLE IF EXISTS email_config;

CREATE TABLE email_config (
    id INT AUTO_INCREMENT PRIMARY KEY,
    host VARBINARY(255) NOT NULL, -- Cambiado a VARBINARY para almacenar datos encriptados
    port VARBINARY(255) NOT NULL, -- Cambiado a VARBINARY para almacenar datos encriptados
    username VARBINARY(255) NOT NULL, -- Cambiado a VARBINARY para almacenar datos encriptados
    password VARBINARY(255) NOT NULL, -- Cambiado a VARBINARY para almacenar datos encriptados
    smtp_auth TINYINT(1) NOT NULL,
    starttls_enabled TINYINT(1) NOT NULL
);

DELIMITER //

CREATE PROCEDURE InsertEncryptedEmailConfig(
    IN p_host VARCHAR(255),
    IN p_port INT,
    IN p_username VARCHAR(255),
    IN p_password VARCHAR(255),
    IN p_smtp_auth TINYINT(1),
    IN p_starttls_enabled TINYINT(1)
)
BEGIN
    INSERT INTO email_config (host, port, username, password, smtp_auth, starttls_enabled)
    VALUES (
        AES_ENCRYPT(p_host, 'tu_clave_secreta'),
        AES_ENCRYPT(CAST(p_port AS CHAR), 'tu_clave_secreta'), 
        AES_ENCRYPT(p_username, 'tu_clave_secreta'), 
        AES_ENCRYPT(p_password, 'tu_clave_secreta'),
        p_smtp_auth,
        p_starttls_enabled
    );
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE SelectDecryptedEmailConfig()
BEGIN
    SELECT 
        CAST(AES_DECRYPT(host, 'tu_clave_secreta') AS CHAR(255)) AS host,
        CAST(CAST(AES_DECRYPT(port, 'tu_clave_secreta') AS CHAR) AS UNSIGNED) AS port,
        CAST(AES_DECRYPT(username, 'tu_clave_secreta') AS CHAR(255)) AS username,
        CAST(AES_DECRYPT(password, 'tu_clave_secreta') AS CHAR(255)) AS password,
        smtp_auth, 
        starttls_enabled
    FROM email_config;
END;
//
DELIMITER ;

-- Llama al stored procedure para insertar datos encriptados -- outn rrid aonl wdjx de Kevin
CALL InsertEncryptedEmailConfig('smtp.gmail.com', 587, 'rvenegass28@gmail.com', 'abvv ezlz eabj sovg', 1, 1);
DROP PROCEDURE IF EXISTS InsertEncryptedEmailConfig;

-- Llama al stored procedure para obtener datos desencriptados
-- CALL SelectDecryptedEmailConfig();


-- Luego, puedes seleccionar los datos de la tabla para ver los resultados encriptados
SELECT * FROM email_config;
