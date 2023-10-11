package com.example.demo.controllers;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService service;

	@GetMapping
	public ResponseEntity<List<UserDTO>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}

	@GetMapping(value = "/{userId}")
	public ResponseEntity<UserDTO> findById(@PathVariable String userId) {
		User obj = service.findById(userId);
		return ResponseEntity.ok().body(new UserDTO(obj));
	}

	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody UserDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@DeleteMapping(value = "/{userId}")
	public ResponseEntity<Void> delete(@PathVariable String userId) {
		service.delete(userId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{userId}")
	public ResponseEntity<UserDTO> update(@RequestBody UserDTO dto, @PathVariable final String userId) {
		dto = service.update(dto, userId);
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping(value = "/{userId}/posts")
	public ResponseEntity<List<Post>> findPosts(@PathVariable final String userId) {
		var user = service.findById(userId);
		return ResponseEntity.ok().body((user.getPosts()));
	}
}
