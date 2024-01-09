CREATE DATABASE videos;

USE videos;

-- Tabla para almacenar las secciones
CREATE TABLE IF NOT EXISTS sections (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    added_by INT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    sequence_number INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (added_by) REFERENCES users.users(id)
);

-- Tabla para almacenar los videos
CREATE TABLE IF NOT EXISTS videos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(2048) NOT NULL UNIQUE,
    content TEXT,
    sequence_number INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    section_id INT,
    added_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (section_id) REFERENCES sections(id),
    FOREIGN KEY (added_by) REFERENCES users.users(id)
);

-- Tabla para rastrear qué videos han sido vistos por cada usuario
CREATE TABLE IF NOT EXISTS user_video_watched (
    user_id INT NOT NULL,
    video_id INT NOT NULL,
    watched BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users.users(id),
    FOREIGN KEY (video_id) REFERENCES videos(id),
    PRIMARY KEY (user_id, video_id)
);

-- Procedimiento para obtener todos los videos agrupados por secciones y filtrados por usuario
DELIMITER //
CREATE PROCEDURE spGetAllVideosBySectionsAndUser(IN p_user_id INT)
BEGIN
    SET SESSION group_concat_max_len = 1000000;

    SELECT 
        s.id AS section_id,
        s.name AS section_name,
        s.sequence_number AS section_sequence_number,
        s.created_at AS section_created_at,
        s.is_active AS section_is_active,
        CASE
            WHEN COUNT(v.id) = 0 THEN NULL
            ELSE 
                GROUP_CONCAT(
                    JSON_OBJECT(
                        'id', v.id,
                        'title', v.title,
                        'url', v.url,
                        'content', v.content,
                        'sequence_number', v.sequence_number,
                        'created_at', v.created_at,
                        'is_active', v.is_active,
                        'is_watched', IFNULL(uvw.watched, FALSE)
                    )
                    ORDER BY v.sequence_number ASC, v.created_at ASC
                )
        END AS videos
    FROM 
        sections AS s
    LEFT JOIN 
        videos AS v ON s.id = v.section_id AND v.is_active = TRUE
    LEFT JOIN 
        user_video_watched AS uvw ON v.id = uvw.video_id AND uvw.user_id = p_user_id
    WHERE
        s.is_active = TRUE
    GROUP BY 
        s.id, s.name, s.sequence_number, s.created_at, s.is_active
    ORDER BY
        s.sequence_number ASC, s.created_at ASC;
END;
//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE spGetAllVideosGroupedBySections()
BEGIN
    -- Aumenta el límite para GROUP_CONCAT si es necesario
    SET SESSION group_concat_max_len = 1000000;
    
    SELECT 
        s.id AS section_id,
        s.name AS section_name,
        s.sequence_number AS section_sequence_number,
        s.created_at AS section_created_at,
        s.is_active AS section_is_active,
        CASE
            WHEN COUNT(v.id) = 0 THEN NULL
            ELSE 
                COALESCE(
                    GROUP_CONCAT(
                        JSON_OBJECT(
                            'id', v.id,
                            'title', v.title,
                            'url', v.url,
                            'content', v.content,
                            'sequence_number', v.sequence_number,
                            'created_at', v.created_at,
                            'is_active', v.is_active
                            -- 'is_watched' ha sido removido de aquí
                        )
                        ORDER BY v.sequence_number ASC, v.created_at ASC
                    )
                )
        END AS videos
    FROM 
        sections AS s
    LEFT JOIN 
        videos AS v ON s.id = v.section_id AND v.is_active = TRUE
    WHERE
        s.is_active = TRUE
    GROUP BY 
        s.id, s.name, s.sequence_number, s.created_at, s.is_active
    ORDER BY
        s.sequence_number ASC, s.created_at ASC;
END;
//
DELIMITER ;



