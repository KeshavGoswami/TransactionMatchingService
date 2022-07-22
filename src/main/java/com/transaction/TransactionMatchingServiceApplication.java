package com.transaction;

import com.transaction.enums.EntityType;
import com.transaction.exception.InvalidKeyException;
import com.transaction.models.Entity;
import com.transaction.models.OutputEntity;
import com.transaction.service.CSVParserService;
import com.transaction.service.EntityMatchingService;
import com.transaction.serviceImpl.CSVParserServiceImpl;
import com.transaction.serviceImpl.EntityMatchingServiceImpl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TransactionMatchingServiceApplication {

    public static void main(String[] args) throws IllegalAccessException, IOException, ParseException, InvalidKeyException {

        CSVParserService csvParserService = new CSVParserServiceImpl();
        EntityMatchingService entityMatchingService = new EntityMatchingServiceImpl();

        String buyerFilePath = getInputFromConsole("Enter Buyer File Path");
        csvParserService.parseFile(buyerFilePath, EntityType.BUYER);
        String supplierFilePath = getInputFromConsole("Enter Supplier File Path");
        csvParserService.parseFile(supplierFilePath, EntityType.SUPPLIER);

        int numberThreshold = Integer.parseInt(getInputFromConsole("Enter threshold for number"));
        int dateThreshold = Integer.parseInt(getInputFromConsole("Enter threshold days for date"));
        int stringThreshold = Integer.parseInt(getInputFromConsole("Enter threshold for string matching"));

        List<OutputEntity> outputEntityList = entityMatchingService.matchEntities(numberThreshold, dateThreshold, stringThreshold);
        printOnConsole(outputEntityList);
    }

    private static void printOnConsole(List<OutputEntity> outputEntityList) throws IllegalAccessException, ParseException {
        for(OutputEntity outputEntity: outputEntityList) {
            Field[] fields = Entity.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(outputEntity.getBuyer()!=null && !field.getName().equalsIgnoreCase("date"))
                    System.out.print(field.get(outputEntity.getBuyer()) + "\t");
                else if(outputEntity.getBuyer()!=null && field.getName().equalsIgnoreCase("date"))
                    printDate(String.valueOf(field.get(outputEntity.getBuyer())));
            }
            System.out.print(outputEntity.getMatchType()+"\t");
            for (Field field : fields) {
                field.setAccessible(true);
                if(outputEntity.getSupplier()!=null && !field.getName().equalsIgnoreCase("date"))
                    System.out.print(field.get(outputEntity.getSupplier()) + "\t");
                else if(outputEntity.getSupplier()!=null && field.getName().equalsIgnoreCase("date"))
                    printDate(String.valueOf(field.get(outputEntity.getSupplier())));
            }
            System.out.println();
        }
    }

    private static void printDate(String dateStr) throws ParseException {
        DateFormat parser = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = parser.parse(dateStr);

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print(formatter.format(date) + "\t");
    }

    private static String getInputFromConsole(String message) {
        Scanner sc= new Scanner(System.in);
        System.out.println(message);
        return sc.nextLine();
    }
}
