/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.redpacketui.utils.RedPacketUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.util.EMLog;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.Constant;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.SuperWeChatModel;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.PreferenceManager;

/**
 * settings screen
 *
 *
 */
@SuppressWarnings({"FieldCanBeLocal"})
public class SettingsActivity extends BaseActivity implements OnClickListener{
    @BindView(R.id.ll_change)
    LinearLayout llChange;
    @BindView(R.id.rl_mail_log)
    RelativeLayout rlMailLog;
    @BindView(R.id.switch_notification)
    EaseSwitchButton switchNotification;
    @BindView(R.id.rl_switch_notification)
    RelativeLayout rlSwitchNotification;
    @BindView(R.id.textview1)
    TextView textview1;
    @BindView(R.id.switch_sound)
    EaseSwitchButton switchSound;
    @BindView(R.id.rl_switch_sound)
    RelativeLayout rlSwitchSound;
    @BindView(R.id.textview2)
    TextView textview2;
    @BindView(R.id.switch_vibrate)
    EaseSwitchButton switchVibrate;
    @BindView(R.id.rl_switch_vibrate)
    RelativeLayout rlSwitchVibrate;
    @BindView(R.id.rl_push_settings)
    RelativeLayout rlPushSettings;
    @BindView(R.id.switch_speaker)
    EaseSwitchButton switchSpeaker;
    @BindView(R.id.rl_switch_speaker)
    RelativeLayout rlSwitchSpeaker;
    @BindView(R.id.edit_custom_appkey)
    EditText editCustomAppkey;
    @BindView(R.id.switch_custom_appkey)
    EaseSwitchButton switchCustomAppkey;
    @BindView(R.id.rl_custom_appkey)
    RelativeLayout rlCustomAppkey;
    @BindView(R.id.switch_custom_server)
    EaseSwitchButton switchCustomServer;
    @BindView(R.id.rl_custom_server)
    RelativeLayout rlCustomServer;
    @BindView(R.id.ll_user_profile)
    LinearLayout llUserProfile;
    @BindView(R.id.ll_black_list)
    LinearLayout llBlackList;
    @BindView(R.id.ll_diagnose)
    LinearLayout llDiagnose;
    @BindView(R.id.ll_set_push_nick)
    LinearLayout llSetPushNick;
    @BindView(R.id.ll_call_option)
    LinearLayout llCallOption;
    @BindView(R.id.textview4)
    TextView textview4;
    @BindView(R.id.switch_owner_leave)
    EaseSwitchButton switchOwnerLeave;
    @BindView(R.id.rl_switch_chatroom_owner_leave)
    RelativeLayout rlSwitchChatroomOwnerLeave;
    @BindView(R.id.switch_delete_msg_when_exit_group)
    EaseSwitchButton switchDeleteMsgWhenExitGroup;
    @BindView(R.id.rl_switch_delete_msg_when_exit_group)
    RelativeLayout rlSwitchDeleteMsgWhenExitGroup;
    @BindView(R.id.switch_auto_accept_group_invitation)
    EaseSwitchButton switchAutoAcceptGroupInvitation;
    @BindView(R.id.rl_switch_auto_accept_group_invitation)
    RelativeLayout rlSwitchAutoAcceptGroupInvitation;
    @BindView(R.id.switch_adaptive_video_encode)
    EaseSwitchButton switchAdaptiveVideoEncode;
    @BindView(R.id.rl_switch_adaptive_video_encode)
    RelativeLayout rlSwitchAdaptiveVideoEncode;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    private SuperWeChatModel settingsModel;
    private EMOptions chatOptions;
    /*
	*//**
     * new message notification
     *//*
	private RelativeLayout rl_switch_notification;
	*//**
     * sound
     *//*
	private RelativeLayout rl_switch_sound;
	*//**
     * vibration
     *//*
	private RelativeLayout rl_switch_vibrate;
	*//**
     * speaker
     *//*
	private RelativeLayout rl_switch_speaker;


	*//**
     * line between sound and vibration
     *//*
	private TextView textview1, textview2;

	private LinearLayout blacklistContainer;
	
	private LinearLayout userProfileContainer;
	
	*//**
     * logout
     *//*
	private Button logoutBtn;

	private RelativeLayout rl_switch_chatroom_leave;
	
    private RelativeLayout rl_switch_delete_msg_when_exit_group;
    private RelativeLayout rl_switch_auto_accept_group_invitation;
    private RelativeLayout rl_switch_adaptive_video_encode;
	private RelativeLayout rl_custom_appkey;
    private RelativeLayout rl_custom_server;
	RelativeLayout rl_push_settings;
	private LinearLayout   ll_call_option;
	private RelativeLayout rl_mail_log;

	*/

