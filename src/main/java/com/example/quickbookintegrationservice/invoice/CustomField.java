package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomField{
    @JsonProperty("DefinitionId")
    public String definitionId;
    @JsonProperty("StringValue") 
    public String stringValue;
    @JsonProperty("Type") 
    public String type;
    @JsonProperty("Name") 
    public String name;
}