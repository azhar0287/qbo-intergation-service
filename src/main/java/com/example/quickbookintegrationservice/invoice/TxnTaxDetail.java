package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class TxnTaxDetail{
    @JsonProperty("TxnTaxCodeRef") 
    public TxnTaxCodeRef txnTaxCodeRef;
    @JsonProperty("TotalTax") 
    public double totalTax;
    @JsonProperty("TaxLine")
    public ArrayList<TaxLine> taxLine;
}