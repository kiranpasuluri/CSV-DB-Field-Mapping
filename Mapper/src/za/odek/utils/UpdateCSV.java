package za.odek.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import com.google.common.collect.Table.Cell;
 
//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.WorkbookSettings;
// 
class UpdateCSV {
	
	public static void main(String[] args) throws IOException {
		
		String staticField = "kiran";
    //try {
      //csv file containing data
      String strFile = "C:\\Users\\Kiran Pasuluri\\Desktop\\useraccounts.csv";
      //CSVReader reader = new CSVReader(new FileReader(strFile));
      BufferedReader reader = new BufferedReader(new FileReader(strFile));
      String nextLine;
      int lineNumber = 0;
      while ((nextLine = reader.readLine()) != null) {
        lineNumber++;
        System.out.print("Line # " + lineNumber+" :");
        
        // nextLine[] is an array of values from the line
        System.out.println(nextLine);
        if (nextLine.equals(staticField)) {
        	
			
		}
      }
   // }
  }
}
