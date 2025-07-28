package snowsan0113.clipscreen.manager;

import snowsan0113.clipscreen.util.Location;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class ScreenshotManager {

    private static final File save_file;

    static {
        try {
            String path_string = new SettingManager(SettingManager.SettingFile.SETTING).getObjectValue("save_path");
            save_file = new File(path_string + "/screenshot.png");
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(Location start, Location end) throws AWTException, IOException {
        double sc_x = Math.min(start.x(), end.x());
        double sc_y = Math.min(start.y(), end.y());
        double sc_w = Math.abs(start.x() - end.x());
        double sc_h = Math.abs(start.y() - end.y());

        Robot robot = new Robot();
        Rectangle captureArea = new Rectangle((int) sc_x, (int) sc_y, (int) sc_w, (int) sc_h);
        BufferedImage image = robot.createScreenCapture(captureArea);
        ImageIO.write(image, "png", save_file);

        SystemTray tray = SystemTray.getSystemTray();
        Image inv_image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        TrayIcon trayIcon = new TrayIcon(inv_image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);
        trayIcon.setImageAutoSize(true);
        trayIcon.displayMessage("スクリーンショットが保存されました",
                "保存先：" + save_file.toURI().getPath(), TrayIcon.MessageType.INFO);
    }
}
