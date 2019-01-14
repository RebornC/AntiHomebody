package com.example.yc.androidsrc.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CaptureResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.PersonalMsgPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IPersonalMsgPresenter;
import com.example.yc.androidsrc.ui.impl.IPersonalMsgView;
import com.example.yc.androidsrc.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * 编辑资料界面
 *
 * @author RebornC
 * Created on 2019/1/10.
 */

public class UserSettingsActivity extends AppCompatActivity implements IPersonalMsgView, View.OnClickListener {

    private IPersonalMsgPresenter personalMsgPresenter;
    private _User curUser;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private AlertDialog dialog;
    private TextView input_edt;

    // 头像
    private ListView listView1;
    private List<Map<String,Object>> listData1 = new ArrayList<>();
    private SimpleAdapter simpleAdapter1;
    private String imageUrl;
    private static String LackOfHeadPortrait = "0";
    // 其余信息
    private ListView listView2;
    private List<Map<String,Object>> listData2 = new ArrayList<>();
    private SimpleAdapter simpleAdapter2;
    private String userName;
    private String userSignature;
    private String phoneNumber;
    private String defaultStr = "点击修改";

    private static final String key = "CODE";
    private static final int UPLOAD_HEADPORTRAIT_CODE = 1; // 上传头像
    private static final int UPLOAD_MESSAGE_CODE = 2; // 上传其他用户信息
    private int modifyMsgFlag = 0; // 0表示修改名字，1表示修改签名
    private ArrayList<Integer> codeList = new ArrayList<>(); // 返回上一个界面时所携带的数据

