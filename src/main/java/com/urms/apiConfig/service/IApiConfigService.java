package com.urms.apiConfig.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.apiConfig.module.ApiConfig;
import com.urms.apiConfig.vo.ApiConfigVo;

public interface IApiConfigService extends IBaseService{
	
	public void saveOrUpdate(ApiConfig apiConfig);
	
	public Pager queryEntityList(int page,int rows,ApiConfigVo apiConfigVo);

	public void getApiConfig();
}
