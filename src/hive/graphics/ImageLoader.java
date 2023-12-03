package hive.graphics;

import hive.GraphicLogger;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    private ImageLoader(){}

    public static Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            GraphicLogger.getLogger().error("ImageLoader: error while trying to load image from path: {}", path);
            return null;
        }
    }
}
