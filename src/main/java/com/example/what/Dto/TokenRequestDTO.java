package com.example.what.Dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class TokenRequestDTO {
    private String accessToken;
    private String refreshToken;
}
