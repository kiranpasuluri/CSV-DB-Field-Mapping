package za.odek.action;

import java.io.ByteArrayInputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class SFTPTest {

    public static void main(String[] args) {
        JSch jsch = new JSch();
        Session session = null;
        try {
//        	//SessionOptions.SshHostKeyFingerprint
//        	String knownHostPublicKey = "ssh-rsa 2048 7f:ac:2a:e4:d9:2a:38:09:e1:1a:2e:98:ae:01:e4:41";
//	    	jsch.setKnownHosts(new ByteArrayInputStream(knownHostPublicKey.getBytes()));
            session = jsch.getSession("Genesys", "rpsftp.realpeople.co.za",22); //default port is 22
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.setPassword("@3QcqF4!".getBytes());
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("Connected");
        } catch (JSchException e) {
            e.printStackTrace(System.out);
        } catch (Exception e){
            e.printStackTrace(System.out);
        } finally{
            session.disconnect();
            System.out.println("Disconnected");
        }
    }

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

        @Override
        public String getPassphrase() {
            return null;
        }
        @Override
        public String getPassword() {
            return null;
        }
        @Override
        public boolean promptPassphrase(String arg0) {
            return false;
        }
        @Override
        public boolean promptPassword(String arg0) {
            return false;
        }
        @Override
        public boolean promptYesNo(String arg0) {
            return false;
        }
        @Override
        public void showMessage(String arg0) {
        }
        @Override
        public String[] promptKeyboardInteractive(String arg0, String arg1,
                String arg2, String[] arg3, boolean[] arg4) {
            return null;
        }
    }
}
