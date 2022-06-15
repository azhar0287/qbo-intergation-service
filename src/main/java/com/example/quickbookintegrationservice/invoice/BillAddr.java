package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillAddr{
    @JsonProperty("Line4")
    public String line4;
    @JsonProperty("Line3") 
    public String line3;
    @JsonProperty("Line2") 
    public String line2;
    @JsonProperty("Line1") 
    public String line1;
    @JsonProperty("Long") 
    public String _long;
    @JsonProperty("Lat") 
    public String lat;
    @JsonProperty("Id") 
    public String id;
}