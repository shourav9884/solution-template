package com.tigerit.exam;


import com.tigerit.exam.entity.Table;

import java.util.ArrayList;
import java.util.List;

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
        while(numOfTestCase>0)
        {
            Integer numOfTables = readLineAsInteger();
            List<Table> tables = new ArrayList<>();
            for(int i = 0 ;i<numOfTables;i++){
                Table table = getTable();
                tables.add(table);
            }
            numOfTestCase--;
        }

        Integer integer = readLineAsInteger();

        // sample output process
//        printLine(string);
        printLine(integer);
    }
    private Table getTable()
    {
        Table table = new Table();
        table.name = readLine();

        String rowColumnCount = readLine();
        String[] splittedString = rowColumnCount.split(" ");

        int columnCount = Integer.parseInt(splittedString[0]);
        int rowCount = Integer.parseInt(splittedString[1]);


        table.columns = getColumns(columnCount);

        List<List<Integer>> rows = new ArrayList<>();

        for(int j=0;j<rowCount;j++){
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
        for(int i=0;i<columnCount;i++) row.add(Integer.parseInt(rowStrings[i]));
        return row;
    }
}
