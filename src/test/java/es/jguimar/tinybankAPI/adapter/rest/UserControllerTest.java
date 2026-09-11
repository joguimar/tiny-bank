package es.jguimar.tinybankAPI.adapter.rest;


import es.jguimar.tinybankAPI.adapter.rest.tranform.UserMapperImpl;
import es.jguimar.tinybankAPI.application.service.CreateUserService;
import es.jguimar.tinybankAPI.domain.model.User;
import es.jguimar.tinybankAPI.infrastructure.exception.ResourceExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateUserService userService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, new UserMapperImpl())).build();
    }

    @Test
    public void createNewUser_shouldReturnOk() throws Exception {
        // Given
        given(userService.create(any()))
                .willReturn(User.builder().id("abc1234").build());

        given(userService.create(any()))
                .willReturn(User.builder().id("abc1234").build());

        // When
        final ResultActions result = mockMvc.perform(
                post("/user/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"Secure safebox 05\",\n" +
                                "  \"password\": \"extremelySecurePassword\"\n" +
                                "}"));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("abc1234"));
    }

    @Test
    public void createNewUserDuplicated_shouldReturnKO() throws Exception {

        // Given
        given(userService.create(any()))
                .willThrow(new ResourceExistsException());

        // When
        final ResultActions result = mockMvc.perform(
                post("/user/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"Secure safebox 05\",\n" +
                                "  \"password\": \"extremelySecurePassword\"\n" +
                                "}"));

        // Then
        result.andExpect(status().isConflict());
    }

    @Test
    public void createNewUserWrongInput_shouldReturnKO() throws Exception {

        // When
        final ResultActions result = mockMvc.perform(
                post("/user/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"password\": \"extremelySecurePassword\"\n" +
                                "}"));

        // Then
        result.andExpect(status().isBadRequest());
    }
}