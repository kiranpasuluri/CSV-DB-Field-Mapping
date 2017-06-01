package za.odek.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;

import za.odek.utils.FileScanner;

public class UploadFileAction extends ActionSupport implements ServletContextAware, SessionAware {

	private static final long serialVersionUID = -4748500436762141116L;

	@Override
	public String execute() {
		
		System.out.println("File Name is:" + getFileFileName());
		System.out.println("File ContentType is:" + getFileContentType());
		System.out.println("Files Directory is:" + file.getAbsolutePath());
		
		FileScanner fileScan = new FileScanner();
		try {

			csvheader = fileScan.getHeadersFromFile(file.getAbsolutePath());
			//System.out.println("Header from upload ::"+csvheader);
			sessionMap.put("csvHeaders", csvheader);
			FilesUtil.saveFile(getFile(), getFileFileName(), context.getRealPath("") + File.separator + filesPath);
		} catch (IOException e) {
			e.printStackTrace();
			return INPUT;
		}
		 catch (Exception e) {
				e.printStackTrace();
				return INPUT;
		}
		return SUCCESS;

	}
	private boolean loadmap;
	private File file;
	private String fileContentType;
	private String fileFileName;
	private String filesPath;
	private ServletContext context;
	private List<String> csvheader = new ArrayList<>();
	private Map<String, Object> sessionMap;

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;

	}

	/**
	 * @return the csvheader
	 */
	public List<String> getCsvheader() {
		return csvheader;
	}

	/**
	 * @param csvheader
	 *            the csvheader to set
	 */
	public void setCsvheader(List<String> csvheader) {
		this.csvheader = csvheader;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public void setFilesPath(String filesPath) {
		this.filesPath = filesPath;
	}

	@Override
	public void setServletContext(ServletContext ctx) {
		this.context = ctx;
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

}