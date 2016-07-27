package com.example.uchihakirito.zhishu;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;


public class MainActivity extends Activity implements View.OnClickListener{

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;


    private UserFragment userFragment;
    private View home_layout;
    private View search_layout;


    private View user_layout;
    private ImageView homeImage;
    private ImageView searchImage;


    private ImageView userImage;




    private TextView tv_actionbar;
    private Button btn_add;


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "da21168170bcd694ec7d1391e417b263");
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        initViews();
        fragmentManager = getFragmentManager();

        setTabSelection(0);
    }

    public void initViews() {
        home_layout = findViewById(R.id.home_layout);
        search_layout = findViewById(R.id.search_layout);
        user_layout = findViewById(R.id.user_layout);
        homeImage = (ImageView) findViewById(R.id.home_image);
        searchImage = (ImageView) findViewById(R.id.search_image);
        userImage = (ImageView) findViewById(R.id.user_image);
        tv_actionbar = (TextView) findViewById(R.id.tv_actionbar);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        home_layout.setOnClickListener(this);
        search_layout.setOnClickListener(this);
        user_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.home_layout:

                setTabSelection(0);
                break;
            case R.id.search_layout:

                setTabSelection(1);
                break;

            case R.id.user_layout:
                MyUser currentUser = BmobUser.getCurrentUser(this, MyUser.class);
                if (currentUser == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, 1);

                } else {
                    setTabSelection(4);
                }
                break;
            case R.id.btn_add:

                showPopupWindow(v);

                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
            MyUser currentUser = BmobUser.getCurrentUser(this, MyUser.class);
            if (currentUser == null) {
                setTabSelection(0);
            } else {


                clearSelection();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                hideFragments(transaction);
                tv_actionbar.setText("用户");
                userImage.setImageResource(R.drawable.ic_person_black_selected);
                userFragment = new UserFragment();
                transaction.add(R.id.content, userFragment);
                transaction.commit();
            }
                break;
            case 2:
            setTabSelection(0);
            homeFragment.getData();


        }
    }

    public void setTabSelection(int index) {

        clearSelection();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideFragments(transaction);
        switch (index) {
            case 0:
                tv_actionbar.setText("图书");
                homeImage.setImageResource(R.drawable.ic_explore_black_selected);
                if (homeFragment == null) {

                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);


                } else {


                    transaction.show(homeFragment);
                }
                break;
            case 1:
                tv_actionbar.setText("搜索");
                searchImage.setImageResource(R.drawable.ic_find_in_page_black_selected);
                if (searchFragment == null) {

                    searchFragment = new SearchFragment();
                    transaction.add(R.id.content, searchFragment);

                } else {

                    transaction.show(searchFragment);
                }
                break;

            case 4:
            default:
                tv_actionbar.setText("用户");
                userImage.setImageResource(R.drawable.ic_person_black_selected);
                if (userFragment == null) {

                    userFragment = new UserFragment();
                    transaction.add(R.id.content, userFragment);

                } else {


                    transaction.show(userFragment);
                }
                break;
        }
        transaction.commit();
    }


    private void clearSelection() {
        homeImage.setImageResource(R.drawable.ic_explore_black_unselected);

        searchImage.setImageResource(R.drawable.ic_find_in_page_black_unselected);
        userImage.setImageResource(R.drawable.ic_person_black_unselected);

    }


    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (searchFragment != null) {
            transaction.hide(searchFragment);
        }


        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.pop_window, null);
        // 设置按钮的点击事件
        TextView tv_newbokk = (TextView) contentView.findViewById(R.id.tv_newbook);
        TextView tv_logout = (TextView) contentView.findViewById(R.id.tv_logout);



            final PopupWindow popupWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindow.setTouchable(true);

            popupWindow.setTouchInterceptor(new View.OnTouchListener()

            {

                @Override
                public boolean onTouch (View v, MotionEvent event){


                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
            }

            );

            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
            // 我觉得这里是API的一个bug
            popupWindow.setBackgroundDrawable(

            getResources()

            .

            getDrawable(
                    R.drawable.selectmenu_bg_downward)

            );

            // 设置好参数之后再show
            popupWindow.showAsDropDown(view, 0, 25);


        tv_newbokk.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                                popupWindow.dismiss();
                                              MyUser currentUser = BmobUser.getCurrentUser(MainActivity.this, MyUser.class);
                                              if (currentUser == null) {
                                                  Toast.makeText(MainActivity.this, "请先登录哟", Toast.LENGTH_SHORT).show();
                                                  Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                                                  startActivityForResult(intent, 1);
                                              } else {
                                                  Intent intent = new Intent(MainActivity.this, NewBookActivity.class);
                                                  startActivityForResult(intent, 2);
                                              }
                                          }
                                      }

        );
        tv_logout.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View v) {
                                             popupWindow.dismiss();
                                             BmobUser.logOut(MainActivity.this);   //清除缓存用户对象
                                             BmobUser currentUser = BmobUser.getCurrentUser(MainActivity.this); // 现在的currentUser是null了
                                             if (currentUser == null) {
                                                 Toast.makeText(MainActivity.this, "退出登录成功!", Toast.LENGTH_SHORT).show();
                                                 setTabSelection(0);

                                             } else {
                                                 Toast.makeText(MainActivity.this, "退出登录失败!请检查网络连接是否正常!", Toast.LENGTH_SHORT).show();
                                             }

                                         }
                                     }

        );
        }


}
