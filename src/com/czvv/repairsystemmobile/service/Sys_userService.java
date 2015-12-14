package com.czvv.repairsystemmobile.service;

import java.util.List;
import java.util.Map;

import com.czvv.repairsystemmobile.bean.UserInfoBean;
import com.czvv.repairsystemmobile.bean.UserInfoBean.UserInfo;

public interface Sys_userService {
	public boolean deleteAllUserLog();
	public List<UserInfo> getAllUserLogList();
	boolean addUserLog(UserInfo userInfo);
	public boolean avaiLogin(String userName,String pwd);
	public UserInfo getUserInfo(String userNmae);
}
