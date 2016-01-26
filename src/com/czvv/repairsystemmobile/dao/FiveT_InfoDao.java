package com.czvv.repairsystemmobile.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.czvv.repairsystemmobile.bean.FiveTEqptInfoBean;
import com.czvv.repairsystemmobile.bean.FiveTEqptInfoBean.FiveTEqpt;
import com.czvv.repairsystemmobile.db.DBHelper;
import com.czvv.repairsystemmobile.service.FiveTService;

public class FiveT_InfoDao implements FiveTService {

	private DBHelper helper;

	private FiveT_InfoDao(Context context) {
		helper = new DBHelper(context);
	}

	public static FiveT_InfoDao instance;

	public synchronized static FiveT_InfoDao getInstance(Context context) {
		if (instance == null) {
			instance = new FiveT_InfoDao(context);
		}
		return instance;
	}

	private final String TABLE_NAME = "FiveT_Eqpt";

	@Override
	public boolean addFiveTEqpt(FiveTEqpt fiveTEqpt) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("EqptID", fiveTEqpt.EqptID);
			values.put("EqptType", fiveTEqpt.EqptType);
			values.put("ProbeStation", fiveTEqpt.ProbeStation);
			values.put("dept_id", fiveTEqpt.dept_id);
			values.put("EqptAddress", fiveTEqpt.EqptAddress);
			values.put("EqptUse", fiveTEqpt.EqptUse);
			values.put("EqptOwner", fiveTEqpt.EqptOwner);
			values.put("EqptStatus", fiveTEqpt.EqptStatus);
			values.put("InDate", fiveTEqpt.InDate);
			values.put("CreateDate", fiveTEqpt.CreateDate);
			values.put("LastUpdateDate", fiveTEqpt.LastUpdateDate);
			values.put("WorkAreaID", fiveTEqpt.WorkAreaID);
			values.put("EqptModel", fiveTEqpt.EqptModel);
			values.put("Manufacturer", fiveTEqpt.Manufacturer);
			values.put("RepairDeptID", fiveTEqpt.RepairDeptID);
			database.insert(TABLE_NAME, null, values);
			flag = true;
		} catch (Exception e) {
			System.out.println("----addFiveTEqpt-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean deleteAllFiveTEqpt() {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from " + TABLE_NAME;
			database = helper.getWritableDatabase();// 实现对数据库的写的操作
			database.execSQL(sql);
			flag = true;
		} catch (Exception e) {
			System.out.println("----deleteAllFiveTEqpt-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public List<FiveTEqpt> getAllFiveTEqptList() {
		List<FiveTEqpt> list = new ArrayList<FiveTEqpt>();
		FiveTEqptInfoBean bean = new FiveTEqptInfoBean();
		FiveTEqpt fiveTEqpt;
		String sql = "select * from " + TABLE_NAME;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				fiveTEqpt = bean.new FiveTEqpt();
				fiveTEqpt.EqptID = cursor.getString(cursor
						.getColumnIndex("EqptID"));
				fiveTEqpt.EqptType = cursor.getString(cursor
						.getColumnIndex("EqptType"));
				fiveTEqpt.ProbeStation = cursor.getString(cursor
						.getColumnIndex("ProbeStation"));
				fiveTEqpt.dept_id = cursor.getString(cursor
						.getColumnIndex("dept_id"));
				fiveTEqpt.EqptAddress = cursor.getString(cursor
						.getColumnIndex("EqptAddress"));
				fiveTEqpt.EqptUse = cursor.getString(cursor
						.getColumnIndex("EqptUse"));
				fiveTEqpt.EqptOwner = cursor.getString(cursor
						.getColumnIndex("EqptOwner"));
				fiveTEqpt.EqptStatus = cursor.getString(cursor
						.getColumnIndex("EqptStatus"));
				fiveTEqpt.InDate = cursor.getString(cursor
						.getColumnIndex("InDate"));
				fiveTEqpt.CreateDate = cursor.getString(cursor
						.getColumnIndex("CreateDate"));
				fiveTEqpt.LastUpdateDate = cursor.getString(cursor
						.getColumnIndex("LastUpdateDate"));
				fiveTEqpt.WorkAreaID = cursor.getString(cursor
						.getColumnIndex("WorkAreaID"));
				fiveTEqpt.EqptModel = cursor.getString(cursor
						.getColumnIndex("EqptModel"));
				fiveTEqpt.Manufacturer = cursor.getString(cursor
						.getColumnIndex("Manufacturer"));
				fiveTEqpt.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
				list.add(fiveTEqpt);
				fiveTEqpt = null;
			}
		} catch (Exception e) {
			System.out.println("----getAllFiveTEqptList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	@Override
	public FiveTEqpt getFiveTEqpt(String eqptName) {
		FiveTEqptInfoBean bean = new FiveTEqptInfoBean();
		FiveTEqpt fiveTEqpt = null;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "EqptAddress = ?",
					new String[] { eqptName }, null, null, null);
			if (cursor.moveToNext()) {
				fiveTEqpt = bean.new FiveTEqpt();
				fiveTEqpt.EqptID = cursor.getString(cursor
						.getColumnIndex("EqptID"));
				fiveTEqpt.EqptType = cursor.getString(cursor
						.getColumnIndex("EqptType"));
				fiveTEqpt.ProbeStation = cursor.getString(cursor
						.getColumnIndex("ProbeStation"));
				fiveTEqpt.dept_id = cursor.getString(cursor
						.getColumnIndex("dept_id"));
				fiveTEqpt.EqptAddress = cursor.getString(cursor
						.getColumnIndex("EqptAddress"));
				fiveTEqpt.EqptUse = cursor.getString(cursor
						.getColumnIndex("EqptUse"));
				fiveTEqpt.EqptOwner = cursor.getString(cursor
						.getColumnIndex("EqptOwner"));
				fiveTEqpt.EqptStatus = cursor.getString(cursor
						.getColumnIndex("EqptStatus"));
				fiveTEqpt.InDate = cursor.getString(cursor
						.getColumnIndex("InDate"));
				fiveTEqpt.CreateDate = cursor.getString(cursor
						.getColumnIndex("CreateDate"));
				fiveTEqpt.LastUpdateDate = cursor.getString(cursor
						.getColumnIndex("LastUpdateDate"));
				fiveTEqpt.WorkAreaID = cursor.getString(cursor
						.getColumnIndex("WorkAreaID"));
				fiveTEqpt.EqptModel = cursor.getString(cursor
						.getColumnIndex("EqptModel"));
				fiveTEqpt.Manufacturer = cursor.getString(cursor
						.getColumnIndex("Manufacturer"));
				fiveTEqpt.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
			}
		} catch (Exception e) {
			System.out.println("----getAllFiveTEqptList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return fiveTEqpt;
	}
	
	@Override
	public FiveTEqpt getFiveTEqpt1(String eqptID) {
		FiveTEqptInfoBean bean = new FiveTEqptInfoBean();
		FiveTEqpt fiveTEqpt = null;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "EqptID = ?",
					new String[] { eqptID }, null, null, null);
			if (cursor.moveToNext()) {
				fiveTEqpt = bean.new FiveTEqpt();
				fiveTEqpt.EqptID = cursor.getString(cursor
						.getColumnIndex("EqptID"));
				fiveTEqpt.EqptType = cursor.getString(cursor
						.getColumnIndex("EqptType"));
				fiveTEqpt.ProbeStation = cursor.getString(cursor
						.getColumnIndex("ProbeStation"));
				fiveTEqpt.dept_id = cursor.getString(cursor
						.getColumnIndex("dept_id"));
				fiveTEqpt.EqptAddress = cursor.getString(cursor
						.getColumnIndex("EqptAddress"));
				fiveTEqpt.EqptUse = cursor.getString(cursor
						.getColumnIndex("EqptUse"));
				fiveTEqpt.EqptOwner = cursor.getString(cursor
						.getColumnIndex("EqptOwner"));
				fiveTEqpt.EqptStatus = cursor.getString(cursor
						.getColumnIndex("EqptStatus"));
				fiveTEqpt.InDate = cursor.getString(cursor
						.getColumnIndex("InDate"));
				fiveTEqpt.CreateDate = cursor.getString(cursor
						.getColumnIndex("CreateDate"));
				fiveTEqpt.LastUpdateDate = cursor.getString(cursor
						.getColumnIndex("LastUpdateDate"));
				fiveTEqpt.WorkAreaID = cursor.getString(cursor
						.getColumnIndex("WorkAreaID"));
				fiveTEqpt.EqptModel = cursor.getString(cursor
						.getColumnIndex("EqptModel"));
				fiveTEqpt.Manufacturer = cursor.getString(cursor
						.getColumnIndex("Manufacturer"));
				fiveTEqpt.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
			}
		} catch (Exception e) {
			System.out.println("----getAllFiveTEqptList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return fiveTEqpt;
	}

}
