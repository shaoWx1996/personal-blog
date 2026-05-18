package com.blog.service;

import com.blog.dto.response.JwtResponse;
import com.blog.dto.request.LoginRequest;

public interface UserService {
    JwtResponse login(LoginRequest loginRequest);
}
