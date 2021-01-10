package com.danieljames.directory;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupHome extends AppCompatActivity {

    JSONObject groupDetails;
    int groupPosition;
    Map<String, ArrayList<JSONObject>> subGroups = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home);
        try {
            this.groupPosition = getIntent().getIntExtra("groupPosition", -1);
            this.groupDetails = GroupsData.groupsData.groups.get(groupPosition).groupDetails;
            JSONArray list = (JSONArray) groupDetails.get("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject house = (JSONObject) list.get(i);
                String subGroup = (String) house.get("subGroup");
                ArrayList<JSONObject> subGroupHouses = subGroups.get(subGroup);
                if (subGroupHouses == null) {
                    ArrayList<JSONObject> houseList = new ArrayList<>();
                    houseList.add(house);
                    subGroups.put(subGroup, houseList);
                } else {
                    subGroupHouses.add(house);
                }
            }
            ListView listView = (ListView)findViewById(R.id.group_home_list);
            final String[] streets = new String[subGroups.keySet().size()];
            int i = 0;
            for (String street: subGroups.keySet()) {
                streets[i] = street;
                i += 1;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, streets) {
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
//            subGroups.keySet();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openSubGroup(subGroups.get(streets[position]));
//                        openVisitHouse(array.getJSONObject(position));
                }
            });
            System.out.println(subGroups);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openSubGroup(ArrayList<JSONObject> subGroup) {
        JSONArray houses = new JSONArray();
        for (int i = 0; i < subGroup.size(); i++) {
            houses.add(subGroup.get(i));
        }
        Intent intent;
        intent = new Intent(this, SubGroupHome.class);
        intent.putExtra("houses", houses.toString());
        intent.putExtra("groupPosition", this.groupPosition);
        startActivity(intent);
    }
}
