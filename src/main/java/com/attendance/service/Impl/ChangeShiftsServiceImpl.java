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

import com.attendance.dao.IChangeShiftsDao;
import com.attendance.module.ChangeShifts;
import com.attendance.ql.AttendanceQl;
import com.attendance.service.IChangeShiftsService;
import com.attendance.vo.ChangeShiftsVo;
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
@Service("changeShiftsServiceImpl")
public class ChangeShiftsServiceImpl extends BaseServiceImpl implements IChangeShiftsService{

	@Autowired
	public IChangeShiftsDao changeShiftsDaoImpl;
	
	@Override
	public Pager queryEntityList(Integer page, Integer rows,
			ChangeShiftsVo changeShiftsVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(changeShiftsVo.getCreatorId())){
			criterionsList.add(Restrictions.eq("creatorId",  changeShiftsVo.getCreatorId()));
		}
		if(changeShiftsVo.getApprovalStatus()!=null){
			criterionsList.add(Restrictions.eq("approvalStatus",  changeShiftsVo.getApprovalStatus()));
		}
		return this.changeShiftsDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime"), ChangeShifts.class);
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
	    			csVo.setApplyMenId(applyMenId);
	    			User applyMen= changeShiftsDaoImpl.getEntityById(User.class, applyMenId);
	    			if(applyMen!=null){
	    				csVo.setApplyMenName(applyMen.getUserName());
	    			}
	    		} 
	    		if(obj[6]!=null){
	    			String beApplyMenId=obj[6].toString();
	    			csVo.setBeApplyMenId(beApplyMenId);
	    			User beApplyMen= changeShiftsDaoImpl.getEntityById(User.class, beApplyMenId);
	    			if(beApplyMen!=null){
	    				csVo.setBeApplyMenName(beApplyMen.getUserName());
	    			}
	    		} 
	    		if(obj[7]!=null) csVo.setApplyMenShift(obj[7].toString());
	    		if(obj[8]!=null) csVo.setBeApplyMenShift(obj[8].toString());
	    		if(obj[9]!=null) {
	    			try {
						csVo.setChangeDate(DateUtil.format(obj[9].toString(), DateUtil.JAVA_DATE_FORMAT_YMD));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    		if(obj[10]!=null) csVo.setChangeType(obj[10].toString());
	    		list.add(csVo);
	    	}
	    	pager.setPageList(list);
	    }
	    return pager;
	}

	/*******************************************以下是调班记录********************************************************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Pager queryChangeShiftsRecord(Integer page, Integer rows,
			ChangeShiftsVo changeShiftsVo) {
		List<Object> listSql=getQueryRecordSql(changeShiftsVo);
		String sql=(String)listSql.get(0);
		List<Object> paramList=(ArrayList)listSql.get(1);
	    Pager pager= changeShiftsDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
	    if(pager!=null){
	    	List<ChangeShiftsVo> list = new ArrayList<ChangeShiftsVo>();
	    	for (int i = 0; i < pager.getPageList().size(); i++) {
	    		Object[] obj = (Object[])pager.getPageList().get(i);
	    		ChangeShiftsVo csVo = new ChangeShiftsVo();
	    		if(obj[0]!=null) csVo.setId(obj[0].toString());
	    		if(obj[1]!=null) csVo.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
	    		if(obj[2]!=null) csVo.setApprovalNumber(obj[2].toString());
	    		if(obj[3]!=null){
	    			try {
	    				csVo.setChangeDate(DateUtil.getDateFromString(obj[3].toString()));
	    				csVo.setChangeDateStr(
	    						DateUtil.getDateFormatString(DateUtil.getDateFromString(obj[3].toString()),
	    								DateUtil.JAVA_DATE_FORMAT_YMD));
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	    		}
	    		if(obj[4]!=null) csVo.setCreatorOrgName(obj[4].toString());
	    		if(obj[5]!=null) csVo.setCreatorName(obj[5].toString());
	    		if(obj[6]!=null) csVo.setCreatorJobNumber(obj[6].toString());
	    		if(obj[7]!=null) csVo.setApplyMenShift(obj[7].toString());
	    		if(obj[8]!=null){
	    			String beApplyMenId=obj[8].toString();
	    			User beApplyMen=this.baseDaoImpl.getEntityById(User.class, beApplyMenId);
	    			csVo.setBeApplyMenId(obj[8].toString());
	    			if(beApplyMen!=null){
	    				csVo.setBeApplyMenName(beApplyMen.getUserName());
	    				csVo.setBeApplyMenJobNumber(beApplyMen.getJobNumber());
	    			}
	    		}
	    		if(obj[9]!=null) csVo.setBeApplyMenShift(obj[9].toString());
	    		if(obj[10]!=null) csVo.setChangeType(obj[10].toString());
	    		if(obj[11]!=null) csVo.setApprovalStatus(Integer.parseInt(obj[11].toString()));
	    		if(obj[12]!=null){
	    			String approvalUserId=obj[12].toString();
	    			User approvalUser=this.baseDaoImpl.getEntityById(User.class, approvalUserId);
	    			csVo.setApprovalUserId(obj[12].toString());
	    			if(approvalUser!=null){
	    				csVo.setApprovalUserName(approvalUser.getUserName());
	    			}
	    		}
	    		if(obj[13]!=null) csVo.setChangeReason(obj[13].toString());
	    		if(obj[14]!=null) csVo.setApprovalTime(DateUtil.getDateFromString(obj[14].toString()));
	    		if(obj[15]!=null) csVo.setApprovalContent(obj[15].toString());
	    		
	    		list.add(csVo);
	    	}
	    	pager.setPageList(list);
	    }
	    return pager; 
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<ChangeShiftsVo> queryRecordList(ChangeShiftsVo changeShiftsVo) {
		
		List<Object> listSql=getQueryRecordSql(changeShiftsVo);
		String sql=(String)listSql.get(0);
		List<Object> paramList=(ArrayList)listSql.get(1);
		List<Object> objList= changeShiftsDaoImpl.queryEntitySQLList(sql.toString(), paramList);
		List<ChangeShiftsVo> list = new ArrayList<ChangeShiftsVo>();
		if(objList!=null){
	    	for (int i = 0; i < objList.size(); i++) {
	    		Object[] obj = (Object[])objList.get(i);
	    		ChangeShiftsVo csVo = new ChangeShiftsVo();
	    		if(obj[0]!=null) csVo.setId(obj[0].toString());
	    		if(obj[1]!=null) csVo.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
	    		if(obj[2]!=null) csVo.setApprovalNumber(obj[2].toString());
	    		if(obj[3]!=null){
	    			try {
	    				csVo.setChangeDate(DateUtil.format(obj[3].toString(),DateUtil.JAVA_DATE_FORMAT_YMD));
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	    		}
	    		if(obj[4]!=null) csVo.setCreatorOrgName(obj[4].toString());
	    		if(obj[5]!=null) csVo.setCreatorName(obj[5].toString());
	    		if(obj[6]!=null) csVo.setCreatorJobNumber(obj[6].toString());
	    		if(obj[7]!=null) csVo.setApplyMenShift(obj[7].toString());
	    		if(obj[8]!=null){
	    			String beApplyMenId=obj[8].toString();
	    			User beApplyMen=this.baseDaoImpl.getEntityById(User.class, beApplyMenId);
	    			csVo.setBeApplyMenId(obj[8].toString());
	    			if(beApplyMen!=null){
	    				csVo.setBeApplyMenName(beApplyMen.getUserName());
	    				csVo.setBeApplyMenJobNumber(beApplyMen.getJobNumber());
	    			}
	    		}
	    		if(obj[9]!=null) csVo.setBeApplyMenShift(obj[9].toString());
	    		if(obj[10]!=null) csVo.setChangeType(obj[10].toString());
	    		if(obj[11]!=null) csVo.setApprovalStatus(Integer.parseInt(obj[11].toString()));
	    		if(obj[12]!=null){
	    			String approvalUserId=obj[12].toString();
	    			User approvalUser=this.baseDaoImpl.getEntityById(User.class, approvalUserId);
	    			csVo.setApprovalUserId(obj[12].toString());
	    			if(approvalUser!=null){
	    				csVo.setApprovalUserName(approvalUser.getUserName());
	    			}
	    		}
	    		if(obj[13]!=null) csVo.setChangeReason(obj[13].toString());
	    		if(obj[14]!=null) csVo.setApprovalTime(DateUtil.getDateFromString(obj[14].toString()));
	    		if(obj[15]!=null) csVo.setApprovalContent(obj[15].toString());
	    		list.add(csVo);
	    	}
	    }
		return list;
	}
	
	/**
	 * 
	 * @方法：@param changeShiftsVo
	 * @方法：@return
	 * @描述：查询条件
	 * @return
	 * @author: qinyongqian
	 * @date:2019年8月29日
	 */
	private List<Object> getQueryRecordSql(ChangeShiftsVo changeShiftsVo){
		List<Object> list=new ArrayList<>();
		
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(AttendanceQl.MySql.changeShiftsRecord);
		if(StringUtils.isNotBlank(changeShiftsVo.getCreatorName())){
			sql.append(" and s.CREATOR_NAME like ? ");		
			paramList.add("%"+changeShiftsVo.getCreatorName()+"%");
		}
		if(StringUtils.isNotBlank(changeShiftsVo.getCreatorJobNumber())){
			sql.append(" and u.JOB_NUMBER = ? ");		
			paramList.add(changeShiftsVo.getCreatorJobNumber());
		}
		if(changeShiftsVo.getApprovalStatus()!=null){
			sql.append(" and s.APPROVAL_STATUS = ? ");		
			paramList.add(changeShiftsVo.getApprovalStatus());
		}
		if(changeShiftsVo.getRecordDateStart()!=null){
			sql.append(" and s.CREATE_TIME >= ? ");		
			paramList.add(changeShiftsVo.getRecordDateStart());
		}
		if(changeShiftsVo.getRecordDateEnd()!=null){
			sql.append(" and s.CREATE_TIME <= ? ");		
			paramList.add(DateUtil.getDateEndTime(changeShiftsVo.getRecordDateEnd()));
		}
		if(StringUtils.isNotBlank(changeShiftsVo.getCreatorOrgId())){
			sql.append(" and o.ID = ? ");		
			paramList.add(changeShiftsVo.getCreatorOrgId());
		}
		sql.append(" ORDER BY s.CREATE_TIME DESC");
		
		list.add(sql.toString());
		list.add(paramList);
		
		return list;
	}
	

	@Override
	public HSSFWorkbook exportChangeShiftsRecord(ChangeShiftsVo changeShiftsVo) {
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

		List<ChangeShiftsVo> rwList = this.queryRecordList(changeShiftsVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("调班记录汇总");

		//列宽自适应（该方法在老版本的POI中效果不佳）
		/*sheet.autoSizeColumn(i);*/
		//设置列宽
		for (int i = 0; i < 17; i++) {
			if(i >=1){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5/2);
			}
		}

		//第一行
		HSSFRow row3 = sheet.createRow(0);
		row3.setHeightInPoints(30);		//行高
		HSSFCell cell;
		String[] title2 = {"序号","审批编号","申请时间","调班日期","申请部门","申请人","申请人工号","申请人班次","申请理由","被调人","被调人工号",
				"被调人班次","调班方式","审批人","审批时间","审批内容","审批状态"};
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
			ChangeShiftsVo csVo= rwList.get(i);
			for (int j = 0; j < title2.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(i+1);	break;
					case 1: cell.setCellValue(csVo.getApprovalNumber());	break;
					case 2: cell.setCellValue(DateUtil.getDateFormatString(csVo.getCreateTime(),DateUtil.JAVA_DATE_FORMAT_YMDHM));	break;
					case 3: cell.setCellValue(DateUtil.getDateFormatString(csVo.getChangeDate(),DateUtil.JAVA_DATE_FORMAT_YMD));	break;
					case 4: 
						cell.setCellValue(csVo.getCreatorOrgName());	break;
					case 5: cell.setCellValue(csVo.getCreatorName());	break;
					case 6: 
						cell.setCellValue(csVo.getCreatorJobNumber());	break;
					case 7: 
						cell.setCellValue(csVo.getApplyMenShift());	break;
					case 8: 
						cell.setCellValue(csVo.getChangeReason());	break;
					case 9: cell.setCellValue(csVo.getBeApplyMenName());	break;
					case 10: 
						cell.setCellValue(csVo.getBeApplyMenJobNumber());	break;
					case 11: 
						cell.setCellValue(csVo.getBeApplyMenShift());	break;
					case 12: cell.setCellValue(DictUtils.getDictValue("changeShifts_Type", csVo.getChangeType().toString()));	break;
					case 13: 
		    			cell.setCellValue(csVo.getApprovalUserName());
						break;
					case 14: 
						cell.setCellValue(DateUtil.getDateFormatString(csVo.getApprovalTime(),DateUtil.JAVA_DATE_FORMAT_YMDHM));	break;
					case 15: 
		    			cell.setCellValue(csVo.getApprovalContent());
						break;
					case 16: cell.setCellValue(DictUtils.getDictValue("Approval_Status", csVo.getApprovalStatus().toString()));	break;
					
				}
				cell.setCellStyle(mainStyle_center);
			}
			row.setHeightInPoints(realRowH);
		}

		return wb;
	}

	/*******************************************以下是调班统计********************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Pager queryChangeShiftsStatistics(Integer page, Integer rows,
			ChangeShiftsVo changeShiftsVo) {
		List<Object> listSql=getQueryStatisticsSql(changeShiftsVo);
		String sql=(String)listSql.get(0);
		List<Object> paramList=(ArrayList)listSql.get(1);
		Pager pager= changeShiftsDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
	    if(pager!=null){
	    	List<ChangeShiftsVo> list = new ArrayList<ChangeShiftsVo>();
	    	for (int i = 0; i < pager.getPageList().size(); i++) {
	    		Object[] obj = (Object[])pager.getPageList().get(i);
	    		ChangeShiftsVo csVo = new ChangeShiftsVo();
	    		if(obj[0]!=null) csVo.setId(obj[0].toString());
	    		if(obj[1]!=null) csVo.setCreatorId(obj[1].toString());
	    		if(obj[2]!=null) csVo.setCreatorName(obj[2].toString());
	    		if(obj[3]!=null) csVo.setCreatorJobNumber(obj[3].toString());
	    		if(obj[4]!=null) csVo.setCreatorOrgName(obj[4].toString());
	    		csVo.setRecordDateStart(changeShiftsVo.getRecordDateStart());
	    		csVo.setRecordDateEnd(changeShiftsVo.getRecordDateEnd());
	    		list.add(csVo);
	    	}
	    	pager.setPageList(list);
	    }
	    return pager;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<ChangeShiftsVo> queryStatisticsList(ChangeShiftsVo changeShiftsVo) {
		List<Object> listSql=getQueryStatisticsSql(changeShiftsVo);
		String sql=(String)listSql.get(0);
		List<Object> paramList=(ArrayList)listSql.get(1);
		List<Object> objList= changeShiftsDaoImpl.queryEntitySQLList(sql.toString(), paramList);
		List<ChangeShiftsVo> list = new ArrayList<ChangeShiftsVo>();
		if(objList!=null){
	    	for (int i = 0; i < objList.size(); i++) {
	    		Object[] obj = (Object[])objList.get(i);
	    		ChangeShiftsVo csVo = new ChangeShiftsVo();
	    		if(obj[0]!=null) csVo.setId(obj[0].toString());
	    		if(obj[1]!=null) csVo.setCreatorId(obj[1].toString());
	    		if(obj[2]!=null) csVo.setCreatorName(obj[2].toString());
	    		if(obj[3]!=null) csVo.setCreatorJobNumber(obj[3].toString());
	    		if(obj[4]!=null) csVo.setCreatorOrgName(obj[4].toString());
	    		csVo.setRecordDateStart(changeShiftsVo.getRecordDateStart());
	    		csVo.setRecordDateEnd(changeShiftsVo.getRecordDateEnd());
	    		list.add(csVo);
	    	}
	    }
		return list;
	}
	
	/**
	 * 
	 * @方法：@param changeShiftsVo
	 * @方法：@return
	 * @描述：查询条件
	 * @return
	 * @author: qinyongqian
	 * @date:2019年8月29日
	 */
	private List<Object> getQueryStatisticsSql(ChangeShiftsVo changeShiftsVo){
		List<Object> list=new ArrayList<>();
		
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(AttendanceQl.MySql.changeShiftsStatistics);
		if(changeShiftsVo.getRecordDateStart()!=null){
			sql.append(" and t.CREATE_TIME >= ? ");		
			paramList.add(changeShiftsVo.getRecordDateStart());
		}
		if(changeShiftsVo.getRecordDateEnd()!=null){
			sql.append(" and t.CREATE_TIME <= ? ");		
			paramList.add(DateUtil.getDateEndTime(changeShiftsVo.getRecordDateEnd()));
		}
		if(StringUtils.isNotBlank(changeShiftsVo.getCreatorOrgId())){
			sql.append(" and o.ID = ? ");		
			paramList.add(changeShiftsVo.getCreatorOrgId());
		}
		if(StringUtils.isNotBlank(changeShiftsVo.getCreatorName())){
			sql.append(" and u.USER_NAME like ? ");		
			paramList.add("%"+changeShiftsVo.getCreatorName()+"%");
		}
		if(StringUtils.isNotBlank(changeShiftsVo.getCreatorJobNumber())){
			sql.append(" and u.JOB_NUMBER = ? ");		
			paramList.add(changeShiftsVo.getCreatorJobNumber());
		}
		sql.append(" GROUP BY t.CREATOR_ID ORDER BY u.JOB_NUMBER");
		
		list.add(sql.toString());
		list.add(paramList);
		
		return list;
	}

	@Override
	public HSSFWorkbook exportChangeShiftsStatistics(ChangeShiftsVo changeShiftsVo) {
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

		List<ChangeShiftsVo> rwList = this.queryStatisticsList(changeShiftsVo);

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
		String[] title2 = {"序号","申请人","申请人工号","申请人部门","统计时间自","统计时间至","调班次数"};
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
			ChangeShiftsVo csVo= rwList.get(i);
			for (int j = 0; j < title2.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(i+1);	break;
					case 1: cell.setCellValue(csVo.getCreatorName());	break;
					case 2: 
						cell.setCellValue(csVo.getCreatorJobNumber());	break;
					case 3: 
						cell.setCellValue(csVo.getCreatorOrgName());	break;
					case 4: 
						cell.setCellValue(DateUtil.getDateFormatString(csVo.getRecordDateStart(),DateUtil.JAVA_DATE_FORMAT_YMD));	break;
					case 5: 
						cell.setCellValue(DateUtil.getDateFormatString(csVo.getRecordDateEnd(),DateUtil.JAVA_DATE_FORMAT_YMD));	break;
					case 6: cell.setCellValue(csVo.getId()); break;
				}
				cell.setCellStyle(mainStyle_center);
			}
			row.setHeightInPoints(realRowH);
		}

		return wb;
	}

	

	

}
