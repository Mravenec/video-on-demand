package org.tuprimernegocio.tuprimernegocio.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.tuprimernegocio.tuprimernegocio.user.UserRepository.ValidateUserResult;

import com.tuprimernegocio.library.database.jooq.users.tables.records.AddressRecord;
import com.tuprimernegocio.library.database.jooq.users.tables.records.PhonesRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Registers a new user based on the incoming payload.
     * 
     * @param payload A map containing the user's email, password, and full name.
     * @return HTTP response status.
     */
    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, Object> payload) {
        return userService.registerUser(payload);
    }

    /**
     * Validates a user based on their email and password.
     * 
     * @param payload A map containing the user's email and password.
     * @return The result of the validation, containing user details if successful,
     *         or an error message otherwise.
     */
    @CrossOrigin(origins = "https://s1.tuprimernegocio.org:8080")
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody Map<String, String> payload) {
        return userService.loginUser(payload);
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @GetMapping("/validateToken")
    public ResponseEntity<Object> validateToken(@RequestHeader("Authorization") String authHeader) {
        return userService.validateToken(authHeader);
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @GetMapping("/userInfo")
    public ResponseEntity<Object> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        return userService.getUserInfo(authHeader);
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @GetMapping("/findAllUsersByAdmin/{adminId}")
    public ResponseEntity<List<ValidateUserResult>> findAllUsersByAdmin(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer adminId) {
        return userService.findAllUsersByAdmin(authHeader, adminId);
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @PostMapping("/updatePhones")
    public ResponseEntity<?> updatePhones(@RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> payload) {
        Integer userId = (Integer) payload.get("userId");
        String whatsapp = (String) payload.get("whatsapp");
        String otherNumbers = (String) payload.get("otherNumbers");
        userService.updatePhones(headers, userId, whatsapp, otherNumbers);
        return ResponseEntity.ok().body("Phones updated successfully");
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @GetMapping("/selectPhones/{userId}")
    public ResponseEntity<?> selectPhones(@RequestHeader Map<String, String> headers,
            @PathVariable Integer userId) {
        PhonesRecord phones = userService.selectPhones(headers, userId);
        Map<String, Object> phonesMap = new HashMap<>();
        phonesMap.put("userId", phones.getValue("user_id")); // Cambio realizado aquí
        phonesMap.put("whatsapp", phones.getWhatsapp());
        phonesMap.put("otherNumbers", phones.getOtherNumbers());
        return ResponseEntity.ok().body(phonesMap);
    }

    // Reset Password:
    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            userService.processPasswordResetRequest(email);
            return ResponseEntity.ok("Reset password link has been sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        try {
            String token = payload.get("token");
            String newPassword = payload.get("newPassword");

            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Your password has been updated.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @GetMapping("/selectAddress/{userId}")
    public ResponseEntity<?> selectAddress(@RequestHeader Map<String, String> headers,
            @PathVariable Integer userId) {
        AddressRecord address = userService.selectAddress(headers, userId);
        Map<String, Object> addressMap = new HashMap<>();

        // Agregando las propiedades de AddressRecord
        addressMap.put("userId", address.getValue("user_id"));
        addressMap.put("addressLine1", address.getValue("address_line1"));
        addressMap.put("addressLine2", address.getValue("address_line2"));
        addressMap.put("province", address.getValue("province"));
        addressMap.put("canton", address.getValue("canton"));
        addressMap.put("postalCode", address.getValue("postal_code"));

        return ResponseEntity.ok().body(addressMap);
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @PostMapping("/updateAddress")
    public ResponseEntity<?> updateAddress(@RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> payload) {
        Integer userId = (Integer) payload.get("userId");
        String addressLine1 = (String) payload.get("addressLine1");
        String addressLine2 = (String) payload.get("addressLine2");
        String province = (String) payload.get("province");
        String canton = (String) payload.get("canton");
        String postalCode = (String) payload.get("postalCode");
        userService.updateAddress(headers, userId, addressLine1, addressLine2, province, canton, postalCode);
        return ResponseEntity.ok().body("Address updated successfully");
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @DeleteMapping("/deleteAddress/{userId}")
    public ResponseEntity<?> deleteAddress(@RequestHeader Map<String, String> headers,
            @PathVariable Integer userId) {
        userService.deleteAddress(headers, userId);
        return ResponseEntity.ok().body("Address deleted successfully");
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @PutMapping("/updateUserProfilePicture")
    public ResponseEntity<?> updateUserProfilePicture(
            @RequestHeader Map<String, String> headers,
            @RequestParam("userId") Integer userId,
            @RequestParam("profilePicture") MultipartFile profilePicture) {
        try {
            return userService.updateUserProfilePicture(headers, userId, profilePicture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "https://tuprimernegocio.org")
    @GetMapping("/getUserProfilePicture/{userId}")
    public ResponseEntity<?> getUserProfilePicture(@RequestHeader Map<String, String> headers,
            @PathVariable Integer userId) {
        Map<String, Object> profilePictureResponse = userService.getUserProfilePicture(headers, userId);
        if (profilePictureResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile picture not found");
        }
        return ResponseEntity.ok().body(profilePictureResponse);
    }

}
