package za.odek.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.base.Joiner;

import za.odek.db.MySQLDAO;
import za.odek.db.OracleDAO;
import za.odek.db.PostgresDAO;
import za.odek.db.SqlServerDAO;
import za.odek.dto.ConnectionDetailsDTO;

public class OdekSQLAction implements SessionAware {

	private String dbHostName;
	private int dbPort;
	private String dbUser;
	private String dbPassword;
	private String dbname;
	private int schemaId;
	private String schemaName;
	private String database;
	private Map<Integer, String> schemaMap = new HashMap<>();
	private Map<String, String> columnMap = new HashMap<>();
	private List<String> databaseList = new ArrayList<>();
	private List<String> tables = new ArrayList<>();
	private String tablename;
	private String csvDBMap;
	private boolean submitform;
	private boolean loadmap;
	private int dbOperation;
	public List<String> mapfilesList = new ArrayList<>();
	private String mapname;
	private String mapselected;
	private String staticData;
	private String saveAsMapName;
	
	public String execute() throws Exception {
		
		dbname = getDbname();
		staticData = getStaticData();
		
		if (loadmap) {
			getFileNamesFromLocation();
		}
		if (StringUtils.isNotEmpty(mapselected)){
			Map<Integer, List<String>> vMapFromLocation = getMapFromLocation();
			sessionMap.put("CSVDBMap", vMapFromLocation);
		}
		if (StringUtils.isEmpty(dbname)) {
				dbname = (String) sessionMap.get("dbtype");
				System.out.println("Dbname from session::"+dbname);
		}
		ConnectionDetailsDTO detailsDTO = null;
		
		if(sessionMap.get("conDetails") != null){
			detailsDTO = (ConnectionDetailsDTO) sessionMap.get("conDetails");
		}
		if(!StringUtils.isEmpty(dbHostName) && !StringUtils.isEmpty(dbUser) && !StringUtils.isEmpty(dbPassword) && !StringUtils.isEmpty(dbname) && dbPort != 0){
			detailsDTO = new ConnectionDetailsDTO(dbHostName, dbPort, dbUser, dbPassword, dbname);
			sessionMap.put("conDetails", detailsDTO);
		}		
		
		if ("Microsoft SQL Server".equals(dbname)) {
			try {
				setDatabaseList(new SqlServerDAO(detailsDTO).getSchemaNames());
				sessionMap.put("dbtype", dbname);
				database = getDatabase();				
				if (StringUtils.isNotEmpty(database)) {
					setTables(new SqlServerDAO(detailsDTO).getTables(database));
					sessionMap.put("databaseSelected", database);
				}
				tablename = getTablename();				
				if (StringUtils.isNotEmpty(tablename)) {					
					sessionMap.put("columns", new SqlServerDAO(detailsDTO).getTableMetadata(String.valueOf(sessionMap.get("databaseSelected")), tablename));
					sessionMap.put("sesstableName", tablename);
				}
				//System.out.println("Map returned ::" + getCsvDBMap());	
				if (StringUtils.isNotEmpty(getCsvDBMap())) {
					StringTokenizer strings = new StringTokenizer(getCsvDBMap(), "@");
					Map<Integer, List<String>> vMap = new HashMap<>();
					for (int i = 0; strings.hasMoreTokens(); i++)
				    {
						String nextToken = strings.nextToken();
						//System.out.println("next token:" + nextToken);
						StringTokenizer mapValue = new StringTokenizer(nextToken, "=");
						List<String> mapDataList = new ArrayList<>();
						while (mapValue.hasMoreTokens()) {
							mapDataList.add(mapValue.nextToken());					
						}
						vMap.put(i, mapDataList);
					}
					saveMapToFile(vMap);
					sessionMap.put("CSVDBMap", vMap);
				}
				System.out.println(isSubmitform());
				if (isSubmitform()) {
					//System.out.println("form submission");
					if(getDbOperation()==1){
						System.out.println("DbOperation Append= "+getDbOperation());
					}else if(getDbOperation()==2){
						System.out.println("DbOperation OverWrite= "+getDbOperation());
						new SqlServerDAO(detailsDTO).overWriteTableData(String.valueOf(sessionMap.get("databaseSelected")),(String) (sessionMap.get("sesstableName")));
					}						
					new SqlServerDAO(detailsDTO).insertRecord(String.valueOf(sessionMap.get("databaseSelected")),(String) (sessionMap.get("sesstableName")), (Map<Integer, List<String>>) sessionMap.get("CSVDBMap"),(List<String>) sessionMap.get("csvHeaders"));
				}				
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e);
			}
		}
		
