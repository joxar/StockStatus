package main;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import utility.*;

public class Main {

	static final String FTYPE_HTML = "html";
	static final String FTYPE_GIF = "gif";
	static final String PROP_FILE = "env.properties";
	static final String SEND_TO = "sendTo";
	static final String USER_NAME = "userName";
	static final String PASSWORD = "password";
	static final String PAGE_URL = "pageURL";
	static final String PAGE_NUM = "pageNum";


	public static void main(String[] args) {

		// initialize
		PropGetter pg = null;
		CommandExecutor ce = new CommandExecutor();
		HTMLAnalyzer htmlAnlyzr = new HTMLAnalyzer();
		MailSender ml = new MailSender();

		String propFile = PROP_FILE;
		Properties prop = new Properties();

		ArrayList<String> pageURLs = new ArrayList<String>();
		ArrayList<String> resourceURLs = new ArrayList<String>();
		ArrayList<String> htmlFiles = new ArrayList<String>();
		ArrayList<String> resourceFiles = new ArrayList<String>();

		try {

			// get properties
			pg = new PropGetter(propFile);
			prop = pg.getProp();
			int pageNum = Integer.decode((String) prop.get(PAGE_NUM));

			// set target URLs of target pages
			String tmpURL = "";
			for (int i=0; i<pageNum; i++) {
				tmpURL = (String)prop.get(PAGE_URL+i);
				pageURLs.add(tmpURL);
			}

			// get HTML files by given URLs
			htmlFiles = ce.execWget(pageURLs, FTYPE_HTML);

			// analyze given HTML files and set URLs of target resource
			resourceURLs = htmlAnlyzr.getResourceURL(htmlFiles);

			// save a file of an item element for each given resource URL
			resourceFiles = ce.execWget(resourceURLs, FTYPE_GIF);

			// send a mail
			String uname = (String)prop.get(USER_NAME);
			String pw = (String)prop.get(PASSWORD);
			ml.sendMail(uname, pw, resourceFiles);

		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

