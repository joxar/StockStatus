package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropGetter {

	static String propFile = "";

	public PropGetter(String propFile) {
		this.propFile = propFile;
	}

	public Properties getProp() throws IOException {

		Properties prop = null;
		File file = null;
		InputStream is = null;

		try {

			file = new File(this.propFile);
			prop = new Properties();

			is = new FileInputStream(file);
			prop.load(is);

		} catch (IOException ioe) {
			throw ioe;
		}

		return prop;
	}

}
