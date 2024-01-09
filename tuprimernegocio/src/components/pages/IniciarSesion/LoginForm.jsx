import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useDispatch, useSelector } from 'react-redux';
import { login } from '../../../store/actions/authActions';
import './LoginForm.css';

function LoginForm() {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

  // Estado local para seguir el estado de la autenticación y visibilidad de contraseña
  const [authFailed, setAuthFailed] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/dashboard');
    } else if (authFailed) {
      console.error("No se ha podido autenticar al usuario");
    }
  }, [isAuthenticated, authFailed, navigate]);

  const onSubmit = async (data) => {
    try {
      console.log("Dispatching login...");
      await dispatch(login(data));
      console.log("Dispatched login.");

      if (!isAuthenticated) {
        console.log("Authentication failed.");
        setAuthFailed(true);
      } else {
        console.log("Authentication succeeded.");
      }
    } catch (error) {
      console.log("Caught an error during login:", error);
      setAuthFailed(true);
    }
  };

  // Función para alternar la visibilidad de la contraseña
  const togglePasswordVisibility = () => {
    setShowPassword(showPassword);
  };

  // Función para resetear el mensaje de error cuando se editan los campos
  const resetError = () => {
    if (authFailed) {
      setAuthFailed(false);
    }
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit(onSubmit)}>
        <div className="form-group">
          <label htmlFor="email">Email:</label>
          <input 
            type="text" 
            id="email" 
            {...register('email', { required: true })}
            onChange={resetError}
          />
          {errors.email && <span className="error-message">El email es obligatorio</span>}
        </div>
        <div className="form-group">
          <label htmlFor="password">Contraseña:</label>
          <input 
            type={showPassword ? "text" : "password"} 
            id="password" 
            {...register('password', { required: true })}
            onChange={resetError}
            onClick={togglePasswordVisibility}  // Alternar visibilidad al hacer clic
          />
          {errors.password && <span className="error-message">La contraseña es obligatoria</span>}
        </div>
        {authFailed && <span className="error-message">No se pudo iniciar sesión. Por favor, inténtalo de nuevo.</span>}
        <button type="submit">Iniciar Sesión</button>
      </form>
      <div className="login-footer">
        <Link to="/iniciar-sesion/registrarse">Registrarse</Link>
        <Link to="/iniciar-sesion/olvide-contrasena">Olvidé mi contraseña</Link>
      </div>
    </div>
  );
}

export default LoginForm;