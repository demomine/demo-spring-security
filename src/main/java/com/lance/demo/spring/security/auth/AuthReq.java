package com.lance.demo.spring.security.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthReq {
    private String type;
    @NotBlank
    private String username;
    @NotBlank
    private String credential;
}
