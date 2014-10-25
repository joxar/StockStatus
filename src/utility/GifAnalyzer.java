package utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GifAnalyzer {

	public int getRGBcode(String pictFile, int[] xy) throws IOException {

		// xy: deviceSpec-pixelX-pixelY
		int rgbCode = 0;

		// pictInfoMap: keyName-fileName
		File file = new File(pictFile);
		BufferedImage bi = null;

		try {

			bi = ImageIO.read(file);
			rgbCode = bi.getRGB(xy[0], xy[1]);

		} catch (IOException e) {
			throw e;
		}

		return rgbCode;
	}

}
