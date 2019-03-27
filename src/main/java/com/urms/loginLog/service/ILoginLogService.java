package com.urms.loginLog.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.loginLog.module.LoginLog;
import com.urms.loginLog.vo.LoginLogVo;

public interface ILoginLogService extends IBaseService{
	
	public void saveOrUpdate(LoginLog loginLog);
	
	public Pager queryEntityList(int page,int rows,LoginLogVo loginLogVo);
}
