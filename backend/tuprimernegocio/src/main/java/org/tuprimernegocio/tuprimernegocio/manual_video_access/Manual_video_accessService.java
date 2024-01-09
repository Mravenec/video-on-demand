package org.tuprimernegocio.tuprimernegocio.manual_video_access;

import com.tuprimernegocio.library.database.jooq.manual_video_access.tables.pojos.VideoAccesses;

import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tuprimernegocio.tuprimernegocio.user.UserService;

import java.util.List;
import java.util.Map;

@Service
public class Manual_video_accessService {
    private static final Logger logger = LoggerFactory.getLogger(Manual_video_accessRepository.class);

    @Autowired
    private Manual_video_accessRepository manualVideoAccessRepository;

    @Autowired
    private UserService userService;

    public ResponseEntity<List<Manual_video_accessRepository.ValidateVideoAccessResult>> getActiveVideoAccessUsers(
            Map<String, String> headers,
            Integer adminId) {
        try {
            String authHeader = null;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if ("Authorization".equalsIgnoreCase(entry.getKey())) {
                    authHeader = entry.getValue();
                    break;
                }
            }

            if (authHeader == null) {
                logger.error("Auth header is null");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            ResponseEntity<Object> validationResponse = userService.validateToken(authHeader);
            HttpStatus validationStatus = validationResponse.getStatusCode();

            if (validationStatus != HttpStatus.OK) {
                logger.error("Token validation failed with status: {}", validationStatus);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Map<String, Object> responseBody = (Map<String, Object>) validationResponse.getBody();
            Boolean isValid = (Boolean) responseBody.get("isValid");

            if (!isValid) {
                logger.error("Token is not valid according to the response body.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            List<Manual_video_accessRepository.ValidateVideoAccessResult> activeUsers = manualVideoAccessRepository
                    .fetchActiveVideoAccesses(adminId);
            return ResponseEntity.ok(activeUsers);
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while retrieving active video access users: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve active video access users");
        }
    }

    public ResponseEntity<Map<String, Object>> insertVideoAccess(Map<String, String> headers,
            VideoAccesses videoAccess) {
        try {
            String authHeader = null;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if ("Authorization".equalsIgnoreCase(entry.getKey())) {
                    authHeader = entry.getValue();
                    break;
                }
            }

            if (authHeader == null) {
                logger.error("Auth header is null");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            ResponseEntity<Object> validationResponse = userService.validateToken(authHeader);
            HttpStatus validationStatus = validationResponse.getStatusCode();

            if (validationStatus != HttpStatus.OK) {
                logger.error("Token validation failed with status: {}", validationStatus);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Map<String, Object> responseBody = (Map<String, Object>) validationResponse.getBody();
            Boolean isValid = (Boolean) responseBody.get("isValid");

            if (!isValid) {
                logger.error("Token is not valid according to the response body.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            // Llamada al repositorio para insertar el acceso de video
            Map<String, Object> result = manualVideoAccessRepository.insertVideoAccess(videoAccess);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while inserting video access: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert video access");
        }
    }

    public ResponseEntity<Map<String, Object>> updateVideoAccessEmail(Map<String, String> headers, Integer accessId,
            String newEmail, Integer adminId) {
        try {
            String authHeader = null;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if ("Authorization".equalsIgnoreCase(entry.getKey())) {
                    authHeader = entry.getValue();
                    break;
                }
            }

            if (authHeader == null) {
                logger.error("Auth header is null");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            ResponseEntity<Object> validationResponse = userService.validateToken(authHeader);
            HttpStatus validationStatus = validationResponse.getStatusCode();

            if (validationStatus != HttpStatus.OK) {
                logger.error("Token validation failed with status: {}", validationStatus);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Map<String, Object> responseBody = (Map<String, Object>) validationResponse.getBody();
            Boolean isValid = (Boolean) responseBody.get("isValid");

            if (!isValid) {
                logger.error("Token is not valid according to the response body.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            // Llamada al repositorio para actualizar el correo electrónico del acceso de
            // video
            Map<String, Object> result = manualVideoAccessRepository.updateVideoAccessEmail(accessId, newEmail,
                    adminId);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while updating video access email: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update video access email");
        }
    }

    public ResponseEntity<Map<String, Object>> deactivateVideoAccess(Map<String, String> headers, Integer accessId,
            Integer adminId) {
        try {
            String authHeader = null;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if ("Authorization".equalsIgnoreCase(entry.getKey())) {
                    authHeader = entry.getValue();
                    break;
                }
            }

            if (authHeader == null) {
                logger.error("Auth header is null");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            ResponseEntity<Object> validationResponse = userService.validateToken(authHeader);
            HttpStatus validationStatus = validationResponse.getStatusCode();

            if (validationStatus != HttpStatus.OK) {
                logger.error("Token validation failed with status: {}", validationStatus);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Map<String, Object> responseBody = (Map<String, Object>) validationResponse.getBody();
            Boolean isValid = (Boolean) responseBody.get("isValid");

            if (!isValid) {
                logger.error("Token is not valid according to the response body.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            // Llamada al repositorio para desactivar el acceso de video
            Map<String, Object> result = manualVideoAccessRepository.deactivateVideoAccess(accessId, adminId);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while deactivating video access: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to deactivate video access");
        }
    }

    /*
     * public ResponseEntity<List<String>> getActiveVideoAccessEmails() {
     * try {
     * List<String> activeEmails =
     * manualVideoAccessRepository.fetchActiveVideoAccessEmails();
     * return ResponseEntity.ok(activeEmails);
     * } catch (Exception e) {
     * throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
     * "Failed to retrieve active video access emails");
     * }
     * }
     */
}
