package com.dangjian.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.dangjian.module.PartyMember;
import com.dangjian.vo.PartyMemberVo;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月1日
 *
 */
public interface IPartyMemberService extends IBaseService{

	/**********以下是 党员档案方法************/
	public Pager queryPartyMemberEntityList(Integer page, Integer rows,PartyMemberVo partyMemberVo);
	
	public void deletePartyMemberEntitys(String ids);
	
	public void saveOrUpdatePartyMember(PartyMember partyMember);
	
	public PartyMember getPartyMemberByUser(UserVo userVo);
	
	public boolean isHaveMember(String memberName,String branch);

}