DELIMITER //
CREATE PROCEDURE spCreateSection(IN p_name VARCHAR(255), IN p_admin_id INT)
BEGIN
    DECLARE admin_is_active BOOLEAN;
    DECLARE existing_name_count INT;
    DECLARE next_sequence_number INT;
    
    -- Verificar si el ID pertenece a un administrador activo
    SELECT is_active INTO admin_is_active FROM users.admin_roles WHERE user_id = p_admin_id;

    IF admin_is_active THEN
        -- Verificar si el nombre ya existe en la tabla
        SELECT COUNT(*) INTO existing_name_count FROM videos.sections WHERE name = p_name;
        
        IF existing_name_count = 0 THEN
            -- Obtener el siguiente número de secuencia
            SELECT COALESCE(MAX(sequence_number), 0) + 1 INTO next_sequence_number FROM videos.sections;
            
            -- Insertar la nueva sección con el siguiente número de secuencia y el timestamp actual
            INSERT INTO videos.sections (name, added_by, is_active, sequence_number, created_at) 
            VALUES (p_name, p_admin_id, TRUE, next_sequence_number, CURRENT_TIMESTAMP);
            
            SELECT 'Sección agregada correctamente' AS message;  -- Agregar mensaje de confirmación
        ELSE
            SELECT 'Error: Duplicate section name' AS message;
        END IF;
    ELSE
        SELECT 'Permission denied: Not an active admin' AS message;
    END IF;
    
END;
//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE spInsertVideo(
    IN p_title VARCHAR(255),
    IN p_url VARCHAR(2048),
    IN p_content TEXT,
    IN p_sequence_number INT,
    IN p_section_id INT,
    IN p_admin_id INT
)
BEGIN
    DECLARE admin_is_active BOOLEAN;
    DECLARE duplicate_url INT;
    
    -- Verificar si el ID pertenece a un administrador activo
    SELECT is_active INTO admin_is_active
    FROM users.admin_roles
    WHERE user_id = p_admin_id;
    
    IF admin_is_active THEN
        -- Verificar si la URL ya existe en la tabla de videos
        SELECT COUNT(*) INTO duplicate_url
        FROM videos
        WHERE url = p_url;
        
        IF duplicate_url = 0 THEN
            IF p_sequence_number IS NULL THEN
                -- Obtener el número de secuencia máximo en la misma sección y sumar 1
                SELECT COALESCE(MAX(sequence_number), 0) + 1 INTO p_sequence_number
                FROM videos
                WHERE section_id = p_section_id;
            END IF;
            
            INSERT INTO videos (
                title,
                url,
                content,
                sequence_number,
                is_active,
                section_id,
                added_by
            ) VALUES (
                p_title,
                p_url,
                p_content,
                p_sequence_number,
                TRUE,
                p_section_id,
                p_admin_id
            );
            
            SELECT 'Video agregado correctamente' AS message; -- Agregar mensaje de confirmación
        ELSE
            SELECT 'Error: URL already exists' AS message;
        END IF;
    ELSE
        SELECT 'Permission denied: Not an active admin' AS message;
    END IF;
    
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE spUpdateSectionSequence(IN p_section_id INT, IN new_sequence_number INT, IN p_admin_id INT)
BEGIN
    DECLARE old_sequence_number INT;
    DECLARE admin_is_active BOOLEAN;

    -- Verificar si el ID pertenece a un administrador activo
    SELECT is_active INTO admin_is_active
    FROM users.admin_roles
    WHERE user_id = p_admin_id;

    IF admin_is_active THEN
        SELECT sequence_number INTO old_sequence_number FROM sections WHERE id = p_section_id;

        IF old_sequence_number = new_sequence_number THEN
            SELECT 'No changes needed';
        ELSE
            IF old_sequence_number < new_sequence_number THEN
                UPDATE sections SET sequence_number = sequence_number - 1 WHERE sequence_number > old_sequence_number AND sequence_number <= new_sequence_number;
            ELSE
                UPDATE sections SET sequence_number = sequence_number + 1 WHERE sequence_number >= new_sequence_number AND sequence_number < old_sequence_number;
            END IF;

            UPDATE sections SET sequence_number = new_sequence_number WHERE id = p_section_id;

            SELECT 'Sequence updated successfully';
        END IF;
    ELSE
        SELECT 'Permission denied: Not an active admin' AS message;
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE spUpdateVideoSequence(
    IN p_section_id INT,
    IN p_video_id INT, 
    IN p_new_sequence_number INT,
    IN p_admin_id INT 
)
BEGIN
    DECLARE v_old_sequence_number INT;
    DECLARE v_new_sequence_number INT;
    DECLARE admin_is_active BOOLEAN;

    -- Verificar si el ID pertenece a un administrador activo
    SELECT is_active INTO admin_is_active
    FROM users.admin_roles
    WHERE user_id = p_admin_id;

    IF admin_is_active THEN
        SET v_new_sequence_number = p_new_sequence_number;

        -- Obtener el antiguo número de secuencia del video
        SELECT sequence_number INTO v_old_sequence_number 
        FROM videos 
        WHERE id = p_video_id AND section_id = p_section_id;

        -- No hacer nada si el nuevo número de secuencia es el mismo que el antiguo
        IF v_old_sequence_number = v_new_sequence_number THEN
            SELECT 'No changes needed';
        ELSE
            -- Actualizar secuencias
            IF v_old_sequence_number < v_new_sequence_number THEN
                UPDATE videos 
                SET sequence_number = sequence_number - 1 
                WHERE sequence_number > v_old_sequence_number 
                AND sequence_number <= v_new_sequence_number
                AND section_id = p_section_id;
            ELSE
                UPDATE videos 
                SET sequence_number = sequence_number + 1 
                WHERE sequence_number >= v_new_sequence_number 
                AND sequence_number < v_old_sequence_number
                AND section_id = p_section_id;
            END IF;

            -- Establecer el nuevo número de secuencia para el video seleccionado
            UPDATE videos 
            SET sequence_number = v_new_sequence_number 
            WHERE id = p_video_id AND section_id = p_section_id;

            SELECT 'Sequence updated successfully';
        END IF;
    ELSE
        SELECT 'Permission denied: Not an active admin' AS message;
    END IF;
