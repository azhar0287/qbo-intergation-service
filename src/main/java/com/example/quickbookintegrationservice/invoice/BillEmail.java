package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillEmail{
    @JsonProperty("Address")
    public String address;
}