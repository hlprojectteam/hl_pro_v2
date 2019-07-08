package com.attendance.controller;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.o.common.beans.BeanUtils;

import com.attendance.module.AttendanceApproval;
import com.attendance.module.ChangeShifts;
import com.attendance.service.IApprovalService;
import com.attendance.service.IChangeShiftsService;
import com.attendance.vo.AttendanceApprovalVo;
import com.attendance.vo.ChangeShiftsVo;
import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.message.service.IMessageService;
import com.common.utils.Common;
import com.common.utils.MathUtil;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;

/**
 * 
 * @Description 换班
 * @author qinyongqian
 * @date 2019年5月20日
 *
 */
@Controller
@RequestMapping(value="/changeShifts")
public class ChangeShiftsController extends BaseController{
	
	@Autowired
	private IChangeShiftsService changeShiftsService;
	@Autowired
	private IApprovalService approvalServiceImpl;
	@Autowired
	private IUserService userServiceImpl;
	@Autowired
	public IMessageService messageServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	private IOrgFrameService orgFrameServiceImpl;
	
	
	@RequestMapping(value="/changeShift_count")
	public void changeShiftCount(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			User user=this.getSessionUser();
			int count= changeShiftsService.queryCountInMonth(user.getId());
			json.put("val", count);
			json.put("result",true);
		} catch (Exception e) {
			json.put("result",false);
			json.put("msg",e.getMessage());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param changeShiftsVo
	 * @描述：提交请假申请
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月2日
	 */
	@Transactional
	@RequestMapping(value = "/submit_")
	public void submit(HttpServletRequest request,HttpServletResponse response, ChangeShiftsVo changeShiftsVo) {
		JSONObject json = new JSONObject();
		json.put("result", false);
		try {
			String Num=MathUtil.randomTwoNumber();
			String approvalTypeKey="1";//调班
			changeShiftsVo.setApprovalNumber(DateUtil.getYYMMDDHHMMSS2(approvalTypeKey+Num));
			changeShiftsVo.setApplyMenId(this.getSessionUser().getId());
			if(StringUtils.isNotBlank(changeShiftsVo.getChangeDateStr())){
				changeShiftsVo.setChangeDate(DateUtil.format(changeShiftsVo.getChangeDateStr(),DateUtil.JAVA_DATE_FORMAT_YMD));
			}
			changeShiftsVo.setApprovalStatus(0);
			ChangeShifts changeShifts=new ChangeShifts();
			BeanUtils.copyProperties(changeShiftsVo, changeShifts);
			this.changeShiftsService.saveOrUpdate(changeShifts);
			
			//查询此人当月已调班次数，如2次内（含2次）由值班站长审批，2次以上由站长审批
			String approvalUserId="";
			int countMonth= this.changeShiftsService.queryCountInMonth(this.getSessionUser().getId());
			if(countMonth>2){
				//2次以上由站长
				approvalUserId= userServiceImpl.findUserIdsByUserIdAndRoleCode(this.getSessionUser().getId(), "Stationmaster");
			}else{
				//2次内由值班站长
				approvalUserId= userServiceImpl.findUserIdsByUserIdAndRoleCode(this.getSessionUser().getId(), "station_agent");
			}
			if(StringUtils.isEmpty(approvalUserId)){
				//找不到审批人
				json.put("result", false);
				json.put("msg", "找不到下一审批人");
				json.put("id", changeShifts.getId());
			}else{
				if(approvalUserId.indexOf(",")>=0){
					//角色包括多个人，如有当前部门有两个值班站长
					String[] ids= approvalUserId.split(",");
					//默认使用第一个id
					approvalUserId=ids[0];
				}
				//同时新增一条空的审批记录，记录审批人ID
				AttendanceApproval attendanceApproval=new AttendanceApproval();
				attendanceApproval.setApprovalRecordId(changeShifts.getId());
				attendanceApproval.setApprovalType(1);//调班类型
				attendanceApproval.setApprovalUserId(approvalUserId);
				approvalServiceImpl.saveOrUpdate(attendanceApproval);
				json.put("result", true);
				json.put("msg", "");
				json.put("id", changeShifts.getId());
				
				/********发送通知 start*********/
				String noticeTitle=Common.msgTitle_KQ_db;
				String content="收到一条调班申请";
				String userIds=approvalUserId;
				String roleCodes="";
				int msgType=Common.msgKQ;
				User nowPerson=this.getSessionUser();
				this.messageServiceImpl.submitSendMsg(noticeTitle,content,userIds,roleCodes,msgType,nowPerson);
				
				/********发送通知 end*********/
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("result", false);
			json.put("msg", e.toString());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param changeShiftsVo
	 * @描述：明细，确认
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月5日
	 */
	@RequestMapping(value = "/confirm_")
	public void confirm(HttpServletRequest request,HttpServletResponse response, ChangeShiftsVo changeShiftsVo) {
		JSONObject json = new JSONObject();
		try {
			ChangeShifts changeShifts=this.changeShiftsService.getEntityById(ChangeShifts.class, changeShiftsVo.getId());
			if(changeShifts!=null){
				BeanUtils.copyProperties(changeShifts, changeShiftsVo);
				User createUser= userServiceImpl.getEntityById(User.class, changeShifts.getCreatorId());
				OrgFrame org=this.orgFrameServiceImpl.getEntityById(OrgFrame.class, createUser.getOrgFrame().getId());
				//获取申请人部门
				if(org!=null){
					changeShiftsVo.setCreatorOrgName(org.getOrgFrameName());
					changeShiftsVo.setCreatorOrgId(org.getId());
				}
				//获取申请人头像
				List<Attach> listAttach = this.attachServiceImpl.queryAttchByFormIdAndOnlyPicture(createUser.getId());
				if(listAttach!=null){
					if(listAttach.size()>0){
						changeShiftsVo.setCreatorAvatar(listAttach.get(0).getPathUpload());
					}
				}
				//取审批流程记录
				AttendanceApprovalVo avo=new AttendanceApprovalVo();
				avo.setApprovalRecordId(changeShiftsVo.getId());
				List<AttendanceApproval> list= approvalServiceImpl.queryAttendanceApproval(avo);
				if(list!=null){
					String approvalUserId =list.get(0).getApprovalUserId();//取第一条记录
					User approvalUser= userServiceImpl.getEntityById(User.class, approvalUserId);
					changeShiftsVo.setApprovalUserId(approvalUserId);
					changeShiftsVo.setApprovalUserName(approvalUser.getUserName());
					changeShiftsVo.setReadTime(list.get(0).getReadTime());
					changeShiftsVo.setApprovalTime(list.get(0).getApprovalTime());
					changeShiftsVo.setApprovalContent(list.get(0).getApprovalContent());
					List<Attach> attachList = attachServiceImpl.queryAttchListByFormId(approvalUserId);
					String avatarUrl = null;
					if(attachList!=null){
						for (int i = 0; i < attachList.size(); i++) {
							avatarUrl = attachList.get(0).getPathUpload();
						}
					}
					changeShiftsVo.setApprovalUserAvatar(avatarUrl);
					
					JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
					config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
					String[] excludes = new String[] {}; // 列表排除信息内容字段，减少传递时间
					config.setExcludes(excludes);
					json.put("content", JSONObject.fromObject(changeShiftsVo, config));
					json.put("result", true);
				}
			}else{
				json.put("result", false);
				json.put("msg", "申请记录为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("result", false);
			json.put("msg", e.toString());
		}finally{
			this.print(json);
		}
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param changeShiftsVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月5日
	 */
	@RequestMapping(value="/changeShifts_list")
	public void list(HttpServletRequest request,HttpServletResponse response,ChangeShiftsVo changeShiftsVo,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			Pager pager=null;
			if(StringUtils.isBlank(changeShiftsVo.getApprovalUserId())){
				//查看
				pager = this.changeShiftsService.queryEntityList(page, rows, changeShiftsVo);
			}else{
				pager = this.changeShiftsService.queryChangeShiftsApproval(page, rows, changeShiftsVo);
			}
			if(page!=null){
				json.put("total", pager.getRowCount());
				json.put("curPageSize", pager.getPageList().size());
				JsonConfig config = new JsonConfig();
				String[] excludes = new String[] {"creatorId","sysCode"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
				json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
				json.put("result",true);
			}
		} catch (Exception e) {
			json.put("result",false);
			json.put("msg",e.getMessage());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param attendanceApprovalVo
	 * @描述：调班审批
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月5日
	 */
	@RequestMapping(value="/changeShifts_approval")
	public void approval(HttpServletRequest request,HttpServletResponse response,AttendanceApprovalVo attendanceApprovalVo){
		JSONObject json = new JSONObject();
		json.put("result", false);
		try {
			ChangeShifts changeShifts=this.changeShiftsService.getEntityById(ChangeShifts.class, attendanceApprovalVo.getApprovalRecordId());
			if(changeShifts!=null){
				if(changeShifts.getApprovalStatus()==2){
					json.put("msg","已经被审批通过");
				}else{
					if(attendanceApprovalVo.getApprovalResult()==1){
						//同意
						changeShifts.setApprovalStatus(2);
					}else if(attendanceApprovalVo.getApprovalResult()==0){
						//不同意
						changeShifts.setApprovalStatus(3);
					}
					this.changeShiftsService.saveOrUpdate(changeShifts);
					
					List<AttendanceApproval> listAA= approvalServiceImpl.queryAttendanceApproval(attendanceApprovalVo);
					if(listAA!=null){
						if(listAA.size()>0){
							AttendanceApproval aa=listAA.get(0);
							aa.setApprovalContent(attendanceApprovalVo.getApprovalContent());
							aa.setApprovalTime(new Date());
							aa.setApprovalResult(attendanceApprovalVo.getApprovalResult());
							this.approvalServiceImpl.saveOrUpdate(aa);
							json.put("result",true);
							
							/********发送通知 start*********/
							String noticeTitle=Common.msgTitle_KQ_db;
							String content="收到一条调班审批回复通知";
							String userIds=changeShifts.getCreatorId();
							String roleCodes="";
							int msgType=Common.msgKQ;
							User nowPerson=this.getSessionUser();
							this.messageServiceImpl.submitSendMsg(noticeTitle,content,userIds,roleCodes,msgType,nowPerson);
							/********发送通知 end*********/
						}else{
							json.put("msg","找不到审批记录");
						}
					}else{
						json.put("msg","找不到审批记录");
					}
				}
			}else{
				json.put("msg","找不到申请记录");
			}
		} catch (Exception e) {
			json.put("result",false);
			json.put("msg",e.getMessage());
		}finally{
			this.print(json.toString());
		}
	}
	
	/******************************私有方法****************************************/
	
}
