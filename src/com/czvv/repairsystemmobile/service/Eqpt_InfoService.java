package com.czvv.repairsystemmobile.service;

import java.util.List;

import com.czvv.repairsystemmobile.bean.EqptInfoBean.EqptInfo;

public interface Eqpt_InfoService {
	public boolean deleteAllEqptInfo();
	public List<EqptInfo> getAllEqptInfoList();
	public List<EqptInfo> getLikeEqptInfoList(String keywords);
	boolean addEqptInfo(EqptInfo eqptInfo);
	public EqptInfo getEqptInfo(String eqptName);
}
