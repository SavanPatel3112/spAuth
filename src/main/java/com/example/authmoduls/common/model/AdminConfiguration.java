package com.example.authmoduls.common.model;

import com.example.authmoduls.common.decorator.NotificationConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.*;

@Document(collection = "admin_config")
@Data
@AllArgsConstructor
@Component
@Builder
public class AdminConfiguration {

    @Id
    String id;
    String from;
    String username;
    String password;
    Set<String> requiredEmailItems = getRequiredItems();
    String host;
    String port;
    boolean smtpAuth;
    boolean starttls;
    String nameRegex;
    String emailRegex;
    String regex;
    String semesterRegex;
    String spiRegex;
    String passwordRegex;
    String mobileNoRegex;
    String createdBy;
    String updatedBy;
    Date created;
    Date updated;
    int otpVerify = 5 ;
    int importRecordLimit = 100;
    Set<String> extensions = getExtensionsData();
    Set<String> techAdmins = getTechAdminEmails();
    NotificationConfiguration notificationConfiguration;
    Map<String, String> userImportMappingFields = new LinkedHashMap<>();

    public AdminConfiguration() {
        this.from = "savan.p@techroversolutions.com";
        this.username = "savan.p@techroversolutions.com";
        this.password = "Techrovers@2022";
        this.host = "smtp.office365.com";
        this.port = "587";
        this.smtpAuth = true;
        this.starttls = true;
        this.extensions = getExtensionsData();
        this.emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        this.regex = "^(?=.{1,64}@)[a-z0-9_-]+(\\.[a-z0-9_-]+)*@" + "[^-][a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,})$";
        this.semesterRegex = "^[0-8]{1}$";
        this.spiRegex = "^[0-10]{2}$";
        this.passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,18}$";
        this.mobileNoRegex = "^[0-9]{10}$";
        this.nameRegex = "^[0-9#$@!%&*?.-_=]{1,15}$";
        this.notificationConfiguration = new NotificationConfiguration();
        userImportMappingFields.put("First Name", "firstName");
        userImportMappingFields.put("Last Name", "lastName");
        userImportMappingFields.put("Middle Name", "middleName");
        userImportMappingFields.put("Address", "Address");
        userImportMappingFields.put("City", "city");
        userImportMappingFields.put("State", "state");
        userImportMappingFields.put("Email", "email");
        userImportMappingFields.put("UserName", "userName");
        userImportMappingFields.put("MobileNo", "mobileNo");
    }

    private Set<String> getRequiredItems() {
        Set<String> requiredEmailItems = new HashSet<>();
        requiredEmailItems.add("@");
        return requiredEmailItems;
    }

    private Set<String> getExtensionsData() {
        Set<String> extensions = new HashSet<>();
        extensions.add("gmail.com");
        extensions.add("yahoo.com");
        return extensions;
    }

    private Set<String> getTechAdminEmails() {
        Set<String> emails = new HashSet<>();
        emails.add("savan.p@techroversolutions.com");
        return emails;
    }


}
