package com.example.what.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDTO {
    private String token;

    @Builder
    public ResponseDTO(String token) {
        this.token = token;
    }
}
