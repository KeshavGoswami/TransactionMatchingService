package com.transaction.serviceImpl;

import com.transaction.enums.MatchType;
import com.transaction.models.Entity;
import com.transaction.models.MatchScore;
import com.transaction.models.OutputEntity;
import com.transaction.repositories.EntityRepository;
import com.transaction.service.EntityMatchingService;
import com.transaction.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

public class EntityMatchingServiceImpl implements EntityMatchingService {

    @Override
    public List<OutputEntity> matchEntities(int numThreshold, int dateThreshold, int stringThreshold) throws IllegalAccessException {

        List<OutputEntity> outputEntities = new ArrayList<>();
        Set<Integer> supplierMatchedIdx = new HashSet<>();
        for (Entity buyerEntity : EntityRepository.INSTANCE.getBuyerEntities()) {
            OutputEntity outputEntity = new OutputEntity(buyerEntity);
            Entity bestMatchSupplier = null;
            MatchScore bestMatchScore = new MatchScore(null, Integer.MAX_VALUE);
            for(Entity supplierEntity: EntityRepository.INSTANCE.getSupplierEntities())  {
                outputEntity.setSupplier(supplierEntity);
                MatchScore matchScore = checkMatch(buyerEntity, supplierEntity, numThreshold, dateThreshold, stringThreshold);
                bestMatchSupplier = updateMatchScore(matchScore, bestMatchScore, bestMatchSupplier, supplierEntity);
                if(bestMatchScore.getMatchType()==MatchType.EXACT)
                    break;
            }
            if(bestMatchSupplier!=null)
                supplierMatchedIdx.add(EntityRepository.INSTANCE.getSupplierEntities().indexOf(bestMatchSupplier));
            outputEntity.setMatchType(bestMatchScore.getMatchType());
            outputEntity.setSupplier(bestMatchSupplier);
            outputEntities.add(outputEntity);
        }
        for(int i=0; i<EntityRepository.INSTANCE.getSupplierEntities().size(); i++) {
            if(!supplierMatchedIdx.contains(i)) {
                OutputEntity outputEntity = new OutputEntity();
                outputEntity.setSupplier(EntityRepository.INSTANCE.getSupplierEntities().get(i));
                outputEntity.setMatchType(MatchType.ONLY_IN_SUPPLIER);
                outputEntities.add(outputEntity);
            }
        }
        return outputEntities;
    }

    private Entity updateMatchScore(MatchScore matchScore, MatchScore bestMatchScore, Entity bestMatchSupplier, Entity supplierEntity) {
        switch (matchScore.getMatchType()) {
            case EXACT:
                bestMatchScore.setMatchType(matchScore.getMatchType());
                bestMatchSupplier = supplierEntity;
                break;
            case PARTIAL:
                if(matchScore.getMatchScore() < bestMatchScore.getMatchScore()) {
                    bestMatchScore.setMatchScore(matchScore.getMatchScore());
                    bestMatchSupplier = supplierEntity;
                    bestMatchScore.setMatchType(matchScore.getMatchType());
                }
                break;
            case ONLY_IN_BUYER:
                if(bestMatchScore.getMatchType()==null) {
                    bestMatchScore.setMatchType(matchScore.getMatchType());
                }
                break;
        }
        return bestMatchSupplier;
    }

    public MatchScore checkMatch(Entity buyerEntity, Entity supplierEntity, int numThreshold, int dateThreshold, int stringThreshold) throws IllegalAccessException {
        MatchScore matchScore = new MatchScore();
        matchScore.setMatchType(MatchType.EXACT);
        Field[] fields = Entity.class.getDeclaredFields();
        Class<?> numType = Double.class;
        Class<?> stringType = String.class;
        Class<?> dateType = Date.class;
        for(Field field: fields) {
            field.setAccessible(true);
            if(field.getType().isAssignableFrom(numType))
                matchScore = matchNumFields(buyerEntity, supplierEntity, matchScore, field, numThreshold);
            else if(field.getType().isAssignableFrom(dateType))
                matchScore = matchDateFields(buyerEntity, supplierEntity, matchScore, field, dateThreshold);
            else if(field.getType().isAssignableFrom(stringType))
                matchScore = matchStringFields(buyerEntity, supplierEntity, matchScore, field, stringThreshold);
        }

        return matchScore;
    }

    public MatchScore matchStringFields(Entity buyerEntity, Entity supplierEntity, MatchScore matchScore, Field field, int stringThreshold) throws IllegalAccessException {
        String buyerValue = String.valueOf(field.get(buyerEntity));
        String supplierValue = String.valueOf(field.get(supplierEntity));
        int diff = StringUtils.similar(buyerValue, supplierValue, stringThreshold);
        return addMatchScore(diff, matchScore, stringThreshold);
    }

    private MatchScore matchDateFields(Entity buyerEntity, Entity supplierEntity, MatchScore matchScore, Field field, int dateThreshold) throws IllegalAccessException {
        Date buyerValue = (Date) field.get(buyerEntity);
        Date supplierValue = (Date) field.get(supplierEntity);
        int diff = (int) Math.abs(buyerValue.getTime() - supplierValue.getTime());
        diff = diff/(1000*60*60*24);
        return addMatchScore(diff, matchScore, dateThreshold);
    }

    private MatchScore matchNumFields(Entity buyerEntity, Entity supplierEntity, MatchScore matchScore, Field field, int numThreshold) throws IllegalAccessException {
        Double buyerValue = Double.valueOf(String.valueOf(field.get(buyerEntity)));
        Double supplierValue = Double.valueOf(String.valueOf(field.get(supplierEntity)));
        int diff = (int) Math.abs(buyerValue - supplierValue);
        return addMatchScore(diff, matchScore, numThreshold);
    }

    private MatchScore addMatchScore(int diff, MatchScore matchScore, int threshold) {
        MatchType matchType = matchScore.getMatchType();
        matchScore.setMatchScore(matchScore.getMatchScore()+diff);
        if(diff==0){ //do nothing
        }
        else if(diff<=threshold && (matchType==MatchType.EXACT || matchType==MatchType.PARTIAL))
            matchType = MatchType.PARTIAL;
        else matchType = MatchType.ONLY_IN_BUYER;
        matchScore.setMatchType(matchType);
        return matchScore;
    }
}
