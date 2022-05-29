package com.anuradha.theprogzone.service;

import com.anuradha.theprogzone.dto.ImageDTO;

import java.util.List;

public interface ImageService {
    void uploadImage(ImageDTO imageDTO);

    void deleteImage(String imageName);

    List<String> getImageUrls();
}
