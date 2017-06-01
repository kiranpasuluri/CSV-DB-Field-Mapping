package za.odek.dto;

import java.io.Serializable;
import java.util.Map;

public class ConnectionDetailsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  dbHostName;
	private int dbPort;
	private String dbUser;
	private String dbPassword;
	private String dbname;
	
	public ConnectionDetailsDTO(String pDbHostName, int pDbPort, String pDbUser, String pDbPassword, String pDbname) {
		super();
		dbHostName = pDbHostName;
		dbPort = pDbPort;
		dbUser = pDbUser;
		dbPassword = pDbPassword;
		dbname = pDbname;
	}
	/**
	 * @return the dbHostName
	 */
	public String getDbHostName() {
		return dbHostName;
	}
	/**
	 * @param pDbHostName the dbHostName to set
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
	 * @param pDbPort the dbPort to set
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
	 * @param pDbUser the dbUser to set
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
	 * @param pDbPassword the dbPassword to set
	 */
	public void setDbPassword(String pDbPassword) {
		dbPassword = pDbPassword;
	}
	/**
	 * @return the dbname
	 */
	public String getDbname() {
		return dbname;
	}
	/**
	 * @param pDbname the dbname to set
	 */
	public void setDbname(String pDbname) {
		dbname = pDbname;
	}
	
	
}
