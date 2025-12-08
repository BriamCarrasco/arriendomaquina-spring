package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.dto.LoginRequestDto;
import com.briamcarrasco.arriendomaquinaria.jwt.JWTAuthtenticationConfig;
import com.briamcarrasco.arriendomaquinaria.service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private JWTAuthtenticationConfig jwtConfig;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_success_redirectsToHome() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("password123");

        UserDetails userDetails = new User("testUser", "encodedPass",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(passwordEncoder.matches("password123", "encodedPass")).thenReturn(true);
        when(jwtConfig.getJWTToken("testUser", "ROLE_USER")).thenReturn("Bearer token123");

        String result = controller.login(loginRequest, request, response);

        assertEquals("redirect:/home", result);
        verify(response, atLeastOnce()).addCookie(any());
    }

    @Test
    void login_wrongPassword_redirectsToLoginError() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("wrongPassword");

        UserDetails userDetails = new User("testUser", "encodedPass",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrongPassword", "encodedPass")).thenReturn(false);

        String result = controller.login(loginRequest, request, response);

        assertEquals("redirect:/login?error", result);
    }

    @Test
    void login_userNotFound_redirectsToLoginError() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("unknownUser");
        loginRequest.setPassword("pass");

        when(userDetailsService.loadUserByUsername("unknownUser"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        String result = controller.login(loginRequest, request, response);

        assertEquals("redirect:/login?error", result);
    }

    @Test
    void redirectToLogin_returnsRedirect() {
        String result = controller.redirectToLogin();
        assertEquals("redirect:/login", result);
    }

    @Test
    void sanitizeForHeader_withNull_returnsEmpty() throws Exception {
        Method method = AuthController.class.getDeclaredMethod("sanitizeForHeader", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(controller, (Object) null);
        assertEquals("", result);
    }
}
