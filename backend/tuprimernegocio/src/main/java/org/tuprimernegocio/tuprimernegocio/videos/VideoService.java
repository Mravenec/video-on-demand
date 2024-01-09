package org.tuprimernegocio.tuprimernegocio.videos;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tuprimernegocio.tuprimernegocio.user.UserService;

@Service
public class VideoService {

    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserService userService;

    public Map<String, Object> createSection(Map<String, String> headers, String name, Integer adminId) {
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

            return videoRepository.createSection(name, adminId);

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while creating a section: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create a section");
        }
    }

    public List<Map<String, Object>> getAllVideosGroupedBySections(Map<String, String> headers) {
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

            return videoRepository.getAllVideosGroupedBySections();

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while retrieving videos grouped by sections: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve videos");
        }
    }

    public Map<String, Object> insertVideo(Map<String, String> headers, String title, String url, String content,
            Integer sequenceNumber, Integer sectionId, Integer adminId) {
        try {
            System.out.println("Headers: " + headers);
            System.out.println("Title: " + title);
            System.out.println("URL: " + url);
            System.out.println("Content: " + content);
            System.out.println("Sequence Number: " + sequenceNumber);
            System.out.println("Section ID: " + sectionId);
            System.out.println("Admin ID: " + adminId);

            String authHeader = null;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if ("Authorization".equalsIgnoreCase(entry.getKey())) {
                    authHeader = entry.getValue();
                    break;
                }
            }

            if (authHeader == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            ResponseEntity<Object> validationResponse = userService.validateToken(authHeader);
            HttpStatus validationStatus = validationResponse.getStatusCode();

            if (validationStatus != HttpStatus.OK) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Map<String, Object> responseBody = (Map<String, Object>) validationResponse.getBody();
            Boolean isValid = (Boolean) responseBody.get("isValid");

            if (!isValid) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            return videoRepository.insertVideo(title, url, content, sequenceNumber, sectionId, adminId);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert a video");
        }
    }

    public void updateSectionSequence(Map<String, String> headers, Integer sectionId, Integer newSequenceNumber,
            Integer addedBy) {
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

            videoRepository.updateSectionSequence(sectionId, newSequenceNumber, addedBy);

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while updating section sequence: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update section sequence");
        }
    }

    public void updateVideoSequence(Map<String, String> headers, Integer sectionId, Integer videoId,
            Integer newSequenceNumber, Integer addedBy) {
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

            videoRepository.updateVideoSequence(sectionId, videoId, newSequenceNumber, addedBy);

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while updating video sequence: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update video sequence");
        }
    }

    public void updateSectionName(Map<String, String> headers, Integer sectionId, String newName) {
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

            videoRepository.updateSectionName(sectionId, newName);

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while updating section name: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update section name");
        }
    }

    public void updateVideoTitle(Map<String, String> headers, Integer videoId, String newTitle) {
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

            // Llama al método en el repositorio para actualizar el título del video
            videoRepository.updateVideoTitle(videoId, newTitle);

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while updating video title: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update video title");
        }
    }

    public void deactivateVideo(Map<String, String> headers, int videoId) {
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

            // Llama al método en el repositorio para desactivar un video por su ID
            videoRepository.deactivateVideo(videoId);

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while deactivating video: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to deactivate video");
        }
    }

    public void deactivateSection(Map<String, String> headers, int sectionId) {
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

            // Llamar al método en el repositorio para desactivar una sección por su ID
            videoRepository.deactivateSection(sectionId);

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while deactivating section: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to deactivate section");
        }
    }

    public void updateVideoUrl(Map<String, String> headers, int videoId, String newUrl) {
        try {
            // Realiza la validación del encabezado de autorización
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

            // Llama al método en el repositorio para actualizar la URL del video
            videoRepository.updateVideoUrl(videoId, newUrl);

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while updating video URL: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update video URL");
        }
    }

    public void updateVideoDescription(Map<String, String> headers, Integer videoId, String newDescription) {
        try {
            // Realiza la validación del encabezado de autorización
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

            // Llama al método en el repositorio para actualizar la descripción del video
            videoRepository.updateVideoDescription(videoId, newDescription);

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while updating video description: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update video description");
        }
    }

    public void markVideoAsWatched(Map<String, String> headers, Integer userId, Integer videoId) {
        try {
            // Realiza la validación del encabezado de autorización
            String authHeader = null;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if ("Authorization".equalsIgnoreCase(entry.getKey())) {
                    authHeader = entry.getValue();
                    break;
                }
            }
    
            if (authHeader == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
    
            ResponseEntity<Object> validationResponse = userService.validateToken(authHeader);
            HttpStatus validationStatus = validationResponse.getStatusCode();
    
            if (validationStatus != HttpStatus.OK) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
    
            Map<String, Object> responseBody = (Map<String, Object>) validationResponse.getBody();
            Boolean isValid = (Boolean) responseBody.get("isValid");
    
            if (!isValid) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
    
            // Llama al método en el repositorio para marcar el video como visto
            videoRepository.markVideoAsWatched(userId, videoId);
    
        } catch (ResponseStatusException e) {
            throw e;
    
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to mark video as watched");
        }
    }
    
    public void unmarkVideoAsWatched(Map<String, String> headers, Integer userId, Integer videoId) {
        try {
            // Realiza la validación del encabezado de autorización
            String authHeader = null;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if ("Authorization".equalsIgnoreCase(entry.getKey())) {
                    authHeader = entry.getValue();
                    break;
                }
            }
    
            if (authHeader == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
    
            ResponseEntity<Object> validationResponse = userService.validateToken(authHeader);
            HttpStatus validationStatus = validationResponse.getStatusCode();
    
            if (validationStatus != HttpStatus.OK) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
    
            Map<String, Object> responseBody = (Map<String, Object>) validationResponse.getBody();
            Boolean isValid = (Boolean) responseBody.get("isValid");
    
            if (!isValid) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
    
            // Llama al método en el repositorio para desmarcar el video como visto
            videoRepository.unmarkVideoAsWatched(userId, videoId);
    
        } catch (ResponseStatusException e) {
            throw e;
    
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to unmark video as watched");
        }
    }
    
    
    public List<Map<String, Object>> getAllVideosBySectionsAndUser(Map<String, String> headers, Integer userId) {
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
    
            return videoRepository.getAllVideosBySectionsAndUser(userId);
    
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;
    
        } catch (Exception e) {
            logger.error("Error occurred while retrieving videos by sections and user: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve videos by sections and user");
        }
    }
    

}
