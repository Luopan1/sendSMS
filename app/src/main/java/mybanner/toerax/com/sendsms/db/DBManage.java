package mybanner.toerax.com.sendsms.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mybanner.toerax.com.sendsms.bean.Message;

public class DBManage {

    private DBHelper dbHelper = null;

    private Context context;

    public static final String VIEWED = "sendems";//足迹

    public DBManage(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
    }


    public void insertCollect(String jsonObject) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("merchantsName", jsonObject);
        values.put("time", System.currentTimeMillis());
        db.insert(DBManage.VIEWED, null, values);
        db.close();
    }







	/*private boolean isAddedCollect(GoodPrice goodPrice) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String sql = "select * from " + VIEWED
				+ " where id = ? ";
		Cursor mcursor = db.rawQuery(sql, new String[]{goodPrice.getId()});
		if (mcursor != null) {
			while (mcursor.moveToNext()) {
				// id存在  就删除 然后在添加
				if (mcursor.getString(mcursor.getColumnIndex("id")).equals(goodPrice.getId())) {
					db.delete(DBManage.VIEWED,"id=?",new String[]{goodPrice.getId()});
					return false;
				}
			}
			mcursor.close();
		}
		return false;
	}
	*/

    public String toLongTimeString(Date dt) {
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
        return myFmt.format(dt);
    }

    /*	*/

    /**
     * 查询数据库中收藏的足迹
     *
     * @return
     **/
    public ArrayList<Message> queryCollect() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<Message> mNewsInfos = new ArrayList<Message>();
        try {
            String sql = "select * from  " + DBManage.VIEWED
                    + " order by time desc";
            cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Message entity = new Message();
                    String merchantsName = cursor.getString(cursor.getColumnIndex("merchantsName"));
                    //				entity.(merchantsName);
                    Log.e("TAG+++++item",merchantsName);
                    JSONObject jsonObject = JSON.parseObject(merchantsName);
                    entity.setMessage(jsonObject.getString("message"));
                    entity.setPhone(jsonObject.getString("phone"));
                    entity.setStatue(jsonObject.getString("statue"));
                    entity.setTime(jsonObject.getString("time"));
                    mNewsInfos.add(entity);
                }
                db.close();
            }
        } catch (Exception e) {

        }
        return mNewsInfos;
    }


	/*public void deleteFootPrintByTime(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		ArrayList<GoodPrice> mNewsInfos = new ArrayList<GoodPrice>();
		String sql = "select * from  " + DBManage.VIEWED
				+ " order by time desc";
		cursor = db.rawQuery(sql, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String time = cursor.getString(cursor.getColumnIndex("time"));
				//7天之内的保存  不是7天之内的就删除
				if (!isLatestWeek(new Date(Long.parseLong(time)))){
						String id = cursor.getString(cursor.getColumnIndex("id"));
						db.delete(DBManage.VIEWED,"id=?",new String[]{id});
				}
			}
			db.close();
		}

	}*/

    /**
     * 记录浏览的新闻id
     *
     * @param id
     * @param time
     */
    // String.valueOf(System.currentTimeMillis())时间戳
    public void insertNewsId(String id, String time) {
        if (!id.equals("")) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "select * from " + VIEWED
                    + " where newsId = ? ";
            Cursor cursor = db.rawQuery(sql, new String[]{id});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    ContentValues value = new ContentValues();
                    value.put("datetime", time);
                    db.update(VIEWED, value, " newsId = ?", new String[]{id});
                } else {
                    ContentValues values = new ContentValues();
                    values.put("newsId", id);
                    values.put("datetime", time);
                    db.insert(VIEWED, null, values);
                }
                cursor.close();
            }
        } else {
            System.out.println("d");
        }
    }


}
