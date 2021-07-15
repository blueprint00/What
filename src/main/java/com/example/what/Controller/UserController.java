package com.example.what.Controller;

import com.example.what.Domain.User_info;
import com.example.what.Dto.TokenResponseDTO;
import com.example.what.Dto.TokenRequestDTO;
import com.example.what.Dto.UserDTO;
import com.example.what.Jwt.JwtAuthenticationFilter;
import com.example.what.Jwt.TokenProvider;
import com.example.what.Service.UserService;

import javassist.NotFoundException;
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
import java.util.Map;

import static com.example.what.Util.ApiUtil.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserService userService;

    public UserController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

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
        return new ResponseEntity<>(tokenResponseDTO, httpHeaders, HttpStatus.OK);
        // return ResponseEntity.ok(userService.login(userDTO));
    }

    @PostMapping("/reissue")
    public ApiResult<TokenResponseDTO> reissue(@RequestBody TokenRequestDTO tokenRequestDTO) {
//        TokenResponseDTO tokenResponseDTO = userService.reissue(tokenRequestDTO);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + tokenResponseDTO.getAccessToken());
        return success(userService.reissue(tokenRequestDTO));
//        return new ResponseEntity<>(tokenResponseDTO, httpHeaders, HttpStatus.OK);
        // return ResponseEntity.ok(userService.reissue(userDTO));

    }

    @PostMapping("/{user_id}/logout")
    public void logout(@PathVariable String user_id){
        userService.logout(user_id);
    }

    @PostMapping("/{user_id}/delete")
    public void deleteUser(@PathVariable String user_id){
        userService.deleteUser(user_id);
    }

//    @PostMapping("/authenticate")
//    public ResponseEntity<TokenResponseDTO> authorize(@Valid @RequestBody UserDTO userDTO) {
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(userDTO.getUser_id(), userDTO.getPassword());
//
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        TokenResponseDTO jwt = tokenProvider.createToken(authentication);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt.getAccessToken());
//
//        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
//    }

//    @GetMapping("/user")
//    public ResponseEntity<User_info> getMyUserInfo(){
//        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
//    }

}
