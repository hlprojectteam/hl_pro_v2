package com.urms.sysConfig.service.impl;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.common.utils.Common;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.cache.Cache;
import com.common.utils.helper.Pager;
import com.urms.sysConfig.dao.ISysConfigDao;
import com.urms.sysConfig.module.SysConfig;
import com.urms.sysConfig.service.ISysConfigService;
import com.urms.sysConfig.vo.SysConfigVo;

@Repository("sysConfigServiceImpl")
public class SysConfigServiceImpl extends BaseServiceImpl implements ISysConfigService{
	
	@Autowired
	public ISysConfigDao sysConfigDaoImpl;

	@Override
	public void saveOrUpdate(SysConfig sysConfig){
		if(StringUtils.isBlank(sysConfig.getId())){
			this.save(sysConfig);
		}else{
			this.update(sysConfig);			
		}
		this.getSysConfig();//获得子系统键值对
	}
	
	@Override
	public Pager queryEntityList(int page,int rows,SysConfigVo sysConfigVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(sysConfigVo.getSysKey())){
			criterionsList.add(Restrictions.like("sysKey", "%"+sysConfigVo.getSysKey()+"%"));
		}
		if(StringUtils.isNotBlank(sysConfigVo.getSysValue())){
			criterionsList.add(Restrictions.like("sysValue", "%"+sysConfigVo.getSysValue()+"%"));
		}
		if(StringUtils.isNotBlank(sysConfigVo.getSysCode())){
			criterionsList.add(Restrictions.eq("sysCode", sysConfigVo.getSysCode()));
		}
		return sysConfigDaoImpl.queryList(page, rows, criterionsList, Order.desc("createTime") ,SysConfig.class);
	}
	
	@Override
	public List<SysConfig> querySysConfigList(SysConfigVo sysConfigVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(sysConfigVo.getId()))
			criterionsList.add(Restrictions.eq("id", sysConfigVo.getId()));
		if(StringUtils.isNotBlank(sysConfigVo.getSysKey()))
			criterionsList.add(Restrictions.eq("sysKey", sysConfigVo.getSysKey()));
		if(StringUtils.isNotBlank(sysConfigVo.getSysValue()))
			criterionsList.add(Restrictions.eq("sysValue", sysConfigVo.getSysValue()));
		if(StringUtils.isNotBlank(sysConfigVo.getSysCode()))
			criterionsList.add(Restrictions.eq("sysCode", sysConfigVo.getSysCode()));
		return sysConfigDaoImpl.queryList(criterionsList,Order.asc("createTime"), SysConfig.class);
	}
	/**
	 * 加载系统配置信息 - 置放缓存
	 */
	@Override
	public void getSysConfig() {
		//List<SysConfig> list = this.sysConfigDaoImpl.queryAllEntity(SysConfig.class, null);
//		List<Criterion> criterionsList = new ArrayList<Criterion>();
//		String sysCode = getProperty("sysCode");//系统编码
//		if(StringUtils.isNotBlank(sysCode)){
//			criterionsList.add(Restrictions.eq("sysCode", sysCode));
//		}
//		List<SysConfig> list = this.sysConfigDaoImpl.queryEntityList(criterionsList,null, SysConfig.class);
//		Cache.getSysConfig.clear();
//		for (int i = 0; i < list.size(); i++) {
//			Cache.getSysConfig.put(list.get(i).getSysKey(), list.get(i).getSysValue());
//		}
		
		List<SysConfig> list = this.sysConfigDaoImpl.queryAllEntity(SysConfig.class, null);
		Cache.getSysConfig.clear();
		for (int i = 0; i < list.size(); i++) {
			Cache.getSysConfig.put(list.get(i).getSysKey(), list.get(i).getSysValue());
		}
	}

	public ISysConfigDao getSysConfigDaoImpl() {
		return sysConfigDaoImpl;
	}

	public void setSysConfigDaoImpl(ISysConfigDao sysConfigDaoImpl) {
		this.sysConfigDaoImpl = sysConfigDaoImpl;
	}

	//读取配置文件
	private String getProperty(String key) {
		String str = "";
		try {
			str = new String(ResourceBundle.getBundle("config").getString(key).getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("com.common.utils.Common_读取配置信息", e);
		}
		return str;
	}
}
