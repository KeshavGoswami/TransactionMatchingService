package com.transaction.repositories;

import com.transaction.models.Entity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EntityRepository {

    List<Entity> buyerEntities = new ArrayList<>();
    List<Entity> supplierEntities = new ArrayList<>();

    public static EntityRepository INSTANCE = new EntityRepository();

    public void addBuyerEntity(Entity buyerEntity) {
        this.buyerEntities.add(buyerEntity);
    }

    public void addSupplierEntity(Entity supplierEntity) {
        this.supplierEntities.add(supplierEntity);
    }
}
