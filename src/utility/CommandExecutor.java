package utility;

import java.io.IOException;
import java.util.HashMap;


public class CommandExecutor {

	static final String DNAME_TEMPLATE = "output/";
	static final String SPACE = "_";

	/*** get HTML files by given URLs ***/
	public HashMap<String,String> execWget(HashMap<String,String> targetInfoMap, String ftype) throws IOException, InterruptedException {

		String[] command = new String[4];
		String output = "";

		Runtime rt = Runtime.getRuntime();
		Process ps = null;

		try {

			int idx = 0;
			for (String key : targetInfoMap.keySet()) {

				output = DNAME_TEMPLATE + idx + "_" + key + "." + ftype;

				// set command
				command[0] = "/usr/local/bin/wget";
				command[1] = targetInfoMap.get(key);
				command[2] = "-O";
				command[3] = output;

				// execute command
				ps = rt.exec(command);
				ps.waitFor();

				targetInfoMap.put(key, output); //output: name of downloaded file
				idx++;
			}

		} catch (InterruptedException ie) {
			throw ie;
		} catch(IOException ioe) {
			throw ioe;
		}

		return targetInfoMap;
	}
}
