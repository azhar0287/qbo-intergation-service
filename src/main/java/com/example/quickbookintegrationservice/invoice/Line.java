package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Line{
    @JsonProperty("Description")
    public String description;
    @JsonProperty("DetailType") 
    public String detailType;
    @JsonProperty("SalesItemLineDetail") 
    public SalesItemLineDetail salesItemLineDetail;
    @JsonProperty("LineNum") 
    public int lineNum;
    @JsonProperty("Amount") 
    public double amount;
    @JsonProperty("Id") 
    public String id;
    @JsonProperty("SubTotalLineDetail") 
    public SubTotalLineDetail subTotalLineDetail;
}