package com.sourav.story;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRVClickListner{
    private List<StoryData> story = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
    }

    private void getData() {
        story.add(new StoryData("1", "1","1"));
        story.add(new StoryData("2","2","2"));
        story.add(new StoryData("3","3","3"));
        story.add(new StoryData("4","4","4"));
        initRview();
    }

    private void initRview() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        NewAdapter newAdapter = new NewAdapter(this, story);
        recyclerView.setAdapter(newAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemViewCacheSize(0);
        newAdapter.setOnClick(MainActivity.this);

    }

    @Override
    public void onClick(int pos) {
        Toast.makeText(this, "Clicked at pos "+ pos ,Toast.LENGTH_SHORT).show();
    }
}
