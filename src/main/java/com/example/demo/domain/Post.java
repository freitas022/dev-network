package com.example.demo.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.dto.AuthorDTO;
import com.example.demo.dto.CommentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Post implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private Date date;
	private String title;
	private String body;
	private AuthorDTO author;

	@Getter
	private List<CommentDTO> comments = new ArrayList<>();

	public Post(String id, Date date, String title, String body, AuthorDTO author) {
		super();
		this.id = id;
		this.date = date;
		this.title = title;
		this.body = body;
		this.author = author;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		return Objects.equals(id, other.id);
	}

}
