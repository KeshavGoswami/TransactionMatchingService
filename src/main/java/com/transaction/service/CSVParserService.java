package com.transaction.service;

import com.transaction.enums.EntityType;
import com.transaction.exception.InvalidKeyException;
import com.transaction.models.Entity;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CSVParserService {

    public void parseFile(String filePath, EntityType entityType) throws IOException, ParseException, InvalidKeyException;

    public void addValueToEntity(Entity entity, String key, String value) throws InvalidKeyException, ParseException;

}
