package utility;

import java.io.IOException;
import java.util.ArrayList;

public class CommandExecutor {

	static final String DNAME_TEMPLATE = "output/";
	static final String FNAME_TEMPLATE = "_test";

	public ArrayList<String> execWget(ArrayList<String> targetURLs, String ftype) throws IOException, InterruptedException {

		String[] command = new String[4];
		ArrayList<String> outputList = new ArrayList<String>();
		String output = "";

		Runtime rt = Runtime.getRuntime();
		Process ps = null;

		try {

			for (int i=0; i<targetURLs.size(); i++) {
				output = DNAME_TEMPLATE + i + FNAME_TEMPLATE + "." + ftype;

				// set command
				command[0] = "/usr/local/bin/wget";
				command[1] = targetURLs.get(i);
				command[2] = "-O";
				command[3] = output;

				// execute
				ps = rt.exec(command);
				ps.waitFor();

				outputList.add(output);
			}

			return outputList;

		} catch (InterruptedException ie) {
			throw ie;
		} catch(IOException ioe) {
			throw ioe;
		}

	}
}
