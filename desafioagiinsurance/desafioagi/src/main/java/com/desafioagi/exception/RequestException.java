package com.desafioagi.exception;

public class RequestException extends RuntimeException{

    public RequestException(String mensagem) {
        super(mensagem);

    }
}