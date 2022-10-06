package me.lucky.core.api.database;

import me.lucky.core.api.database.annotations.DataColumn;
import me.lucky.core.api.database.annotations.DataTable;
import me.lucky.core.api.database.entities.Player;
import me.lucky.core.api.utils.CoreFactory;
import me.lucky.core.api.utils.SysLog;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.data.Table;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiConsumer;

public class Operations {
    public static <T> int Insert(Connection openConnection, T dbobject) throws IllegalAccessException {
        Class<?> dbobjectClass = dbobject.getClass();
        if (!dbobjectClass.isAnnotationPresent(DataTable.class)) {
            throw new AnnotationFormatError("Die Annotation DataTable wirde ben\u00c3\u00b6tigt");
        }
        String dataTable = dbobjectClass.getAnnotation(DataTable.class).Name();
        StringBuilder columns = new StringBuilder();
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        for (Field field : dbobject.getClass().getDeclaredFields()) {
            mapFields(parameter, dbobject, field);
        }
        StringBuilder dbParams = new StringBuilder();
        boolean first = true;
        for (Map.Entry entry : parameter.entrySet()) {
            if (!first) {
                columns.append(", ");
                dbParams.append(", ");
            }
            dbParams.append(":").append((String) entry.getKey());
            columns.append((String) entry.getKey());
            first = false;
        }
        String connectionQuery = "INSERT INTO " + dataTable + "( " + columns + " ) VALUES ( " + dbParams + ")";
        Query query = openConnection.createQuery(connectionQuery);
        for (Map.Entry entry : parameter.entrySet()) {
            query.addParameter((String) entry.getKey(), entry.getValue());
        }
        return Integer.parseInt(query.executeUpdate().getKey(Long.class).toString());
    }

    public static <T> void Update(Connection openConnection, T dbobject, int ID) throws IllegalAccessException {
        Class<?> dbobjectClass = dbobject.getClass();
        if (!dbobjectClass.isAnnotationPresent(DataTable.class)) {
            throw new AnnotationFormatError("Die Annotation DataTable wirde ben\u00c3\u00b6tigt");
        }
        String dataTable = dbobjectClass.getAnnotation(DataTable.class).Name();
        StringBuilder columns = new StringBuilder();
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        for (Field field : dbobjectClass.getDeclaredFields()) {
            mapFields(parameter, dbobject, field);
        }
        boolean first = true;
        for (Map.Entry entry : parameter.entrySet()) {
            if (!first) {
                columns.append(", ");
            }
            columns.append((String) entry.getKey()).append(" = ").append(":").append((String) entry.getKey());
            first = false;
        }
        String connectionQuery = "UPDATE " + dataTable + " SET " + columns + " WHERE ID = :ID";
        Query query = openConnection.createQuery(connectionQuery);
        for (Map.Entry entry : parameter.entrySet()) {
            query.addParameter((String) entry.getKey(), entry.getValue());
        }
        query.addParameter("ID", ID);
        query.executeUpdate();
    }

    public static <T> T GetEntryById(Connection openConnection, Class<T> dbObjectClass, int ID) {
        if (!dbObjectClass.isAnnotationPresent(DataTable.class)) {
            throw new AnnotationFormatError("Die Annotation DataTable wirde ben\u00c3\u00b6tigt");
        }
        String dataTable = dbObjectClass.getAnnotation(DataTable.class).Name();
        StringBuilder columns = new StringBuilder();
        String connectionQuery = "SELECT * FROM " + dataTable + " WHERE ID = :p1";

        Query query = Operations.mapColumns(openConnection.createQueryWithParams(connectionQuery, ID), dbObjectClass);
        return query.executeAndFetchFirst(dbObjectClass);
    }

    public static Table ExecuteQueryTable(Connection openConnection, String queryString, DBParameter... parameters) {
        Query query = openConnection.createQuery(queryString);
        for (DBParameter param : parameters) {
            query.addParameter(param.Name, param.Value);
        }

        return query.executeAndFetchTable();
    }

