package com.example.springbootblogrest.controller;

import com.example.springbootblogrest.payload.JWTAuthResponse;
import com.example.springbootblogrest.payload.LoginDto;
import com.example.springbootblogrest.payload.RegisterDto;
import com.example.springbootblogrest.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }


    // build login rest api
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){

        String token = authService.login(loginDto);


        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }


    // build register rest api
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * JWT Development Steps
     * Create JwtAuthenticationEntryPoint
     * Add Jwt properties in application properties file
     * Create JwtTokenProvider - Utility class
     * Create JwtAuthenticationFilter
     * Create JwtAuthResponse DTO
     * Configure JWT in Spring security
     * Change Login rest api to return jwt token
     * */





}
