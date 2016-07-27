package com.example.uchihakirito.zhishu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;


import cn.bmob.v3.Bmob;

/**
 * Created by UchihaKirito on 2015/5/28.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getActivity(), "da21168170bcd694ec7d1391e417b263");
    }


}
