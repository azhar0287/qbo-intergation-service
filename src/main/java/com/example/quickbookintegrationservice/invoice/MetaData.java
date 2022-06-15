package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MetaData{
    @JsonProperty("CreateTime")
    public Date createTime;
    @JsonProperty("LastUpdatedTime") 
    public Date lastUpdatedTime;
}