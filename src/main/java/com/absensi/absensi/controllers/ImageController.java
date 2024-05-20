package com.absensi.absensi.controllers;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.jsonwebtoken.io.IOException;

@Controller
@RequestMapping("/api/v1/image")
public class ImageController {
    private static final String UPLOAD_DIR = "./uploads/";

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> viewImage(@PathVariable String fileName) throws MalformedURLException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

}
