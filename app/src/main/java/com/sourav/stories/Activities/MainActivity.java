package com.sourav.stories.Activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.MergeAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sourav.stories.Adapters.HeaderAdapter;
import com.sourav.stories.Adapters.NewAdapter;
import com.sourav.stories.Interfaces.OnBottomSheetClickListner;
import com.sourav.stories.Interfaces.OnRVClickListner;
import com.sourav.stories.OtherKindsOfViews.BottomSheetViewer;
import com.sourav.stories.R;
import com.sourav.stories.Stuffs.RealmEngine;
import com.sourav.stories.Stuffs.StoryData;
import com.sourav.stories.Stuffs.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements OnRVClickListner, OnBottomSheetClickListner, View.OnClickListener {
    private static final String TAG = "MainActivity" ;
    private CoordinatorLayout parent;
    private List<StoryData> story = new ArrayList<>();
    private ExtendedFloatingActionButton efab;
    private MenuItem nightmode;
    private Tools tools = Tools.getInstance();
    private RealmEngine realmEngine;
    private RecyclerView recyclerView;
    private RealmResults<StoryData> realmResults;
    private NewAdapter newAdapter;
    private HeaderAdapter headerAdapter;
    private SearchView searchView;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (tools.getFromSharedPref(this, Tools.PREFTYPE_NAME, Tools.USERNAME) == null){
            openSetupWizard();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRealm();
        initSystemUI();
        initView();
        initData();
        initRecyclerView();
    }

    private void openSetupWizard() {
        startActivity(new Intent(MainActivity.this, WizardActivity.class));
    }

    private void initData() {
        story = realmEngine.getSearchResults();
    }

    private void initData(String query) {
        story = realmEngine.getSearchResults(query);
        refresh();
    }

    private void refresh() {
        initRecyclerView();
    }


    private void initRealm() {
        Realm.init(this);
        realmEngine = RealmEngine.getInstance();
        realmEngine.initRealm();
        realmResults = realmEngine.getSearchResults(); //todo: FIX THIS

        realmResults.addChangeListener(storyData -> {
            initData();
            newAdapter.notifyDataSetChanged();
            headerAdapter.notifyDataSetChanged();
        });
    }

    private void initView() {
        parent = findViewById(R.id.parent_view);
        efab = findViewById(R.id.fab);
        efab.setOnClickListener(this);

        tools.setSystemBarColor(this, R.color.grey_5);
        tools.setSystemBarLight(this);
        tools.setNavigationBarColor(getWindow().getDecorView(),this,R.color.grey_3,true);

        toolbarTitle = findViewById(R.id.toolbar_title);
    }

    private void initSystemUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_80));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        newAdapter = new NewAdapter(this, story);
        headerAdapter = new HeaderAdapter(tools.getFromSharedPref(this,Tools.PREFTYPE_NAME, Tools.USERNAME), realmResults.size(), this);
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
                switch (direction){
                    case ItemTouchHelper.RIGHT:
                        openEditor(viewHolder.getLayoutPosition() - 1);
                        Log.d(TAG, "onSwiped: Swiped Right");
                        break;
                    case ItemTouchHelper.LEFT:
                        Log.d(TAG, "onSwiped: Swiped left");
                        performDelete(viewHolder.getLayoutPosition() - 1);
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftActionIcon(R.drawable.ic_outline_delete_24)
                        .addSwipeRightActionIcon(R.drawable.ic_outline_edit_24)
                        .setSwipeLeftActionIconTint(R.color.red_600)
                        .setSwipeRightActionIconTint(R.color.colorAccentLight)
                        .addSwipeRightLabel("Edit")
                        .addSwipeLeftLabel("Delete")
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
                    if (efab.isShown()) {
                        efab.shrink();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (efab.isShown()) {
                        efab.extend();
                    }
                }
            }
        });
        newAdapter.setOnClick(MainActivity.this);
    }

    //Deletes an item and shows toast
    private void performDelete(int pos) {
        String uid = story.get(pos).getUniqueID();
        StoryData deletecStory = realmEngine.getSpecificData(uid);  //Saves the entry about to be deleted
        realmEngine.deleteData(uid);
        recyclerView.removeViewAt(pos);
        showSnack(deletecStory);
    }

    private void showSnack(StoryData deletedStory) {
        Snackbar snackbar = Snackbar
                .make(parent, "Deleted Successfully!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", view -> {
                    //adds the previously deleted entry
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
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnSearchClickListener(v -> toolbarTitle.setVisibility(View.GONE));
        searchView.setOnCloseListener(() -> {
            toolbarTitle.setVisibility(View.VISIBLE);
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                Log.d(TAG, "onQueryTextChange: "+ newText);
                initData(newText);
                return false;
            }
        });
        return true;
    }

    private Bundle bundleData(int i){

        String date = story.get(i).getDate();
        String time = story.get(i).getTime();
        String body = story.get(i).getBody();
        String uid = story.get(i).getUniqueID();
        long timestamp = story.get(i).getTimestamp();

        Bundle args = new Bundle();
        args.putString(Tools.BODY, body);
        args.putString(Tools.DATE, date);
        args.putString(Tools.TIME, time);
        args.putLong(Tools.TIMESTAMP, timestamp);
        args.putInt(Tools.POSITION,i);
        args.putString(Tools.UID, uid);

        return args;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    //Opens the editor with bundled data from selected item
    public void openEditor(int pos){
        Intent intent = new Intent(MainActivity.this, WriteActivity.class);
        intent.putExtras(bundleData(pos));
        startActivity(intent);
    }

    //Only opens the editor
    public void openEditor(){
        Intent intent = new Intent(MainActivity.this, WriteActivity.class);
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
        if (v.getId() == efab.getId()) {
            openEditor();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.backup:
                openSelectedActivity(BackupRestoreActivity.class);
                break;
            case R.id.about:
                openSelectedActivity(AboutActivity.class);
                break;
            case R.id.settings:
                tools.errorToast(this, "Settings not implemented Yet");
                break;
            case R.id.nightSwitch:
                tools.errorToast(this, "Nightmode not implemented Yet");
                throw new RuntimeException("This is a crash");
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSelectedActivity(Class<?> c){
        Intent intent = new Intent(MainActivity.this, c);
        startActivity(intent);
    }



    @Override
    public void onBottomSheetButtonClick(View view, int pos) {
        openEditor(pos);
    }
}
