package mybanner.toerax.com.sendsms;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mybanner.toerax.com.sendsms.adapter.BaseRecyclerAdapter;
import mybanner.toerax.com.sendsms.adapter.BaseRecyclerHolder;
import mybanner.toerax.com.sendsms.base.BaseToolbarActivity;
import mybanner.toerax.com.sendsms.bean.Message;
import mybanner.toerax.com.sendsms.network.Constants;
import mybanner.toerax.com.sendsms.network.LoadingDialog;
import mybanner.toerax.com.sendsms.receiver.AdminReceiver;
import mybanner.toerax.com.sendsms.services.MyServices;

public class MainActivity extends BaseToolbarActivity {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    BaseRecyclerAdapter<Message> mAdapter;
    public static final String LOGIN_SP_INFO = "Info";
    List<Message> mMessagesLists = new ArrayList<>();
    private UpDataUi mUpDataUi;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        getData data = new getData();
        data.execute();

    }

    @Override
    public void RightOnClick() {
        jumpToActivity(SettingActivity.class, false);
    }

    @Override
    protected void setListener() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

    }

    @Override
    protected void initView() {
        initToobar(-1, "转发记录", R.mipmap.shezhi);
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(MainActivity.this, MyServices.class);
                startService(intent);
            } else {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Toast.makeText(MainActivity.this, "Em...悬浮窗可以简单保活", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        } else {
            //SDK在23以下，不用管.
            Intent intent = new Intent(MainActivity.this, MyServices.class);
            startService(intent);

        }
        DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE); //获取设备管理器
        ComponentName componentName = new ComponentName(this, AdminReceiver.class); //创建设备组件 需要创建AdminReceiver
        if (policyManager.isAdminActive(componentName)) {

        } else {
            Toast.makeText(MainActivity.this, "必须先激活设备管理器！", Toast.LENGTH_SHORT).show();
            activateManage();
        }

        mUpDataUi = new UpDataUi();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(800);
        registerReceiver(mUpDataUi, filter);


    }

    public class getData extends AsyncTask<Void, Void, List<Message>> {
        @Override
        protected void onPreExecute() {
            LoadingDialog.createLoadingDialog(MainActivity.this, "查询中....");
        }

        @Override
        protected List<Message> doInBackground(Void... voids) {
            return mDBManage.queryCollect();
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            mMessagesLists.clear();
            mMessagesLists.addAll(messages);
            if (mAdapter == null) {
                mAdapter = new BaseRecyclerAdapter<Message>(MainActivity.this, mMessagesLists, R.layout.item_sms) {
                    @Override
                    public void convert(BaseRecyclerHolder holder, Message item, int position, boolean isScrolling) {
                        holder.setText(R.id.phoneNumber, item.getPhone());
                        holder.setText(R.id.time, item.getTime());
                        holder.setText(R.id.message, item.getMessage());
                        holder.setText(R.id.statue, item.getStatue());
                        TextView status = holder.getView(R.id.statue);
                        if (item.getStatue().equals("转发成功")) {
                            status.setTextColor(getResources().getColor(R.color.black));
                        } else {
                            status.setTextColor(getResources().getColor(R.color.color_red_d1021c));
                        }
                    }
                };
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
            LoadingDialog.closeDialog();
            mRefresh.setRefreshing(false);

        }
    }

    @Override
    protected void initBase() {
        isShowBackImage = false;
        isShowToolBar = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.httpHost = "https://" + APP.getInstance().getSharedPreferences(LOGIN_SP_INFO, 0).getString("IP", "sc.ftqq.com") + "/";
        Constants.key = APP.getInstance().getSharedPreferences(LOGIN_SP_INFO, 0).getString("key", "SCU28677T61434f8fc1d7c4c5c08e6aeba795e4165b371bc71a693");
        Constants.phone = APP.getInstance().getSharedPreferences(LOGIN_SP_INFO, 0).getString("phone", "无电话");
        Log.e("TAG++re",Constants.phone+"............");
    }

    private void activateManage() {
        // 隐式启动
        ComponentName componentName = new ComponentName(mContext, AdminReceiver.class);
        //权限列表
        //EXTRA_DEVICE_ADMIN参数中说明了用到哪些权限，
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //附加数据
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        //描述
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "可以进行保活啊!!!");
        startActivity(intent);
    }


    public class UpDataUi extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUpDataUi != null)
            unregisterReceiver(mUpDataUi);
    }
}