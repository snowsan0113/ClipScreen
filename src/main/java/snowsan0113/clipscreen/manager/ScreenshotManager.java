package snowsan0113.clipscreen.manager;

import snowsan0113.clipscreen.util.Location;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenshotManager {

    private static final File save_file = new File("screenshot.png");

    public static void save(Location start, Location end) throws AWTException, IOException {
        double sc_x = Math.min(start.x(), end.x());
        double sc_y = Math.min(start.y(), end.y());
        double sc_w = Math.abs(start.x() - end.x());
        double sc_h = Math.abs(start.y() - end.y());

        Robot robot = new Robot();
        Rectangle captureArea = new Rectangle((int) sc_x, (int) sc_y, (int) sc_w, (int) sc_h);
        BufferedImage image = robot.createScreenCapture(captureArea);
        ImageIO.write(image, "png", save_file);
    }
}
