package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.db.InviteMessgeDao;
import cn.ucai.superwechat.domain.InviteMessage;
import cn.ucai.superwechat.model.IUserModel;
import cn.ucai.superwechat.model.OnCompleteListener;
import cn.ucai.superwechat.model.UserModel;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;

public class FirentProfileActivity extends BaseActivity {
    private static final String TAG = "FirentProfileActivity";
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    User user;
    @BindView(R.id.tv_userinfo_nick)
    TextView tvUserinfoNick;
    @BindView(R.id.tv_userinfo_name)
    TextView tvUserinfoName;
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.btn_add_contact)
    Button btnAddContact;
    @BindView(R.id.btn_send_msg)
    Button btnSendMsg;
    @BindView(R.id.btn_send_video)
    Button btnSendVideo;
    private ProgressDialog progressDialog;
    IUserModel userModel;
    InviteMessage msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firent_profile);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        userModel = new UserModel();
        imgBack.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(R.string.title_user_detail_profile);
        if (user != null) {
            showUserInfo();
        } else {
            msg = (InviteMessage) getIntent().getSerializableExtra(I.User.PASSWORD);
            if (msg != null){
                user = new User(msg.getFrom());
                user.setAvatar(msg.getAvatar());
                user.setMUserNick(msg.getNickName());
                showUserInfo();
            }else {
                MFGT.finish(FirentProfileActivity.this);
            }
        }
    }

    private void showUserInfo() {
        tvUserinfoNick.setText(user.getMUserNick());
        tvUserinfoName.setText(user.getMUserName());
        EaseUserUtils.setAppUserAvatar(FirentProfileActivity.this, user.getMUserName(), profileImage);
        if (isFirent()) {
            SuperWeChatHelper.getInstance().saveAppContact(user);
            btnSendMsg.setVisibility(View.VISIBLE);
            btnSendVideo.setVisibility(View.VISIBLE);
        } else {
            btnAddContact.setVisibility(View.VISIBLE);
        }
        syncUserInfo();
    }

    public boolean isFirent() {
        return SuperWeChatHelper.getInstance().getAppContactList().containsKey(user.getMUserName());
    }

    @OnClick({R.id.btn_add_contact, R.id.btn_send_msg, R.id.btn_send_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_contact:
                addContact();
                break;
            case R.id.btn_send_msg:
                finish();
                MFGT.gotoChatActivity(FirentProfileActivity.this,user.getMUserName());
                break;
            case R.id.btn_send_video:
                finish();
                startVideoCall();
                break;
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(FirentProfileActivity.this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(FirentProfileActivity.this, VideoCallActivity.class).putExtra("username", user.getMUserName())
                    .putExtra("isComingCall", false));
            // videoCallBtn.setEnabled(false);
        }
    }

    private void addContact() {
        boolean isConfirm = true;
        if (isConfirm){
            MFGT.gotoSendAddFirentActivity(FirentProfileActivity.this, user.getMUserName());
        }else {
            MFGT.finish(FirentProfileActivity.this);
        }
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        MFGT.finish(FirentProfileActivity.this);
    }

    private void syncUserInfo(){
        //从服务器异步加载用户最新信息，填充到好友列表或者新的朋友列表
        userModel.loadUserInfo(FirentProfileActivity.this, user.getMUserName(), new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String r) {
                if (r != null){
                    Result result = ResultUtils.getResultFromJson(r,User.class);
                    if (result != null && result.isRetMsg()){
                        User user = (User) result.getRetData();
                        if (user != null){
                            if (msg != null){
                                ContentValues values = new ContentValues();
                                values.put(InviteMessgeDao.COLUMN_NAME_NICK,user.getMUserNick());
                                values.put(InviteMessgeDao.COLUMN_NAME_AVATAR,user.getAvatar());
                                InviteMessgeDao dao = new InviteMessgeDao(FirentProfileActivity.this);
                                dao.updateMessage(msg.getId(),values);
                                L.e(TAG,"msg = " + msg);
                            }else if (isFirent()){
                                SuperWeChatHelper.getInstance().saveAppContact(user);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
