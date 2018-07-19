package mybanner.toerax.com.sendsms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mybanner.toerax.com.sendsms.base.BaseToolbarActivity;

public class SettingActivity extends BaseToolbarActivity {


    @BindView(R.id.ipAddress)
    EditText mIpAddress;
    @BindView(R.id.port)
    EditText mPort;
    @BindView(R.id.key)
    EditText mKey;
    @BindView(R.id.save)
    Button mSave;
    public static final String LOGIN_SP_INFO = "Info";
    @BindView(R.id.phone)
    EditText mPhone;


    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initBase() {

    }

    @Override
    protected void initView() {
        SharedPreferences sp = APP.getInstance().getSharedPreferences(LOGIN_SP_INFO, 0);
        initToobar(-1, "参数设置", -1);
        mIpAddress.setText(sp.getString("IP", "sc.ftqq.com"));
        mPort.setText(sp.getString("port", "80"));
        mKey.setText(sp.getString("key", "SCU28677T61434f8fc1d7c4c5c08e6aeba795e4165b371bc71a693"));
        mPhone.setText(sp.getString("phone", ""));
    }

    @OnClick(R.id.save)
    public void onViewClicked() {
        if (mIpAddress.getText().toString().trim().isEmpty()) {
            ShowToast("ip地址不能为空");
        } else if (mKey.getText().toString().trim().isEmpty()) {
            ShowToast("key不能为空");
        } else {
            SharedPreferences sp = APP.getInstance().getSharedPreferences(LOGIN_SP_INFO, 0);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("IP", mIpAddress.getText().toString().trim());
            edit.putString("port", mPort.getText().toString().trim());
            edit.putString("key", mKey.getText().toString().trim());
            edit.putString("phone", mPhone.getText().toString().trim());
            edit.apply();
            if (mPort.getText().toString().trim().isEmpty()) {
                ShowToast("端口号为空!!");
            }
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
