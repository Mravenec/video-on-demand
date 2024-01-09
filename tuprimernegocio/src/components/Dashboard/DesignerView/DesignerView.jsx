import React from 'react';
import { useSelector } from 'react-redux';

const DesignerView = () => {
  const user = useSelector((state) => state.auth.user);

  return (
    <div>
      <h2>Panel del Diseñador</h2>
      <p>Bienvenido, {user?.fullName}</p>
      <p>ID de Usuario: {user?.userId}</p>
      <p>Email: {user?.email}</p>
      <p>Usuario Activo: {String(user?.userIsActive)}</p>
      <p>Admin Activo: {String(user?.adminIsActive)}</p>
      <p>Token JWT: {user?.jwtToken}</p>
      <p>Mensaje de error: {user?.errorMessage || 'Ninguno'}</p>
      {/* Aquí puedes agregar más características y funciones específicas para el Diseñador */}
    </div>
  );
};

export default DesignerView;
