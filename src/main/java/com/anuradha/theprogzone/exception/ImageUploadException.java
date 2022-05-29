package com.anuradha.theprogzone.exception;

import lombok.Data;

@Data
public class ImageUploadException extends RuntimeException {
    private String code;

    public ImageUploadException(String message, String code) {
        super(message);
        this.code = code;
    }
}
