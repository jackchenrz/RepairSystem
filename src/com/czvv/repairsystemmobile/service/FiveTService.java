package com.czvv.repairsystemmobile.service;

import java.util.List;

import com.czvv.repairsystemmobile.bean.FiveTEqptInfoBean.FiveTEqpt;

public interface FiveTService {
	public boolean deleteAllFiveTEqpt();
	public List<FiveTEqpt> getAllFiveTEqptList();
	boolean addFiveTEqpt(FiveTEqpt fiveTEqpt);
	public FiveTEqpt getFiveTEqpt(String eqptName);
	FiveTEqpt getFiveTEqpt1(String eqptID);
}
