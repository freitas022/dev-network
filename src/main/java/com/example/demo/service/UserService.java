package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.PostDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String MESSAGE = "Registro não encontrado.";
    @Autowired
    UserRepository repository;

    public List<UserDTO> findAll() {
        List<User> users = repository.findAll();
        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public UserDTO findById(final String userId) {
        return repository.findById(userId)
                .map(UserDTO::new)
                .orElseThrow(() -> new ObjectNotFoundException(MESSAGE));
    }

    public UserDTO insert(UserDTO dto) {
        var user = new User();
        fromDTOToEntity(dto, user);
        user = repository.save(user);
        return new UserDTO(user);
    }

    public void delete(final String userId) {
        repository.delete(repository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(MESSAGE)));
    }

    public UserDTO update(UserDTO dto, final String userId) {
        return repository.findById(userId)
				.map(user -> {
					fromDTOToEntity(dto, user);
					repository.save(user);
					return new UserDTO(user);
				}).orElseThrow(() -> new ObjectNotFoundException(MESSAGE));
    }

    public List<PostDTO> findPosts(final String id) {
        return repository.findById(id)
                .map(user -> user.getPosts()
                        .stream()
                        .map(PostDTO::new)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ObjectNotFoundException(MESSAGE));
    }

    private void fromDTOToEntity(UserDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
    }
}
