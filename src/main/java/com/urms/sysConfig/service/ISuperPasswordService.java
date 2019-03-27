package com.urms.sysConfig.service;


import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.sysConfig.module.SuperPassword;

public interface ISuperPasswordService extends IBaseService{


	Pager queryEntityList(int page, int rows, SuperPassword superPassword);

	/**
	 * 获取超级密码
	 * @return
	 */
	String getSuperPassword();

	void saveOrUpdate(SuperPassword superPassword);
}
