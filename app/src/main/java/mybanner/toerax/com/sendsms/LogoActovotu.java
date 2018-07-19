package mybanner.toerax.com.sendsms;

import android.Manifest;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import mybanner.toerax.com.sendsms.base.BaseToolbarActivity;
import mybanner.toerax.com.sendsms.view.CountDownProgressView;

public class LogoActovotu extends BaseToolbarActivity {


    @BindView(R.id.LogoImage)
    ImageView mLogoImage;
    @BindView(R.id.CountdownProgressView)
    CountDownProgressView mCountdownProgressView;

    @Override
    protected int getContentView() {
        return R.layout.activity_logo_actovotu;
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
        Glide.with(this).load(R.mipmap.logo).asBitmap().into(mLogoImage);
        mCountdownProgressView.setTimeMillis(3000);
        mCountdownProgressView.setEnabled(false);
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.RECEIVE_BOOT_COMPLETED
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (aBoolean) {
                    mCountdownProgressView.start();
                    mCountdownProgressView.setEnabled(true);
                } else {
                    ShowToastLong(getString(R.string.nopermission));
                }
            }
        });
        mCountdownProgressView.setProgressListener(new CountDownProgressView.OnProgressListener() {
            @Override
            public void onProgress(int progress) {
                if (progress == 0) {
                    jumpToActivity(MainActivity.class, true);
                }

            }
        });
        mCountdownProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowToastLong(getString(R.string.watchDog));
            }
        });

    }

    @Override
    public void setFullScreen() {
        isFullScreen = true;
        isShowToolBar = false;
        isShowBackImage = false;
    }

}
