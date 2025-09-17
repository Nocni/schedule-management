package rs.raf.AuthService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import rs.raf.AuthService.dtos.TokenRequestDto;
import rs.raf.AuthService.dtos.TokenResponseDto;
import rs.raf.AuthService.dtos.UserDto;
import rs.raf.AuthService.models.Role;
import rs.raf.AuthService.services.RoleService;
import rs.raf.AuthService.services.UserService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPrijava() throws Exception {
        // Arrange
        TokenRequestDto request = new TokenRequestDto();
        request.setUsername("admin");
        request.setPassword("admin");

        TokenResponseDto response = new TokenResponseDto();
        response.setToken("test-jwt-token");

        when(userService.login(any(TokenRequestDto.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/auth/prijava")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }

    @Test
    public void testUloge() throws Exception {
        // Arrange
        Role role = new Role();
        role.setName("ROLE_NASTAVNIK");
        role.setDescription("Nastavnik role");

        Role savedRole = new Role();
        savedRole.setRoleId(1);
        savedRole.setName("ROLE_NASTAVNIK");
        savedRole.setDescription("Nastavnik role");

        when(roleService.add(any(Role.class))).thenReturn(savedRole);

        // Act & Assert
        mockMvc.perform(post("/auth/uloge")
                .header("Authorization", "Bearer valid-admin-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleId").value(1))
                .andExpect(jsonPath("$.name").value("ROLE_NASTAVNIK"));
    }

    @Test
    public void testDodelaUloge() throws Exception {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("profesor1");

        when(userService.assignRole(eq(1), eq("ROLE_NASTAVNIK"))).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(post("/auth/dodela-uloge")
                .header("Authorization", "Bearer valid-admin-token")
                .param("userId", "1")
                .param("roleName", "ROLE_NASTAVNIK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("profesor1"));
    }
}