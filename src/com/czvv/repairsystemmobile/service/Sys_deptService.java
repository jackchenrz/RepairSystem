package com.czvv.repairsystemmobile.service;

import java.util.List;
import java.util.Map;

import com.czvv.repairsystemmobile.bean.DeptInfoBean.DeptInfo;
import com.czvv.repairsystemmobile.bean.UserInfoBean;
import com.czvv.repairsystemmobile.bean.UserInfoBean.UserInfo;

public interface Sys_deptService {
	public boolean deleteAllDept();
	public List<DeptInfo> getAllDeptList(int is_repairdept);
	boolean addDept(DeptInfo deptInfo);
	public DeptInfo getDeptInfo(String deptNmae);
	public DeptInfo getDeptInfo1(String deptID);
}
