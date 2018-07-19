package mybanner.toerax.com.sendsms.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import mybanner.toerax.com.sendsms.MainActivity;
import mybanner.toerax.com.sendsms.R;

/**
 * Created by 19233 on 2018/7/18 9:15.
 */
public class MyServices extends Service {
    private static final String TAG = "MainService";

    //要引用的布局文件.
    ConstraintLayout toucherLayout;
    //布局参数.
    WindowManager.LayoutParams params;
    //实例化的WindowManager.
    WindowManager windowManager;

    ImageButton imageButton1;

    //状态栏高度.（接下来会用到）
    int statusBarHeight = -1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createToucher();
    }

    private void createToucher() {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        //注意，这里的width和height均使用px而非dp.这里我偷了个懒
        //如果你想完全对应布局设置，需要先获取到机器的dpi
        //px与dp的换算为px = dp * (dpi / 160).
        params.width = 300;
        params.height = 300;

        final LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (ConstraintLayout) inflater.inflate(R.layout.windowsaleart, null);
        //添加toucherlayout
        windowManager.addView(toucherLayout, params);

        Log.i(TAG, "toucherlayout-->left:" + toucherLayout.getLeft());
        Log.i(TAG, "toucherlayout-->right:" + toucherLayout.getRight());
        Log.i(TAG, "toucherlayout-->top:" + toucherLayout.getTop());
        Log.i(TAG, "toucherlayout-->bottom:" + toucherLayout.getBottom());

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG, "状态栏高度为:" + statusBarHeight);

        //浮动窗口按钮.
        imageButton1 = (ImageButton) toucherLayout.findViewById(R.id.imageButton1);

        //其他代码...

        imageButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //ImageButton我放在了布局中心，布局一共300dp
                params.x = (int) event.getRawX() - 150;
                //这就是状态栏偏移量用的地方
                params.y = (int) event.getRawY() - 150 - statusBarHeight;
                windowManager.updateViewLayout(toucherLayout, params);
                return false;
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onDestroy() {
        //用imageButton检查悬浮窗还在不在，这里可以不要。优化悬浮窗时要用到。
        if (imageButton1 != null) {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }

}
