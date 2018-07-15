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

import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.service.ReceiveService;


import com.example.asus.lantalk.utils.LoadingDialogUtil;
import com.example.asus.lantalk.utils.NetWorkUtil;
import com.example.asus.lantalk.utils.NetIPUtil;

import static com.example.asus.lantalk.constant.Constant.sOPENWIFI;

public class MainActivity extends AppCompatActivity
        implements
        ReceiveService.OnConnectListener {
    private Toolbar mToolbar;

    private PeerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {

        mAdapter = new PeerAdapter(App.getmSocketBeanList(), this);
        mRecyclerView = findViewById(R.id.rv_devices);
        mToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        ReceiveService.setConnectListener(this);

        mAdapter.setOnClickListener(new PeerAdapter.OnClickListener() {
            @Override
            public void OnClick(SocketBean socketBean) {
                TalkActivity.actionStart(MainActivity.this, socketBean.getSendIP(), socketBean.getSendName(), socketBean.getProfilePicture());
            }
        });
        LoadingDialogUtil.createLoadingDialog(this,"正在搜寻对等方！");
    }


    /**
     * 收到新的局域网对等方成员，添加自己到列表
     *
     * @param bean
     */

    @Override
    public void onConnectCallBack(final SocketBean bean) {
        if (!App.getmSocketBeanList().contains(bean)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    App.getmSocketBeanList().add(bean);
                    mAdapter.notifyItemInserted(App.getmSocketBeanList().size());
                    LoadingDialogUtil.closeDialog();
                }
            });


        }
    }

    /**
     * 接收成功后的回调，如果是连接则显示消息
     *
     * @param bean
     */

    @Override
    public void onReceiveCallBack(final SocketBean bean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int size = App.getmSocketBeanList().size();
                for (int i = 0; i < size; i++) {
                    if (bean.equals(App.getmSocketBeanList().get(i))) {
                        App.getmSocketBeanList().get(i).setMessage(bean.getMessage());

                        mAdapter.notifyItemChanged(i);
                    }
                }
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
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), sOPENWIFI);
                break;


        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case sOPENWIFI:
                if (resultCode == RESULT_OK && NetWorkUtil.isWifiConnected(MainActivity.this)) {
                    App.sIP = NetIPUtil.getLocAddress();

                }
                break;
        }
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
