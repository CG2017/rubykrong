import org.apache.commons.imaging.ImageReadException;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        ImageProcessor imageProcessor = new ImageProcessor();
        try {
            imageProcessor.processImages();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageReadException e) {
            e.printStackTrace();
        }
    }
}
