package com.example.demo.controllers;

import com.example.demo.dto.PostDTO;
import com.example.demo.controllers.util.URL;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

    @Autowired
    PostService service;

    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostDTO> findById(@PathVariable String postId) {
        var dto = service.findById(postId);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(path = "/titlesearch")
    public ResponseEntity<List<PostDTO>> findByTitle(
            @RequestParam(value = "text", defaultValue = "") String title)
			throws UnsupportedEncodingException {

        title = URL.decodeParam(title);
        var listDto = service.findByTitle(title);
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/fullsearch")
    public ResponseEntity<List<PostDTO>> fullSearch(
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "minDate", defaultValue = "") String minDate,
            @RequestParam(value = "maxDate", defaultValue = "") String maxDate)
            throws UnsupportedEncodingException, ParseException {

        text = URL.decodeParam(text);
        Instant min = URL.convertDate(minDate, Instant.EPOCH);
        Instant max = URL.convertDate(maxDate, Instant.now());

        var listDto = service.fullSearch(text, min, max);
        return ResponseEntity.ok().body(listDto);
    }
}