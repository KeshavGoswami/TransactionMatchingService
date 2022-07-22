package com.transaction.serviceImpl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.transaction.enums.EntityType;
import com.transaction.exception.InvalidKeyException;
import com.transaction.models.Buyer;
import com.transaction.models.Entity;
import com.transaction.models.Supplier;
import com.transaction.repositories.EntityRepository;
import com.transaction.service.CSVParserService;
import com.transaction.utils.Constants;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;


public class CSVParserServiceImpl implements CSVParserService {

    @Override
    public void parseFile(String filePath, EntityType entityType) throws IOException, ParseException, InvalidKeyException {
        FileReader filereader = new FileReader(filePath);
        CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(0).build();
        List<String[]> allData = csvReader.readAll();
        String[] keys = allData.get(0);
        allData.remove(0);
        // Parse Data
        for (String[] row : allData)
            parseAndAddEntity(row, keys, entityType);
    }

    private void parseAndAddEntity(String[] row, String[] keys, EntityType entityType) throws ParseException, InvalidKeyException {
        Entity entity = null;
        switch (entityType) {
            case BUYER -> {
                entity = new Buyer();
                EntityRepository.INSTANCE.addBuyerEntity(entity);
            }
            case SUPPLIER -> {
                entity = new Supplier();
                EntityRepository.INSTANCE.addSupplierEntity(entity);
            }
        }

        for (int i=0; i<row.length; i++) {
            String value = row[i];
            String key = keys[i].replaceAll("[^A-Za-z]+", "").toLowerCase(Locale.ROOT);
            addValueToEntity(entity, key, value);
        }
    }

    @Override
    public void addValueToEntity(Entity entity, String key, String value) throws InvalidKeyException, ParseException {
        if(value.isEmpty() && !key.equalsIgnoreCase(Constants.GST_IN) && !key.equalsIgnoreCase(Constants.BILL_NO))
            value = "0";
        value = value.replaceAll("[^a-zA-Z0-9./-]","");
        switch (key) {
            case Constants.GST_IN -> entity.setGstIn(value);
            case Constants.DATE -> entity.setDate(value);
            case Constants.BILL_NO -> entity.setBillNo(value);
            case Constants.GST_RATE -> entity.setGstRate(Double.valueOf(value));
            case Constants.TAXABLE_VALUE -> entity.setTaxableValue(Double.valueOf(value));
            case Constants.IGST -> entity.setIGst(Double.valueOf(value));
            case Constants.CGST -> entity.setCGst(Double.valueOf(value));
            case Constants.SGST -> entity.setSGst(Double.valueOf(value));
            case Constants.TOTAL -> entity.setTotal(Double.valueOf(value));
            default -> throw new InvalidKeyException("Invalid Key: " + key);
        }
    }
}
