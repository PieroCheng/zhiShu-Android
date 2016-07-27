package com.example.uchihakirito.zhishu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.utils.BmobLog;
import com.example.uchihakirito.zhishu.utils.App;
import com.example.uchihakirito.zhishu.utils.DemoUtils;
import com.example.uchihakirito.zhishu.utils.SharedpreferencesUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * Created by UchihaKirito on 2015/6/17.
 */
public class RegisterActivity2 extends Activity implements View.OnClickListener {
    Button btn_register;
    ImageView im_avatar, ic_set_avatar, ic_set_sex, ic_set_school;
    EditText et_phone;
    TextView tv_set_sex, tv_set_school;
    private SharedpreferencesUtil sharedpreferencesUtil;
    private ImageView currentTackPictureImagView;
    int SELECT_PIC_KITKAT = 1001;
    int SELECT_PIC = 1002;
    int TAKE_BIG_PICTURE = 1003;
    int CROP_BIG_PICTURE = 1004;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "da21168170bcd694ec7d1391e417b263");
        setContentView(R.layout.register_layout_2);
        init();
    }

    public void init() {
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        im_avatar = (ImageView) findViewById(R.id.im_avatar);
        ic_set_avatar = (ImageView) findViewById(R.id.ic_set_avatar);
        ic_set_avatar.setOnClickListener(this);//增加的设置头像按钮
        ic_set_sex = (ImageView) findViewById(R.id.ic_set_sex);
        ic_set_sex.setOnClickListener(this);
        ic_set_school = (ImageView) findViewById(R.id.ic_set_school);
        ic_set_school.setOnClickListener(this);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_set_sex = (TextView) findViewById(R.id.tv_set_sex);
        tv_set_school = (TextView) findViewById(R.id.tv_set_school);
        sharedpreferencesUtil = new SharedpreferencesUtil(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_set_avatar:
                showAvatarChooseDialog();
                break;
            case R.id.ic_set_sex:
                showSexChooseDialog();
                break;
            case R.id.ic_set_school:
                showSchoolChooseDialog();
                break;
            //case R.id.btn_register:
            //   mobilePhone();
            //   break;
        }
    }


    Toast mToast;

    public void ShowToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text,
                                Toast.LENGTH_LONG);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });

        }
    }

    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mToast == null) {
                    mToast = Toast.makeText(getApplicationContext(), resId,
                            Toast.LENGTH_LONG);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        });
    }

    String[] avatar = new String[]{"启动相机", "选择相册"};

    private void showAvatarChooseDialog() {
        new AlertDialog.Builder(this).
                setTitle("选择上传图片的方式").
                setIcon(android.R.drawable.ic_dialog_info).
                setSingleChoiceItems(avatar, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                BmobLog.i("点击的是" + avatar[which]);
                                updateAvatar(which);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }

    private void updateAvatar(int which) {
        switch (which) {
            case 0:
                takePhoto();
                break;
            case 1:
                chooseAlbum();
                break;
        }
    }


    private void takePhoto() {
        sharedpreferencesUtil.delectImageTemp();
        currentTackPictureImagView = im_avatar;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, sharedpreferencesUtil.getImageTempNameUri());
        startActivityForResult(intent, TAKE_BIG_PICTURE);
    }

    private void chooseAlbum() {
        sharedpreferencesUtil.delectImageTemp();
        currentTackPictureImagView = im_avatar;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, SELECT_PIC_KITKAT);
        } else {
            startActivityForResult(intent, SELECT_PIC);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String mFileName;
        Bitmap mColorBitmap;
        if (requestCode == SELECT_PIC) {
            if (resultCode == RESULT_OK) {
                mFileName = DemoUtils.getDataColumn(getApplicationContext(), data.getData(), null, null);
                mColorBitmap = DemoUtils.getBitmap(mFileName);
                im_avatar.setImageBitmap(mColorBitmap);
                DemoUtils.copyFile(mFileName, sharedpreferencesUtil.getImageTempNameString2());
                cropImageUri(sharedpreferencesUtil.getImageTempNameUri(), 400, 400, CROP_BIG_PICTURE);
            }
        }
        if (requestCode == SELECT_PIC_KITKAT) {
            if (resultCode == RESULT_OK) {
                mFileName = DemoUtils.getPath(getApplicationContext(), data.getData());
                mColorBitmap = DemoUtils.getBitmap(mFileName);
                im_avatar.setImageBitmap(mColorBitmap);
                DemoUtils.copyFile(mFileName, sharedpreferencesUtil.getImageTempNameString2());
                cropImageUri(sharedpreferencesUtil.getImageTempNameUri(), 400, 400, CROP_BIG_PICTURE);
            }
        }
        if (requestCode == TAKE_BIG_PICTURE) {
            cropImageUri(sharedpreferencesUtil.getImageTempNameUri(), 400, 400,
                    CROP_BIG_PICTURE);
        }
        if (requestCode == CROP_BIG_PICTURE) {
            if (sharedpreferencesUtil.getImageTempNameUri() != null) {
                Bitmap bitmap2 = DemoUtils.decodeUriAsBitmap(this,
                        sharedpreferencesUtil.getImageTempNameUri());
                App.firstRun();
                File cacheDirProducts = new File(App.CACHE_DIR_PRODUCTS);
                if (!cacheDirProducts.exists()) {
                    cacheDirProducts.mkdirs();
                }
                FileOutputStream b = null;
                String fileName = App.CACHE_DIR_PRODUCTS + "/" + System.currentTimeMillis() + ".jpg";
                try {
                    b = new FileOutputStream(fileName);
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 90, b);// ������д���ļ�
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        b.flush();
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (currentTackPictureImagView != null) {
                    currentTackPictureImagView.setTag(fileName);
                    currentTackPictureImagView.setImageBitmap(DemoUtils.loadFromPath(fileName));
                    File temp = new File(Environment.getExternalStorageDirectory()
                            .getPath() + "/yourAppCacheFolder/");// 自已缓存文件夹
                    if (!temp.exists()) {
                        temp.mkdir();
                    }
                    final File tempFile = new File(temp.getAbsolutePath() + "/"
                            + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名
                    // 图像保存到文件中
                    FileOutputStream foutput = null;
                    try {
                        foutput = new FileOutputStream(tempFile);
                        if (DemoUtils.loadFromPath(fileName).compress(Bitmap.CompressFormat.JPEG, 100, foutput)) {
                            btn_register.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final BmobFile file = new BmobFile(new File(tempFile.getAbsolutePath()));
                                    file.upload(RegisterActivity2.this, new UploadFileListener() {
                                        @Override
                                        public void onSuccess() {
                                            MyUser user = MyUser.getCurrentUser(RegisterActivity2.this, MyUser.class);
                                            user.setAvatar(file);
                                            user.update(RegisterActivity2.this, user.getObjectId(), new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    String phone = "";
                                                    final MyUser user = MyUser.getCurrentUser(RegisterActivity2.this, MyUser.class);
                                                    phone = et_phone.getText().toString();
                                                    user.setMobilePhoneNumber(phone);
                                                    user.update(RegisterActivity2.this, new UpdateListener() {
                                                        @Override
                                                        public void onSuccess() {
                                                            ShowToast("注册完成！");
                                                        }

                                                        @Override
                                                        public void onFailure(int arg0, String arg1) {
                                                            ShowToast("onFailure:" + arg1);
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                    ShowToast("失败2");
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            ShowToast("失败1");
                                        }
                                    });
                                }
                            });
                        }
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }


    String[] sex = new String[]{"男", "女", "诶嘿嘿"};

    private void showSexChooseDialog() {
        new AlertDialog.Builder(this)
                .setTitle("单选框")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(sex, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                BmobLog.i("点击的是" + sex[which]);
                                updateSex(which);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }

    private void updateSex(int which) {
        final MyUser user = MyUser.getCurrentUser(this, MyUser.class);
        switch (which) {
            case 0:
                user.setSex("男");
                break;
            case 1:
                user.setSex("女");
                break;
            case 2:
                user.setSex("诶嘿嘿");
                break;
        }
        user.update(this, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                final MyUser u = MyUser.getCurrentUser(RegisterActivity2.this, MyUser.class);
                tv_set_sex.setText(u.getSex());
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ShowToast("onFailure:" + arg1);
            }
        });
    }

    String[] school = new String[]{"北京信息科技大学", "北京林业大学", "北京大学", "清华大学"};

    private void showSchoolChooseDialog() {
        new AlertDialog.Builder(this)
                .setTitle("单选框")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(school, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                BmobLog.i("点击的是" + school[which]);
                                updateSchool(which);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }

    private void updateSchool(int which) {
        final MyUser user = MyUser.getCurrentUser(this, MyUser.class);
        switch (which) {
            case 0:
                user.setSchool("北京信息科技大学");
                break;
            case 1:
                user.setSchool("北京林业大学");
                break;
            case 2:
                user.setSchool("北京大学");
                break;
            case 3:
                user.setSchool("清华大学");
                break;
        }
        user.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                final MyUser u = MyUser.getCurrentUser(RegisterActivity2.this, MyUser.class);
                tv_set_school.setText(u.getSchool());
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                ShowToast("onFailure:" + arg1);
            }
        });
    }
}
