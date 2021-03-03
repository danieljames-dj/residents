package com.danieljames.directory.model;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupModel {

    public String groupName;
    public String summary;
    public ArrayList<String> inputDetails, personDetails, subGroups;
    public JSONObject groupDetails;
    public HashMap<String, ArrayList<JSONObject>> list;
}
