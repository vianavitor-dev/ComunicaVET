package com.projetointegrador.comunicavet.dto.comment;

import java.time.LocalDate;

// Uso: quando retornando dados do Comentário; alterando;
public record CommentDTO(
        Long id,
        String text,
        String writerName,
        String writerEmail,
        LocalDate createdAt,
        int reportCount,
        int likesCount
) {
}
