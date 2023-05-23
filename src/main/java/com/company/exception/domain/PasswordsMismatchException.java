package com.company.exception.domain;

public class PasswordsMismatchException extends Exception{
    public PasswordsMismatchException(String message){
        super(message);
    }
}
