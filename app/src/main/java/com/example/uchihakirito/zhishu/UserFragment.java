package com.example.uchihakirito.zhishu;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by UchihaKirito on 2015/5/11.
 */
public class UserFragment extends Fragment implements ReflashListView.IReflashListener {


    private ReflashListView lv_tab1;
    private ReflashListView lv_tab2;

    private ImageView avator;
    private TextView tv_username;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getActivity(), "da21168170bcd694ec7d1391e417b263");



    }
    @Override
    public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);

        if(hidden){

        }
        else{

                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.tab1_layout, null);
                View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.tab2_layout, null);
                lv_tab1 = (ReflashListView) view1.findViewById(R.id.lv_tab1);
                getData1();
                lv_tab2 = (ReflashListView) view2.findViewById(R.id.lv_tab2);
                getData2();



        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View user_layout = inflater.inflate(R.layout.user_layout,
                container, false);
        avator = (ImageView) user_layout.findViewById(R.id.iv_user_avatar);
        tv_username = (TextView) user_layout.findViewById(R.id.tv_username);
        MyUser currentUser= BmobUser.getCurrentUser(getActivity(),MyUser.class);
        if (currentUser == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {

           getUserInfo();

            ViewPager pager = null;
            PagerTabStrip tabStrip = null;
            final ArrayList<View> viewContainter = new ArrayList<View>();
            final ArrayList<String> titleContainer = new ArrayList<String>();


            pager = (ViewPager) user_layout.findViewById(R.id.viewpager);
            tabStrip = (PagerTabStrip) user_layout.findViewById(R.id.tabstrip);
            //取消tab下面的长横线
            tabStrip.setDrawFullUnderline(false);

            //设置当前tab页签的下划线颜色
            tabStrip.setTabIndicatorColor(Color.GREEN);
            tabStrip.setTextSpacing(200);

            View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.tab1_layout, null);
            View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.tab2_layout, null);
            lv_tab1 = (ReflashListView) view1.findViewById(R.id.lv_tab1);
            getData1();
            lv_tab2 = (ReflashListView) view2.findViewById(R.id.lv_tab2);
            getData2();
            //viewpager开始添加view
            viewContainter.add(view1);
            viewContainter.add(view2);

            //页签项
            titleContainer.add("我发布的");
            titleContainer.add("我的收藏");


            pager.setAdapter(new PagerAdapter() {

                //viewpager中的组件数量
                @Override
                public int getCount() {
                    return viewContainter.size();
                }

                //滑动切换的时候销毁当前的组件
                @Override
                public void destroyItem(ViewGroup container, int position,
                                        Object object) {
                    ((ViewPager) container).removeView(viewContainter.get(position));
                }

                //每次滑动的时候生成的组件
                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    ((ViewPager) container).addView(viewContainter.get(position));
                    return viewContainter.get(position);
                }

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }

                @Override
                public int getItemPosition(Object object) {
                    return super.getItemPosition(object);
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return titleContainer.get(position);
                }
            });


        lv_tab1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        lv_tab2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        }
            return user_layout;
        }
        @Override
        public void onReflash () {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    getData1();
//                    getData2();

                    //通知listview 刷新数据完毕；
                    lv_tab1.reflashComplete();
                    lv_tab2.reflashComplete();
                }
            }, 2000);
        }

    public void getData1() {
        final BmobQuery<Book> query = new BmobQuery<Book>();
        query.order("-createdAt");
        MyUser user = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        query.addWhereEqualTo("user", new BmobPointer(user));
        query.include("user");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        query.findObjects(getActivity(), new FindListener<Book>() {

            @Override
            public void onSuccess(List<Book> books) {
                // TODO Auto-generated method stub
                lv_tab1.setInterface(UserFragment.this);

                HomeAdapter ha=new HomeAdapter(getActivity(),books);
                lv_tab1.setAdapter(ha);
           ha.notifyDataSetChanged();

            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub
//                Toast.makeText(getActivity(), arg0, Toast.LENGTH_SHORT).show();

            }

        });
    }
    public void getData2() {
        final BmobQuery<Book> query = new BmobQuery<Book>();
        query.order("-createdAt");

        query.addWhereEqualTo("iscollection", true);
        query.include("user");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        query.findObjects(getActivity(), new FindListener<Book>() {

            @Override
            public void onSuccess(List<Book> books) {
                // TODO Auto-generated method stub
                lv_tab2.setInterface(UserFragment.this);



                HomeAdapter ha=new HomeAdapter(getActivity(),books);
                lv_tab2.setAdapter(ha);
                ha.notifyDataSetChanged();


            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub
//                Toast.makeText(getActivity(), arg0, Toast.LENGTH_SHORT).show();

            }

        });
    }
    public void getUserInfo(){
        String avatarUrl = null;
        MyUser currentUser= BmobUser.getCurrentUser(getActivity(), MyUser.class);

        if (currentUser.getAvatar() == null){
            avator.setImageResource(R.drawable.user_icon_default_main);


        }
        else {

            avatarUrl = currentUser.getAvatar().getFileUrl(getActivity());

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true)
                    .cacheInMemory(true)
                    .showImageOnFail(R.drawable.user_icon_default_main)
                    .showImageForEmptyUri(R.drawable.user_icon_default_main)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(avatarUrl, avator, options);
        }

        tv_username.setText(currentUser.getUsername());
}
}

