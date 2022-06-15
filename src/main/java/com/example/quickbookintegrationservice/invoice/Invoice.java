package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Invoice{
    @JsonProperty("TxnDate")
    public String txnDate;
    public String domain;
    @JsonProperty("PrintStatus") 
    public String printStatus;
    @JsonProperty("SalesTermRef") 
    public SalesTermRef salesTermRef;
    @JsonProperty("TotalAmt") 
    public double totalAmt;
    @JsonProperty("Line") 
    public ArrayList<Line> line;
    @JsonProperty("DueDate") 
    public String dueDate;
    @JsonProperty("ApplyTaxAfterDiscount") 
    public boolean applyTaxAfterDiscount;
    @JsonProperty("DocNumber") 
    public String docNumber;
    public boolean sparse;
    @JsonProperty("CustomerMemo") 
    public CustomerMemo customerMemo;
    @JsonProperty("Deposit") 
    public int deposit;
    @JsonProperty("Balance") 
    public double balance;
    @JsonProperty("CustomerRef") 
    public CustomerRef customerRef;
    @JsonProperty("TxnTaxDetail") 
    public TxnTaxDetail txnTaxDetail;
    @JsonProperty("SyncToken") 
    public String syncToken;
    @JsonProperty("LinkedTxn") 
    public ArrayList<LinkedTxn> linkedTxn;
    @JsonProperty("BillEmail") 
    public BillEmail billEmail;
    @JsonProperty("ShipAddr") 
    public ShipAddr shipAddr;
    @JsonProperty("EmailStatus") 
    public String emailStatus;
    @JsonProperty("BillAddr") 
    public BillAddr billAddr;
    @JsonProperty("MetaData") 
    public MetaData metaData;
    @JsonProperty("CustomField") 
    public ArrayList<CustomField> customField;
    @JsonProperty("Id") 
    public String id;
}