    /**
     * Diagnose
     *//*
	private LinearLayout llDiagnose;
	*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_fragment_conversation_settings);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(EMClient.getInstance().getCurrentUser())) {
            btnLogout.setText(getString(R.string.button_logout) + "(" + EMClient.getInstance().getCurrentUser() + ")");
        }
        settingsModel = SuperWeChatHelper.getInstance().getModel();
        chatOptions = EMClient.getInstance().getOptions();
        // the vibrate and sound notification are allowed or not?
        if (settingsModel.getSettingMsgNotification()) {
            switchNotification.openSwitch();
        } else {
            switchNotification.closeSwitch();
        }

        // sound notification is switched on or not?
        if (settingsModel.getSettingMsgSound()) {
            switchSound.openSwitch();
        } else {
            switchSound.closeSwitch();
        }

        // vibrate notification is switched on or not?
        if (settingsModel.getSettingMsgVibrate()) {
            switchVibrate.openSwitch();
        } else {
            switchVibrate.closeSwitch();
        }

        // the speaker is switched on or not?
        if (settingsModel.getSettingMsgSpeaker()) {
            switchSpeaker.openSwitch();
        } else {
            switchSpeaker.closeSwitch();
        }

        // if allow owner leave
        if (settingsModel.isChatroomOwnerLeaveAllowed()) {
            switchOwnerLeave.openSwitch();
        } else {
            switchOwnerLeave.closeSwitch();
        }

        // delete messages when exit group?
        if (settingsModel.isDeleteMessagesAsExitGroup()) {
            switchDeleteMsgWhenExitGroup.openSwitch();
        } else {
            switchDeleteMsgWhenExitGroup.closeSwitch();
        }

        if (settingsModel.isAutoAcceptGroupInvitation()) {
            switchAutoAcceptGroupInvitation.openSwitch();
        } else {
            switchAutoAcceptGroupInvitation.closeSwitch();
        }

        if (settingsModel.isAdaptiveVideoEncode()) {
            switchAdaptiveVideoEncode.openSwitch();
            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(false);
        } else {
            switchAdaptiveVideoEncode.closeSwitch();
            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(true);
        }

        if (settingsModel.isCustomServerEnable()) {
            switchCustomServer.openSwitch();
        } else {
            switchCustomServer.closeSwitch();
        }

        if (settingsModel.isCustomAppkeyEnabled()) {
            switchCustomAppkey.openSwitch();
        } else {
            switchCustomAppkey.closeSwitch();
        }
        editCustomAppkey.setEnabled(settingsModel.isCustomAppkeyEnabled());

        editCustomAppkey.setText(settingsModel.getCutomAppkey());
        editCustomAppkey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                PreferenceManager.getInstance().setCustomAppkey(s.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //red packet code : 进入零钱页面
            case R.id.ll_change:
                RedPacketUtil.startChangeActivity(SettingsActivity.this);
                break;
            //end of red packet code
            case R.id.rl_switch_notification:
                if (switchNotification.isSwitchOpen()) {
                    switchNotification.closeSwitch();
                    rlSwitchSound.setVisibility(View.GONE);
                    switchVibrate.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    textview2.setVisibility(View.GONE);
                    settingsModel.setSettingMsgNotification(false);
                } else {
                    switchNotification.openSwitch();
                    rlSwitchSound.setVisibility(View.VISIBLE);
                    switchVibrate.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    textview2.setVisibility(View.VISIBLE);
                    settingsModel.setSettingMsgNotification(true);
                }
                break;
            case R.id.rl_switch_sound:
                if (switchSound.isSwitchOpen()) {
                    switchSound.closeSwitch();
                    settingsModel.setSettingMsgSound(false);
                } else {
                    switchSound.openSwitch();
                    settingsModel.setSettingMsgSound(true);
                }
                break;
            case R.id.rl_switch_vibrate:
                if (switchVibrate.isSwitchOpen()) {
                    switchVibrate.closeSwitch();
                    settingsModel.setSettingMsgVibrate(false);
                } else {
                    switchVibrate.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.rl_switch_speaker:
                if (switchSpeaker.isSwitchOpen()) {
                    switchSpeaker.closeSwitch();
                    settingsModel.setSettingMsgSpeaker(false);
                } else {
                    switchSpeaker.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.rl_switch_chatroom_owner_leave:
                if (switchOwnerLeave.isSwitchOpen()) {
                    switchOwnerLeave.closeSwitch();
                    settingsModel.allowChatroomOwnerLeave(false);
                    chatOptions.allowChatroomOwnerLeave(false);
                } else {
                    switchOwnerLeave.openSwitch();
                    settingsModel.allowChatroomOwnerLeave(true);
                    chatOptions.allowChatroomOwnerLeave(true);
                }
                break;
            case R.id.rl_switch_delete_msg_when_exit_group:
                if (switchDeleteMsgWhenExitGroup.isSwitchOpen()) {
                    switchDeleteMsgWhenExitGroup.closeSwitch();
                    settingsModel.setDeleteMessagesAsExitGroup(false);
                    chatOptions.setDeleteMessagesAsExitGroup(false);
                } else {
                    switchDeleteMsgWhenExitGroup.openSwitch();
                    settingsModel.setDeleteMessagesAsExitGroup(true);
                    chatOptions.setDeleteMessagesAsExitGroup(true);
                }
                break;
            case R.id.rl_switch_auto_accept_group_invitation:
                if (switchAutoAcceptGroupInvitation.isSwitchOpen()) {
                    switchAutoAcceptGroupInvitation.closeSwitch();
                    settingsModel.setAutoAcceptGroupInvitation(false);
                    chatOptions.setAutoAcceptGroupInvitation(false);
                } else {
                    switchAutoAcceptGroupInvitation.openSwitch();
                    settingsModel.setAutoAcceptGroupInvitation(true);
                    chatOptions.setAutoAcceptGroupInvitation(true);
                }
                break;
            case R.id.rl_switch_adaptive_video_encode:
                EMLog.d("switch", "" + !switchAdaptiveVideoEncode.isSwitchOpen());
                if (switchAdaptiveVideoEncode.isSwitchOpen()) {
                    switchAdaptiveVideoEncode.closeSwitch();
                    settingsModel.setAdaptiveVideoEncode(false);
                    EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(true);

                } else {
                    switchAdaptiveVideoEncode.openSwitch();
                    settingsModel.setAdaptiveVideoEncode(true);
                    EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(false);
                }
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.ll_black_list:
                startActivity(new Intent(SettingsActivity.this, BlacklistActivity.class));
                break;
            case R.id.ll_diagnose:
                startActivity(new Intent(SettingsActivity.this, DiagnoseActivity.class));
                break;
            case R.id.ll_set_push_nick:
                startActivity(new Intent(SettingsActivity.this, OfflinePushNickActivity.class));
                break;
            case R.id.ll_call_option:
                startActivity(new Intent(SettingsActivity.this, CallOptionActivity.class));
                break;
            case R.id.ll_user_profile:
                startActivity(new Intent(SettingsActivity.this, UserProfileActivity.class).putExtra("setting", true)
                        .putExtra("username", EMClient.getInstance().getCurrentUser()));
                break;
            case R.id.switch_custom_server:
                if (switchCustomServer.isSwitchOpen()) {
                    switchCustomServer.closeSwitch();
                    settingsModel.enableCustomServer(false);
                } else {
                    switchCustomServer.openSwitch();
                    settingsModel.enableCustomServer(true);
                }
                break;
            case R.id.switch_custom_appkey:
                if (switchCustomAppkey.isSwitchOpen()) {
                    switchCustomAppkey.closeSwitch();
                    settingsModel.enableCustomAppkey(false);
                } else {
                    switchCustomAppkey.openSwitch();
                    settingsModel.enableCustomAppkey(true);
                }
                editCustomAppkey.setEnabled(switchCustomAppkey.isSwitchOpen());
                break;
            case R.id.rl_custom_server:
                startActivity(new Intent(SettingsActivity.this, SetServersActivity.class));
                break;
            case R.id.rl_push_settings:
                startActivity(new Intent(SettingsActivity.this, OfflinePushSettingsActivity.class));
                break;
            case R.id.rl_mail_log:
                sendLogThroughMail();
                break;
            default:
                break;
        }
    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(SettingsActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        SuperWeChatHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // show login screen
                        MFGT.finish(SettingsActivity.this);
                        MFGT.gotoLoginActivity(SettingsActivity.this);

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(SettingsActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void sendLogThroughMail() {
        String logPath = "";
        try {
            logPath = EMClient.getInstance().compressLogs();
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SettingsActivity.this, "compress logs failed", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        File f = new File(logPath);
        File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (f.exists() && f.canRead()) {
            try {
                storage.mkdirs();
                File temp = File.createTempFile("hyphenate", ".log.gz", storage);
                if (!temp.canWrite()) {
                    return;
                }
                boolean result = f.renameTo(temp);
                if (result == false) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "log");
                intent.putExtra(Intent.EXTRA_TEXT, "log in attachment: " + temp.getAbsolutePath());

                intent.setType("application/octet-stream");
                ArrayList<Uri> uris = new ArrayList<>();
                uris.add(Uri.fromFile(temp));
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                startActivity(intent);
            } catch (final Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
