package com.anuradha.theprogzone.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageDTO {
    private MultipartFile image;
}
