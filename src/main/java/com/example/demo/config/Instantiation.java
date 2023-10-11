package com.example.demo.config;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.AuthorDTO;
import com.example.demo.dto.CommentDTO;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

@Configuration
public class Instantiation implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PostRepository postRepository;
	
	@Override
	public void run(String... args) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ssXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		userRepository.deleteAll();
		postRepository.deleteAll();

		// Inserir e salvar users
		User maria = User.builder()
				.name("Maria Brown")
				.email("maria@gmail.com")
				.posts((new ArrayList<>())).build();

		User alex = User.builder()
				.name("Alex Green")
				.email("alex@gmail.com")
				.posts((new ArrayList<>())).build();

		User bob = User.builder()
				.name("Bob Grey")
				.email("bob@gmail.com")
				.posts((new ArrayList<>())).build();
		
		userRepository.saveAll(Arrays.asList(maria, alex, bob));

		// Inserir e salvar posts
		Post post1 = Post.builder()
				.date(sdf.parse("21/03/2018T16:32:03Z").toInstant())
				.title("Partiu viagem")
				.body("Vou viajar para São Paulo. Abraços!")
				.comments(new ArrayList<>())
				.author(new AuthorDTO(maria)).build();

		Post post2 = Post.builder()
				.date(sdf.parse("23/03/2018T07:54:48Z").toInstant())
				.title("Bom dia")
				.body("Acordei feliz hoje!")
				.comments(new ArrayList<>())
				.author(new AuthorDTO(maria)).build();

		Post post3 = Post.builder()
				.date(sdf.parse("11/10/2023T15:28:53Z").toInstant())
				.title("Boa tarde")
				.body("Muitas reuniões por aqui.")
				.comments(new ArrayList<>())
				.author(new AuthorDTO(alex)).build();

		// Inserir comentários aos posts e seus respectivos autores
		CommentDTO c1 = CommentDTO.builder()
				.text("Boa viagem minha amiga!")
				.date(sdf.parse("21/03/2018T18:32:03Z").toInstant())
				.author(new AuthorDTO(alex))
				.build();

		CommentDTO c2 = CommentDTO.builder()
				.text("Aproveite.")
				.date(sdf.parse("22/03/2018T09:10:26Z").toInstant())
				.author(new AuthorDTO(bob))
				.build();

		CommentDTO c3 = CommentDTO.builder()
				.text("Tenha um ótimo dia!")
				.date(sdf.parse("23/03/2018T08:10:46Z").toInstant())
				.author(new AuthorDTO(alex))
				.build();

		CommentDTO c4 = CommentDTO.builder()
				.text("Vamos dar um passeio?")
				.date(sdf.parse("23/03/2018T14:23:12Z").toInstant())
				.author(new AuthorDTO(bob))
				.build();

		CommentDTO c5 = CommentDTO.builder()
				.text("Vamos pra cima!")
				.date(sdf.parse("11/10/2023T15:45:10Z").toInstant())
				.author(new AuthorDTO(maria))
				.build();
		// Adicionar ao array de comentários dos posts
		post1.getComments().addAll(Arrays.asList(c1, c2));
		post2.getComments().addAll(Arrays.asList(c3,c4));
		post3.getComments().add((c5));

		// salvar no banco de dados
		postRepository.saveAll(Arrays.asList(post1, post2, post3));

		//vincular posts aos seus respectivos usuários
		maria.getPosts().addAll(Arrays.asList(post1, post2));
		bob.getPosts().add(post3);

		userRepository.saveAll(Arrays.asList(maria, bob));
		
	}

}
