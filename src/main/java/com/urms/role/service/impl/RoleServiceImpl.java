package com.urms.role.service.impl;



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
import com.common.utils.helper.Pager;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.role.dao.IRoleDao;
import com.urms.role.module.Role;
import com.urms.role.ql.RoleQL;
import com.urms.role.service.IRoleService;
import com.urms.role.vo.RoleVo;
import com.urms.user.module.User;

@Repository("roleServiceImpl")
@SuppressWarnings("unchecked")
public class RoleServiceImpl extends BaseServiceImpl implements IRoleService{
	
	@Autowired
	public IRoleDao roleDaoImpl;
	@Autowired
	public IOrgFrameService orgFrameServiceImpl;
	
	@Override
	public Pager queryEntityList(int page,int rows,RoleVo roleVo,User user){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(roleVo.getOrgFrame()!=null){
			if(StringUtils.isNotBlank(roleVo.getOrgFrame().getId())){
				List<OrgFrame> ofList = orgFrameServiceImpl.getOrgFrameChildren(roleVo.getOrgFrame().getId());
				if(!ofList.isEmpty()){					
					String[] orgFrameIds = new String[ofList.size()+1];
					for (int i = 0; i < ofList.size(); i++) {
						orgFrameIds[i] = ofList.get(i).getId();
					}
					orgFrameIds[ofList.size()] = roleVo.getOrgFrame().getId();
					criterionsList.add(Restrictions.in("orgFrame.id", orgFrameIds));//下级所有角色				
				}else{
					criterionsList.add(Restrictions.eq("orgFrame.id", roleVo.getOrgFrame().getId()));	
				}
			}
		}
		if(StringUtils.isNotBlank(roleVo.getRoleName()))
			criterionsList.add(Restrictions.like("roleName", "%"+roleVo.getRoleName()+"%"));
		if(StringUtils.isNotBlank(roleVo.getRoleCode()))
			criterionsList.add(Restrictions.like("roleCode", "%"+roleVo.getRoleCode()+"%"));
		if(user.getType()!=1){//不是超级管理员情况下
			criterionsList.add(Restrictions.eq("sysCode", user.getSysCode()));
		}
		if(StringUtils.isNotBlank(roleVo.getSysCode())){
			criterionsList.add(Restrictions.eq("sysCode", roleVo.getSysCode()));
		}
		Pager pager = roleDaoImpl.queryList(page, rows, criterionsList, Order.asc("roleCode") ,Role.class);
		List<RoleVo> rvo = new ArrayList<RoleVo>();
		List<Role> list = (List<Role>)pager.getPageList();
		for (int i = 0; i < list.size(); i++) {
			RoleVo rVo = new RoleVo();
			BeanUtils.copyProperties(list.get(i), rVo);
			rVo.setOrgFrameNames(list.get(i).getOrgFrame().getpNames());//父亲节点集合
			rvo.add(rVo);
		}
		pager.setPageList(rvo);
		return pager;
	}
	
	@Override
	public List<Role> queryRoleList(RoleVo roleVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(roleVo.getOrgFrame()!=null){
			if(StringUtils.isNotBlank(roleVo.getOrgFrame().getId())){
				List<OrgFrame> ofList = orgFrameServiceImpl.getOrgFrameChildren(roleVo.getOrgFrame().getId());
				if(!ofList.isEmpty()){					
					String[] orgFrameIds = new String[ofList.size()+1];
					for (int i = 0; i < ofList.size(); i++) {
						orgFrameIds[i] = ofList.get(i).getId();
					}
					orgFrameIds[ofList.size()] = roleVo.getOrgFrame().getId();
					criterionsList.add(Restrictions.in("orgFrame.id", orgFrameIds));//下级所有角色				
				}else{
					criterionsList.add(Restrictions.eq("orgFrame.id", roleVo.getOrgFrame().getId()));	
				}
			}
		}
		if(StringUtils.isNotBlank(roleVo.getRoleName()))
			criterionsList.add(Restrictions.like("roleName", "%"+roleVo.getRoleName()+"%"));
		if(StringUtils.isNotBlank(roleVo.getSysCode()))
			criterionsList.add(Restrictions.eq("sysCode", roleVo.getSysCode()));
		return roleDaoImpl.queryList(criterionsList, Order.desc("createTime"), Role.class);
	}
	
	@Override
	public void saveOrUpdate(Role role){
		if(StringUtils.isBlank(role.getId())){
			this.save(role);
		}else{
			this.update(role);
			Role r = this.getEntityById(Role.class, role.getId());
			role.setUsers(r.getUsers());
			role.setOrgFrame(r.getOrgFrame());
			BeanUtils.copyProperties(role,r);
			this.update(r);
		}
	}
	
	@Override
	public Pager queryRelationUser(int page,int rows,RoleVo roleVo){
		List<Object> attr = new ArrayList<Object>();
		if(StringUtils.isNotBlank(roleVo.getId())){
			attr.add(roleVo.getId());
			return roleDaoImpl.queryEntityHQLList(page, rows, RoleQL.HQL.RelationUser, attr);
		}else if(StringUtils.isNotBlank(roleVo.getRoleCode())){
			attr.add(roleVo.getRoleCode());
			return roleDaoImpl.queryEntityHQLList(page, rows, RoleQL.HQL.RelationUserByRoleCode, attr);
		}
		return null;
	}
	
	@Override
	public boolean isRoleExist(RoleVo roleVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(roleVo.getRoleName()))
			criterionsList.add(Restrictions.eq("roleName", roleVo.getRoleName()));
		if(StringUtils.isNotBlank(roleVo.getRoleCode()))
			criterionsList.add(Restrictions.eq("roleCode", roleVo.getRoleCode()));
		if(StringUtils.isNotBlank(roleVo.getId()))
			criterionsList.add(Restrictions.eq("id", roleVo.getId()));
		List<Role> list= roleDaoImpl.queryList(criterionsList, Order.desc("createTime"), Role.class);
		if(list!=null){
			if(list.size()>0)
				return true;
		}
		return false;
	}
	
	public IRoleDao getRoleDaoImpl() {
		return roleDaoImpl;
	}

	public void setRoleDaoImpl(IRoleDao roleDaoImpl) {
		this.roleDaoImpl = roleDaoImpl;
	}

	@Override
	public Role getRole(RoleVo roleVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(roleVo.getRoleName()))
			criterionsList.add(Restrictions.eq("roleName", roleVo.getRoleName()));
		if(StringUtils.isNotBlank(roleVo.getRoleCode()))
			criterionsList.add(Restrictions.eq("roleCode", roleVo.getRoleCode()));
		if(StringUtils.isNotBlank(roleVo.getId()))
			criterionsList.add(Restrictions.eq("id", roleVo.getId()));
		List<Role> list= roleDaoImpl.queryList(criterionsList, Order.desc("createTime"), Role.class);
		if(list!=null){
			if(list.size()>0)
				return list.get(0);
		}
		return null;
	}

	


	
}
