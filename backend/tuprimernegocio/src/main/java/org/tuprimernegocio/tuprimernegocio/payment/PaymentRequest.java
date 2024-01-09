package org.tuprimernegocio.tuprimernegocio.payment;

public class PaymentRequest {
    private String email;
    private Long amount;
    private String description;
    private String currency;
    private String cardNumber;
    private Integer expMonth;
    private Integer expYear;
    private String cvc;
    private String name;
    private String country;
    private String postalCode;

    // Constructor
    public PaymentRequest() {
        // Puedes inicializar los campos aquí si lo deseas
    }

    // Método getter para el campo 'email'
    public String getEmail() {
        return email;
    }

    // Método setter para el campo 'email'
    public void setEmail(String email) {
        this.email = email;
    }

    // Método getter para el campo 'amount'
    public Long getAmount() {
        return amount;
    }

    // Método setter para el campo 'amount'
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    // Método getter para el campo 'description'
    public String getDescription() {
        return description;
    }

    // Método setter para el campo 'description'
    public void setDescription(String description) {
        this.description = description;
    }

    // Método getter para el campo 'currency'
    public String getCurrency() {
        return currency;
    }

    // Método setter para el campo 'currency'
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Método getter para el campo 'cardNumber'
    public String getCardNumber() {
        return cardNumber;
    }

    // Método setter para el campo 'cardNumber'
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    // Método getter para el campo 'expMonth'
    public Integer getExpMonth() {
        return expMonth;
    }

    // Método setter para el campo 'expMonth'
    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    // Método getter para el campo 'expYear'
    public Integer getExpYear() {
        return expYear;
    }

    // Método setter para el campo 'expYear'
    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }

    // Método getter para el campo 'cvc'
    public String getCvc() {
        return cvc;
    }

    // Método setter para el campo 'cvc'
    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    // Método getter para el campo 'name'
    public String getName() {
        return name;
    }

    // Método setter para el campo 'name'
    public void setName(String name) {
        this.name = name;
    }

    // Método getter para el campo 'country'
    public String getCountry() {
        return country;
    }

    // Método setter para el campo 'country'
    public void setCountry(String country) {
        this.country = country;
    }

    // Método getter para el campo 'postalCode'
    public String getPostalCode() {
        return postalCode;
    }

    // Método setter para el campo 'postalCode'
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
