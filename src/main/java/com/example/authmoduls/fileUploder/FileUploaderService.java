package com.example.authmoduls.fileUploder;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploaderService {


    String uploadFile(MultipartFile file, String path);
}
