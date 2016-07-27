package com.example.uchihakirito.zhishu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Redfire on 2015/6/19.
 */
public class HomeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Book> mlist;
    private Context mcontext;



    HomeAdapter(Context context, List<Book> list) {
        mlist = list;
        mcontext = context;
        mInflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_list, null);
            viewHolder.subject = (TextView) convertView.findViewById(R.id.tv_subject);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.tv_describe);
            viewHolder.price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.grade = (TextView) convertView.findViewById(R.id.tv_grade);
            viewHolder.user = (TextView) convertView.findViewById(R.id.tv_user);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.avator = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.gridView = (NoScrollGridView) convertView.findViewById(R.id.gridview);
            viewHolder.school = (TextView) convertView.findViewById(R.id.tv_school);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.tv_phone);

            viewHolder.iv_collection = (ImageView) convertView.findViewById(R.id.iv_collection);




            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Book book = mlist.get(position);
        viewHolder.subject.setText(book.getSubject());
        viewHolder.describe.setText(book.getDescribe());
        viewHolder.time.setText(book.getCreatedAt());
        viewHolder.price.setText(book.getPrice());
        viewHolder.grade.setText(book.getGrade());
        viewHolder.school.setText(book.getUser().getSchool());
        viewHolder.user.setText(book.getUser().getUsername());
        viewHolder.phone.setText(book.getUser().getMobilePhoneNumber());



        String avatarUrl = null;
        avatarUrl = book.getUser().getAvatar().getFileUrl(mcontext);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(avatarUrl, viewHolder.avator, options);

        final ArrayList<String> imageUrls = book.getImageUrls();

        if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
            viewHolder.gridView.setVisibility(View.GONE);
        } else {

            viewHolder.gridView.setAdapter(new NoScrollGridAdapter(imageUrls,mcontext));

        }


        viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrower(position, imageUrls);
            }
        });


            final ImageView collection = viewHolder.iv_collection;

        if (book.getIscollection() == true) {
            collection.setImageResource(R.drawable.ic_collection_selected);

        } else {
            collection.setImageResource(R.drawable.ic_collection_unselected);
        }


        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.getIscollection() == false) {


                    book.setIscollection(true);

                    book.update(mcontext, book.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            collection.setImageResource(R.drawable.ic_collection_selected);
                            Toast.makeText(mcontext, "收藏成功！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                } else {


                    book.setIscollection(false);
                    book.update(mcontext, book.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            collection.setImageResource(R.drawable.ic_collection_unselected);
                            Toast.makeText(mcontext, "取消收藏！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });

                }
            }
        });





        return convertView;
    }

    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mcontext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mcontext.startActivity(intent);
    }

    static final class ViewHolder {
        public TextView subject;
        public TextView describe;
        public TextView grade;
        public TextView price;
        public TextView user;
        public TextView time;
        public TextView school;
        public ImageView avator;

        public NoScrollGridView gridView;
        public TextView phone;

        public ImageView iv_collection;




    }

}




