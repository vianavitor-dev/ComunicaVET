package com.projetointegrador.comunicavet.exceptions;


/**
Essa exceção será lançada quando um determinado recurso no banco de dados não existir.
 **/
public class NotFoundResourceException extends RuntimeException {
    public NotFoundResourceException(String message) {
        super(message);
    }
}
