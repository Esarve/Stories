package com.sourav.story.Activities;

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
import com.sourav.story.Adapters.HeaderAdapter;
import com.sourav.story.Adapters.NewAdapter;
import com.sourav.story.Interfaces.OnBottomSheetClickListner;
import com.sourav.story.Interfaces.OnRVClickListner;
import com.sourav.story.OtherKindsOfViews.BottomSheetViewer;
import com.sourav.story.R;
import com.sourav.story.Stuffs.RealmEngine;
import com.sourav.story.Stuffs.StoryData;
import com.sourav.story.Stuffs.Tools;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements OnRVClickListner, OnBottomSheetClickListner, View.OnClickListener {
    private static final String TAG = "Main Activity" ;
    private List<StoryData> story = new ArrayList<>();
    private FloatingActionButton fab;
    private MenuItem nightmode;
    private Tools tools = Tools.getInstance();
    private Intent intent;
    private RealmEngine realmEngine;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRealm();
        initToolbar();
        initView();
        initData();
        //getData();
        initRecyclerView();
    }

    private void initData() {
        story = realmEngine.getResults();
    }

    private void initRealm() {
        Realm.init(this);
        realmEngine = RealmEngine.getInstance();
        realmEngine.initRealm();
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        tools.setSystemBarColor(this, R.color.grey_5);
        tools.setSystemBarLight(this);
        tools.setNavigationBarColor(getWindow().getDecorView(),this,R.color.grey_3,true);

        intent = new Intent(MainActivity.this,WriteActivity.class);
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
            story.add(new StoryData("9:30 PM","3 July",getResources().getString(R.string.lorem)));
            story.add(new StoryData("7:25 PM","8 March","IDK what happened today"));
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
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
        recyclerView.invalidate();
    }

    //Init appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_80));

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem nightmode = menu.findItem(R.id.nightSwitch);
        nightmode.setOnMenuItemClickListener(item -> {
            Toast.makeText(getApplicationContext(),"NIGHT MODE: WIP", Toast.LENGTH_SHORT).show();
            return false;
        });
        //SearchView searchView = (SearchView) searchItem.getActionView();
        return true;
    }

    private Bundle bundleData(int i){

        String date = story.get(i).getDate();
        String time = story.get(i).getTime();
        String body = story.get(i).getBody();

        Bundle args = new Bundle();
        args.putString(Tools.BODY,body);
        args.putString(Tools.DATE,date);
        args.putString(Tools.TIME,time);
        args.putInt(Tools.POSITION,i);

        return args;
    }

    public void openEditor(int pos){
        intent.putExtras(bundleData(pos));
        openEditor();
    }

    public void openEditor(){
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    //OnClick Listeners
    @Override
    public void onClick(int pos) {
        // Create Bottom Sheet
        BottomSheetViewer bottomSheetViewer = new BottomSheetViewer();
        bottomSheetViewer.setArguments(bundleData(pos));
        bottomSheetViewer.show(getSupportFragmentManager(), "viewer");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == fab.getId()){
            openEditor();
        }
    }

    @Override
    public void onBottomSheetButtonClick(View view, int pos) {
        //Toast.makeText(this,"BottomSheet Button Clicked", Toast.LENGTH_SHORT).show();
        openEditor(pos);
    }


}
