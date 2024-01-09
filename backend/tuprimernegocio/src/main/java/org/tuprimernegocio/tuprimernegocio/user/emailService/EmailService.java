package org.tuprimernegocio.tuprimernegocio.user.emailService;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tuprimernegocio.library.database.jooq.email_config.tables.pojos.EmailConfig;

import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private EmailConfigRepository emailConfigRepository;

    private JavaMailSenderImpl mailSender;

    private void configureMailSender() {
        EmailConfig config = emailConfigRepository.getEmailConfig();
        mailSender = new JavaMailSenderImpl();

        // Asegúrate de que las propiedades SMTP estén correctamente configuradas
        mailSender.setHost(new String(config.getHost()));
        mailSender.setPort(Integer.parseInt(new String(config.getPort())));
        mailSender.setUsername(new String(config.getUsername()));
        mailSender.setPassword(new String(config.getPassword()));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", config.getSmtpAuth() == 1);
        props.put("mail.smtp.starttls.enable", config.getStarttlsEnabled() == 1);
    }

     public void sendEmail(String to, String subject, String text) {
        if (mailSender == null) {
            configureMailSender();
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@tuprimernegocio.org"); // Establece el remitente como el usuario del servidor SMTP
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
        } catch (MailException e) {
            // Manejar excepciones relacionadas con el envío de correo electrónico
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }
    }
}
