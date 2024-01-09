import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { FaUser, FaVideo, FaToolbox, FaUsers } from 'react-icons/fa';
import './AdminView.css';  // Asegúrate de que la ruta de importación sea la correcta
import Videos from './Videos/Videos';
import ManualvideoAccess from './ManualvideoAccess/ManualvideoAccess'; // Importa el componente ManualvideoAccess
import UserManagement from './UserManagement/UserManagement'; 

const AdminView = () => {
  const [currentView, setCurrentView] = useState(null);
  const [isExpanded, setIsExpanded] = useState(false);
  const user = useSelector((state) => state.auth.user);

  const handleMouseEnter = () => {
    setIsExpanded(true);
  };

  const handleMouseLeave = () => {
    setIsExpanded(false);
  };

  const renderContent = () => {
    switch (currentView) {
      case 'profile':
        return (
          <>
            <div className="user-info">
              <h2>Vista de Administrador</h2>
              <p><span>Nombre Completo:</span> {user.fullName}</p>
              <p><span>Email:</span> {user.email}</p>
              <p><span>ID de Usuario:</span> {user.userId}</p>
              <p><span>Usuario Activo:</span> {String(user.userIsActive)}</p>
              <p><span>Admin Activo:</span> {String(user.adminIsActive)}</p>
              <p><span>Token JWT:</span> {user.jwtToken}</p>
              {user.errorMessage && <p><span>Error:</span> {user.errorMessage}</p>}
            </div>
          </>
        );
      case 'videos':
        return (
          <>
            <Videos />
          </>
        );
        case 'manualVideoAccess':
        return (
        <>
        <ManualvideoAccess />
        </>
        );

        case 'userManagement': // Caso para UserManagement
        return(
          <> 
          <UserManagement />
          </>
        );
      default:
        return null;
    }
  };

  return (
    <div>
      <div
        className="sidebar"
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
      >
        <div
          className="sidebar-icon"
          onClick={() => setCurrentView('profile')}
        >
          <FaUser />
          <span className="sidebar-text">Perfil</span>
        </div>
        <div
          className="sidebar-icon"
          onClick={() => setCurrentView('videos')}
        >
          <FaVideo />
          <span className="sidebar-text">Videos</span>
        </div>
        <div
          className="sidebar-icon"
          onClick={() => setCurrentView('manualVideoAccess')}
        >
          <FaToolbox />
          <span className="sidebar-text">Acceso Manual a Videos</span>
        </div>
        <div
          className="sidebar-icon"
          onClick={() => setCurrentView('userManagement')}
        >
          <FaUsers />
          <span className="sidebar-text">Gestión de Usuarios</span>
        </div>
      </div>
      <div className={isExpanded ? 'content-area-expanded' : 'content-area'}>
        {renderContent()}
      </div>
    </div>
  );
};

export default AdminView;
