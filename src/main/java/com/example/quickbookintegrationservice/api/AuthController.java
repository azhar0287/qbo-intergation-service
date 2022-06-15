package com.example.quickbookintegrationservice.api;

import com.example.quickbookintegrationservice.mappers.UserInfoRequest;
import com.intuit.oauth2.data.BearerTokenResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/QuickBook"})
@CrossOrigin("*")
public class AuthController {

    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

    @Autowired
    AuthService authService;

    @PostMapping(value = "/user")
    ResponseEntity createUser(@RequestBody UserInfoRequest userInfoRequest) {
        ResponseEntity response = null;
        try {
            response = authService.createUser(userInfoRequest);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return response;
    }

    @GetMapping(value = "/token")
    BearerTokenResponse getRefreshToken( @RequestParam("userUuid") String userUuid) {
        BearerTokenResponse response = null;
        try {
            response = authService.getBearerToken(userUuid);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return response;
    }
}
