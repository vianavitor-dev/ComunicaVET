package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.comment.CommentDTO;
import com.projetointegrador.comunicavet.dto.comment.NewCommentDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.CommentDTOMapper;
import com.projetointegrador.comunicavet.model.Comment;
import com.projetointegrador.comunicavet.model.User;
import com.projetointegrador.comunicavet.repository.CommentRepository;
import com.projetointegrador.comunicavet.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository repository;

    @Autowired
    private UserRepository userRepository;

    public void register(NewCommentDTO dto) throws NotFoundResourceException {
        User user = userRepository.findById(dto.writerId())
                .orElseThrow(() -> new NotFoundResourceException("Usuário não encontrado"));

        Comment comment = CommentDTOMapper.toComment(dto, user);
        comment.setReportCount(0);
        comment.setLikesCount(0);
        comment.activate();

        // Pega a data de hoje com base na "zona" informada
        Instant now = Instant.now();
        LocalDate localDateNow = ZonedDateTime.ofInstant(
                now,
                ZoneId.of("America/Sao_Paulo")
        ).toLocalDate();

        comment.setCreateAt(localDateNow);

        repository.save(comment);
    }

    public Iterable<CommentDTO> getAll() {
        List<CommentDTO> result = ((List<Comment>) repository.findAll())
                .stream()
                .map(CommentDTOMapper::toCommentDTO)
                .toList();

        return result;
    }

    public Iterable<CommentDTO> getByUser(@NotNull Long userId) throws NotFoundResourceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException("Usuário não encontrado"));

        List<CommentDTO> result = ((List<Comment>) repository.findByUser(user))
                .stream()
                .map(CommentDTOMapper::toCommentDTO)
                .toList();

        return result;
    }

    public CommentDTO getById(@NotNull Long id) throws NotFoundResourceException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        return CommentDTOMapper.toCommentDTO(comment);
    }

    public void editText(@NotNull Long id, String newText) throws NotFoundResourceException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        comment.setText(newText);
        repository.save(comment);
    }

    public void addLike(@NotNull Long id) throws NotFoundResourceException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        comment.setLikesCount(comment.getLikesCount() + 1);
        repository.save(comment);
    }

    public void removeLike(@NotNull Long id) throws NotFoundResourceException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        // Caso a contagem de likes do comentário seja igual ou menor que zero
        // ele não faz nada
        if (comment.getLikesCount() <= 0) {
            return;
        }

        // TODO: Fazer ele remover apenas o like que o próprio usuário adicionou
        comment.setLikesCount(comment.getLikesCount() - 1);
        repository.save(comment);
    }

    public void addReport(@NotNull Long id) throws NotFoundResourceException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        comment.setReportCount(comment.getReportCount() + 1);
        repository.save(comment);
    }

    public void removeReport(@NotNull Long id) throws NotFoundResourceException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        // Apenas remove a denuncia que o usuário fez
        if (comment.getReportCount() <= 0) {
            return;
        }

        // TODO: Fazer ele remover apenas a denuncia que o próprio usuário adicionou
        comment.setReportCount(comment.getReportCount() - 1);
        repository.save(comment);
    }

    public void deleteById(Long id) throws NotFoundResourceException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        repository.save(comment);
    }
}

