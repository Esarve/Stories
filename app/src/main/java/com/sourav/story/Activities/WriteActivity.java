package com.sourav.story.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sourav.story.R;
import com.sourav.story.Stuffs.RealmEngine;
import com.sourav.story.Stuffs.StoryData;
import com.sourav.story.Stuffs.Tools;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private Tools tools = Tools.getInstance();
    private String ediTime, editDate, editBody,
            entryTime, entryDate, entryWeekDay;
    private long timestamp;
    private String TAG = "Write";
    private boolean isEditMode = false;
    private String prev = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        fetchData();
        initView();
    }

    private void fetchData() {
        if (getIntent().getExtras() != null) {
            isEditMode = true;
            ediTime = getIntent().getExtras().getString(Tools.TIME);
            editDate = getIntent().getExtras().getString(Tools.DATE);
            editBody = getIntent().getExtras().getString(Tools.BODY);
            timestamp = getIntent().getExtras().getLong(Tools.TIMESTAMP);
        }
        entryTime = tools.getCurrentTimeDate(Tools.TIME_FORMAT);
        entryDate = tools.getCurrentTimeDate(Tools.DATE_FORMAT);
        entryWeekDay = tools.getCurrentTimeDate(Tools.WEEKDAY_FORMAT);

    }

    private void initView() {
        ImageButton clickTimedate = findViewById(R.id.btnTimeDate);
        ImageButton clickCancel = findViewById(R.id.btnCancel);
        FloatingActionButton fab = findViewById(R.id.add_button);
        editText = findViewById(R.id.etWrite);
        TextView displayTime = findViewById(R.id.tvTime);
        TextView displayDate = findViewById(R.id.tvDate);

        clickTimedate.setOnClickListener(this);
        clickCancel.setOnClickListener(this);
        fab.setOnClickListener(v -> {
            if (isDuplicate()){
                writeData();
                finish();
            }else
                tools.errorToast(this,"You have already entered the same story!");
            closeKeyboard();
        });

        tools.setSystemBarColor(this, R.color.grey_5);
        tools.setSystemBarLight(this);
        tools.setNavigationBarColor(getWindow().getDecorView(),this, R.color.grey_3,true);

        if (isEditMode) {
            String fullDate = editDate + ", " + tools.getDayOfWeekFromDate(editDate);
            displayDate.setText(fullDate);
            displayTime.setText(ediTime);
            editText.setText(editBody);
        } else {
            String fullDate = entryDate + ", " + entryWeekDay;
            displayDate.setText(fullDate);
            displayTime.setText(entryTime);
        }
        editText.requestFocus();
    }

    private boolean isDuplicate() {
        boolean result;
        result = prev.equalsIgnoreCase(editText.getText().toString());
        prev = editText.getText().toString();
        return !result;
    }

    private void writeData() {
        RealmEngine realmEngine = RealmEngine.getInstance();
        String bodyFinal = editText.getText().toString();
        if (!bodyFinal.isEmpty()) {
            if (!isEditMode) {
                realmEngine.insertData(entryTime, entryDate, bodyFinal);
            } else {
                realmEngine.deleteData(timestamp);
                realmEngine.addSpecificStory(
                        new StoryData(
                                ediTime, editDate, bodyFinal, timestamp
                        )
                );
            }
            Log.d(TAG, "writeData: Modified Millis: " + Tools.generateMillis(editDate, ediTime));
        } else {
            String message = "Text field is empty";
            tools.errorToast(this, message);
        }

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTimeDate:
                Toast.makeText(this, "TimeDate Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCancel:
                finish();
                break;
            default:
                break;
        }
    }
}
