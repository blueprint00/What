package com.example.what.Controller;

import com.example.what.Domain.User_info;
import com.example.what.Dto.TokenDTO;
import com.example.what.Dto.ResponseDTO;
import com.example.what.Dto.UserDTO;
import com.example.what.Jwt.JwtAuthenticationFilter;
import com.example.what.Jwt.TokenProvider;
import com.example.what.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.login(userDTO));
    }

    @PostMapping
    public ResponseEntity<TokenDTO> reissue(@RequestBody TokenDTO tokenDTO) {
        return ResponseEntity.ok(userService.reissue(tokenDTO));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<User_info> getMyUserInfo(){
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{user_id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User_info> getUserInfo(@PathVariable String user_id){
        return ResponseEntity.ok(userService.getUserWithAuthorities(user_id).get());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDTO> authorize(@Valid @RequestBody UserDTO userDTO) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUser_id(), userDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDTO jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt.getAccessToken());

        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }

//    @PostMapping("/login")
//    public ResponseDTO doLogin(HttpServletRequest request){
//        String user_id = (String) request.getAttribute("user_id");
//        String password = (String) request.getAttribute("password");
//        String token = TokenProvider.createToken(user_id);
//        server_response = ResponseDTO.builder()
//                .token(token)
//                .tokenType("bearer")
//                .build();
//        userService.saveUserInfo(user_id, password);
//        return server_response;
//    }

//    @PostMapping("/register")
//    public void doRegister(HttpServletRequest request){
//        String user_id = (String) request.getAttribute("user_id");
//        String password = (String) request.getAttribute("password");
//        if(userService.checkById(user_id))
//            userService.saveUserInfo(user_id, password);
//        else throw(new RuntimeException("유저 아이디가 이미 존재합니다."));
//    }
}
