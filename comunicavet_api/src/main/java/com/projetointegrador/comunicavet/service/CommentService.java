package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.comment.CommentDTO;
import com.projetointegrador.comunicavet.dto.comment.NewCommentDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.CommentDTOMapper;
import com.projetointegrador.comunicavet.model.Comment;
import com.projetointegrador.comunicavet.model.LikedComment;
import com.projetointegrador.comunicavet.model.User;
import com.projetointegrador.comunicavet.repository.CommentRepository;
import com.projetointegrador.comunicavet.repository.LikedCommentRepository;
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

    @Autowired
    private LikedCommentRepository likedCommentRepository;

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

    public void addLike(@NotNull Long id, @NotNull Long userId)
            throws NotFoundResourceException {

        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        // Verifica se este Usuário já curtiu este comentário antes
        boolean wasLikedBefore = likedCommentRepository
                .findByUserIdAndComment(userId, comment).isPresent();

        if (wasLikedBefore) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException("Usuário não encontrado"));

        // Registra no Banco de Dados que o Usuário curtiu este Comentário
        LikedComment likedComment = new LikedComment();
        likedComment.setUser(user);
        likedComment.setComment(comment);

        likedCommentRepository.save(likedComment);

        comment.setLikesCount(comment.getLikesCount() + 1);
        repository.save(comment);
    }

    public void removeLike(@NotNull Long id, @NotNull Long userId)
            throws NotFoundResourceException {

        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        // Caso a contagem de likes do Comentário seja igual ou menor que zero
        // a função não prossegue
        if (comment.getLikesCount() <= 0) {
            return;
        }

        // Verifica se este Usuário já curtiu este comentário antes
        LikedComment likedComment = likedCommentRepository
                .findByUserIdAndComment(userId, comment)
                .orElseThrow(() -> new NotFoundResourceException("Este Comentário não foi curtido ainda"));

        likedCommentRepository.delete(likedComment);

        comment.setLikesCount(comment.getLikesCount() - 1);
        repository.save(comment);

    }

    public void deleteById(Long id) throws NotFoundResourceException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Comentário não encontrado"));

        repository.save(comment);
    }
}

