package com.example.asus.lantalk.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.adapter.MessageAdapter;
import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;
import com.example.asus.lantalk.service.ReceiveService;
import com.example.asus.lantalk.service.SendIntentService;
import com.example.asus.lantalk.utils.LoadingDialogUtil;
import com.example.asus.lantalk.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.asus.lantalk.constant.Constant.SEND_PEER_BEAN;

public class TalkActivity extends AppCompatActivity implements
        ReceiveService.OnReceiveListener,SendIntentService.OnSendListener{
     private EditText mEditText;
     private Toolbar mToolbar;
     private TextView mName;
     private RecyclerView mRvMsgContent;
     private Button mSendButton;
     private MessageAdapter mAdapter;

     private List<SocketBean> mBeanList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        initToolbar();
        initView();
        initListener();




    }
    private void initToolbar() {
        mToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setTitle("");

    }


    private void initView(){
        mName = findViewById(R.id.tv_name);
        mName.setText(getIntent().getStringExtra(SEND_PEER_BEAN));
        mRvMsgContent = findViewById(R.id.rv_msg_content);
        mEditText = findViewById(R.id.et_input);
        mSendButton  = findViewById(R.id.btn_send);
        mBeanList = new ArrayList<>();
        mAdapter = new MessageAdapter(mBeanList);
        mRvMsgContent.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRvMsgContent.setAdapter(mAdapter);
        SendIntentService.setSendListener(this);
        ReceiveService.setReceiveListener(this);
    }


    private void initListener(){
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (!TextUtils.isEmpty(mEditText.getText().toString())){
                   Intent intent = new Intent(TalkActivity.this, SendIntentService.class);
                   SocketBean socketBean = new SocketBean();
                   socketBean.setReceiveIP(mName.getText().toString());
                   socketBean.setMessage(mEditText.getText().toString());
                   socketBean.setSendIP(App.sIP);
                   socketBean.setSendName(App.sName);
                   socketBean.setTime(TimeUtil.getCurrentTime());
                   intent.setAction(Constant.ACTION_SEND_MSG);
                   socketBean.setStatus(Constant.RECEIVE);
                   socketBean.setType(Constant.MINE);
                   intent.putExtra(SEND_PEER_BEAN, socketBean);
                   startService(intent);
                   mBeanList.add(socketBean);
                   mAdapter.notifyDataSetChanged();
               }

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();

            }
            break;
            case R.id.menu_picture:{
                SocketBean socketBean = new SocketBean();
                socketBean.setSendName("mine");
                socketBean.setMessage(mEditText.getText().toString());
                socketBean.setTime("2018-3-3");
                socketBean.setType(Constant.PEER);
                mBeanList.add(socketBean);
                mAdapter.notifyDataSetChanged();
            }
            break;

        }
        return true;

    }


    @Override
    public void onReceiveCallBack(final SocketBean bean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bean.setType(Constant.PEER);
                mBeanList.add(bean);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onReceiceFailed() {

    }

    @Override
    public void OnFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TalkActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }




    public static void actionStart(Context context,String address){
        Intent intent = new Intent(context,TalkActivity.class);
        intent.putExtra(SEND_PEER_BEAN,address);
        context.startActivity(intent);
    }

}
