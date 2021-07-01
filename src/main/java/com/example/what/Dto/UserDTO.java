package com.example.what.Dto;

import com.example.what.Domain.User_info;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String user_id;
    private String password;
    private User_info.Authority authority;

    @Builder
    public UserDTO(String user_id, String password, User_info.Authority authority) {
        this.user_id = user_id;
        this.password = password;
        this.authority = authority;
    }

    public User_info toEntity(){
        return User_info.builder()
                .user_id(user_id)
                .password(password)
                .authority(authority)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(user_id, password);
    }

}
