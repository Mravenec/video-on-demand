import React, { useState, useEffect } from 'react';
import './BodyQuienesSomos.css';

function BodyQuienesSomos() {
  const [image, setImage] = useState('around.jpeg');
  const [isFading, setIsFading] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsFading(true);

      setTimeout(() => {
        setImage(image === 'around.jpeg' ? 'front.jpeg' : 'around.jpeg');
        setIsFading(false);
      }, 200); // Esto permite un segundo para la transición de opacidad
    }, 3600);

    return () => clearTimeout(timer);
  }, [image]);

  return (
    <div className="body-quienes-somos">
      <div className="text-content">
        <p>
          Somos una empresa seria con más de 12 años de
          experiencia en el mercado vehicular y en la que 
          nos comprometemos a ayudarte a generar 
          ingresos por medio de esta industria.
        </p>
        <p>
          No será necesario utilizar capital de inicio, <br /> 
          ¡apresurate, ven y aprende con nosotros!
        </p>
      </div>
      <div className={`image-content ${isFading ? 'fading' : ''}`}>
        <img src={`./pages/QuienesSomosPictures/${image}`} alt="Quienes Somos" />
      </div>
    </div>
  );
}

export default BodyQuienesSomos;
