package mybanner.toerax.com.sendsms.network;

import android.content.Context;
import android.util.Log;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


/**
 * Created by jie on 2017/3/15.
 */

public class CookieManger implements CookieJar {


    private static Context mContext;

    public static PersistentCookieStore cookieStore;

    public CookieManger(Context context) {
        mContext = context;
        if (cookieStore == null ) {
            cookieStore = new PersistentCookieStore(mContext);
        }

    }



    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                Log.e("======","===="+item.toString());
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies =cookieStore.get(url);
        return cookies;
    }

}
