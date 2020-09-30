package com.danieljames.directory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupListing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_listing);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    updateGroupsListing(idToken);
                } else {
                    Toast.makeText(GroupListing.this, ((FirebaseAuthException)task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
                }
            });
        } else {
            // TODO: sign out
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createGroup:
                createGroup();
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void updateGroupsListing(final String token) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                GlobalConstants.getInstance().baseUrl + "/v1/getGroups",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            updateView(obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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

    private void updateView(JSONObject obj) {
        try {
            String[] groupsList = {};
            ArrayList<String> arrayList = new ArrayList<String>();
            final JSONArray array = obj.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                arrayList.add((String) array.get(i));
            }
            groupsList = arrayList.toArray(new String[0]);
            ListView list = (ListView)findViewById(R.id.list);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.group_listing_row, R.id.group_listing_row_textview, groupsList);
            list.setAdapter(arrayAdapter);
            final String[] finalGroupsList = groupsList;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openGroup(finalGroupsList[position]);
//                    try {
//                        openVisitHouse(array.getJSONObject(position));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    Toast.makeText(getApplicationContext(), "Hello World", Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, Auth.class));
        finish();
    }

    protected void openGroup(String groupId) {
        //
    }

    protected void createGroup() {
        startActivity(new Intent(this, CreateGroup.class));
    }
}
