package com.example.uchihakirito.zhishu;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Redfire on 2015/6/19.
 */
public class CommentAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<Comment> mlist;
    private Context mcontext;

    CommentAdapter(Context context,List<Comment> list ){
        mlist =list;
        mcontext =context;
        mInflater =LayoutInflater.from(context);

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
            convertView = mInflater.inflate(R.layout.item_comment, null);
            viewHolder.avator = (ImageView) convertView.findViewById(R.id.iv_item_comment_avator);
            viewHolder.user = (TextView) convertView.findViewById(R.id.tv_item_comment_user);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_item_comment_time);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.tv_item_comment_describe);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment =mlist.get(position);
        viewHolder.user.setText(comment.getName().getUsername());
        viewHolder.describe.setText(comment.getComment());
        viewHolder.time.setText(comment.getCreatedAt());
        String avatarUrl = null;
        avatarUrl = comment.getName().getAvatar().getFileUrl(mcontext);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(avatarUrl,viewHolder.avator,options );

        
        return convertView;
    }
    class ViewHolder{
        public ImageView avator;
        public TextView user;
        public TextView describe;
        public TextView time;

    }
}
