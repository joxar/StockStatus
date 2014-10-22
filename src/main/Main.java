package main;

import java.awt.List;
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

		String[] resultInfo = null;

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
			//int[] XY_64G_SPG = {440, 1565};
			//test
			int[] XY_64G_SPG = {440, 1650};
			int[] XY_64G_SLV = {440, 1695};
			int[] XY_64G_GLD = {440, 1735};
			int[] XY_128G_SPG = {440, 1610};
			int[] XY_128G_SLV = {440, 1820};
			int[] XY_128G_GLD = {440, 1860};

			deviceInfoMap.put("64G_SPACEGREY", XY_64G_SPG);
			deviceInfoMap.put("64G_SILVEr", XY_64G_SLV);
			deviceInfoMap.put("64G_GOLD", XY_64G_GLD);
			deviceInfoMap.put("128G_SPACEGREY", XY_128G_SPG);
			deviceInfoMap.put("128G_SILVER", XY_128G_SLV);
			deviceInfoMap.put("128G_GPLD", XY_128G_GLD);

			/*
			 * exist: -16757083
			 * not : -1
			 */
			ArrayList<String[]> resultInfoList = new ArrayList<String[]>();
			GifAnalyzer ga = new GifAnalyzer();
			for (String key : pageInfoMap.keySet()) {
				for (String key2 : deviceInfoMap.keySet()) {
					resultInfo = new String[4];
					resultInfo[0] = key; // store name
					resultInfo[1] = key2; // device spec
					resultInfo[2] = pageInfoMap.get(key); // table gif
					resultInfo[3] = Integer.toString(ga.getRGBcode(pageInfoMap.get(key), deviceInfoMap.get(key2))); // rgb info on given pict

					resultInfoList.add(resultInfo);
				}

			}

			/*** judge and send a mail ***/
			for (int i=0; i<resultInfoList.size(); i++) {
				// no stock
				if ( Integer.valueOf((resultInfoList.get(i))[3]) == -1) {
					//skip

				// stock exists
				} else {
					String uname = (String)prop.get(USER_NAME);
					String pw = (String)prop.get(PASSWORD);
					System.out.println(resultInfoList.get(i)[2]);
					ml.sendMail(uname, pw, (String) prop.get(SEND_TO), resultInfoList.get(i)[0]+":"+resultInfoList.get(i)[1], resultInfoList.get(i)[2]);
				}
			}

		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
