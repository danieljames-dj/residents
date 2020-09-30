package com.danieljames.directory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CreateGroup extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Button createGroupButton = findViewById(R.id.createGroupButton);
        createGroupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createGroupButton:
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    currentUser.getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                createGroup(idToken);
                            } else {
                                Toast.makeText(CreateGroup.this, ((FirebaseAuthException)task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Fetching token failed", Toast.LENGTH_SHORT).show();
                }
        }
    }

    protected void createGroup(final String token) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                GlobalConstants.getInstance().baseUrl + "/v1/createGroup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("success").contentEquals("true")) {
                                groupCreated();
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("gname", ((EditText)findViewById(R.id.group_name_input)).getText().toString());
                return params;
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

    protected void groupCreated() {
        Toast.makeText(getApplicationContext(), "Successfully created the group", Toast.LENGTH_LONG).show();
        finish();
    }
}
