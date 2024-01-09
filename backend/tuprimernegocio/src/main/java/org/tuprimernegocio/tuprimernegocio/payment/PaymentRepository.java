package org.tuprimernegocio.tuprimernegocio.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentListParams;
import com.stripe.param.checkout.SessionListParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tuprimernegocio.tuprimernegocio.manual_video_access.Manual_video_accessRepository;

@Repository
public class PaymentRepository {

    public PaymentRepository() {
        Stripe.apiKey = "sk_test_51OD8MCEmQl5nvMENis3S8zopbjiAE4xqGFAP53C7lDibaGms5ab3hUrhuHaDMQVFE0yoVNXP5yhlbACXUufHTDZK00H442AcGw";
    }

    @Autowired
    private Manual_video_accessRepository manualVideoAccessRepository;


    public String createPaymentIntent(PaymentRequest paymentRequest) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setCurrency("usd")
                .setAmount(paymentRequest.getAmount()) // amount in cents
                .setDescription(paymentRequest.getDescription())
                .addPaymentMethodType("card")
                .setReceiptEmail(paymentRequest.getEmail())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getId();
    }

    public List<Map<String, Object>> retrieveConfirmedPayments() throws Exception {
        PaymentIntentListParams paymentIntentParams = PaymentIntentListParams.builder().build();
        List<PaymentIntent> allPaymentIntents = PaymentIntent.list(paymentIntentParams).getData();

        return allPaymentIntents.stream()
                .filter(paymentIntent -> "succeeded".equals(paymentIntent.getStatus()))
                .map(paymentIntent -> {
                    Map<String, Object> paymentDetails = new HashMap<>();
                    paymentDetails.put("id", paymentIntent.getId());

                    // Recuperar el correo electrónico desde la sesión de Checkout
                    String email = retrieveEmailFromCheckoutSession(paymentIntent.getId());
                    paymentDetails.put("email", email);

                    return paymentDetails;
                })
                .collect(Collectors.toList());
    }

    private String retrieveEmailFromCheckoutSession(String paymentIntentId) {
        try {
            SessionListParams sessionParams = SessionListParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .build();
            List<Session> sessions = Session.list(sessionParams).getData();

            // Tomar el primer elemento de la lista, ya que debería haber solo una sesión por PaymentIntent
            if (!sessions.isEmpty()) {
                Session session = sessions.get(0);
                return session.getCustomerDetails().getEmail();
            }
        } catch (Exception e) {
            // Manejar excepciones y errores
        }
        return null;
    }

    

    public String handlePayment(PaymentRequest paymentRequest) throws StripeException {
        // Crea el PaymentMethod
        Map<String, Object> cardDetails = new HashMap<>();
        cardDetails.put("number", paymentRequest.getCardNumber());
        cardDetails.put("exp_month", paymentRequest.getExpMonth());
        cardDetails.put("exp_year", paymentRequest.getExpYear());
        cardDetails.put("cvc", paymentRequest.getCvc());

        Map<String, Object> paymentMethodParams = new HashMap<>();
        paymentMethodParams.put("type", "card");
        paymentMethodParams.put("card", cardDetails);

        PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);

        // Crea el PaymentIntent
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount(paymentRequest.getAmount())
                .setCurrency(paymentRequest.getCurrency())
                .setPaymentMethod(paymentMethod.getId())
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .setConfirm(true)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);

        // Confirmar el PaymentIntent si es necesario
        if (!"succeeded".equals(paymentIntent.getStatus())) {
            PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                    .setPaymentMethod(paymentMethod.getId())
                    .build();

            paymentIntent = paymentIntent.confirm(confirmParams);
        }

        return paymentIntent.getId();
    }

    /* 
    public Map<String, String> findPaymentIntentByEmail(String emailToFind) throws Exception {
        List<Map<String, Object>> confirmedPayments = retrieveConfirmedPayments();

        return confirmedPayments.stream()
                .filter(paymentDetails -> emailToFind.equals(paymentDetails.get("email")))
                .findFirst()
                .map(paymentDetails -> {
                    Map<String, String> result = new HashMap<>();
                    result.put("id", (String) paymentDetails.get("id"));
                    result.put("email", (String) paymentDetails.get("email"));
                    return result;
                })
                .orElse(null); // Retorna null si no se encuentra ningún PaymentIntent con ese correo
    }
    */

    public Map<String, Object> findPaymentIntentByEmail(String emailToFind) throws Exception {
        // Verifica si el email está en la lista de acceso activo a videos
        List<String> activeEmails = manualVideoAccessRepository.fetchActiveVideoAccessEmails();
        
        if (activeEmails.contains(emailToFind)) {
            // Si el email se encuentra en la lista, retorna la información indicando que fue añadido manualmente
            Map<String, Object> manualAddResult = new HashMap<>();
            manualAddResult.put("email", emailToFind);
            manualAddResult.put("permission", true);
            manualAddResult.put("id", "Added manually by Admin");
            return manualAddResult;
        }

        // Si el email no está en la lista de acceso a videos, busca en los pagos confirmados de Stripe
        List<Map<String, Object>> confirmedPayments = retrieveConfirmedPayments();

        return confirmedPayments.stream()
                                .filter(paymentDetails -> emailToFind.equals(paymentDetails.get("email")))
                                .findFirst()
                                .map(paymentDetails -> {
                                    Map<String, Object> stripeResult = new HashMap<>();
                                    stripeResult.put("id", paymentDetails.get("id"));
                                    stripeResult.put("email", paymentDetails.get("email"));
                                    stripeResult.put("permission", true);
                                    return stripeResult;
                                })
                                .orElse(null); // Retorna null si no se encuentra ningún PaymentIntent con ese correo
    }
    
}
