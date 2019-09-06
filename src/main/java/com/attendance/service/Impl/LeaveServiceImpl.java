package com.attendance.service.Impl;

import java.util.ArrayList;
import java.util.List;








import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance.dao.ILeaveDao;
import com.attendance.module.Leave;
import com.attendance.ql.AttendanceQl;
import com.attendance.service.ILeaveService;
import com.attendance.vo.LeaveVo;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.common.utils.tld.DictUtils;
import com.urms.user.module.User;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年5月20日
 *
 */
@Service("leaveServiceImpl")
public class LeaveServiceImpl extends BaseServiceImpl implements ILeaveService{

	@Autowired
	public ILeaveDao leaveDaoImpl;
	@Override
	public Pager queryEntityList(Integer page, Integer rows, LeaveVo leaveVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(leaveVo.getCreatorId())){
			criterionsList.add(Restrictions.eq("creatorId",  leaveVo.getCreatorId()));
		}
		if(leaveVo.getApprovalStatus()!=null){
			criterionsList.add(Restrictions.eq("approvalStatus",  leaveVo.getApprovalStatus()));
		}
		return this.leaveDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime"), Leave.class);
	}

	@Override
	public void deleteEntitys(String ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(Leave leave) {
		if(StringUtils.isBlank(leave.getId())){
			this.save(leave);
		}else{
			this.update(leave);
		}
	}

	@Override
	public List<Leave> queryLeave(LeaveVo leaveVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer queryCountInMonth(String UserId) {
		String sql="";
		sql = AttendanceQl.MySql.leaveMonthNum;
		sql = sql.replace("?", UserId);
		if(StringUtils.isNotEmpty(sql)){
			List<Object> list= leaveDaoImpl.queryBySql(sql);
			if(list!=null){
				return list.size();
			}
		}
		return 0;
	}

	@Override
	public Pager queryLeaveApproval(Integer page, Integer rows, LeaveVo leaveVo) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(AttendanceQl.MySql.leaveApprovalList);
		if(StringUtils.isNotBlank(leaveVo.getId())){
			sql.append(" AND a.ID = ? ");		
			paramList.add(leaveVo.getId());
		}
		if(StringUtils.isNotBlank(leaveVo.getCreatorId())){
			sql.append(" and a.CREATOR_ID = ? ");		
			paramList.add(leaveVo.getCreatorId());
		}
		if(leaveVo.getApprovalStatus()!=null){
			sql.append(" and a.APPROVAL_STATUS = ? ");		
			paramList.add(leaveVo.getApprovalStatus());
		}
		if(StringUtils.isNotBlank(leaveVo.getApprovalUserId())){
			sql.append(" and b.APPROVAL_USER_ID = ? ");		
			paramList.add(leaveVo.getApprovalUserId());
		}
		sql.append(" ORDER BY a.CREATE_TIME DESC");
	    Pager pager= leaveDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
	    if(pager!=null){
	    	List<LeaveVo> list = new ArrayList<LeaveVo>();
	    	for (int i = 0; i < pager.getPageList().size(); i++) {
	    		Object[] obj = (Object[])pager.getPageList().get(i);
	    		LeaveVo lVo = new LeaveVo();
	    		if(obj[0]!=null) lVo.setId(obj[0].toString());
	    		if(obj[1]!=null) lVo.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
	    		if(obj[2]!=null) lVo.setApprovalNumber(obj[2].toString());
	    		if(obj[3]!=null) lVo.setCreatorName(obj[3].toString());
	    		if(obj[4]!=null) lVo.setApprovalStatus(Integer.parseInt(obj[4].toString()));
	    		if(obj[5]!=null) lVo.setStartTime(DateUtil.getDateFromString(obj[5].toString()));
	    		if(obj[6]!=null) lVo.setEndTime(DateUtil.getDateFromString(obj[6].toString()));
	    		list.add(lVo);
	    	}
	    	pager.setPageList(list);
	    }
	    return pager;
	}

	/*******************************************以下是请假记录********************************************************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Pager queryLeaveRecord(Integer page, Integer rows, LeaveVo leaveVo) {
		List<Object> listSql=getQueryRecordSql(leaveVo);
		String sql=(String)listSql.get(0);
		List<Object> paramList=(ArrayList)listSql.get(1);
	    Pager pager= leaveDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
	    if(pager!=null){
	    	List<LeaveVo> list = new ArrayList<LeaveVo>();
	    	for (int i = 0; i < pager.getPageList().size(); i++) {
	    		Object[] obj = (Object[])pager.getPageList().get(i);
	    		LeaveVo lVo = new LeaveVo();
	    		if(obj[0]!=null) lVo.setId(obj[0].toString());
	    		if(obj[1]!=null) lVo.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
	    		if(obj[2]!=null) lVo.setApprovalNumber(obj[2].toString());
	    		if(obj[3]!=null) lVo.setCreatorId(obj[3].toString());
	    		if(obj[4]!=null) lVo.setCreatorName(obj[4].toString());
	    		if(obj[5]!=null) lVo.setStartTime(DateUtil.getDateFromString(obj[5].toString()));
	    		if(obj[6]!=null) lVo.setEndTime(DateUtil.getDateFromString(obj[6].toString()));
	    		if(obj[7]!=null) lVo.setTimeLength(Double.parseDouble(obj[7].toString()));
	    		if(obj[8]!=null) lVo.setLeaveType(obj[8].toString());
	    		if(obj[9]!=null) lVo.setApprovalStatus(Integer.parseInt(obj[9].toString()));
	    		if(obj[10]!=null){
	    			String approvalUserId=obj[10].toString();
	    			User approvalUser=this.baseDaoImpl.getEntityById(User.class, approvalUserId);
	    			lVo.setApprovalUserId(obj[10].toString());
	    			if(approvalUser!=null){
	    				lVo.setApprovalUserName(approvalUser.getUserName());
	    			}
	    		}
	    		if(obj[11]!=null) lVo.setCreatorJobNumber(obj[11].toString());
	    		if(obj[12]!=null) lVo.setCreatorOrgName(obj[12].toString());
	    		if(obj[13]!=null) lVo.setLeaveReason(obj[13].toString());
	    		if(obj[14]!=null) lVo.setOutAddress(obj[14].toString());
	    		if(obj[15]!=null) lVo.setApprovalTime(DateUtil.getDateFromString(obj[15].toString()));
	    		if(obj[16]!=null) lVo.setApprovalContent(obj[16].toString());
	    		list.add(lVo);
	    	}
	    	pager.setPageList(list);
	    }
	    return pager;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<LeaveVo> queryRecordList(LeaveVo leaveVo) {
		List<Object> listSql=getQueryRecordSql(leaveVo);
		String sql=(String)listSql.get(0);
		List<Object> paramList=(ArrayList)listSql.get(1);
		List<Object> objList= leaveDaoImpl.queryEntitySQLList(sql.toString(), paramList);
		
		List<LeaveVo> list = new ArrayList<LeaveVo>();
		
		if(objList!=null){
	    	for (int i = 0; i < objList.size(); i++) {
	    		Object[] obj = (Object[])objList.get(i);
	    		LeaveVo lVo = new LeaveVo();
	    		if(obj[0]!=null) lVo.setId(obj[0].toString());
	    		if(obj[1]!=null) lVo.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
	    		if(obj[2]!=null) lVo.setApprovalNumber(obj[2].toString());
	    		if(obj[3]!=null) lVo.setCreatorId(obj[3].toString());
	    		if(obj[4]!=null) lVo.setCreatorName(obj[4].toString());
	    		if(obj[5]!=null) lVo.setStartTime(DateUtil.getDateFromString(obj[5].toString()));
	    		if(obj[6]!=null) lVo.setEndTime(DateUtil.getDateFromString(obj[6].toString()));
	    		if(obj[7]!=null) lVo.setTimeLength(Double.parseDouble(obj[7].toString()));
	    		if(obj[8]!=null) lVo.setLeaveType(obj[8].toString());
	    		if(obj[9]!=null) lVo.setApprovalStatus(Integer.parseInt(obj[9].toString()));
	    		if(obj[10]!=null){
	    			String approvalUserId=obj[10].toString();
	    			User approvalUser=this.baseDaoImpl.getEntityById(User.class, approvalUserId);
	    			lVo.setApprovalUserId(obj[10].toString());
	    			if(approvalUser!=null){
	    				lVo.setApprovalUserName(approvalUser.getUserName());
	    			}
	    		}
	    		if(obj[11]!=null) lVo.setCreatorJobNumber(obj[11].toString());
	    		if(obj[12]!=null) lVo.setCreatorOrgName(obj[12].toString());
	    		if(obj[13]!=null) lVo.setLeaveReason(obj[13].toString());
	    		if(obj[14]!=null) lVo.setOutAddress(obj[14].toString());
	    		if(obj[15]!=null) lVo.setApprovalTime(DateUtil.getDateFromString(obj[15].toString()));
	    		if(obj[16]!=null) lVo.setApprovalContent(obj[16].toString());
	    		list.add(lVo);
	    	}
	    }
		return list;
	}
	
	private List<Object> getQueryRecordSql(LeaveVo leaveVo){
		List<Object> list=new ArrayList<>();
		
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(AttendanceQl.MySql.leaveRecord);
		if(StringUtils.isNotBlank(leaveVo.getId())){
			sql.append(" AND a.ID = ? ");		
			paramList.add(leaveVo.getId());
		}
		if(StringUtils.isNotBlank(leaveVo.getCreatorName())){
			sql.append(" and a.CREATOR_NAME like ? ");		
			paramList.add("%"+leaveVo.getCreatorName()+"%");
		}
		if(StringUtils.isNotBlank(leaveVo.getCreatorJobNumber())){
			sql.append(" and b.JOB_NUMBER = ? ");		
			paramList.add(leaveVo.getCreatorJobNumber());
		}
		if(leaveVo.getApprovalStatus()!=null){
			sql.append(" and a.APPROVAL_STATUS = ? ");		
			paramList.add(leaveVo.getApprovalStatus());
		}
		if(leaveVo.getRecordDateStart()!=null){
			sql.append(" and a.CREATE_TIME >= ? ");		
			paramList.add(leaveVo.getRecordDateStart());
		}
		if(leaveVo.getRecordDateEnd()!=null){
			sql.append(" and a.CREATE_TIME <= ? ");		
			paramList.add(DateUtil.getDateEndTime(leaveVo.getRecordDateEnd()));
		}
		if(StringUtils.isNotBlank(leaveVo.getCreatorOrgId())){
			sql.append(" and c.ID = ? ");		
			paramList.add(leaveVo.getCreatorOrgId());
		}
		sql.append(" ORDER BY a.CREATE_TIME DESC");
		
		list.add(sql.toString());
		list.add(paramList);
		
		return list;
	}

	@Override
	public HSSFWorkbook exportLeaveRecord(LeaveVo leaveVo) {
		//创建HSSFWorkbook
		HSSFWorkbook wb = new HSSFWorkbook();

		//单元格 基础样式
		HSSFCellStyle mainStyle = wb.createCellStyle();
		mainStyle.setBorderBottom(BorderStyle.THIN); 	//下边框
		mainStyle.setBorderLeft(BorderStyle.THIN);		//左边框
		mainStyle.setBorderTop(BorderStyle.THIN);		//上边框
		mainStyle.setBorderRight(BorderStyle.THIN);		//右边框
		mainStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
		mainStyle.setWrapText(true);					//设置自动换行

		//基础样式 水平居中
		HSSFCellStyle mainStyle_center = wb.createCellStyle();
		mainStyle_center.cloneStyleFrom(mainStyle);
		mainStyle_center.setAlignment(HorizontalAlignment.CENTER);
		//基础样式 水平靠左
		HSSFCellStyle mainStyle_left = wb.createCellStyle();
		mainStyle_left.cloneStyleFrom(mainStyle);
		mainStyle_left.setAlignment(HorizontalAlignment.LEFT);


		//设置第三行样式(普通列标题样式)
		HSSFCellStyle r2_style = wb.createCellStyle();
		r2_style.cloneStyleFrom(mainStyle_center);	//基础样式 水平居中
		HSSFFont r2_font = wb.createFont();
		r2_font.setBold(true);						//字体加粗
		r2_style.setFont(r2_font);

		List<LeaveVo> rwList = this.queryRecordList(leaveVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("请假记录汇总");

		//列宽自适应（该方法在老版本的POI中效果不佳）
		/*sheet.autoSizeColumn(i);*/
		//设置列宽
		for (int i = 0; i < 16; i++) {
			if(i >=1){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5/2);
			}
		}


		//第一行
		HSSFRow row3 = sheet.createRow(0);
		row3.setHeightInPoints(30);		//行高
		HSSFCell cell;
		String[] title2 = {"序号","审批编号","申请时间","申请人部门","申请人","申请人工号","请假开始时间","请假结束时间","请假时长（小时）",
				"类型","请假原因","外出地址","审批人","审批时间","审批内容","审批状态"};
		for(int i=0;i<title2.length;i++){
			cell = row3.createCell(i);		//创建单元格
			cell.setCellValue(title2[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		//第二行 及 之后的行
		HSSFRow row;
		for (int i = 0; i < rwList.size(); i++) {
			row = sheet.createRow(i + 1);	//创建行
			float realRowH=30;
			LeaveVo lvo= rwList.get(i);
			for (int j = 0; j < title2.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(i+1);	break;
					case 1: cell.setCellValue(lvo.getApprovalNumber());	break;
					case 2: cell.setCellValue(DateUtil.getDateFormatString(lvo.getCreateTime(),DateUtil.JAVA_DATE_FORMAT_YMDHM));	break;
					case 3: 
						cell.setCellValue(lvo.getCreatorOrgName());	break;
					case 4: cell.setCellValue(lvo.getCreatorName());	break;
					case 5: 
						cell.setCellValue(lvo.getCreatorJobNumber());	break;
					case 6: 
						cell.setCellValue(DateUtil.getDateFormatString(lvo.getStartTime(),DateUtil.JAVA_DATE_FORMAT_YMDHM));	break;
					case 7: 
						cell.setCellValue(DateUtil.getDateFormatString(lvo.getEndTime(),DateUtil.JAVA_DATE_FORMAT_YMDHM));	break;
					case 8: cell.setCellValue(lvo.getTimeLength()); break;
					case 9: 
						if(StringUtils.isNotBlank(lvo.getLeaveType())){
							if(lvo.getLeaveType().indexOf(",")>0){
								String[] arr= lvo.getLeaveType().split(",");//拆分
								StringBuffer value=new StringBuffer();
								for (int k = 0; k < arr.length; k++) {
									String key=arr[k];
									String val= DictUtils.getDictValue("Leave_Type", key);
									value.append(val);
									value.append(",");
								}
								value=value.delete(value.length()-1, value.length());
								cell.setCellValue(value.toString());
							}else{
								cell.setCellValue(DictUtils.getDictValue("Leave_Type", lvo.getLeaveType()));
							}
						}else{
							cell.setCellValue("");
						}
						break;
					case 10: cell.setCellValue(lvo.getLeaveReason()); break;
					case 11: cell.setCellValue(lvo.getOutAddress()); break;
					case 12: 
		    			cell.setCellValue(lvo.getApprovalUserName());
						break;
					case 13: 
						cell.setCellValue(DateUtil.getDateFormatString(lvo.getApprovalTime(),DateUtil.JAVA_DATE_FORMAT_YMDHM));	break;
					case 14: 
		    			cell.setCellValue(lvo.getApprovalContent());
						break;
					case 15: cell.setCellValue(DictUtils.getDictValue("Approval_Status", lvo.getApprovalStatus().toString()));	break;
				}
				cell.setCellStyle(mainStyle_center);
			}
			row.setHeightInPoints(realRowH);
		}

		return wb;
	}
	
	
	/*******************************************以下是请假统计********************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Pager queryLeaveStatistics(Integer page, Integer rows,LeaveVo leaveVo) {
		List<Object> listSql=getQueryStatisticsSql(leaveVo);
		String sql=(String)listSql.get(0);
		List<Object> paramList=(ArrayList)listSql.get(1);
	    Pager pager= leaveDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
	    if(pager!=null){
	    	List<LeaveVo> list = new ArrayList<LeaveVo>();
	    	for (int i = 0; i < pager.getPageList().size(); i++) {
	    		Object[] obj = (Object[])pager.getPageList().get(i);
	    		LeaveVo lVo = new LeaveVo();
	    		if(obj[0]!=null) lVo.setId(obj[0].toString());
	    		if(obj[1]!=null) lVo.setCreatorId(obj[1].toString());
	    		if(obj[2]!=null) lVo.setCreatorName(obj[2].toString());
	    		if(obj[3]!=null) lVo.setCreatorJobNumber(obj[3].toString());
	    		if(obj[4]!=null) lVo.setCreatorOrgName(obj[4].toString());
	    		lVo.setStartTime(leaveVo.getRecordDateStart());
	    		lVo.setEndTime(leaveVo.getRecordDateEnd());
	    		list.add(lVo);
	    	}
	    	pager.setPageList(list);
	    }
	    return pager;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<LeaveVo> queryStatisticsList(LeaveVo leaveVo) {
		List<Object> listSql=getQueryStatisticsSql(leaveVo);
		String sql=(String)listSql.get(0);
		List<Object> paramList=(ArrayList)listSql.get(1);
		
		List<Object> objList= leaveDaoImpl.queryEntitySQLList(sql.toString(), paramList);
		
		List<LeaveVo> list = new ArrayList<LeaveVo>();
		
		if(objList!=null){
	    	for (int i = 0; i < objList.size(); i++) {
	    		Object[] obj = (Object[])objList.get(i);
	    		LeaveVo lVo = new LeaveVo();
	    		if(obj[0]!=null) lVo.setId(obj[0].toString());
	    		if(obj[1]!=null) lVo.setCreatorId(obj[1].toString());
	    		if(obj[2]!=null) lVo.setCreatorName(obj[2].toString());
	    		if(obj[3]!=null) lVo.setCreatorJobNumber(obj[3].toString());
	    		if(obj[4]!=null) lVo.setCreatorOrgName(obj[4].toString());
	    		lVo.setRecordDateStart(leaveVo.getRecordDateStart());
	    		lVo.setRecordDateEnd(leaveVo.getRecordDateEnd());
	    		list.add(lVo);
	    	}
	    }
		return list;
	}
	
	private List<Object> getQueryStatisticsSql(LeaveVo leaveVo){
		List<Object> list=new ArrayList<>();
		
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(AttendanceQl.MySql.leaveStatistics);
		if(leaveVo.getRecordDateStart()!=null){
			sql.append(" and t.CREATE_TIME >= ? ");		
			paramList.add(leaveVo.getRecordDateStart());
		}
		if(leaveVo.getRecordDateEnd()!=null){
			sql.append(" and t.CREATE_TIME <= ? ");		
			paramList.add(DateUtil.getDateEndTime(leaveVo.getRecordDateEnd()));
		}
		if(StringUtils.isNotBlank(leaveVo.getCreatorOrgId())){
			sql.append(" and o.ID = ? ");		
			paramList.add(leaveVo.getCreatorOrgId());
		}
		if(StringUtils.isNotBlank(leaveVo.getCreatorName())){
			sql.append(" and u.USER_NAME like ? ");		
			paramList.add("%"+leaveVo.getCreatorName()+"%");
		}
		if(StringUtils.isNotBlank(leaveVo.getCreatorJobNumber())){
			sql.append(" and u.JOB_NUMBER = ? ");		
			paramList.add(leaveVo.getCreatorJobNumber());
		}
		sql.append(" GROUP BY t.CREATOR_ID ORDER BY u.JOB_NUMBER");
		
		list.add(sql.toString());
		list.add(paramList);
		
		return list;
	}

	@Override
	public HSSFWorkbook exportLeaveStatistics(LeaveVo leaveVo) {
		//创建HSSFWorkbook
		HSSFWorkbook wb = new HSSFWorkbook();

		//单元格 基础样式
		HSSFCellStyle mainStyle = wb.createCellStyle();
		mainStyle.setBorderBottom(BorderStyle.THIN); 	//下边框
		mainStyle.setBorderLeft(BorderStyle.THIN);		//左边框
		mainStyle.setBorderTop(BorderStyle.THIN);		//上边框
		mainStyle.setBorderRight(BorderStyle.THIN);		//右边框
		mainStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
		mainStyle.setWrapText(true);					//设置自动换行

		//基础样式 水平居中
		HSSFCellStyle mainStyle_center = wb.createCellStyle();
		mainStyle_center.cloneStyleFrom(mainStyle);
		mainStyle_center.setAlignment(HorizontalAlignment.CENTER);
		//基础样式 水平靠左
		HSSFCellStyle mainStyle_left = wb.createCellStyle();
		mainStyle_left.cloneStyleFrom(mainStyle);
		mainStyle_left.setAlignment(HorizontalAlignment.LEFT);


		//设置第三行样式(普通列标题样式)
		HSSFCellStyle r2_style = wb.createCellStyle();
		r2_style.cloneStyleFrom(mainStyle_center);	//基础样式 水平居中
		HSSFFont r2_font = wb.createFont();
		r2_font.setBold(true);						//字体加粗
		r2_style.setFont(r2_font);

		List<LeaveVo> rwList = this.queryStatisticsList(leaveVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("请假统计汇总");

		//列宽自适应（该方法在老版本的POI中效果不佳）
		/*sheet.autoSizeColumn(i);*/
		//设置列宽
		for (int i = 0; i < 7; i++) {
			if(i >=1){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5/2);
			}
		}


		//第一行
		HSSFRow row3 = sheet.createRow(0);
		row3.setHeightInPoints(30);		//行高
		HSSFCell cell;
		String[] title2 = {"序号","申请人","申请人工号","申请人部门","统计时间自","统计时间至","请假次数"};
		for(int i=0;i<title2.length;i++){
			cell = row3.createCell(i);		//创建单元格
			cell.setCellValue(title2[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		//第二行 及 之后的行
		HSSFRow row;
		for (int i = 0; i < rwList.size(); i++) {
			row = sheet.createRow(i + 1);	//创建行
			float realRowH=30;
			LeaveVo lvo= rwList.get(i);
			for (int j = 0; j < title2.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(i+1);	break;
					case 1: cell.setCellValue(lvo.getCreatorName());	break;
					case 2: 
						cell.setCellValue(lvo.getCreatorJobNumber());	break;
					case 3: 
						cell.setCellValue(lvo.getCreatorOrgName());	break;
					case 4: 
						cell.setCellValue(DateUtil.getDateFormatString(lvo.getStartTime(),DateUtil.JAVA_DATE_FORMAT_YMD));	break;
					case 5: 
						cell.setCellValue(DateUtil.getDateFormatString(lvo.getEndTime(),DateUtil.JAVA_DATE_FORMAT_YMD));	break;
					case 6: cell.setCellValue(lvo.getId()); break;
				}
				cell.setCellStyle(mainStyle_center);
			}
			row.setHeightInPoints(realRowH);
		}

		return wb;
	}

	

}
