package com.czvv.repairsystemmobile.bean;

import java.io.Serializable;
import java.util.List;


public class ToRepairedBean implements Serializable{
	
	public List<ToRepair> ds;
	public class ToRepair implements Serializable{
		public String RepairID;
		public String RepairType;
		public String EqptID;
		public String EqptName;
		public String EqptType;
		public String ProbeStation;
		public String FaultStatus;
		public String Specification;
		public String Manufacturer;
		public String UserID;
		public String UserDeptID;
		public String FaultOccu_Time;
		public String FaultReceiveTime;
		public String FaultAppearance;
		public String CreateDate;
		public String RepairHandleID;
		public String RepairDeptID;
		public String RepairUserName;
		public String ArriveTime;
		public String RepairFinishTime;
		public String FaultHandle;
		public String FaultType;
		public String FaultReason;
		public String FeedbackTime;
		public String FaultUseHours;
		public String FaultLevel;
		public String repair_dept_name;
		public String dept_name;
		public String submit_dept_name;
		public String lrdate;
		public String fkdate;
		public String IsReturnSy;
		public String IsReturnWx;
		public String IsDocument;
		public String ImageUrl;
		public String ImageUrlPath;
	}
}
