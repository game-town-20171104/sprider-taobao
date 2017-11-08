package com.ylfin.spider.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVTools {
    private
    static  final  String NEW_LINE_SEPARATOR = "\n";
    public static void write(List<Map> list,String fileName) {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        //创建 CSVFormat
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        try {
            fileWriter = new  FileWriter(fileName);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(list.get(0).keySet());
            for (Map map:list){
                csvFilePrinter.printRecord(map.values());
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally
        {
            try
            {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch  (IOException e) {
                     e.printStackTrace();
            }

        }

    }


    public static void writeObject(List<Object> list ,String fileName){

    }
    public static void main(String[] args) {
        List list = new ArrayList();
        Map<String,String > m = new HashMap<String, String>();
        m.put("name","张三");
        m.put("value","100");
        list.add(m);

        m =new HashMap<String, String>();
        m.put("name","张三2");
        m.put("value","1000");
        list.add(m);

        CSVTools.write(list,"D:/test.csv");
    }
}
