package za.odek.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;


public class FilesUtil implements SessionAware {
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
		FilesUtil.sessionMap = pSessionMap;
	}

	private static Map<String, Object> sessionMap;

	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);

	}
	public static void saveFile(File file, String fileName, String filesDirectory) throws IOException{
		FileInputStream in = null;
        FileOutputStream out = null;
        
        File dir = new File (filesDirectory);
        if ( !dir.exists() )
           dir.mkdirs();
        
        String targetPath =  dir.getPath() + File.separator + fileName;
        System.out.println("source file path ::"+file.getAbsolutePath());
        System.out.println("saving file to ::" + targetPath);
        //sessionMap.put("uploadedCSVFile", targetPath);
        File destinationFile = new File ( targetPath);
        try {
            in = new FileInputStream( file );
            out = new FileOutputStream( destinationFile );
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);
            }

        }finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        
	}
}