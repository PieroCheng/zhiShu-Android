package com.example.uchihakirito.zhishu;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


/**
 * Created by Redfire on 2015/6/7.
 */
public class MyUser extends BmobUser {
    private String school;
    private BmobFile avatar;
    private String mobilePhoneNumber;
    private String sex;



    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {this.sex = sex;}

    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public BmobFile getAvatar() {
        return this.avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public String getMobilePhoneNumber() {
        return this.mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {this.mobilePhoneNumber = mobilePhoneNumber;}
}
