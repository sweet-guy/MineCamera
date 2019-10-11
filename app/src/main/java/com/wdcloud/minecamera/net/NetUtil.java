package com.wdcloud.minecamera.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Info:
 * Created by Umbrella.
 * CreateTime: 2019/10/11 14:02
 */
public class NetUtil {
    /**
     * 没有连接网络
     */
    private static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    private static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    private static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return NETWORK_WIFI;
                }
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return NETWORK_MOBILE;
                }
            }
            else
            {
                //sdk小于21
                int type = activeNetworkInfo.getType();
                if(type==ConnectivityManager.TYPE_MOBILE)
                {
                    return NETWORK_MOBILE;
                }
                if(type==ConnectivityManager.TYPE_WIFI)
                {
                    return NETWORK_WIFI;
                }
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }
}
