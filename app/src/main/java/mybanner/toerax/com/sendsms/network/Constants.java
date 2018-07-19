package mybanner.toerax.com.sendsms.network;

import mybanner.toerax.com.sendsms.APP;

/**
 * @author LuoPan on 2018/3/8 11:17.
 */

public class Constants {
    public static final String LOGIN_SP_INFO = "Info";
    public static String httpHost = "https://" + APP.getInstance().getSharedPreferences(LOGIN_SP_INFO, 0).getString("IP", "sc.ftqq.com") + "/";
    public static String key = APP.getInstance().getSharedPreferences(LOGIN_SP_INFO, 0).getString("key", "SCU28677T61434f8fc1d7c4c5c08e6aeba795e4165b371bc71a693");
    public static String phone = APP.getInstance().getSharedPreferences(LOGIN_SP_INFO, 0).getString("phone", "");
}
