package org.tuprimernegocio.tuprimernegocio.user;


import com.tuprimernegocio.library.database.jooq.users.tables.pojos.PersonalData;
import com.tuprimernegocio.library.database.jooq.users.tables.pojos.Users;
import com.tuprimernegocio.library.database.jooq.users.tables.records.AddressRecord;
import com.tuprimernegocio.library.database.jooq.users.tables.records.PhonesRecord;

import org.apache.tomcat.jni.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tuprimernegocio.tuprimernegocio.user.UserRepository.ValidateUserResult;
import org.tuprimernegocio.tuprimernegocio.user.emailService.EmailService;
import org.tuprimernegocio.tuprimernegocio.user.security.JwtTokenUtil;
import org.tuprimernegocio.tuprimernegocio.videos.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailService emailService;
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Esta estructura podría estar en tu UserService o en un servicio dedicado para
    // manejar tokens.
    private static final Map<String, List<String>> BLACKLISTED_TOKENS_BY_EMAIL = new HashMap<>();

    public ResponseEntity<String> registerUser(Map<String, Object> payload) {
        try {
            createUser(payload);
            return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while registering the user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createUser(Map<String, Object> payload) {
        Users users = new Users();
        users.setEmail((String) payload.get("email"));
        users.setPasswordHash((String) payload.get("password"));

        PersonalData personalData = new PersonalData();
        personalData.setFullName((String) payload.get("fullName"));

        userRepository.insertUser(users, personalData);
    }

    public ResponseEntity<Object> loginUser(Map<String, String> payload) {
        try {
            UserRepository.ValidateUserResult result = validateUser(payload.get("email"), payload.get("password"));

            if (result.errorMessage != null) {
                return new ResponseEntity<>(result.errorMessage, HttpStatus.UNAUTHORIZED);
            }

            // Antes de generar un nuevo token, invalida los tokens anteriores
            jwtTokenUtil.invalidatePreviousTokens(result.email);

            // Generar un nuevo token con el 'custom secret' basado en el hash de la
            // contraseña del usuario.
            String token = jwtTokenUtil.generateTokenWithCustomSecret(result.email, result.passwordHash);
            result.jwtToken = token;

            // Ya no es necesario agregar el token a una lista negra, ya que ahora se
            // gestiona mediante JwtTokenUtil.
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while validating the user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void blacklistOldTokensForUser(String email) {
        // Marcar todos los tokens anteriores como inválidos (o simplemente eliminarlos
        // de la lista negra).
        BLACKLISTED_TOKENS_BY_EMAIL.remove(email);
    }

    private UserRepository.ValidateUserResult validateUser(String email, String password) throws Exception {
        return userRepository.validateUser(email, password);
    }

    public ResponseEntity<Object> validateToken(String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            // Extraer el email del token utilizando la clave secreta general
            String email = jwtTokenUtil.extractUsernameUsingDefaultKey(token);

            // Validar el token utilizando el mapa activeTokensByUser
            if (!jwtTokenUtil.isActiveTokenForUser(token, email)) {
                return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("email", email);
            responseBody.put("isValid", true);

            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while validating the token: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getUserInfo(String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            // Usamos el método existente para validar el token.
            ResponseEntity<Object> validateTokenResponse = validateToken(authHeader);
            if (validateTokenResponse.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }

            // Obtener el email desde el token
            String email = jwtTokenUtil.extractUsernameUsingDefaultKey(token);

            // Usar el email para buscar información adicional del usuario.
            ValidateUserResult userInfo = userRepository.findUserByEmail(email);

            // Agregar el token al resultado
            userInfo.jwtToken = token;

            return new ResponseEntity<>(userInfo, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while fetching user info: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<ValidateUserResult>> findAllUsersByAdmin(String authHeader, Integer adminId) {
        try {
            // Validar el token utilizando la misma lógica que en tu servicio actual
            ResponseEntity<Object> validationResponse = validateToken(authHeader);
            HttpStatus validationStatus = validationResponse.getStatusCode();

            if (validationStatus != HttpStatus.OK) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Map<String, Object> responseBody = (Map<String, Object>) validationResponse.getBody();
            Boolean isValid = (Boolean) responseBody.get("isValid");

            if (!isValid) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            // Llamar al método del repositorio JOOQ para obtener los usuarios
            List<ValidateUserResult> users = userRepository.findAllUsersByAdmin(adminId);

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch users");
        }
    }

    public void updatePhones(Map<String, String> headers, Integer userId, String whatsapp, String otherNumbers) {
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

            ResponseEntity<Object> validationResponse = this.validateToken(authHeader);
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
            userRepository.updatePhones(userId, whatsapp, otherNumbers);
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while creating a section: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create a section");
        }
    }

    public PhonesRecord selectPhones(Map<String, String> headers, Integer userId) {
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

            ResponseEntity<Object> validationResponse = this.validateToken(authHeader);
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
            return userRepository.selectPhones(userId);
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while creating a section: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create a section");
        }
    }

    public AddressRecord selectAddress(Map<String, String> headers, Integer userId) {
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

            ResponseEntity<Object> validationResponse = this.validateToken(authHeader);
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
            return userRepository.selectAddress(userId);
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while creating a section: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create a section");
        }
    }

    public void updateAddress(Map<String, String> headers, Integer userId, String addressLine1, String addressLine2,
            String province, String canton, String postalCode) {
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

            ResponseEntity<Object> validationResponse = this.validateToken(authHeader);
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
            userRepository.updateAddress(userId, addressLine1, addressLine2, province, canton, postalCode);
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while creating a section: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create a section");
        }
    }

    public void deleteAddress(Map<String, String> headers, Integer userId) {
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

            ResponseEntity<Object> validationResponse = this.validateToken(authHeader);
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
            userRepository.deleteAddress(userId);
        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;

        } catch (Exception e) {
            logger.error("Error occurred while creating a section: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create a section");
        }
    }

    public ResponseEntity<?> updateUserProfilePicture(Map<String, String> headers, Integer userId, MultipartFile profilePicture) {
    try {
        // La validación del encabezado de autorización se mantiene como en tu código
        String authHeader = headers.get("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            logger.error("Auth header is null");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        ResponseEntity<Object> validationResponse = this.validateToken(authHeader);
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

        // Convertir MultipartFile a byte[]
        byte[] imageBytes = profilePicture.getBytes();
        userRepository.updateUserProfilePicture(userId, imageBytes);

        return ResponseEntity.ok().body("Profile picture updated successfully");

    } catch (IOException e) {
        logger.error("Error processing image file: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image file");
    } catch (ResponseStatusException e) {
        logger.error("Authorization error: {}", e.getReason());
        throw e;
    } catch (Exception e) {
        logger.error("Error occurred while updating profile picture: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update profile picture");
    }
}

    public Map<String, Object> getUserProfilePicture(Map<String, String> headers, Integer userId) {
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
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized - Auth header is null");
            }

            ResponseEntity<Object> validationResponse = this.validateToken(authHeader);
            HttpStatus validationStatus = validationResponse.getStatusCode();

            if (validationStatus != HttpStatus.OK) {
                logger.error("Token validation failed with status: {}", validationStatus);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized - Token validation failed");
            }

            Map<String, Object> responseBody = (Map<String, Object>) validationResponse.getBody();
            Boolean isValid = (Boolean) responseBody.get("isValid");

            if (!isValid) {
                logger.error("Token is not valid according to the response body.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized - Token is not valid");
            }

            byte[] profilePicture = userRepository.getUserProfilePicture(userId);
            if (profilePicture == null) {
                return null; // Retorna null si no se encuentra la imagen de perfil
            }

            // Crear un mapa con la estructura deseada
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("profile_picture", Base64.getEncoder().encodeToString(profilePicture)); // Codificar en Base64
            response.put("statusCode", "OK");
            response.put("statusCodeValue", 200);

            return response;

        } catch (ResponseStatusException e) {
            logger.error("Authorization error: {}", e.getReason());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while getting profile picture: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get profile picture");
        }
    }

    // Password Reset
    
    public void processPasswordResetRequest(String email) {
        // Valida si el email existe en la base de datos
        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email not found");
        }

        // Genera el token de restablecimiento de contraseña
        String token = jwtTokenUtil.generatePasswordResetToken(email);

        // Enviar el correo electrónico con el enlace de restablecimiento
        sendPasswordResetEmail(email, token);
    }

    private void sendPasswordResetEmail(String to, String token) {
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String subject = "Restablecimiento de Contraseña";
        String text = "Por favor, haz clic en el siguiente enlace para restablecer tu contraseña:\n\n" + resetLink;

        // Usa el servicio de correo electrónico para enviar el correo
        emailService.sendEmail(to, subject, text);
    }

    public void resetPassword(String token, String newPassword) throws Exception {
        if (!jwtTokenUtil.validatePasswordResetToken(token)) {
            throw new Exception("Invalid or expired token.");
        }
    
        String email = jwtTokenUtil.extractUsernameUsingDefaultKey(token);
        userRepository.updatePassword(email, newPassword);
        jwtTokenUtil.invalidatePasswordResetToken(token);
    }


    

}