END;
//
DELIMITER ;


-- Primero, insertamos una sección para tener un punto de referencia
CALL spCreateSection('Introducción', 3);  -- Suponiendo que 3 es un admin_id válido
CALL spCreateSection('Módulo 1 Captación de la Oportunidad', 3); 
CALL spCreateSection('Módulo 2 Negociación de la Oportunidad', 3);  -- Suponiendo que 3 es un admin_id válido
CALL spCreateSection('Módulo 3 Monetización', 3);

-- Luego, insertamos un video en esa sección
CALL spInsertVideo('BIENVENIDA', 'https://vimeo.com/896306796?share=copy', 'Contenido del video 1', 1, 1, 3);  -- Suponiendo que 1 es un admin_id válido y 1 es un section_id válido
CALL spInsertVideo('Estructura de negocio', 'https://vimeo.com/896306953?share=copy', 'Contenido del video 2', 2, 1, 3);  -- Suponiendo que 1 es un admin_id válido y 1 es un section_id válido
CALL spInsertVideo('Lo que necesitas para iniciar', 'https://vimeo.com/896307919?share=copy', 'Contenido del video 3', 3, 1, 3);
CALL spInsertVideo('Mi historia', 'https://vimeo.com/896308935?share=copy', 'Contenido del video 4', 4, 1, 3);

CALL spInsertVideo('Captacion Oportunidad', 'https://vimeo.com/896306881?share=copy', 'Contenido del video 5', 1, 2, 3);
CALL spInsertVideo('Metodo 1 Publicidad Digital', 'https://vimeo.com/896310151?share=copy', 'Contenido del video 6', 2, 2, 3);
CALL spInsertVideo('Metodo 2 Lista Caliente', 'https://vimeo.com/896311328?share=copy', 'Contenido del video 7', 3, 2, 3);
CALL spInsertVideo('Metodo 3 Remates', 'https://vimeo.com/896314159?share=copy', 'Contenido del video 8', 4, 2, 3);
CALL spInsertVideo('Metodo 4 Gruas', 'https://vimeo.com/896314159?share=copy', 'Contenido del video 9', 5, 2, 3);
CALL spInsertVideo('Metodo 5 Visitar Talleres', 'https://vimeo.com/896314159?share=copy', 'Contenido del video 10', 6, 2, 3);
CALL spInsertVideo('Metodo 6 Captación en el momento', 'https://vimeo.com/896319338?share=copy', 'Contenido del video 11', 7, 2, 3);
CALL spInsertVideo('Metodo 7 App 0 Riesgo', 'https://vimeo.com/896319338?share=copy', 'Contenido del video 12', 8, 2, 3);
CALL spInsertVideo('Metodo 8 Rent a Car', 'https://vimeo.com/896322938?share=copy', 'Contenido del video 13', 9, 2, 3);
CALL spInsertVideo('Metodo 9 Referidos', 'https://vimeo.com/896323560?share=copy', 'Contenido del video 14', 10, 2, 3);

