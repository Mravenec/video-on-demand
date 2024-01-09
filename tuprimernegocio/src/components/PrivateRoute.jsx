// src/components/PrivateRoute.js
import { useSelector } from 'react-redux';
import { Route, Navigate } from 'react-router-dom';

export const PrivateRoute = ({ children, ...props }) => {
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

  return (
    <Route {...props} element={isAuthenticated ? children : <Navigate to="/iniciar-sesion" />} />
  );
};

