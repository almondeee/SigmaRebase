package com.mentalfrostbyte.jello.util.system;

import com.google.gson.*;
import com.mentalfrostbyte.Client;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static boolean fresh = false;

    public static void save(JsonObject var0, File var1) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(var0);

        try (FileOutputStream var4 = new FileOutputStream(var1)) {
            IOUtils.write(json, var4, "UTF-8");
        }
    }

    public static JsonObject readJsonFile(File file) throws IOException {
        JsonObject var3 = new JsonObject();

        if (file.exists()) {
            try (FileInputStream var4 = new FileInputStream(file)) {
                String var5 = IOUtils.toString(var4, StandardCharsets.UTF_8);

                if (var5 != null && !var5.isEmpty()) {
                    try {
                        var3 = JsonParser.parseString(var5).getAsJsonObject();
                    } catch (JsonParseException var7) {
                        Client.LOGGER.warn("Error when reading json from config. Continuing, but no preferences will be loaded.", var7);
                    }
                } else {
                    Client.LOGGER.warn("Empty config file");
                }
            }
        } else {
            Client.LOGGER.info("Config does not exist... creating new config file...");
            fresh = true;
            file.createNewFile();
        }

        return var3;
    }

    public static File getFileFromDialog() {
        FileDialog fileDialog = new FileDialog((Frame) null, "Select a .txt file");
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setFile("*.txt");

        fileDialog.setVisible(true);
        fileDialog.setAlwaysOnTop(true);

        String fileName = fileDialog.getFile();
        if (fileName == null) {
            return null;
        }

        return new File(fileDialog.getDirectory(), fileName);
    }
}