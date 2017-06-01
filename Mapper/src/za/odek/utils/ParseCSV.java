package za.odek.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import com.opencsv.*;
//import au.com.bytecode.opencsv.CSVWriter;

public class ParseCSV {

	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// try {
		// csv file containing data
		String strFile = "C:\\Users\\Kiran Pasuluri\\Desktop\\useraccounts.csv";
		// create BufferedReader to read csv file
		/*
		 * BufferedReader br = new BufferedReader( new FileReader(strFile));
		 * String strLine = ""; StringTokenizer st = null; int lineNumber = 0,
		 * tokenNumber = 0;
		 * 
		 * //read comma separated file line by line while( (strLine =
		 * br.readLine()) != null) { lineNumber++;
		 * 
		 * //break comma separated line using "," st = new
		 * StringTokenizer(strLine, ",");
		 * 
		 * while(st.hasMoreTokens()) { //display csv values tokenNumber++;
		 * System.out.println("Line # " + lineNumber +", Token # " +
		 * tokenNumber+ ", Token : "+ st.nextToken()); } }
		 */
		// }
		/*
		 * Reader in; Iterable<CSVRecord> records = null; try { in = new
		 * FileReader(strFile); CSVFormat header =
		 * CSVFormat.EXCEL.withSkipHeaderRecord(true);
		 * System.out.println(header.getHeader()); records =
		 * CSVFormat.EXCEL.withHeader().parse(in); // header will be ignored }
		 * catch (IOException e) { e.printStackTrace(); }
		 * 
		 * for (CSVRecord record : records) { String line = ""; for ( int i=0; i
		 * < record.size(); i++) {
		 * 
		 * if ( line == "" ) line = line.concat(record.get(i)); else line =
		 * line.concat("," + record.get(i)); } System.out.println("read line: "
		 * + line); }
		 */

		// static int[] getRowsColsNo() {
		/*
		 * Scanner scanIn = null; int rows = 0; int cols = 0; String InputLine =
		 * ""; try { scanIn = new Scanner(new BufferedReader( new
		 * FileReader("C:\\Users\\Kiran Pasuluri\\Desktop\\useraccounts.csv")));
		 * scanIn.useDelimiter(","); while (scanIn.hasNextLine()) { InputLine =
		 * scanIn.nextLine(); String[] InArray = InputLine.split(","); rows++;
		 * cols = InArray.length; if (InArray != null) {
		 * 
		 * } }
		 * 
		 * } catch (Exception e) { System.out.println(e); }
		 * System.out.println(rows); System.out.println(cols);
		 */

		// input file lookslike at the following
		// 1;2;3
		// 4;5;6
		// 7;8;9

		}

	/*public void updateCsvFile(String source, String destination) throws Exception {
		String strFile = "C:\\Users\\Kiran Pasuluri\\Desktop\\useraccounts.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(strFile));
		CSVParser reader = new CSVParser(new FileReader(source), null, 0, 0);
		List<String[]> csvBody = writer.writeAll(rs, includeColumnNames)readAll();
		csvBody.get(1)[2] = "NewValue";

		//CSVWriter writer = new CSVWriter(new FileWriter(destination), SEPARATOR, ' ');
		writer.writeAll(csvBody);
		writer.flush();
	}*/
}

