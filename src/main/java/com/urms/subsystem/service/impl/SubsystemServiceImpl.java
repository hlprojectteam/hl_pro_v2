package com.urms.subsystem.service.impl;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.cache.Cache;
import com.common.utils.helper.Pager;
import com.urms.subsystem.dao.ISubsystemDao;
import com.urms.subsystem.module.Subsystem;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.subsystem.vo.SubsystemVo;
import com.urms.user.module.User;

@Repository("subsystemServiceImpl")
public class SubsystemServiceImpl extends BaseServiceImpl implements ISubsystemService{
	
	@Autowired
	public ISubsystemDao subsystemDaoImpl;

	@Override
	public void saveOrUpdate(Subsystem subsystem){
		if(StringUtils.isBlank(subsystem.getId())){
			this.save(subsystem);
		}else{
			Subsystem s = this.getEntityById(Subsystem.class, subsystem.getId());
			subsystem.setMenus(s.getMenus());
			BeanUtils.copyProperties(subsystem,s);
			this.update(s);			
		}
		this.getSubsystem();//获得子系统键值对
	}
	
	@Override
	public Pager queryEntityList(int page,int rows,SubsystemVo subsystemVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(subsystemVo.getSysName())){
			criterionsList.add(Restrictions.like("sysName", "%"+subsystemVo.getSysName()+"%"));
		}
		if(StringUtils.isNotBlank(subsystemVo.getSysCode())){
			criterionsList.add(Restrictions.like("sysCode", "%"+subsystemVo.getSysCode()+"%"));
		}
		return subsystemDaoImpl.queryList(page, rows, criterionsList, Order.asc("order") ,Subsystem.class);
	}
	
	@Override
	public List<Subsystem> getSubsystemList(SubsystemVo subsystemVo,User user){
		List<Subsystem> list = new ArrayList<Subsystem>();
		//数据过滤
		if(user.getType()!=1){//如果不是是超管
			List<Subsystem> sList = this.getSubsystemList(new SubsystemVo());
			for (Subsystem ss:sList) {
				if(user.getSysCode().equals(ss.getSysCode())){//编码相同
					list.add(ss);
					break;
				}
			}
		}else{
			list = getSubsystemList(new SubsystemVo());
		}
		return list;
	}
	
	@Override
	public void getSubsystem() {
		List<Subsystem> list = this.getSubsystemList(new SubsystemVo());
		Cache.getSubsystem.clear();
		for (int i = 0; i < list.size(); i++) {
			Cache.getSubsystem.put(list.get(i).getSysCode(), list.get(i).getSysName());
		}
	}
	
	@Override
	public List<Subsystem> getSubsystemList(SubsystemVo subsystemVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(subsystemVo.getId()))
			criterionsList.add(Restrictions.eq("id", subsystemVo.getId()));
		if(StringUtils.isNotBlank(subsystemVo.getSysCode()))
			criterionsList.add(Restrictions.eq("sysCode", subsystemVo.getSysCode()));
		return subsystemDaoImpl.queryList(criterionsList,Order.asc("order"), Subsystem.class);
	}
	
	public ISubsystemDao getSubsystemDaoImpl() {
		return subsystemDaoImpl;
	}

	public void setSubsystemDaoImpl(ISubsystemDao subsystemDaoImpl) {
		this.subsystemDaoImpl = subsystemDaoImpl;
	}

}
