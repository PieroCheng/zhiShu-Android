package com.example.uchihakirito.zhishu;

import cn.bmob.v3.BmobObject;

/**
 * Created by Redfire on 2015/6/7.
 */
public class Comment extends BmobObject {
    private MyUser name;

    private String comment;
    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MyUser getName() {
        return name;
    }

    public void setName(MyUser name) {
        this.name = name;
    }
}

