package com.example.asus.lantalk.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.asus.OnScanListener;
import com.example.asus.lantalk.R;
import com.example.asus.lantalk.adapter.DeviceAdapter;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.service.ScanService;
import com.example.asus.lantalk.utils.LoadingDialogUtil;
import com.example.asus.lantalk.utils.NetWorkUtil;
import com.example.asus.lantalk.utils.ScanDeviceUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private List<SocketBean> mSocketBeanList;
    private DeviceAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";
    private ScanService.PeerBinder mBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (ScanService.PeerBinder) iBinder;
            List<String> mBinderIPList = mBinder.getIPList();
            int size = mBinderIPList.size();
            Log.e(TAG, "onServiceConnected: " + size);
            for (int i = 0; i < size; i++) {
                SocketBean socketBean = new SocketBean();
                socketBean.setPeerIP(mBinderIPList.get(i));
                mSocketBeanList.add(socketBean);
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocketBeanList = new ArrayList<>();
        mAdapter = new DeviceAdapter(mSocketBeanList);
        mRecyclerView = findViewById(R.id.rv_devices);
        mToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnClickListener(new DeviceAdapter.OnClickListener() {
            @Override
            public void OnClick(SocketBean socketBean) {
                TalkActivity.actionStart(MainActivity.this, socketBean.getPeerIP());
            }
        });

//        LoadingDialogUtil.createLoadingDialog(this,"正在搜索对等方");
        ScanDeviceUtil.setOnScanListener(new OnScanListener() {
            @Override
            public void OnSuccessed(int i) {
                LoadingDialogUtil.closeDialog();
              Toast.makeText(MainActivity.this,"共搜索到"+i+"个对等方!",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void OnFailed() {
                LoadingDialogUtil.closeDialog();

                Toast.makeText(MainActivity.this,"搜索失败!",Toast.LENGTH_SHORT).show();
            }
        });

//        if (NetWorkUtil.isNetworkConnected(this) && NetWorkUtil.isWifiConnected(this)) {
//            Intent intent = new Intent(MainActivity.this, ScanService.class);
//            bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
//        } else {
//            Toast.makeText(this, "请打开WiFi连接！", Toast.LENGTH_SHORT).show();
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                break;
            case R.id.menu_search:
                if (NetWorkUtil.isNetworkConnected(this) && NetWorkUtil.isWifiConnected(this)) {
                    LoadingDialogUtil.createLoadingDialog(MainActivity.this,"正在搜索对等方！");
                    Intent intent = new Intent(MainActivity.this, ScanService.class);
                    bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
                } else {
                    Toast.makeText(this, "请打开WiFi连接！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
