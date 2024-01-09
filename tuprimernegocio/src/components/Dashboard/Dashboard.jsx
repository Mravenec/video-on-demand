import React, { useState, useEffect, useCallback } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { logout } from '../../store/actions/authActions';
import UserView from './UserView/UserView';
import AdminView from './AdminView/AdminView';
import DesignerView from './DesignerView/DesignerView';
import './Dashboard.css';

const Dashboard = () => {
  const [viewMode, setViewMode] = useState('');  // Inicializar con cadena vacía
  const user = useSelector((state) => state.auth.user);
  const dispatch = useDispatch();

  const handleLogout = useCallback(() => {
    dispatch(logout());
  }, [dispatch]);

  useEffect(() => {
    if (user?.adminIsActive) setViewMode('admin');
    else if (user?.userIsActive) setViewMode('user');
    else setViewMode('');
  }, [user]);

  const renderView = useCallback(() => {
    if (!user) {
      return <p>Cargando...</p>;
    }

    switch (viewMode) {
      case 'admin':
        return <AdminView />;
      case 'designer':
        return <DesignerView />;
      case 'user':
        return <UserView />;
      case '':
        return <p>Selecciona un modo de vista</p>;
      default:
        return <p>Modo de vista no reconocido</p>;
    }
  }, [user, viewMode]);

  return (
    <div className="dashboard-container">
      <div className="header">
        {user?.adminIsActive && (
          <select 
            onChange={(e) => setViewMode(e.target.value)} 
            value={viewMode} 
            className="view-selector"
          >
            <option value="" disabled>Selecciona un modo</option>
            <option value="admin">Administrador</option>
            <option value="designer">Diseñador</option>
            {user?.userIsActive && <option value="user">Usuario</option>}
          </select>
        )}
        <button onClick={handleLogout} className="logout-button">
          Logout
        </button>
      </div>
      <div className="h1-container">
        <h1>{user ? `Bienvenido, ${user.fullName}` : 'Cargando...'}</h1>
      </div>
      {renderView()}
    </div>
  );
};

export default Dashboard;
