package com.example.uchihakirito.zhishu;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by UchihaKirito on 2015/6/1.
 */
public class Book extends BmobObject {
    private String subject;
    private String describe;
    private MyUser user;
    private String price;

    private List<BmobFile> photos;
    private String grade;
    private Boolean iscollection=false;
    private ArrayList<String> imageUrls;



    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }



    public List<BmobFile> getPhotos() {
        return photos;
    }

    public void setPhotos(List<BmobFile> photos) {
        this.photos = photos;
    }

    public Boolean getIscollection() {
        return iscollection;
    }

    public void setIscollection(Boolean iscollection) {
        this.iscollection = iscollection;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }







    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
