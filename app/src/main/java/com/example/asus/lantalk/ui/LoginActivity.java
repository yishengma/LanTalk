package com.example.asus.lantalk.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.lantalk.R;
import com.example.asus.lantalk.adapter.ProfilePhotoAdapter;
import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.listener.GalleryPageTransformer;

import com.example.asus.lantalk.service.BroadcastService;
import com.example.asus.lantalk.service.ReceiveService;
import com.example.asus.lantalk.utils.NetWorkUtil;
import com.example.asus.lantalk.utils.ProfilePicturePickUtil;
import com.example.asus.lantalk.utils.NetIPUtil;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.asus.lantalk.app.App.sProfilePicture;
import static com.example.asus.lantalk.constant.Constant.sOPENWIFI;

public class LoginActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private Button mLoginButton;
    private CircleImageView mCircleImageView;
    private Dialog mPickDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();


    }

    private void initView() {
        requestWindowFeature(1);
        // if (Build.VERSION.SDK_INT >= 19)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.activity_login);
        mNameEditText = findViewById(R.id.et_input);
        mLoginButton = findViewById(R.id.btn_login);
        mCircleImageView = findViewById(R.id.iv_head_portrait);


    }

    private void initListener() {
        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString();


                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(LoginActivity.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(App.sIP) && !NetWorkUtil.isWifiConnected(LoginActivity.this)) {

                    Toast.makeText(LoginActivity.this, "请先接入局域网！", Toast.LENGTH_SHORT).show();

                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), sOPENWIFI);
                    return;

                }

                App.sName = name;
                App.sIP = NetIPUtil.getLocAddress();

                startService();

                MainActivity.actionStart(LoginActivity.this);
                finish();


            }
        });
    }

    /**
     * 开启wife 回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case sOPENWIFI:
                if (resultCode == RESULT_OK && NetWorkUtil.isWifiConnected(LoginActivity.this)) {
                    App.sIP = NetIPUtil.getLocAddress();

                }
                break;
        }
    }

    /**
     * 启动接收的服务和线程
     */
    private void startService() {
        Intent receiveIntent = new Intent(getApplicationContext(), ReceiveService.class);
        startService(receiveIntent);
        Intent broadcastIntent = new Intent(getApplicationContext(), BroadcastService.class);
        startService(broadcastIntent);
    }


    public void createDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_pick_head, null);
        initViewPager(view);
        initDialog(view);
    }

    private void initViewPager(View view) {
        final ViewPager viewPager = view.findViewById(R.id.vp_head_portrait);
        viewPager.setPageMargin(-20);
        viewPager.setOffscreenPageLimit(3);
        ProfilePhotoAdapter adapter = new ProfilePhotoAdapter(this);
        adapter.setOnClickListener(new ProfilePhotoAdapter.OnClickListener() {
            @Override
            public void OnClick(int position) {
                mPickDialog.dismiss();
                sProfilePicture = position;
                Glide.with(LoginActivity.this).load(ProfilePicturePickUtil.getImageDrawable(position)).into(mCircleImageView);
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(false, new GalleryPageTransformer());
        viewPager.setCurrentItem(sProfilePicture, false);
    }

    private void initDialog(View view) {
        LinearLayout layout = view.findViewById(R.id.linear);
        mPickDialog = new Dialog(this, R.style.PickDialogStyle);
        mPickDialog.setCancelable(true);
        mPickDialog.setCanceledOnTouchOutside(true);
        mPickDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        Window window = mPickDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickDialog.dismiss();
            }
        });
        mPickDialog.show();
    }

}
