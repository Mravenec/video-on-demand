import React, { useState, useEffect, useMemo } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useForm } from 'react-hook-form';
import {
  findPaymentIntentByEmail,
  updateUserProfilePicture,
  updateAddress,
  updatePhones
} from '../../../store/actions/authActions';
import { FaUser, FaVideo, FaUpload } from 'react-icons/fa';
import './UserView.css';
import Videos from './Videos/Videos';
import PaymentView from './Payment/PaymentView'; // Importa PaymentView aquí

const UserView = () => {
  const [currentView, setCurrentView] = useState('profile');
  const [hasVideoPermission, setHasVideoPermission] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);
  const userInfo = useSelector((state) => state.auth.user);
  const dispatch = useDispatch();
  const { register, handleSubmit } = useForm();

  useEffect(() => {
    if (userInfo && userInfo.email) {
      dispatch(findPaymentIntentByEmail(userInfo.email));
    }
  }, [dispatch, userInfo]);

  const paymentIntent = useSelector(state => state.auth.paymentIntent);

  useEffect(() => {
    if (paymentIntent) {
      setHasVideoPermission(paymentIntent.permission);
    }
  }, [paymentIntent]);
  /*
    const handleVideoClick = () => {
      if (hasVideoPermission) {
        setCurrentView('videos'); // Muestra la vista de videos
      } else {
        setCurrentView('payment'); // Reemplaza '/path-to-payment-view' con la ruta correcta hacia la vista de pago
      }
    };
  */
  const handleVideoClick = async () => {
    if (currentView === 'videos') {
      return; // No cambies la vista si ya estás en 'videos'
    }

    if (hasVideoPermission) {
      setCurrentView('videos'); // Muestra la vista de videos
    } else {
      // No cambies la vista inmediatamente, solo si la validación es exitosa
      try {
        await dispatch(findPaymentIntentByEmail(userInfo.email));

        if (paymentIntent && paymentIntent.permission) {
          setCurrentView('videos'); // Cambia a la vista de videos si la validación es exitosa
        } else {
          setCurrentView('payment'); // Cambia a la vista de pago si no hay permiso
        }
      } catch (error) {
        console.error('Error al obtener el permiso de pago:', error);
        // Maneja el error aquí si es necesario
      }
    }
  };

  const handleMouseEnter = () => {
    setIsExpanded(true);
  };

  const handleMouseLeave = () => {
    setIsExpanded(false);
  };

  const renderContent = useMemo(() => {
    const { fullName, email, profilePicture, addressLine1, addressLine2, province, canton, postalCode, whatsapp, otherNumbers } = userInfo;

    const onSubmitProfile = async (data) => {
      const formData = new FormData();
      if (data.profilePicture && data.profilePicture[0]) {
        // Para propósitos de depuración, vamos a imprimir lo que se está enviando.
    console.log('Archivo a enviar:', data.profilePicture[0]);
    
        formData.append('profilePicture', data.profilePicture[0]);
      }
      formData.append('email', data.email);
      formData.append('fullName', data.fullName);
      await dispatch(updateUserProfilePicture(formData));
    };

    const onSubmitAddress = (data) => {
      dispatch(updateAddress(data));
    };

    const onSubmitPhones = (data) => {
      dispatch(updatePhones(data));
    };

    switch (currentView) {
      case 'profile':
        return (
          <div className="user-info">
            <h2>Tus datos personales</h2>
            <p>Bienvenido, {fullName}</p>
            <div className="forms-container">
              <form onSubmit={handleSubmit(onSubmitProfile)} className="form-profile" encType="multipart/form-data">
                <h3>Información</h3>
                <div className="profile-picture">
                  <label htmlFor="uploadProfilePicture" className="upload-icon">
                    <FaUpload />
                  </label>
                  {profilePicture ? <img src={profilePicture} alt="Perfil" /> : <p>Imagen de perfil no disponible</p>}
                  <input
                    type="file"
                    accept="image/*"
                    id="uploadProfilePicture"
                    name="profilePicture"
                    {...register('profilePicture')}
                    style={{ display: 'none' }}
                  />
                </div>
                <input
                  name="fullName"
                  {...register('fullName')}
                  defaultValue={fullName}
                  placeholder="Nombre completo"
                  disabled 
                />
                <input
                  name="email"
                  {...register('email')}
                  defaultValue={email}
                  placeholder="Correo electrónico"
                  disabled  // Hace que el campo de correo electrónico sea de solo lectura
                />
                <button type="submit">Actualizar foto de perfil</button>
              </form>


              <form onSubmit={handleSubmit(onSubmitAddress)} className="form-address">
                <h3>Dirección</h3>
                <input
                  name="addressLine1"
                  {...register('addressLine1')}
                  defaultValue={addressLine1}
                  placeholder="Línea de dirección 1"
                />
                <input
                  name="addressLine2"
                  {...register('addressLine2')}
                  defaultValue={addressLine2}
                  placeholder="Línea de dirección 2"
                />
                <input
                  name="province"
                  {...register('province')}
                  defaultValue={province}
                  placeholder="Provincia"
                />
                <input
                  name="canton"
                  {...register('canton')}
                  defaultValue={canton}
                  placeholder="Cantón"
                />
                <input
                  name="postalCode"
                  {...register('postalCode')}
                  defaultValue={postalCode}
                  placeholder="Código Postal"
                />
                <button type="submit">Actualizar Dirección</button>
              </form>

              <form onSubmit={handleSubmit(onSubmitPhones)} className="form-phones">
                <h3>Números de contacto</h3>
                <input
                  name="whatsapp"
                  {...register('whatsapp')}
                  defaultValue={whatsapp}
                  placeholder="WhatsApp"
                />
                <input
                  name="otherNumbers"
                  {...register('otherNumbers')}
                  defaultValue={otherNumbers}
                  placeholder="Otros números"
                />
                <button type="submit">Actualizar Números</button>
              </form>
            </div>
          </div>
        );
      case 'videos':
        return <Videos />;
      case 'payment': // Agrega un caso para la vista de pago
        return <PaymentView />;
      default:
        return null;
    }
  }, [currentView, userInfo, register, handleSubmit, dispatch]);

  return (
    <div>
      <div className="sidebar" onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
        <div className="sidebar-icon" onClick={() => setCurrentView('profile')}>
          <FaUser />
          <span className="sidebar-text">Perfil</span>
        </div>
        <div className="sidebar-icon" onClick={handleVideoClick}>
          <FaVideo />
          <span className="sidebar-text">Videos</span>
        </div>
      </div>
      <div className={isExpanded ? 'content-area-expanded' : 'content-area'}>
        {renderContent}
      </div>
    </div>
  );
};

export default React.memo(UserView);