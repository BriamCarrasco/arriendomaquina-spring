package com.briamcarrasco.arriendomaquinaria.service;

import com.briamcarrasco.arriendomaquinaria.model.User;

public interface UserService {

    User createUser(String username, String password, String email);
}