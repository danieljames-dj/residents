package com.danieljames.directory.GroupHome;

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

import com.danieljames.directory.R;
import com.danieljames.directory.SubGroup.SubGroupHome;
import com.danieljames.directory.model.GroupModel;
import com.danieljames.directory.model.GroupsData;

import org.json.simple.JSONObject;

public class GroupHome extends AppCompatActivity {

    JSONObject groupDetails;
    int groupPosition;
    GroupModel groupModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home);
        try {
            this.groupPosition = getIntent().getIntExtra("groupPosition", -1);
            this.groupModel = GroupsData.groupsData.groups.get(this.groupPosition);
            setTitle(this.groupModel.groupName);
            this.groupDetails = GroupsData.groupsData.groups.get(groupPosition).groupDetails;
            ListView listView = findViewById(R.id.group_home_list);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, this.groupModel.subGroups.toArray(new String[this.groupModel.subGroups.size()])) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = view.findViewById(android.R.id.text1);
                    text.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.chevron_24dp), null);
                    return view;
                }
            };
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openSubGroup(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openSubGroup(int subgroupPosition) {
        Intent intent;
        intent = new Intent(this, SubGroupHome.class);
        intent.putExtra("subgroupPosition", subgroupPosition);
        intent.putExtra("groupPosition", this.groupPosition);
        startActivity(intent);
    }
}
