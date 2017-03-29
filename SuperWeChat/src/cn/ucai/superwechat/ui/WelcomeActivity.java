package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.utils.MFGT;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_sign_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                MFGT.gotoLoginActivity(WelcomeActivity.this);
                MFGT.finish(WelcomeActivity.this);
                break;
            case R.id.btn_sign_up:
                MFGT.gotoRegisterActivity(WelcomeActivity.this);
                MFGT.finish(WelcomeActivity.this);
                break;
        }
    }
}
