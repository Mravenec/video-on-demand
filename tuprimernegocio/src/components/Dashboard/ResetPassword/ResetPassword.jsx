import React, { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useLocation, useNavigate } from 'react-router-dom';
import { resetPassword } from '../../../store/actions/authActions';
import { useForm } from 'react-hook-form';
import './ResetPassword.css';

const ResetPassword = () => {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [passwordUpdated, setPasswordUpdated] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [showPopup, setShowPopup] = useState(false);

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const token = queryParams.get('token');

  useEffect(() => {
    if (passwordUpdated) {
      setShowPopup(true);
      setTimeout(() => {
        setShowPopup(false);
        navigate('/iniciar-sesion');
      }, 2000);
    }
  }, [passwordUpdated, navigate]);

  const onSubmit = data => {
    if (data.password !== data.confirmPassword) {
      setErrorMessage('Las contraseñas no coinciden');
      return;
    }
    dispatch(resetPassword(token, data.password))
      .then(() => setPasswordUpdated(true))
      .catch(error => setErrorMessage(error.message));
  };

  if (showPopup) {
    return <div className="popup">Tu contraseña ha sido actualizada con éxito, redirigiendo...</div>;
  }

  return (
    <div className="reset-password-container">
      <div className="reset-password-form">
        <h2>Restablecer Contraseña</h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="form-group">
            <label htmlFor="password">Nueva Contraseña:</label>
            <input
              name="password"
              type="password"
              {...register('password', { 
                required: 'La contraseña es obligatoria',
                minLength: {
                  value: 8,
                  message: 'La contraseña debe tener al menos 8 caracteres'
                },
                pattern: {
                  value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
                  message: 'La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número'
                }
              })}
            />
            {errors.password && <span className="error-message">{errors.password.message}</span>}
          </div>
          <div className="form-group">
            <label htmlFor="confirmPassword">Confirmar Nueva Contraseña:</label>
            <input
              name="confirmPassword"
              type="password"
              {...register('confirmPassword', { required: 'La confirmación de la contraseña es obligatoria' })}
            />
            {errors.confirmPassword && <span className="error-message">{errors.confirmPassword.message}</span>}
          </div>
          {errorMessage && <div className="error-message">{errorMessage}</div>}
          <button type="submit" className="submit-button">Restablecer Contraseña</button>
        </form>
      </div>
    </div>
  );
};

export default ResetPassword;
