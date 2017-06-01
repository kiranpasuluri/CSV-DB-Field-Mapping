package za.odek.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class CSVReader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String csvFile = "C:\\Users\\Kiran Pasuluri\\Desktop\\useraccounts.csv";
        String line = "";
        String cvsSplitBy = ",";
        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader(csvFile));
            
            while ((line = br.readLine()) != null) {

            	 line = br.readLine(); // Reading header, Ignoring 
            	 System.out.println("line : \n"+line);
            	while ((line = br.readLine()) != null && !line.isEmpty()) { 
            		String[] fields = line.split(","); 
            		System.out.println(Arrays.toString(fields));
                //System.out.println("header"+fields);

            }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }}}