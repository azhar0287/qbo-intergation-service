package com.example.quickbookintegrationservice.bakcupservices;

import com.example.quickbookintegrationservice.client.OAuth2PlatformClientFactory;
import com.intuit.ipp.exception.FMSException;
import com.intuit.oauth2.config.OAuth2Config;
import com.intuit.oauth2.config.Scope;
import com.intuit.oauth2.exception.ConnectionException;
import com.intuit.oauth2.exception.InvalidRequestException;
import com.intuit.oauth2.exception.OAuthException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController

public class IntegrationController {
    private static final Logger LOGGER = LogManager.getLogger(IntegrationController.class);

    @Autowired
    OAuth2PlatformClientFactory factory;

    @Autowired
    IntegrationService integrationService;

    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @RequestMapping("/connected")
    public String connected() {
        return "connected";
    }

    /**
     * Controller mapping for connectToQuickbooks button
     * @return
     */
    @RequestMapping("/connectToQuickbooks")
    public RedirectView connectToQuickbooks(HttpSession session) {
        LOGGER.info("inside connectToQuickbooks ");
        OAuth2Config oauth2Config = factory.getOAuth2Config();

        String redirectUri = factory.getPropertyValue("OAuth2AppRedirectUri");

        String csrf = oauth2Config.generateCSRFToken();
        session.setAttribute("csrfToken", csrf);
        try {
            List<Scope> scopes = new ArrayList<Scope>();
            scopes.add(Scope.Accounting);
            return new RedirectView(oauth2Config.prepareUrl(scopes, redirectUri, csrf), true, true, false);
        } catch (com.intuit.oauth2.exception.InvalidRequestException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/token")
    public String getToken() throws InvalidRequestException, ConnectionException, OAuthException {
        return integrationService.getToken();

    }

    @PostMapping(value = "/Invoice")
    public void createInvoice() throws OAuthException, InvalidRequestException, FMSException, ConnectionException {
        integrationService.createInvoice();
    }


}
