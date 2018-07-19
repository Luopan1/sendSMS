package mybanner.toerax.com.sendsms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库
 *
 * @author wu cheng hong
 * @ClassName: DBHelper
 * @Description: TODO
 * @date 2016-1-20 下午3:44:04
 */
public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper dbHelper = null;
	private static final String DATABASE_NAME = "sendsms.db";
	private static int version = 1;
	private Context context;

	protected DBHelper(Context context) {
		super(context, DATABASE_NAME, null, version);
		this.context=context;
	}

	public static DBHelper getInstance(Context context) {
		if (null == dbHelper) {
			dbHelper = new DBHelper(context);
		}
		return dbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//				context.deleteDatabase(DATABASE_NAME);
		// 足迹1
		StringBuffer sqlCollect = new StringBuffer();
		sqlCollect
				.append("create table " + DBManage.VIEWED
						+ "( _id integer primary key autoincrement , ")
				.append("merchantsName text ,")
				.append("time timestamp ,")
				.append("brand text )");
		db.execSQL(sqlCollect.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion==2){
				db.execSQL("ALTER TABLE  " + DBManage.VIEWED
						+ "  ADD COLUMN sellTime");
			}

	}
}


