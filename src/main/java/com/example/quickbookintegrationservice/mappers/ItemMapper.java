package com.example.quickbookintegrationservice.mappers;

import com.intuit.ipp.data.ItemTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor
public class ItemMapper {

    private String name;
    private String description;
    private boolean taxable;
    private BigDecimal unitPrice;
    private ItemTypeEnum itemTypeEnum;

}
