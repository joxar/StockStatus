package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLAnalyzer {

	public ArrayList<String> getResourceURL(ArrayList<String> fileList) throws IOException {

		// keyword-start: <div class="Img01A">
		// keyword-end: </div>
		String kwd_start = "<div class=\"Img01A\">";
		String kwd_end = "</div>";
		File inFile = null;

		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<String> result = null;
		//String regex = "<div class=\"Img01A\"><img src=";
		String regex_start = "(.*)<div class=\"Img01A\"><img src=\"";
		String regex_end = "\"/></div>(.*)";
		String tmpStr = "";
		String line = "";

		result = new ArrayList<String>();

		try {

			for (int i=0; i<fileList.size(); i++) {

				// open given HTML file
				inFile = new File(fileList.get(i));
				fr = new FileReader(inFile);
				br = new BufferedReader(fr);

				while ( (line = br.readLine()) != null) {

					if (Pattern.compile(kwd_start).matcher(line).find()) {

						tmpStr = replaceMatchString(regex_start, line);
						tmpStr = replaceMatchString(regex_end, tmpStr);

						result.add(tmpStr);

					}
				}
			}

			return result;

		} catch (IOException e) {
			throw e;
		}
	}


	private static String replaceMatchString(String regex, String target) {

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);

		if (matcher.find()) {
			return matcher.replaceFirst("");
		} else {
			throw new IllegalStateException("No match found.");
		}

	}
}
