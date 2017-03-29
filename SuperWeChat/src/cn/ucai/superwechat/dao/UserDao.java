package cn.ucai.superwechat.dao;

import android.content.Context;

import com.hyphenate.easeui.domain.User;

import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.utils.SharePrefrenceUtils;

/**
 * Created by Administrator on 2017/3/21.
 */

public class UserDao  {
    public static final String USER_TABLE_NAME = "t_fulicenter_user";
    public static final String USER_COLUMN_NAME = "m_user_name";
    public static final String USER_COLUMN_NICK = "m_user_nick";
    public static final String USER_COLUMN_AVATAR = "m_user_avatar_id";
    public static final String USER_COLUMN_AVATAR_PATH = "m_user_avatar_path";
    public static final String USER_COLUMN_AVATAR_SUFFIX = "m_user_avatar_suffix";
    public static final String USER_COLUMN_AVATAR_TYPE = "m_user_avatar_type";
    public static final String USER_COLUMN_AVATAR_UPDATE_TIME = "m_user_update_time";

    private static UserDao instance;

    public static UserDao getInstance(Context context) {
        if (instance == null){
            instance = new UserDao(context);
        }
        return instance;
    }

    public UserDao(Context context){
        DBManager.getInstance().initDB(context);
    }
    public boolean saveUserInfo(User user){
        return DBManager.getInstance().saveUserInfo(user);
    }
    public User getUserInfo(String userName){
        if (userName == null){
            return null;
        }else {
            return DBManager.getInstance().getUserInfo(userName);
        }
    }
    /*
    清除数据
     */
    public void logout() {
        SuperWeChatApplication.setUser(null);//清除内存中的数据信息
        SharePrefrenceUtils.getInstance().removeUser();//清除首选项的数据信息
        DBManager.getInstance().closeDB();//关闭数据库
    }
}
