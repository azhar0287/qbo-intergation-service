package com.example.quickbookintegrationservice.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class DefaultResponse {
    String responseIdentifier;
    String description;
    String responseCode;

    /*
    * Constructors
     */
    public DefaultResponse(String responseIdentifier, String description, String responseCode) {
        this.responseIdentifier = responseIdentifier;
        this.description = description;
        this.responseCode = responseCode;
    }

}
