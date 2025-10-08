package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.comment.CommentDTO;
import com.projetointegrador.comunicavet.dto.comment.NewCommentDTO;
import com.projetointegrador.comunicavet.service.CommentService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@CrossOrigin("*")
public class CommentController {

    @Autowired
    private CommentService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> register(@RequestBody NewCommentDTO dto) {
        service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(false, "Comentário criado", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<CommentDTO>>> getAll() {
        var list = service.getAll();
        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de comentários", list));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Iterable<CommentDTO>>> getByUser(@PathVariable Long userId) {
        var list = service.getByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(false, "Comentários do usuário", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> getById(@PathVariable Long id) {
        var dto = service.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Comentário encontrado", dto));
    }

    @PatchMapping("/{id}/text")
    public ResponseEntity<ApiResponse<?>> editText(@PathVariable Long id, @RequestParam("newText") String newText) {
        service.editText(id, newText);
        return ResponseEntity.ok(new ApiResponse<>(false, "Texto do comentário alterado", null));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<?>> addLike(@PathVariable Long id) {
        service.addLike(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Like adicionado", null));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<?>> removeLike(@PathVariable Long id) {
        service.removeLike(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Like removido", null));
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<ApiResponse<?>> addReport(@PathVariable Long id) {
        service.addReport(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Relato adicionado", null));
    }

    @DeleteMapping("/{id}/report")
    public ResponseEntity<ApiResponse<?>> removeReport(@PathVariable Long id) {
        service.removeReport(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Relato removido", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Comentário removido", null));
    }
}
