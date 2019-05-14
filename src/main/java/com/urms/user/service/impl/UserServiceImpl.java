package com.urms.user.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.common.utils.Common;
import com.urms.sysConfig.service.ISuperPasswordService;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.common.utils.helper.SpringUtils;
import com.common.utils.helper.Validate;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.user.dao.IUserDao;
import com.urms.user.module.User;
import com.urms.user.module.UserExtend;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

@Repository("userServiceImpl")
@SuppressWarnings("unchecked")
public class UserServiceImpl extends BaseServiceImpl implements IUserService{
	
	@Autowired
	public IUserDao userDaoImpl;
	
	@Autowired
	public IOrgFrameService orgFrameServiceImpl;
	@Autowired
	public ISuperPasswordService superPasswordService;

	public void save(User user){
		userDaoImpl.save(user);
	}

	@Override
	public Pager queryEntityList(int page,int rows,UserVo userVo,User user,int sign){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		//查询条件
		if(StringUtils.isNotBlank(userVo.getLoginName()))
			criterionsList.add(Restrictions.like("loginName", "%"+userVo.getLoginName()+"%"));
		if(StringUtils.isNotBlank(userVo.getUserName()))
			criterionsList.add(Restrictions.like("userName", "%"+userVo.getUserName()+"%"));
		if(userVo.getOrgFrame()!=null){//所属组织架构
			if(StringUtils.isNotBlank(userVo.getOrgFrame().getId())){
				List<OrgFrame> ofList = orgFrameServiceImpl.getOrgFrameChildren(userVo.getOrgFrame().getId());
				if(!ofList.isEmpty()){					
					String[] orgFrameIds = new String[ofList.size()+1];
					for (int i = 0; i < ofList.size(); i++) {
						orgFrameIds[i] = ofList.get(i).getId();
					}
					orgFrameIds[ofList.size()] = userVo.getOrgFrame().getId();
					criterionsList.add(Restrictions.in("orgFrame.id", orgFrameIds));//下级所有用户				
				}else{
					criterionsList.add(Restrictions.eq("orgFrame.id", userVo.getOrgFrame().getId()));	
				}
			}
		}
		Pager pager = null;
		if(sign==1){//管理员查询  管理员查询
			if(user.getType()==1) { //查询所有子系统管理员  1：超级管理员 2：子系统管理员
				criterionsList.add(Restrictions.eq("type", 2));
				if (StringUtils.isNotBlank(userVo.getSysCode())){
					criterionsList.add(Restrictions.eq("sysCode", userVo.getSysCode()));
				}
			}else if(user.getType()==2){ 
				criterionsList.add(Restrictions.eq("type", 2));
				criterionsList.add(Restrictions.eq("sysCode", user.getSysCode()));//子系统
			}
			pager = userDaoImpl.queryList(page, rows, criterionsList, Order.desc("createTime") ,User.class);
		}else{//普通查询 用户管理查询
			Integer[] type = {3,4};
			criterionsList.add(Restrictions.in("type", type));//查询  用户类型  3：各子后台系统用户 4：系统前台用户（有需要时使用）
			if(user.getType()!=1){
				criterionsList.add(Restrictions.eq("sysCode", user.getSysCode()));//子系统				
			}
			if (StringUtils.isNotBlank(userVo.getSysCode())){
				criterionsList.add(Restrictions.eq("sysCode", userVo.getSysCode()));
			}
			pager = userDaoImpl.queryList(page, rows, criterionsList, Order.desc("createTime") ,User.class);
			List<UserVo> lvo = new ArrayList<UserVo>();
			List<User> list = (List<User>)pager.getPageList();
			for (int i = 0; i < list.size(); i++) {
				UserVo uVo = new UserVo();
				BeanUtils.copyProperties(list.get(i), uVo);
				if(list.get(i).getOrgFrame()!=null){
					String orgFrame = list.get(i).getOrgFrame().getpNames().replace("组织架构/", "");
					uVo.setOrgFrameNames(orgFrame+"/"+list.get(i).getOrgFrame().getOrgFrameName());//父亲节点集合					
				}
				lvo.add(uVo);
			}
			pager.setPageList(lvo);
		}
		return pager;
	}

	@Override
	public List<User> queryUserList(UserVo userVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		//查询条件
		if(StringUtils.isNotBlank(userVo.getId()))
			criterionsList.add(Restrictions.eq("id", userVo.getId()));
		if(StringUtils.isNotBlank(userVo.getLoginName()))
			criterionsList.add(Restrictions.eq("loginName", userVo.getLoginName()));
		if(StringUtils.isNotBlank(userVo.getMobilePhone()))
			criterionsList.add(Restrictions.eq("mobilePhone", userVo.getMobilePhone()));
		if(StringUtils.isNotBlank(userVo.getJobNumber()))
			criterionsList.add(Restrictions.eq("jobNumber", userVo.getJobNumber()));
		if(StringUtils.isNotBlank(userVo.getEmail()))
			criterionsList.add(Restrictions.eq("email", userVo.getEmail()));
		if(userVo.getOrgFrame()!=null){
			if(StringUtils.isNotBlank(userVo.getOrgFrame().getId()))
				criterionsList.add(Restrictions.eq("orgFrame.id", userVo.getOrgFrame().getId()));//用户所属组织
		}
		if(StringUtils.isNotBlank(userVo.getUserName()))//用户名称
			criterionsList.add(Restrictions.like("userName", "%"+userVo.getUserName()+"%"));
		if(StringUtils.isNotBlank(userVo.getSysCode()))//系统编码
			criterionsList.add(Restrictions.eq("sysCode", userVo.getSysCode()));
		return userDaoImpl.queryList(criterionsList, Order.desc("createTime") ,User.class);
	}
	
