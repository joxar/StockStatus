package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLAnalyzer {

	/*** constants ***/
	private static String KEYWORD = "<div class=\"Img01A\">";
	private static String REGEX_START = "(.*)<div class=\"Img01A\"><img src=\"";
	private static String REGEX_END = "\"/></div>(.*)";


	public HashMap<String,String> getResourceURL(HashMap<String,String> pageInfoMap) throws IOException, IllegalStateException {

		File inFile = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {

			for (String key : pageInfoMap.keySet()) {

				// read given HTML file
				inFile = new File(pageInfoMap.get(key));
				fr = new FileReader(inFile);
				br = new BufferedReader(fr);

				String line = "";
				String tmpStr = "";
				while ( (line = br.readLine()) != null) {

					if (Pattern.compile(KEYWORD).matcher(line).find()) {
						tmpStr = replaceMatchString(REGEX_START, line);
						tmpStr = replaceMatchString(REGEX_END, tmpStr);

						pageInfoMap.put(key, tmpStr);
					}
				}

				br.close();
				fr.close();
			}

			return pageInfoMap;

		} catch (IOException e) {
			throw e;
		}
	}


	private static String replaceMatchString(String regex, String target) throws IllegalStateException {

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);

		if (matcher.find()) {
			return matcher.replaceFirst("");
		} else {
			throw new IllegalStateException("No match found.");
		}

	}
}
