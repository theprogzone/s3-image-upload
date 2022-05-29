package com.anuradha.theprogzone.exception;

import lombok.Data;

@Data
public class ConvertingMultiPartToFileException extends RuntimeException {
    private String code;

    public ConvertingMultiPartToFileException(String message, String code) {
        super(message);
        this.code = code;
    }
}
