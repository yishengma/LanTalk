package com.example.asus.lantalk.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.asus.lantalk.listener.OnScanListener;
import com.example.asus.lantalk.R;
import com.example.asus.lantalk.adapter.DeviceAdapter;
import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.service.ReceiveService;
import com.example.asus.lantalk.service.ScanService;
import com.example.asus.lantalk.service.SendIntentService;
import com.example.asus.lantalk.utils.LoadingDialogUtil;
import com.example.asus.lantalk.utils.NetWorkUtil;
import com.example.asus.lantalk.utils.ScanDeviceUtil;
import com.example.asus.lantalk.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.asus.lantalk.constant.Constant.SEND_PEER_BEAN;

public class MainActivity extends AppCompatActivity
        implements SendIntentService.OnSendListener,
        ReceiveService.OnConnectListener {
    private Toolbar mToolbar;
    private List<SocketBean> mSocketBeanList;
    private DeviceAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";


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
        ReceiveService.setConnectListener(this);

        mAdapter.setOnClickListener(new DeviceAdapter.OnClickListener() {
            @Override
            public void OnClick(SocketBean socketBean) {
                LoadingDialogUtil.createLoadingDialog(MainActivity.this, "正在连接...");
                Intent intent = new Intent(MainActivity.this, SendIntentService.class);
                SendIntentService.setSendListener(MainActivity.this);
                intent.setAction(Constant.ACTION_SEND_MSG);
                socketBean.setStatus(Constant.REQUEST);
                socketBean.setTime(TimeUtil.getCurrentTime());
                socketBean.setSendIP(App.sIP);
                socketBean.setSendName(App.sName);
                intent.putExtra(SEND_PEER_BEAN, socketBean);
                startService(intent);

            }
        });
       ScanDeviceUtil.setOnScanListener(new OnScanListener() {
           @Override
           public void OnSuccess(final List<String> list) {
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      int size = list.size();
                      for (int i = 0; i < size; i++) {
                          SocketBean socketBean = new SocketBean();
                          socketBean.setReceiveIP(list.get(i));
                          mSocketBeanList.add(socketBean);
                      }
                      mAdapter.notifyDataSetChanged();
                      LoadingDialogUtil.closeDialog();
                  }

              });
           }

           @Override
           public void OnFailed() {

           }
       });



    }

    @Override
    public void onRefuseCallBack() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "对方拒绝了你的请求！", Toast.LENGTH_SHORT).show();
                LoadingDialogUtil.closeDialog();
            }
        });
    }

    @Override
    public void onAccessCallBack(final SocketBean bean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialogUtil.closeDialog();
                TalkActivity.actionStart(MainActivity.this, bean.getSendIP(), bean.getSendName());

            }
        });
    }

    @Override
    public void onRequestCallBack(final SocketBean bean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.iv_logo)
                        .setTitle("来自 IP:" + bean.getSendIP())
                        .setMessage(bean.getSendName() + "请求与您通信！")
                        .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, SendIntentService.class);
                                SendIntentService.setSendListener(MainActivity.this);
                                SocketBean socketBean = new SocketBean();
                                socketBean.setReceiveIP(bean.getSendIP());
                                socketBean.setSendIP(App.sIP);
                                socketBean.setSendName(App.sName);
                                intent.setAction(Constant.ACTION_SEND_MSG);
                                socketBean.setStatus(Constant.REFUSE);
                                intent.putExtra(SEND_PEER_BEAN, socketBean);
                                startService(intent);
                                dialog.dismiss();
                                LoadingDialogUtil.closeDialog();
                            }
                        })
                        .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, SendIntentService.class);
                                SendIntentService.setSendListener(MainActivity.this);
                                SocketBean socketBean = new SocketBean();
                                intent.setAction(Constant.ACTION_SEND_MSG);
                                socketBean.setReceiveIP(bean.getSendIP());
                                socketBean.setSendIP(App.sIP);
                                socketBean.setSendName(App.sName);
                                socketBean.setStatus(Constant.CONNECT);
                                intent.putExtra(SEND_PEER_BEAN, socketBean);
                                startService(intent);
                                dialog.dismiss();
                                LoadingDialogUtil.closeDialog();
                                TalkActivity.actionStart(MainActivity.this, bean.getSendIP(), bean.getSendName());

                            }
                        }).create();
                dialog.show();
            }
        });
    }

    @Override
    public void onConnectFailed() {

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
                    if ( LoadingDialogUtil.createLoadingDialog(MainActivity.this, "正在搜索对等方！")){
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

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
