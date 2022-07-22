package com.transaction.models;

import com.transaction.enums.MatchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OutputEntity {
    MatchType matchType;
    Entity buyer;
    Entity supplier;

    public OutputEntity(Entity buyerEntity) {
        this.buyer = buyerEntity;
    }
}
