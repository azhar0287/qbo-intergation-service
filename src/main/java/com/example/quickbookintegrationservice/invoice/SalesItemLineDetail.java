package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SalesItemLineDetail{
    @JsonProperty("TaxCodeRef")
    public TaxCodeRef taxCodeRef;
    @JsonProperty("Qty") 
    public int qty;
    @JsonProperty("UnitPrice") 
    public double unitPrice;
    @JsonProperty("ItemRef") 
    public ItemRef itemRef;
}