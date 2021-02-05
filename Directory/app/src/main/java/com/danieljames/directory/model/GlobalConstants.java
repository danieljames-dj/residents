package com.danieljames.directory.model;

public class GlobalConstants {

    public String baseUrl = "https://directory.drvdatalabs.com/api";

    private static GlobalConstants instance = null;
    protected GlobalConstants() {}
    public static GlobalConstants getInstance() {
        if(instance == null) {
            instance = new GlobalConstants();
        }
        return instance;
    }
}
