package com.projetointegrador.comunicavet.dto.comment;

// Uso: quando criando um novo Comentário
public record NewCommentDTO(
        String text,
        Long writerId
) {
}
