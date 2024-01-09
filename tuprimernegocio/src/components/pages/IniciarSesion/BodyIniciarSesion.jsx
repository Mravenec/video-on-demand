import React from 'react';
import { Outlet } from 'react-router-dom';

function BodyIniciarSesion() {
    return (
        <div className="body-iniciar-sesion">
            <Outlet />
        </div>
    );
}

export default BodyIniciarSesion;
