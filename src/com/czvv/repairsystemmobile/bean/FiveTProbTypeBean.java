package com.czvv.repairsystemmobile.bean;

import java.util.List;

public class FiveTProbTypeBean {

	public List<FiveTProbType> ds;
	public class FiveTProbType{
		public String FiveTEqptProbTypeID;
		public String Name;
		public String Type;
		public String Response;
		public String SysName;
		public String Remark;
		
		@Override
		public String toString() {
			return SysName + " " + Type + " " + " " + Name + " " + Response;
		}
	}
}
