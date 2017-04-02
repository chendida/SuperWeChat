package cn.ucai.superwechat.ui;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.model.IUserModel;
import cn.ucai.superwechat.model.OnCompleteListener;
import cn.ucai.superwechat.model.UserModel;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.PreferenceManager;
import cn.ucai.superwechat.utils.ResultUtils;

public class UserProfileActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private static final int REQUESTCODE_PICK = 1;
    private static final int REQUESTCODE_CUTTING = 2;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.iv_userinfo_avatar)
    ImageView ivUserinfoAvatar;
    @BindView(R.id.tv_userinfo_nick)
    TextView tvUserinfoNick;
    @BindView(R.id.tv_userinfo_name)
    TextView tvUserinfoName;

    String avatarName;

    private ProgressDialog dialog;
    IUserModel userModel;
    User user;
    UpdateAvatarReceiver updateAvatarReceiver;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_user_profile);
        ButterKnife.bind(this);
        userModel = new UserModel();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        imgBack.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(R.string.title_user_profile);
    }

    private void initListener() {
        updateAvatarReceiver = new UpdateAvatarReceiver();
        IntentFilter filter = new IntentFilter(I.REQUEST_UPDATE_AVATAR);
        registerReceiver(updateAvatarReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateAvatarReceiver != null){
            unregisterReceiver(updateAvatarReceiver);
        }
    }

    class UpdateAvatarReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(I.Avatar.UPDATE_TIME, false);
            updateAvatarView(success);
        }
    }

    private void updateAvatarView(boolean success) {
        dialog.dismiss();
        if (success) {
            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_success),
                    Toast.LENGTH_SHORT).show();
            user = SuperWeChatHelper.getInstance().getUserProfileManager().getCurrentAppUserInfo();
            EaseUserUtils.setAppUserAvatar(UserProfileActivity.this,user.getMUserName(),ivUserinfoAvatar);
            L.e(TAG,"updateAvatarView,success = " + success);
        } else {
            Toast.makeText(UserProfileActivity.this, getString(R.string.toast_updatephoto_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        user = SuperWeChatHelper.getInstance().getUserProfileManager().getCurrentAppUserInfo();
        if (user != null ){
            L.e(TAG,"initData()" + user);
            showUserInfo();
        }else {
            return;
        }
    }

    private void showUserInfo() {
        String username = user.getMUserName();
        EaseUserUtils.setAppUserNick(username, tvUserinfoNick);
        tvUserinfoName.setText(username);
        EaseUserUtils.setAppUserAvatar(UserProfileActivity.this, username, ivUserinfoAvatar);
    }

    private void uploadHeadPhoto() {
        Builder builder = new Builder(this);
        builder.setTitle(R.string.dl_title_upload_photo);
        builder.setItems(new String[]{getString(R.string.dl_msg_take_photo), getString(R.string.dl_msg_local_upload)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                Toast.makeText(UserProfileActivity.this, getString(R.string.toast_no_support),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(pickIntent, REQUESTCODE_PICK);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    private void updateRemoteNick(final String nickName) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_nick), getString(R.string.dl_waiting));
        userModel.updateUserNick(UserProfileActivity.this, EMClient.getInstance().getCurrentUser(),
                nickName, new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String str ) {
                        L.e(TAG,"userModel.updateUserNick,str = " + str);
                        dialog.dismiss();
                        if (str != null){
                            L.e(TAG,"userModel.updateUserNick,str1 = " + str);
                            Result result = ResultUtils.getResultFromJson(str, User.class);
                            if (result != null){
                                L.e(TAG,"userModel.updateUserNick,result = " + result);
                                if (result.isRetMsg()){
                                    L.e(TAG,"userModel.updateUserNick,result.isRetMsg() = " + result.isRetMsg());
                                    User user = (User) result.getRetData();
                                    if (user != null){
                                        L.e(TAG,"userModel.updateUserNick,user111 = " + user);
                                        //将用户昵称保存到首选项
                                        PreferenceManager.getInstance().setCurrentUserNick(nickName);
                                        //将用户信息保存到数据库
                                        SuperWeChatHelper.getInstance().saveAppContact(user);
                                        tvUserinfoNick.setText(nickName);
                                        CommonUtils.showShortToast(R.string.toast_updatenick_success);
                                    }
                                }else {
                                    CommonUtils.showShortToast(R.string.toast_updatenick_fail);
                                }
                            }
                            else {
                                CommonUtils.showShortToast(R.string.toast_updatenick_fail);
                            }
                        }
                        else {
                            CommonUtils.showShortToast(R.string.toast_updatenick_fail);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        dialog.dismiss();
                        L.e(TAG,"userModel.updateUserNick,error = " + error);
                        CommonUtils.showShortToast(R.string.toast_updatenick_fail);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:
                if (data == null || data.getData() == null) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadAppUserAvatar(File file) {
        dialog = ProgressDialog.show(this, getString(R.string.dl_update_photo), getString(R.string.dl_waiting));
        SuperWeChatHelper.getInstance().getUserProfileManager().uploadUserAvatar(file);
        dialog.show();
    }

    private File saveBitmapFile(Bitmap bitmap) {
        if (bitmap != null){
            String imgDirPath = getAvatarPath(UserProfileActivity.this, I.AVATAR_TYPE + "/");
            File dir = new File(imgDirPath);
            String imgPath =  getAvatarName() + ".jpg";
            File file = new File(dir,imgPath);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            L.e(TAG,"saveBitmapFile,file = " + file.getAbsolutePath());
            return file;
        }
        return null;
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * save the picture data
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        L.e(TAG,"setPicToView, picdata = " + picdata);
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            L.e(TAG,"setPicToView, photo = " + photo);
            uploadAppUserAvatar(saveBitmapFile(photo));
        }
    }
    @OnClick({R.id.iv_userinfo_avatar, R.id.tv_userinfo_nick,R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_userinfo_avatar:
                uploadHeadPhoto();
                break;
            case R.id.tv_userinfo_nick:
                final EditText editText = new EditText(this);
                new Builder(this).setTitle(R.string.setting_nickname).setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                        .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nickName = editText.getText().toString();
                                if (TextUtils.isEmpty(nickName)) {
                                    Toast.makeText(UserProfileActivity.this, getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (PreferenceManager.getInstance().getCurrentUserNick().equals(nickName)){
                                    CommonUtils.showShortToast(R.string.toast_no_updatenick);
                                    return;
                                }
                                updateRemoteNick(nickName);
                            }
                        }).setNegativeButton(R.string.dl_cancel, null).show();
                break;
            case R.id.img_back:
                MFGT.finish(UserProfileActivity.this);
                break;
            default:
                break;
        }
    }

    /**
     * 返回头像保存在sd卡的位置:
     * Android/data/cn.ucai.superwechat/files/pictures/user_avatar
     *
     * @param context
     * @param path
     * @return
     */
    public static String getAvatarPath(Context context, String path) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File folder = new File(dir, path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder.getAbsolutePath();
    }
    private String getAvatarName(){
        avatarName = user.getMUserName() + System.currentTimeMillis();
        return avatarName;
    }
}
