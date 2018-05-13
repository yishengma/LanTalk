package com.example.asus.lantalk.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.example.asus.lantalk.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.asus.lantalk.constant.Constant.SEND_PEER_BEAN;
import static com.example.asus.lantalk.constant.Constant.sCHOOSEALBUM;

public class TalkActivity extends AppCompatActivity implements
        ReceiveService.OnReceiveListener,SendIntentService.OnSendListener{
     private EditText mEditText;
     private Toolbar mToolbar;
     private TextView mName;
     private RecyclerView mRvMsgContent;
     private Button mSendButton;
     private MessageAdapter mAdapter;

     private List<SocketBean> mBeanList;
     private SocketBean mSocketBean;
    private String mPhotoPath;//背景照的路径



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
                   mSocketBean = new SocketBean();
                   mSocketBean.setReceiveIP(mName.getText().toString());
                   mSocketBean.setMessage(mEditText.getText().toString());
                   mSocketBean.setSendIP(App.sIP);
                   mSocketBean.setSendName(App.sName);
                   mSocketBean.setTime(TimeUtil.getCurrentTime());
                   intent.setAction(Constant.ACTION_SEND_MSG);
                   mSocketBean.setStatus(Constant.RECEIVE);
                   mSocketBean.setType(Constant.MINE);
                   intent.putExtra(SEND_PEER_BEAN, mSocketBean);
                   startService(intent);

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
                if (ContextCompat.checkSelfPermission(TalkActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TalkActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/");
                    startActivityForResult(intent, sCHOOSEALBUM);
                }
            }
            break;

        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           switch (requestCode){
               case sCHOOSEALBUM :
                   if (requestCode==RESULT_OK){
                       if (Build.VERSION.SDK_INT >= 19) {
                           mPhotoPath = handleImageOnKitKat(data);
                       } else {
                           Uri uri = data.getData();
                           mPhotoPath = getmImageFilePath(uri,null);
                       }
                   }
           }
    }

    @Override
    public void onReceiveCallBack(final SocketBean bean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bean.setType(Constant.PEER);
                mBeanList.add(bean);
                mAdapter.notifyDataSetChanged();
                mRvMsgContent.smoothScrollToPosition(mAdapter.getItemCount()-1);

            }
        });
    }

    @Override
    public void onReceiceFailed() {

    }

    @Override
    public void onSendSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBeanList.add(mSocketBean);
                mAdapter.notifyDataSetChanged();
                mRvMsgContent.smoothScrollToPosition(mAdapter.getItemCount()-1);
                mEditText.setText("");
            }
        });
    }

    @Override
    public void onSendFail() {
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


    @TargetApi(19)
    public String handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                mPhotoPath = getmImageFilePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                mPhotoPath = getmImageFilePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            mPhotoPath = getmImageFilePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            mPhotoPath = uri.getPath();
        }

        return mPhotoPath;


    }

    public String getmImageFilePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


}
