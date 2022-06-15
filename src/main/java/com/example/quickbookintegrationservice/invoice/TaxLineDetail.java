package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaxLineDetail{
    @JsonProperty("NetAmountTaxable")
    public double netAmountTaxable;
    @JsonProperty("TaxPercent") 
    public int taxPercent;
    @JsonProperty("TaxRateRef") 
    public TaxRateRef taxRateRef;
    @JsonProperty("PercentBased") 
    public boolean percentBased;
}