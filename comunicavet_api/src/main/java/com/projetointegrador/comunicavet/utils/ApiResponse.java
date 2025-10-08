package com.projetointegrador.comunicavet.utils;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;


/**
 * Este 'record' será utilizado como corpo
 * para as Responses que a API irá enviar ao Frontend.
 *
 * @param error Informa se houve um erro no processo
 * @param message Descreve o resultado do processo
 * @param <T> Tipo do dado a ser retornado
 * @param data Dado a ser retornado. Opcional
 */
public record ApiResponse <T> (
        @NotNull boolean error,
        @NotNull String message,
        @Nullable T data
){ }