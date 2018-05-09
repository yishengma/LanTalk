package com.example.asus.lantalk.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.entity.PeerBean;
import com.example.asus.lantalk.service.SendIntentService;
import com.example.asus.lantalk.thread.ServerThread;
import com.example.asus.lantalk.utils.CrazyitMap;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.asus.lantalk.constant.Constant.ACTION_SEND_MSG;
import static com.example.asus.lantalk.constant.Constant.SEND_PEER_BEAN;
import static com.example.asus.lantalk.constant.Constant.SERVER_PORT;

public class TalkActivity extends AppCompatActivity {
     private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        mEditText = findViewById(R.id.et_input);
        final String s = getIntent().getStringExtra(SEND_PEER_BEAN);
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeerBean peerBean = new PeerBean();
                peerBean.setPeerIP(s);
                peerBean.setMessage(mEditText.getText().toString());
                Intent intent = new Intent(TalkActivity.this, SendIntentService.class);
                intent.setAction(ACTION_SEND_MSG);
                intent.putExtra(SEND_PEER_BEAN,peerBean);
                startService(intent);
            }
        });



    }

    public static void actionStart(Context context,String address){
        Intent intent = new Intent(context,TalkActivity.class);
        intent.putExtra(SEND_PEER_BEAN,address);
        context.startActivity(intent);
    }

}
