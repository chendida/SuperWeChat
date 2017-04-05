package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.MFGT;

public class FirentProfileActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firent_profile);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        initView();
    }

    private void initView() {
        imgBack.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(R.string.title_user_detail_profile);
        if (user != null) {
            showUserInfo();
        } else {
            MFGT.finish(FirentProfileActivity.this);
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
                break;
            case R.id.btn_send_video:
                break;
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
}
