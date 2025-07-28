package snowsan0113.clipscreen;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
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
        VBox main_pane = new VBox();
        Scene scene = new Scene(main_pane, 320, 240);
        stage.setTitle("スクリーンショット");
        stage.setScene(scene);

        //メニュー
        Menu menu_file = new Menu("ファイル");
        MenuItem exit_menu_item = new MenuItem("終了");
        exit_menu_item.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });
        menu_file.getItems().addAll(exit_menu_item);

        Menu menu_help = new Menu("ヘルプ");
        MenuItem ver_menu_item = new MenuItem("バージョン情報");
        menu_help.getItems().addAll(ver_menu_item);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu_file, menu_help);
        main_pane.getChildren().add(menuBar);

        //ボタン
        Button save_button = new Button("保存する");
        save_button.setPrefWidth(scene.getWidth());
        save_button.setPrefHeight(scene.getHeight());
        save_button.setStyle("-fx-background-color: silver;");
        save_button.setOnAction(actionEvent -> {
            if (start_clip != null && end_clip != null) {
                try {
                    ScreenshotManager.save(
                            new Location(start_clip.x(), start_clip.y(), start_clip.z()),
                            new Location(end_clip.x(), end_clip.y(), end_clip.z())
                    );
                    System.out.println("スクリーンショットが保存されました。");
                } catch (AWTException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("スタート地点とエンド地点が設定されていません。");
                stage.setAlwaysOnTop(false);
                error.show();

                error.setOnCloseRequest(e -> stage.setAlwaysOnTop(true));
            }
        });
        save_button.setOnMouseEntered(e -> save_button.setStyle("-fx-background-color: #3399ff;"));
        save_button.setOnMouseExited(e -> save_button.setStyle("-fx-background-color: silver;"));
        main_pane.getChildren().add(save_button);

        stage.show();
        stage.setAlwaysOnTop(true);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        DisplayMode displayMode = env.getDefaultScreenDevice().getDisplayMode();
        int width = displayMode.getWidth();
        int height = displayMode.getHeight();

        Pane screen_pane = new Pane();
        Scene screen_scene = new Scene(screen_pane, width, height);
        Stage screen_stage = new Stage();
        screen_stage.setOpacity(0.2);
        screen_stage.setTitle("クリップしたい場所を選択してください。");
        screen_stage.setScene(screen_scene);
        screen_stage.show();

        screen_scene.setOnMouseClicked(e -> {
            Circle start_circle = null;
            Circle end_circle = null;
            if (start_clip == null && end_clip == null) {
                start_clip = new Location(e.getScreenX(), e.getScreenY(), 0);
                start_circle = new Circle(start_clip.x(), start_clip.y(), 10);
                screen_pane.getChildren().add(start_circle);
                System.out.println("スタートでクリックした場所は" + start_clip.x() + "," + start_clip.y() + "," + start_clip.z());
            }
            else if (start_clip != null && end_clip == null) {
                end_clip = new Location(e.getScreenX(), e.getScreenY() ,0);
                end_circle = new Circle(end_clip.x(), end_clip.y(), 10);
                double sc_x = Math.min(start_clip.x(), end_clip.x());
                double sc_y = Math.min(start_clip.y(), end_clip.y());
                double sc_w = Math.abs(start_clip.x() - end_clip.x());
                double sc_h = Math.abs(start_clip.y() - end_clip.y());
                Rectangle clip_area = new Rectangle((int) sc_x, (int) sc_y, (int) sc_w, (int) sc_h);
                screen_pane.getChildren().add(clip_area);
                screen_pane.getChildren().add(end_circle);
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
