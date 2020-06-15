package com.sourav.story.Stuffs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;

import com.sourav.story.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tools {

    //KEYs for bundle stuffs
    public static final String TIME = "header";
    public static final String TIMESTAMP = "timestamp";
    public static final String  BODY = "body";
    public static final String DATE = "date";
    public static final String POSITION = "pos";

    //Time date format stuffs
    public static final String TIME_FORMAT = "hh:mm a";
    public static final String DATE_FORMAT = "MMMM dd";
    public static final String DATEWITHDAY_FORMAT = "MMMM dd, EEEE";
    public static final String WEEKDAY_FORMAT = "EEEE";

    public static final String USERNAME = "name";
    public static final String PREFTYPE_NAME = "namepref";


    private static Tools instance;

    public static Tools getInstance(){
        if (instance == null)
            instance = new Tools();
        return instance;
    }

    //Systembar, navigationbar stuffs
    public void setSystemBarColor(Activity act, @ColorRes int color) {
        Window window = act.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(act.getResources().getColor(color));
    }

    public  void setSystemBarLight(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = act.findViewById(android.R.id.content);
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public void changeMenuIconColor(Menu menu, @ColorInt int color) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable == null) continue;
            drawable.mutate();
            drawable.setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    color, BlendModeCompat.SRC_ATOP));;
        }
    }

    public void setNavigationBarColor(View view, Activity activity, int color,boolean isLightMode){
        int flags = view.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isLightMode) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow()
                    .setNavigationBarColor(ContextCompat.getColor(activity.getApplication()
                                    .getApplicationContext(),
                            color));
        }
    }

    //Converts date & time in millis
    public static long generateMillis(String date, String time) {
        String myDate = date + " " + time;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT + " " + TIME_FORMAT, Locale.getDefault());
        try {
            Date dateObj = sdf.parse(myDate);
            return dateObj.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //Shows custom error toast
    public void errorToast(Context context, String message) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View custom_view = inflater.inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(context.getResources().getColor(R.color.red_600));
        toast.setView(custom_view);
        toast.show();
    }

    //Gets current date and time
    public String getCurrentTimeDate(String timeOrDate) {
        DateFormat dateFormat = new SimpleDateFormat(timeOrDate, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getDayOfWeekFromDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        SimpleDateFormat simpleDateFormatForWeekDay = new SimpleDateFormat(WEEKDAY_FORMAT, Locale.getDefault());
        try {
            Date obj = simpleDateFormat.parse(date);
            if (obj != null) return simpleDateFormatForWeekDay.format(obj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    //saves value into shared preference
    public void saveToSharedPref(Context context, String prefType, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //gets value from shared preference
    public String getFromSharedPref(Context context, String prefType, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    //converts formatted text into plain text
    public String getPlainText(String html){
        Document doc = Jsoup.parse(html);
        return doc.body().text();
    }

}
