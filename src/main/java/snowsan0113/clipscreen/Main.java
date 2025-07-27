package snowsan0113.clipscreen;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import snowsan0113.clipscreen.manager.ScreenshotManager;
import snowsan0113.clipscreen.util.Location;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {

    private Location start_clip = null;
    private Location end_clip = null;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        HBox main_pane = new HBox();
        Scene scene = new Scene(main_pane, 320, 240);
        stage.setTitle("スクリーンショット");
        stage.setScene(scene);
        stage.show();
        stage.setAlwaysOnTop(true);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        DisplayMode displayMode = env.getDefaultScreenDevice().getDisplayMode();
        int width = displayMode.getWidth();
        int height = displayMode.getHeight();

        HBox screen_pane = new HBox();
        Scene screen_scene = new Scene(screen_pane, width, height);
        Stage screen_stage = new Stage();
        screen_stage.setOpacity(0.2);
        screen_stage.setTitle("クリップしたい場所を選択してください。");
        screen_stage.setScene(screen_scene);
        screen_stage.show();

        screen_scene.setOnMouseClicked(e -> {
            if (start_clip == null && end_clip == null) {
                start_clip = new Location(e.getScreenX(), e.getScreenY(), 0);
                System.out.println("スタートでクリックした場所は" + start_clip.x() + "," + start_clip.y() + "," + start_clip.z());
            }
            else if (start_clip != null && end_clip == null) {
                end_clip = new Location(e.getScreenX(), e.getScreenY() ,0);
                System.out.println("エンドでクリックした場所は" + end_clip.x() + "," + end_clip.y() + "," + end_clip.z());
            }
            else if (start_clip != null && end_clip != null) {
                try {
                    ScreenshotManager.save(
                            new Location(start_clip.x(), start_clip.y(), start_clip.z()),
                            new Location(end_clip.x(), end_clip.y(), end_clip.z())
                    );
                    System.out.println("スクリーンショットが保存されました。");
                    System.out.println("//スタートでクリックした場所は" + start_clip.x() + "," + start_clip.y() + "," + start_clip.z());
                    System.out.println("//エンドでクリックした場所は" + end_clip.x() + "," + end_clip.y() + "," + end_clip.z());
                    resetLocation();
                } catch (AWTException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void resetLocation() {
        start_clip = null;
        end_clip = null;
    }
}
