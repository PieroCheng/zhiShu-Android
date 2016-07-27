package com.example.uchihakirito.zhishu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by UchihaKirito on 2015/6/10.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    EditText L_username, L_password;
    Button btn_login;
    TextView btn_intent_register;
    Toast mToast;

    String username = "";
    String password = "";

    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "da21168170bcd694ec7d1391e417b263");
        setContentView(R.layout.login_layout);
        init();
    }



    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mToast == null) {
                    mToast = Toast.makeText(LoginActivity.this.getApplicationContext(), resId,
                            Toast.LENGTH_LONG);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        });
    }

    public void init() {
        L_username = (EditText) findViewById(R.id.L_username);
        L_password = (EditText) findViewById(R.id.L_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_intent_register = (TextView) findViewById(R.id.btn_intent_register);
        btn_login.setOnClickListener(this);
        btn_intent_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_intent_register) {
            Intent intent = new Intent(LoginActivity.this,
                    RegisterActivity1.class);
            startActivity(intent);
        } else {
            boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
            if (!isNetConnected) {
                ShowToast(R.string.network_tips);
                return;
            }
            login();
        }
    }


    private void login() {
        final BmobUser bu2 = new BmobUser();
        username = L_username.getText().toString();
        password = L_password.getText().toString();
        bu2.setUsername(username);
        bu2.setPassword(password);
        bu2.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

                Toast tst = Toast.makeText(LoginActivity.this, "登陆成功!", Toast.LENGTH_SHORT);


                tst.show();
                finish();


            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub

                Toast tst = Toast.makeText(LoginActivity.this, "登录失败!" + msg, Toast.LENGTH_SHORT);

                tst.show();
            }
        });
    }
}

