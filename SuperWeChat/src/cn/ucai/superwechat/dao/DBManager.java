package cn.ucai.superwechat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hyphenate.easeui.domain.User;


/**
 * Created by Administrator on 2017/3/21.
 */

public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();
    DBOpenHelper mHelper;
    static DBManager dbManager = new DBManager();
    public synchronized void initDB(Context context){
        mHelper = DBOpenHelper.getInstance(context);
    }
    public static DBManager getInstance(){
        return dbManager;
    }
    public boolean saveUserInfo(User user){
        SQLiteDatabase database = mHelper.getWritableDatabase();
        if (database.isOpen()){
            ContentValues values = new ContentValues();
            values.put(UserDao.USER_COLUMN_NAME,user.getMUserName());
            values.put(UserDao.USER_COLUMN_NICK,user.getMUserNick());
            values.put(UserDao.USER_COLUMN_AVATAR,user.getMAvatarId());
            values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMAvatarPath());
            values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMAvatarType());
            values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMAvatarSuffix());
            values.put(UserDao.USER_COLUMN_AVATAR_UPDATE_TIME,user.getMAvatarLastUpdateTime());
            if (user != null) {
                return database.replace(UserDao.USER_TABLE_NAME, null,values) != -1;
            }
            Log.e(TAG,"insert,user = " + user);
        }
        return false;
    }

    public User getUserInfo(String userName){
        SQLiteDatabase database = mHelper.getReadableDatabase();
        if (database.isOpen()){
            String sql = "select * from " + UserDao.USER_TABLE_NAME + " where " +
                    UserDao.USER_COLUMN_NAME +"= '" + userName +"'";
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()){
                User user = new User();
                user.setMUserName(userName);
                user.setMUserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
                user.setMAvatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR)));
                user.setMAvatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
                user.setMAvatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
                user.setMAvatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
                user.setMAvatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_UPDATE_TIME)));
                return user;
            }
        }
        return null;
    }

    public void closeDB() {
        mHelper.closeDB();
    }
}
