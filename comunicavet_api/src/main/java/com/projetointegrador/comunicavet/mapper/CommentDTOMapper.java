package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.comment.CommentDTO;
import com.projetointegrador.comunicavet.dto.comment.NewCommentDTO;
import com.projetointegrador.comunicavet.model.Comment;
import com.projetointegrador.comunicavet.model.User;

public class CommentDTOMapper {
    public static Comment toComment(NewCommentDTO dto, User user) {
        Comment c = new Comment();
        c.setUser(user);
        c.setText(dto.text());

        return c;
    }

    public static Comment toComment(CommentDTO dto, User user) {
        Comment c = new Comment();
        c.setId(dto.id());
        c.setUser(user);
        c.setText(dto.text());
        c.setReportCount(dto.reportCount());
        c.setLikesCount(dto.likesCount());
        c.setCreateAt(dto.createdAt());

        return c;
    }

    public static CommentDTO toCommentDTO(Comment entity) {
        return new CommentDTO(
                entity.getId(), entity.getText(), entity.getUser().getName(),
                entity.getUser().getEmail(), entity.getCreateAt(), entity.getReportCount(),
                entity.getLikesCount()
        );
    }
}
