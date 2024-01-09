package org.tuprimernegocio.tuprimernegocio.user.emailService;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.tuprimernegocio.library.database.jooq.email_config.routines.Selectdecryptedemailconfig;
import com.tuprimernegocio.library.database.jooq.email_config.tables.pojos.EmailConfig;

import java.util.List;

@Repository
public class EmailConfigRepository {

    @Autowired
    private DSLContext dslContext;

    public EmailConfig getEmailConfig() {
        // Instancia del procedimiento almacenado
        Selectdecryptedemailconfig selectDecryptedEmailConfig = new Selectdecryptedemailconfig();

        // Ejecutar el procedimiento almacenado
        selectDecryptedEmailConfig.execute(dslContext.configuration());

        // Obtener el conjunto de resultados y mapearlo directamente al POJO EmailConfig
        List<EmailConfig> emailConfigList = selectDecryptedEmailConfig.getResults().get(0).into(EmailConfig.class);

        if (emailConfigList.isEmpty()) {
            throw new RuntimeException("No email configuration found");
        }

        return emailConfigList.get(0);
    }
}