package org.tuprimernegocio.tuprimernegocio.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tuprimernegocio.tuprimernegocio.user.security.JwtTokenUtil;
import org.jooq.Record;
import org.jooq.Result;

import com.tuprimernegocio.library.database.jooq.users.routines.Spdeleteaddress;
import com.tuprimernegocio.library.database.jooq.users.routines.Spfindallusersbyadmin;
import com.tuprimernegocio.library.database.jooq.users.routines.Spfinduserbyemail;
import com.tuprimernegocio.library.database.jooq.users.routines.Spinsertuser;
import com.tuprimernegocio.library.database.jooq.users.routines.Spselectaddress;
import com.tuprimernegocio.library.database.jooq.users.routines.Spselectphones;
import com.tuprimernegocio.library.database.jooq.users.routines.Spupdateaddress;
import com.tuprimernegocio.library.database.jooq.users.routines.Spupdatephones;
import com.tuprimernegocio.library.database.jooq.users.routines.Spupdateuserpassword;
import com.tuprimernegocio.library.database.jooq.users.routines.Spvalidateuser;
import com.tuprimernegocio.library.database.jooq.users.tables.pojos.PersonalData;
import com.tuprimernegocio.library.database.jooq.users.tables.pojos.Users;
import com.tuprimernegocio.library.database.jooq.users.tables.records.AddressRecord;
import com.tuprimernegocio.library.database.jooq.users.tables.records.PhonesRecord;
import com.tuprimernegocio.library.database.jooq.users.tables.records.UserProfilepictureRecord;
import static com.tuprimernegocio.library.database.jooq.users.tables.UserProfilepicture.USER_PROFILEPICTURE;
import static com.tuprimernegocio.library.database.jooq.users.tables.Users.USERS_;

@Repository
public class UserRepository {

    @Autowired
    DSLContext dslContext;
    @Autowired
    org.jooq.Configuration configuration;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private Map<String, String> activeTokensByUser = new HashMap<>();

    public void insertUser(Users users, PersonalData personalData) {
        System.out.println(users.toString());
        System.out.println(personalData.toString());
        Spinsertuser insertuser = new Spinsertuser();

        // Asumiendo que el objeto 'users' tiene métodos para obtener email, contraseña
        // y nombre completo.
        insertuser.setPEmail(users.getEmail());
        insertuser.setPPassword(users.getPasswordHash());
        insertuser.setPFullName(personalData.getFullName());

        // Ejecutar el procedimiento almacenado
        insertuser.execute(configuration);
    }

    public ValidateUserResult validateUser(String email, String password) throws Exception {
        // Instancia del procedimiento almacenado
        Spvalidateuser spValidateUser = new Spvalidateuser();

        // Configurar los parámetros
        spValidateUser.setPEmail(email);
        spValidateUser.setPPassword(password);

        // Ejecutar el procedimiento almacenado
        spValidateUser.execute(configuration); // Asumiendo que 'configuration' es tu configuración de jOOQ

        // Aquí, tendrás que obtener el conjunto de resultados desde spValidateUser
        // Este paso puede variar dependiendo de cómo esté configurado jOOQ para manejar
        // los conjuntos de resultados.
        Result<Record> records = spValidateUser.getResults().get(0);

        ValidateUserResult result = new ValidateUserResult();

        if (records.isEmpty()) {
            throw new Exception("Invalid email or password");
        }

        Record record = records.get(0);

        result.userId = record.getValue("id", Integer.class);
        result.email = record.getValue("email", String.class);
        result.passwordHash = record.getValue("password_hash", String.class);
        result.fullName = record.getValue("full_name", String.class);
        result.userIsActive = record.getValue("user_is_active", Boolean.class); // Cambiado a Boolean
        result.adminIsActive = record.getValue("admin_is_active", Boolean.class); // Cambiado a Boolean

        // Campos añadidos
        result.addressLine1 = record.getValue("address_line1", String.class);
        result.addressLine2 = record.getValue("address_line2", String.class);
        result.province = record.getValue("province", String.class);
        result.canton = record.getValue("canton", String.class);
        result.postalCode = record.getValue("postal_code", String.class);
        result.whatsapp = record.getValue("whatsapp", String.class);
        result.otherNumbers = record.getValue("other_numbers", String.class);

        result.profilePicture = this.getUserProfilePicture(result.userId);

        return result;
    }

