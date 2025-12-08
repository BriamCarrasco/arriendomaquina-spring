package com.briamcarrasco.arriendomaquinaria.controller;

import com.briamcarrasco.arriendomaquinaria.model.User;
import com.briamcarrasco.arriendomaquinaria.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminUserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private AdminUserController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listUsers_returnsAdminPanel() {
        List<User> users = List.of(new User());
        when(userService.getAllUsers()).thenReturn(users);

        String result = controller.listUsers(model);

        assertEquals("adminpanel", result);
        verify(model).addAttribute("users", users);
    }

    @Test
    void createUser_redirectsToUsers() {
        String result = controller.createUser("testUser", "pass123", "test@email.com", "USER");

        verify(userService).adminCreate("testUser", "pass123", "test@email.com", "USER");
        assertEquals("redirect:/api/admin/users", result);
    }

    @Test
    void deleteUser_redirectsToUsers() {
        String result = controller.deleteUser(1L);

        verify(userService).deleteUser(1L);
        assertEquals("redirect:/api/admin/users", result);
    }

    @Test
    void showEditForm_userFound_returnsEditUser() {
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);

        String result = controller.showEditForm(1L, model);

        assertEquals("editUser", result);
        verify(model).addAttribute("user", user);
    }

    @Test
    void showEditForm_userNotFound_redirectsToUsers() {
        when(userService.findById(99L)).thenReturn(null);

        String result = controller.showEditForm(99L, model);

        assertEquals("redirect:/api/admin/users", result);
    }

    @Test
    void updateUser_redirectsToUsers() {
        String result = controller.updateUser(1L, "newName", "new@email.com");

        verify(userService).updateUser(1L, "newName", "new@email.com");
        assertEquals("redirect:/api/admin/users", result);
    }
}