    // 用于接收图库选择或拍照完成后的结果回调
    private static final int PHOTO_TK = 0; // 图库
    private static final int PHOTO_CLIP = 2; // 裁剪
    private static String img_uri = "";
    private Uri contentUri;
    private Bitmap photo;
    private OutputStream out;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        findView();
        getUserData();
        initUserHeadPortrait();
        initUserMsg();
        clickEvent();
    }


    public void findView() {
        personalMsgPresenter = new PersonalMsgPresenterCompl(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("个人资料");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLastActivity();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        listView1 = (ListView) findViewById(R.id.list_view_1);
        listView2 = (ListView) findViewById(R.id.list_view_2);
        listView2.addHeaderView(new ViewStub(this)); // 顶部分割线
        listView2.addFooterView(new ViewStub(this)); // 底部分割线
    }


    public void getUserData() {
        curUser = BmobUser.getCurrentUser(_User.class);
        userName = curUser.getNickName();
        if (curUser.getHeadPortrait() == null) {
            imageUrl = LackOfHeadPortrait;
        } else {
            imageUrl = curUser.getHeadPortrait().getFileUrl();
        }
        if (curUser.getSignature() == null) {
            userSignature = getResources().getString(R.string.default_signature);
        } else {
            userSignature = curUser.getSignature();
        }
        phoneNumber = curUser.getMobilePhoneNumber();
    }


    public void initUserHeadPortrait() {
        // 用户头像实例化
        listData1.clear();
        Map<String,Object> temp_1 = new LinkedHashMap<>();
        temp_1.put("text", "头像");
        temp_1.put("image", imageUrl);
        listData1.add(temp_1);
        simpleAdapter1 = new SimpleAdapter(this, listData1, R.layout.list_view_item_2,
                new String[] {"text", "image"}, new int[] {R.id.text, R.id.image});
        simpleAdapter1.setViewBinder(new SimpleAdapter.ViewBinder() {
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // 判断是否为我们要处理的对象
                if(view instanceof ImageView && data instanceof String){
                    ImageView iv = (ImageView) view;
                    if (data.equals(LackOfHeadPortrait)) {
                        // 当用户未上传头像时，使用系统默认头像
                        Glide.with(UserSettingsActivity.this).load(R.mipmap.head).into(iv);
                    } else {
                        Glide.with(UserSettingsActivity.this).load(imageUrl).into(iv);
                    }
                    return true;
                }else
                    return false;
            }
        });
        listView1.setAdapter(simpleAdapter1);
    }


    public void initUserMsg() {
        // 用户信息实例化
        listData2.clear();
        String[] attrList = new String[] {"昵称","签名","账号","密码"};
        String[] textList = new String[] {userName, defaultStr, phoneNumber, defaultStr};
        for (int i = 0; i < attrList.length; i++) {
            Map<String,Object> temp_2 = new LinkedHashMap<>();
            temp_2.put("attr", attrList[i]);
            temp_2.put("text", textList[i]);
            listData2.add(temp_2);
        }
        simpleAdapter2 = new SimpleAdapter(this, listData2, R.layout.list_view_item_3,
                new String[] {"attr","text"}, new int[] {R.id.attr, R.id.text});
        listView2.setAdapter(simpleAdapter2);
    }


    public void clickEvent() {
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // 打开相册，选择图片并裁剪，上传
                    Intent intent = new Intent(Intent.ACTION_PICK, null); // 请选择文件
                    intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*"); // 返回结果和标识
                    startActivityForResult(intent, PHOTO_TK);
                }
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    modifyMsgFlag = 0;
                    popupModifyMsgDialog(UserSettingsActivity.this);
                    input_edt.setText(userName);
                } else if (position == 2) {
                    modifyMsgFlag = 1;
                    popupModifyMsgDialog(UserSettingsActivity.this);
                    input_edt.setText(userSignature);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_comfirm:
                String newStr = input_edt.getText().toString();
                if (newStr.equals("")) {
                    ToastUtil.showShort(UserSettingsActivity.this, "输入不能为空哦");
                } else {
                    dialog.dismiss();
                    progressBar.setVisibility(View.VISIBLE);
                    if (modifyMsgFlag == 0) {
                        // 修改名字
                        personalMsgPresenter.modifyUserNickname(newStr);
                    } else if (modifyMsgFlag == 1) {
                        // 修改签名
                        personalMsgPresenter.modifyUserSignature(newStr);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_TK:
                    startPhotoZoom(data.getData());
                    break;
                case PHOTO_CLIP:
                    // 将裁剪后的图像转成BitMap
                    try {
                        photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 创建路径
                    String path = Environment.getExternalStorageDirectory().getPath() + "/Pic";
                    // 获取外部储存目录
                    File file = new File(path);
                    // 创建新目录
                    file.mkdirs();
                    // 以当前时间重新命名文件
                    long i = System.currentTimeMillis();
                    // 生成新的文件
                    img_uri = file.toString() + "/" + i + ".png";
                    file = new File(file.toString() + "/" + i + ".png");
                    // 创建输出流
                    try {
                        out = new FileOutputStream(file.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 压缩文件
                    boolean flag = photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    if (file.getName() != null || !file.getName().equals("")) {
                        // 显示
                        uploadImage(img_uri);
                    }
                    break;
            }
        }
    }


    /**
     * 裁剪图片的方法.
     * 用于拍照完成或者选择本地图片之后
     */
    private Uri uritempFile;

    public void startPhotoZoom(Uri uri) {
        // 解决小米选择相册图片截取照片不进onActivityResult报“保存时发生错误，保存失败”的bug
        // 小米选择相册时如果不使用系统裁剪，直接压缩不会报这个错误，如果裁剪就会报如上错误
        // 需要把URI地址转为图片地址，再包成file文件转为URI
        if (uri.toString().contains("com.miui.gallery.open")) {
            uri = getImageContentUri(this, new File(getRealFilePath(this, uri)));
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        // uritempFile为Uri类变量，实例化uritempFile
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 开启临时权限
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 针对7.0以上的操作
            intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
            uritempFile = uri;
        } else {
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }


    /**
     * 将图片上传到Bmob
     */
    public void uploadImage(String uri) {
        progressBar.setVisibility(View.VISIBLE);
        personalMsgPresenter.uploadHeadPortrait(uri);
    }


    /**
     * 将URI转为图片的路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * 弹出带有输入框的自定义对话框，用于修改用户信息
     *
     * @param context
     */
    public void popupModifyMsgDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.modify_msg_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        input_edt = (EditText) view.findViewById(R.id.dialog_edit);
        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
        Button btn_comfirm = (Button)view.findViewById(R.id.btn_comfirm);
        // 取消/确定按钮监听事件处理
        btn_cancel.setOnClickListener(this);
        btn_comfirm.setOnClickListener(this);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.offWhite);
    }


    public void returnLastActivity() {
        // 返回上一个界面，传递bundle，根据bundle携带的数据判断是否要更新界面
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(key, codeList);
        intent.putExtra(key, bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onUpdateData(boolean result, int resultCode, String message) {
        progressBar.setVisibility(View.GONE);
        ToastUtil.showShort(UserSettingsActivity.this, message);
        // 刷新界面
        if (result && resultCode == UPLOAD_HEADPORTRAIT_CODE) {
            codeList.add(UPLOAD_HEADPORTRAIT_CODE);
            getUserData();
            initUserHeadPortrait();
        } else if (result && resultCode == UPLOAD_MESSAGE_CODE) {
            codeList.add(UPLOAD_MESSAGE_CODE);
            getUserData();
            initUserMsg();
        }
    }

}


