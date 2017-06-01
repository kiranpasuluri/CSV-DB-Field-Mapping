package za.odek.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;

import com.google.common.base.Joiner;

import za.odek.action.FilesUtil;
import za.odek.dto.ConnectionDetailsDTO;

public class PostgresDAO  implements SessionAware {

	private ConnectionDetailsDTO mConnectionDetailsDTO;
	public PostgresDAO(ConnectionDetailsDTO pConnectionDetailsDTO){
		mConnectionDetailsDTO = pConnectionDetailsDTO;
	}
	private Connection con = null;
	
	public Map<Integer, String> getSchemas() {
		//con = getConnection();
		con = getLocalhostConnection();
		Map<Integer, String> schemaMap = null;
		Statement statement = null;
		ResultSet results = null;
		try {

			// Create a result set

			statement = con.createStatement();

			results = statement.executeQuery("select schema_name from information_schema.schemata");//select catalog_name,schema_name from information_schema.schemata
			schemaMap = new HashMap<>();
			
			while (results.next()) {
				schemaMap.put(new Integer(results.getString("catalog_name")), results.getString("schema_name"));
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		} finally {
			closeConnection(statement, results);
		}
		return schemaMap;
	}
	
	public List<String> getSchemaNames() {
		//con = getConnection();
		con = getLocalhostConnection();
		List<String> dbList = null;
		Statement statement = null;
		ResultSet results = null;
		try {

			// Create a result set

			statement = con.createStatement();

			results = statement.executeQuery("select schema_name from information_schema.schemata");
			dbList = new ArrayList<>();
			
			while (results.next()) {
				dbList.add(results.getString("schema_name"));
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		} finally {
			closeConnection(statement, results);
		}
		return dbList;
	}
	
	public List<String> getTables(String schemaName) throws SQLException {
		List<String> tables = new ArrayList<>();
		Statement statement = null;
		ResultSet results = null;
		try {
			con = getLocalhostConnection();
			statement = con.createStatement();
			results = statement.executeQuery("select tablename from pg_tables where schemaname='"+schemaName+"'");
			while (results.next()) {
				tables.add(results.getString("tablename"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnection(statement, results);
		}
		return tables;
	}
	
	public void overWriteTableData(String database, String tablename) {
		con = getLocalhostConnection();
		Statement statement = null;
		ResultSet results = null;

		try {
			statement = con.createStatement();
			results = statement.executeQuery("delete FROM \"" + database + "\"." + tablename);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnection(statement, results);
		}
	}
	public Map<String, String> getTableMetadata(String database, String tablename) {
		//con = getConnection();
		con = getLocalhostConnection();
		Map<String, String> columnMap = new HashMap<>();
		Statement statement = null;
		ResultSet results = null;
		try {

			// Create a result set

			statement = con.createStatement();
			System.out.println("databse :"+database+" tablename :"+tablename+";");
			System.out.println("statement :"+statement);
			results = statement.executeQuery("SELECT column_name,data_type FROM information_schema.columns WHERE table_schema = '"+database+"' AND table_name = '"+tablename+"'");
			ResultSetMetaData metaData = results.getMetaData();
			if(results != null){
				while (results.next()) {
					columnMap.put(results.getString("column_name"), results.getString("data_type"));
				}
			}
//			if (metaData != null) {
//				System.out.println(database + "." + tablename + "ColumnCount()" + metaData.getColumnCount());
//				for (int i = 1; i <= metaData.getColumnCount(); i++) {
//					columnMap.put(results.getString("column_name"), results.getString("data_type"));
//					//columnMap.put(metaData.getColumnName(i).valueOf(metaData), metaData.getColumnName(i).valueOf(metaData));
//				}
//			}

		} catch (Exception e) {

			System.out.println(e.getMessage());
		} finally {
			closeConnection(statement, results);
		}
		return columnMap;
			
	}
		private void closeResultset(ResultSet results) {
			if (results != null)
				try {
					results.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		
		private void closeStatement(Statement statement) {
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		
		private void closeConnection(Statement statement, ResultSet results) {
			if (con != null) {
				try {
					closeStatement(statement);
					closeResultset(results);
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		/*private Connection getConnection() {

			Connection connection = null;
			try {

				String driver = "org.postgresql.Driver";
				String url = "jdbc:postgresql://localhost:5432/postgres"; // jdbc:jtds:sqlserver://dbhost:dbport;DatabaseName=dbname
				String username = "postgres";
				//String password = "1qazXsw2";
				String password = "postgres";
				Class.forName(driver); // load MySQL driver

				// Create a connection to the database

				connection = DriverManager.getConnection(url, username, password);

				System.out.println("Successfully Connected to the database!");

			} catch (ClassNotFoundException e) {

				System.out.println("Could not find the database driver " + e.getMessage());
			} catch (SQLException e) {

				System.out.println("Could not connect to the database " + e.getMessage());
			}


			return connection;
		}*/

		private Connection getLocalhostConnection() {

			Connection connection = null;
			try {

				String driver = "org.postgresql.Driver";
				String url = "jdbc:postgresql://"+mConnectionDetailsDTO.getDbHostName()+":"+mConnectionDetailsDTO.getDbPort()+"/postgres"; // jdbc:jtds:sqlserver://dbhost:dbport;DatabaseName=dbname
				//String username = "postgres";
				//String password = "1qazXsw2";
				//String password = "postgres";
				Class.forName(driver); // load MySQL driver

				// Create a connection to the database

				connection = DriverManager.getConnection(url, mConnectionDetailsDTO.getDbUser(), mConnectionDetailsDTO.getDbPassword());

				System.out.println("Successfully Connected to the database!");

			} catch (ClassNotFoundException e) {

				System.out.println("Could not find the database driver " + e.getMessage());
			} catch (SQLException e) {

				System.out.println("Could not connect to the database " + e.getMessage());
			}


			return connection;
		}
		
		public void insertRecord(String database, String tablename, Map<Integer, List<String>> pMap, List<String> csvHeader)
				throws FileNotFoundException, IOException {
			List<String> tableColumns;
			String query = getQueryFromMap(pMap, database, tablename);
			System.out.println("Query:" + query);

			String[] vStrings = getvCSVValues().split(",");
			System.out.println("Strings ::" + vStrings);
			System.out.println("getvCSVValues() ::" + getvCSVValues());
			String[] headerArray = (String[]) csvHeader.toArray(new String[csvHeader.size()]);
			for (int i = 0; i < headerArray.length; i++) {
				System.out.println("Header ::" + headerArray[i]);
			}
			
			//"insert into \""+database+"\".\""+tablename+"\"(" +  + ") values ('");

			StringBuffer SQL = new StringBuffer("insert into \""+database+"\".\""+tablename+"\"(" + "" + ") values (");
			System.out.println(SQL);
//			if(sessionMap.get("uploadedCSVFile") != null){
//				System.out.println("******  uploadedCSVFile in insert ******"+sessionMap.get("uploadedCSVFile"));
//			}
			
			// Read file from location where you have stored
			File file = new File("C:/Users/Kiran Pasuluri/Desktop/Employee.csv");

			Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader(headerArray).withSkipHeaderRecord()
					.parse(new FileReader(file));
			List<String> insertSQL = new ArrayList<>();
			Iterator<CSVRecord> vIterator = records.iterator();

			while (vIterator.hasNext()) {
				CSVRecord record = (CSVRecord) vIterator.next();
				System.out.println("RECORD:" + record);
				String row = "";
				if (record.size() > 1) {

					for (int i = 0; i < vStrings.length; i++) {
						System.out.println("vStrings[i] ::" + vStrings[i]);
						if (csvHeaderConstants.containsKey(vStrings[i])) {
							String value = csvHeaderConstants.get(vStrings[i]);
							System.out.println("Contains Key and value is ::" + value);
							if ("null".equals(value.trim())) {
								System.out.println("in null if");
								if (i == vStrings.length - 1) {
									row += "'" + StringUtils.trim(record.get(vStrings[i])) + "')";
								} else {
									row += "'" + StringUtils.trim(record.get(vStrings[i])) + "',";
								}
							} else {
								if (i == vStrings.length - 1) {
									row += "'" + StringUtils.trim(value) + "')";
								} else {
									row += "'" + StringUtils.trim(value) + "',";
								}
							}
						}

					}
				}
				System.out.println("row = " + row);
				insertSQL.add(query + row);

			}
			Statement statement = null;
			ResultSet results = null;
			for (String sql : insertSQL) {
				System.out.println("insertSQL : " + sql);
				con = getLocalhostConnection();
				try {
					statement = con.createStatement();
					statement.addBatch(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					statement.executeBatch();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					closeConnection(statement, results);
				}
			}
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private String getQueryFromMap(Map<Integer, List<String>> map, String pDatabase, String pTablename) {
			String query = "insert into \"" + pDatabase + "\".\"" + pTablename + "\"(<Column>) values (";
			int tableColumn = 1;
			int csvColumn = 2;
			int coulmnConstant = 3;
			int datatype = 4;
			int index = 1;
			String tableColumns = "";
			String csvColumns = "";
			String csvValues = "";
			String coulmnConstants = "";
			Map<String, String> csvHeaderConstants = new HashMap();
			String key = null;
			for (Map.Entry m : map.entrySet()) {

				index = 1;
				List<String> temp = (List<String>) m.getValue();
				for (String s : temp) {
					System.out.println("S From ArrayList::" + s);
					if (String.valueOf(map.size() - 1).equals(m.getKey().toString())) {
						if (index == tableColumn)
							tableColumns = tableColumns + s;
						if (index == csvColumn) {
							csvColumns = csvColumns + s + ",";
							key = s;
						}
						if (index == coulmnConstant) {
							System.out.println("----coulmnConstant---" + coulmnConstant);
							if (StringUtils.isNotEmpty(s))
								coulmnConstants = s;
							if (StringUtils.isEmpty(s))
								coulmnConstants = null;
						}

						if (index == datatype) {
							System.out.println("coulmnConstants ::" + coulmnConstants);
							if (StringUtils.isNotEmpty(coulmnConstants)) {
								if (s.equalsIgnoreCase("varchar"))
									csvValues = csvValues + "'" + coulmnConstants + "',";
								else if (s.equalsIgnoreCase("int"))
									csvValues = csvValues + coulmnConstants + ",";
								csvHeaderConstants.put(key, coulmnConstants);
							} else {
								csvValues = csvValues + "," + null + ",";
								csvHeaderConstants.put(key, null);
							}
						}
					} else {
						if (index == tableColumn)
							tableColumns = tableColumns + s + ",";
						if (index == csvColumn) {
							csvColumns = csvColumns + s + ",";
							key = s;
						}
						if (index == coulmnConstant) {
							System.out.println("----coulmnConstant---" + csvValues);
							if (StringUtils.isNotEmpty(s))
								coulmnConstants = s;
							if (StringUtils.isEmpty(s))
								coulmnConstants = null;
						}

						if (index == datatype) {
							System.out.println("coulmnConstants ::" + coulmnConstants);
							System.out.println("csvValues ::" + csvValues);
							if (StringUtils.isNotEmpty(coulmnConstants)) {
								System.out.println("Is not empty");
								if (s.equalsIgnoreCase("varchar"))
									csvValues = csvValues + "'" + coulmnConstants + "',";
								else if (s.equalsIgnoreCase("int"))
									csvValues = csvValues + coulmnConstants + ",";
								csvHeaderConstants.put(key, coulmnConstants);
							}
							if (StringUtils.isEmpty(coulmnConstants)) {
								csvValues = csvValues + "," + null + ",";
								csvHeaderConstants.put(key, null);
							}
						}
					}
					index++;
				}
			}

			query = query.replace("<Column>", tableColumns);
			query = query.replace("<values>", csvValues);
			setvCSVValues(csvColumns);
			setCsvHeaderConstants(csvHeaderConstants);
			return query;
		}
		/**
		 * @return the vCSVValues
		 */
		public String getvCSVValues() {
			return vCSVValues;
		}

		/**
		 * @param pVCSVValues
		 *            the vCSVValues to set
		 */
		public void setvCSVValues(String pVCSVValues) {
			vCSVValues = pVCSVValues;
		}

		/**
		 * @return the csvHeaderConstants
		 */
		public Map<String, String> getCsvHeaderConstants() {
			return csvHeaderConstants;
		}

		/**
		 * @param pCsvHeaderConstants
		 *            the csvHeaderConstants to set
		 */
		public void setCsvHeaderConstants(Map<String, String> pCsvHeaderConstants) {
			csvHeaderConstants = pCsvHeaderConstants;
		}

		private String vCSVValues;
		private Map<String, String> csvHeaderConstants;

		@Override
		public void setSession(Map<String, Object> sessionMap) {
			this.setSessionMap(sessionMap);

		}

		/**
		 * @return the sessionMap
		 */
		public Map<String, Object> getSessionMap() {
			return sessionMap;
		}

		/**
		 * @param pSessionMap the sessionMap to set
		 */
		public void setSessionMap(Map<String, Object> pSessionMap) {
			sessionMap = pSessionMap;
		}

		private Map<String, Object> sessionMap;
	}
