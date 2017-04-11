package cn.ucai.superwechat.model;

import android.content.Context;

import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.domain.Group;

import java.io.File;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface IGroupModel {
    /*
    新建群组
     */
    void  createGroup(Context context, EMGroup group, File file, OnCompleteListener<String>listener);
    /*
    添加群成员
     */
    void addGroupMember(Context context,String userName,String group_hxid,OnCompleteListener<String>listener);
    /*
    批量添加群成员
     */
    void addGroupMembers(Context context,String userNames,String group_hxid,OnCompleteListener<String>listener);
}
