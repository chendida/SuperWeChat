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
}
