package com.danieljames.directory;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class GroupsData {

    public static GroupsData groupsData;
    private boolean isLoggedIn;
    private FirebaseAuth mAuth;
    private Context context;
    private String rootPath;
    private String fileName = "directory.json";
    public JSONArray fileContents;
    public SyncHandler syncHandler;

    GroupsData(Context context) {
        this.isLoggedIn = false;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
           isLoggedIn = true;
        }
        this.context = context;
        this.rootPath = context.getExternalFilesDir(null).getAbsolutePath() + "/DRV/";
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void login() {
        isLoggedIn = true;
    }

    public void logout() {
        isLoggedIn = false;
    }

    public void readFile() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader fileReader = new FileReader(rootPath + fileName)) {
            fileContents = (JSONArray) jsonParser.parse(fileReader);
            syncHandler.synCompleteled();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        File file = new File(rootPath + fileName);
//        if (file.exists()) {
//            try {
//                InputStream in = new FileInputStream(file);
//                BufferedReader reader;
//                reader = new BufferedReader(new InputStreamReader(in));
//                String line = reader.readLine();
//                while (line != null) {
//                    readLine(line);
//                    line = reader.readLine();
//                }
//                in.close();
//                setKeyValuePairs();
//            } catch (IOException e) {
//                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private void writeFile() {
        File folder = new File(rootPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File fileObj = new File(rootPath + fileName);
        if (!fileObj.exists()) {
            try {
                fileObj.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileWriter file = new FileWriter(rootPath + fileName)) {
            file.write(fileContents.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sync() {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    updateGroupsListing(idToken);
                } else {
                    Toast.makeText(context, ((FirebaseAuthException)task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
                }
            });
        }
    }

    protected void updateGroupsListing(final String token) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                GlobalConstants.getInstance().baseUrl + "/v1/getGroups",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONParser jsonParser = new JSONParser();
                            JSONObject obj = (JSONObject) jsonParser.parse(response);
                            fileContents = (JSONArray) ((JSONObject)obj.get("data")).get("groups");;//.getJSONObject("data").getJSONArray("groups");
                            syncHandler.synCompleteled();
                            Toast.makeText(context, "Synced", Toast.LENGTH_LONG).show();
                            writeFile();
//                            updateView(obj);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", token);
                return headers;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                String json;
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    try {
                        json = new String(volleyError.networkResponse.data,
                                HttpHeaderParser.parseCharset(volleyError.networkResponse.headers));
                    } catch (UnsupportedEncodingException e) {
                        return new VolleyError(e.getMessage());
                    }
                    return new VolleyError(json);
                }
                return volleyError;
            }
        };
        queue.add(jsonObjectRequest);
    }

}