    public ValidateUserResult findUserByEmail(String email) throws Exception {
        // Instancia del procedimiento almacenado
        Spfinduserbyemail spFindUserByEmail = new Spfinduserbyemail();

        // Configurar los parámetros
        spFindUserByEmail.setPEmail(email);

        // Ejecutar el procedimiento almacenado
        spFindUserByEmail.execute(configuration);

        // Aquí, tendrás que obtener el conjunto de resultados desde spFindUserByEmail
        // Este paso puede variar dependiendo de cómo esté configurado jOOQ para manejar
        // los conjuntos de resultados.
        Result<Record> records = spFindUserByEmail.getResults().get(0);

        ValidateUserResult result = new ValidateUserResult();

        if (records.isEmpty()) {
            throw new Exception("No user found with the given email");
        }

        Record record = records.get(0);

        result.userId = record.getValue("id", Integer.class);
        result.email = record.getValue("email", String.class);
        result.passwordHash = record.getValue("password_hash", String.class);
        result.fullName = record.getValue("full_name", String.class);
        result.userIsActive = record.getValue("user_is_active", Boolean.class);
        result.adminIsActive = record.getValue("admin_is_active", Boolean.class);

        // Campos añadidos
        result.addressLine1 = record.getValue("address_line1", String.class);
        result.addressLine2 = record.getValue("address_line2", String.class);
        result.province = record.getValue("province", String.class);
        result.canton = record.getValue("canton", String.class);
        result.postalCode = record.getValue("postal_code", String.class);
        result.whatsapp = record.getValue("whatsapp", String.class);
        result.otherNumbers = record.getValue("other_numbers", String.class);

        result.profilePicture = this.getUserProfilePicture(result.userId);

        return result;
    }

    public String getCustomSecretByEmail(String email) throws Exception {
        ValidateUserResult result = findUserByEmail(email);
        return result.passwordHash;
    }

    public void updatePhones(Integer userId, String whatsapp, String otherNumbers) {
        Spupdatephones spUpdatePhones = new Spupdatephones();
        spUpdatePhones.setPUserId(userId);
        spUpdatePhones.setPWhatsapp(whatsapp);
        spUpdatePhones.setPOtherNumbers(otherNumbers);

        spUpdatePhones.execute(configuration);
    }

    public PhonesRecord selectPhones(Integer userId) {
        Spselectphones spSelectPhones = new Spselectphones();
        spSelectPhones.setPUserId(userId);
        spSelectPhones.execute(configuration);

        // Asumiendo que el primer resultado es el que nos interesa
        List<PhonesRecord> results = spSelectPhones.getResults().get(0).into(PhonesRecord.class);
        return results.isEmpty() ? null : results.get(0);
    }

    public AddressRecord selectAddress(Integer userId) {
        Spselectaddress spSelectAddress = new Spselectaddress();
        spSelectAddress.setPUserId(userId);
        spSelectAddress.execute(configuration);

        // Asumiendo que el primer resultado es el que nos interesa
        List<AddressRecord> results = spSelectAddress.getResults().get(0).into(AddressRecord.class);
        return results.isEmpty() ? null : results.get(0);
    }

    public void updateAddress(Integer userId, String addressLine1, String addressLine2, String province, String canton,
            String postalCode) {
        Spupdateaddress spUpdateAddress = new Spupdateaddress();
        spUpdateAddress.setPUserId(userId);
        spUpdateAddress.setPAddressLine1(addressLine1);
        spUpdateAddress.setPAddressLine2(addressLine2);
        spUpdateAddress.setPProvince(province);
        spUpdateAddress.setPCanton(canton);
        spUpdateAddress.setPPostalCode(postalCode);

        spUpdateAddress.execute(configuration);
    }

    public void deleteAddress(Integer userId) {
        Spdeleteaddress spDeleteAddress = new Spdeleteaddress();
        spDeleteAddress.setPUserId(userId);

        spDeleteAddress.execute(configuration);
    }

