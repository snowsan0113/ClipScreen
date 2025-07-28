package snowsan0113.clipscreen.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import snowsan0113.clipscreen.Main;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class SettingManager {

    private File file;
    private Gson gson;

    public SettingManager(SettingFile file_enum) {
        this.file = new File(file_enum.filename);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public String getObjectValue(String key) throws IOException, URISyntaxException {
        JsonObject raw_json = getRawJson();
        String[] keys = key.split("\\."); // 「.」で区切る
        JsonObject now_json = raw_json; //jsonを代入する
        for (int n = 0; n < keys.length - 1; n++) { //keyの1個前未満をループする。（keyが2個だと、1回だけ実行）
            now_json = now_json.getAsJsonObject(keys[n]); // jsonを代入する
        }

        return now_json.get(keys[keys.length - 1]).getAsString(); //key数 - 1（最後のキー）を取得する
    }

    public void saveObjectValue(String key, String value) throws IOException, URISyntaxException {
        JsonObject raw_json = getRawJson();
        String[] keys = key.split("\\."); // 「.」で区切る
        JsonObject now_json = raw_json; //jsonを代入する
        for (int n = 0; n < keys.length - 1; n++) { //keyの1個前未満をループする。（keyが2個だと、1回だけ実行）
            now_json = now_json.getAsJsonObject(keys[n]); // jsonを代入する
        }

        now_json.addProperty(keys[keys.length - 1], value);
        writeFile(raw_json.toString());
    }

    public JsonObject getRawJson() throws IOException, URISyntaxException {
        createJson();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {
            return gson.fromJson(reader, JsonObject.class);
        }
    }

    public void writeFile(String date) {
        try (BufferedWriter write = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
            write.write(date);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createJson() throws IOException, URISyntaxException {
        if (!file.exists()) {
            file.createNewFile();

            try (InputStream in = Main.class.getResourceAsStream("/config.json")) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    JsonObject json = gson.fromJson(reader, JsonObject.class);
                    writeFile(json.toString());
                }
            }

        }
    }

    public enum SettingFile {
        SETTING("config.json");

        private final String filename;

        SettingFile(String filename) {
            this.filename = filename;
        }
    }
}
