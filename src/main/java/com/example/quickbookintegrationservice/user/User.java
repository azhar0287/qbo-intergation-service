package com.example.quickbookintegrationservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Blob;
import java.time.ZonedDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private String userName;
    private String companyName;
    private String clientId;
    private String clientSecret;
    private String refreshToken;


    private String accessToken;

    private int refreshTokenExpiry; // will be in days
    private String realmId;
    private String minorVersion;

    @JsonIgnore
    @CreationTimestamp
    private ZonedDateTime createdAt; //our system created datetime i.e. for record

    @JsonIgnore
    @UpdateTimestamp
    private ZonedDateTime updatedAt;


}
