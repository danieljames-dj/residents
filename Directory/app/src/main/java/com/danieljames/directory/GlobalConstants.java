package com.danieljames.directory;

public class GlobalConstants {

    String baseUrl = "http://10.0.2.2:3000/api";

    private static GlobalConstants instance = null;
    protected GlobalConstants() {}
    public static GlobalConstants getInstance() {
        if(instance == null) {
            instance = new GlobalConstants();
        }
        return instance;
    }
}
