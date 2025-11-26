package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.exceptions.DuplicateResourceException;
import com.projetointegrador.comunicavet.exceptions.InvalidCredentialsException;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
        Lida com as exceções do tipo NotFoundResourceException.
        Esta a exceção é disparada quando uma pesquisa ao
        Banco de Dados na qual é esperada retornar algo,
        não retorna nenhum valor
     */
    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundResourceException(
            NotFoundResourceException ex
    ) {
        /*
            Retorna uma ResponseEntity para o Frontend, informando que houve erro,
            assim como um status 404 (not found) com uma mensagem
            informando sobre a causa da Exceção
         */
        return new ResponseEntity<>(
                new ApiResponse<>(true, ex.getMessage(), null),
                HttpStatus.NOT_FOUND
        );
    }

    /*
        Lida com as exceções do tipo DuplicateResourceException.
        Esta exceção é disparada quando é detectado a presença
        de outro registro igual a este na qual é esperado que todos
        os registros sejam únicos
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateResourceException(
            DuplicateResourceException ex
    ) {
        /*
            Retorna uma ResponseEntity para o Frontend, informando que houve erro,
            assim como um status 400 (bad request) com uma mensagem
            informando sobre a causa da Exceção
         */
        return new ResponseEntity<>(
                new ApiResponse<>(true, ex.getMessage(), null),
                HttpStatus.BAD_REQUEST
        );
    }

    /*
        Lida com as exceções do tipo InvalidCredentialsException.
        Esta exceção é disparada quando o usuário comete algum erro
        ao preencher a suas crediencias na tentativa de logar no sistema
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidCredentialsException(
            InvalidCredentialsException ex
    ) {
        /*
            Retorna uma ResponseEntity para o Frontend, informando que houve erro,
            assim como um status 400 (bad request) com uma mensagem
            informando sobre a causa da Exceção
         */
        return new ResponseEntity<>(
                new ApiResponse<>(true, ex.getMessage(), null),
                HttpStatus.BAD_REQUEST
        );
    }

    /*
        Lida com as exceções do tipo IllegalAccessException.
        Esta exceção é disparada quando o sistema tenta acessar
        um dado que no qual não tem permissão
     */
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<?> handleIllegalAccessException(IllegalAccessException ex) {
        /*
            Retorna uma ResponseEntity para o Frontend, informando que houve erro,
            assim como um status 500 (internal server error) com uma mensagem
            informando sobre a causa da Exceção
         */
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(true, ex.getMessage(), null));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<?>> handleIOException(
            IOException ex
    ) {
        return new ResponseEntity<>(
                new ApiResponse<>(true, ex.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
