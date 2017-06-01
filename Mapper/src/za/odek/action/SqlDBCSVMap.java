package za.odek.action;

import java.io.BufferedReader;  
import java.io.FileNotFoundException;  
import java.io.FileReader;  
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class SqlDBCSVMap { 
	
	/** 
     * @param args 
	 * @throws IOException 
    */  
    public static void main(String[] args) throws IOException { 
        // TODO Auto-generated method stub  
    	BufferedReader brc = new BufferedReader( new InputStreamReader(System.in));
    	System.out.println("Please enter a  csv file name including file location : Example :C:/person.csv");
        String fileName= brc.readLine();
        		//"C:/Users/Kiran Pasuluri/Desktop/person.csv";  
        System.out.println(fileName);
        System.out.println("Please enter a Database details ");
        System.out.println("HostName ");
        String hostname= brc.readLine();
        System.out.println("Port ");
        String port= brc.readLine();
        System.out.println("UserName ");
        String userName= brc.readLine();
        System.out.println("Password ");
        String password= brc.readLine();
        System.out.println("HostName "+hostname+",Port "+port+",UserName "+userName+",password "+password);
        Connection connection = null;
		try {
			//192.168.0.145:1433
			String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			String url = "jdbc:jtds:sqlserver://"+hostname+":"+port+"/gen_ocs"; // jdbc:jtds:sqlserver://dbhost:dbport;DatabaseName=dbname
//			String username = "genesys";
//			String password = "g3n3sys";
			Class.forName(driver); // load MySQL driver

			// Create a connection to the database

			connection = DriverManager.getConnection(url, userName, password);
			if (connection != null) {
				System.out.println("Successfully Connected to the database!");
			}
			

		} catch (ClassNotFoundException e) {

			System.out.println("Could not find the database driver " + e.getMessage());
		} catch (SQLException e) {

			System.out.println("Could not connect to the database " + e.getMessage());
		}

        try {    
        @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader( new FileReader(fileName));    
            StringTokenizer st = null;    
            int lineNumber = 0, tokenNumber = 0;    
            // open file input stream
    		@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

    		// read file line by line 
    		String line = reader.readLine();
    		List<String> result = null;
    		if (line.indexOf("\t") != -1) {
    			result = Arrays.asList(line.split("\\t"));
    		} else {
    			result = Arrays.asList(line.split("\\s*,\\s*"));
    		}
    		System.out.println(result);
    		// read file data line by line 
            while( (fileName = br.readLine()) != null)    
            {    

                if(lineNumber++ == 0)  
                   continue;                  

                //break comma separated line using ","    
                st = new StringTokenizer(fileName, ",");    

                while(st.hasMoreTokens())    
                {    
                    //display csv values    
                    tokenNumber++;    
                    System.out.print(st.nextToken() + ','); 
                }    

                //new line    
                System.out.println(" ");    

                //reset token number    
                tokenNumber = 0;    

            }    
        } catch (FileNotFoundException e) {    
            // TODO Auto-generated catch block    
            e.printStackTrace();    
        } catch (IOException e) {    
            // TODO Auto-generated catch block    
            e.printStackTrace();    
        }
    }
}