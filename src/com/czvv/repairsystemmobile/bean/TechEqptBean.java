package com.czvv.repairsystemmobile.bean;

import java.util.List;

public class TechEqptBean {
	public List<TechEqpt> ds;
	public class TechEqpt{
		public String TechEqptID;
		public String EqptName;
		public String dept_id;
		public String EqptAddress;
		public String EqptUse;
		public String EqptOwner;
		public String EqptStatus;
		public String InDate;
		public String CreateDate;
		public String LastUpdateDate;
		public String EqptInfoID;
		public String WorkAreaID;
		public String EqptType;
		public String RepairDeptID;
	}
}
