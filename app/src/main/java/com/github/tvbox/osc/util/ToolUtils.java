package com.github.tvbox.osc.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.beanry.NoticeBean;
import com.github.tvbox.osc.beanry.ReLevelBean;
import com.github.tvbox.osc.view.HomeDialog;
import com.github.tvbox.osc.view.LiveLoadingDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class ToolUtils {
    private static final String TAG = "ToolUtils";
    /**
     * @brief 对话框。
     */
    private static LiveLoadingDialog Loadingdialog = null;
//零熙唯一QQ：1007713299//零熙唯一QQ：1007713299//零熙唯一QQ：1007713299
    /**
     * @brief 显示函数。
     * @author joychang
     * @param[in] context 上下文。
     * @param[in] title   标题文字。
     * @param[in] message 内容文字。
     * @note 显示加载对话框处理。
     */
    public static void loadingShow_tv(Context context, int message) {
        if (Loadingdialog != null && Loadingdialog.isShowing()) {
            Loadingdialog.dismiss();
        }
        //使用以下方式显示对话框，按返回键可关闭对话框
        Loadingdialog = new LiveLoadingDialog(context);
        Loadingdialog.setLoadingMsg(message);
        Loadingdialog.setCanceledOnTouchOutside(false);
        Loadingdialog.show();
    }

    /**
     * @brief 关闭函数。
     * @author joychang
     * @note 关闭加载对话框处理。
     */
    public static void loadingClose_Tv() {
        if (Loadingdialog != null) {
            Loadingdialog.cancel();
            Loadingdialog = null;
        } else {
            Log.w(TAG, "close(): mDialog is not showing");
        }

        Log.d(TAG, "close() end");
    }

    /**
     * 获取当前应用包名
     *
     * @return
     */
    public static String getCurrentPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 下载apk文件进行安装
     *
     * @param context
     * @param mHandler 更新显示进度的handler
     * @param url
     * @author drowtram
     */
    public static void startDownloadApk(final Context context, final String url, final Handler mHandler) {
        Log.d(TAG, "startDownloadApk: "+url);
        showToast(context, "正在后台下载，完成后提示安装...", R.drawable.toast_smile);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sdPath = Environment.getExternalStorageDirectory() + "/";
                String mSavePath = sdPath + "loudTV";
                File dir = new File(mSavePath);
                Looper.prepare();
                String apkName = url.substring(url.lastIndexOf("/") + 1);
                Log.d("zhouchuan", "文件名称" + apkName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = null;
                try {
                    HttpGet hGet = null;
                    Log.d(TAG, "run111: "+url);
                    if (url.contains(" ")){
                        hGet = new HttpGet(url.replaceAll(" ", "%20"));//替换掉空格字符串，不然下载不成功
                    }else{
                        hGet = new HttpGet(url);
                    }
                    HttpResponse hResponse = new DefaultHttpClient().execute(hGet);
                    if (hResponse.getStatusLine().getStatusCode() == 200) {
                        InputStream is = hResponse.getEntity().getContent();
                        float downsize = 0;
                        if (mHandler != null) {
                            //获取下载的文件大小
                            float size = hResponse.getEntity().getContentLength();
                            mHandler.obtainMessage(1001, size).sendToTarget();//发消息给handler处理更新信息
                        }
                        fos = context.openFileOutput(apkName, Context.MODE_PRIVATE);
                        byte[] buffer = new byte[8192];
                        int count = 0;
                        while ((count = is.read(buffer)) != -1) {
                            if (mHandler != null) {
                                downsize += count;
                                mHandler.obtainMessage(1002, downsize).sendToTarget();//发消息给handler处理更新信息
                            }
                            fos.write(buffer, 0, count);
                        }
                        fos.close();
                        is.close();
                        installApk(context, "/data/data/" + getCurrentPackageName(context) + "/files/" + apkName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }

    /**
     * 判断apk文件是否可以安装
     *
     * @param context
     * @param filePath
     * @return
     */
    public static boolean getUninatllApkInfo(Context context, String filePath) {
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                result = true;
            }
        } catch (Exception e) {
            result = false;
            Log.e("zhouchuan", "*****  解析未安装的 apk 出现异常 *****" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 安装apk文件
     *
     * @param fileName
     * @author drowtram
     */
    public static void installApk(Context context, String fileName) {
        if (getUninatllApkInfo(context, fileName)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File apkFile = new File(fileName);
            try {
                String[] args2 = {"chmod", "604", apkFile.getPath()};
                Runtime.getRuntime().exec(args2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", apkFile);
                context.grantUriPermission(getCurrentPackageName(context), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                    if (!hasInstallPermission) {
                        Intent intent1 = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                        return;
                    }
                }
            } else {
                Uri uri = Uri.fromFile(apkFile);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                try {
                    String[] args2 = {"chmod", "604", apkFile.getPath()};
                    Runtime.getRuntime().exec(args2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                context.startActivity(intent);
            }
        } else {
            Toast.makeText(context, "文件还没下载完成，请耐心等待。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取当前应用版本号
     *
     * @return
     */
    @SuppressWarnings("unused")
    public static String getVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 自定义Toast
     *
     * @param context
     * @param text
     * @param image
     */
    @SuppressLint("InflateParams")
    public static void showToast(Context context, String text, int image) {
        View view = LayoutInflater.from(context).inflate(R.layout.tv_toast, null);
        TextView tv_toast = view.findViewById(R.id.tv_smtv_toast);
        ImageView iv_toast = view.findViewById(R.id.iv_smtv_toast);
        tv_toast.setText(text);
        iv_toast.setBackgroundResource(image);
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static boolean getIsEmpty(String text) {
        return text != null && !text.equals("") && text.length() >= 1; //true
    }

    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }

    /*
     * 将时间转换为时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static int dateToStamp(String s) {
        int res = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(s);
            assert date != null;
            long ts = date.getTime() / 1000;
            res = Integer.parseInt(String.valueOf(ts));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    /*
     * 将时间戳转换为时间 by茶茶qq205888578
     */

    public static String stampToDate(int s) {
        String res;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = (long) s * 1000;

        if (lt == 999999999000L) {
            // 原始时间戳等于999999999时，返回永久会员
            return "永久会员";
        }

        if (lt == 888888888000L) {
            // 原始时间戳等于888888888时，返回免费模式
            return "免费模式";
        }
        // 时间戳大于当前时间，返回时间格式，否则返回未开通会员
        if (lt > System.currentTimeMillis()) {
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        }

        return "未开通会员";
    }

    /**
     * 处理加密
     **/
    public static String setSign(String data) {
        String signData;
        if (data.equals("null")) {
            signData = "t=" + System.currentTimeMillis() / 1000 + "&" + HawkConfig.API_KEY;
        } else {
            signData = data + "&t=" + System.currentTimeMillis() / 1000 + "&" + HawkConfig.API_KEY;
        }
        return MD5.encode(signData);
    }

    /**
     * 处理请求接口
     **/
    public static String setApi(String act) {
        Log.d(TAG, "setApi: " + HawkConfig.MMM_MMM + "/api.php?act=" + act + "&app=" + HawkConfig.APP_ID);
        return HawkConfig.MMM_MMM + "/api.php?act=" + act + "&app=" + HawkConfig.APP_ID;
    }

    /**
     * 初始化数据
     **/
    public static boolean iniData(Response<String> response, Context context) {
        try {
            JSONObject jo = new JSONObject(BaseR.decry_R(response.body()));
            Log.d(TAG, "请求结果："+context + "====code: " + jo.getString("code") + "===data:" + response.body());
            if (jo.getInt("code") == 200) {
                return true;
            } else {
                Toast.makeText(context, jo.getString("msg" + context), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 初始化多线路数据
     **/
    public static boolean iniData2(Response<String> response, Context context) {
        try {
            String responseBody = response.body(); // 获取响应体的字符串
            Log.d(TAG, "请求结果：" + context + "====data:" + responseBody);

            JSONObject jo = new JSONObject(responseBody); // 创建 JSON 对象

            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 显示升级提示的对话框
     */
    public static void HomeDialog(Context context, String text) {

        StringBuilder str = new StringBuilder();
        HomeDialog.Builder builder = new HomeDialog.Builder(context);
        builder.setTitle("公告");
        String[] remarks = text.split(";");
        for (int i = 0; i < remarks.length; i++) {
            if (i == remarks.length - 1) {
                str.append(remarks[i]);
            } else {
                str.append(remarks[i]).append("\n");
            }
        }
        builder.setMessage(str.toString());
        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
