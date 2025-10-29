package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.comment.CommentDTO;
import com.projetointegrador.comunicavet.dto.comment.NewCommentDTO;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.Comment;
import com.projetointegrador.comunicavet.model.User;

public class CommentDTOMapper {
    public static Comment toComment(NewCommentDTO dto, User user, Clinic clinic) {
        Comment c = new Comment();
        c.setUser(user);
        c.setText(dto.text());
        c.setClinic(clinic);

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
