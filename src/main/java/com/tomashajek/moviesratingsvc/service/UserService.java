package com.tomashajek.moviesratingsvc.service;

import com.tomashajek.moviesratingsvc.model.dto.UserLoginRequest;
import com.tomashajek.moviesratingsvc.model.dto.UserLoginResponse;
import com.tomashajek.moviesratingsvc.model.dto.UserRegisterRequest;
import com.tomashajek.moviesratingsvc.model.dto.UserRegisterResponse;

public interface UserService {

    UserRegisterResponse register(UserRegisterRequest request);
    UserLoginResponse login(UserLoginRequest request);
    void delete(String email);

}
