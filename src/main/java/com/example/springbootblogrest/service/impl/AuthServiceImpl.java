package com.example.springbootblogrest.service.impl;

import com.example.springbootblogrest.entity.Role;
import com.example.springbootblogrest.entity.User;
import com.example.springbootblogrest.exception.BlogAPIException;
import com.example.springbootblogrest.payload.LoginDto;
import com.example.springbootblogrest.payload.RegisterDto;
import com.example.springbootblogrest.repository.RoleRepository;
import com.example.springbootblogrest.repository.UserRepository;
import com.example.springbootblogrest.security.JwtTokenProvider;
import com.example.springbootblogrest.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider){

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }



    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //JWT implementations
        String token = jwtTokenProvider.generateToken(authentication);


        return token;
    }

    private String encodeTo(String password){

        if (password.isBlank() || password.isEmpty()){
            throw new RuntimeException("Password is empty or blank");
        }

        return passwordEncoder.encode(password);
    }

    @Override
    public String register(RegisterDto registerDto) {

        // add check for username exist in database
        if (userRepository.existsByUsername(registerDto.getUsername())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists");
        }

        // add check for email exists in database
        if (userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists");

        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(encodeTo(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();

        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);




        return "User registered successfully";
    }
}
