package com.example.what.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TokenRequestDTO {
    private String accessToken;
    private String refreshToken;
}
