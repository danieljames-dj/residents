package com.danieljames.directory;

public class GlobalConstants {

    String baseUrl = "https://directory.drvdatalabs.com/api";

    private static GlobalConstants instance = null;
    protected GlobalConstants() {}
    public static GlobalConstants getInstance() {
        if(instance == null) {
            instance = new GlobalConstants();
        }
        return instance;
    }
}
