package com.sourav.stories.Activities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sourav.stories.Interfaces.OnAlertDialogActionClickListener;
import com.sourav.stories.R;
import com.sourav.stories.Stuffs.RealmEngine;
import com.sourav.stories.Stuffs.StoryData;
import com.sourav.stories.Stuffs.Tools;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import jp.wasabeef.richeditor.RichEditor;

public class WriteActivity extends AppCompatActivity implements OnAlertDialogActionClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private RichEditor editText;
    private Tools tools = Tools.getInstance();
    private String ediTime, editDate, editBody,
            entryTime, entryDate, entryWeekDay, uid;
    private long timestamp;
    private String TAG = "Write";
    private boolean isEditMode = false;
    private String prev = "";
    private ImageButton ibBold, ibItalic, ibUnderline, ibBullet, ibClock, ibCalender;
    private boolean selectBold, selectItalic, selectUnderline, selectBullet = false;

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
            uid = getIntent().getExtras().getString(Tools.UID);
        }
        entryTime = tools.getCurrentTimeDate(Tools.TIME_FORMAT);
        entryDate = tools.getCurrentTimeDate(Tools.DATE_FORMAT);
        entryWeekDay = tools.getCurrentTimeDate(Tools.WEEKDAY_FORMAT);

    }

    private void initView() {
        FloatingActionButton fab = findViewById(R.id.add_button);
        TextView displayTime = findViewById(R.id.tvTime);
        TextView displayDate = findViewById(R.id.tvDate);
        editText = findViewById(R.id.etWrite);
        editText. setPadding(16,16,16,16);
        editText.focusEditor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.setFocusedByDefault(true);
        }
        ibBold = findViewById(R.id.action_bold);
        ibItalic = findViewById(R.id.action_italic);
        ibUnderline = findViewById(R.id.action_underline);
        ibBullet = findViewById(R.id.action_bullet);
        ibClock = findViewById(R.id.action_clock);
        ibCalender = findViewById(R.id.action_calender);

        ibBold.setOnClickListener(v -> {
            editText.setBold();
            setSelected(ibBold);
        });

        ibItalic.setOnClickListener(v -> {
            editText.setItalic();
            setSelected(ibItalic);
        });
        ibUnderline.setOnClickListener(v -> {
            editText.setUnderline();
            setSelected(ibUnderline);
        });
        ibBullet.setOnClickListener(v -> {
            editText.setBullets();
            setSelected(ibBullet);
        });

        fab.setOnClickListener(v -> {
            writeData();
            //closeKeyboard();
        });

        ibClock.setOnClickListener(v -> showTimePicker());

        ibCalender.setOnClickListener(v -> showDatePicker());

        tools.setSystemBarColor(this, R.color.grey_5);
        tools.setSystemBarLight(this);
        tools.setNavigationBarColor(getWindow().getDecorView(),this, R.color.grey_3,true);

        if (isEditMode) {
            String fullDate = editDate + ", " + tools.getDayOfWeekFromDate(editDate);
            displayDate.setText(fullDate);
            displayTime.setText(ediTime);
            editText.setHtml(editBody);
        } else {
            String fullDate = entryDate + ", " + entryWeekDay;
            displayDate.setText(fullDate);
            displayTime.setText(entryTime);
        }

        editText.setOnInitialLoadListener(isReady -> {
            if (isReady) {
                editText.focusEditor();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
            }
        });

        tools.setListener(WriteActivity.this);
    }

    private void writeData() {
        RealmEngine realmEngine = RealmEngine.getInstance();
        String bodyFinal = editText.getHtml();
        if (editText.getHtml()!=null && !editText.getHtml().isEmpty()) {
            if (!isEditMode) {
                realmEngine.insertData(entryTime, entryDate, bodyFinal);
                prev = bodyFinal;
            } else {
                realmEngine.deleteData(uid);
                realmEngine.addSpecificStory(
                        new StoryData(
                                ediTime, editDate, bodyFinal, timestamp, uid
                        )
                );
            }
            Log.d(TAG, "writeData: Modified Millis: " + Tools.generateMillis(editDate, ediTime));
            finish();
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

    private void setSelected(ImageButton imageButton){
        switch (imageButton.getId()) {
            case R.id.action_bold:
                if (selectBold){
                    removefilter(imageButton);
                    selectBold = false;
                }else{
                    setFilter(imageButton);
                    selectBold = true;
                }
                break;
            case R.id.action_italic:
                if (selectItalic){
                    removefilter(imageButton);
                    selectItalic = false;
                }else{
                    setFilter(imageButton);
                    selectItalic = true;
                }
                break;
            case R.id.action_underline:
                if (selectUnderline){
                    removefilter(imageButton);
                    selectUnderline = false;
                }else{
                    setFilter(imageButton);
                    selectUnderline = true;
                }
                break;
            case R.id.action_bullet:
                if (selectBullet){
                    removefilter(imageButton);
                    selectBullet = false;
                }else{
                    setFilter(imageButton);
                    selectBullet = true;
                }
                break;
        }
    }

    private void setFilter(ImageButton imageButton){
        imageButton.getBackground().setColorFilter(getResources().getColor(R.color.grey_10), PorterDuff.Mode.SRC_ATOP);
        imageButton.invalidate();
    }

    private void removefilter(ImageButton imageButton){
        imageButton.getBackground().clearColorFilter();
        imageButton.invalidate();
    }

    @Override
    public void onBackPressed() {
        if (editText.getHtml() != null){
            tools.createSimpleAlert(this, "You have unsaved entry. Do you want to save it before leaving?", "Save", "Leave");
        }else super.onBackPressed();
    }


    @Override
    public void onPositiveClick() {
        writeData();
    }

    @Override
    public void onNegativeClick() {
        finish();
    }

    private void showDatePicker(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                WriteActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setOkColor(getResources().getColor(R.color.grey_3));
        dpd.setCancelColor(getResources().getColor(R.color.grey_3));
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
        Log.d(TAG, "showDatePicker: ");
    }

    private void showTimePicker(){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                WriteActivity.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setOkColor(getResources().getColor(R.color.grey_3));
        tpd.setCancelColor(getResources().getColor(R.color.grey_3));
        tpd.show(getSupportFragmentManager(), "Timepickerdialog");
        Log.d(TAG, "showTimePicker: ");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }
}
