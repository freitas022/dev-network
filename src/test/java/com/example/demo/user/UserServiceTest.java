package com.example.demo.user;

import com.example.demo.domain.User;
import com.example.demo.dto.PostDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.MatcherAssert.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService service;

    private User user;
    private UserDTO dto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("652d6efe346efd4bf8f870c3")
                .name("User Test")
                .email("newuser@mail.com")
                .posts(new ArrayList<>()).build();
        dto = UserDTO.builder()
                .id("652d6efe346efd4bf8f870c3")
                .name("User Test")
                .email("newuser@mail.com").build();
    }

    @Test
    @DisplayName("JUnit test insert - given calling the insert method it must return the saved object")
    void testInsert() {
        // Given / Arrange
        given(this.repository.save(any(User.class))).willReturn(user);
        // When / Act
        var actual = service.insert(dto);
        // Then / Assert
        assertEquals(actual.getId(), user.getId());
        assertEquals(actual.getName(), user.getName());
        assertEquals(actual.getEmail(), user.getEmail());
    }

    @DisplayName("JUnit test update - given calling the update method it must return an updated object")
    @Test
    void testUpdate() {
        // Given / Arrange
        given(repository.findById(user.getId())).willReturn(Optional.of(user));
        given(repository.save(any(User.class))).willReturn(user);

        var data = UserDTO.builder().name("Update").email("update@test.com").build();
        // When / Act
        var actual = service.update(data, user.getId());
        // Then / Assert
        assertThat(actual.getId(), is(equalTo("652d6efe346efd4bf8f870c3")));
        assertThat(actual.getName(), is(equalTo("Update")));
        assertEquals(actual.getEmail(), data.getEmail());
    }
    
    @DisplayName("JUnit test update - should return a 404 status code given user is not found")
    @Test
    void testUpdateNotFound() {
        // Given / Arrange
    	var id = "652efd48d83ed1ab2f53130c";
        given(repository.findById(id)).willReturn(Optional.empty());

        var data = UserDTO.builder().name("Update").email("update@test.com").build();
        // When / Act
        assertThrows(ObjectNotFoundException.class, () -> this.service.update(data, id));
        // Then / Assert
        verify(this.repository, times(1)).findById(id);
        verify(this.repository, never()).save(any());
    }

    @DisplayName("JUnit test findById - given calling the findById method it must return the object with the selected id")
    @Test
    void testFindById() {
        // Given / Arrange
        var id = "652ef04d076ec3cf266ae2b8";
        var expected = User.builder().id(id).name("Lionel Messi").email("10messi@test.com").build();

        given(repository.findById(id)).willReturn(Optional.of(expected));
        // When / Act
        var actual = service.findById(expected.getId());

        // Then / Assert
        assertThat(actual.getId(), is(equalTo(expected.getId())));
        assertThat(actual.getName(), is(equalTo(expected.getName())));
        assertThat(actual.getEmail(), is(equalTo(expected.getEmail())));
    }

    @DisplayName("JUnit test findById - should return a 404 status code when user is not found")
    @Test
    void testFindByIdNotFound() {
        // Given / Arrange
        var id = "652ef04d076ec3cf266ae2b0";

        given(this.repository.findById(id)).willReturn(Optional.empty());

        // When / Act
        assertThrows(ObjectNotFoundException.class, () -> this.service.findById(id));
        // Then / Assert
        verify(repository, times(1)).findById(id);
    }

    @DisplayName("JUnit test delete - should soft delete a user")
    @Test
    void testDelete() {
        // Given / Arrange
        var id = "652d6efe346efd4bf8f870c3";
        willDoNothing().given(this.repository).delete(any());
        given(this.repository.findById(anyString())).willReturn(Optional.of(user));
        // When / Act
        this.service.delete(id);
        // Then / Assert
        verify(this.repository).findById(id);
        verify(this.repository).delete(any());
    }

    @DisplayName("JUnit test delete - should return a 404 status code given user is not found")
    @Test
    void testDeleteNotFound() {
        // Given / Arrange
        var id = "652d6efe346efd4bf8f870d1";
        given(this.repository.findById(id)).willReturn(Optional.empty());

        // When / Act
        assertThrows(ObjectNotFoundException.class, () -> this.service.delete(id));
        // Then / Assert
        verify(this.repository).findById(id);
        verify(this.repository, never()).delete(any());
    }

    @DisplayName("JUnit test findAll - should return a list of users")
    @Test
    void testFindAll() {
        // Given / Arrange
        var list = new ArrayList<User>();
        list.add(user);

        given(this.repository.findAll()).willReturn(list);
        // When / Act
        var actual = this.service.findAll();
        // Then / Assert
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @DisplayName("JUnit test findAll - should return a empty list")
    @Test
    void testFindAllEmpty() {
        // Given / Arrange
        given(this.repository.findAll()).willReturn(Collections.emptyList());

        // When / Act
        var actual = this.service.findAll();

        // Then / Assert
        assertTrue(actual.isEmpty());
        verify(this.repository, times(1)).findAll();
    }

    @DisplayName("JUnit test findPosts - should return an list of Posts belonging to a user")
    @Test
    void testFindPosts() {
        // Given / Arrange
        given(this.repository.findById(user.getId())).willReturn(Optional.of(user));
        // When / Act
        List<PostDTO> dtoPosts = this.service.findPosts(user.getId());
        // Then / Assert
        assertTrue(dtoPosts.isEmpty());
    }
    
    @DisplayName("JUnit test findPosts - should return a 404 status code given user is not found")
    @Test
    void testFindPostsNotFoundUserId() {
        // Given / Arrange
    	var id = "652efeed9f49dd73b0d68637";
        given(this.repository.findById(id)).willReturn(Optional.empty());
        // When / Act
        assertThrows(ObjectNotFoundException.class, () -> this.service.findPosts(id));
        // Then / Assert
        verify(this.repository, times(1)).findById(id);
    }
}