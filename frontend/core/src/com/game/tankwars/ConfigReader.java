package com.game.tankwars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            FileHandle fileHandle = Gdx.files.internal("config.properties");
            InputStream input = fileHandle.read();
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading configuration file.");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
