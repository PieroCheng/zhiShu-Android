package com.example.uchihakirito.zhishu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Redfire on 2015/6/20.
 */
public class CommentActivity extends Activity implements ReflashListView.IReflashListener{
    private ReflashListView lv_comment;

    private EditText et_commit;
    private LinearLayout ll_comment;
    private Button bt_send;
    private ImageView iv_avatar_comment;
    private TextView tv_user_commnet;
    private TextView tv_time_comment;
    private TextView tv_describe_comment;
    private TextView tv_price_comment;
    private ImageView iv_photo_comment;
    private TextView tv_grade_comment;
    private TextView tv_subject_comment;
    private TextView tv_school_comment;
    private TextView iv_nocomment;
    private TextView tv_back_title;
    private Button btn_back;
    private ImageView iv_collection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        Bmob.initialize(this, "da21168170bcd694ec7d1391e417b263");


        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("book");
        final Book book1 = (Book) bundle.getSerializable("book");
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view=mInflater.inflate(R.layout.item_list, null);

        lv_comment = (ReflashListView) findViewById(R.id.lv_comment);


        bt_send = (Button) findViewById(R.id.bt_send);
        et_commit = (EditText) findViewById(R.id.et_commit);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);

        iv_collection = (ImageView) view.findViewById(R.id.iv_collection);
        iv_avatar_comment = (ImageView) view.findViewById(R.id.iv_avatar);
        tv_user_commnet = (TextView) view.findViewById(R.id.tv_user);
        tv_time_comment = (TextView) view.findViewById(R.id.tv_time);
        tv_describe_comment = (TextView) view.findViewById(R.id.tv_describe);
        tv_price_comment = (TextView) view.findViewById(R.id.tv_price);

        tv_grade_comment = (TextView) view.findViewById(R.id.tv_grade);
        tv_subject_comment = (TextView)view. findViewById(R.id.tv_subject);
        tv_school_comment = (TextView) view.findViewById(R.id.tv_school);
        iv_nocomment = (TextView) findViewById(R.id.tv_nocomment);
        tv_back_title = (TextView) findViewById(R.id.tv_back_title);
        btn_back = (Button) findViewById(R.id.btn_back);


        tv_user_commnet.setText(book1.getUser().getUsername());
        tv_time_comment.setText(book1.getCreatedAt());
        tv_describe_comment.setText(book1.getDescribe());
        tv_price_comment.setText(book1.getPrice());
        tv_grade_comment.setText(book1.getGrade());
        tv_subject_comment.setText(book1.getSubject());
        tv_school_comment.setText(book1.getUser().getSchool());

        if (book1.getIscollection() == true) {
            iv_collection.setImageResource(R.drawable.ic_collection_selected);

        } else {
            iv_collection.setImageResource(R.drawable.ic_collection_unselected);
        }

        String avatarUrl = null;
        avatarUrl = book1.getUser().getAvatar().getFileUrl(this);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .showImageOnFail(R.drawable.user_icon_default_main)
                .showImageForEmptyUri(R.drawable.user_icon_default_main)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(avatarUrl, iv_avatar_comment, options);

        tv_back_title.setText("评论");

        lv_comment.addHeaderView(view,null,false);
        getData();
        iv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book1.getIscollection() == false) {

                    book1.setIscollection(true);
                    book1.update(CommentActivity.this, book1.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            iv_collection.setImageResource(R.drawable.ic_collection_selected);
                            Toast.makeText(CommentActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                } else {
                    book1.setIscollection(false);
                    book1.update(CommentActivity.this, book1.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            iv_collection.setImageResource(R.drawable.ic_collection_unselected);
                            Toast.makeText(CommentActivity.this, "取消收藏！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        bt_send.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {


                                           MyUser user = BmobUser.getCurrentUser(CommentActivity.this, MyUser.class);
                                           Book book = new Book();
                                           book.setObjectId(book1.getObjectId());
                                           final Comment comment = new Comment();
                                           comment.setComment(et_commit.getText().toString());
                                           comment.setName(user);
                                           comment.setBook(book);


                                           comment.save(CommentActivity.this, new SaveListener() {

                                               @Override
                                               public void onSuccess() {


                                                   et_commit.setText("");
                                                   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                   imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                                   Toast.makeText(CommentActivity.this, "发送成功!", Toast.LENGTH_SHORT).show();

                                                   final BmobQuery<Comment> query = new BmobQuery<Comment>();
                                                   Book book = new Book();
                                                   book.setObjectId(book1.getObjectId());
                                                   query.addWhereEqualTo("book", new BmobPointer(book));
                                                   query.order("-createdAt");
                                                   query.include("name");
                                                   query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
                                                   query.findObjects(CommentActivity.this, new FindListener<Comment>() {

                                                       @Override
                                                       public void onSuccess(List<Comment> comments) {
                                                           // TODO Auto-generated method stub


                                                           lv_comment.setAdapter(new CommentAdapter(CommentActivity.this, comments));

                                                       }

                                                       @Override
                                                       public void onError(int code, String arg0) {
                                                           // TODO Auto-generated method stub
                                                       }
                                                   });


                                               }

                                               @Override
                                               public void onFailure(int code, String arg0) {
                                                   Toast.makeText(CommentActivity.this, "发送失败,请检查网络是否正常!", Toast.LENGTH_SHORT).show();

                                               }
                                           });
                                       }


                                   }

        );



        lv_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_item_comment_user = (TextView) view.findViewById(R.id.tv_item_comment_user);
                et_commit.setText("回复" + tv_item_comment_user.getText().toString() + ":");
                et_commit.setSelection(tv_item_comment_user.getText().toString().length()+3);
                et_commit.setFocusableInTouchMode(true);
                et_commit.requestFocus();

            }
        });

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
                lv_comment.reflashComplete();
            }
        }, 2000);
    }
    private void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("book");
        final Book book1 = (Book) bundle.getSerializable("book");
        final BmobQuery<Comment> query = new BmobQuery<Comment>();
        Book book = new Book();
        book.setObjectId(book1.getObjectId());
        query.addWhereEqualTo("book", new BmobPointer(book));
        query.order("-createdAt");
        query.include("name");

        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(this, new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> comments) {
                // TODO Auto-generated method
                if (comments.isEmpty()) {
                    iv_nocomment.setVisibility(View.VISIBLE);


                }
                lv_comment.setInterface(CommentActivity.this);
                lv_comment.setAdapter(new CommentAdapter(CommentActivity.this, comments));

            }

            @Override
            public void onError(int code, String arg0) {
                // TODO Auto-generated method stub


            }

        });

    }
}
