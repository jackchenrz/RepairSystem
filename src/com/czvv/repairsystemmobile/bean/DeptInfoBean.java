package com.czvv.repairsystemmobile.bean;

import java.util.List;

public class DeptInfoBean {
	public List<DeptInfo> ds;
	public class DeptInfo{
		public String dept_id;
		public String dept_name;
		public int deptlevel;
		public boolean is_repairdept;
		public boolean is_repairgroup;
		public boolean is_used;
		public boolean is_usedept;
		public boolean is_workarea;
		public String p_dept_id;
		public String short_dept_name;
		public String sort_no;
	}
}
