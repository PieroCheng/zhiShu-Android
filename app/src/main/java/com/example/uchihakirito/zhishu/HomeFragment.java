package com.example.uchihakirito.zhishu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.example.uchihakirito.zhishu.ReflashListView.IReflashListener;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by UchihaKirito on 2015/5/11.
 */
public class HomeFragment extends Fragment implements IReflashListener {

private ReflashListView lv_home;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getActivity(), "da21168170bcd694ec7d1391e417b263");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View home_layout = inflater.inflate(R.layout.home_layout,
                container, false);




        lv_home = (ReflashListView) home_layout.findViewById(R.id.lv_home);
        getData();

        lv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Adapter adapter = parent.getAdapter();
                Book book = (Book) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", book);
                intent.putExtra("book", bundle);
                startActivity(intent);


            }
        });

        return home_layout;

    }


    @Override
    public void onReflash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                getData();
                //通知listview 刷新数据完毕；
                lv_home.reflashComplete();
            }
        }, 2000);
    }

    public void getData() {
        final BmobQuery<Book> query = new BmobQuery<Book>();
        query.order("-createdAt");
        query.include("user");
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(getActivity(), new FindListener<Book>() {

            @Override
            public void onSuccess(List<Book> books) {
                // TODO Auto-generated method stub
                lv_home.setInterface(HomeFragment.this);


                lv_home.setAdapter(new HomeAdapter(getActivity(), books));


            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub
                //     Toast.makeText(getActivity(), arg0, Toast.LENGTH_SHORT).show();

            }


        });
    }

}

