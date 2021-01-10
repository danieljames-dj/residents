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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubGroupHome extends AppCompatActivity {

    JSONArray houses;
    Map<String, ArrayList<JSONObject>> subGroups = new HashMap<>();
    int groupPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.groupPosition = getIntent().getIntExtra("groupPosition", -1);
        setContentView(R.layout.activity_sub_group_home);
        try {
            this.houses = new JSONArray(getIntent().getStringExtra("houses"));
            String[] heads = new String[houses.length()];
            for (int i = 0; i < houses.length(); i++) {
                JSONObject house = (JSONObject) houses.get(i);
                heads[i] = (String) house.get("Name");
            }
            ListView listView = (ListView)findViewById(R.id.sub_group_home_list);
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
//            subGroups.keySet();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        openHouse((JSONObject) houses.get(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    openSubGroup(subGroups.get(streets[position]));
//                        openVisitHouse(array.getJSONObject(position));
                }
            });
//            System.out.println(subGroups);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void openHouse(JSONObject house) {
        Intent intent;
        intent = new Intent(this, HousePage.class);
        intent.putExtra("house", house.toString());
        intent.putExtra("groupPosition", this.groupPosition);
        startActivity(intent);
    }
}
