package com.example.authmoduls.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class EmailModel {
    String to;
    String subject;
    String message;
    String templateName;
    Set<String> bcc;
    Set<String> cc;
    File file;
}
