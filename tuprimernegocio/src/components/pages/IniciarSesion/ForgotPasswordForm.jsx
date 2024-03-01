import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { forgotPassword } from '../../../store/actions/authActions';
import './ForgotPasswordForm.css';

function ForgotPasswordForm() {
    const [email, setEmail] = useState('');
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [showPopup, setShowPopup] = useState(false);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const forgotPasswordMessage = useSelector(state => state.auth.forgotPasswordMessage);
    const forgotPasswordError = useSelector(state => state.auth.forgotPasswordError);

    useEffect(() => {
        // Comprobar si se ha enviado el formulario, hay un mensaje de éxito y no hay errores
        if (isSubmitted && forgotPasswordMessage && !forgotPasswordError) {
            setShowPopup(true);
            setTimeout(() => {
                setShowPopup(false);
                navigate('/iniciar-sesion', { state: { message: "Revisa tu bandeja de entrada para las instrucciones de restablecimiento de contraseña." } });
            }, 3000);
        }
    }, [forgotPasswordMessage, forgotPasswordError, isSubmitted, navigate]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        setIsSubmitted(true);
        await dispatch(forgotPassword(email));
    };

    return (

        <div className='body'>
        <div className="forgot-password-form">
            <h2>Restablecer Contraseña</h2>
            <form onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="email">Correo Electrónico:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Enviar enlace de restablecimiento</button>
                {forgotPasswordError && <div className="error-message">{forgotPasswordError}</div>}
            </form>
            {showPopup && <div className="popup">Enlace de restablecimiento enviado, por favor revisa la bandeja de entrada de tu correo electrónico...</div>}
        </div>
        </div>
    );
}

export default ForgotPasswordForm;
