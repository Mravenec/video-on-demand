import React, { useState } from 'react';
import Footer from '../../../Footer/Footer';

const PaymentView = () => {
  const [showMessage, setShowMessage] = useState(false);

  const handleButtonClick = () => {
    setShowMessage(true);

    // Comentado: Redirige a la URL de Checkout de Stripe
    // window.location.href = 'https://donate.stripe.com/test_8wM001dWt0Ms5TW7ss';
  };

  return (
    <div style={{ textAlign: 'center' }}>
      <button onClick={handleButtonClick}>Pague con Stripe</button>
      {showMessage && (
        <p style={{ marginTop: '20px' }}>
          Por favor ponte en contacto con  
          <a href="mailto:roberto@tuprimernegocio.org"> roberto@tuprimernegocio.org </a> 
          o al número de WhatsApp 
          <a href="https://wa.me/50671902300"> +(506) 7190 2300 </a>
          para saber cómo efectuar el pago.
        </p>
      )}
      <Footer />
    </div>
  );
};

export default PaymentView;
