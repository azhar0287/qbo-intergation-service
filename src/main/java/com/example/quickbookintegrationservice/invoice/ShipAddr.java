package com.example.quickbookintegrationservice.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipAddr{
    @JsonProperty("City")
    public String city;
    @JsonProperty("Line1") 
    public String line1;
    @JsonProperty("PostalCode") 
    public String postalCode;
    @JsonProperty("Lat") 
    public String lat;
    @JsonProperty("Long") 
    public String _long;
    @JsonProperty("CountrySubDivisionCode") 
    public String countrySubDivisionCode;
    @JsonProperty("Id") 
    public String id;
}