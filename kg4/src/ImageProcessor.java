

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;

import java.io.File;
import java.io.IOException;


public class ImageProcessor {

    public void processImages() throws IOException, ImageReadException {
        File directory = new File("images/");
        File[] files = directory.listFiles();

        for(File file : files != null ? files : new File[0]) {
            ImageInfo imageInfo = Imaging.getImageInfo(file);
            System.out.println(String.format(" File: %s:\n type: %s\n size: %sx%s\n dpi: %dx%d\n compression: %s\n" +
                            "-------------",
                    file.getName(), imageInfo.getFormatName(), imageInfo.getWidth(), imageInfo.getHeight(),
                    imageInfo.getPhysicalHeightDpi(), imageInfo.getPhysicalWidthDpi(),
                    imageInfo.getCompressionAlgorithm()));
        }
    }
}
