package com.github.tvbox.osc.player.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.beanry.InitBean;
import com.github.tvbox.osc.beanry.ReUserBean;
import com.github.tvbox.osc.beanry.UserInfoBean;
import com.github.tvbox.osc.ui.activity.VipActivity;
import com.github.tvbox.osc.ui.activity.VipCardActivity;
import com.github.tvbox.osc.util.BaseR;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.MMkvUtils;
import com.github.tvbox.osc.util.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import xyz.doikki.videoplayer.controller.BaseVideoController;
import xyz.doikki.videoplayer.controller.IControlComponent;
import xyz.doikki.videoplayer.controller.IGestureComponent;
import xyz.doikki.videoplayer.player.VideoView;
import xyz.doikki.videoplayer.util.PlayerUtils;


/*
直播控制界面

* */

public abstract class BaseController2 extends BaseVideoController implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnTouchListener {
    private static final String TAG = "BaseController2";
    private GestureDetector mGestureDetector;
    private AudioManager mAudioManager;
    private boolean mIsGestureEnabled = true;
    private int mStreamVolume;
    private float mBrightness;
    private int mSeekPosition;
    private boolean mFirstTouch;
    private boolean mChangePosition;
    private boolean mChangeBrightness;
    private boolean mChangeVolume;
    private boolean mCanChangePosition = true;
    private boolean mEnableInNormal;
    private boolean mCanSlide;
    private int mCurPlayState;
    private long VIPTime = 0; //到期时间
    private long currentTime = 1; //当前时间
    protected Handler mHandler;
    protected HandlerCallback mHandlerCallback;

    protected interface HandlerCallback {
        void callback(Message msg);
    }

    private boolean mIsDoubleTapTogglePlayEnabled = true;

