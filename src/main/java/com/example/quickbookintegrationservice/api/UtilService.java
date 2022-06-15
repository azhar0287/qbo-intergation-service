package com.example.quickbookintegrationservice.api;

import com.example.quickbookintegrationservice.mappers.QboExceptionMessage;
import com.example.quickbookintegrationservice.mappers.QboServiceResponse;
import com.example.quickbookintegrationservice.user.User;
import com.example.quickbookintegrationservice.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.exception.AuthenticationException;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.services.QueryResult;
import com.intuit.oauth2.data.BearerTokenResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.quickbookintegrationservice.util.Constant.AUTHENTICATION_FAILED;
import static com.example.quickbookintegrationservice.util.Constant.UNAUTHORIZED_CODE;

@Service
public class UtilService {
    private static final Logger LOGGER = LogManager.getLogger(UtilService.class);

    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;

    public User checkForRefreshToken(String uuid) {
        User user = authService.getUserDetails(uuid);
        try {
            QboServiceResponse qboServiceResponse = updateRefreshToken(user);
            if (qboServiceResponse.getSavedCustomer() == null && qboServiceResponse.getQboExceptionMessage() != null) {

                QboExceptionMessage message = qboServiceResponse.getQboExceptionMessage();
                if(message.getStatusCode().equalsIgnoreCase(UNAUTHORIZED_CODE) && message.getDetail().equalsIgnoreCase(AUTHENTICATION_FAILED)) {
                    LOGGER.info("Got 401 error");

                    BearerTokenResponse bearerTokenResponse  = authService.getBearerToken(uuid);
                    user.setAccessToken(bearerTokenResponse.getAccessToken()); //setting new access token
                    user.setRefreshToken(bearerTokenResponse.getRefreshToken());
                    userRepository.save(user); //Updating User
                    LOGGER.info("User has updated with Latest Token: ");
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return user;
    }

    /**
     * We are here fetching company info just to check our token
     * */
    public QboServiceResponse updateRefreshToken(User user) {
        QboServiceResponse qboServiceResponse = new QboServiceResponse();
        try {
            if(user != null) {

                DataService service = authService.getDataService(user.getRealmId(), user.getAccessToken(), user.getMinorVersion());
                String sql = "select * from companyinfo";

                QueryResult queryResult = service.executeQuery(sql);
                CompanyInfo companyInfo = (CompanyInfo) queryResult.getEntities().get(0);

                ObjectMapper mapper = new ObjectMapper();
                LOGGER.info("Company Info: "+mapper.writeValueAsString(companyInfo));
                qboServiceResponse.setCompanyInfo(companyInfo);

            }
        } catch (FMSException fe) {
            qboServiceResponse.setQboExceptionMessage(getParsedQboException(fe));
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return qboServiceResponse;
    }

    public QboExceptionMessage getParsedQboException(FMSException fe) {
        QboExceptionMessage qboExceptionMessage = null;
        try {
            if (fe instanceof AuthenticationException) {
                AuthenticationException ae = (AuthenticationException) fe;
                String message = ae.getErrorList().get(0).getMessage();
                String[] array = message.split(";");
                qboExceptionMessage = new QboExceptionMessage(getParsedString(array[0],"="), getParsedString(array[1],"="), getParsedString(array[2], "="));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return qboExceptionMessage;
    }
    public QboExceptionMessage getParsedDuplicateQboException(FMSException fe) {
        QboExceptionMessage qboExceptionMessage = new QboExceptionMessage();
        try {
            if (fe instanceof FMSException) {
                FMSException ae = (FMSException) fe;
                String message = ae.getErrorList().get(0).getMessage();
                String code = ae.getErrorList().get(0).getCode();

                if(message.equalsIgnoreCase("Duplicate Name Exists Error") && code.equalsIgnoreCase("6240") ) {
                    qboExceptionMessage.setDetail(message);
                    qboExceptionMessage.setStatusCode(code);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return qboExceptionMessage;
    }

    public String getParsedString(String str, String delimiter) {
        try {
            String[] array = str.split(delimiter);
            return array[1];
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }

}
