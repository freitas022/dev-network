package com.example.demo.controllers;

import com.example.demo.dto.PostDTO;
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
		try {
			return ResponseEntity.ok().body(service.findById(userId));
		}
		catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody UserDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
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
	public ResponseEntity<List<PostDTO>> findPosts(@PathVariable final String userId) {
			var list = service.findPosts(userId);
			return ResponseEntity.ok().body(list);


	}
}
