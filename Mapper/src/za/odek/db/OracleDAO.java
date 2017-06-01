package za.odek.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class OracleDAO {

	private Connection getLocalhostConnection() {
		 
        Connection conn = null;
 
        try {
            
            Class.forName("oracle.jdbc.OracleDriver");
           
            String dbURL2 = "jdbc:oracle:thin:@localhost:1521:productDB";
            String username = "tiger";
            String password = "scott";
            
            conn = DriverManager.getConnection(dbURL2, username, password);
            if (conn != null) {
                System.out.println("Connected with connection");
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
		return conn;
    }

	public List<String> getDatabases() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getTables(String pDatabase) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getTableMetadata(String pValueOf, String pTablename) {
		// TODO Auto-generated method stub
		return null;
	}
}