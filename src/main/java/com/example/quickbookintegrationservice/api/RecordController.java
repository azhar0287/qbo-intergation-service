package com.example.quickbookintegrationservice.api;

import com.example.quickbookintegrationservice.mappers.InvoiceRequestMapper;
import com.example.quickbookintegrationservice.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/Records"})
@CrossOrigin("*")
public class RecordController {
    private static final Logger LOGGER = LogManager.getLogger(RecordController.class);

    @Autowired
    AuthService authService;
    @Autowired
    RecordService recordService;
    @Autowired
    UtilService utilService;

    @PostMapping(value = "/invoice")
    ResponseEntity createInvoice(@RequestBody InvoiceRequestMapper mapper) {
        return null;
    }

    @PostMapping(value = "/refreshToken")
    User refreshToken(@RequestParam("userUuid") String userUuid) {
        return utilService.checkForRefreshToken(userUuid);
    }


    @PostMapping(value = "/customer")
    String createCustomer(@RequestParam("userUuid") String userUuid) {
        String response = null;
        try {
            recordService.createCustomer(userUuid);
            LOGGER.info("User");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return response;
    }

    @PostMapping(value = "/item")
    String createItem(@RequestParam("userUuid") String userUuid) {
        String response = null;
        try {
            recordService.createItem(userUuid);
            LOGGER.info("User");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return response;
    }


}
