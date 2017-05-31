package com.tigerit.exam;


import com.tigerit.exam.entity.Query;
import com.tigerit.exam.entity.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tigerit.exam.IO.*;

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
public class Solution implements Runnable {
    @Override
    public void run() {
        // your application entry point

        // sample input process
//        String string = readLine();
        Integer numOfTestCase = readLineAsInteger();
        for (int k = 0; k < numOfTestCase; k++) {
            Integer numOfTables = readLineAsInteger();
            List<Table> tables = new ArrayList<>();
            for (int i = 0; i < numOfTables; i++) {
                Table table = getTableFromInput();
                tables.add(table);
            }

            Integer numOfQueries = readLineAsInteger();
            List<Query> queries = new ArrayList<>();
            for (int i = 0; i < numOfQueries; i++) {
                queries.add(getSingleQueryFromInput());
                readLine();
            }
            printLine("Test: " + (k + 1));
            printResult(tables, queries);
        }
    }

    private void printResult(List<Table> tables, List<Query> queries) {
        for (Query query:queries) {
            printSingleQueryResult(tables, query);
            printLine("");
        }
    }

    private void printSingleQueryResult(List<Table> tables, Query query) {
        Map<String, Table> tableMap = new HashMap<>();
        for (Table table : tables) tableMap.put(table.name, table);
        Table resultTable = new Table();
        List<String> resultColumns = new ArrayList<>();

        Table fromTable = tableMap.get(query.fromTable);
        Table joinTable = tableMap.get(query.joinTable);

        if (query.selectColumns.equals("*")) {

            for (int i = 0; i < fromTable.columns.size(); i++) {
                resultColumns.add(fromTable.columns.get(i));
            }
            for (int i = 0; i < joinTable.columns.size(); i++) {
                resultColumns.add(joinTable.columns.get(i));
            }
            resultTable.columns = resultColumns;

        } else {
            String[] cols = query.selectColumns.split(",");

            for (int i = 0; i < cols.length; i++) {
                String column = cols[i].trim();
                String[] arr = column.split("\\.");
                if (arr.length > 1) resultColumns.add(arr[1].trim());
                else resultColumns.add(column);
            }
            resultTable.columns = resultColumns;
        }
        String onClause = query.onClause;
        String[] onClauseArr = onClause.split(" = ");

        String firstTable = onClauseArr[0].split("\\.")[0];
        String firstColumn = onClauseArr[0].split("\\.")[1];
        String secondTable = onClauseArr[1].split("\\.")[0];
        String secondColumn = onClauseArr[1].split("\\.")[1];
        String fromColumn = "";
        String onColumn = "";
        if (firstTable.equals(query.fromTable) || firstTable.equals(query.fromTableShort)) {
            firstTable = query.fromTable;
            fromColumn = firstColumn;
        } else if (firstTable.equals(query.joinTable) || firstTable.equals(query.joinTableShort)) {
            firstTable = query.joinTable;
            onColumn = firstColumn;
        }
        if (secondTable.equals(query.fromTable) || secondTable.equals(query.fromTableShort)) {
            secondTable = query.fromTable;
            fromColumn = secondColumn;
        } else if (secondTable.equals(query.joinTable) || secondTable.equals(query.joinTableShort)) {
            secondTable = query.joinTable;
            onColumn = secondColumn;
        }

        List<List<Integer>> resultRows = new ArrayList<>();
        for (int i = 0; i < fromTable.rows.size(); i++) {
            for (int j = 0; j < joinTable.rows.size(); j++) {
                if (fromTable.rows.get(i).get(fromTable.columnIndexMap.get(fromColumn)) == joinTable.rows.get(j).get(joinTable.columnIndexMap.get(onColumn))) {
                    resultRows.add(addRow(fromTable, joinTable, query, fromTable.rows.get(i), joinTable.rows.get(j)));
                }
            }
        }

        resultTable.rows = resultRows;
        printTable(resultTable);
    }

    private void printTable(Table resultTable) {
        String column = "";
        for (int i = 0; i < resultTable.columns.size(); i++) {
            column += resultTable.columns.get(i) + " ";
        }
        printLine(column.trim());
        for (int i = 0; i < resultTable.rows.size(); i++) {
            String row = "";
            for (int j = 0; j < resultTable.rows.get(i).size(); j++) {
                row += resultTable.rows.get(i).get(j) + " ";
            }
            printLine(row.trim());
        }
    }

