package com.transaction.models;

import com.transaction.enums.EntityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Entity {
    String gstIn;
    Date date;
    String billNo;
    Double gstRate;
    Double taxableValue;
    Double iGst;
    Double sGst;
    Double cGst;
    Double total;
    EntityType entityType;

    public Entity(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setDate(String value) throws ParseException {

        DateFormat slashFormat = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
        DateFormat hyphenFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        if(value.contains("/"))
            this.date = slashFormat.parse(value);
        else if(value.contains("-"))
            this.date = hyphenFormat.parse(value);
    }
}
