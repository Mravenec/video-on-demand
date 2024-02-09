import React from 'react';
import './BodyContactenos.css';
import { HiOutlineMail} from "react-icons/hi";
import { FaWhatsapp, FaFacebook } from "react-icons/fa";
import { FaUserTie } from "react-icons/fa6";
import { GrInstagram } from "react-icons/gr";


function BodyContactenos() {
    return (
        <div className="body-contactenos">
         
            {
                /*<div className="contact-info">
                <h2>Encuéntranos</h2>
               <iframe
                    title="location-map"
                    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3929.304570573929!2d-84.15163638464237!3d10.039860392828174!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x8fa0e366d24a7a7d%3A0x7040f1b1e1a1e1!2sUnnamed%20Road%2C%20Provincia%20de%20Alajuela%2C%20Alajuela!5e0!3m2!1ses!2scr!4v1616662243506!5m2!1ses!2scr"
                    width="600"
                    height="450"
                    style={{ border:0 }}
                    allowFullScreen=""
                    loading="lazy"
                ></iframe>
                <p className="address">
                    Dirección: <br />
                    Santa Bárbara, Heredia, 40401, Costa Rica
                </p>
            </div>
            */}
            <div className="contact-details">
            <h2>Póngase en contacto con nosotros</h2>

            <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
            <HiOutlineMail size={50} style={{ marginRight: '10px' }}/> 
                <p> Correo electrónico: <a href="mailto:info@roberto@tuprimernegocio.org" aria-label="info@tuprimernegocio.org">info@tuprimernegocio.org</a> para recibir más información general.</p>
                
                </div>

                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                <FaUserTie size={40} style={{ marginRight: '10px' }}/>
                <p>Encargado directo: <a href="mailto:roberto@tuprimernegocio.org" aria-label="mailto:roberto@tuprimernegocio.org">roberto@tuprimernegocio.org</a></p>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                <FaWhatsapp size={40} style={{ marginRight: '10px' }}/> 
                <p>Whatsapp: <a href="https://wa.me/50663244284" aria-label="https://wa.me/50663244284">+(506) 6324 4284</a></p>
            </div>
            </div>


            <div className="contact-info">
              <h2>Búsquenos en nuestras redes sociales </h2>

              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                <GrInstagram size={40} style={{ marginRight: '10px', color: '#db34d3'}}/> 
                <a href="https://www.instagram.com/tuprimernegociocr/" aria-label="https://www.instagram.com/tuprimernegociocr/">@tuprimernegociocr</a>
            </div>

            <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                <FaFacebook size={40} style={{ marginRight: '10px', color: 'blue'}}/> 
                <a href="https://www.fb.com" aria-label="https://www.fb.com">Facebook</a>

            </div>
              </div>
            </div>
        

        
    );
}

export default BodyContactenos;