    public void updateUserProfilePicture(Integer userId, byte[] newProfilePicture) {
        // Encuentra la entrada existente de la imagen de perfil del usuario
        UserProfilepictureRecord existingRecord = dslContext
            .selectFrom(USER_PROFILEPICTURE)
            .where(USER_PROFILEPICTURE.USER_ID.eq(userId))
            .fetchOne();
    
        // Si existe, actualiza la imagen de perfil
        if (existingRecord != null) {
            existingRecord.setProfilePicture(newProfilePicture);
            existingRecord.update();
        } else {
            // Si no existe, crea una nueva entrada
            UserProfilepictureRecord newRecord = dslContext.newRecord(USER_PROFILEPICTURE);
            newRecord.setUserId(userId);
            newRecord.setProfilePicture(newProfilePicture);
            newRecord.store();
        }
    }
    

    public byte[] getUserProfilePicture(Integer userId) {
        UserProfilepictureRecord record = dslContext.selectFrom(USER_PROFILEPICTURE)
                .where(USER_PROFILEPICTURE.USER_ID.eq(userId))
                .fetchOne();

        return record != null ? record.getProfilePicture() : null;
    }

    // Reset Password

    public void updatePassword(String email, String newPassword) {
        Spupdateuserpassword spUpdateUserPassword = new Spupdateuserpassword();
        spUpdateUserPassword.setPEmail(email);
        spUpdateUserPassword.setPNewPassword(newPassword);

        spUpdateUserPassword.execute(configuration);
    }

    public boolean existsByEmail(String email) {
        // Realiza una consulta directa a la tabla 'users' para verificar si existe un
        // email
        return dslContext.selectCount()
                .from(USERS_)
                .where(USERS_.EMAIL.eq(email))
                .fetchOne(0, int.class) > 0;
    }

    public List<ValidateUserResult> findAllUsersByAdmin(Integer adminId) {
        // Instancia del procedimiento almacenado
        Spfindallusersbyadmin spFindAllUsersByAdmin = new Spfindallusersbyadmin();

        // Configurar los parámetros
        spFindAllUsersByAdmin.setAdminId(adminId);

        // Ejecutar el procedimiento almacenado
        spFindAllUsersByAdmin.execute(configuration);

        // Aquí, tendrás que obtener el conjunto de resultados desde spFindAllUsersByAdmin
        // Este paso puede variar dependiendo de cómo esté configurado jOOQ para manejar
        // los conjuntos de resultados.
        Result<Record> records = spFindAllUsersByAdmin.getResults().get(0);

        List<ValidateUserResult> results = new ArrayList<>();

        for (Record record : records) {
            ValidateUserResult result = new ValidateUserResult();

            result.userId = record.getValue("id", Integer.class);
            result.email = record.getValue("email", String.class);
         //   result.passwordHash = record.getValue("password_hash", String.class);
            result.fullName = record.getValue("full_name", String.class);
            result.userIsActive = record.getValue("user_is_active", Boolean.class);
            result.adminIsActive = record.getValue("admin_is_active", Boolean.class);

            // Campos añadidos
            result.addressLine1 = record.getValue("address_line1", String.class);
            result.addressLine2 = record.getValue("address_line2", String.class);
            result.province = record.getValue("province", String.class);
            result.canton = record.getValue("canton", String.class);
            result.postalCode = record.getValue("postal_code", String.class);
            result.whatsapp = record.getValue("whatsapp", String.class);
            result.otherNumbers = record.getValue("other_numbers", String.class);

            result.profilePicture = this.getUserProfilePicture(result.userId);

            results.add(result);
        }

        return results;
    }

    public static class ValidateUserResult {
        public Integer userId;
        public String email;
        public String passwordHash;
        public String fullName;
        public Boolean userIsActive;
        public Boolean adminIsActive;
        public String jwtToken;
        public Object errorMessage;

        // Campos añadidos
        public String addressLine1;
        public String addressLine2;
        public String province;
        public String canton;
        public String postalCode;
        public String whatsapp;
        public String otherNumbers;

        public byte[] profilePicture;

    }
}
