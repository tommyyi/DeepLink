package com.laser.tools;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */
public class UiUtil {

    private static SoftReference<Context> sContextReference;
    private static SoftReference<Handler> sUiHandlerReference;
    private static volatile Handler sBackHandler;
    private static SoftReference<Toast> sToastReference;
    private static boolean sAutoRelease = false;

    public static void init(Context context) {
        init(context, false);
    }

    public static void init(Context context, boolean autoRelease) {
        Context appContext = context.getApplicationContext();
        sContextReference = new SoftReference<>(appContext);
        sAutoRelease = autoRelease;
    }


    private static Handler getUiHandler() {
        if (sUiHandlerReference != null && sUiHandlerReference.get() != null) {
            return sUiHandlerReference.get();
        }
        synchronized (UiUtil.class) {
            if (sUiHandlerReference == null || sUiHandlerReference.get() == null) {
                Handler handler = new Handler(Looper.getMainLooper());
                sUiHandlerReference = new SoftReference<>(handler);
            }
            return sUiHandlerReference.get();
        }
    }


    private static Handler getBackHandler() {
        if (sBackHandler != null) {
            return sBackHandler;
        }
        synchronized (UiUtil.class) {
            if (sBackHandler == null) {
                HandlerThread handlerThread = new HandlerThread("trifleThings");
                handlerThread.start();
                sBackHandler = new Handler(handlerThread.getLooper());
            }
            return sBackHandler;
        }
    }


    public static Context getContext() {
        return sContextReference.get();
    }


    public static void postUiThread(Runnable runnable) {
        getUiHandler().post(runnable);
    }

    public static void postUiThreadDelayed(Runnable runnable, long delayMillis) {
        getUiHandler().postDelayed(runnable, delayMillis);
    }


    public static void postBackThread(final Runnable runnable) {
        Runnable realRunable = getRealRunnable(runnable);
        getBackHandler().post(realRunable);
    }


    public static void postBackThreadDelayed(Runnable runnable, long delayMillis) {
        Runnable realRunable = getRealRunnable(runnable);
        getBackHandler().postDelayed(realRunable, delayMillis);
    }

    @NonNull
    private static Runnable getRealRunnable(final Runnable runnable) {
        if (!sAutoRelease) {
            return runnable;
        }
        return new Runnable() {
            @Override
            public void run() {
                runnable.run();
                if (sContextReference == null) {
                    return;
                }
                Context context = sContextReference.get();
                if (context != null) {
                    if (isAppExit(context)) {
                        quit();
                    }
                }
            }
        };
    }

    public static void quit() {
        if (sBackHandler != null) {
            Looper looper = sBackHandler.getLooper();
            sBackHandler = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                looper.quitSafely();
            } else {
                try {
                    looper.quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void showShortToast(CharSequence text) {
        Toast toast = getToast(-1);
        if (toast != null) {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void showShortToast(@StringRes int resId) {
        Toast toast = getToast(-1);
        if (toast != null) {
            toast.setText(resId);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public static void showLongToast(CharSequence text) {
        Toast toast = getToast(-1);
        if (toast != null) {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public static void showLongToast(@StringRes int resId) {
        Toast toast = getToast(-1);
        if (toast != null) {
            toast.setText(resId);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }


    public static void showCenterShortToast(CharSequence text) {
        Toast toast = getToast(Gravity.CENTER);
        if (toast != null) {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void showCenterShortToast(@StringRes int resId) {
        Toast toast = getToast(Gravity.CENTER);
        if (toast != null) {
            toast.setText(resId);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public static void showCenterLongToast(CharSequence text) {
        Toast toast = getToast(Gravity.CENTER);
        if (toast != null) {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public static void showCenterLongToast(@StringRes int resId) {
        Toast toast = getToast(Gravity.CENTER);
        if (toast != null) {
            toast.setText(resId);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }


    private static Toast getToast(int gravity) {
        if (sContextReference == null)
            return null;

        Context context = sContextReference.get();
        if (context == null) {
            return null;
        }
        if (sToastReference != null && sToastReference.get() != null) {
            Toast toast = sToastReference.get();
            if (gravity == -1) {
                return toast;
            }
            toast.setGravity(gravity, 0, 0);
            return toast;
        }
        synchronized (UiUtil.class) {
            if (sToastReference == null || sToastReference.get() == null) {
                @SuppressLint("ShowToast") Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                if (gravity == -1) {
                    return toast;
                }
                toast.setGravity(gravity, 0, 0);
                sToastReference = new SoftReference<>(toast);
            }
        }
        return sToastReference.get();
    }

    /**
     * @param context
     * @return true/false表示用户是否在应用中
     */
    private static boolean isAppExit(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null || appProcesses.size() == 0)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}