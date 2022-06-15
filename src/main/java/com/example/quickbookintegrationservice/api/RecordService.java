package com.example.quickbookintegrationservice.api;

import com.example.quickbookintegrationservice.bakcupservices.IntegrationService;
import com.example.quickbookintegrationservice.mappers.ItemMapper;
import com.example.quickbookintegrationservice.mappers.QboExceptionMessage;
import com.example.quickbookintegrationservice.mappers.QboServiceResponse;
import com.example.quickbookintegrationservice.response.DefaultResponse;
import com.example.quickbookintegrationservice.user.User;
import com.example.quickbookintegrationservice.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.ipp.data.*;
import com.intuit.ipp.exception.AuthenticationException;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.services.QueryResult;
import com.intuit.oauth2.data.BearerTokenResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.example.quickbookintegrationservice.util.Constant.AUTHENTICATION_FAILED;
import static com.example.quickbookintegrationservice.util.Constant.UNAUTHORIZED_CODE;

@Service
public class RecordService {
    private static final Logger LOGGER = LogManager.getLogger(RecordService.class);

    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IntegrationService integrationService;
    @Autowired
    UtilService utilService;

    public ResponseEntity createCustomer(String uuid) {
        User user = utilService.checkForRefreshToken(uuid);
        Customer customer = getCustomerMapData();
        Customer savedCustomer = null;
        QboServiceResponse qboServiceResponse = new QboServiceResponse();
        try {
            if(user != null) {
                DataService service = authService.getDataService(user.getRealmId(), user.getAccessToken(), user.getMinorVersion());
                savedCustomer = service.add(customer);
                if(savedCustomer!= null) {
                    LOGGER.info("Customer has posted in QB name: "+savedCustomer.getDisplayName());
                    //save unique customer id in our table here for our reference

                }
            }
        } catch (FMSException fe) {

            qboServiceResponse.setQboExceptionMessage(utilService.getParsedQboException(fe));
            LOGGER.error(fe.getMessage(), fe);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ResponseEntity(new DefaultResponse("Success","Customer has saved","S001"), HttpStatus.OK);
    }

    public Customer getCustomerMapData() {

        Customer customer = new Customer();
        customer.setDisplayName("Azhar  Azhar Ali");
        customer.setCompanyName("BB CC");

        EmailAddress emailAddr = new EmailAddress();
        emailAddr.setAddress("apg@gmail.com");
        customer.setPrimaryEmailAddr(emailAddr);
        return customer;
    }

    /**
     * Create Item request
     * @parm uuid
     * @return
     * @throws FMSException
     */
    public ResponseEntity createItem(String uuid, ItemMapper itemMapper) {
        User user = utilService.checkForRefreshToken(uuid);
        QboServiceResponse qboServiceResponse = new QboServiceResponse();
        Item savedItem = null;
        try {
            if(user != null) {
                DataService service = authService.getDataService(user.getRealmId(), user.getAccessToken(), user.getMinorVersion());
                Item item = mapItemFields(service, itemMapper);
                savedItem = service.add(item);
                if(savedItem!= null) {
                    LOGGER.info("Item has posted in QB name: "+savedItem.getName());
                    //save unique Item id in our table here for our reference
                    return new ResponseEntity(new DefaultResponse("Success","Customer has saved","S001"), HttpStatus.OK);
                }
            }
        } catch (FMSException fe) {
            qboServiceResponse.setQboExceptionMessage(utilService.getParsedDuplicateQboException(fe));
            String code = qboServiceResponse.getQboExceptionMessage().getStatusCode();
            String message = qboServiceResponse.getQboExceptionMessage().getDetail();
            return new ResponseEntity(new DefaultResponse("Failure", message, code), HttpStatus.OK);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ResponseEntity(new DefaultResponse("Failure", "aaaaaaaaa", "F001"), HttpStatus.OK);

    }

    private Item mapItemFields(DataService service, ItemMapper itemMapper) throws FMSException {
        try {
            Item item = new Item();
            item.setName(itemMapper.getName());
            item.setTaxable(itemMapper.isTaxable());
            item.setUnitPrice(itemMapper.getUnitPrice());
            item.setType(ItemTypeEnum.SERVICE);  //should be from external

            Account incomeAccount = integrationService.getIncomeBankAccount(service);
            item.setIncomeAccountRef(createRef(incomeAccount));

            return item;
        } catch (FMSException fe ) {
            LOGGER.error("FMS Exception "+fe.getMessage(), fe);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private ReferenceType createRef(IntuitEntity entity) {
        ReferenceType referenceType = new ReferenceType();
        referenceType.setValue(entity.getId());
        return referenceType;
    }
}
