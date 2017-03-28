package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.R;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    Button mbtnLogin,mbtnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mbtnLogin = (Button) findViewById(R.id.btn_login);
        mbtnSignUp = (Button) findViewById(R.id.btn_sign_up);
        mbtnLogin.setOnClickListener(this);
        mbtnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Log.e(TAG, "btn_login");
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.btn_sign_up:
                Log.e(TAG, "btn_sign_up");
                startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
                finish();
                break;
        }
    }
}
