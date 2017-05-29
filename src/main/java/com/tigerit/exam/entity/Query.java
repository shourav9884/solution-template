package com.tigerit.exam.entity;

import java.util.List;

/**
 * Created by nati on 5/28/17.
 */
public class Query {
    public String selectColumns;
    public String fromTable;
    public String fromTableShort;
    public String joinTable;
    public String joinTableShort;
    public String onClause;

    @Override
    public String toString() {
        return "Query{" +
                "selectColumns='" + selectColumns + '\'' +
                ", fromTable='" + fromTable + '\'' +
                ", fromTableShort='" + fromTableShort + '\'' +
                ", joinTable='" + joinTable + '\'' +
                ", joinTableShort='" + joinTableShort + '\'' +
                ", onClause='" + onClause + '\'' +
                '}';
    }
}
