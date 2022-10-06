package me.lucky.core.api.database;

import de.mobiuscode.nameof.Name;
import me.lucky.core.api.database.annotations.DataColumn;
import me.lucky.core.api.database.entities.Player;
import me.lucky.core.api.utils.CoreFactory;
import sun.text.resources.ext.FormatData_ga;

import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class WhereParameter<Y, T> {
    private final Object value;
    private final String propertyName;
    private final String colName;

    public WhereParameter(Class<Y> dbObjectClass, Function<Y, T> function, T value)  {
        String name = Name.of(dbObjectClass, function);
        String colName1 = name;
        this.value = value;

        this.propertyName = name;
        try {
            Field field = dbObjectClass.getDeclaredField(name);
            DataColumn column = field.getAnnotation(DataColumn.class);

            colName1 = column.Key();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        this.colName = colName1;
    }

    public Object getValue() {
        return value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getColName() {
        return colName;
    }

}
