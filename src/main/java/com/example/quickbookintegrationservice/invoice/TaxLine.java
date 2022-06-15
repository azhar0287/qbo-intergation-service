package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaxLine{
    @JsonProperty("DetailType")
    public String detailType;
    @JsonProperty("Amount") 
    public double amount;
    @JsonProperty("TaxLineDetail") 
    public TaxLineDetail taxLineDetail;
}