package com.czvv.repairsystemmobile;

import android.os.Environment;

public class Constants {
	// public static final String SERVICE_URL = "http://192.168.1.105:7001/WebSerApp.asmx";
	public static final String SERVICE_PAGE = "/WebSerApp.asmx";
	public static final String SERVICE_NAMESPACE = "http://tempuri.org/";
	public static final String SERVICE_GETSYSUSER = "Getsys_user";// �õ��û�
	public static final String SERVICE_GETSYS_DEPT = "Getsys_dept";// �õ�ά�޲���
	public static final String SERVICE_GETTECH_EQPT = "GetTech_Eqpt";// �õ���е�豸
	public static final String SERVICE_GETREPAIR_SUBMIT = "GetRepair_Submit";// �õ��ϴ�
	public static final String SERVICE_GETREPAIR_HANDLE = "GetRepair_Handle";// �õ����޴���
	public static final String SERVICE_GETTECHREPAIRINGLIST= "GetTechRepairingList";// �õ���е�豸ά���б�
	public static final String SERVICE_GETTECHREPAIREDLIST= "GetTechRepairedList";// �õ���е�豸��ά���б�
	public static final String SERVICE_GET5TREPAIRINGLIST= "Get5TRepairingList";// �õ��а��豸ά���б�
	public static final String SERVICE_GET5TREPAIREDLIST= "Get5TRepairedList";// �õ��а��豸��ά���б�
	public static final String SERVICE_GETFIVET_EQPT_PROB_TYPE = "GetFiveT_Eqpt_Prob_Type";// �õ��а��豸��������
	public static final String SERVICE_RETURNREPAIR_HANDLE = "ReturnRepair_Handle";// �õ���е�豸ά���б�
	public static final String SERVICE_GETFIVET_EQPT = "GetFiveT_Eqpt";// �õ��а��豸
	public static final String SERVICE_GETEQPT_INFO = "GetEqpt_Info"; 
	public static final String SERVICE_UPLOADIMAGE = "uploadImage"; //�ϴ���Ƭ
	public static final String SERVICE_RETURNREPAIR_SUBMIT = "ReturnRepair_Submit"; //�ύ��
	
	public static final String FILEPATH = Environment.getExternalStorageDirectory() + "/.problems/";//ͼƬ�Ĵ�ŵ�ַ
	public static final int DEVICENAME = 1;
	public static final int REPAIRDEPARTMENTLIST = 2;

	public static final String ADMIN_INFO = "adminInfo";// ����admin����Ա��Ϣ��xml�ļ���
	public static final String DB_NAME = "RepairSystem.db";// ���ݿ�����

	public static final String ADMIN_LOGINNAME = "local";// ����Ա��¼��
	public static final String ADMIN_PASSWORD = "111111";// ����Ա����
	public static final String BIRTHDAY = "birthday";// ����Ա����
}
