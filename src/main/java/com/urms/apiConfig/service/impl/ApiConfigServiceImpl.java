package com.urms.apiConfig.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.cache.Cache;
import com.common.utils.helper.Pager;
import com.urms.apiConfig.dao.IApiConfigDao;
import com.urms.apiConfig.module.ApiConfig;
import com.urms.apiConfig.service.IApiConfigService;
import com.urms.apiConfig.vo.ApiConfigVo;
import com.urms.sysConfig.module.SysConfig;

/**
 * API综合接口配置
 * @author Mr.Wang
 * @date 2018-07-25 17:08:21
 */
@Repository("apiConfigServiceImpl")
public class ApiConfigServiceImpl extends BaseServiceImpl implements IApiConfigService {

	@Autowired
	public IApiConfigDao apiConfigDaoImpl;

	@Override
	public void saveOrUpdate(ApiConfig apiConfig) {
		if (StringUtils.isBlank(apiConfig.getId())) {
			this.save(apiConfig);
		} else {
			this.update(apiConfig);
		}
	}

	@Override
	public Pager queryEntityList(int page, int rows, ApiConfigVo apiConfigVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if (apiConfigVo.getApiType() != null) {
			criterionsList.add(Restrictions.eq("apiType", apiConfigVo.getApiType()));
		}
		return this.apiConfigDaoImpl.queryList(page, rows, criterionsList, Order.desc("createTime"), ApiConfig.class);
	}

	public IApiConfigDao getApiConfigDaoImpl() {
		return apiConfigDaoImpl;
	}

	public void setApiConfigDaoImpl(IApiConfigDao apiConfigDaoImpl) {
		this.apiConfigDaoImpl = apiConfigDaoImpl;
	}

	//加载API接口配置信息，置于缓存
	@Override
	public void getApiConfig() {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		List<ApiConfig> list = this.apiConfigDaoImpl.queryEntityList(criterionsList,null, ApiConfig.class);
		Cache.getApiConfig.clear();
		for (int i = 0; i < list.size(); i++) {
			Cache.getApiConfig.put(list.get(i).getApiType(), list.get(i));
		}
	}
}
