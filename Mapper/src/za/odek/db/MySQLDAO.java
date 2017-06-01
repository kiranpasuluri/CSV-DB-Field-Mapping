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

public class MySQLDAO implements SessionAware {

	private ConnectionDetailsDTO mConnectionDetailsDTO;

	public MySQLDAO(ConnectionDetailsDTO pConnectionDetailsDTO) {
		mConnectionDetailsDTO = pConnectionDetailsDTO;
	}

	private Connection con = null;

	public Map<Integer, String> getSchemas() {
		con = getLocalhostConnection();
		Map<Integer, String> schemaMap = null;
		Statement statement = null;
		ResultSet results = null;
		try {
			statement = con.createStatement();

			results = statement.executeQuery("SELECT schema_id, name FROM sys.schemas");
			schemaMap = new HashMap<>();

			while (results.next()) {
				schemaMap.put(new Integer(results.getInt("schema_id")), results.getString("name"));
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		} finally {
			closeConnection(statement, results);
		}
		return schemaMap;
	}

	public List<String> getSchemaNames() {

		con = getLocalhostConnection();
		List<String> dbList = null;
		Statement statement = null;
		ResultSet results = null;
		try {
			statement = con.createStatement();

			results = statement.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA");
			dbList = new ArrayList<>();

			while (results.next()) {
				dbList.add(results.getString("SCHEMA_NAME"));
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
			results = statement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='"+schemaName+"';");
			while (results.next()) {
				tables.add(results.getString("TABLE_NAME"));
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
			results = statement.executeQuery("delete FROM " + database + "." + tablename);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnection(statement, results);
		}
	}

	public Map<String, String> getTableMetadata(String database, String tablename) {

		con = getLocalhostConnection();
		Map<String, String> columnMap = new HashMap<>();
		Statement statement = null;
		ResultSet results = null;
		try {
			statement = con.createStatement();
			
			results = statement.executeQuery("SELECT COLUMN_NAME,DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA ='"+database+"' AND TABLE_NAME ='"+tablename+"';");
				while (results.next()) {					
					columnMap.put(results.getString("COLUMN_NAME"), results.getString("DATA_TYPE"));
				}

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

	private Connection getLocalhostConnection() {

		Connection connection = null;
		try {

			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://" + mConnectionDetailsDTO.getDbHostName() + ":"
					+ mConnectionDetailsDTO.getDbPort() + "?autoReconnect=true&useSSL=false"; // jdbc:jtds:sqlserver://dbhost:dbport;DatabaseName=dbname
			
			System.out.println(url);
			Class.forName(driver); // load MySQL driver

			// Create a connection to the database

			connection = DriverManager.getConnection(url, mConnectionDetailsDTO.getDbUser(),
					mConnectionDetailsDTO.getDbPassword());

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
		String[] vStrings = getvCSVValues().split(",");
		String[] headerArray = (String[]) csvHeader.toArray(new String[csvHeader.size()]);
		for (int i = 0; i < headerArray.length; i++) {
		}

		StringBuffer SQL = new StringBuffer("insert into " + database + "." + tablename + "(" + "" + ") values (");
		System.out.println(SQL);
		// Read file from location where you have stored
		File file = new File("C:/Users/Kiran Pasuluri/Desktop/person.csv");

		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader(headerArray).withSkipHeaderRecord()
				.parse(new FileReader(file));
		List<String> insertSQL = new ArrayList<>();
		Iterator<CSVRecord> vIterator = records.iterator();

		while (vIterator.hasNext()) {
			CSVRecord record = (CSVRecord) vIterator.next();
			String row = "";
			if (record.size() > 1) {

				for (int i = 0; i < vStrings.length; i++) {
					if (csvHeaderConstants.containsKey(vStrings[i])) {
						String value = csvHeaderConstants.get(vStrings[i]);
						if ("null".equals(value.trim())) {
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
		String query = "insert into " + pDatabase + "." + pTablename + "(<Column>) values (";
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
				if (String.valueOf(map.size() - 1).equals(m.getKey().toString())) {
					if (index == tableColumn)
						tableColumns = tableColumns + s;
					if (index == csvColumn) {
						csvColumns = csvColumns + s + ",";
						key = s;
					}
					if (index == coulmnConstant) {
						if (StringUtils.isNotEmpty(s))
							coulmnConstants = s;
						if (StringUtils.isEmpty(s))
							coulmnConstants = null;
					}

					if (index == datatype) {
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
						if (StringUtils.isNotEmpty(s))
							coulmnConstants = s;
						if (StringUtils.isEmpty(s))
							coulmnConstants = null;
					}

					if (index == datatype) {
						if (StringUtils.isNotEmpty(coulmnConstants)) {
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
