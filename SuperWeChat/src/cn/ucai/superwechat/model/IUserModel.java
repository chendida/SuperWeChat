package cn.ucai.superwechat.model;

import android.content.Context;

import java.io.File;

/**
 * Created by Administrator on 2017/3/29.
 */

public interface IUserModel {
    /*
    注册
     */
    void register(Context context,String userName,String nick,String password
            ,OnCompleteListener<String>listener);
    /*
    取消注册
     */
    void unregister(Context context,String userName,OnCompleteListener<String>listener);
    /*
    登录
     */
    void login(Context context,String userName,String password,OnCompleteListener<String>listener);
    /*
    根据用户名查找用户信息
     */
    void loadUserInfo(Context context,String userName,OnCompleteListener<String>listener);
    /*
    更新用户昵称
     */
    void updateUserNick(Context context,String userName,String userNick,OnCompleteListener<String>listener);

    /*
    更新用户头像
     */
    void updateUserAvatar(Context context, String userName,File file, OnCompleteListener<String>listener);
}
