package cn.ucai.superwechat.model;

import android.content.Context;

import com.hyphenate.easeui.domain.Group;

import java.io.File;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/4/10.
 */

public class GroupModel implements IGroupModel {
    @Override
    public void createGroup(Context context, Group group, File file, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_CREATE_GROUP)
                .addParam(I.Group.HX_ID,group.getMGroupHxid())
                .addParam(I.Group.NAME,group.getMGroupName())
                .addParam(I.Group.DESCRIPTION,group.getMGroupDescription())
                .addParam(I.Group.OWNER,group.getMGroupOwner())
                .addParam(I.Group.IS_PUBLIC,String.valueOf(group.getMGroupIsPublic()))
                .addParam(I.Group.ALLOW_INVITES,String.valueOf(group.getMGroupAllowInvites()))
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

}
