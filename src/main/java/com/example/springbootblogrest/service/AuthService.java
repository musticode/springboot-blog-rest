package com.example.springbootblogrest.service;

import com.example.springbootblogrest.payload.LoginDto;
import com.example.springbootblogrest.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
