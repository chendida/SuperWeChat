package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.MFGT;

public class SendAddFirentActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.btn_send)
    Button btnSend;
    String toAddUserName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_add_firent);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        toAddUserName = getIntent().getStringExtra(I.User.USER_NAME);
        if (toAddUserName == null) {
            MFGT.finish(SendAddFirentActivity.this);
        }
    }

    private void initView() {
        imgBack.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);
        btnSend.setBackgroundColor(0x4CAF50);
        txtTitle.setText(R.string.str_send_broadcast);
        etMsg.setText(getString(R.string.addcontact_send_msg_prefix) +
                SuperWeChatHelper.getInstance().getUserProfileManager().getCurrentAppUserInfo().getMUserNick());
    }

    @OnClick(R.id.btn_send)
    public void sendMsgOnClick() {
        if (toAddUserName != null) {
            addContact();
        }
    }

    /**
     * add contact
     */
    public void addContact() {
        if (EMClient.getInstance().getCurrentUser().equals(toAddUserName)) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if (SuperWeChatHelper.getInstance().getAppContactList().containsKey(toAddUserName)) {
            //let the user know the contact already in your contact list
            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(toAddUserName)) {
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }
        showAddDialog();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = etMsg.getText().toString();
                    EMClient.getInstance().contactManager().addContact(toAddUserName, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void showAddDialog() {
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    @OnClick(R.id.img_back)
    public void onClick() {
        MFGT.finish(SendAddFirentActivity.this);
    }
}
