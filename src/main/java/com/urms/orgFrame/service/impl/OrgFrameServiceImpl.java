package com.urms.orgFrame.service.impl;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.urms.orgFrame.dao.IOrgFrameDao;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

@Repository("orgFrameServiceImpl")
public class OrgFrameServiceImpl extends BaseServiceImpl implements IOrgFrameService{
	
	@Autowired
	public IOrgFrameDao orgFrameDaoImpl;
	@Autowired
	public IUserService userServiceImpl;

	@Override
	public void saveOrUpdate(OrgFrame orgFrame){
		if(StringUtils.isBlank(orgFrame.getId())){
			this.save(orgFrame);
		}else{
			this.update(orgFrame);
		}
	}
	
	public void save(OrgFrame orgFrame){
		OrgFrame pOrgFrame = this.getEntityById(OrgFrame.class, orgFrame.getpId());
		orgFrame.setCreateTime(new Date());
		orgFrame.setIsLeaf(1);//叶子节点
		if(StringUtils.isNotBlank(pOrgFrame.getpIds())){
			orgFrame.setpIds(pOrgFrame.getpIds()+"/"+pOrgFrame.getId());			
			orgFrame.setLevel(orgFrame.getpIds().split("/").length);//层数
		}else{
			orgFrame.setpIds(pOrgFrame.getId());	
			orgFrame.setLevel(1);//层数
		}
		if(StringUtils.isNotBlank(pOrgFrame.getpNames()))
			orgFrame.setpNames(pOrgFrame.getpNames()+"/"+pOrgFrame.getOrgFrameName());			
		else
			orgFrame.setpNames(pOrgFrame.getOrgFrameName());	
		orgFrameDaoImpl.save(orgFrame);
		if(pOrgFrame.getIsLeaf()==1){
			pOrgFrame.setIsLeaf(0);
			orgFrameDaoImpl.update(pOrgFrame);
		}
	}

