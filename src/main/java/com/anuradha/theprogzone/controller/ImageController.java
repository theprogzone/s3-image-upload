package com.anuradha.theprogzone.controller;

import com.anuradha.theprogzone.dto.ImageDTO;
import com.anuradha.theprogzone.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/view")
    public ResponseEntity<?> viewImages() {
        return ResponseEntity.ok(imageService.getImageUrls());
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(ImageDTO imageDTO) {
        imageService.uploadImage(imageDTO);
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam(value = "id") String imageName) {
        imageService.deleteImage(imageName);
        return ResponseEntity.ok("Success");
    }
}
