package za.odek.action;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPAction {

	private String ftpHostName;
	private String ftpPortNumber;
	private String ftpUserName;
	private String ftpPassword;
	private String ftpRemotedir;
	
	/**
	 * @return the ftpHostName
	 */
	public String getFtpHostName() {
		return ftpHostName;
	}
	/**
	 * @param pFtpHostName the ftpHostName to set
	 */
	public void setFtpHostName(String pFtpHostName) {
		ftpHostName = pFtpHostName;
	}
	/**
	 * @return the ftpPortNumber
	 */
	public String getFtpPortNumber() {
		return ftpPortNumber;
	}
	/**
	 * @param pFtpPortNumber the ftpPortNumber to set
	 */
	public void setFtpPortNumber(String pFtpPortNumber) {
		ftpPortNumber = pFtpPortNumber;
	}
	/**
	 * @return the ftpUserName
	 */
	public String getFtpUserName() {
		return ftpUserName;
	}
	/**
	 * @param pFtpUserName the ftpUserName to set
	 */
	public void setFtpUserName(String pFtpUserName) {
		ftpUserName = pFtpUserName;
	}
	/**
	 * @return the ftpPassword
	 */
	public String getFtpPassword() {
		return ftpPassword;
	}
	/**
	 * @param pFtpPassword the ftpPassword to set
	 */
	public void setFtpPassword(String pFtpPassword) {
		ftpPassword = pFtpPassword;
	}
	private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
	public String execute() {
		System.err.println(ftpHostName+","+ftpPassword+","+ftpPortNumber+","+ftpUserName+","+ftpRemotedir);
		FTPClient ftpClient = new FTPClient();
		
        try {
            ftpClient.connect(getFtpHostName(),22);//Integer.parseInt(getFtpPortNumber())
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return "success";
            }
            boolean success = ftpClient.login(getFtpUserName(), getFtpPassword());
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("Could not login to the server");
                return "success";
            } else {
                System.out.println("LOGGED IN SERVER");
            }
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }
        return "success";
	}
	/**
	 * @return the ftpRemotedir
	 */
	public String getFtpRemotedir() {
		return ftpRemotedir;
	}
	/**
	 * @param pFtpRemotedir the ftpRemotedir to set
	 */
	public void setFtpRemotedir(String pFtpRemotedir) {
		ftpRemotedir = pFtpRemotedir;
	}
}