CALL spInsertVideo('Cálculo de Salvamiento', 'https://vimeo.com/896307033?share=copy', 'Contenido del video 15', 1, 3, 3);
CALL spInsertVideo('Cuando no es negocio', 'https://vimeo.com/896309728?share=copy', 'Contenido del video 16', 2, 3, 3);
CALL spInsertVideo('Finalización del trámite', 'https://vimeo.com/896311069?share=copy', 'Contenido del video 17', 3, 3, 3);
CALL spInsertVideo('Lista de Contactos', 'https://vimeo.com/896312153?share=copy', 'Contenido del video 18', 4, 3, 3);
CALL spInsertVideo('Llamada de Oferta', 'https://vimeo.com/896313527?share=copy', 'Contenido del video 19', 5, 3, 3);
CALL spInsertVideo('Manejo de Objeciones', 'https://vimeo.com/896314533?share=copy', 'Contenido del video 20', 6, 3, 3);
CALL spInsertVideo('Preguntas clave', 'https://vimeo.com/896316329?share=copy', 'Contenido del video 21', 7, 3, 3);
CALL spInsertVideo(' Seguimiento', 'https://vimeo.com/896325218?share=copy', 'Contenido del video 22', 8, 3, 3);

CALL spInsertVideo('Buscar Taller', 'https://vimeo.com/896307157?share=copy', 'Contenido del video 23', 1, 4, 3);
CALL spInsertVideo('Checklist', 'https://vimeo.com/896312530?share=copy', 'Contenido del video 24', 2, 4, 3);
CALL spInsertVideo('Comunidad', 'https://vimeo.com/896312967?share=copy', 'Contenido del video 25', 3, 4, 3);
CALL spInsertVideo('Contactos Extranjero', 'https://vimeo.com/896314238?share=copy', 'Contenido del video 26', 4, 4, 3);
CALL spInsertVideo('Cotización de repuestos', 'https://vimeo.com/896314535?share=copy', 'Contenido del video 27', 5, 4, 3);
CALL spInsertVideo('Diego Venegas', 'https://vimeo.com/896327439?share=copy', 'Contenido del video 28', 6, 4, 3);
CALL spInsertVideo('Opción 1 Comisión', 'https://vimeo.com/896327666?share=copy', 'Contenido del video 29', 7, 4, 3);
CALL spInsertVideo('Opción 2 Comisión y Diferencial', 'https://vimeo.com/896327918?share=copy', 'Contenido del video 30', 8, 4, 3);
CALL spInsertVideo('Opcion 3 Contado, Crédito, Inversionista', 'https://vimeo.com/896328483?share=copy', 'Contenido del video 31', 9, 4, 3);
CALL spInsertVideo('Panamá', 'https://vimeo.com/896328995?share=copy', 'Contenido del video 32', 10, 4, 3);
CALL spInsertVideo('Proceso de Venta', 'https://vimeo.com/896329103?share=copy', 'Contenido del video 33', 11, 4, 3);
CALL spInsertVideo('Reparación', 'https://vimeo.com/896329462?share=copy', 'Contenido del video 34', 12, 4, 3);
CALL spInsertVideo('Repuestos en Alibaba', 'https://vimeo.com/896329734?share=copy', 'Contenido del video 35', 13, 4, 3);
CALL spInsertVideo('Traer repuestos de manera virtual', 'https://vimeo.com/896330111?share=copy', 'Contenido del video 36', 14, 4, 3);

-- CALL spUpdateSectionSequence(1, 1,3);

-- CALL spUpdateVideoSequence(1, 2, 2,3);
-- CALL spUpdateVideoSequence(3, 5, 2,3);


SELECT * FROM videos;
SELECT * FROM sections;

CALL spGetAllVideosGroupedBySections();
CALL spGetAllVideosBySectionsAndUser(1);






