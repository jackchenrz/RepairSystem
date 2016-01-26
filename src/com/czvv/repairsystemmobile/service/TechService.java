package com.czvv.repairsystemmobile.service;

import java.util.List;

import com.czvv.repairsystemmobile.bean.DeptInfoBean.DeptInfo;
import com.czvv.repairsystemmobile.bean.FiveTEqptInfoBean.FiveTEqpt;
import com.czvv.repairsystemmobile.bean.TechEqptBean.TechEqpt;

public interface TechService {
	public boolean deleteAllTechEqpt();
	public List<TechEqpt> getAllTechEqptList();
	boolean addTechEqpt(TechEqpt techEqpt);
	String getTechEqptID(String EqptInfoID);
	TechEqpt getTechEqpt(String EqptInfoID);
}
