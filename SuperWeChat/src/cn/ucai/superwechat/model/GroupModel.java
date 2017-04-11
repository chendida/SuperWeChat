package cn.ucai.superwechat.model;

import android.content.Context;

import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.domain.Group;

import java.io.File;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/4/10.
 */

public class GroupModel implements IGroupModel {
    @Override
    public void createGroup(Context context, EMGroup group, File file, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_CREATE_GROUP)
                .addParam(I.Group.HX_ID,group.getGroupId())
                .addParam(I.Group.NAME,group.getGroupName())
                .addParam(I.Group.DESCRIPTION,group.getDescription())
                .addParam(I.Group.OWNER,group.getOwner())
                .addParam(I.Group.IS_PUBLIC,String.valueOf(group.isPublic()))
                .addParam(I.Group.ALLOW_INVITES,String.valueOf(group.isAllowInvites()))
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    @Override
    public void addGroupMember(Context context, String userName, String group_hxid, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBER)
                .addParam(I.Member.USER_NAME,userName)
                .addParam(I.Member.GROUP_HX_ID,group_hxid)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void addGroupMembers(Context context, String userNames, String group_hxid, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBERS)
                .addParam(I.Member.USER_NAME,userNames)
                .addParam(I.Member.GROUP_HX_ID,group_hxid)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void deleteGroupMember(Context context, String userNames, String myGroupId, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_GROUP_MEMBER)
                .addParam(I.Member.GROUP_ID,myGroupId)
                .addParam(I.Member.USER_NAME,userNames)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void findGroupByHxId(Context context, String hxId, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GROUP_BY_HXID)
                .addParam(I.Group.HX_ID,hxId)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void updateGroupName(Context context, String groupId, String groupName, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_GROUP_NAME)
                .addParam(I.Group.GROUP_ID,groupId)
                .addParam(I.Group.NAME,groupName)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void deleteGroup(Context context, String hxId, OnCompleteListener<String> listener) {
        OkHttpUtils<String>utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_GROUP_BY_HXID)
                .addParam(I.Group.HX_ID,hxId)
                .targetClass(String.class)
                .execute(listener);
    }
}
