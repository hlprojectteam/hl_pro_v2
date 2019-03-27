package com.datacenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.common.utils.helper.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.ITransferRegistrationDao;
import com.datacenter.module.TransferRegistration;
import com.datacenter.service.ITransferRegistrationService;
import com.datacenter.vo.TransferRegistrationVo;

/**
 * @Description 交接班登记表 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("transferRegistrationServiceImpl")
public class TransferRegistrationServiceImpl extends BaseServiceImpl implements ITransferRegistrationService{

	@Autowired
	private ITransferRegistrationDao transferRegistrationDaoImpl;


	@Override
	public Pager queryEntityList(Integer page, Integer rows, TransferRegistrationVo transferRegistrationVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer("select  id,create_time,duty_Date,exception_,form_Number,handover_Matters,handover_Time,lase_Watcher,shift_,this_Watcher,title_,ttId,watch_Time_End,watch_Time_Start,weather_  from dc_TransferRegistration where 1 = 1 ");
		if(StringUtils.isNotBlank(transferRegistrationVo.getTtId())){
			objectList.add(transferRegistrationVo.getTtId());
			sql.append(" and ttId = ? ");
		}
		if(transferRegistrationVo.getDutyDateStart() != null){		//日期Start
			objectList.add(transferRegistrationVo.getDutyDateStart());
			sql.append(" and duty_Date >= ? ");
		}
		if(transferRegistrationVo.getDutyDateEnd() != null){		//日期End
			objectList.add(transferRegistrationVo.getDutyDateEnd());
			sql.append(" and duty_Date <= ? ");
		}
		//排序, 先根据日期倒序排序,再根据班次顺序排序
		sql.append(" order by duty_Date desc,shift_ asc ");

		Pager pager = this.transferRegistrationDaoImpl.queryEntitySQLList(page, rows, sql.toString(), objectList);

		ArrayList<TransferRegistrationVo> trVoList = new ArrayList<>();
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			TransferRegistrationVo trVo = new TransferRegistrationVo();
			if(obj[0]!=null)  trVo.setId(obj[0].toString());
			if(obj[1]!=null)  trVo.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
			if(obj[2]!=null)  trVo.setDutyDate(DateUtil.getDateFromString(obj[2].toString()));
			if(obj[3]!=null)  trVo.setException(obj[3].toString());
			if(obj[4]!=null)  trVo.setFormNumber(obj[4].toString());
			if(obj[5]!=null)  trVo.setHandoverMatters(obj[5].toString());
			if(obj[6]!=null)  trVo.setHandoverTime(DateUtil.getDateFromString(obj[6].toString()));
			if(obj[7]!=null)  trVo.setLaseWatcher(obj[7].toString());
			if(obj[8]!=null)  trVo.setShift(Integer.parseInt(obj[8].toString()));
			if(obj[9]!=null)  trVo.setThisWatcher(obj[9].toString());
			if(obj[10]!=null)  trVo.setTitle(obj[10].toString());
			if(obj[11]!=null)  trVo.setTtId(obj[11].toString());
			if(obj[12]!=null)  trVo.setWatchTimeEnd(DateUtil.getDateFromString(obj[12].toString()));
			if(obj[13]!=null)  trVo.setWatchTimeStart(DateUtil.getDateFromString(obj[13].toString()));
			if(obj[14]!=null)  trVo.setWeather(Integer.parseInt(obj[14].toString()));
			trVoList.add(trVo);
		}
		pager.setPageList(trVoList);
		return pager;
	}

	@Override
	public TransferRegistration saveOrUpdate(TransferRegistrationVo transferRegistrationVo) {
		TransferRegistration transferRegistration = new TransferRegistration();
		BeanUtils.copyProperties(transferRegistrationVo, transferRegistration);
		if(StringUtils.isBlank(transferRegistration.getId())){
			this.save(transferRegistration);
		}else{
			this.update(transferRegistration);
		}
		return transferRegistration;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.transferRegistrationDaoImpl.excuteBySql("delete from dc_TransferRegistration where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<TransferRegistration> list = this.transferRegistrationDaoImpl.queryEntityList(params, Order.desc("createTime"), TransferRegistration.class);	//根据主表Id获取子表关联数据
		for (TransferRegistration transferRegistration : list) {
			transferRegistration.setDutyDate(dutyDate);
			this.update(transferRegistration);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<TransferRegistration> queryEntityList(TransferRegistrationVo transferRegistrationVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from TransferRegistration where 1 = 1 ");
		if(StringUtils.isNotBlank(transferRegistrationVo.getTtId())){
			objectList.add(transferRegistrationVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(transferRegistrationVo.getDutyDateStart() != null){		//日期Start
			objectList.add(transferRegistrationVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(transferRegistrationVo.getDutyDateEnd() != null){		//日期End
			objectList.add(transferRegistrationVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 先根据日期倒序排序,再根据班次顺序排序
		hql.append(" order by dutyDate desc,shift asc ");

		List<TransferRegistration> trList = this.transferRegistrationDaoImpl.queryEntityHQLList(hql.toString(), objectList, TransferRegistration.class);
		return trList;
	}

}
