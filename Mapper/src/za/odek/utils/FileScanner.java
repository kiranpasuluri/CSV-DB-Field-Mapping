package za.odek.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileScanner {

	public static void main(String[] args) throws IOException {
		// open file input stream
		BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Kiran Pasuluri\\Desktop\\person.csv"));

		// read file line by line FamilyCover_GenRPAC_l1 - Copy.csv
		String line = null;
		Scanner scanner = null;
		int index = 0;
		List<String> empList = new ArrayList<>();

		while ((line = reader.readLine()) != null) {
			scanner = new Scanner(line);
			if (scanner.findInLine(",") != null) {
				scanner.useDelimiter(",");
				while (scanner.hasNext()) {
					String data = scanner.next();
					System.out.println(data);
				}
			}

			scanner = new Scanner(line);
			if (scanner.findInLine("\t") != null) {
				scanner.useDelimiter("\t");
				while (scanner.hasNext()) {
					String data = scanner.next();
					System.out.println(data);
				}
			}
			break;
		}

		// close reader
		reader.close();

		System.out.println(empList);

	}
	
	
	@SuppressWarnings("resource")
	public List<String> getHeadersFromFile(String filePath) throws Exception
	{
		
		// open file input stream
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		// read file line by line FamilyCover_GenRPAC_l1 - Copy.csv
		String line = reader.readLine();
		List<String> result = null;
		if (line.indexOf("\t") != -1) {
			result = Arrays.asList(line.split("\\t"));
		} else {
			result = Arrays.asList(line.split("\\s*,\\s*"));
		}
		return result;
		/*List<String> headers = new ArrayList<String>();
		 * Scanner scanner = null;
		 * while ((line = reader.readLine()) != null) {
			
			scanner = new Scanner(line);
			String vNext = scanner.next();
			System.out.println("vNext ::"+vNext);
			headers.add(vNext);
			
			if (scanner.findInLine(",") != null) {
				scanner.useDelimiter(",");
				
				while (scanner.hasNext()) {
					String data = scanner.next();
					System.out.println("data in comma::"+data);
					headers.add(data);
				}
			} else if (scanner.findInLine("\t") != null) {
				scanner.useDelimiter("\t");
				while (scanner.hasNext()) {
					String data = scanner.next();
					System.out.println("data in tab::"+data);
					headers.add(data);
				}
			} 
			
			break;
		}

		reader.close();
		return headers;*/
	}
	
	public List<String> getValuesFromFile(String filePath) throws Exception
	{
		
		// open file input stream
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		// read file line by line FamilyCover_GenRPAC_l1 - Copy.csv
		String line = reader.readLine();
		List<String> result = null;
		if (line.indexOf("\t") != -1) {
			result = Arrays.asList(line.split("\\t"));
		} else {
			result = Arrays.asList(line.split("\\s*,\\s*"));
		}
		return result;
	}

}