    public BaseController2(@NonNull Context context) {
        super(context);
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int what = msg.what;
                switch (what) {
                    case 100: { // 亮度+音量调整
                        mSlideInfo.setVisibility(VISIBLE);
                        mSlideInfo.setText(msg.obj.toString());
                        break;
                    }
                    case 101: { // 亮度+音量调整 关闭
                        mSlideInfo.setVisibility(GONE);
                        break;
                    }
                    default: {
                        if (mHandlerCallback != null)
                            mHandlerCallback.callback(msg);
                        break;
                    }
                }
                return false;
            }
        });
    }

    public BaseController2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseController2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private TextView mSlideInfo;
    private ProgressBar mLoading;
    private ViewGroup mPauseRoot;
    private TextView mPauseTime;
    private TextView mPauseShiKan;

    @Override
    protected void initView() {
        super.initView();
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);
        mSlideInfo = findViewWithTag("vod_control_slide_info");
        mLoading = findViewWithTag("vod_control_loading");
        mPauseRoot = findViewWithTag("vod_control_pause");
        mPauseTime = findViewWithTag("vod_control_pause_t");
        mPauseShiKan = findViewWithTag("vod_control_slide_user_shikan");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setProgress(int duration, int position) {
        super.setProgress(duration, position);
        isEndExperience(position); //播放进度
        mPauseTime.setText(PlayerUtils.stringForTime(position) + " / " + PlayerUtils.stringForTime(duration));
    }

    private boolean isReplay = false;
    private boolean isToPay = false; //false试看结束返回详情页，true支付页
    private final InitBean initBean = MMkvUtils.loadInitBean("");
    private ReUserBean userData;

    private void isEndExperience(int second) {
        if (ToolUtils.getIsEmpty(initBean.msg.uiMode) && !initBean.msg.uiMode.equals("n")) { //收费模式
            //  if (VIPTime < System.currentTimeMillis() / 1000) { //非会员
            if (VIPTime < System.currentTimeMillis() / 1000 && VIPTime != 999999999) {
                if (isReplay) {
                    second = 0;
                    isReplay = false;
                    mControlWrapper.seekTo(second);
                }
                if ((second / 1000) > 360) { //试看六分钟
                    if (userData != null && ToolUtils.getIsEmpty(userData.msg.token)){
                        getUserInfo(userData.msg.token, true);  //每次结束试看的时候进行一次网络验证
                    }else {
                        EndTheTrial(true);
                    }
                } else if ((second / 1000) > 10) { //显示试看标签
                    if (mPauseShiKan.getVisibility() == GONE) mPauseShiKan.setVisibility(VISIBLE);
                }
            } else { //是会员
                if (mPauseShiKan.getVisibility() == VISIBLE) mPauseShiKan.setVisibility(GONE);
            }
        } else { //免费模式
            if (mPauseShiKan.getVisibility() == VISIBLE) mPauseShiKan.setVisibility(GONE);
        }
    }

    @Override
    protected void onPlayStateChanged(int playState) {
        super.onPlayStateChanged(playState);
        switch (playState) {
            case VideoView.STATE_IDLE:
                mLoading.setVisibility(GONE);
                Log.d(TAG, "VideoView.STATE_IDLE");
                break;
            case VideoView.STATE_PLAYING: //开始播放
                mLoading.setVisibility(GONE);
                mPauseRoot.setVisibility(GONE);
                //每个视频开始播放前都检测会员状态
                userData = MMkvUtils.loadReUserBean("");
                if (userData != null && ToolUtils.getIsEmpty(userData.msg.token)) { //已经登录
                    getUserInfo(userData.msg.token, false); //刷新会员信息
                } else {
                    VIPTime = currentTime - 1000; //强制执行试看
                    MMkvUtils.saveReUserBean(null); //清除账户信息
                }
                Log.d(TAG, "VideoView.STATE_PLAYING");
                break;
            case VideoView.STATE_PAUSED:
                mPauseRoot.setVisibility(VISIBLE);
                mLoading.setVisibility(GONE);
                Log.d(TAG, "VideoView.STATE_PAUSED");
                break;
            case VideoView.STATE_PREPARED:
                Log.d(TAG, "VideoView.STATE_PREPARED");
                break;
            case VideoView.STATE_ERROR:
                Log.d(TAG, "VideoView.STATE_ERROR");
                break;
            case VideoView.STATE_BUFFERED:
                mLoading.setVisibility(GONE);
                Log.d(TAG, "VideoView.STATE_BUFFERED");
                break;
            case VideoView.STATE_PREPARING:
                Log.d(TAG, "VideoView.STATE_PREPARING");
                break;
            case VideoView.STATE_BUFFERING:
                mLoading.setVisibility(VISIBLE);
                Log.d(TAG, "VideoView.STATE_BUFFERING");
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                mLoading.setVisibility(GONE);
                mPauseRoot.setVisibility(GONE);
                Log.d(TAG, "VideoView.STATE_PLAYBACK_COMPLETED");
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void getUserInfo(String token, boolean toPay) {
        new Thread(() -> {
            OkGo.<String>post(ToolUtils.setApi("get_info"))
                    .params("token", token)
                    .params("t", System.currentTimeMillis() / 1000)
                    .params("sign", ToolUtils.setSign("token=" + token))
                    .execute(new AbsCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                JSONObject jo = new JSONObject(response.body());
                                if (jo.getInt("code") == 200) {
                                    UserInfoBean userInfoData = new Gson().fromJson(BaseR.decry_R(response.body()), UserInfoBean.class);
                                    VIPTime = userInfoData.msg.vip; //刷新会员到期时间截
                                    userData.msg.info.vip = userInfoData.msg.vip;
                                    userData.msg.info.fen = userInfoData.msg.fen;
                                    mControlWrapper.isUserVip(VIPTime > currentTime); //非会员不保存进度，防止无试看时间
                                    Log.d(TAG, "是否会员: "+(VIPTime > currentTime));
                                    MMkvUtils.saveReUserBean(userData);
                                } else {
                                    VIPTime = currentTime - 1000; //强制执行试看
                                    MMkvUtils.saveReUserBean(null); //清除会员信息
                                    Toast.makeText(mActivity, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                                EndTheTrial(toPay);
                                Log.d(TAG, "isEndExperience: " + jo.getString("msg") + VIPTime);
                            } catch (JSONException e) {
                                VIPTime = currentTime - 1000; //强制执行试看
                                MMkvUtils.saveReUserBean(null);//清除会员信息
                                Toast.makeText(mActivity, "未知错误", Toast.LENGTH_SHORT).show();
                                if (toPay) { //请求失败试看完成直接返回
                                    assert mActivity != null;
                                    mActivity.finish();
                                }
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<String> error) {
                            VIPTime = currentTime - 1000; //执行试看
                            MMkvUtils.saveReUserBean(null);//清除会员信息
                            Toast.makeText(mActivity, "请求服务器失败", Toast.LENGTH_SHORT).show();
                            if (toPay) { //请求失败试看完成直接返回
                                assert mActivity != null;
                                mActivity.finish();
                            }
                        }

                        @Override
                        public String convertResponse(okhttp3.Response response) throws Throwable {
                            assert response.body() != null;
                            return response.body().string();
                        }
                    });
        }).start();
    }

    private void EndTheTrial(boolean toPay){
        // if (VIPTime < System.currentTimeMillis() / 1000) { //二次判断
        if (VIPTime < System.currentTimeMillis() / 1000 && VIPTime != 999999999) {
            if (toPay){
                mControlWrapper.pause();
                assert mActivity != null;
                ToolUtils.showToast(mActivity, "试看结束，完整观看需开通会员！", R.drawable.toast_err);
                if (isToPay) {
                    isReplay = true;
                    if (initBean.msg.pay.state.equals("y")) {
                        Intent intent = new Intent(mActivity, VipActivity.class);
                        mActivity.startActivity(intent);
                    } else if (ToolUtils.getIsEmpty(initBean.msg.kamiUrl)) {
                        Intent intent = new Intent(mActivity, VipCardActivity.class);
                        mActivity.startActivity(intent);
                    } else {
                        mActivity.finish();
                    }
                } else {
                    mActivity.finish();
                }
            }
        }
    }

    @Override
    public void setPlayerState(int playerState) {
        super.setPlayerState(playerState);
        if (playerState == VideoView.PLAYER_NORMAL) {
            mCanSlide = mEnableInNormal;
        } else if (playerState == VideoView.PLAYER_FULL_SCREEN) {
            mCanSlide = true;
        }
    }

    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);
        mCurPlayState = playState;
    }

    protected boolean isInPlaybackState() {
        return mControlWrapper != null
                && mCurPlayState != VideoView.STATE_ERROR
                && mCurPlayState != VideoView.STATE_IDLE
                && mCurPlayState != VideoView.STATE_PREPARING
                && mCurPlayState != VideoView.STATE_PREPARED
                && mCurPlayState != VideoView.STATE_START_ABORT
                && mCurPlayState != VideoView.STATE_PLAYBACK_COMPLETED;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 手指按下的瞬间
     */
    @Override
    public boolean onDown(MotionEvent e) {
        if (!isInPlaybackState() //不处于播放状态
                || !mIsGestureEnabled //关闭了手势
                || PlayerUtils.isEdge(getContext(), e)) //处于屏幕边沿
            return true;
        mStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity == null) {
            mBrightness = 0;
        } else {
            mBrightness = activity.getWindow().getAttributes().screenBrightness;
        }
        mFirstTouch = true;
        mChangePosition = false;
        mChangeBrightness = false;
        mChangeVolume = false;
        return true;
    }

    /**
     * 单击
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (isInPlaybackState()) {
            mControlWrapper.toggleShowState();
        }
        return true;
    }

    /**
     * 双击
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mIsDoubleTapTogglePlayEnabled && !isLocked() && isInPlaybackState()) togglePlay();
        return true;
    }

    /**
     * 在屏幕上滑动
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!isInPlaybackState() //不处于播放状态
                || !mIsGestureEnabled //关闭了手势
                || !mCanSlide //关闭了滑动手势
                || isLocked() //锁住了屏幕
                || PlayerUtils.isEdge(getContext(), e1)) //处于屏幕边沿
            return true;
        float deltaX = e1.getX() - e2.getX();
        float deltaY = e1.getY() - e2.getY();
        if (mFirstTouch) {
            mChangePosition = Math.abs(distanceX) >= Math.abs(distanceY);
            if (!mChangePosition) {
                //半屏宽度
                int halfScreen = PlayerUtils.getScreenWidth(getContext(), true) / 2;
                if (e2.getX() > halfScreen) {
                    mChangeVolume = true;
                } else {
                    mChangeBrightness = true;
                }
            }

            if (mChangePosition) {
                //根据用户设置是否可以滑动调节进度来决定最终是否可以滑动调节进度
                mChangePosition = mCanChangePosition;
            }

            if (mChangePosition || mChangeBrightness || mChangeVolume) {
                for (Map.Entry<IControlComponent, Boolean> next : mControlComponents.entrySet()) {
                    IControlComponent component = next.getKey();
                    if (component instanceof IGestureComponent) {
                        ((IGestureComponent) component).onStartSlide();
                    }
                }
            }
            mFirstTouch = false;
        }
        if (mChangePosition) {
            slideToChangePosition(deltaX);
        } else if (mChangeBrightness) {
            slideToChangeBrightness(deltaY);
        } else if (mChangeVolume) {
            slideToChangeVolume(deltaY);
        }
        return true;
    }

    protected void slideToChangePosition(float deltaX) {
        deltaX = -deltaX;
        int width = getMeasuredWidth();
        int duration = (int) mControlWrapper.getDuration();
        int currentPosition = (int) mControlWrapper.getCurrentPosition();
        int position = (int) (deltaX / width * 120000 + currentPosition);
        if (position > duration) position = duration;
        if (position < 0) position = 0;
        for (Map.Entry<IControlComponent, Boolean> next : mControlComponents.entrySet()) {
            IControlComponent component = next.getKey();
            if (component instanceof IGestureComponent) {
                ((IGestureComponent) component).onPositionChange(position, currentPosition, duration);
            }
        }
        updateSeekUI(currentPosition, position, duration);
        mSeekPosition = position;
    }

    protected void slideToChangeBrightness(float deltaY) {
        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity == null) return;
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        int height = getMeasuredHeight();
        if (mBrightness == -1.0f) mBrightness = 0.5f;
        float brightness = deltaY * 2 / height * 1.0f + mBrightness;
        if (brightness < 0) {
            brightness = 0f;
        }
        if (brightness > 1.0f) brightness = 1.0f;
        int percent = (int) (brightness * 100);
        attributes.screenBrightness = brightness;
        window.setAttributes(attributes);
        for (Map.Entry<IControlComponent, Boolean> next : mControlComponents.entrySet()) {
            IControlComponent component = next.getKey();
            if (component instanceof IGestureComponent) {
                ((IGestureComponent) component).onBrightnessChange(percent);
            }
        }
        Message msg = Message.obtain();
        msg.what = 100;
        msg.obj = "亮度" + percent + "%";
        mHandler.sendMessage(msg);
        mHandler.removeMessages(101);
        mHandler.sendEmptyMessageDelayed(101, 1000);
    }

    protected void slideToChangeVolume(float deltaY) {
        int streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int height = getMeasuredHeight();
        float deltaV = deltaY * 2 / height * streamMaxVolume;
        float index = mStreamVolume + deltaV;
        if (index > streamMaxVolume) index = streamMaxVolume;
        if (index < 0) index = 0;
        int percent = (int) (index / streamMaxVolume * 100);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) index, 0);
        for (Map.Entry<IControlComponent, Boolean> next : mControlComponents.entrySet()) {
            IControlComponent component = next.getKey();
            if (component instanceof IGestureComponent) {
                ((IGestureComponent) component).onVolumeChange(percent);
            }
        }
        Message msg = Message.obtain();
        msg.what = 100;
        msg.obj = "音量" + percent + "%";
        mHandler.sendMessage(msg);
        mHandler.removeMessages(101);
        mHandler.sendEmptyMessageDelayed(101, 1000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //滑动结束时事件处理
        if (!mGestureDetector.onTouchEvent(event)) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    stopSlide();
                    if (mSeekPosition > 0) {
                        mControlWrapper.seekTo(mSeekPosition);
                        mSeekPosition = 0;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    stopSlide();
                    mSeekPosition = 0;
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void stopSlide() {
        for (Map.Entry<IControlComponent, Boolean> next : mControlComponents.entrySet()) {
            IControlComponent component = next.getKey();
            if (component instanceof IGestureComponent) {
                ((IGestureComponent) component).onStopSlide();
            }
        }
    }

    protected void updateSeekUI(int curr, int seekTo, int duration) {

    }

    /**
     * 设置是否可以滑动调节进度，默认可以
     */
    public void setCanChangePosition(boolean canChangePosition) {
        mCanChangePosition = canChangePosition;
    }

    /**
     * 是否在竖屏模式下开始手势控制，默认关闭
     */
    public void setEnableInNormal(boolean enableInNormal) {
        mEnableInNormal = enableInNormal;
    }

    /**
     * 是否开启手势控制，默认开启，关闭之后，手势调节进度，音量，亮度功能将关闭
     */
    public void setGestureEnabled(boolean gestureEnabled) {
        mIsGestureEnabled = gestureEnabled;
    }

    /**
     * 是否开启双击播放/暂停，默认开启
     */
    public void setDoubleTapTogglePlayEnabled(boolean enabled) {
        mIsDoubleTapTogglePlayEnabled = enabled;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onKeyEvent(KeyEvent event) {
        return false;
    }
}
