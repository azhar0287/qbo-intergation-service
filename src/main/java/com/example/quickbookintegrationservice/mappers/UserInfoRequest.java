package com.example.quickbookintegrationservice.mappers;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserInfoRequest {

    private String userName;
    private String companyName;
    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private String accessToken;
    private int refreshTokenExpiry; // will be in days
    private String realmId;

}