    public static <T> T ExecuteScalarQuery(Connection openConnection, Class<T> tClass, WhereParameter... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ");
        builder.append(tClass.getAnnotation(DataTable.class).Name());
        builder.append(" WHERE ");

        DBParameter[] dbParameters = new DBParameter[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            WhereParameter parameter = parameters[i];
            if(i != 0) {
                builder.append(" AND ");
            }

            builder.append(parameter.getColName());
            builder.append("= :");
            builder.append(parameter.getPropertyName());

            dbParameters[i] = new DBParameter<>(parameter.getPropertyName(), parameter.getValue());
        }

        CoreFactory.getRunningCore().getSysLog().LogDebug(builder.toString());

        return Operations.ExecuteScalarQuery(openConnection, tClass, builder.toString(), dbParameters);
    }

    public static <T> List<T> ExecuteQuery(Connection openConnection, Class<T> tClass, WhereParameter... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ");
        builder.append(tClass.getAnnotation(DataTable.class).Name());
        builder.append(" WHERE");

        DBParameter[] dbParameters = new DBParameter[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            WhereParameter parameter = parameters[i];
            if(i != 0) {
                builder.append(" AND ");
            }

            dbParameters[i] = new DBParameter<>(parameter.getPropertyName(), parameter.getValue());

            builder.append(parameter.getColName());
            builder.append("=:");
            builder.append(parameter.getPropertyName());
        }

        return Operations.ExecuteQuery(openConnection, tClass, builder.toString(), dbParameters);
    }

    public static <T> List<T> ExecuteQuery(Connection openConnection, Class<T> tClass, String queryString, DBParameter... parameters) {
        Query query = Operations.mapColumns(openConnection.createQuery(queryString), tClass);
        for (DBParameter param : parameters) {
            query.addParameter(param.Name, param.Value);
        }

        return query.executeAndFetch(tClass);
    }

    public static <T> T ExecuteScalarQuery(Connection openConnection, Class<T> tClass, String queryString, DBParameter... parameters) {
        Query query = Operations.mapColumns(openConnection.createQuery(queryString), tClass);
        for (DBParameter param : parameters) {
            query.addParameter(param.Name, param.Value);
        }

        return query.executeAndFetchFirst(tClass);
    }

    public static <T> boolean DeleteID(Connection openConnecion, T dbObject, int id) {
        if (!dbObject.getClass().isAnnotationPresent(DataTable.class)) {
            throw new AnnotationFormatError("Die Annotation DataTable wirde ben\u00c3\u00b6tigt");
        }
        try {
            String dataTable = dbObject.getClass().getAnnotation(DataTable.class).Name();
            String connectioNQuery = "DELETE FROM " + dataTable + " WHERE ID = :ID";
            openConnecion.createQuery(connectioNQuery).addParameter("ID", id).executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Object getValueFromField(Field field, Object instance) {
        try {
            if(field.getModifiers() == Modifier.PUBLIC) {
                return field.get(instance);
            } else {
                String fieldName = field.getName();
                fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                return instance.getClass().getMethod("get" + fieldName).invoke(instance);
            }
        } catch (Exception e) {
            CoreFactory.getRunningCore().getSysLog().LogException("Beim Laden des Values ist ein Fehler aufgetreten!", e);
            return null;
        }
    }

    private static <T> void mapFields(HashMap<String, Object> parameter, T dbobject, Field field) {
        Object fieldValue = Operations.getValueFromField(field, dbobject);
        DataColumn annotation = field.getAnnotation(DataColumn.class);
        if (annotation == null || annotation.IsAuto()) {
            return;
        }
        if (annotation.IsConstraint() && (Integer) fieldValue == 0) {
            parameter.put(annotation.Key(), null);
            return;
        }
        parameter.put(annotation.Key(), fieldValue);
    }

    private static <T> Query mapColumns(Query query, Class<T> dbObjectClass) {
        SysLog sysLog = CoreFactory.getRunningCore().getSysLog();
        for (Field field :
                dbObjectClass.getDeclaredFields()) {
            DataColumn annotation = field.getAnnotation(DataColumn.class);

            if(annotation == null) {
                continue;
            }

            query = query.addColumnMapping(annotation.Key(), field.getName());
        }

        return query;
    }
}
