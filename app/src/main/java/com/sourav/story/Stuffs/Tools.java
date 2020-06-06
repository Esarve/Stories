package com.sourav.story.Stuffs;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;

public class Tools {

    public static final String TIME = "header";
    public static final String  BODY = "body";
    public static final String DATE = "date";
    public static final String POSITION = "pos";

    private static Tools instance;

    public static Tools getInstance(){
        if (instance == null)
            instance = new Tools();
        return instance;
    }

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
        }
        view.setSystemUiVisibility(flags);
        activity.getWindow()
                .setNavigationBarColor(ContextCompat.getColor(activity.getApplication()
                                .getApplicationContext(),
                        color));
    }
}
