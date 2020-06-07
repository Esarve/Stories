package com.sourav.story.Activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.MergeAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import io.realm.RealmResults;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements OnRVClickListner, OnBottomSheetClickListner, View.OnClickListener {
    private static final String TAG = "Main Activity" ;
    private CoordinatorLayout parent;
    private List<StoryData> story = new ArrayList<>();
    private FloatingActionButton fab;
    private MenuItem nightmode;
    private Tools tools = Tools.getInstance();
    private Intent intent;
    private RealmEngine realmEngine;
    private RecyclerView recyclerView;
    private RealmResults<StoryData> realmResults;
    private NewAdapter newAdapter;
    private HeaderAdapter headerAdapter;

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
        realmResults = realmEngine.getResults();

        realmResults.addChangeListener(storyData -> {
            initData();
            newAdapter.notifyDataSetChanged();
            headerAdapter.notifyDataSetChanged();
        });
    }

    private void initView() {
        parent = findViewById(R.id.parent_view);
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

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        newAdapter = new NewAdapter(this, story);
        headerAdapter = new HeaderAdapter("Hi Sourav", realmResults.size(), this);
        MergeAdapter mergeAdapter = new MergeAdapter(headerAdapter, newAdapter);
        recyclerView.setAdapter(mergeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //SWIPE
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                performDelete(viewHolder.getLayoutPosition() - 1);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addActionIcon(R.drawable.ic_delete_white_24dp)
                        .setActionIconTint(R.color.colorAccent)
                        .addSwipeRightLabel("delete")
                        .addSwipeLeftLabel("delete")
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            //This method will make the header card unswappable
            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof HeaderAdapter.ViewHolder) return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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

    //Deletes an item and shows toast
    private void performDelete(int pos) {
        long timestamp = story.get(pos).getTimestamp();
        StoryData deletecStory = realmEngine.getSpecificData(timestamp);
        realmEngine.deleteData(timestamp);
        recyclerView.removeViewAt(pos);
        showSnack(deletecStory);
    }

    private void showSnack(StoryData deletedStory) {
        Snackbar snackbar = Snackbar
                .make(parent, "Deleted Successfully!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", view -> {
                    realmEngine.addSpecificStory(deletedStory);
                    Toast.makeText(this, "UNDO huhuhahah", Toast.LENGTH_SHORT).show();
                });

        snackbar.show();
    }

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
        openEditor(pos);
    }
}