	@Override
	public Pager queryEntityList(int page,int rows,OrgFrameVo orgFrameVo,User user){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(orgFrameVo.getId())){//查询当前节点下所有子节点
			OrgFrame of = this.getEntityById(OrgFrame.class, orgFrameVo.getId());
			criterionsList.add(Restrictions.ge("level", of.getLevel()));//大于等于当前等级
			criterionsList.add(Restrictions.like("pIds", "%"+of.getId()+"%"));
		}
		if(StringUtils.isNotBlank(orgFrameVo.getOrgFrameName())){
			criterionsList.add(Restrictions.like("orgFrameName", "%"+orgFrameVo.getOrgFrameName()+"%"));
		}
		if(StringUtils.isNotBlank(orgFrameVo.getOrgFrameCode())){
			criterionsList.add(Restrictions.like("orgFrameCode", "%"+orgFrameVo.getOrgFrameCode()+"%"));
		}
		//权限过滤
		if(user.getType()!=1)//不是超级管理员情况下只能看到自己当前子系统组织
			criterionsList.add(Restrictions.eq("sysCode", user.getSysCode()));
		return orgFrameDaoImpl.queryList(page, rows, criterionsList, Order.asc("order") ,OrgFrame.class);
	}
	
	@Override
	public List<OrgFrame> queryEntityListByPId(String pid){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("pId", pid));
		return orgFrameDaoImpl.queryList(criterionsList, Order.asc("order") ,OrgFrame.class);
	}
	
	@Override
	public List<OrgFrame> queryOrgFrameList(OrgFrameVo orgFrameVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(orgFrameVo.getId()))
			criterionsList.add(Restrictions.eq("id", orgFrameVo.getId()));
		if(StringUtils.isNotBlank(orgFrameVo.getpId()))
			criterionsList.add(Restrictions.eq("pId", orgFrameVo.getpId()));
		if(orgFrameVo.getLevel()!=null)
			criterionsList.add(Restrictions.eq("level", orgFrameVo.getLevel()));
		if(orgFrameVo.getOrgFrameCode()!=null)
			criterionsList.add(Restrictions.eq("orgFrameCode", orgFrameVo.getOrgFrameCode()));
		if(orgFrameVo.getOrgFrameName()!=null)
			criterionsList.add(Restrictions.eq("orgFrameName", orgFrameVo.getOrgFrameName()));
		if(orgFrameVo.getSysCode()!=null)
			criterionsList.add(Restrictions.eq("sysCode", orgFrameVo.getSysCode()));
		if(orgFrameVo.getpIds()!=null)
			criterionsList.add(Restrictions.like("pIds", "%"+orgFrameVo.getpIds()+"%"));
		return orgFrameDaoImpl.queryList(criterionsList,Order.asc("order"), OrgFrame.class);
	}
	
	@Override
	public List<OrgFrame> getOrgFrameChildren(String orgFrameId){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(orgFrameId)){//查询当前节点下所有子节点
			OrgFrame of = this.getEntityById(OrgFrame.class, orgFrameId);
			criterionsList.add(Restrictions.ge("level", of.getLevel()));//大于等于当前等级
			criterionsList.add(Restrictions.like("pIds", "%"+of.getId()+"%"));
		}
		return orgFrameDaoImpl.queryList(criterionsList,Order.asc("order"), OrgFrame.class);
	}
	@Override
	public void deleteTree(String ids) {
		String[] idsZ = ids.split(",");
		for (int i = 0; i < idsZ.length; i++) {
			OrgFrame orgFrame = orgFrameDaoImpl.getEntityById(OrgFrame.class, idsZ[i]);
			String pid = orgFrame.getpId();//父id
			orgFrameDaoImpl.delete(orgFrame);
			//修改父页面
			OrgFrameVo orgFrameVo = new OrgFrameVo();
			orgFrameVo.setpId(pid);
			List<OrgFrame> list = this.queryOrgFrameList(orgFrameVo);
			if(list.isEmpty()){
				OrgFrame porgFrame = this.getEntityById(OrgFrame.class,pid);
				porgFrame.setIsLeaf(1);
				orgFrameDaoImpl.update(porgFrame);
			}
		}
	}
	
	//凭系统编码获得组织架构的根节点
	public OrgFrame getOrgFrameRootBySysCode(String sysCode){
		List<OrgFrame> list = null;
		OrgFrameVo orgFrameVo = new OrgFrameVo();
		orgFrameVo.setSysCode(sysCode);	 
		orgFrameVo.setLevel(1);
		list = this.queryOrgFrameList(orgFrameVo);
		if(list.isEmpty()){
			orgFrameVo.setLevel(2);
			list = this.queryOrgFrameList(orgFrameVo);
			if(list.isEmpty()){
				orgFrameVo.setLevel(3);
				list = this.queryOrgFrameList(orgFrameVo);
				return list.get(0);//3级
			}else{
				return list.get(0);//2级
			}
		}else{
			return list.get(0);//1级
		}
	}
	
	public IOrgFrameDao getOrgFrameDaoImpl() {
		return orgFrameDaoImpl;
	}

	public void setOrgFrameDaoImpl(IOrgFrameDao orgFrameDaoImpl) {
		this.orgFrameDaoImpl = orgFrameDaoImpl;
	}

	@Override
	public OrgFrame getOgrFrameByUser(UserVo userVo) {
		// TODO Auto-generated method stub
		User user= userServiceImpl.getUser(userVo.getId(), userVo.getLoginName(), userVo.getMobilePhone(), userVo.getJobNumber());
		if(user!=null){
			String orgId=user.getOrgFrame().getId();
			if(StringUtils.isNotBlank(orgId)){
				OrgFrame orgFrame= orgFrameDaoImpl.getEntityById(OrgFrame.class, orgId);
				return orgFrame;
			}
		}
		return null;
	}

}
