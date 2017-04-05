package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.hyphenate.easeui.domain.User;

import java.util.ArrayList;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.ui.AddContactActivity;
import cn.ucai.superwechat.ui.FirentProfileActivity;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
import cn.ucai.superwechat.ui.SettingsActivity;
import cn.ucai.superwechat.ui.UserProfileActivity;
import cn.ucai.superwechat.ui.WelcomeActivity;


/**
 * Created by Administrator on 2017/3/16.
 */

public class MFGT {
    public static void startActivity(Activity activity, Class cls){
        activity.startActivity(new Intent(activity,cls));
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startActivity(Activity activity,Intent intent){
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void gotoMain(Activity activity) {
        startActivity(activity, MainActivity.class);
    }

    public static void startActivityForResult(Activity activity,Intent intent,int requestCode){
        activity.startActivityForResult(intent,requestCode);
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void gotoWelcomeActivity(Activity activity) {
        startActivity(activity,WelcomeActivity.class);
    }

    public static void gotoLoginActivity(Activity activity) {
        startActivity(activity,LoginActivity.class);
    }

    public static void gotoRegisterActivity(Activity activity) {
        startActivity(activity,RegisterActivity.class);
    }

    public static void gotoSettingsActivity(Activity activity) {
        startActivity(activity, SettingsActivity.class);
    }

    public static void gotoUserProfileActivity(Activity activity) {
        startActivity(activity, UserProfileActivity.class);
    }

    public static void gotoAddContact(Activity activity) {
        startActivity(activity, AddContactActivity.class);
    }

    public static void gotoFrientProfileActivity(Activity activity, User user) {
        startActivity(activity, new Intent(activity,FirentProfileActivity.class)
        .putExtra(I.User.USER_NAME,user));
    }
}
