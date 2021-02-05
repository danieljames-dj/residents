package com.danieljames.directory.model;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class GroupModel {

    public String groupName;
    public String summary;
    public ArrayList<String> inputDetails, personDetails, subGroups;
    public JSONObject groupDetails;
}
