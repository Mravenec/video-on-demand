
import './BodyQuienesSomos.css';

function BodyQuienesSomos() {
  //const [image, setImage] = useState('Fotot1n.jpeg');
//  const [isFading, setIsFading] = useState(false);

 // useEffect(() => {
   // const timer = setTimeout(() => {
    //  setIsFading(true);

     // setTimeout(() => {
       // setImage(image === 'around.jpeg' ? 'front.jpeg' : 'around.jpeg');
        //setIsFading(false);
      //}, 200); // Esto permite un segundo para la transición de opacidad
    //}, 3600);

    //return () => clearTimeout(timer);
 // }, [image]);

  return (
    <div className="body-quienes-somos">
      <div className="text-content">
        <p>
          En "Tu Primer Negocio" contamos con más de 
          12 años de experiencia en la industria automotriz, destacándonos
          por nuestra profunda comprensión del sector.
          <br />
          <br />
          En los últimos 2 años, hemos logrado una rentabilidad excepcional
          superando el medio millón de dólares, gracias a un método sencillo que sólo
          requiere un teléfono celular.
          <br />
          <br />

          Nos destacamos por nuestra capacidad para adaptarnos a las tendencias actuales y aprovechar 
          la tecnología de manera innovadora.
          <br />
          <br />

          Únete a nosotros para experimentar una nueva era en la industria automotriz 
          y descubre cómo "Tu primer negocio" puede ser una puerta hacia el éxito financiero.
          
        </p> 
     
      </div>
      <div className={`image-content`}>
        <img src={`./pages/QuienesSomosPictures/Fotot1n.jpeg`} alt="Quienes Somos" />
      </div>
      </div>
  );
}

export default BodyQuienesSomos;
