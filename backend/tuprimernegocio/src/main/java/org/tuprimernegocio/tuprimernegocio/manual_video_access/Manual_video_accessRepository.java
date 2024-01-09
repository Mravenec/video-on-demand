package org.tuprimernegocio.tuprimernegocio.manual_video_access;

import com.tuprimernegocio.library.database.jooq.manual_video_access.routines.Spdeactivatevideoaccess;
import com.tuprimernegocio.library.database.jooq.manual_video_access.routines.Spgetactivevideoaccessemails;
import com.tuprimernegocio.library.database.jooq.manual_video_access.routines.Spgetactivevideoaccessusers;
import com.tuprimernegocio.library.database.jooq.manual_video_access.routines.Spgetallactivevideoaccessusers;
import com.tuprimernegocio.library.database.jooq.manual_video_access.routines.Spinsertvideoaccess;
import com.tuprimernegocio.library.database.jooq.manual_video_access.routines.Spupdatevideoaccessemail;
import com.tuprimernegocio.library.database.jooq.manual_video_access.tables.pojos.VideoAccesses;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Manual_video_accessRepository {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private org.jooq.Configuration configuration;

    public List<ValidateVideoAccessResult> fetchActiveVideoAccesses(Integer adminId) {
        Spgetactivevideoaccessusers procedure = new Spgetactivevideoaccessusers();
        procedure.setPAdminId(adminId);

        // Ejecutar la rutina almacenada
        procedure.execute(configuration);

        // Obtener los resultados
        List<ValidateVideoAccessResult> videoAccessResults = new ArrayList<>();
        Result<Record> results = procedure.getResults().get(0);
        for (Record record : results) {
            ValidateVideoAccessResult result = new ValidateVideoAccessResult();

            result.id = record.getValue("id", Integer.class);
            result.email = record.getValue("email", String.class);
            result.isActive = byteToBoolean(record.getValue("is_active", Byte.class));
            // Cambiar "admin_id" a "admin_email" para reflejar el campo real en el
            // procedimiento almacenado
            result.adminEmail = record.getValue("admin_email", String.class);
            result.createdAt = record.getValue("created_at", Timestamp.class);
            result.updatedAt = record.getValue("updated_at", Timestamp.class);

            videoAccessResults.add(result);
        }

        return videoAccessResults;
    }

    public Map<String, Object> updateVideoAccessEmail(Integer accessId, String newEmail, Integer adminId) {
        Spupdatevideoaccessemail procedure = new Spupdatevideoaccessemail();
        procedure.setPAccessId(accessId);
        procedure.setPNewEmail(newEmail);
        procedure.setPAdminId(adminId);

        // Ejecutar el procedimiento almacenado
        procedure.execute(configuration);

        // Obtener y procesar los resultados
        Result<Record> records = procedure.getResults().get(0);
        Map<String, Object> result = new HashMap<>();
        if (!records.isEmpty()) {
            Record record = records.get(0);
            for (var field : record.fields()) {
                result.put(field.getName(), record.get(field));
            }
        }
        return result;
    }

    public Map<String, Object> deactivateVideoAccess(Integer accessId, Integer adminId) {
    Spdeactivatevideoaccess procedure = new Spdeactivatevideoaccess();
    procedure.setPAccessId(accessId);
    procedure.setPAdminId(adminId);

    // Ejecutar el procedimiento almacenado
    procedure.execute(configuration);

    // Obtener y procesar los resultados
    Result<Record> records = procedure.getResults().get(0);
    Map<String, Object> result = new HashMap<>();
    if (!records.isEmpty()) {
        Record record = records.get(0);
        for (var field : record.fields()) {
            result.put(field.getName(), record.get(field));
        }
    }
    return result;
}


    public List<ValidateVideoAccessResult> fetchAllActiveVideoAccesses() {
        Spgetallactivevideoaccessusers procedure = new Spgetallactivevideoaccessusers();

        // Ejecutar la rutina almacenada
        procedure.execute(configuration);

        // Obtener los resultados
        List<ValidateVideoAccessResult> videoAccessResults = new ArrayList<>();
        Result<Record> results = procedure.getResults().get(0);
        for (Record record : results) {
            ValidateVideoAccessResult result = new ValidateVideoAccessResult();

            result.id = record.getValue("id", Integer.class);
            result.email = record.getValue("email", String.class);
            result.isActive = byteToBoolean(record.getValue("is_active", Byte.class));
            result.adminEmail = record.getValue("admin_email", String.class);
            result.createdAt = record.getValue("created_at", Timestamp.class);
            result.updatedAt = record.getValue("updated_at", Timestamp.class);

            videoAccessResults.add(result);
        }

        return videoAccessResults;
    }

    public List<String> fetchActiveVideoAccessEmails() {
        Spgetactivevideoaccessemails procedure = new Spgetactivevideoaccessemails();

        // Ejecutar la rutina almacenada
        procedure.execute(configuration);

        // Obtener los resultados
        List<String> activeEmails = new ArrayList<>();
        Result<?> results = procedure.getResults().get(0);
        for (Object email : results.getValues("email")) {
            activeEmails.add((String) email);
        }

        return activeEmails;
    }

    public Map<String, Object> insertVideoAccess(VideoAccesses videoAccess) {
        // Asumiendo que tienes un procedimiento almacenado para insertar
        // Puedes adaptar esto según tu lógica de negocio y procedimientos almacenados
        Spinsertvideoaccess spInsertVideoAccess = new Spinsertvideoaccess();
        spInsertVideoAccess.setPEmail(videoAccess.getEmail());
        spInsertVideoAccess.setPAdminId(videoAccess.getAdminId());
        // ... establecer otros campos según sea necesario

        // Ejecutar el procedimiento almacenado
        spInsertVideoAccess.execute(dslContext.configuration());

        // Obtener y procesar los resultados
        Result<Record> records = spInsertVideoAccess.getResults().get(0);
        Map<String, Object> result = new HashMap<>();
        if (!records.isEmpty()) {
            Record record = records.get(0);
            for (var field : record.fields()) {
                result.put(field.getName(), record.get(field));
            }
        }
        return result;
    }

    private Boolean byteToBoolean(Byte byteValue) {
        return byteValue != null && byteValue != 0;
    }

    public static class ValidateVideoAccessResult {
        public Integer id;
        public String email;
        public Boolean isActive;
        // Cambiar el nombre del campo para reflejar el cambio en el procedimiento
        // almacenado
        public String adminEmail;
        public Timestamp createdAt;
        public Timestamp updatedAt;
    }
}
