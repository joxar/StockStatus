package utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class GifAnalyzer {

	public String[] getRGBcode(String pictFile, HashMap<String,int[]> device_xy) throws IOException {

		// xy: deviceSpec-pixelX-pixelY
		String[] rgbInfoArray = new String[2];

		// pictInfoMap: keyName-fileName
		File file = new File(pictFile);
		BufferedImage bi = null;
		int rgbCode = 0;

		try {

			bi = ImageIO.read(file);

			for (String deviceSpec : device_xy.keySet()) {
				rgbCode = bi.getRGB(device_xy.get(deviceSpec)[0], device_xy.get(deviceSpec)[1]);
				rgbInfoArray[0] = deviceSpec; // device spec
				rgbInfoArray[1] = Integer.toString(rgbCode); // rgbCode
			}

		} catch (IOException e) {
			throw e;
		}

		return rgbInfoArray;
	}

}
