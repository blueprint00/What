package com.example.what.Controller;

import com.example.what.Domain.User_info;
import com.example.what.Dto.TokenResponseDTO;
import com.example.what.Dto.TokenRequestDTO;
import com.example.what.Dto.UserDTO;
import com.example.what.Jwt.JwtAuthenticationFilter;
import com.example.what.Jwt.TokenProvider;
import com.example.what.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class WhatController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/signup")
    public ResponseEntity<User_info> signup(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.signup(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody UserDTO userDTO){
        TokenResponseDTO tokenResponseDTO = userService.login(userDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + tokenResponseDTO.getAccessToken());
        return ResponseEntity.ok(tokenResponseDTO);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDTO> reissue(@RequestBody TokenRequestDTO tokenRequestDTO) {
        TokenResponseDTO tokenResponseDTO = userService.reissue(tokenRequestDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + tokenResponseDTO.getAccessToken());
        return ResponseEntity.ok(tokenResponseDTO);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponseDTO> authorize(@Valid @RequestBody UserDTO userDTO) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUser_id(), userDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenResponseDTO jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt.getAccessToken());

        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }
}
