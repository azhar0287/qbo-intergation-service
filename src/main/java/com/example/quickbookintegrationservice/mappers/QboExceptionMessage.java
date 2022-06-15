package com.example.quickbookintegrationservice.mappers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class QboExceptionMessage {
    private String detail;
    private String errorCode;
    private String statusCode;

    public QboExceptionMessage(String detail, String errorCode, String statusCode) {
        this.detail = detail;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
