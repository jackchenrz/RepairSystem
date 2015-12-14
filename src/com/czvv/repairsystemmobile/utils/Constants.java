package com.czvv.repairsystemmobile.utils;

import android.os.Environment;

public class Constants {
	// public static final String SERVICE_URL = "http://192.168.1.105:7001/WebSerApp.asmx";
	public static final String SERVICE_PAGE = "/WebSerApp.asmx";
	public static final String SERVICE_NAMESPACE = "http://tempuri.org/";
	public static final String SERVICE_GETSYSUSER = "Getsys_user";// 得到用户
	public static final String SERVICE_GETSYS_DEPT = "Getsys_dept";// 得到维修部门
	public static final String SERVICE_GETTECH_EQPT = "GetTech_Eqpt";// 得到机械设备
	public static final String SERVICE_GETREPAIR_SUBMIT = "GetRepair_Submit";// 得到上传
	public static final String SERVICE_GETREPAIR_HANDLE = "GetRepair_Handle";// 得到报修处理
	public static final String SERVICE_GETFIVET_EQPT = "GetFiveT_Eqpt";// 得到行安设备
	public static final String SERVICE_GETEQPT_INFO = "GetEqpt_Info"; 
	public static final String SERVICE_UPLOADIMAGE = "uploadImage"; //上传照片
	public static final String SERVICE_RETURNREPAIR_SUBMIT = "ReturnRepair_Submit"; //提交表单
	
	public static final String FILEPATH = Environment.getExternalStorageDirectory() + "/.problems/";//图片的存放地址
	public static final int DEVICENAME = 1;
	public static final int REPAIRDEPARTMENTLIST = 2;

	public static final String ADMIN_INFO = "adminInfo";// 保存admin管理员信息的xml文件名
	public static final String DB_NAME = "RepairSystem.db";// 数据库名称

	public static final String ADMIN_LOGINNAME = "ADMIN";// 管理员登录名
	public static final String ADMIN_PASSWORD = "74535838";// 管理员密码
	public static final String BIRTHDAY = "birthday";// 管理员密码
}
