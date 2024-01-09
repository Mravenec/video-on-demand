import React from 'react';
import './BodyHome.css';


const BodyHome = () => {
  return (
    <div className="body-home">
      <img src={"/TPNlogo.png"} alt="Logo de TUPRIMERNEGOCIO" />
      <p className="quote">
      El conocimiento es poder, aprende a generar más dinero,<br /> 
      solamente con la información que te voy a brindar.
      </p>
    </div>
  );
};

export default BodyHome;