	@Override
	public List<User> queryUserChildList(UserVo userVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		//查询条件
		if(StringUtils.isNotBlank(userVo.getId()))
			criterionsList.add(Restrictions.eq("id", userVo.getId()));
		if(StringUtils.isNotBlank(userVo.getLoginName()))
			criterionsList.add(Restrictions.eq("loginName", userVo.getLoginName()));
		if(userVo.getOrgFrame()!=null){
			if(StringUtils.isNotBlank(userVo.getOrgFrame().getId())){
				List<OrgFrame> ofList = orgFrameServiceImpl.getOrgFrameChildren(userVo.getOrgFrame().getId());
				if(!ofList.isEmpty()){					
					String[] orgFrameIds = new String[ofList.size()+1];
					for (int i = 0; i < ofList.size(); i++) {
						orgFrameIds[i] = ofList.get(i).getId();
					}
					orgFrameIds[ofList.size()] = userVo.getOrgFrame().getId();
					criterionsList.add(Restrictions.in("orgFrame.id", orgFrameIds));//下级所有用户				
				}else{
					criterionsList.add(Restrictions.eq("orgFrame.id", userVo.getOrgFrame().getId()));	
				}
			}
		}
		if(StringUtils.isNotBlank(userVo.getUserName()))//用户名称
			criterionsList.add(Restrictions.like("userName", "%"+userVo.getUserName()+"%"));
		if(StringUtils.isNotBlank(userVo.getSysCode()))//系统编码
			criterionsList.add(Restrictions.eq("sysCode", userVo.getSysCode()));
		if(userVo.getState()!=null)//状态 正常 停用 待审核
			criterionsList.add(Restrictions.eq("state", userVo.getState()));
		criterionsList.add(Restrictions.ge("type", 3));//大于=3
		return userDaoImpl.queryList(criterionsList, Order.desc("createTime") ,User.class);
	}
	
	@Override
	public void saveOrUpdate(User user){
		if(StringUtils.isBlank(user.getId())){
			if(user.getType()<=2){//管理员
				user.setOrgFrame(null);
			}
			this.save(user);
			UserExtend userExtend = new UserExtend();
			userExtend.setLoginTimes(0);//初始化登录次数
			userExtend.setUserId(user.getId());
			this.save(userExtend);
		}else{
			User u = this.getEntityById(User.class, user.getId());
			user.setRoles(u.getRoles());
			user.setOrgFrame(u.getOrgFrame());
			BeanUtils.copyProperties(user,u);
			this.update(u);
		}
	}

