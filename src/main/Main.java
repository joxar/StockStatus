package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import utility.*;

public class Main {

	/*** constants ***/
	// for output files
	static final String FTYPE_HTML = "html";
	static final String FTYPE_GIF = "gif";
	static final String SEPARATOR = ";";
	static final String PROP_FILE = "env.properties";

	// for mail sender
	static final String SEND_TO = "sendTo";
	static final String USER_NAME = "userName";
	static final String PASSWORD = "password";
	static final String PAGE_URL = "pageURL";
	static final String PAGE_NUM = "pageNum";


	public static void main(String[] args) {

		/*** initialize ***/
		PropGetter pg = null;
		CommandExecutor ce = new CommandExecutor();
		HTMLAnalyzer htmlAnlyzr = new HTMLAnalyzer();
		MailSender ml = new MailSender();

		HashMap<String,String> pageInfoMap = new HashMap<String,String>();
		ArrayList<int[]> xyOnPicts = new ArrayList<int[]>();
		HashMap<String,int[]> deviceInfoMap = new HashMap<String,int[]>();
		ArrayList<String[]> resultInfoList = new ArrayList<String[]>();
		String[] resultInfo = new String[4];

		String propFile = PROP_FILE;
		Properties prop = new Properties();

		try {

			/*** get properties ***/
			pg = new PropGetter(propFile);
			prop = pg.getProps();

			/*** set URLs of target pages ***/
			int pageNum = Integer.decode((String)prop.get(PAGE_NUM));
			String[] tmpStoreData = new String[pageNum];
			for (int i=0; i<pageNum; i++) {
				tmpStoreData[i] = (String)prop.get(PAGE_URL+i);
			}

			// get data of target pages
			pageInfoMap = pg.getMappingData(tmpStoreData, SEPARATOR); //[0]:store name [1]URL

			/*** get HTML files by given URLs ***/
			pageInfoMap = ce.execWget(pageInfoMap, FTYPE_HTML);

			/*** analyze given HTML files and set URLs for target resources ***/
			pageInfoMap = htmlAnlyzr.getResourceURL(pageInfoMap);

			/*** get gif files by given URLs ***/
			pageInfoMap = ce.execWget(pageInfoMap, FTYPE_GIF);

			/** judge Color **/
			int[] XY_64G_SLV = {440, 1695};
			int[] XY_64G_GLD = {440, 1735};
			int[] XY_128G_SLV = {440, 1820};
			int[] XY_128G_GLD = {440, 1860};
			int[] TEST = {440, 1650};
//			64G S 440,1695
//			64G G 440,1735
//			128G S 440,1820
//			128G G 440,1860

			deviceInfoMap.put("XY_64G_SLV", XY_64G_SLV);
			deviceInfoMap.put("XY_64G_GLD", XY_64G_GLD);
			deviceInfoMap.put("XY_128G_SLV", XY_128G_SLV);
			deviceInfoMap.put("XY_128G_GLD", XY_128G_GLD);
			/*
			 * exist: -16757083
			 * not : -1
			 */
			deviceInfoMap.put("TEST", TEST);

			GifAnalyzer ga = new GifAnalyzer();
			for (String key : pageInfoMap.keySet()) {
				resultInfo[0] = key; // store name
				resultInfo[1] = ga.getRGBcode(pageInfoMap.get(key), deviceInfoMap)[0]; // device spec
				resultInfo[2] = pageInfoMap.get(key); // table gif
				resultInfo[3] = ga.getRGBcode(pageInfoMap.get(key), deviceInfoMap)[1]; // cell rgb

				resultInfoList.add(resultInfo);
			}

			/*** judge and send a mail ***/
//				for (String key : stockInfoAsRGBMap.keySet()) {
//
//				// no stock
//				if (stockInfoAsRGBMap.get(key) == -1) {
//					System.out.println(key + " : NOT exists");
//
//				// stock exists
//				} else {
//					String uname = (String)prop.get(USER_NAME);
//					String pw = (String)prop.get(PASSWORD);
//					ml.sendMail(uname, pw, msgContent, resourceFiles);
//				}
//			}

		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
