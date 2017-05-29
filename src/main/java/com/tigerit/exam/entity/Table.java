package com.tigerit.exam.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nati on 5/28/17.
 */
public class Table {
    public String name;
    public List<String> columns;
    public List<List<Integer>> rows;
    public Map<String,Integer> columnIndexMap;
    public Table(){
        columnIndexMap = new HashMap<>();
    }
}
