package com.czvv.repairsystemmobile.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.czvv.repairsystemmobile.bean.ToRepairSave;
import com.czvv.repairsystemmobile.db.DBHelper;

public class ToRepairDao {

	private DBHelper helper;

	private ToRepairDao(Context context) {
		helper = new DBHelper(context);
	}

	public static ToRepairDao instance;

	public synchronized static ToRepairDao getInstance(Context context) {
		if (instance == null) {
			instance = new ToRepairDao(context);
		}
		return instance;
	}

	private final String TABLE_NAME = "torepair_handler";

	public boolean addTorepair(ToRepairSave toRepair) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("RepairID", toRepair.RepairID);
			values.put("RepairDeptID", toRepair.RepairDeptID);
			values.put("FaultType", toRepair.FaultType);
			values.put("FaultReason", toRepair.FaultReason);
			values.put("RepairUserName", toRepair.RepairUserName);
			values.put("FaultHandle", toRepair.FaultHandle);
			values.put("RepairFinishTime", toRepair.RepairFinishTime);
			values.put("ProbType", toRepair.ProbType);
			values.put("ProbSysName", toRepair.ProbSysName);
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

	public boolean deleteAllToRepairSave() {
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

	public List<ToRepairSave> getAllToRepairSave() {
		List<ToRepairSave> list = new ArrayList<ToRepairSave>();
		ToRepairSave toRepairSave;
		String sql = "select * from " + TABLE_NAME;
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				toRepairSave = new ToRepairSave();
				toRepairSave.RepairID = cursor.getString(cursor
						.getColumnIndex("RepairID"));
				toRepairSave.RepairDeptID = cursor.getString(cursor
						.getColumnIndex("RepairDeptID"));
				toRepairSave.FaultType = cursor.getString(cursor
						.getColumnIndex("FaultType"));
				toRepairSave.FaultReason = cursor.getString(cursor
						.getColumnIndex("FaultReason"));
				toRepairSave.RepairUserName = cursor.getString(cursor
						.getColumnIndex("RepairUserName"));
				toRepairSave.FaultHandle = cursor.getString(cursor
						.getColumnIndex("FaultHandle"));
				toRepairSave.RepairFinishTime = cursor.getString(cursor
						.getColumnIndex("RepairFinishTime"));
				toRepairSave.ProbType = cursor.getString(cursor
						.getColumnIndex("ProbType"));
				toRepairSave.ProbSysName = cursor.getString(cursor
						.getColumnIndex("ProbSysName"));
				list.add(toRepairSave);
				toRepairSave = null;
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
	
	public void delToRepair(String RepairID) {
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			database.delete(TABLE_NAME, "RepairID = ?",
					new String[] { RepairID });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
