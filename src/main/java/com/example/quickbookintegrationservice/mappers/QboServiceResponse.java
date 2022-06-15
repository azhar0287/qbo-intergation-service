package com.example.quickbookintegrationservice.mappers;

import com.example.quickbookintegrationservice.invoice.Invoice;
import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.data.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class QboServiceResponse {

    private QboExceptionMessage qboExceptionMessage;
    private Customer savedCustomer;
    private CompanyInfo companyInfo;


}
