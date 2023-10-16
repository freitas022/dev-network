package com.example.demo.user;

import com.example.demo.controllers.UserController;
import com.example.demo.domain.User;
import com.example.demo.dto.PostDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import com.example.demo.service.exception.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest
@ActiveProfiles("test")
@SpringJUnitConfig(classes = {UserController.class})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService service;
    UserDTO dtoUser;

    User user;

    @BeforeEach
    void setUp() {
        //building new dto
        dtoUser = UserDTO.builder()
                .id("652d6efe346efd4bf8f870c3")
                .name("User Secret")
                .email("newuser@mail.com").build();
        //building new user
        user = User.builder()
                .id("652db910a55bd8bd525b71ea")
                .name("User Secret 2")
                .email("newuser@mail.com")
                .posts(new ArrayList<>()).build();
    }

    @Test
    @DisplayName("JUnit test for given User Object when Create User then Return Saved User")
    void insert() throws Exception {

        // Given / Arrange
        given(service.insert(any(UserDTO.class))).willReturn(dtoUser);

        // When / Act
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dtoUser)));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("JUnit test Should return a 404 status code when user is not found")
    void findByIdNotFound() throws Exception {

        // Given / Arrange
        String id = "6529b2e7e92207d97084b987";
        given(service.findById(id))
                .willThrow(new ObjectNotFoundException("User not found"));

        // When / Act
        var response = mockMvc.perform(get("/users/{id}", id));

        // Then / Assert
        response.andExpect(status()
                        .isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("JUnit test for Given List of Users when findAll Users then Return Users List")
    void testGivenListOfUsers_WhenFindAllUsers_thenReturnUsersList() throws Exception {

        // Given / Arrange
        var list = new ArrayList<UserDTO>();
        list.add(dtoUser);
        list.add(UserDTO.builder()
                .email("User 2")
                .email("user.2@mail.com")
                .build());

        given(service.findAll()).willReturn(list);

        // When / Act
        ResultActions response = mockMvc.perform(get("/users"));

        // Then / Assert
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    @DisplayName("JUnit test for Given id when findById then Return UserDTO Object")
    void testGivenUserId_WhenFindById_thenReturnUserDTOObject() throws Exception {

        // Given / Arrange
        var id = "652d6efe346efd4bf8f870c3";
        var user = UserDTO.builder()
                .id(id)
                .name("John Doe")
                .email("john@mail.com").build();

        given(service.findById(id)).willReturn(user);

        // When / Act
        ResultActions response = mockMvc.perform(get("/users/{id}", id));

        // Then / Assert
        response.
                andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("JUnit test for Given id when Delete then Return NoContent")
    void testGivenUserId_WhenDelete_thenReturnNotContent() throws Exception {

        // Given / Arrange
        String id = "6529ba020593b6ad11f21e71";
        willDoNothing().given(service).delete(id);

        // When / Act
        var response = mockMvc.perform(delete("/users/{id}", id));

        // Then / Assert
        response.
                andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("JUnit test for Given Updated User when Update then Return Updated User Object")
    void testGivenUser_WhenUpdate_thenReturnUpdatedUser() throws Exception {

        // Given / Arrange
        String id = "652d6efe346efd4bf8f870c3";
        var userDTO = UserDTO.builder()
                .id(id)
                .name("Eliot")
                .email("eliot002@mail.com")
                .build();

        given(service.update(any(UserDTO.class), eq(id))).willReturn(userDTO);

        //When / Act

        var response = mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)));

        //Then / Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(userDTO.getName())))
                .andExpect(jsonPath("$.email", is(userDTO.getEmail())));
    }

    @Test
    @DisplayName("JUnit Test Given User ID When find posts Then return List of Posts")
    void testGivenUserId_WhenFindPosts_thenReturnListOfPosts() throws Exception {

        // Given / Arrange
        var id = "652db910a55bd8bd525b71ea";
        var posts = new ArrayList<PostDTO>();

        given(service.findPosts(id)).willReturn(posts);

        // When / Act
        var response = mockMvc.perform(get("/users/{id}/posts", id)
                .contentType(MediaType.APPLICATION_JSON));

        //Then / Assert
        response.andDo(print())
                .andExpect(status().isOk());
    }

}