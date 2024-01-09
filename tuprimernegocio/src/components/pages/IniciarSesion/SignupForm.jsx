import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useForm } from 'react-hook-form';
import { register } from '../../../store/actions/authActions';
import { useNavigate } from 'react-router-dom';
import './SignupForm.css';

function SignupForm() {
    const [showPopup, setShowPopup] = useState(false);  // Nuevo estado para mostrar el pop-up
    const dispatch = useDispatch();
    const registerError = useSelector(state => state.auth.registerError);
    const registerSuccess = useSelector(state => state.auth.registerSuccess);
    const navigate = useNavigate();
    const { register: formRegister, handleSubmit, formState: { errors } } = useForm();

    useEffect(() => {
        if (registerSuccess) {
            setShowPopup(true);  // Mostrar el pop-up
            setTimeout(() => {
                setShowPopup(false);  // Ocultar el pop-up
                navigate('/iniciar-sesion');  // Redirigir al formulario de inicio de sesión
            }, 2000);  // Tiempo antes de redirigir
        }
    }, [registerSuccess, navigate]);

    const onSubmit = async (data) => {
        if (data.password !== data.passwordConfirm) {
            console.error('Las contraseñas no coinciden');
            return;
        }
        const payload = {
            username: data.username,
            email: data.email,
            password: data.password,
            fullName: data.username
        };
        dispatch(register(payload));
    };

    return (
        <div>
            {/* Pop-up de éxito */}
            {showPopup && <div className="popup">Registro exitoso, redirigiendo...</div>}
            <form className="signup-form" onSubmit={handleSubmit(onSubmit)}>
                {registerError && <div className="error">{registerError}</div>}
                {registerSuccess && <div className="success">Registro exitoso</div>}
                <div className="form-group">
                    <label htmlFor="signup-username">Nombre Completo:</label>
                    <input type="text" id="signup-username" {...formRegister('username', { required: 'El nombre de usuario es obligatorio' })} />
                    {errors.username && <span>{errors.username.message}</span>}
                </div>
                <div className="form-group">
                    <label htmlFor="signup-email">Correo Electrónico:</label>
                    <input type="email" id="signup-email" {...formRegister('email', {
                        required: 'El correo electrónico es obligatorio',
                        pattern: {
                            value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i,
                            message: 'Formato de correo electrónico inválido'
                        }
                    })} />
                    {errors.email && <span>{errors.email.message}</span>}
                </div>
                <div className="form-group">
                    <label htmlFor="signup-password">Contraseña:</label>
                    <input type="password" id="signup-password" {...formRegister('password', {
                        required: 'La contraseña es obligatoria',
                        pattern: {
                            value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
                            message: 'La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número'
                        }
                    })} />
                    {errors.password && <span>{errors.password.message}</span>}
                </div>
                <div className="form-group">
                    <label htmlFor="signup-password-confirm">Confirmar Contraseña:</label>
                    <input type="password" id="signup-password-confirm" {...formRegister('passwordConfirm', { required: 'La confirmación de la contraseña es obligatoria' })} />
                    {errors.passwordConfirm && <span>{errors.passwordConfirm.message}</span>}
                </div>
                <button type="submit">Registrarse</button>
            </form>
        </div>
    );
}

export default SignupForm;