    private List<Integer> addRow(Table fromTable, Table joinTable, Query query, List<Integer> fromRow, List<Integer> joinRow) {
        String columns = query.selectColumns;
        List<Integer> result = new ArrayList<>();
        if (columns.equals("*")) {
            for (int i = 0; i < fromRow.size(); i++) {
                result.add(fromRow.get(i));
            }
            for (int i = 0; i < joinRow.size(); i++) {
                result.add(joinRow.get(i));
            }
        } else {
            String[] selectedColumns = columns.split(",");
            Map<Integer, Boolean> fromColumns = new HashMap<>();
            Map<Integer, Boolean> joinColumns = new HashMap<>();

            for (int i = 0; i < selectedColumns.length; i++) {
                String column = selectedColumns[i].trim();
                String tableName = column.split("\\.")[0];
                String columnName = column.split("\\.")[1];
                if (query.fromTableShort.equals(tableName) || query.fromTable.equals(tableName))
                    fromColumns.put(fromTable.columnIndexMap.get(columnName), true);
                else
                    joinColumns.put(joinTable.columnIndexMap.get(columnName), true);
            }
            for (int i = 0; i < fromRow.size(); i++) {
                if (fromColumns.containsKey(i)) result.add(fromRow.get(i));
            }
            for (int i = 0; i < joinRow.size(); i++) {
                if (joinColumns.containsKey(i)) result.add(joinRow.get(i));
            }
        }
        return result;
    }

    private Query getSingleQueryFromInput() {
        Query query = new Query();
        query = getSelectColumns(query);
        query = getFromTable(query);
        query = getJoinTable(query);
        query = getOnClauseString(query);
        return query;
    }

    private Query getOnClauseString(Query query) {
        String onClauseString = readLine();
        onClauseString = onClauseString.replace("ON ", "").trim();
        query.onClause = onClauseString;
        return query;
    }

    private Query getJoinTable(Query query) {
        String joinTableString = readLine();
        joinTableString = joinTableString.replace("JOIN ", "").trim();
        String[] joinTableArr = joinTableString.split(" ");
        query.joinTable = joinTableArr[0];
        if (joinTableArr.length > 1) query.joinTableShort = joinTableArr[1];
        return query;
    }

    private Query getFromTable(Query query) {
        String fromTableString = readLine();
        fromTableString = fromTableString.replace("FROM ", "").trim();
        String[] fromTableArr = fromTableString.split(" ");
        query.fromTable = fromTableArr[0];
        if (fromTableArr.length > 1) query.fromTableShort = fromTableArr[1];
        return query;
    }

    private Query getSelectColumns(Query query) {
        String selectString = readLine();
        selectString = selectString.replace("SELECT ", "").trim();
        query.selectColumns = selectString;
        return query;
    }

    private Table getTableFromInput() {
        Table table = new Table();
        table.name = readLine();

        String rowColumnCount = readLine();
        String[] splittedString = rowColumnCount.split(" ");

        int columnCount = Integer.parseInt(splittedString[0]);
        int rowCount = Integer.parseInt(splittedString[1]);


        table.columns = getColumns(columnCount);
        Map<String, Integer> columnMap = new HashMap<>();
        for (int i = 0; i < table.columns.size(); i++) {
            columnMap.put(table.columns.get(i), i);
        }
        table.columnIndexMap = columnMap;

        List<List<Integer>> rows = new ArrayList<>();

        for (int j = 0; j < rowCount; j++) {
            List<Integer> row = getSingleRow(columnCount);
            rows.add(row);
        }

        table.rows = rows;
        return table;
    }

    private List<String> getColumns(int columnCount) {
        String columnInLine = readLine();
        String[] splittedColumns = columnInLine.split(" ");

        List<String> columns = new ArrayList<>();

        for (int j = 0; j < columnCount; j++) {
            columns.add(splittedColumns[j]);
        }
        return columns;
    }

    private List<Integer> getSingleRow(int columnCount) {
        List<Integer> row = new ArrayList<>();
        String rowLine = readLine();
        String[] rowStrings = rowLine.split(" ");
        for (int i = 0; i < columnCount; i++) row.add(Integer.parseInt(rowStrings[i]));
        return row;
    }
}
