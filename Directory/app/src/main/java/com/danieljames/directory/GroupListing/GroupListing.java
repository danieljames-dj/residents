package com.danieljames.directory.GroupListing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.danieljames.directory.model.GroupModel;
import com.danieljames.directory.model.GroupsData;
import com.danieljames.directory.R;
import com.danieljames.directory.model.SyncHandler;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupListing extends AppCompatActivity implements SyncHandler {

    private static final int AUTH_KEY = 1;
    GroupListingAdapter groupListingAdapter;
    ArrayList<String> groupList = new ArrayList<>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_group_listing);
        groupListingAdapter = new GroupListingAdapter(this);
        if (GroupsData.groupsData == null) {
            GroupsData.groupsData = new GroupsData(this);
        }
        list = findViewById(R.id.list);
        list.setEmptyView(findViewById(R.id.empty_list_item));
        list.setAdapter(groupListingAdapter);
        GroupsData.groupsData.syncHandler = this;
        GroupsData.groupsData.readFile();
        this.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate((GroupsData.groupsData.isLoggedIn() ? R.menu.logged_in_main_menu : R.menu.logged_out_main_menu), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                                .setIsSmartLockEnabled(false, true)
                                .build(),
                        AUTH_KEY);
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                updateLogin(false);
                Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_LONG).show();
                return true;
            case R.id.sync:
                GroupsData.groupsData.sync();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_KEY) {
            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                currentUser.getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(GroupListing.this, "Login Success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(GroupListing.this, (task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                    updateLogin(true);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void synCompleteled() {
        groupList.clear();
        GroupsData.groupsData.groups.clear();
        JSONArray array = GroupsData.groupsData.fileContents;
        for (int i = 0; i < array.size(); i++) {
            JSONObject groupDetails = (JSONObject)array.get(i);
            GroupModel groupModel = new GroupModel();
            groupModel.groupDetails = groupDetails;
            GroupsData.groupsData.groups.add(groupModel);
            groupModel.groupName = (String)groupDetails.get("gname");
            groupModel.summary = (String)groupDetails.get("summary");
            groupModel.inputDetails = new ArrayList<>();
            JSONArray inputDetails = (JSONArray) groupDetails.get("inputDetails");
            if (inputDetails != null) {
                for (int j = 0; j < inputDetails.size(); j++) {
                    String inputDetail = inputDetails.get(j).toString();
                    groupModel.inputDetails.add(inputDetail);
                }
            }
            JSONArray personDetails = (JSONArray) groupDetails.get("personDetails");
            groupModel.personDetails = new ArrayList<>();
            if (personDetails != null) {
                for (int j = 0; j < personDetails.size(); j++) {
                    String personDetail = personDetails.get(j).toString();
                    groupModel.personDetails.add(personDetail);
                }
            }
            JSONArray subGroups = (JSONArray) groupDetails.get("subGroups");
            groupModel.subGroups = new ArrayList<>();
            if (subGroups != null) {
                for (int j = 0; j < subGroups.size(); j++) {
                    String subGroup = subGroups.get(j).toString();
                    groupModel.subGroups.add(subGroup);
                }
            }
            groupList.add((String)groupDetails.get("gname"));
        }
        groupListingAdapter.notifyDataSetChanged();
    }

    private void updateLogin(boolean isLoggedIn) {
        GroupsData.groupsData.updateLogin(isLoggedIn);
        invalidateOptionsMenu();
    }
}
