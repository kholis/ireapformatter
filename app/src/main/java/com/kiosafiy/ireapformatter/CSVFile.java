// https://javapapers.com/android/android-read-csv-file/
package com.kiosafiy.ireapformatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public List read(){
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                if ( ! (csvLine.endsWith(",0.0")) && ! (csvLine.endsWith(",Quantity")) ) {
                    String[] row = csvLine.split(",");
                    if ( ! (row[5].contains("-")) ) {
                        String row3 = row[3].replace(".0","");
                        // Set 1000 separator
                        String harga = String.format("%,d", Integer.parseInt(row3)).replace(',', '.');
                        String jml = row[5].replace(".0","");
                        resultList.add(row[2] + " | " + harga + " | " + jml);
                    }
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }

    public String read2(){
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                //String row = csvLine.split(",");
                if ( ! csvLine.endsWith(",0.0")) {
                    sb.append(csvLine + "\n");
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }

        String everything = sb.toString();
        return everything;
    }
}
