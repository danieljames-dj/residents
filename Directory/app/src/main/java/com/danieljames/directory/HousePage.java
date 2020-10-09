package com.danieljames.directory;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Space;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HousePage extends AppCompatActivity {

    JSONObject house;

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
        LinearLayout linearLayout = findViewById(R.id.housePage);
        setSampleImage();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            this.house = new JSONObject(getIntent().getStringExtra("house"));
            for (Iterator<String> it = house.keys(); it.hasNext(); ) {
                String key = it.next();
                String value = (String) house.get(key);
//                System.out.println(key);
//                System.out.println(value);
                if (!key.contentEquals("id")) {
                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("First Line", key.toUpperCase());
                    datum.put("Second Line", value);
                    data.add(datum);
                    Map<String, String> datum1 = new HashMap<String, String>(2);
                    datum1.put("First Line", key.toUpperCase());
                    datum1.put("Second Line", value);
                    data.add(datum1);
//                    TextView keyView = new TextView(this);
//                    keyView.setText(key);
//                    keyView.setTypeface(null, Typeface.BOLD);
//                    keyView.setTextSize(20);
//                    linearLayout.addView(keyView);
//                    TextView keyView1 = new TextView(this);
//                    keyView1.setText(key);
//                    keyView1.setTypeface(null, Typeface.BOLD);
//                    keyView1.setTextSize(20);
//                    linearLayout.addView(keyView1);
//                    TextView keyView2 = new TextView(this);
//                    keyView2.setText(key);
//                    keyView2.setTypeface(null, Typeface.BOLD);
//                    keyView2.setTextSize(20);
//                    linearLayout.addView(keyView2);
//                    TextView valueView = new TextView(this);
//                    valueView.setText(value);
//                    valueView.setTextSize(20);
////                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
////                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////                    params.setMargins(5, 5, 5, 20);
//                    linearLayout.addView(valueView);
//                    Space space = new Space(this);
//                    space.setMinimumHeight(20);
//                    linearLayout.addView(space);
                }
            }
//            System.out.println(subGroups);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"First Line", "Second Line" },
                new int[] {android.R.id.text1, android.R.id.text2 }) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);

//                text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                text.setTextSize(16);
                TextView text_2 = view.findViewById(android.R.id.text2);
                text_2.setTypeface(null, Typeface.BOLD);
//                text_2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                text_2.setTextSize(24);
//                Object x = getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp);
//                text.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp), null);
                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }
        };
        ListView listView = findViewById(R.id.house_page_list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
