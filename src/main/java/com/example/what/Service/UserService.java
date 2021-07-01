package com.example.what.Service;

import com.example.what.Domain.*;
import com.example.what.Dto.TokenDTO;
import com.example.what.Dto.UserDTO;
import com.example.what.Jwt.TokenProvider;
import com.example.what.Util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final User_info_repository user_info_repository;
    private final Refresh_token_repository refresh_token_repository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User_info signup(UserDTO userDTO){
        if(user_info_repository.findOneWithAuthoritiesByUser_id(userDTO.getUser_id()).orElse(null) != null){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        User_info user_info = User_info.builder()
                .user_id(userDTO.getUser_id())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .authority(userDTO.getAuthority())
                .build();

        return user_info_repository.save(user_info);
    }

    @Transactional
    public Optional<User_info> getUserWithAuthorities(String user_id){
        return user_info_repository.findOneWithAuthoritiesByUser_id(user_id);
    }

    @Transactional
    public Optional<User_info> getMyUserWithAuthorities(){
        return SecurityUtil.getCurrentUserId().flatMap(user_info_repository::findOneWithAuthoritiesByUser_id);
    }

    @Transactional
    public TokenDTO login(UserDTO userDTO){
        UsernamePasswordAuthenticationToken authenticationToken = userDTO.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDTO tokenDTO = tokenProvider.createToken(authentication);

        Refresh_token refresh_token = Refresh_token.builder()
                .refresh_key(authentication.getName())
                .refresh_value(tokenDTO.getRefreshToken())
                .build();

        refresh_token_repository.save(refresh_token);

        return tokenDTO;
    }

    @Transactional
    public TokenDTO reissue(TokenDTO tokenDTO){
        if(!tokenProvider.validateToken(tokenDTO.getRefreshToken())){
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenDTO.getAccessToken());

        Refresh_token refresh_token = refresh_token_repository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if(!refresh_token.getRefresh_value().equals(tokenDTO.getRefreshToken())){
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDTO newTokenDTO = tokenProvider.createToken(authentication);

        Refresh_token newRefresh_token = refresh_token.updateValue(newTokenDTO.getRefreshToken());
        refresh_token_repository.save(newRefresh_token);

        return newTokenDTO;
    }

}
