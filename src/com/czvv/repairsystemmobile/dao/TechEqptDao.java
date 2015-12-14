package com.czvv.repairsystemmobile.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.czvv.repairsystemmobile.bean.TechEqptBean;
import com.czvv.repairsystemmobile.bean.TechEqptBean.TechEqpt;
import com.czvv.repairsystemmobile.bean.UserInfoBean;
import com.czvv.repairsystemmobile.bean.TechEqptBean.TechEqpt;
import com.czvv.repairsystemmobile.bean.UserInfoBean.UserInfo;
import com.czvv.repairsystemmobile.db.DBHelper;
import com.czvv.repairsystemmobile.service.Sys_deptService;
import com.czvv.repairsystemmobile.service.Sys_userService;
import com.czvv.repairsystemmobile.service.TechService;
import com.czvv.repairsystemmobile.utils.BooleanAndintUtil;

public class TechEqptDao implements TechService {

	private DBHelper helper;

	private TechEqptDao(Context context) {
		helper = new DBHelper(context);
	}

	public static TechEqptDao instance;

	public synchronized static TechEqptDao getInstance(Context context) {
		if (instance == null) {
			instance = new TechEqptDao(context);
		}
		return instance;
	}

	private final String TABLE_NAME = "Tech_Eqpt";

	@Override
	public boolean addTechEqpt(TechEqpt techEqpt) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("TechEqptID", techEqpt.TechEqptID);
			values.put("EqptName", techEqpt.EqptName);
			values.put("dept_id", techEqpt.dept_id);
			values.put("EqptAddress", techEqpt.EqptAddress);
			values.put("EqptUse", techEqpt.EqptUse);
			values.put("EqptOwner", techEqpt.EqptOwner);
			values.put("EqptStatus", techEqpt.EqptStatus);
			values.put("InDate", techEqpt.InDate);
			values.put("CreateDate", techEqpt.CreateDate);
			values.put("LastUpdateDate", techEqpt.LastUpdateDate);
			values.put("EqptInfoID", techEqpt.EqptInfoID);
			values.put("WorkAreaID", techEqpt.WorkAreaID);
			values.put("EqptType", techEqpt.EqptType);
			values.put("RepairDeptID", techEqpt.RepairDeptID);
			database.insert(TABLE_NAME, null, values);
			flag = true;
		} catch (Exception e) {
			System.out.println("----addTechEqpt-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean deleteAllTechEqpt() {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from " + TABLE_NAME;
			database = helper.getWritableDatabase();// 实现对数据库的写的操作
			database.execSQL(sql);
			flag = true;
		} catch (Exception e) {
			System.out.println("----deleteAllTechEqpt-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public List<TechEqpt> getAllTechEqptList() {
		List<TechEqpt> list = new ArrayList<TechEqpt>();
		TechEqptBean bean = new TechEqptBean();
		TechEqpt techEqpt;
		String sql = "select * from " + TABLE_NAME;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				techEqpt = bean.new TechEqpt();
				techEqpt.TechEqptID = cursor.getString(cursor
						.getColumnIndex("TechEqptID"));
				techEqpt.EqptName = cursor.getString(cursor
						.getColumnIndex("EqptName"));
				techEqpt.dept_id = cursor.getString(cursor
						.getColumnIndex("dept_id"));
				techEqpt.EqptAddress = cursor.getString(cursor
						.getColumnIndex("EqptAddress"));
				techEqpt.EqptUse = cursor.getString(cursor
						.getColumnIndex("EqptUse"));
				techEqpt.EqptOwner = cursor.getString(cursor
						.getColumnIndex("EqptOwner"));
				techEqpt.EqptStatus = cursor.getString(cursor
						.getColumnIndex("EqptStatus"));
				techEqpt.InDate = cursor.getString(cursor
						.getColumnIndex("InDate"));
				techEqpt.CreateDate = cursor.getString(cursor
						.getColumnIndex("CreateDate"));
				techEqpt.LastUpdateDate = cursor.getString(cursor
						.getColumnIndex("LastUpdateDate"));
				techEqpt.EqptInfoID = cursor.getString(cursor
						.getColumnIndex("EqptInfoID"));
				techEqpt.WorkAreaID = cursor.getString(cursor
						.getColumnIndex("WorkAreaID"));
				techEqpt.EqptType = cursor.getString(cursor
						.getColumnIndex("EqptType"));
				techEqpt.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
				list.add(techEqpt);
				techEqpt = null;
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
	public String getTechEqptID(String EqptInfoID) {

		String techEqptID = null;
		SQLiteDatabase database = null;

		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME,
					new String[] { "TechEqptID" }, "EqptInfoID = ?",
					new String[] { EqptInfoID }, null, null, null);
			if (cursor.moveToNext()) {
				techEqptID = cursor.getString(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return techEqptID;
	}

}
