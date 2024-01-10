package org.tuprimernegocio.tuprimernegocio.manual_video_access;

import com.tuprimernegocio.library.database.jooq.manual_video_access.tables.pojos.VideoAccesses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manual-video-access")
public class Manual_video_accessController {

    @Autowired
    private Manual_video_accessService manualVideoAccessService;

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @GetMapping("/active-users/{adminId}")
    public ResponseEntity<List<Manual_video_accessRepository.ValidateVideoAccessResult>> getActiveVideoAccessUsers(
            @RequestHeader Map<String, String> headers,
            @PathVariable Integer adminId) {
        return manualVideoAccessService.getActiveVideoAccessUsers(headers, adminId);
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @PostMapping("/insert-video-access")
    public ResponseEntity<Map<String, Object>> insertVideoAccess(
            @RequestHeader Map<String, String> headers,
            @RequestBody VideoAccesses videoAccess) {

        return manualVideoAccessService.insertVideoAccess(headers, videoAccess);
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @PutMapping("/update-video-access-email")
    public ResponseEntity<Map<String, Object>> updateVideoAccessEmail(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> requestData) {

        // Obtener los valores del mapa. Asegúrate de realizar la conversión de tipos
        // adecuada.
        Integer accessId = (Integer) requestData.get("accessId");
        String newEmail = (String) requestData.get("newEmail");
        Integer adminId = (Integer) requestData.get("adminId");
        return manualVideoAccessService.updateVideoAccessEmail(headers, accessId, newEmail, adminId);
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @PutMapping("/deactivate-video-access")
    public ResponseEntity<Map<String, Object>> deactivateVideoAccess(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> requestData) {

        Integer accessId = (Integer) requestData.get("accessId");
        Integer adminId = (Integer) requestData.get("adminId");

        return manualVideoAccessService.deactivateVideoAccess(headers, accessId, adminId);
    }

    /*
     * @CrossOrigin(origins = "http://localhost:3000")
     * 
     * @GetMapping("/active-emails")
     * public ResponseEntity<List<String>> getActiveVideoAccessEmails() {
     * return manualVideoAccessService.getActiveVideoAccessEmails();
     * }
     */
}
