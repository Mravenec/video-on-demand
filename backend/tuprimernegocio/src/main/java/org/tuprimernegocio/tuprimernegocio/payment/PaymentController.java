package org.tuprimernegocio.tuprimernegocio.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/stripe")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/createPaymentIntent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        try {
            String paymentIntentId = paymentService.createPaymentIntent(paymentRequest);
            return ResponseEntity.ok().body(paymentIntentId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/confirmedPayments")
    public ResponseEntity<?> getConfirmedPayments() {
        try {
            List<Map<String, Object>> confirmedPayments = paymentService.getConfirmedPayments();
            return ResponseEntity.ok().body(confirmedPayments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/payment")
    public ResponseEntity<?> handlePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            String paymentIntentId = paymentService.handlePayment(paymentRequest);
            return ResponseEntity.ok().body(paymentIntentId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/findPaymentIntentByEmail/{email}")
    public ResponseEntity<?> findPaymentIntentByEmail(@PathVariable String email) {
        try {
            Map<String, Object> paymentIntent = paymentService.findPaymentIntentByEmail(email);
            Map<String, Object> response = new HashMap<>();
            if (paymentIntent != null) {
                response.putAll(paymentIntent);
                response.put("permission", true); // Agrega un valor booleano
            } else {
                response.put("permission", false);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    

}
