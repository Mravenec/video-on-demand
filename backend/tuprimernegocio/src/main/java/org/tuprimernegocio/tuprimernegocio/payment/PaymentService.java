package org.tuprimernegocio.tuprimernegocio.payment;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.model.PaymentIntent;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public String createPaymentIntent(PaymentRequest paymentRequest) throws Exception {
        return paymentRepository.createPaymentIntent(paymentRequest);
    }

    public List<Map<String, Object>> getConfirmedPayments() throws Exception {
        return paymentRepository.retrieveConfirmedPayments();
    }

    public String handlePayment(PaymentRequest paymentRequest) throws Exception {
        return paymentRepository.handlePayment(paymentRequest);
    }

    public Map<String, Object> findPaymentIntentByEmail(String email) throws Exception {
        return paymentRepository.findPaymentIntentByEmail(email);
    }
}
