package com.example.quickbookintegrationservice.api;

import com.example.quickbookintegrationservice.mappers.UserInfoRequest;
import com.example.quickbookintegrationservice.response.DefaultResponse;
import com.example.quickbookintegrationservice.user.User;
import com.example.quickbookintegrationservice.user.UserRepository;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.core.ServiceType;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.security.OAuth2Authorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.util.Config;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.config.Environment;
import com.intuit.oauth2.config.OAuth2Config;
import com.intuit.oauth2.data.BearerTokenResponse;
import com.intuit.oauth2.exception.OAuthException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private static final Logger LOGGER = LogManager.getLogger(AuthService.class);

    @Autowired
    UserRepository userRepository;

    public ResponseEntity createUser(UserInfoRequest mapper) throws OAuthException {
        try {
            User user = new User();
            user.setUserName(mapper.getUserName());
            user.setCompanyName(mapper.getCompanyName());
            user.setClientId(mapper.getClientId());
            user.setClientSecret(mapper.getClientSecret());
            user.setRefreshToken(mapper.getRefreshToken());
            user.setUuid(UUID.randomUUID().toString());
            user.setRealmId(mapper.getRealmId());
            user.setAccessToken(mapper.getAccessToken());
            userRepository.save(user);
            LOGGER.info("User has created");

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ResponseEntity(new DefaultResponse("Success","User has saved","S001"), HttpStatus.OK);
    }

    public BearerTokenResponse getBearerToken(String userUuid) throws OAuthException {

        User user = userRepository.getUserByUuid(userUuid);
        BearerTokenResponse bearerTokenResponse = null;
        if( user != null) {
            //Prepare config
            OAuth2Config oauth2Config = new OAuth2Config.OAuth2ConfigBuilder(user.getClientId(), user.getClientSecret())
                    .callDiscoveryAPI(Environment.SANDBOX) // call discovery API to populate urls
                    .buildConfig();

            OAuth2PlatformClient client  = new OAuth2PlatformClient(oauth2Config);
            bearerTokenResponse = client.refreshToken(user.getRefreshToken()); //set refresh token
            if(bearerTokenResponse != null) {
                user.setRefreshToken(bearerTokenResponse.getRefreshToken()); //saving new refreshToken
                user.setAccessToken(bearerTokenResponse.getAccessToken());
            }
        }
        else {
            LOGGER.info("No User found against this UUid: "+userUuid);
        }
        return bearerTokenResponse;
    }

    public DataService getDataService(String realmId, String accessToken, String minorVersion) throws FMSException {

        Context context = prepareContext(realmId, accessToken);
        context.setMinorVersion(minorVersion);

        // create dataservice
        return new DataService(context);
    }
    public DataService getDataService(String realmId, String accessToken) throws FMSException {

        Context context = prepareContext(realmId, accessToken);

        // create dataservice
        return new DataService(context);
    }

    private Context prepareContext(String realmId, String accessToken) throws FMSException {
        String url = "https://sandbox-quickbooks.api.intuit.com"+ "/v3/company";

        Config.setProperty(Config.BASE_URL_QBO, url);
        //create oauth object
        OAuth2Authorizer oauth = new OAuth2Authorizer(accessToken);
        //create context
        Context context = new Context(oauth, ServiceType.QBO, realmId);
        return context;
    }

    public User getUserDetails(String uuid) {
        User user = null;
        try {
            user = userRepository.getUserByUuid(uuid);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

        }
        return user;
    }

}
