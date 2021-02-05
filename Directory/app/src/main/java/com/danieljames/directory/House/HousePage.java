package com.danieljames.directory.House;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.danieljames.directory.R;
import com.danieljames.directory.model.GroupsData;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;

public class HousePage extends AppCompatActivity {

    JSONObject house;
    int groupPosition;
    org.json.simple.JSONObject groupDetails;

    private void setSampleImage() {
//        ImageView imageView = findViewById(R.id.houseImage);
//        String data1
//        }
//        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_page);
        this.groupPosition = getIntent().getIntExtra("groupPosition", -1);
//        this.groupDetails = GroupsData.groupsData.groups.get(groupPosition).groupDetails;
//        org.json.simple.JSONArray list = (JSONArray) groupDetails.get("list");
//        for (int i = 0; i < list.size(); i++) {
//            org.json.simple.JSONObject house = (org.json.simple.JSONObject) list.get(i);
//            String subGroup = (String) house.get("subGroup");
//            ArrayList<org.json.simple.JSONObject> subGroupHouses = subGroups.get(subGroup);
//            if (subGroupHouses == null) {
//                ArrayList<org.json.simple.JSONObject> houseList = new ArrayList<>();
//                houseList.add(house);
//                subGroups.put(subGroup, houseList);
//            } else {
//                subGroupHouses.add(house);
//            }
//        }
        TableLayout tableLayout = findViewById(R.id.house_table);
        setSampleImage();
        try {
            this.house = new JSONObject(getIntent().getStringExtra("house"));
            ArrayList<String> fields = GroupsData.groupsData.groups.get(this.groupPosition).inputDetails;
            for (int i = 0; i < fields.size(); i++) {
                String key = fields.get(i);
                if (house.has(key)) {
                    TableRow tableRow = new TableRow(this);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tableRow.setGravity(Gravity.CENTER);
                    TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(0, 20, 0, 20);
                    tableRow.setLayoutParams(layoutParams);
                    String value = (String) house.get(key);
                    TextView keyView = new TextView(this);
                    keyView.setText(key);
                    keyView.setAllCaps(true);
                    keyView.setTextAppearance(android.R.style.TextAppearance_Medium);
                    tableRow.addView(keyView);
                    TextView valueView = new TextView(this);
                    valueView.setText(value);
                    valueView.setEms(10);
                    tableRow.addView(valueView);
                    tableLayout.addView(tableRow);
                }
            }
            TableLayout linearLayout = findViewById(R.id.house_members);
            JSONArray persons = (JSONArray) this.house.get("persons");
            for (int i = 0; i < persons.length(); i++) {
                TableLayout personTable = new TableLayout(this);
                personTable.setStretchAllColumns(true);
                personTable.setPadding(16, 16, 16, 16);
                for (int j = 0; j < GroupsData.groupsData.groups.get(this.groupPosition).personDetails.size(); j++) {
                    TableRow tableRow = new TableRow(this);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tableRow.setGravity(Gravity.CENTER);
                    TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(0, 20, 0, 20);
                    tableRow.setLayoutParams(layoutParams);
                    String key = GroupsData.groupsData.groups.get(this.groupPosition).personDetails.get(j);
                    String value;
                    if (((JSONObject) persons.get(i)).has(key)) {
                        value = (String) ((JSONObject) persons.get(i)).get(key);
                    } else {
                        value = "";
                    }
//                    String value = (String) ((JSONObject) persons.get(i)).get(key);
                    TextView keyView = new TextView(this);
                    keyView.setText(key);
                    keyView.setAllCaps(true);
                    keyView.setTextAppearance(android.R.style.TextAppearance_Medium);
                    tableRow.addView(keyView);
                    TextView valueView = new TextView(this);
                    valueView.setText(value);
                    valueView.setEms(10);
                    tableRow.addView(valueView);
                    personTable.addView(tableRow);
                }
                linearLayout.addView(personTable);
            }
//            for (Iterator<String> it = house.keys(); it.hasNext(); ) {
//                TableRow tableRow = new TableRow(this);
//                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//                tableRow.setGravity(Gravity.CENTER);
//                TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                layoutParams.setMargins(0, 20, 0, 20);
//                tableRow.setLayoutParams(layoutParams);
//                String key = it.next();
////                System.out.println(key);
////                System.out.println(value);
//                if (!key.contentEquals("id") && !key.contentEquals("persons")) {
//                    String value = (String) house.get(key);
//                    Map<String, String> datum = new HashMap<String, String>(2);
//                    datum.put("First Line", key.toUpperCase());
//                    datum.put("Second Line", value);
//                    data.add(datum);
//                    Map<String, String> datum1 = new HashMap<String, String>(2);
//                    datum1.put("First Line", key.toUpperCase());
//                    datum1.put("Second Line", value);
//                    data.add(datum1);
//                    TextView keyView = new TextView(this);
//                    keyView.setText(key);
//                    keyView.setAllCaps(true);
//                    keyView.setTextAppearance(android.R.style.TextAppearance_Medium);
////                    keyView.setTypeface(null, Typeface.BOLD);
////                    keyView.setTextSize(20);
//                    tableRow.addView(keyView);
////                    TextView keyView1 = new TextView(this);
////                    keyView1.setText(key);
////                    keyView1.setTypeface(null, Typeface.BOLD);
////                    keyView1.setTextSize(20);
////                    linearLayout.addView(keyView1);
////                    TextView keyView2 = new TextView(this);
////                    keyView2.setText(key);
////                    keyView2.setTypeface(null, Typeface.BOLD);
////                    keyView2.setTextSize(20);
////                    linearLayout.addView(keyView2);
//                    TextView valueView = new TextView(this);
//                    valueView.setText(value);
//                    valueView.setEms(10);
////                    valueView.setTextSize(20);
////                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
////                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////                    params.setMargins(5, 5, 5, 20);
//                    tableRow.addView(valueView);
////                    Space space = new Space(this);
////                    space.setMinimumHeight(20);
////                    linearLayout.addView(space);
//                    tableLayout.addView(tableRow);
//                }
//            }
//            System.out.println(subGroups);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        SimpleAdapter adapter = new SimpleAdapter(this, data,
//                android.R.layout.simple_list_item_2,
//                new String[] {"First Line", "Second Line" },
//                new int[] {android.R.id.text1, android.R.id.text2 }) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text = view.findViewById(android.R.id.text1);
//
////                text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//                text.setTextSize(16);
//                TextView text_2 = view.findViewById(android.R.id.text2);
//                text_2.setTypeface(null, Typeface.BOLD);
////                text_2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//                text_2.setTextSize(24);
////                Object x = getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp);
////                text.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp), null);
//                return view;
//            }
//
//            @Override
//            public boolean isEnabled(int position) {
//                return false;
//            }
//        };
////        ListView listView = findViewById(R.id.house_page_list);
////        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }
}
