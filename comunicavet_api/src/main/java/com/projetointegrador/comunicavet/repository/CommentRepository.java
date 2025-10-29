package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.Comment;
import com.projetointegrador.comunicavet.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByUser(User user);
    List<Comment> findByClinic(Clinic clinic);
}
