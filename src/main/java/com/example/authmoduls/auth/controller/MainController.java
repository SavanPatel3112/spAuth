package com.example.authmoduls.auth.controller;

import com.example.authmoduls.fileUploder.FileManager;
import com.example.authmoduls.fileUploder.FileUploaderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@AllArgsConstructor
@Slf4j
public class MainController {
    private final FileManager fileManager;
    private final FileUploaderService fileUploaderService;

/*    @GetMapping({"/"})
    public ResponseEntity<List<File>> listEverything() throws IOException, GeneralSecurityException {
        List<File> files = fileManager.listEverything();
        return ResponseEntity.ok(files);
    }

    @GetMapping({"/list","/list/{parentId}"})
    public ResponseEntity<List<File>> list(@PathVariable(required = false) String parentId) throws IOException, GeneralSecurityException {
        List<File> files = fileManager.listFolderContent(parentId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws IOException, GeneralSecurityException {
        fileManager.downloadFile(id, response.getOutputStream());
    }

    @GetMapping("/directory/create")
    public ResponseEntity<String> createDirectory(@RequestParam String path) throws Exception {
        String parentId = fileManager.getFolderId(path);
        return ResponseEntity.ok("parentId: "+parentId);
    }*/

    @RequestMapping( name = "uploadFile",value = "/upload", method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
     public ResponseEntity<String> uploadFile(@RequestBody MultipartFile[] files, @RequestParam(required = false) String path) {
        log.info("fileUpload start");
        if (files!=null) {
            int fileSize = files.length;
            log.info("length:{}",fileSize);
            AtomicReference<String> fileId = new AtomicReference<>("");
            log.info("fileId:{}",fileId);
            AtomicReference<String> fileName = new AtomicReference<>("");
            log.info("fileName:{}",fileName);
            Arrays.asList(files).forEach(
                    file -> {
                        fileId.set(fileUploaderService.uploadFile(file, path));
                        fileName.set(file.getOriginalFilename());
                    }
            );

            if (fileSize > 1) {
                return ResponseEntity.ok("files uploaded successfully");
            }
            return ResponseEntity.ok(fileName + ", uploaded successfully");
        }
        return ResponseEntity.ok("Success");
    }

/*    @GetMapping("/delete/{id}")
    public void delete(@PathVariable String id) throws Exception {
        fileManager.deleteFile(id);
    }

    @RequestMapping(value = "/preview/cv", method = RequestMethod.GET)
    protected String preivewSection(
            HttpServletRequest request,
            HttpSession httpSession,
            HttpServletResponse response) {
        try {
            byte[] documentInBytes = Files.readAllBytes(Path.of("root"));
            response.setDateHeader("Expires", -1);
            response.setContentType("application/pdf");
            response.setContentLength(documentInBytes.length);
            response.getOutputStream().write(documentInBytes);
        } catch (Exception ignored) {
        }
        return null;
    }*/
}