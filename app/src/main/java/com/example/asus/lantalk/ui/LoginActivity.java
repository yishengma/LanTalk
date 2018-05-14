package com.example.asus.lantalk.ui;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.lantalk.R;
import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.utils.ScanDeviceUtil;

public class LoginActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private Button mLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_login);
        mNameEditText = findViewById(R.id.et_name);
        mLoginButton = findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mNameEditText.getText().toString())){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                }else {
                    App.sName = mNameEditText.getText().toString();
                    App.sIP = ScanDeviceUtil.getLocalIPAddress();
                    Log.e("TAG", "onClick: "+App.sIP );
                    MainActivity.actionStart(LoginActivity.this);
                }
            }
        });


    }
}
