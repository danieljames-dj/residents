package com.danieljames.directory.SubGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.danieljames.directory.House.HousePage;
import com.danieljames.directory.R;
import com.danieljames.directory.model.GroupModel;
import com.danieljames.directory.model.GroupsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubGroupHome extends AppCompatActivity {

    JSONArray houses;
    int groupPosition, subgroupPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.groupPosition = getIntent().getIntExtra("groupPosition", -1);
        this.subgroupPosition = getIntent().getIntExtra("subgroupPosition", -1);
        setContentView(R.layout.activity_sub_group_home);
        ListView listView = findViewById(R.id.sub_group_home_list);
        GroupModel group = GroupsData.groupsData.groups.get(this.groupPosition);
        String subGroup = group.subGroups.get(this.subgroupPosition);
        final ArrayList<org.json.simple.JSONObject> houses = group.list.get(subGroup);
        String[] heads = new String[houses.size()];
        for (int i = 0; i < houses.size(); i++) {
            heads[i] = (String) houses.get(i).get("Family Name");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, heads) {
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
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openHouse(houses.get(position));
                //                    openSubGroup(subGroups.get(streets[position]));
//                        openVisitHouse(array.getJSONObject(position));
            }
        });
//        try {
//            this.houses = new JSONArray(getIntent().getStringExtra("houses"));
//            String[] heads = new String[houses.length()];
//            for (int i = 0; i < houses.length(); i++) {
//                JSONObject house = (JSONObject) houses.get(i);
//                heads[i] = (String) house.get("Name");
//            }
////            subGroups.keySet();
////            System.out.println(subGroups);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    void openHouse(org.json.simple.JSONObject house) {
        Intent intent;
        intent = new Intent(this, HousePage.class);
        intent.putExtra("house", house.toString());
        intent.putExtra("groupPosition", this.groupPosition);
        startActivity(intent);
    }
}
