package com.example.uchihakirito.zhishu;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by UchihaKirito on 2015/5/11.
 */
public class SearchFragment extends Fragment implements ReflashListView.IReflashListener {
    private ReflashListView lv_search;

    private EditText et_search;
    private ImageView iv_search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getActivity(), "da21168170bcd694ec7d1391e417b263");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View search_layout = inflater.inflate(R.layout.search_layout,
                container, false);

        iv_search = (ImageView) search_layout.findViewById(R.id.iv_search);
        et_search = (EditText) search_layout.findViewById(R.id.et_search);
        lv_search= (ReflashListView) search_layout.findViewById(R.id.lv_search);

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_search.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "请输入要搜索的内容!", Toast.LENGTH_SHORT).show();
                } else {

                    getSearchDate();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        return search_layout;
    }
    @Override
    public void onReflash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                getSearchDate();
                //通知listview 刷新数据完毕；
                lv_search.reflashComplete();
            }
        }, 2000);
    }


    private void getSearchDate(){
        final BmobQuery<Book> query = new BmobQuery<Book>();
        query.addWhereContains("subject", et_search.getText().toString());
        query.order("-createdAt");
        query.include("user");
       query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(getActivity(), new FindListener<Book>() {

            @Override
            public void onSuccess(List<Book> books) {
                // TODO Auto-generated method stub
                if (books.size()==0){

                    getSearchDate2();
                }
                else {
                    lv_search.setInterface(SearchFragment.this);


                    lv_search.setAdapter(new HomeAdapter(getActivity(), books));
                }

            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub


            }

        });

    }
    private void getSearchDate2() {
        final BmobQuery<Book> query = new BmobQuery<Book>();
        query.addWhereContains("grade", et_search.getText().toString());
        query.order("-createdAt");
        query.include("user");
//        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(getActivity(), new FindListener<Book>() {

            @Override
            public void onSuccess(List<Book> books) {
                // TODO Auto-generated method stub
                if (books.size()==0){

                    Toast.makeText(getActivity(), "并没有找到= =", Toast.LENGTH_SHORT).show();
                }
                else {
                    lv_search.setInterface(SearchFragment.this);


                    lv_search.setAdapter(new HomeAdapter(getActivity(), books));
                }

            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub


            }

        });
    }
}