		if ("PostgreSQL".equals(dbname)) {
			try {
				setDatabaseList(new PostgresDAO(detailsDTO).getSchemaNames());
				sessionMap.put("dbtype", dbname);
				database = getDatabase();
				//System.out.println("database ::" + database);
				if (StringUtils.isNotEmpty(database)) {
					setTables(new PostgresDAO(detailsDTO).getTables(database));
					sessionMap.put("databaseSelected", database);
				}
				tablename = getTablename();
				if (StringUtils.isNotEmpty(tablename)) {					
					sessionMap.put("columns", new PostgresDAO(detailsDTO).getTableMetadata(String.valueOf(sessionMap.get("databaseSelected")), tablename));
					sessionMap.put("sesstableName", tablename);
				}
				//System.out.println("Map returned ::" + getCsvDBMap());
				if (StringUtils.isNotEmpty(getCsvDBMap())) {
					StringTokenizer strings = new StringTokenizer(getCsvDBMap(), "@");
					Map<Integer, List<String>> vMap = new HashMap<>();
					for (int i = 0; strings.hasMoreTokens(); i++)
				    {
						String nextToken = strings.nextToken();
						//System.out.println("next token:" + nextToken);
						StringTokenizer mapValue = new StringTokenizer(nextToken, "=");
						List<String> mapDataList = new ArrayList<>();
						while (mapValue.hasMoreTokens()) {
							mapDataList.add(mapValue.nextToken());					
						}
						vMap.put(i, mapDataList);
					}
					saveMapToFile(vMap);
					sessionMap.put("CSVDBMap", vMap);
				}
				System.out.println(isSubmitform());
				if (isSubmitform()) {
					//System.out.println("form submission");
					if(getDbOperation()==1){
						System.out.println("DbOperation Append= "+getDbOperation());
					}else if(getDbOperation()==2){
						System.out.println("DbOperation OverWrite= "+getDbOperation());
						new PostgresDAO(detailsDTO).overWriteTableData(String.valueOf(sessionMap.get("databaseSelected")),(String) (sessionMap.get("sesstableName")));
					}
					new PostgresDAO(detailsDTO).insertRecord(String.valueOf(sessionMap.get("databaseSelected")),(String) (sessionMap.get("sesstableName")), (Map<Integer, List<String>>) sessionMap.get("CSVDBMap"),(List<String>) sessionMap.get("csvHeaders"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e);
			}
		}
		
		if ("MySQL".equals(dbname)) {
			try {
				setDatabaseList(new MySQLDAO(detailsDTO).getSchemaNames());
				sessionMap.put("dbtype", dbname);
				database = getDatabase();
				//System.out.println("database ::" + database);
				if (StringUtils.isNotEmpty(database)) {
					setTables(new MySQLDAO(detailsDTO).getTables(database));
					sessionMap.put("databaseSelected", database);
				}
				tablename = getTablename();
				if (StringUtils.isNotEmpty(tablename)) {					
					sessionMap.put("columns", new MySQLDAO(detailsDTO).getTableMetadata(String.valueOf(sessionMap.get("databaseSelected")), tablename));
					sessionMap.put("sesstableName", tablename);
				}
				//System.out.println("Map returned ::" + getCsvDBMap());
				if (StringUtils.isNotEmpty(getCsvDBMap())) {
					StringTokenizer strings = new StringTokenizer(getCsvDBMap(), "@");
					Map<Integer, List<String>> vMap = new HashMap<>();
					for (int i = 0; strings.hasMoreTokens(); i++)
				    {
						String nextToken = strings.nextToken();
						//System.out.println("next token:" + nextToken);
						StringTokenizer mapValue = new StringTokenizer(nextToken, "=");
						List<String> mapDataList = new ArrayList<>();
						while (mapValue.hasMoreTokens()) {
							mapDataList.add(mapValue.nextToken());					
						}
						vMap.put(i, mapDataList);
					}
					saveMapToFile(vMap);
					sessionMap.put("CSVDBMap", vMap);
				}
				System.out.println(isSubmitform());
				if (isSubmitform()) {
					//System.out.println("form submission");
					if(getDbOperation()==1){
						System.out.println("DbOperation Append= "+getDbOperation());
					}else if(getDbOperation()==2){
						System.out.println("DbOperation OverWrite= "+getDbOperation());
						new MySQLDAO(detailsDTO).overWriteTableData(String.valueOf(sessionMap.get("databaseSelected")),(String) (sessionMap.get("sesstableName")));
					}
					new MySQLDAO(detailsDTO).insertRecord(String.valueOf(sessionMap.get("databaseSelected")),(String) (sessionMap.get("sesstableName")), (Map<Integer, List<String>>) sessionMap.get("CSVDBMap"),(List<String>) sessionMap.get("csvHeaders"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e);
			}
		}
		
		return "success";
	}

	private Map<Integer, List<String>> getMapFromLocation() {
		JSONParser parser = new JSONParser();
		HashMap<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        try {
        	//System.out.println("Selected Map is ::"+getMapselected());
        	org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) parser.parse(new FileReader("C:\\kiran\\"+getMapselected()));
            Set<Entry<String, List<String>>> vEntrySet = jsonObject.entrySet();
            
            for(Entry<String, List<String>> entry : vEntrySet)
            {
                //System.out.println(entry.getKey()+","+ entry.getValue());
                map.put(Integer.parseInt(entry.getKey()), entry.getValue());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

		return map;
	}

	private void saveMapToFile(Map<Integer, List<String>> pMap) {
			ConnectionDetailsDTO detailsDTO = null;
		
		if(sessionMap.get("conDetails") != null){
			detailsDTO = (ConnectionDetailsDTO) sessionMap.get("conDetails");
		}
		JSONObject jsonObj = new JSONObject(pMap);
		//System.out.println("saveAsMapName : "+getSaveAsMapName());
		String mapName;
		if(StringUtils.isNotEmpty(getSaveAsMapName())){
			 mapName = getSaveAsMapName();
		}else{
			mapName = Long.toString(System.currentTimeMillis());
		}
		try (FileWriter file = new FileWriter("C:\\kiran\\"+mapName+".json")) {

            file.write(jsonObj.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void getFileNamesFromLocation() {
		
		File folder = new File("C:\\kiran");
		FilenameFilter jsonFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".json")) {
					return true;
				} else {
					return false;
				}
			}
		};

		File[] listOfFiles = folder.listFiles(jsonFilter);
		//List<String> filesList = new ArrayList<>();
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        //System.out.println("File " + listOfFiles[i].getName());
		        mapfilesList.add(listOfFiles[i].getName());
		      } 
		    }
			//return mapfilesList;
		    //System.out.println("map file list returned");
		}
	/**
	 * @return the dbname
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * @param dbname
	 *            the dbname to set
	 */
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	/**
	 * @return the schemaMao
	 */
	public Map<Integer, String> getSchemaMap() {
		return schemaMap;
	}

	/**
	 * @param schemaMao
	 *            the schemaMao to set
	 */
	public void setSchemaMap(Map<Integer, String> schemaMap) {
		this.schemaMap = schemaMap;
	}

	/**
	 * @return the schemaId
	 */
	public int getSchemaId() {
		return schemaId;
	}

	/**
	 * @param schemaId
	 *            the schemaId to set
	 */
	public void setSchemaId(int schemaId) {
		this.schemaId = schemaId;
	}

	/**
	 * @return the tables
	 */
	public List<String> getTables() {
		return tables;
	}

	/**
	 * @param tables
	 *            the tables to set
	 */
	public void setTables(List<String> tables) {
		this.tables = tables;
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * @return the tablename
	 */
	public String getTablename() {
		return tablename;
	}

	/**
	 * @param tablename
	 *            the tablename to set
	 */
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	/**
	 * @return the columnMap
	 */
	public Map<String, String> getColumnMap() {
		return columnMap;
	}

	/**
	 * @param columnMap
	 *            the columnMap to set
	 */
	public void setColumnMap(Map<String, String> columnMap) {
		this.columnMap = columnMap;
	}

	/**
	 * @return the databaseList
	 */
	public List<String> getDatabaseList() {
		return databaseList;
	}

	/**
	 * @param databaseList
	 *            the databaseList to set
	 */
	public void setDatabaseList(List<String> databaseList) {
		this.databaseList = databaseList;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;

	}

	private Map<String, Object> sessionMap;

	public void saveCSVHeader(List<String> csvheader) {
		sessionMap.put("csvHeaders", csvheader);
		System.out.println(csvheader.size());

	}

	/**
	 * @return the csvDBMap
	 */
	public String getCsvDBMap() {
		return csvDBMap;
	}

	/**
	 * @param csvDBMap
	 *            the csvDBMap to set
	 */
	public void setCsvDBMap(String csvDBMap) {
		this.csvDBMap = csvDBMap;
	}

	/**
	 * @return the submitform
	 */
	public boolean isSubmitform() {
		return submitform;
	}

	/**
	 * @param submitform
	 *            the submitform to set
	 */
	public void setSubmitform(boolean submitform) {
		this.submitform = submitform;
	}

	/**
	 * @return the dbHostName
	 */
	public String getDbHostName() {
		return dbHostName;
	}

	/**
	 * @param pDbHostName
	 *            the dbHostName to set
	 */
	public void setDbHostName(String pDbHostName) {
		dbHostName = pDbHostName;
	}

	/**
	 * @return the dbPort
	 */
	public int getDbPort() {
		return dbPort;
	}

	/**
	 * @param pDbPort
	 *            the dbPort to set
	 */
	public void setDbPort(int pDbPort) {
		dbPort = pDbPort;
	}

	/**
	 * @return the dbUser
	 */
	public String getDbUser() {
		return dbUser;
	}

	/**
	 * @param pDbUser
	 *            the dbUser to set
	 */
	public void setDbUser(String pDbUser) {
		dbUser = pDbUser;
	}

	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/**
	 * @param pDbPassword
	 *            the dbPassword to set
	 */
	public void setDbPassword(String pDbPassword) {
		dbPassword = pDbPassword;
	}

	/**
	 * @return the loadmap
	 */
	public boolean isLoadmap() {
		return loadmap;
	}

	/**
	 * @param pLoadmap the loadmap to set
	 */
	public void setLoadmap(boolean pLoadmap) {
		loadmap = pLoadmap;
	}

	/**
	 * @return the mapfilesList
	 */
	public List<String> getMapfilesList() {
		return mapfilesList;
	}

	/**
	 * @param pMapfilesList the mapfilesList to set
	 */
	public void setMapfilesList(List<String> pMapfilesList) {
		mapfilesList = pMapfilesList;
	}

	/**
	 * @return the mapname
	 */
	public String getMapname() {
		return mapname;
	}

	/**
	 * @param pMapname the mapname to set
	 */
	public void setMapname(String pMapname) {
		mapname = pMapname;
	}

	/**
	 * @return the mapselected
	 */
	public String getMapselected() {
		return mapselected;
	}

	/**
	 * @param pMapselected the mapselected to set
	 */
	public void setMapselected(String pMapselected) {
		mapselected = pMapselected;
	}

	/**
	 * @return the staticData
	 */
	public String getStaticData() {
		return staticData;
	}

	/**
	 * @param pStaticData the staticData to set
	 */
	public void setStaticData(String pStaticData) {
		staticData = pStaticData;
	}

	/**
	 * @return the saveAsMapName
	 */
	public String getSaveAsMapName() {
		return saveAsMapName;
	}

	/**
	 * @param pSaveAsMapName the saveAsMapName to set
	 */
	public void setSaveAsMapName(String pSaveAsMapName) {
		saveAsMapName = pSaveAsMapName;
	}

	/**
	 * @return the dbOperation
	 */
	public int getDbOperation() {
		return dbOperation;
	}

	/**
	 * @param pDbOperation the dbOperation to set
	 */
	public void setDbOperation(int pDbOperation) {
		dbOperation = pDbOperation;
	}
}
