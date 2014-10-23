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


	static final int[] XY_64G_SPG = {440, 1565};
	static final int[] XY_64G_SLV = {440, 1650};  //test value
	//static final int[] XY_64G_SLV = {440, 1695};
	static final int[] XY_64G_GLD = {440, 1735};
	static final int[] XY_128G_SPG = {440, 1610};
	static final int[] XY_128G_SLV = {440, 1820};
	static final int[] XY_128G_GLD = {440, 1860};

	static final String MSG_64G_SPG = "iPhone6Plus 64G SpaceGrey";
	static final String MSG_64G_SLV = "iPhone6Plus 64G Silver";
	static final String MSG_64G_GLD = "iPhone6Plus 64G Gold";
	static final String MSG_128G_SPG = "iPhone6Plus 128G SpaceGrey";
	static final String MSG_128G_SLV = "iPhone6Plus 128G Silver";
	static final String MSG_128G_GLD = "iPhone6Plus 128G Gold";

	private static Properties prop = new Properties();
	private static String propFile = PROP_FILE;

	public static void main(String[] args) {

		/*** initialize ***/
		PropGetter pg = null;
		CommandExecutor ce = new CommandExecutor();
		HTMLAnalyzer htmlAnlyzr = new HTMLAnalyzer();

		HashMap<String,String> pageInfoMap = new HashMap<String,String>();
		ArrayList<String[]> resultInfoList = new ArrayList<String[]>();

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

			/*** judge stock status and generate info mapping ***/
			resultInfoList = generateMapping(pageInfoMap);

			/*** judge and send a mail ***/
			judgeAndSend(resultInfoList);

		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private static ArrayList<String[]> generateMapping(HashMap<String,String> pageInfoMap) throws IOException {

		HashMap<String,int[]> deviceInfoMap = new HashMap<String,int[]>();
		String[] resultInfo = null;
		ArrayList<String[]> resultInfoList = new ArrayList<String[]>();
		GifAnalyzer ga = new GifAnalyzer();

		/** map message-xy **/
		deviceInfoMap.put(MSG_64G_SPG, XY_64G_SPG);
		deviceInfoMap.put(MSG_64G_SLV, XY_64G_SLV);
		deviceInfoMap.put(MSG_64G_GLD, XY_64G_GLD);
		deviceInfoMap.put(MSG_128G_SPG, XY_128G_SPG);
		deviceInfoMap.put(MSG_128G_SLV, XY_128G_SLV);
		deviceInfoMap.put(MSG_128G_GLD, XY_128G_GLD);

		/*
		 * exist: -16757083
		 * not : -1
		 */
		for (String key : pageInfoMap.keySet()) {
			for (String key2 : deviceInfoMap.keySet()) {
				resultInfo = new String[4];
				resultInfo[0] = key; // store name
				resultInfo[1] = key2; // device spec
				resultInfo[2] = pageInfoMap.get(key); // table gif
				try {
					resultInfo[3] = Integer.toString(ga.getRGBcode(pageInfoMap.get(key), deviceInfoMap.get(key2)));  // rgb info on given pict
				} catch (IOException ioe) {
					throw ioe;
				}

				resultInfoList.add(resultInfo);
			}
		}

		return resultInfoList;
	}

	private static void judgeAndSend(ArrayList<String[]> resultInfoList) throws Exception {

		MailSender ml = new MailSender();

		for (int i=0; i<resultInfoList.size(); i++) {
			// no stock
			if ( Integer.valueOf((resultInfoList.get(i))[3]) == -1) {
				//skip

				// stock exists
			} else {
				String uname = (String)prop.get(USER_NAME);
				String pw = (String)prop.get(PASSWORD);

				try {
					ml.sendMail(uname, pw, (String) prop.get(SEND_TO), resultInfoList.get(i)[0]+":"+resultInfoList.get(i)[1], resultInfoList.get(i)[2]);
				} catch (Exception e) {
					throw e;
				}

			}
		}

	}

}
