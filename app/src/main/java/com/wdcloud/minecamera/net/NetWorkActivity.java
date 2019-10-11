package com.wdcloud.minecamera.net;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.wdcloud.minecamera.R;

public class NetWorkActivity extends AppCompatActivity implements NetEvent {
    /**
     * 监控网络的广播
     */
    private NetBroadcastReceiver netBroadcastReceiver;

    private int netMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work);
    }

    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册广播
        if (netBroadcastReceiver == null) {
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);
            // /** * 设置监听 */
            netBroadcastReceiver.setNetEvent(this);
        }
    }

    private void isNetConnect() {
        switch (netMobile) {
            case 1:
                //wifi
                Toast.makeText(NetWorkActivity.this,"当前网络类型:wifi",Toast.LENGTH_LONG).show();
                break;
            case 0:
                // 移动数据
                Toast.makeText(NetWorkActivity.this,"当前网络类型:移动数据",Toast.LENGTH_LONG).show();
                break;
            case -1:
                Toast.makeText(NetWorkActivity.this,"当前无网络",Toast.LENGTH_LONG).show();
                // 没有网络
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(netBroadcastReceiver!=null)
        {
            unregisterReceiver(netBroadcastReceiver);
        }
    }
}
