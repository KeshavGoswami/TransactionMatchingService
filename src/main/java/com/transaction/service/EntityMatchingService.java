package com.transaction.service;

import com.transaction.models.OutputEntity;

import java.util.List;

public interface EntityMatchingService {

    public List<OutputEntity> matchEntities(int numThreshold, int dateThreshold, int stringThreshold) throws IllegalAccessException;
}
