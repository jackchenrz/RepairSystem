package com.czvv.repairsystemmobile.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.czvv.repairsystemmobile.bean.UserInfoBean;
import com.czvv.repairsystemmobile.bean.UserInfoBean.UserInfo;
import com.czvv.repairsystemmobile.db.DBHelper;
import com.czvv.repairsystemmobile.service.Sys_userService;
import com.czvv.repairsystemmobile.utils.BooleanAndintUtil;

public class Sys_userDao implements Sys_userService {

	private DBHelper helper;

	private Sys_userDao(Context context) {
		helper = new DBHelper(context);
	}

	public static Sys_userDao instance;

	public synchronized static Sys_userDao getInstance(Context context) {
		if (instance == null) {
			instance = new Sys_userDao(context);
		}
		return instance;
	}

	private final String TABLE_NAME = "sys_user";

	@Override
	public boolean addUserLog(UserInfo userInfo) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("user_id", userInfo.user_id);
			values.put("real_name", userInfo.real_name);
			values.put("user_name", userInfo.user_name);
			values.put("password", userInfo.password);
			values.put("home_tel", userInfo.home_tel);
			values.put("office_tel", userInfo.office_tel);
			values.put("create_time", userInfo.create_time);
			values.put("modi_pwd_time", userInfo.modi_pwd_time);
			values.put("recent_access", userInfo.recent_access);
			values.put("is_admin",
					BooleanAndintUtil.Boolean2int(userInfo.is_admin));
			values.put("is_used",
					BooleanAndintUtil.Boolean2int(userInfo.is_used));
			values.put("is_lock",
					BooleanAndintUtil.Boolean2int(userInfo.is_lock));
			values.put("sex", userInfo.sex);
			values.put("email", userInfo.email);
			values.put("remark", userInfo.remark);
			values.put("error_login_times", userInfo.error_login_times);
			values.put("sort_no", userInfo.sort_no);
			values.put("dept_id", userInfo.dept_id);
			values.put("dept_bz_id", userInfo.dept_bz_id);
			values.put("isrepair",
					BooleanAndintUtil.Boolean2int(userInfo.isrepair));
			values.put("isleader",
					BooleanAndintUtil.Boolean2int(userInfo.isleader));

			database.insert(TABLE_NAME, null, values);
			flag = true;
		} catch (Exception e) {
			System.out.println("----addUserLog-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean deleteAllUserLog() {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from " + TABLE_NAME;
			database = helper.getWritableDatabase();// 实现对数据库的写的操作
			database.execSQL(sql);
			flag = true;
		} catch (Exception e) {
			System.out.println("----deleteAllUserLog-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public List<UserInfo> getAllUserLogList() {
		List<UserInfo> list = new ArrayList<UserInfo>();
		UserInfoBean bean = new UserInfoBean();
		UserInfo userInfo;
		String sql = "select * from " + TABLE_NAME;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				userInfo = bean.new UserInfo();
				userInfo.user_id = cursor.getString(cursor
						.getColumnIndex("user_id"));
				userInfo.real_name = cursor.getString(cursor
						.getColumnIndex("real_name"));
				userInfo.user_name = cursor.getString(cursor
						.getColumnIndex("user_name"));
				userInfo.password = cursor.getString(cursor
						.getColumnIndex("password"));
				userInfo.home_tel = cursor.getString(cursor
						.getColumnIndex("home_tel"));
				userInfo.office_tel = cursor.getString(cursor
						.getColumnIndex("office_tel"));
				userInfo.create_time = cursor.getString(cursor
						.getColumnIndex("create_time"));
				userInfo.modi_pwd_time = cursor.getString(cursor
						.getColumnIndex("modi_pwd_time"));
				userInfo.recent_access = cursor.getString(cursor
						.getColumnIndex("recent_access"));
				userInfo.is_admin = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_admin")));
				userInfo.is_used = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_used")));
				userInfo.is_lock = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_lock")));
				userInfo.sex = cursor.getString(cursor.getColumnIndex("sex"));
				userInfo.email = cursor.getString(cursor
						.getColumnIndex("email"));
				userInfo.remark = cursor.getString(cursor
						.getColumnIndex("remark"));
				userInfo.error_login_times = cursor.getString(cursor
						.getColumnIndex("error_login_times"));
				userInfo.sort_no = cursor.getString(cursor
						.getColumnIndex("sort_no"));
				userInfo.dept_id = cursor.getString(cursor
						.getColumnIndex("dept_id"));
				userInfo.dept_bz_id = cursor.getString(cursor
						.getColumnIndex("dept_bz_id"));
				userInfo.isrepair = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_admin")));
				userInfo.isleader = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("isleader")));
				list.add(userInfo);
				userInfo = null;
			}
		} catch (Exception e) {
			System.out.println("----getAllUserLogList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	@Override
	public boolean avaiLogin(String userName, String pwd) {
		SQLiteDatabase database = null;
		boolean flag = false;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null,
					"user_name = ? and password = ? ", new String[] { userName,
							pwd }, null, null, null);
			if (cursor.moveToNext()) {
				flag = true;
			}
		} catch (Exception e) {
			System.out.println("----getUserInfo-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public UserInfo getUserInfo(String userNmae) {
		UserInfoBean bean = new UserInfoBean();
		UserInfo userInfo = null;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "user_name = ?",
					new String[] { userNmae }, null, null, null);
			if (cursor.moveToNext()) {
				userInfo = bean.new UserInfo();
				userInfo.user_id = cursor.getString(cursor
						.getColumnIndex("user_id"));
				userInfo.real_name = cursor.getString(cursor
						.getColumnIndex("real_name"));
				userInfo.user_name = cursor.getString(cursor
						.getColumnIndex("user_name"));
				userInfo.password = cursor.getString(cursor
						.getColumnIndex("password"));
				userInfo.home_tel = cursor.getString(cursor
						.getColumnIndex("home_tel"));
				userInfo.office_tel = cursor.getString(cursor
						.getColumnIndex("office_tel"));
				userInfo.create_time = cursor.getString(cursor
						.getColumnIndex("create_time"));
				userInfo.modi_pwd_time = cursor.getString(cursor
						.getColumnIndex("modi_pwd_time"));
				userInfo.recent_access = cursor.getString(cursor
						.getColumnIndex("recent_access"));
				userInfo.is_admin = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_admin")));
				userInfo.is_used = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_used")));
				userInfo.is_lock = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_lock")));
				userInfo.sex = cursor.getString(cursor.getColumnIndex("sex"));
				userInfo.email = cursor.getString(cursor
						.getColumnIndex("email"));
				userInfo.remark = cursor.getString(cursor
						.getColumnIndex("remark"));
				userInfo.error_login_times = cursor.getString(cursor
						.getColumnIndex("error_login_times"));
				userInfo.sort_no = cursor.getString(cursor
						.getColumnIndex("sort_no"));
				userInfo.dept_id = cursor.getString(cursor
						.getColumnIndex("dept_id"));
				userInfo.dept_bz_id = cursor.getString(cursor
						.getColumnIndex("dept_bz_id"));
				userInfo.isrepair = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("is_admin")));
				userInfo.isleader = BooleanAndintUtil.int2Boolean(cursor
						.getInt(cursor.getColumnIndex("isleader")));
			}
		} catch (Exception e) {
			System.out.println("----getUserInfo-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return userInfo;
	}
}
