package com.realestate.propertyapp.controller;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.realestate.propertyapp.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Value;




@RestController
public class ImageController {
    // images folder path
    @Value("${file.upload-dir}")
    private String uploadDir;
    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            // Have to check that file exists
            if (resource.exists() || resource.isReadable()) { 
                String contentType = Files.probeContentType(file);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .body(resource);
            } else {
                throw new ResourceNotFoundException("File not found or not readable: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL for file: " + filename, e);
        } catch (IOException e) {
            throw new RuntimeException("I/O error while handling file: " + filename, e);
        }
    }
}