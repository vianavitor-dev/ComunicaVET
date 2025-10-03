package com.projetointegrador.comunicavet.exceptions;


/**
Essa exceção será lançada quando um determinado recurso no banco de dados não existir.
 **/
public class NotFoundResourceException extends Exception {
    public NotFoundResourceException(String message) {
        super(message);
    }
}
