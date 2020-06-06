package com.sourav.story;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.MergeAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRVClickListner, View.OnClickListener {
    private static final String TAG = "Main Activity" ;
    private List<StoryData> story = new ArrayList<>();
    private FloatingActionButton fab;
    private MenuItem nightmode;
    private Tools tools = Tools.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
        getData();
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        tools.setSystemBarColor(this, R.color.grey_5);
        tools.setSystemBarLight(this);
        tools.setNavigationBarColor(getWindow().getDecorView(),this,R.color.grey_3,true);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                getResources().getColor(R.color.grey_80),
                BlendModeCompat.SRC_ATOP));
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_80));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getData() {
        for (int i=0; i<10; i++){
            story.add(new StoryData("10:20 PM", "20 January","This is a demo text"));
            story.add(new StoryData("6:30 AM","5 February","This is something i've written"));
            story.add(new StoryData("9:30 PM","3 July","Good Night!"));
            story.add(new StoryData("7:25 PM","8 March","IDK what happened today"));
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        NewAdapter newAdapter = new NewAdapter(this, story);
        HeaderAdapter headerAdapter = new HeaderAdapter(null,null, this);
        MergeAdapter mergeAdapter = new MergeAdapter(headerAdapter, newAdapter);
        recyclerView.setAdapter(mergeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy >0) {
                    // Scroll Down
                    if (fab.isShown()) {
                        fab.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fab.isShown()) {
                        fab.show();
                    }
                }
            }
        });
        newAdapter.setOnClick(MainActivity.this);
    }

    //Init appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_80));

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem nightmode = menu.findItem(R.id.nightSwitch);
        nightmode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(),"NIGHT MODE: WIP", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //SearchView searchView = (SearchView) searchItem.getActionView();
        return true;
    }


    //OnClick Listeners
    @Override
    public void onClick(int pos) {
        Toast.makeText(this, "Clicked at pos "+ pos ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == fab.getId()){
            Toast.makeText(this,"FAB CLICKED", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, WriteActivity.class));
        }
    }
}
