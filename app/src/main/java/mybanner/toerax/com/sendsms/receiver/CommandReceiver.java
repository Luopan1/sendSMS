package mybanner.toerax.com.sendsms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mybanner.toerax.com.sendsms.bean.Message;
import mybanner.toerax.com.sendsms.db.DBManage;
import mybanner.toerax.com.sendsms.network.Constants;
import mybanner.toerax.com.sendsms.network.NetworkUtil;

/**
 * Created by 19233 on 2018/7/17 18:07.
 */
public class CommandReceiver extends BroadcastReceiver {
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    public final String LOGIN_SP_INFO = "Info";

    @Override
    public void onReceive(final Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        Log.e("TAG++++", "收到短信了！！！！");
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    final String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    final String message = currentMessage.getDisplayMessageBody()+">>发送者:"+senderNum;

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                    if (NetworkUtil.isAvailable(context)) {
                        Log.e("TAG+++","有网络~");
                        OkGo.<String>post(Constants.httpHost + Constants.key + ".send?text=" + senderNum)
                                .params("desp", message).execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                JSONObject parse = JSON.parseObject(response.body());
                                Message msg = new Message();
                                msg.setPhone(phoneNumber);
                                msg.setTime(toLongTimeString(new Date(System.currentTimeMillis())));
                                msg.setMessage(message);
                                if (parse.getInteger("errno") == 0) {
                                    msg.setStatue("转发成功");
                                } else {
                                    msg.setStatue("转发失败");
                                    transmitMessageTo(Constants.phone, message, phoneNumber);
                                }
                                DBManage dbManager = new DBManage(context);
                                dbManager.insertCollect(JSON.toJSONString(msg));
                            }

                            @Override
                            public void onError(Response<String> response) {
                                Log.e("TAG+++","失败~");
                                transmitMessageTo(Constants.phone, message, phoneNumber);
                            }
                        });

                    }else {
                        Log.e("TAG+++","没有网络~");
                        Log.e("TAG++++",Constants.phone);
                        transmitMessageTo(Constants.phone, message, phoneNumber);
                    }
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }

    public String toLongTimeString(Date dt) {
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return myFmt.format(dt);
    }

    public void transmitMessageTo(String phoneNumber, String message, String srcAddress) {//转发短信
        SmsManager manager = SmsManager.getDefault();
        /** 切分短信，每七十个汉字切一个，短信长度限制不足七十就只有一个：返回的是字符串的List集合*/
        List<String> texts = manager.divideMessage(message);//这个必须有
        for (String text : texts) {
            manager.sendTextMessage(phoneNumber, null, text, null, null);
        }
    }

}