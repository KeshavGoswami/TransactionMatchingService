package com.transaction.models;

import com.transaction.enums.EntityType;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Buyer extends Entity{
    public Buyer() {
        super(EntityType.BUYER);
    }
}
