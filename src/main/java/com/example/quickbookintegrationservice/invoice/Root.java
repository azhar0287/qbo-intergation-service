package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Root{
    @JsonProperty("Invoice")
    public Invoice invoice;
    public Date time;
}