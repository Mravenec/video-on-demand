package org.tuprimernegocio.tuprimernegocio.videos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuprimernegocio.library.database.jooq.videos.routines.Spcreatesection;
import com.tuprimernegocio.library.database.jooq.videos.routines.Spgetallvideosgroupedbysections;
import com.tuprimernegocio.library.database.jooq.videos.routines.Spgetallvideosbysectionsanduser;
import com.tuprimernegocio.library.database.jooq.videos.routines.Spinsertvideo;
import com.tuprimernegocio.library.database.jooq.videos.routines.Spupdatesectionsequence;
import com.tuprimernegocio.library.database.jooq.videos.routines.Spupdatevideosequence;
import com.tuprimernegocio.library.database.jooq.videos.tables.Sections;
import com.tuprimernegocio.library.database.jooq.videos.tables.Videos;

import static com.tuprimernegocio.library.database.jooq.videos.tables.Sections.SECTIONS;
import static com.tuprimernegocio.library.database.jooq.videos.tables.UserVideoWatched.USER_VIDEO_WATCHED;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import static com.tuprimernegocio.library.database.jooq.videos.tables.Videos.VIDEOS_;
import com.fasterxml.jackson.core.type.TypeReference;

@Repository
public class VideoRepository {

    @Autowired
    private DSLContext dslContext;

    public Map<String, Object> createSection(String name, Integer adminId) {
        Spcreatesection spcreatesection = new Spcreatesection();
        spcreatesection.setPName(name);
        spcreatesection.setPAdminId(adminId);

        // Execute the procedure.
        spcreatesection.execute(dslContext.configuration());

        Result<Record> records = spcreatesection.getResults().get(0);

        Map<String, Object> result = new HashMap<>();
        if (records.isNotEmpty()) {
            Record record = records.get(0); // Taking the first record, adjust as needed
            for (var field : record.fields()) {
                result.put(field.getName(), record.get(field));
            }
        }

        return result;
    }

