import React, { useEffect } from 'react';  // Importar useEffect
import { useDispatch } from 'react-redux';  // Importar useDispatch
import { initializeAuth } from './store/actions/authActions';  // Importar initializeAuth
import { useLocation } from 'react-router-dom';
import Navbar from './components/Navbar/Navbar';
import Footer from './components/Footer/Footer';
import ResetPassword from './components/Dashboard/ResetPassword/ResetPassword';
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate
} from 'react-router-dom';
import { useSelector } from 'react-redux';

import PaymentView from './components/Dashboard/UserView/Payment/PaymentView';


// Home
import HeaderHome from './components/pages/Home/HeaderHome';
import BodyHome from './components/pages/Home/BodyHome';

// QuienesSomos
import HeaderQuienesSomos from './components/pages/QuienesSomos/HeaderQuienesSomos';
import BodyQuienesSomos from './components/pages/QuienesSomos/BodyQuienesSomos';

// Anuncios
//import HeaderComoIniciar from './components/pages/ComoIniciar/HeaderComoIniciar';
import BodyComoIniciar from './components/pages/ComoIniciar/BodyComoIniciar';

// Testimonios
import BodyTestimonios from './components/pages/Testimonios/BodyTestimonios';

// Contactenos
import HeaderContactenos from './components/pages/Contactenos/HeaderContactenos';
import BodyContactenos from './components/pages/Contactenos/BodyContactenos';

// IniciarSesion
//import HeaderIniciarSesion from './components/pages/IniciarSesion/HeaderIniciarSesion';
import BodyIniciarSesion from './components/pages/IniciarSesion/BodyIniciarSesion'; 
import LoginForm from './components/pages/IniciarSesion/LoginForm';
import SignupForm from './components/pages/IniciarSesion/SignupForm';
import ForgotPasswordForm from './components/pages/IniciarSesion/ForgotPasswordForm';

// Dashboard
import Dashboard from './components/Dashboard/Dashboard';

import AdminView from './components/Dashboard/AdminView/AdminView';
import UserView from './components/Dashboard/UserView/UserView';

function PrivateRoute({ children }) {
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  return isAuthenticated ? children : <Navigate to="/iniciar-sesion" />;
}

function AppWrapper() {
  const location = useLocation();
  const showNavbar = location.pathname !== '/dashboard';
  return (
    <>
      {showNavbar && <Navbar />}
      <Routes>
        <Route path="/" element={<><HeaderHome /><BodyHome /></>} />
        <Route path="/quienes-somos" element={<><HeaderQuienesSomos /><BodyQuienesSomos /></>} />
        <Route path="/testimonios" element={<BodyTestimonios />}/>
        <Route path="/como-iniciar" element={<><BodyComoIniciar /></>} />

        <Route path="/contactenos" element={<><HeaderContactenos /><BodyContactenos /></>} />
        <Route path="/iniciar-sesion/*" element={<><BodyIniciarSesion /></>}>
          <Route index element={<LoginForm />} />
          <Route path="registrarse" element={<SignupForm />} />
          <Route path="olvide-contrasena" element={<ForgotPasswordForm />} />
        </Route>
        <Route path="/dashboard/*" element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        }>
          <Route path="admin/videos" element={<AdminView />} />
          <Route path="user/videos" element={<UserView />} />
          <Route path="perfil" element={<h1>Perfil</h1>} /> {/* Ruta para la vista de perfil */}
          <Route path="videos" element={<h1>Videos</h1>} /> {/* Ruta para la vista de videos */}
        </Route>
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/payment" element={<PaymentView />} /> {/* Ruta agregada para PaymentView */}
      </Routes>
      <Footer />
    </>
  );
}

function App() {
  const dispatch = useDispatch();  // Inicializar useDispatch

  useEffect(() => {
    dispatch(initializeAuth());  // Inicializar el estado de autenticación
  }, [dispatch]);  // Añadir dispatch como dependencia

  return (
    <Router>
      <AppWrapper />
    </Router>
  );
}

export default App;