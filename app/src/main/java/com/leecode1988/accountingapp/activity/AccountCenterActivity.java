package com.leecode1988.accountingapp.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.leecode1988.accountingapp.activity.base.BaseActivity;
import com.leecode1988.accountingapp.bean.ImageResponse;
import com.leecode1988.accountingapp.R;
import com.leecode1988.accountingapp.util.SPUtil;
import com.leecode1988.accountingapp.network.UploadPhotoService;
import com.leecode1988.accountingapp.bean.UserBean;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountCenterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AccountCenterActivity";

    private String avatarUrl;
    private String avatarPath;
    private TextView textNickName;
    private TextView textSignature;
    private ImageView imageAvatar;
    private LinearLayout linearNickName, linearSignature, linearAvatar;

    private Button btLoginOut;

    private String nickName, signature;

    private static final int CHOOSE_PHOTO = 2;
    private Button textUpload;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_center);
        initView();
    }


    private void initView() {
        initToolbar();
        textNickName = findViewById(R.id.text_nickname);
        textSignature = findViewById(R.id.text_signature);
        imageAvatar = findViewById(R.id.image_avatar);
        btLoginOut = findViewById(R.id.bt_login_out);

        linearAvatar = findViewById(R.id.ll_avatar);
        linearNickName = findViewById(R.id.ll_nickname);
        linearSignature = findViewById(R.id.ll_signature);

        textUpload = findViewById(R.id.bt_upload);
        textUpload.setOnClickListener(this);

        linearAvatar.setOnClickListener(this);
        linearNickName.setOnClickListener(this);
        linearSignature.setOnClickListener(this);

        textNickName.setOnClickListener(this);
        textSignature.setOnClickListener(this);
        imageAvatar.setOnClickListener(this);

        UserBean user = (UserBean) getIntent().getSerializableExtra("user");
        if (user != null) {
            textNickName.setText(user.getUsername());
            textSignature.setText(user.getSignature());
            Glide.with(this).load(user.getAvatarUrl()).into(imageAvatar);
        }
        btLoginOut.setOnClickListener(this);

    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("个人信息");
    }


    public static void actionStart(Context context, UserBean user) {
        Intent intent = new Intent(context, AccountCenterActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_out:
                BmobUser.logOut();
                Toast.makeText(this, "退出登录成功", Toast.LENGTH_SHORT).show();
                //清除本地缓存
                SPUtil.clearAll();
                finish();
                break;
            case R.id.ll_avatar: {
                if (ContextCompat.checkSelfPermission(AccountCenterActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AccountCenterActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlum();
                }
                break;
            }
            case R.id.image_avatar: {
                break;
            }
            case R.id.ll_nickname:
            case R.id.text_nickname: {
                final EditText editText = new EditText(this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("修改昵称");

                editText.setText(textNickName.getText().toString().trim());

                dialog.setView(editText);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nickName = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(nickName)) {
                            Toast.makeText(AccountCenterActivity.this, "输入内容", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        textNickName.setText(nickName);
                    }
                });
                dialog.create().show();
                break;
            }
            case R.id.ll_signature:
            case R.id.text_signature: {
                final EditText editText = new EditText(this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("修改签名");

                editText.setText(textSignature.getText().toString().trim());

                dialog.setView(editText);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signature = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(signature)) {
                            Toast.makeText(AccountCenterActivity.this, "输入内容", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        textSignature.setText(signature);
                    }
                });
                dialog.create().show();
                break;
            }
            case R.id.bt_upload: {
                Log.d(TAG, "test");
                Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.szvone.cn/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
                UploadPhotoService service = retrofit.create(UploadPhotoService.class);

                RequestBody body =
                    RequestBody.create(MediaType.parse("image/png"), new File(avatarPath));
                MultipartBody.Part file = MultipartBody.Part.createFormData("file", new File(avatarPath).getName(), body);

                Observable observable = service.uploadPhoto("l4CjIfY7kSr050UQkQ", file);

                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ImageResponse>() {
                        @Override
                        public void accept(ImageResponse imageResponse) {
                            avatarUrl = imageResponse.getApi_res().getImg_url();
                            avatarPath = null;
                            Toast.makeText(AccountCenterActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                            textUpload.setVisibility(View.GONE);
                        }
                    });

                break;
            }
            default:
                break;
        }
    }


    /**
     * 打开image媒体文件
     */
    private void openAlum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlum();
                } else {
                    Toast.makeText(this, "请允许该权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                //判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    //4.4及以上系统使用这个方法处理图片
                    handleImage(data);
                } else {
                    //4.4及以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(data);
                }
        }
    }


    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }


    /**
     * 解析图片
     *
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImage(Intent data) {
        if (data == null) {
            return;
        }
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    selection);
            } else if ("com.android.providers.downloads.documents".equals
                (uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                    "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); //根据图片路径选择图片
    }


    /**
     * 显示图片
     *
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        avatarPath = imagePath;
        Glide.with(this)
            .load(imagePath)
            .centerCrop()
            .into(imageAvatar);
    }


    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection,
            null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_center_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_changed:
                if (TextUtils.isEmpty(nickName) && TextUtils.isEmpty(signature) && TextUtils.isEmpty(avatarPath) && TextUtils.isEmpty(avatarUrl)) {
                    Toast.makeText(this, "请修改信息后提交", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    final UserBean newUser = new UserBean();
                    newUser.setUsername(textNickName.getText().toString().trim());
                    newUser.setSignature(textSignature.getText().toString().trim());
                    if (avatarPath != null) {
                        //                        Toast.makeText(this, "请先上传头像", Toast.LENGTH_SHORT).show();
                        //                        textUpload.setVisibility(View.VISIBLE);
                        //                        return true;
                        Log.d(TAG, "test");
                        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://api.szvone.cn/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                        UploadPhotoService service = retrofit.create(UploadPhotoService.class);

                        RequestBody body =
                            RequestBody.create(MediaType.parse("image/png"), new File(avatarPath));
                        MultipartBody.Part file = MultipartBody.Part.createFormData("file", new File(avatarPath).getName(), body);

                        Observable observable = service.uploadPhoto("l4CjIfY7kSr050UQkQ", file);

                        observable.subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .flatMap(new Function<ImageResponse, ObservableSource<String>>() {
                                @Override
                                public ObservableSource<String> apply(ImageResponse response) throws Exception {
                                    String avatar = response.getApi_res().getImg_url();
                                    avatarPath = null;
                                    //                                        Toast.makeText(AccountCenterActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                                    //                                        textUpload.setVisibility(View.GONE);
                                    return Observable.just(avatar);
                                }

                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String url) {
                                    newUser.setAvatarUrl(url);
                                    updateUser(newUser);
                                }
                            });
                    } else {
                        updateUser(newUser);
                    }

                }
                break;

            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return true;
    }


    /**
     * 更新用户信息
     *
     * @param newUser
     */
    public void updateUser(UserBean newUser) {
        UserBean user = BmobUser.getCurrentUser(UserBean.class);
        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(AccountCenterActivity.this, "信息更新成功", Toast.LENGTH_SHORT).show();
                    nickName = null;
                    signature = null;
                    //同步本地缓存的用户信息
                    BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {

                            } else {
                                Toast.makeText(AccountCenterActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AccountCenterActivity.this, "信息更新失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
