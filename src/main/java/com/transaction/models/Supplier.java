package com.transaction.models;

import com.transaction.enums.EntityType;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Supplier extends Entity{
    public Supplier() {
        super(EntityType.SUPPLIER);
    }
}
