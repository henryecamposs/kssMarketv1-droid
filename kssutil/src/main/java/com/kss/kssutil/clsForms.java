package com.kss.kssutil;

import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by KSS on 17/3/2017.
 */
public class clsForms {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
