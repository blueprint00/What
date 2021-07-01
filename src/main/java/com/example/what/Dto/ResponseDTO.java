package com.example.what.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDTO {
    private String token;
//    private String tokenType;

    @Builder
    public ResponseDTO(String token) {//} String tokenType) {
        this.token = token;
//        this.tokenType = tokenType;
    }
}
