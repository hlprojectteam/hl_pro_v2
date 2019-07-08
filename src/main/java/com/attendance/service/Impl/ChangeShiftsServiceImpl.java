package com.attendance.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance.dao.IChangeShiftsDao;
import com.attendance.module.ChangeShifts;
import com.attendance.ql.AttendanceQl;
import com.attendance.service.IChangeShiftsService;
import com.attendance.vo.ChangeShiftsVo;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.urms.user.module.User;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年5月20日
 *
 */
@Service("changeShiftsServiceImpl")
public class ChangeShiftsServiceImpl extends BaseServiceImpl implements IChangeShiftsService{

	@Autowired
	public IChangeShiftsDao changeShiftsDaoImpl;
	
	@Override
	public Pager queryEntityList(Integer page, Integer rows,
			ChangeShifts changeShiftsVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEntitys(String ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(ChangeShifts changeShifts) {
		if(StringUtils.isBlank(changeShifts.getId())){
			this.save(changeShifts);
		}else{
			this.update(changeShifts);
		}
	}

	@Override
	public List<ChangeShifts> queryChangeShifts(ChangeShiftsVo changeShiftsVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer queryCountInMonth(String UserId) {
		String sql="";
		sql = AttendanceQl.MySql.changeShiftsMonthNum;
		sql = sql.replace("?", UserId);
		if(StringUtils.isNotEmpty(sql)){
			List<Object> list= changeShiftsDaoImpl.queryBySql(sql);
			if(list!=null){
				return list.size();
			}
		}
		return 0;
	}

	@Override
	public Pager queryChangeShiftsApproval(Integer page, Integer rows,
			ChangeShiftsVo changeShiftsVo) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(AttendanceQl.MySql.changeShiftsApprovalList);
		if(StringUtils.isNotBlank(changeShiftsVo.getId())){
			sql.append(" AND a.ID = ? ");		
			paramList.add(changeShiftsVo.getId());
		}
		if(StringUtils.isNotBlank(changeShiftsVo.getCreatorId())){
			sql.append(" and a.CREATOR_ID = ? ");		
			paramList.add(changeShiftsVo.getCreatorId());
		}
		if(changeShiftsVo.getApprovalStatus()!=null){
			sql.append(" and a.APPROVAL_STATUS = ? ");		
			paramList.add(changeShiftsVo.getApprovalStatus());
		}
		if(StringUtils.isNotBlank(changeShiftsVo.getApprovalUserId())){
			sql.append(" and b.APPROVAL_USER_ID = ? ");		
			paramList.add(changeShiftsVo.getApprovalUserId());
		}
		sql.append(" ORDER BY a.CREATE_TIME DESC");
	    Pager pager= changeShiftsDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
	    if(pager!=null){
	    	List<ChangeShiftsVo> list = new ArrayList<ChangeShiftsVo>();
	    	for (int i = 0; i < pager.getPageList().size(); i++) {
	    		Object[] obj = (Object[])pager.getPageList().get(i);
	    		ChangeShiftsVo csVo = new ChangeShiftsVo();
	    		if(obj[0]!=null) csVo.setId(obj[0].toString());
	    		if(obj[1]!=null) csVo.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
	    		if(obj[2]!=null) csVo.setApprovalNumber(obj[2].toString());
	    		if(obj[3]!=null) csVo.setCreatorName(obj[3].toString());
	    		if(obj[4]!=null) csVo.setApprovalStatus(Integer.parseInt(obj[4].toString()));
	    		if(obj[5]!=null){
	    			String applyMenId=obj[5].toString();
	    			User applyMen= changeShiftsDaoImpl.getEntityById(User.class, applyMenId);
	    			if(applyMen!=null){
	    				csVo.setApplyMenName(applyMen.getUserName());
	    			}
	    		} 
	    		if(obj[6]!=null){
	    			String beApplyMenId=obj[6].toString();
	    			User beApplyMen= changeShiftsDaoImpl.getEntityById(User.class, beApplyMenId);
	    			if(beApplyMen!=null){
	    				csVo.setBeApplyMenName(beApplyMen.getUserName());
	    			}
	    		} 
	    		if(obj[7]!=null) csVo.setApplyMenShift(obj[7].toString());
	    		if(obj[8]!=null) csVo.setBeApplyMenShift(obj[8].toString());
	    		if(obj[9]!=null) csVo.setChangeDateStr(
	    				DateUtil.getDateFormatString(DateUtil.getDateFromString(obj[9].toString()),
	    						DateUtil.JAVA_DATE_FORMAT_YMD));
	    		list.add(csVo);
	    	}
	    	pager.setPageList(list);
	    }
	    return pager;
	}

	

	

}
