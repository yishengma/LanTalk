package com.example.asus.lantalk.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.adapter.PeerAdapter;
import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.broadcast.ScanResultReceiver;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.service.ReceiveService;
import com.example.asus.lantalk.service.ScanService;
import com.example.asus.lantalk.service.SendIntentService;
import com.example.asus.lantalk.utils.LoadingDialogUtil;
import com.example.asus.lantalk.utils.NetWorkUtil;
import com.example.asus.lantalk.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.asus.lantalk.constant.Constant.SEND_PEER_BEAN;
import static com.example.asus.lantalk.constant.Constant.SERVICE_RECEIVER;

public class MainActivity extends AppCompatActivity
        implements SendIntentService.OnSendListener,
        ReceiveService.OnConnectListener {
    private Toolbar mToolbar;
    private List<SocketBean> mSocketBeanList;
    private PeerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ScanResultReceiver mScanResultReceiver;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initBroadcast();


    }

    private void initView() {
        mSocketBeanList = new ArrayList<>();
        mAdapter = new PeerAdapter(mSocketBeanList, this);
        mRecyclerView = findViewById(R.id.rv_devices);
        mToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        ReceiveService.setConnectListener(this);

        mAdapter.setOnClickListener(new PeerAdapter.OnClickListener() {
            @Override
            public void OnClick(SocketBean socketBean) {
                TalkActivity.actionStart(MainActivity.this,socketBean.getSendIP(),socketBean.getSendName());
            }
        });
    }

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SERVICE_RECEIVER);
        mScanResultReceiver = new ScanResultReceiver();
        registerReceiver(mScanResultReceiver, intentFilter);
    }


    @Override
    public void onRequestCallBack(final SocketBean bean) {

        if (!mSocketBeanList.contains(bean)) {
            Intent intent = new Intent(MainActivity.this, SendIntentService.class);
            SendIntentService.setSendListener(MainActivity.this);
            intent.setAction(Constant.ACTION_SEND_MSG);
            SocketBean socketBean = new SocketBean();
            socketBean.setStatus(Constant.REQUEST);
            socketBean.setTime(TimeUtil.getCurrentTime());
            socketBean.setSendIP(App.sIP);
            socketBean.setSendName(App.sName);
            socketBean.setProfilePicture(App.sProfilePicture);
            socketBean.setReceiveIP(bean.getSendIP());
            intent.putExtra(SEND_PEER_BEAN, socketBean);
            startService(intent);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSocketBeanList.add(bean);
                    mAdapter.notifyDataSetChanged();
                }
            });


        }
    }

    @Override
    public void onReceiveCallBack(final SocketBean bean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int size = mSocketBeanList.size();
                for (int i=0;i<size;i++){
                    if (bean.equals(mSocketBeanList.get(i))){
                        mSocketBeanList.get(i).setMessage(bean.getMessage());
                        mAdapter.notifyItemChanged(i);
                    }
                }
            }
        });
    }

    @Override
    public void onSendSuccess(String type) {

    }

    @Override
    public void onSendFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                LoadingDialogUtil.closeDialog();
            }
        });
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
                    if (LoadingDialogUtil.createLoadingDialog(MainActivity.this, "正在搜索对等方！")) {
                        Intent intent = new Intent(MainActivity.this, ScanService.class);
                        startService(intent);
                    }

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
        if (mScanResultReceiver != null) {
            unregisterReceiver(mScanResultReceiver);
        }

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
