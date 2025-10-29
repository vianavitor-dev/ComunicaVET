package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Comment;
import com.projetointegrador.comunicavet.model.LikedComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikedCommentRepository extends CrudRepository<LikedComment, Long> {
    Optional<LikedComment> findByUserIdAndComment(Long userId, Comment comment);
}