    public List<Map<String, Object>> getAllVideosGroupedBySections() {

        // Create an instance of the procedure
        Spgetallvideosgroupedbysections procedure = new Spgetallvideosgroupedbysections();

        // Use DSLContext to execute the procedure
        procedure.execute(this.dslContext.configuration());

        // Once executed, the procedure should have completed its "Results" collection
        Result<?> result = procedure.getResults().get(0);

        // Convert the records into a list of maps
        List<Map<String, Object>> recordsList = result.stream()
                .map(record -> {
                    Map<String, Object> map = record.intoMap();
                    // Convert the "videos" string into a list of maps
                    String videosJson = (String) map.get("videos");
                    if (videosJson != null) {
                        List<Map<String, Object>> videosList = Arrays.stream(videosJson.split(",(?=\\{)"))
                                .map(str -> {
                                    try {
                                        // Especifica el tipo de mapa esperado (en este caso, String como clave y Object
                                        // como valor)
                                        TypeReference<HashMap<String, Object>> mapType = new TypeReference<HashMap<String, Object>>() {
                                        };
                                        return new ObjectMapper().readValue(str, mapType);
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .collect(Collectors.toList());
                        map.put("videos", videosList);
                    }
                    return map;
                })
                .collect(Collectors.toList());

        return recordsList;
    }

    public Map<String, Object> insertVideo(String title, String url, String content, Integer sequenceNumber,
            Integer sectionId, Integer adminId) {
        Spinsertvideo spInsertVideoProcedure = new Spinsertvideo();

        spInsertVideoProcedure.setPUrl(url);
        spInsertVideoProcedure.setPContent(content);
        // Verificar y ajustar el valor sequenceNumber si es null
        if (sequenceNumber == null) {
            // Realiza una consulta para obtener el máximo sequenceNumber en la misma
            // sección
            Integer maxSequenceNumber = dslContext
                    .select(DSL.max(VIDEOS_.SEQUENCE_NUMBER))
                    .from(VIDEOS_)
                    .where(VIDEOS_.SECTION_ID.eq(sectionId))
                    .fetchOneInto(Integer.class);

            // Ajusta el sequenceNumber en función del resultado de la consulta
            sequenceNumber = (maxSequenceNumber != null) ? maxSequenceNumber + 1 : 1;
        }

        spInsertVideoProcedure.setPTitle(title); // Agregar el título del video
        spInsertVideoProcedure.setPSequenceNumber(sequenceNumber);
        spInsertVideoProcedure.setPSectionId(sectionId);
        spInsertVideoProcedure.setPAdminId(adminId);

        spInsertVideoProcedure.execute(dslContext.configuration());

        Result<Record> records = spInsertVideoProcedure.getResults().get(0);

        Map<String, Object> result = new HashMap<>();
        if (!records.isEmpty()) {
            Record record = records.get(0);
            for (var field : record.fields()) {
                result.put(field.getName(), record.get(field));
            }
        }

        return result;
    }

    public void updateSectionSequence(Integer sectionId, Integer newSequenceNumber, Integer addedBy) {
        Spupdatesectionsequence spUpdateSectionSequence = new Spupdatesectionsequence();
        spUpdateSectionSequence.setPSectionId(sectionId);
        spUpdateSectionSequence.setNewSequenceNumber(newSequenceNumber);
        spUpdateSectionSequence.setPAdminId(addedBy);

        spUpdateSectionSequence.execute(dslContext.configuration());
    }

    public void updateVideoSequence(Integer sectionId, Integer videoId, Integer newSequenceNumber, Integer addedBy) {
        Spupdatevideosequence spUpdateVideoSequence = new Spupdatevideosequence();
        spUpdateVideoSequence.setPSectionId(sectionId);
        spUpdateVideoSequence.setPVideoId(videoId);
        spUpdateVideoSequence.setPNewSequenceNumber(newSequenceNumber);
        spUpdateVideoSequence.setPAdminId(addedBy);

        spUpdateVideoSequence.execute(dslContext.configuration());
    }

    public void updateSectionName(Integer sectionId, String newName) {
        // Utiliza DSLContext para crear la sentencia de actualización
        dslContext.update(SECTIONS)
                .set(SECTIONS.NAME, newName)
                .where(SECTIONS.ID.eq(sectionId))
                .execute();
    }

    public void updateVideoTitle(Integer videoId, String newTitle) {
        // Utiliza DSLContext para crear la sentencia de actualización
        dslContext.update(VIDEOS_)
                .set(VIDEOS_.TITLE, newTitle)
                .where(VIDEOS_.ID.eq(videoId))
                .execute();
    }

    public void deactivateVideo(int videoId) {
        // Genera código JOOQ para desactivar un video por su ID
        dslContext.update(Videos.VIDEOS_)
                .set(Videos.VIDEOS_.IS_ACTIVE, (byte) 0)
                .where(Videos.VIDEOS_.ID.eq(videoId))
                .execute();
    }

    public void deactivateSection(int sectionId) {
        // Genera código JOOQ para desactivar una sección por su ID
        dslContext.update(Sections.SECTIONS)
                .set(Sections.SECTIONS.IS_ACTIVE, (byte) 0)
                .where(Sections.SECTIONS.ID.eq(sectionId))
                .execute();
    }

    public void updateVideoUrl(int videoId, String newUrl) {
        // Utiliza DSLContext para crear la sentencia de actualización
        int updatedRows = dslContext.update(VIDEOS_)
                .set(VIDEOS_.URL, newUrl)
                .where(VIDEOS_.ID.eq(videoId))
                .execute();
    }

    public void updateVideoDescription(Integer videoId, String newDescription) {
        // Utiliza DSLContext para crear la sentencia de actualización
        dslContext.update(VIDEOS_)
                .set(VIDEOS_.CONTENT, newDescription)
                .where(VIDEOS_.ID.eq(videoId))
                .execute();
    }
    
    public void markVideoAsWatched(Integer userId, Integer videoId) {
        dslContext.insertInto(USER_VIDEO_WATCHED)
                  .set(USER_VIDEO_WATCHED.USER_ID, userId)
                  .set(USER_VIDEO_WATCHED.VIDEO_ID, videoId)
                  .set(USER_VIDEO_WATCHED.WATCHED, (byte) 1) // Marca como visto
                  .onDuplicateKeyUpdate()
                  .set(USER_VIDEO_WATCHED.WATCHED, (byte) 1) // Actualiza si ya existe
                  .execute();
    }
    

    public void unmarkVideoAsWatched(Integer userId, Integer videoId) {
        dslContext.insertInto(USER_VIDEO_WATCHED)
                  .set(USER_VIDEO_WATCHED.USER_ID, userId)
                  .set(USER_VIDEO_WATCHED.VIDEO_ID, videoId)
                  .set(USER_VIDEO_WATCHED.WATCHED, (byte) 0) // Marca como no visto
                  .onDuplicateKeyUpdate()
                  .set(USER_VIDEO_WATCHED.WATCHED, (byte) 0) // Actualiza si ya existe
                  .execute();
    }

    public List<Map<String, Object>> getAllVideosBySectionsAndUser(Integer userId) {
        // Crear una instancia del procedimiento almacenado
        Spgetallvideosbysectionsanduser procedure = new Spgetallvideosbysectionsanduser();
        procedure.setPUserId(userId); // Establecer el parámetro del usuario

        // Ejecutar el procedimiento
        procedure.execute(this.dslContext.configuration());

        // Una vez ejecutado, recoger los resultados
        Result<?> result = procedure.getResults().get(0);

        // Convertir los registros en una lista de mapas
        List<Map<String, Object>> recordsList = result.stream()
                .map(record -> {
                    Map<String, Object> map = record.intoMap();
                    // Convertir la cadena "videos" en una lista de mapas
                    String videosJson = (String) map.get("videos");
                    if (videosJson != null) {
                        List<Map<String, Object>> videosList = Arrays.stream(videosJson.split(",(?=\\{)"))
                                .map(str -> {
                                    try {
                                        // Especificar el tipo de mapa esperado
                                        TypeReference<HashMap<String, Object>> mapType = new TypeReference<HashMap<String, Object>>() {};
                                        return new ObjectMapper().readValue(str, mapType);
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .collect(Collectors.toList());
                        map.put("videos", videosList);
                    }
                    return map;
                })
                .collect(Collectors.toList());

        return recordsList;
    }
    

}
