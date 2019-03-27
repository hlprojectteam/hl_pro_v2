package com.urms.loginLog.service.impl;

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
import com.urms.loginLog.dao.ILoginLogDao;
import com.urms.loginLog.module.LoginLog;
import com.urms.loginLog.service.ILoginLogService;
import com.urms.loginLog.vo.LoginLogVo;

@Repository("loginLogServiceImpl")
public class LoginLogServiceImpl extends BaseServiceImpl implements ILoginLogService {

	@Autowired
	public ILoginLogDao loginLogDaoImpl;

	@Override
	public void saveOrUpdate(LoginLog loginLog) {
		if (StringUtils.isBlank(loginLog.getLoginLogId())) {
			this.save(loginLog);
		} else {
			LoginLog log = this.getEntityById(LoginLog.class, loginLog.getLoginLogId());
			long loginTime=0;
			long outTime=0;
			if(log.getLoginTime() != null){
				loginTime = log.getLoginTime().getTime();
				loginLog.setLoginTime(log.getLoginTime());
			}
			outTime = loginLog.getLogOutTime().getTime();
			long onlineTime = outTime - loginTime;
			loginLog.setOnlineTime(onlineTime);

			BeanUtils.copyProperties(loginLog, log);
			this.update(log);
		}
	}

	@Override
	public Pager queryEntityList(int page, int rows, LoginLogVo loginLogVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(loginLogVo.getLoginAccount())) {
			criterionsList.add(Restrictions.like("loginAccount", "%" + loginLogVo.getLoginAccount() + "%"));
		}
		if (StringUtils.isNotBlank(loginLogVo.getLoginIp())) {
			criterionsList.add(Restrictions.eq("loginIp", loginLogVo.getLoginIp()));
		}
		if (StringUtils.isNotBlank(loginLogVo.getUserId())) {
			criterionsList.add(Restrictions.eq("userId", loginLogVo.getUserId()));
		}
		if (loginLogVo.getLoginSource() != null) {
			criterionsList.add(Restrictions.eq("loginSource", loginLogVo.getLoginSource()));
		}
		if (loginLogVo.getLoginState() != null) {
			criterionsList.add(Restrictions.eq("loginState", loginLogVo.getLoginState()));
		}
		if (StringUtils.isNotBlank(loginLogVo.getSysCode())) {
			criterionsList.add(Restrictions.eq("sysCode", loginLogVo.getSysCode()));
		}
		return this.loginLogDaoImpl.queryList(page, rows, criterionsList, Order.desc("loginTime"), LoginLog.class);
	}

	public ILoginLogDao getLoginLogDaoImpl() {
		return loginLogDaoImpl;
	}

	public void setLoginLogDaoImpl(ILoginLogDao loginLogDaoImpl) {
		this.loginLogDaoImpl = loginLogDaoImpl;
	}

}
