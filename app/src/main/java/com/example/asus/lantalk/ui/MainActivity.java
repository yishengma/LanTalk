package com.example.asus.lantalk.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.adapter.DeviceAdapter;
import com.example.asus.lantalk.entity.PeerBean;
import com.example.asus.lantalk.service.ScanService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<PeerBean> mPeerBeanList;
    private DeviceAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";
    private ScanService.PeerBinder mBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (ScanService.PeerBinder)iBinder;
            List<String> mBinderIPList = mBinder.getIPList();
            int size = mBinderIPList.size();
            Log.e(TAG, "onServiceConnected: "+size );
            for (int i=0;i<size;i++){
                PeerBean peerBean = new PeerBean();
                peerBean.setPeerIP(mBinderIPList.get(i));
                mPeerBeanList.add(peerBean);
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
        mPeerBeanList = new ArrayList<>();
        mAdapter = new DeviceAdapter(mPeerBeanList);
        mRecyclerView = findViewById(R.id.rv_devices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        findViewById(R.id.btn_search_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScanService.class);
                bindService(intent,mServiceConnection,BIND_AUTO_CREATE);


            }
        });

        mAdapter.setOnClickListener(new DeviceAdapter.OnClickListener() {
            @Override
            public void OnClick(PeerBean peerBean) {
                TalkActivity.actionStart(MainActivity.this,peerBean.getPeerIP());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}
