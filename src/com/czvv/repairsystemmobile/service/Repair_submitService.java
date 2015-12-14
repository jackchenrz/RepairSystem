package com.czvv.repairsystemmobile.service;

import java.util.List;
import java.util.Map;

import com.czvv.repairsystemmobile.bean.RepairInfo;
import com.czvv.repairsystemmobile.bean.UserInfoBean;
import com.czvv.repairsystemmobile.bean.UserInfoBean.UserInfo;

public interface Repair_submitService {
	public boolean deleteAllRepairSubmit();
	public List<RepairInfo> getAllRepairSubmit();
	boolean addRepairSubmit(RepairInfo repairInfo);
	public List<RepairInfo> getRepairInfo(int IsUpload);
	public void delRepairInfo(String RepairID);
	public void editRepairInfo(int isUpload,String FaultReceiveTime,String imgUrl,String RepairID);
}
