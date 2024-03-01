import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

const Navbar = () => {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <nav className="navbar">
      <img src={"/TPNlogo.png"} alt="TPN Logo" className="navbar-logo" />
      <h1 className="navbar-title">Tu Primer Negocio</h1>
      <div className="hamburger-menu" onClick={() => setMenuOpen(!menuOpen)}>
        <div className="bar"></div>
        <div className="bar"></div>
        <div className="bar"></div>
      </div>
      <ul className={`navbar-list ${menuOpen ? 'active' : ''}`}>
        <li><Link to="/" onClick={() => setMenuOpen(false)}>Inicio</Link></li>
        <li><Link to="/quienes-somos" onClick={() => setMenuOpen(false)}>¿Quiénes somos?</Link></li>
        <li><Link to="/testimonios" onClick={() => setMenuOpen(false)}>Testimonios</Link></li>
        <li><Link to="/como-iniciar" onClick={() => setMenuOpen(false)}>¿Como iniciar?</Link></li>

        <li><Link to="/contactenos" onClick={() => setMenuOpen(false)}>Contáctenos</Link></li>
        <li><Link to="/iniciar-sesion" onClick={() => setMenuOpen(false)}>Iniciar sesión</Link></li>
      </ul>
    </nav>
  );
}

export default Navbar;
