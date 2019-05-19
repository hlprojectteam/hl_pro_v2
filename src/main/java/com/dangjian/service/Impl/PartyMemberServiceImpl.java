package com.dangjian.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.attach.service.IAttachService;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.dangjian.dao.IPartyMemberDao;
import com.dangjian.module.PartyMember;
import com.dangjian.ql.DangjianQl;
import com.dangjian.service.IPartyMemberService;
import com.dangjian.vo.PartyMemberVo;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月1日
 *
 */
@Repository("partyMemberServiceImpl")
public class PartyMemberServiceImpl extends BaseServiceImpl implements IPartyMemberService{

	@Autowired
	public IPartyMemberDao partyMemberDaoImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Override
	public Pager queryPartyMemberEntityList(Integer page, Integer rows,
			PartyMemberVo partyMemberVo) {
		
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append(DangjianQl.MySql.partyMemberEntityListSql);
		if(StringUtils.isNotBlank(partyMemberVo.getBranchId())){
			sql.append(" and br.id = ? ");					
			paramList.add(partyMemberVo.getBranchId());
		}
		if(partyMemberVo.getMemberDuty()!=null){
			sql.append(" and pm.MEMBER_DUTY = ? ");					
			paramList.add(partyMemberVo.getMemberDuty());
		}
		if(StringUtils.isNotBlank(partyMemberVo.getUserName())){		
			sql.append(" and u.USER_NAME like ? ");						
			paramList.add("%"+partyMemberVo.getUserName()+"%");
		}
		if(StringUtils.isNotBlank(partyMemberVo.getUserId())){		
			sql.append(" and u.ID = ? ");						
			paramList.add(partyMemberVo.getUserId());
		}
		sql.append(" ORDER by u.USER_NAME ");

		Pager pager = this.partyMemberDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
		if(pager==null){
			return null;
		}
		List<PartyMemberVo> list = new ArrayList<PartyMemberVo>();
		
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			PartyMemberVo pmVo = new PartyMemberVo();
			if(obj[0]!=null) pmVo.setId(obj[0].toString());
			if(obj[1]!=null) pmVo.setUserName(obj[1].toString());
			if(obj[2]!=null) pmVo.setSex(Integer.parseInt(obj[2].toString()));
			if(obj[3]!=null) pmVo.setBranchName(obj[3].toString());
			if(obj[4]!=null) pmVo.setMemberDuty(Integer.parseInt(obj[4].toString()));
			if(obj[5]!=null) pmVo.setJoininTime(DateUtil.getDateFromString(obj[5].toString()));
			list.add(pmVo);
		}
		
		pager.setPageList(list);
		return pager;
	
	}

	@Override
	public void deletePartyMemberEntitys(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.attachServiceImpl.deleteAttachByFormId(idz[i]);
			this.delete(PartyMember.class, idz[i]);
		}
	}

	@Override
	public void saveOrUpdatePartyMember(PartyMember partyMember) {
		if(StringUtils.isBlank(partyMember.getId())){
			this.save(partyMember);
		}else{
			this.update(partyMember);
		}
		
	}

	@Override
	public PartyMember getPartyMemberByUser(UserVo userVo) {
		// TODO Auto-generated method stub
//		List<Criterion> criterionsList = new ArrayList<Criterion>();
//		if(StringUtils.isNotBlank(userVo.getId())){
//			criterionsList.add(Restrictions.eq("userId", userVo.getu);
//		}
//		return this.partyMemberDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime"), PartyMember.class);
		
		List<Object> attributeList = new ArrayList<Object>();
		String hql="select a from PartyMember a where userId = ?";
		attributeList.add(userVo.getId());
		return partyMemberDaoImpl.getEntityByHQL(hql, attributeList);
	
	}

	@Override
	public boolean isHaveMember(String memberName, String branch) {
		// TODO Auto-generated method stub
//		List<Criterion> criterionsList = new ArrayList<Criterion>();
//		criterionsList.add(Restrictions.eq("buiName", memberName));
//		criterionsList.add(Restrictions.eq("unitNum", unit));
//		criterionsList.add(Restrictions.eq("roomNum", roomNum));
//		List<House> list = this.houseDaoImpl.queryEntityList(criterionsList,
//				null, House.class);
//		if (list != null) {
//			return list.size();
//		}
		return false;
	}

}
