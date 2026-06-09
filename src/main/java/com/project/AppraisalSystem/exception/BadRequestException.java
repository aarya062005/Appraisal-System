package com.project.AppraisalSystem.exception;

public class BadRequestException extends RuntimeException{
    public  BadRequestException(String message) {
        super(message);
    }
}
