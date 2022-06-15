package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinkedTxn{
    @JsonProperty("TxnId")
    public String txnId;
    @JsonProperty("TxnType") 
    public String txnType;
}