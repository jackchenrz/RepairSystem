package com.czvv.repairsystemmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.czvv.repairsystemmobile.utils.Constants;

public class DBHelper extends SQLiteOpenHelper {

	private static String name = Constants.DB_NAME;// 表示数据库的名称
	private static int version = 1;// 表示数据库的版本号码

	public DBHelper(Context context) {
		super(context, name, null, version);
	}

	// 当数据库创建的时候，是第一次被执行,完成对数据库的表的创建
	@Override
	public void onCreate(SQLiteDatabase db) {
		// boolean类型 1是true 0是false
		createUserTable(db);// 用户表
		createDeptTable(db);// 维修部门表
		createTechEqptTable(db);// 机械设备表
		createEqptInfoTable(db);// 机械设备关联表
		createFiveTEqptTable(db);// 行安设备表
		createRepairSubmitTable(db);// 报修上传表
		createRepairHandleTable(db);// 报修处理次
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	private void createUserTable(SQLiteDatabase db) {
		StringBuffer sql = new StringBuffer();
		sql.append("create table sys_user (");
		sql.append("user_id varchar(100) primary key unique not null,");
		sql.append("real_name varchar(50),");
		sql.append("user_name varchar(50) not null,");
		sql.append("password varchar(50) not null,");
		sql.append("home_tel varchar(100),");
		sql.append("office_tel varchar(100),");
		sql.append("create_time varchar(100),");
		sql.append("modi_pwd_time varchar(100),");
		sql.append("recent_access varchar(100),");
		sql.append("is_admin integer,");
		sql.append("is_used integer,");
		sql.append("is_lock integer,");
		sql.append("sex varchar(50),");
		sql.append("email varchar(100),");
		sql.append("remark varchar(100),");
		sql.append("error_login_times varchar(100),");
		sql.append("sort_no varchar(100),");
		sql.append("dept_id varchar(100) not null,");
		sql.append("dept_bz_id varchar(100),");
		sql.append("isrepair integer,");
		sql.append("isleader integer");
		sql.append(");");
		db.execSQL(sql.toString());
	}

	private void createDeptTable(SQLiteDatabase db) {
		StringBuffer sql = new StringBuffer();
		sql.append("create table sys_dept (");
		sql.append("dept_id varchar(100) primary key unique not null,");
		sql.append("dept_name varchar(100) not null,");
		sql.append("p_dept_id varchar(100) not null,");
		sql.append("is_used integer,");
		sql.append("sort_no varchar(100),");
		sql.append("is_usedept integer,");
		sql.append("is_repairdept integer,");
		sql.append("deptlevel integer,");
		sql.append("is_repairgroup integer,");
		sql.append("is_workarea integer,");
		sql.append("short_dept_name varchar(100)");
		sql.append(");");
		db.execSQL(sql.toString());
	}

	private void createTechEqptTable(SQLiteDatabase db) {
		StringBuffer sql = new StringBuffer();
		sql.append("create table Tech_Eqpt (");
		sql.append("TechEqptID varchar(100) primary key unique not null,");
		sql.append("EqptName varchar(100),");
		sql.append("dept_id varchar(100) not null,");
		sql.append("EqptAddress varchar(100),");
		sql.append("EqptUse varchar(100),");
		sql.append("EqptOwner varchar(100),");
		sql.append("EqptStatus varchar(100),");
		sql.append("InDate varchar(100),");
		sql.append("CreateDate varchar(100),");
		sql.append("LastUpdateDate varchar(100),");
		sql.append("EqptInfoID varchar(100) unique not null,");
		sql.append("WorkAreaID varchar(100),");
		sql.append("EqptType varchar(100),");
		sql.append("RepairDeptID varchar(100)");
		sql.append(");");
		db.execSQL(sql.toString());
	}

	private void createEqptInfoTable(SQLiteDatabase db) {
		StringBuffer sql = new StringBuffer();
		sql.append("create table Eqpt_Info(");
		sql.append("EqptInfoID varchar(100) primary key unique not null,");
		sql.append("EqptName varchar(100) not null,");
		sql.append("UsingArea varchar(100),");
		sql.append("FixedAssetsID varchar(100),");
		sql.append("EqptNum varchar(100),");
		sql.append("EqptSpecif varchar(100),");
		sql.append("EqptModel varchar(100),");
		sql.append("Manufacturer varchar(100),");
		sql.append("SettingAddr varchar(100),");
		sql.append("WorkTimes varchar(100),");
		sql.append("RepairMode varchar(100),");
		sql.append("EleRepairName varchar(100),");
		sql.append("MacRepairName varchar(100)");
		sql.append(");");
		db.execSQL(sql.toString());
	}

	private void createFiveTEqptTable(SQLiteDatabase db) {
		StringBuffer sql = new StringBuffer();
		sql.append("create table FiveT_Eqpt(");
		sql.append("EqptID varchar(100) primary key unique not null,");
		sql.append("EqptType varchar(100) not null,");
		sql.append("ProbeStation varchar(100) not null,");
		sql.append("dept_id varchar(100) not null,");
		sql.append("EqptAddress varchar(100) not null,");
		sql.append("EqptUse varchar(100),");
		sql.append("EqptOwner varchar(100),");
		sql.append("EqptStatus varchar(100),");
		sql.append("InDate varchar(100),");
		sql.append("CreateDate varchar(100),");
		sql.append("LastUpdateDate varchar(100),");
		sql.append("WorkAreaID varchar(100),");
		sql.append("EqptModel varchar(100),");
		sql.append("Manufacturer varchar(100),");
		sql.append("RepairDeptID varchar(100)");
		sql.append(");");
		db.execSQL(sql.toString());
	}

	private void createRepairSubmitTable(SQLiteDatabase db) {
		StringBuffer sql = new StringBuffer();
		sql.append("create table Repair_Submit(");
		sql.append("RepairID varchar(100) primary key not null,");
		sql.append("RepairType varchar(100) not null,");
		sql.append("EqptID varchar(100) not null,");
		sql.append("EqptName varchar(100) not null,");
		sql.append("EqptType varchar(100),");
		sql.append("ProbeStation varchar(100),");
		sql.append("FaultStatus varchar(100) not null,");
		sql.append("Specification varchar(100),");
		sql.append("Manufacturer varchar(100),");
		sql.append("UserID varchar(100) not null,");
		sql.append("UserDeptID varchar(100) not null,");
		sql.append("FaultOccu_Time varchar(100) not null,");
		sql.append("FaultReceiveTime varchar(100),");
		sql.append("FaultAppearance varchar(100) not null,");
		sql.append("CreateDate varchar(100),");
		sql.append("LastUpdateDate varchar(100),");
		sql.append("IsStop integer,");
		sql.append("StopTime varchar(100),");
		sql.append("StopHours integer,");
		sql.append("StopMinutes integer,");
		sql.append("IsUpload integer,");
		sql.append("ImageUrl varchar(100) not null,");
		sql.append("RepairDeptID varchar(100)");
		sql.append(");");
		db.execSQL(sql.toString());
	}

	private void createRepairHandleTable(SQLiteDatabase db) {
		StringBuffer sql = new StringBuffer();
		sql.append("create table Repair_Handle(");
		sql.append("RepairHandleID varchar(100) primary key unique not null,");
		sql.append("RepairID varchar(100) unique not null,");
		sql.append("RepairDeptID varchar(100) not null,");
		sql.append("RepairDeptBzID varchar(100),");
		sql.append("RepairUserName varchar(100) not null,");
		sql.append("ArriveTime varchar(100),");
		sql.append("RepairFinishTime varchar(100) not null,");
		sql.append("FaultHandle varchar(100) not null,");
		sql.append("FaultType varchar(100),");
		sql.append("FaultReason varchar(100),");
		sql.append("ProbType varchar(100),");
		sql.append("ProbSysName varchar(100),");
		sql.append("FeedbackTime varchar(100),");
		sql.append("FaultUseHours integer,");
		sql.append("FaultLevel varchar(100),");
		sql.append("CreateDate varchar(100),");
		sql.append("IsReturnSy integer,");
		sql.append("IsReturnWx integer,");
		sql.append("IsDocument integer");
		sql.append(");");
		db.execSQL(sql.toString());
	}
}
