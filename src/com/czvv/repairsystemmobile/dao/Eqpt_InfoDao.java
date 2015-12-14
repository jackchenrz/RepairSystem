package com.czvv.repairsystemmobile.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.czvv.repairsystemmobile.bean.EqptInfoBean;
import com.czvv.repairsystemmobile.bean.EqptInfoBean.EqptInfo;
import com.czvv.repairsystemmobile.db.DBHelper;
import com.czvv.repairsystemmobile.service.Eqpt_InfoService;

public class Eqpt_InfoDao implements Eqpt_InfoService {

	private DBHelper helper;

	private Eqpt_InfoDao(Context context) {
		helper = new DBHelper(context);
	}

	public static Eqpt_InfoDao instance;

	public synchronized static Eqpt_InfoDao getInstance(Context context) {
		if (instance == null) {
			instance = new Eqpt_InfoDao(context);
		}
		return instance;
	}

	private final String TABLE_NAME = "Eqpt_Info";

	@Override
	public boolean addEqptInfo(EqptInfo eqptInfo) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("EqptInfoID", eqptInfo.EqptInfoID);
			values.put("EqptName", eqptInfo.EqptName);
			values.put("UsingArea", eqptInfo.UsingArea);
			values.put("FixedAssetsID", eqptInfo.FixedAssetsID);
			values.put("EqptNum", eqptInfo.EqptNum);
			values.put("EqptSpecif", eqptInfo.EqptSpecif);
			values.put("EqptModel", eqptInfo.EqptModel);
			values.put("Manufacturer", eqptInfo.Manufacturer);
			values.put("SettingAddr", eqptInfo.SettingAddr);
			values.put("WorkTimes", eqptInfo.WorkTimes);
			values.put("RepairMode", eqptInfo.RepairMode);
			values.put("EleRepairName", eqptInfo.EleRepairName);
			values.put("MacRepairName", eqptInfo.MacRepairName);
			database.insert(TABLE_NAME, null, values);
			flag = true;
		} catch (Exception e) {
			System.out.println("----addEqptInfo-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean deleteAllEqptInfo() {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from " + TABLE_NAME;
			database = helper.getWritableDatabase();// 实现对数据库的写的操作
			database.execSQL(sql);
			flag = true;
		} catch (Exception e) {
			System.out.println("----deleteAllEqptInfo-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public List<EqptInfo> getAllEqptInfoList() {
		List<EqptInfo> list = new ArrayList<EqptInfo>();
		EqptInfoBean bean = new EqptInfoBean();
		EqptInfo eqptInfo;
		String sql = "select * from " + TABLE_NAME;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				eqptInfo = bean.new EqptInfo();
				eqptInfo.EqptInfoID = cursor.getString(cursor
						.getColumnIndex("EqptInfoID"));
				eqptInfo.EqptName = cursor.getString(cursor
						.getColumnIndex("EqptName"));
				eqptInfo.UsingArea = cursor.getString(cursor
						.getColumnIndex("UsingArea"));
				eqptInfo.FixedAssetsID = cursor.getString(cursor
						.getColumnIndex("FixedAssetsID"));
				eqptInfo.EqptNum = cursor.getString(cursor
						.getColumnIndex("EqptNum"));
				eqptInfo.EqptSpecif = cursor.getString(cursor
						.getColumnIndex("EqptSpecif"));
				eqptInfo.EqptModel = cursor.getString(cursor
						.getColumnIndex("EqptModel"));
				eqptInfo.Manufacturer = cursor.getString(cursor
						.getColumnIndex("Manufacturer"));
				eqptInfo.SettingAddr = cursor.getString(cursor
						.getColumnIndex("SettingAddr"));
				eqptInfo.WorkTimes = cursor.getString(cursor
						.getColumnIndex("WorkTimes"));
				eqptInfo.RepairMode = cursor.getString(cursor
						.getColumnIndex("RepairMode"));
				eqptInfo.EleRepairName = cursor.getString(cursor
						.getColumnIndex("EleRepairName"));
				eqptInfo.MacRepairName = cursor.getString(cursor
						.getColumnIndex("MacRepairName"));
				list.add(eqptInfo);
				eqptInfo = null;
			}
		} catch (Exception e) {
			System.out.println("----getAllEqptInfoList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	@Override
	public EqptInfo getEqptInfo(String eqptName) {
		EqptInfo eqptInfo = null;
		EqptInfoBean bean = new EqptInfoBean();
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.query(TABLE_NAME, null, "EqptName = ?",
					new String[] { eqptName }, null, null, null);
			if (cursor.moveToNext()) {
				eqptInfo = bean.new EqptInfo();
				eqptInfo.EqptInfoID = cursor.getString(cursor
						.getColumnIndex("EqptInfoID"));
				eqptInfo.EqptName = cursor.getString(cursor
						.getColumnIndex("EqptName"));
				eqptInfo.UsingArea = cursor.getString(cursor
						.getColumnIndex("UsingArea"));
				eqptInfo.FixedAssetsID = cursor.getString(cursor
						.getColumnIndex("FixedAssetsID"));
				eqptInfo.EqptNum = cursor.getString(cursor
						.getColumnIndex("EqptNum"));
				eqptInfo.EqptSpecif = cursor.getString(cursor
						.getColumnIndex("EqptSpecif"));
				eqptInfo.EqptModel = cursor.getString(cursor
						.getColumnIndex("EqptModel"));
				eqptInfo.Manufacturer = cursor.getString(cursor
						.getColumnIndex("Manufacturer"));
				eqptInfo.SettingAddr = cursor.getString(cursor
						.getColumnIndex("SettingAddr"));
				eqptInfo.WorkTimes = cursor.getString(cursor
						.getColumnIndex("WorkTimes"));
				eqptInfo.RepairMode = cursor.getString(cursor
						.getColumnIndex("RepairMode"));
				eqptInfo.EleRepairName = cursor.getString(cursor
						.getColumnIndex("EleRepairName"));
				eqptInfo.MacRepairName = cursor.getString(cursor
						.getColumnIndex("MacRepairName"));
			}
		} catch (Exception e) {
			System.out.println("----getEqptInfo-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return eqptInfo;
	}

	@Override
	public List<EqptInfo> getLikeEqptInfoList(String keywords) {
		List<EqptInfo> list = new ArrayList<EqptInfo>();
		EqptInfoBean bean = new EqptInfoBean();
		EqptInfo eqptInfo;
		String sql = "select * from " + TABLE_NAME + " where EqptName like %"
				+ keywords + "%";

		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				eqptInfo = bean.new EqptInfo();
				eqptInfo.EqptInfoID = cursor.getString(cursor
						.getColumnIndex("EqptInfoID"));
				eqptInfo.EqptName = cursor.getString(cursor
						.getColumnIndex("EqptName"));
				eqptInfo.UsingArea = cursor.getString(cursor
						.getColumnIndex("UsingArea"));
				eqptInfo.FixedAssetsID = cursor.getString(cursor
						.getColumnIndex("FixedAssetsID"));
				eqptInfo.EqptNum = cursor.getString(cursor
						.getColumnIndex("EqptNum"));
				eqptInfo.EqptSpecif = cursor.getString(cursor
						.getColumnIndex("EqptSpecif"));
				eqptInfo.EqptModel = cursor.getString(cursor
						.getColumnIndex("EqptModel"));
				eqptInfo.Manufacturer = cursor.getString(cursor
						.getColumnIndex("Manufacturer"));
				eqptInfo.SettingAddr = cursor.getString(cursor
						.getColumnIndex("SettingAddr"));
				eqptInfo.WorkTimes = cursor.getString(cursor
						.getColumnIndex("WorkTimes"));
				eqptInfo.RepairMode = cursor.getString(cursor
						.getColumnIndex("RepairMode"));
				eqptInfo.EleRepairName = cursor.getString(cursor
						.getColumnIndex("EleRepairName"));
				eqptInfo.MacRepairName = cursor.getString(cursor
						.getColumnIndex("MacRepairName"));
				list.add(eqptInfo);
				eqptInfo = null;
			}
		} catch (Exception e) {
			System.out.println("----getAllEqptInfoList-->" + e.getMessage());
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

}
