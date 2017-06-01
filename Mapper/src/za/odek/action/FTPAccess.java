package za.odek.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.net.ssl.SSLSession;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

public class FTPAccess {
	    private static void showServerReply(FTPClient ftpClient) {
	        String[] replies = ftpClient.getReplyStrings();
	        if (replies != null && replies.length > 0) {
	            for (String aReply : replies) {
	                System.out.println("SERVER: " + aReply);
	            }
	        }
	    }
	    /*public static void main(String[] args) {
	        String server = "rpsftp.realpeople.co.za";
	        int port = 22;
	        String user = "Genesys";
	        String pass = "@3QcqF4!";
	        FTPClient ftpClient = new FTPClient();
	        try {
	            ftpClient.connect(server, port);
	            showServerReply(ftpClient);
	            int replyCode = ftpClient.getReplyCode();
	            if (!FTPReply.isPositiveCompletion(replyCode)) {
	                System.out.println("Operation failed. Server reply code: " + replyCode);
	                return;
	            }
	            boolean success = ftpClient.login(user, pass);
	            showServerReply(ftpClient);
	            if (!success) {
	                System.out.println("Could not login to the server");
	                return;
	            } else {
	                System.out.println("LOGGED IN SERVER");
	            }
	        } catch (IOException ex) {
	            System.out.println("Oops! Something wrong happened");
	            ex.printStackTrace();
	        }
	    }
	    public void getRemoteMapFile(String server,int port,String user,String pass) {
//	        String server = "rpsftp.realpeople.co.za";
//	        int port = 22;
//	        String user = "Genesys";
//	        String pass = "@3QcqF4!";
	        FTPClient ftpClient = new FTPClient();
	        try {
	            ftpClient.connect(server, port);
	            showServerReply(ftpClient);
	            int replyCode = ftpClient.getReplyCode();
	            if (!FTPReply.isPositiveCompletion(replyCode)) {
	                System.out.println("Operation failed. Server reply code: " + replyCode);
	                return;
	            }
	            boolean success = ftpClient.login(user, pass);
	            showServerReply(ftpClient);
	            if (!success) {
	                System.out.println("Could not login to the server");
	                return;
	            } else {
	                System.out.println("LOGGED IN SERVER");
	            }
	        } catch (IOException ex) {
	            System.out.println("Oops! Something wrong happened");
	            ex.printStackTrace();
	        }
	    }*/
	    public static void main(String[] args) throws JSchException, SftpException {
	    	JSch jsch = new JSch();
	    	String knownHostPublicKey = "rpsftp.realpeople.co.za ssh-rsa 2048 7f:ac:2a:e4:d9:2a:38:09:e1:1a:2e:98:ae:01:e4:41";
	    	jsch.setKnownHosts(new ByteArrayInputStream(knownHostPublicKey.getBytes()));
	    	//String knownHostsFilename = "/ToGenesys"; rpsftp.realpeople.co.za 197.97.114.165
	    	//jsch.setKnownHosts( knownHostsFilename );

	    	Session session = jsch.getSession( "Genesys", "rpsftp.realpeople.co.za",22 );    
	    	{
	    	  // "interactive" version
	    	  // can selectively update specified known_hosts file 
	    	  // need to implement UserInfo interface
	    	  // MyUserInfo is a swing implementation provided in 
	    	  //  examples/Sftp.java in the JSch dist
	    	  UserInfo ui = new MyUserInfo();
	    	  session.setUserInfo(ui);

	    	  // OR non-interactive version. Relies in host key being in known-hosts file
	    	  session.setPassword( "@3QcqF4!" );
	    	}

	    	session.connect();

	    	Channel channel = session.openChannel( "sftp" );
	    	channel.connect();

	    	ChannelSftp sftpChannel = (ChannelSftp) channel;
	    	System.out.println(sftpChannel.pwd());
	    	sftpChannel.cd("ToGenesys");
	    	System.out.println(sftpChannel.pwd());
	    	/*sftpChannel.get("remote-file", "local-file" );
	    	// OR*/
	    	String destinationPath = "C:\\demo\\";
	    	Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*");
	    	for(ChannelSftp.LsEntry entry : list) {
	    		System.out.println(entry.getFilename());
				sftpChannel.get(entry.getFilename(), destinationPath);
	    	}
	    	InputStream in = sftpChannel.get( "FamilyCover_GenRPAC_l1.csv.processed.636270696209486083" );
	    	  // process inputstream as needed
			
	    	sftpChannel.exit();
	    	session.disconnect();
		}
	}
