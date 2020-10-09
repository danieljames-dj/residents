package com.danieljames.directory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SyncHandler {

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    ArrayAdapter arrayAdapter;
    ArrayList<String> groupList = new ArrayList<>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_group_listing);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String, String> datum = new HashMap<String, String>(2);
        datum.put("First Line", "First line of text");
        datum.put("Second Line","Second line of text Second line of text Second line of text\nSecond line of text\nSecond line of text");
        data.add(datum);
        Map<String, String> datum1 = new HashMap<String, String>(2);
        datum1.put("First Line", "First line of text");
        datum1.put("Second Line","Second line");
        data.add(datum1);
        data.add(datum1);
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"First Line", "Second Line" },
                new int[] {android.R.id.text1, android.R.id.text2 }) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTypeface(null, Typeface.BOLD);
                Object x = getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp);
//                text.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp), null);
                return view;
            }
        };
//        arrayAdapter = new SimpleAdapter(this, R.layout.group_listing_row, new String[] {"First Line", "Second Line"}, new int[] {R.id.text_1, R.id.text_2});
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, groupList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
//                text.setTypeface(null, Typeface.BOLD);
//                Object x = getDrawable(R.drawable.chevron_24dp);
                text.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.chevron_24dp), null);
                return view;
            }
        };
        Boolean flag = false;
        if (GroupsData.groupsData == null) {
            GroupsData.groupsData = new GroupsData(this);
            flag = true;
        }
        list = findViewById(R.id.list);
        list.setEmptyView(findViewById(R.id.empty_list_item));
        list.setAdapter(arrayAdapter);
//        ((TextView)findViewById(android.R.id.text1)).setTypeface(null, Typeface.BOLD);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    openGroup((JSONObject) GroupsData.groupsData.fileContents.get(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        GroupsData.groupsData.syncHandler = this;
        GroupsData.groupsData.readFile();
        this.invalidateOptionsMenu();
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            startActivity(new Intent(this, GroupListing.class));
//        } else {
//            startActivity(new Intent(this, Auth.class));
//        }
//        finish();
//        Log.e("test", String.valueOf(currentUser));
//        System.out.println(currentUser);
//        setContentView(R.layout.activity_main);
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build());
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .setIsSmartLockEnabled(false, true)
//                        .build(),
//                RC_SIGN_IN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (GroupsData.groupsData.isLoggedIn()) {
            getMenuInflater().inflate(R.menu.logged_in_main_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.logged_out_main_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                login();
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                GroupsData.groupsData.logout();
                Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_LONG).show();
                this.invalidateOptionsMenu();
                return true;
            case R.id.sync:
//                synCompleteled();
                GroupsData.groupsData.sync();
//                GroupsData.groupsData.readFile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetMenu() {
        this.invalidateOptionsMenu();
    }

    private void login() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                currentUser.getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        login(idToken, currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, ((FirebaseAuthException)task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                    GroupsData.groupsData.login();
                    resetMenu();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void login(final String token, final String uid, final String name, final String email) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                GlobalConstants.getInstance().baseUrl + "/v1/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //                            JSONObject obj = new JSONObject(response);
//                            if (obj.getString("success").contentEquals("true")) {
//                                openGroupListing();
//                            } else {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
//                            }
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
                params.put("uid", uid);
                params.put("name", name);
                params.put("email", email);
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

    @Override
    public void synCompleteled() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                groupList.add("test");
//                list.setAdapter(arrayAdapter);
//                arrayAdapter.notifyDataSetChanged();
//            }
//        });
        groupList.clear();
        JSONArray array = GroupsData.groupsData.fileContents;
        for (int i = 0; i < array.size(); i++) {
            groupList.add((String)((JSONObject)array.get(i)).get("gname"));
        }
        arrayAdapter.notifyDataSetChanged();
//        final JSONArray finalGroupsList = array;
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    openGroup((JSONObject) finalGroupsList.get(position));
////                        openVisitHouse(array.getJSONObject(position));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    protected void openGroup(JSONObject group) throws JSONException {
//        Toast.makeText(getApplicationContext(), (String)group.get("gid"), Toast.LENGTH_LONG).show();
        Intent intent;
        intent = new Intent(this, GroupHome.class);
        intent.putExtra("groupDetails", group.toString());
        startActivity(intent);
    }
}
