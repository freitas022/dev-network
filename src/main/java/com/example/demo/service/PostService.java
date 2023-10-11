package com.example.demo.service;

import com.example.demo.dto.PostDTO;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PostService {

    private static final String MESSAGE = "Registro não encontrado.";
    @Autowired
    PostRepository repository;

    public PostDTO findById(String postId) {
        var post = repository.findById(postId)
                .orElseThrow(() -> new ObjectNotFoundException(MESSAGE));
        return new PostDTO(post);
    }

    public List<PostDTO> findByTitle(String title) {
        return repository.searchTitle(title)
                .stream()
                .map(PostDTO::new)
                .toList();
    }

    public List<PostDTO> fullSearch(String text, Instant minDate, Instant maxDate) {
        maxDate = maxDate.plusSeconds(86400); // 24 * 60 * 60
        return repository.fullSearch(text, minDate, maxDate)
                .stream()
                .map(PostDTO::new)
                .toList();
    }
}