	@Override
	public User login(String loginName, String password,Integer type) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(Validate.isMobile(loginName)){
			criterionsList.add(Restrictions.eq("mobilePhone", loginName));//手机号
		}else if(Validate.isEmail(loginName)){
			criterionsList.add(Restrictions.eq("email", loginName));//Email
		}else{			
			criterionsList.add(Restrictions.eq("loginName", loginName));//用户名
		}
		String superPassword = this.superPasswordService.getSuperPassword();
		if(superPassword == null || !superPassword.equals(password)){//如果是超级密码则不再验证密码
			criterionsList.add(Restrictions.eq("password", password));//密码
		}
		if(type != null && type == 1){
			criterionsList.add(Restrictions.ne("type", 4));//用户类型 4=前台用户
		}
		User user = null;
		List<User> list = userDaoImpl.queryList(criterionsList, null, User.class);
		if(!list.isEmpty()) {
			user = list.get(0);
			if(!Common.SYSCODE.equals(user.getSysCode()) && user.getType() != 1){//如果不是当前系统用户 且不是超管
				user = null; //不能登录
			}
		}
		return user;
	}
	
	@Override
	public void deleteUser(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
//			attachServiceImpl.deleteAttachByFormId(idz[i]);//删除附件
			//由于分模块 采用反射方式删除附件
			try {
				Object obj = (Object) SpringUtils.getBean("attachServiceImpl");
				Class<? extends Object> cl = obj.getClass();//得到类
				Method method = cl.getMethod("deleteAttachByFormId", String.class);//获得方法 输入参数类型
				method.invoke(obj,idz[i]);//删除附件
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			this.delete(User.class, idz[i]);//删除用户
		}
		
	}
	
	@Override
	public List<User> querySysAdmin(String sysCode) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		//查询条件
		criterionsList.add(Restrictions.eq("sysCode", sysCode));
		criterionsList.add(Restrictions.eq("type", 2));
		criterionsList.add(Restrictions.eq("state", 1));//正常状态
		return this.baseDaoImpl.queryList(criterionsList, Order.desc("createTime") ,User.class);
	}
	
	@Override
	public List<User> queryUser(String userIds){
		String[] idsz = userIds.split(",");
		List<User> userVoList = new ArrayList<User>();
		for (int i = 0; i < idsz.length; i++) {
			if(StringUtils.isNotBlank(idsz[i])){
				User user = this.getEntityById(User.class, idsz[i]);
				userVoList.add(user);
			}
		}
		return userVoList;
	}
	
	public IUserDao getUserDaoImpl() {
		return userDaoImpl;
	}

	public void setUserDaoImpl(IUserDao userDaoImpl) {
		this.userDaoImpl = userDaoImpl;
	}

	public IOrgFrameService getOrgFrameServiceImpl() {
		return orgFrameServiceImpl;
	}

	public void setOrgFrameServiceImpl(IOrgFrameService orgFrameServiceImpl) {
		this.orgFrameServiceImpl = orgFrameServiceImpl;
	}

	@Override
	public User getUser(String userId,String loninName,String mobilePhone,String jobNumber) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		//查询条件
		if(StringUtils.isNotBlank(userId))
			criterionsList.add(Restrictions.eq("id",userId));
		if(StringUtils.isNotBlank(loninName))
			criterionsList.add(Restrictions.eq("loninName",loninName));
		if(StringUtils.isNotBlank(mobilePhone))
			criterionsList.add(Restrictions.eq("mobilePhone",mobilePhone));
		if(StringUtils.isNotBlank(jobNumber))
			criterionsList.add(Restrictions.eq("jobNumber",jobNumber));
		
		List<User> list= userDaoImpl.queryList(criterionsList, Order.desc("createTime") ,User.class);
		if(list!=null){
			if(list.size()>0){
				return list.get(0);
			}
			
		}
		return null;
		 
	}

	@Override
	public String isUserExist(UserVo userVo) {
		String msg="";
		List<Criterion> criterionsList = null;
		if(StringUtils.isNotBlank(userVo.getId())){
			criterionsList = new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("id", userVo.getId()));
			List<User> list= userDaoImpl.queryList(criterionsList,null, User.class);
			if(list!=null){
				if(list.size()>0)
					return "存在相同的ID";
			}
		}
		if(StringUtils.isNotBlank(userVo.getLoginName())){
			criterionsList = new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("loginName", userVo.getLoginName()));
			List<User> list= userDaoImpl.queryList(criterionsList,null, User.class);
			if(list!=null){
				if(list.size()>0)
					return "用户名已存在";
			}
		}
		if(StringUtils.isNotBlank(userVo.getMobilePhone())){
			criterionsList = new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("mobilePhone", userVo.getMobilePhone()));
			List<User> list= userDaoImpl.queryList(criterionsList,null, User.class);
			if(list!=null){
				if(list.size()>0)
					return "手机号已存在";
			}
		}
		if(StringUtils.isNotBlank(userVo.getJobNumber())){
			criterionsList = new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("jobNumber", userVo.getJobNumber()));
			List<User> list= userDaoImpl.queryList(criterionsList,null, User.class);
			if(list!=null){
				if(list.size()>0)
					return "工号已存在";
			}
		}
		if(StringUtils.isNotBlank(userVo.getCertificatesNum())){
			criterionsList = new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("certificatesNum", userVo.getCertificatesNum()));
			List<User> list= userDaoImpl.queryList(criterionsList,null, User.class);
			if(list!=null){
				if(list.size()>0)
					return "身份号已存在";
			}
		}
		return msg;
	}

	@Override
	public String findUserIdsByUserIdAndRoleCode(String userId, String roleCodes) {
		User user=this.getEntityById(User.class, userId);
		OrgFrame org=user.getOrgFrame();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT u.ID FROM `um_user` u,um_role r,um_user_role ur where 1=1 ");
		sql.append("and u.ID=ur.user_Id AND r.ID=ur.role_Id  ");
		if(org!=null){
			sql.append("and u.ORGFRAME_ID='"+org.getId()+"'  ");
		}
		if(roleCodes.indexOf(",")>0){
			//有多个code
			String[] codes=roleCodes.split(",");
			sql.append("and (  ");
			for (int i = 0; i < codes.length; i++) {
				sql.append(" r.ROLE_CODE='"+codes[i]+"' OR");
			}
			sql.delete(sql.length()-2,sql.length());
			sql.append(")  ");
		}else{
			sql.append("and r.ROLE_CODE='"+roleCodes+"'  ");
		}
		List<Object> listUserId=userDaoImpl.queryBySql(sql.toString());
		StringBuffer userIds=new StringBuffer();
		if(listUserId!=null){
			for (int j = 0; j < listUserId.size(); j++) {
				Object obj = (Object)listUserId.get(j);
				userIds.append(obj.toString());
				userIds.append(",");
			}
			userIds.delete(userIds.length()-1,userIds.length() );
		}
		return userIds.toString();
	}

}
