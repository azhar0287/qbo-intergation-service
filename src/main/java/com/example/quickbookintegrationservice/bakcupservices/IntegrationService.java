package com.example.quickbookintegrationservice.bakcupservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.core.IEntity;
import com.intuit.ipp.core.ServiceType;
import com.intuit.ipp.data.*;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.security.OAuth2Authorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.services.QueryResult;
import com.intuit.ipp.util.Config;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.config.Environment;
import com.intuit.oauth2.config.OAuth2Config;
import com.intuit.oauth2.data.BearerTokenResponse;
import com.intuit.oauth2.exception.ConnectionException;
import com.intuit.oauth2.exception.InvalidRequestException;
import com.intuit.oauth2.exception.OAuthException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class IntegrationService {

    private static final Logger LOGGER = LogManager.getLogger(IntegrationService.class);

    private static final String ACCOUNT_QUERY = "select * from Account where AccountType='%s' maxresults 1";

    private static final String MINOR_VERSION = "65";

    public String getToken() throws  OAuthException {
        //Prepare config
        OAuth2Config oauth2Config = new OAuth2Config.OAuth2ConfigBuilder(
                "ABUvSWjrF4iPNNhvPBBmSKsBBhvLE35mOeTdWCIaStTi6OPsb5",
                "OPqYwGvNci6WuuLctfTFpqRkzjUd5fUEqNgyD55Q"
        )
                .callDiscoveryAPI(Environment.SANDBOX) // call discovery API to populate urls
                .buildConfig();

        OAuth2PlatformClient client  = new OAuth2PlatformClient(oauth2Config);

        BearerTokenResponse bearerTokenResponse = client.refreshToken("AB11663156014bg0Qhy8iF8ydqcz49L9rO11GGK95LBFJ6CTem"); //set refresh token

        LOGGER.info("Check here");
        return bearerTokenResponse.getAccessToken();
    }

    public void createInvoice() throws OAuthException, InvalidRequestException, ConnectionException, FMSException {
        ObjectMapper om = new ObjectMapper();
        //Root root = om.readValue(myJsonString, Root.class);

        //get DataService
        DataService service = this.getDataService("4620816365228704200", this.getToken(), MINOR_VERSION);
        LOGGER.info("Service has created");
        //add customer
        Customer customer = getCustomerWithAllFields();
        Customer savedCustomer = service.add(customer);

        LOGGER.info("Customer has created with name"+customer.getCompanyName());
        //add item
        Item item = getItemFields(service);
        Item savedItem = service.add(item);
        LOGGER.info("Customer has created with name"+item.getName());

        //create invoice using customer and item created above
        Invoice invoice = getInvoiceFields(savedCustomer, savedItem);
        Invoice savedInvoice = service.add(invoice);
        LOGGER.info("invoice has sent");
        LOGGER.info("Invoice with Doc Number: "+savedInvoice.getDocNumber());

    }

    /**
     * Create Customer request
     * @return
     */
    private Customer getCustomerWithAllFields() {
        Customer customer = new Customer();
        customer.setDisplayName("Alok and prakash company");
        customer.setCompanyName("AP company");

        EmailAddress emailAddr = new EmailAddress();
        emailAddr.setAddress("ap@gmail.com");
        customer.setPrimaryEmailAddr(emailAddr);

        return customer;
    }

    /**
     * Create Item request
     * @param service
     * @return
     * @throws FMSException
     */
    private Item getItemFields(DataService service) throws FMSException {

        Item item = new Item();
        item.setName("Item: Landing Services v2");
        item.setTaxable(false);
        item.setUnitPrice(new BigDecimal("300"));
        item.setType(ItemTypeEnum.SERVICE);

        Account incomeAccount = getIncomeBankAccount(service);
        item.setIncomeAccountRef(createRef(incomeAccount));

        return item;
    }

    /**
     * Get Income account
     * @param service
     * @return
     * @throws FMSException
     */
    public Account getIncomeBankAccount(DataService service) throws FMSException {
        QueryResult queryResult = service.executeQuery(String.format(ACCOUNT_QUERY, AccountTypeEnum.INCOME.value()));
        List<? extends IEntity> entities = queryResult.getEntities();
        if(!entities.isEmpty()) {
            return (Account)entities.get(0);
        }
        return createIncomeBankAccount(service);
    }

    /**
     * Create Income account
     * @param service
     * @return
     * @throws FMSException
     */
    public Account createIncomeBankAccount(DataService service) throws FMSException {
        Account account = new Account();
        account.setName("Incom" + RandomStringUtils.randomAlphabetic(5));
        account.setAccountType(AccountTypeEnum.INCOME);

        return service.add(account);
    }

    /**
     * Prepare Invoice request
     * @param customer
     * @param item
     * @return
     */
    private Invoice getInvoiceFields(Customer customer, Item item) {

        Invoice invoice = new Invoice();
        invoice.setCustomerRef(createRef(customer));

        List<Line> invLine = new ArrayList<Line>();
        Line line = new Line();
        line.setAmount(new BigDecimal("100"));
        line.setDetailType(LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL);

        SalesItemLineDetail silDetails = new SalesItemLineDetail();
        silDetails.setItemRef(createRef(item));

        line.setSalesItemLineDetail(silDetails);
        invLine.add(line);
        invoice.setLine(invLine);

        return invoice;
    }

    /**
     * Prepare Payment request
     * @param customer
     * @param invoice
     * @return
     */
    private Payment getPaymentFields(Customer customer, Invoice invoice) {

        Payment payment = new Payment();
        payment.setCustomerRef(createRef(customer));

        payment.setTotalAmt(invoice.getTotalAmt());

        List<LinkedTxn> linkedTxnList = new ArrayList<LinkedTxn>();
        LinkedTxn linkedTxn = new LinkedTxn();
        linkedTxn.setTxnId(invoice.getId());
        linkedTxn.setTxnType(TxnTypeEnum.INVOICE.value());
        linkedTxnList.add(linkedTxn);

        Line line1 = new Line();
        line1.setAmount(invoice.getTotalAmt());
        line1.setLinkedTxn(linkedTxnList);

        List<Line> lineList = new ArrayList<Line>();
        lineList.add(line1);
        payment.setLine(lineList);

        return payment;
    }

    /**
     * Creates reference type for an entity
     *
     * @param entity - IntuitEntity object inherited by each entity
     * @return
     */
    public ReferenceType createRef(IntuitEntity entity) {
        ReferenceType referenceType = new ReferenceType();
        referenceType.setValue(entity.getId());
        return referenceType;
    }

    /**
     * Map object to json string
     * @param entity
     * @return
     */
    private String createResponse(Object entity) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString;
        try {
            jsonInString = mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            return createErrorResponse(e);
        } catch (Exception e) {
            return createErrorResponse(e);
        }
        return jsonInString;
    }

    private String createErrorResponse(Exception e) {
        LOGGER.error("Exception while calling QBO ", e);
        return new JSONObject().put("response","Failed").toString();
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

}
