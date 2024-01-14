package com.github.tvbox.osc.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Date: 2021年9月28日 10:11:52
 * Author: 梦否
 * Example:
 * MyHandler myHandler = MyHandler.getHandler(this);
 * myHandler.setActivityReference(this);
 * handler = myHandler.getHandler();
 */
public class MyHandler {
    // 锁对象
    private static final Object mObject = new Object();
    private Handler mHandler;
    private WeakReference<Activity> mWeakReference;
    private static MyHandler mMyHandler;

    private MyHandler(IHandler iHandler){
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Activity activity = mWeakReference.get();
                Log.e("DetailActivity","activity is null : " + (activity == null));
                if(activity != null){
                    iHandler.handleMessage(msg);
                }
            }
        };
    }

    /**
     * 虚引用，为了完成逻辑：当前activity不存在时候不处理
     * @param activity
     */
    public void setActivityReference(Activity activity){
        if(mWeakReference != null){
//            if(mWeakReference.get() != null){
//                mWeakReference.get().finishActivity(0);
//            }
            mWeakReference.clear();
        }

        mWeakReference = new WeakReference<>(activity);
    }

    public Handler getHandler(){
        return this.mHandler;
    }

    /**
     * 单例，为了复用
     * @param iHandler
     * @return
     */
    public static MyHandler getHandler(IHandler iHandler){
        if(mMyHandler == null){
            synchronized (mObject){
                if(mMyHandler == null){
                    mMyHandler = new MyHandler(iHandler);
                }
            }
        }
        return mMyHandler;
    }


    public static void destroyHandler(){
        mMyHandler = null;
    }

    public interface IHandler{
        void handleMessage(@NonNull Message msg);
    }
}

