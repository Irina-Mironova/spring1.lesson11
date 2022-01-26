package ru.geekbrains.spring1.lesson11.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import ru.geekbrains.spring1.lesson11.services.FileService;

import java.util.UUID;

@RestController
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/storefile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("subtype") int subType) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        String fileHash = fileService.storeFile(file.getBytes(), file.getOriginalFilename(), subType);
        return ResponseEntity.ok(fileHash);
    }

    @GetMapping("/getfile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("hash") UUID hash) throws IOException {
        byte[] array = fileService.downloadFile(hash);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(array));
    }

    @GetMapping("/getfiles")
    public ResponseEntity<?> downloadFiles(@RequestParam("subtype") int subtype) {
        return ResponseEntity.ok(fileService.downloadFiles(subtype));
    }

    @DeleteMapping("/deletefile")
    public ResponseEntity<String> deleteFile(@RequestParam("hash") UUID hash) throws IOException {
        return ResponseEntity.ok(fileService.deleteFile(hash));
    }

}