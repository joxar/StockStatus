package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class PropGetter {

	private String propFile = "";

	/*** set properties file to load ***/
	public PropGetter(String propFile) {
		this.propFile = propFile;
	}


	public Properties getProps() throws IOException {

		/*** initialize ***/
		File file = null;
		Properties prop = null;
		InputStream is = null;

		try {

			/*** load properties ***/
			file = new File(this.propFile);
			prop = new Properties();
			is = new FileInputStream(file);
			prop.load(is);

		} catch (IOException ioe) {
			throw ioe;
		}

		return prop;
	}

	public HashMap<String,String> getMappingData(String[] dataStr, String separator) {

		String[] tmpArray = new String[2];
		HashMap<String,String> resultMap = new HashMap<String,String>();

		for (int i=0; i<dataStr.length; i++) {
			tmpArray = dataStr[i].split(separator);
			resultMap.put(tmpArray[0], tmpArray[1]);  //[0]store name, [1]URL
		}

		return resultMap;
	}

}
