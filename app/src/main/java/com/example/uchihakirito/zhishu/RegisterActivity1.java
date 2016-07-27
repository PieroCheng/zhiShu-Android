package com.example.uchihakirito.zhishu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by UchihaKirito on 2015/6/10.
 */
public class RegisterActivity1 extends Activity implements View.OnClickListener {

    EditText et_username, et_password, et_password_again;
    Button btn_next;
    String username = "";
    String password = "";
    String password_again = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "da21168170bcd694ec7d1391e417b263");
        setContentView(R.layout.register_layout_1);
        init();
    }

    public void init() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_again = (EditText) findViewById(R.id.et_password_again);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);


    }

    Toast mToast;

    public void ShowToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text,
                                Toast.LENGTH_LONG);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });

        }
    }

    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mToast == null) {
                    mToast = Toast.makeText(getApplicationContext(), resId,
                            Toast.LENGTH_LONG);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        final MyUser bu = new MyUser();
                username = et_username.getText().toString();
        password = et_password.getText().toString();
        password_again = et_password_again.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ShowToast(R.string.toast_error_username_null);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.toast_error_password_null);
            return;
        }
        if (!password_again.equals(password)) {
            ShowToast(R.string.toast_error_comfirm_password);
            return;
        }

        boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
        if (!isNetConnected) {
            ShowToast(R.string.network_tips);
            return;
        }

        bu.setUsername(username);
        bu.setPassword(password);


        bu.signUp(this, new SaveListener() {

            @Override
            public void onSuccess() {
                Intent intent = new Intent(RegisterActivity1.this, RegisterActivity2.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int code, String msg) {

                Toast tst = Toast.makeText(RegisterActivity1.this, "失败呢~" + msg, Toast.LENGTH_SHORT);

                tst.show();
            }
        });


    }
}
