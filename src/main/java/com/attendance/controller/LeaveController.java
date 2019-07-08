package com.attendance.controller;


import java.util.ArrayList;
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
import com.attendance.module.Leave;
import com.attendance.service.IApprovalService;
import com.attendance.service.ILeaveService;
import com.attendance.vo.AttendanceApprovalVo;
import com.attendance.vo.LeaveVo;
import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.message.service.IMessageService;
import com.common.utils.Common;
import com.common.utils.MathUtil;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.common.utils.tld.DictUtils;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年5月20日
 *
 */
@Controller
@RequestMapping(value="/leave")
public class LeaveController extends BaseController{
	
	@Autowired
	private ILeaveService leaveServiceImpl;
	@Autowired
	private IApprovalService approvalServiceImpl;
	@Autowired
	private IUserService userServiceImpl;
	@Autowired
	private IOrgFrameService orgFrameServiceImpl;
	@Autowired
	public IMessageService messageServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	
	
	@RequestMapping(value="/leave_count")
	public void leaveCount(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			User user=this.getSessionUser();
			int count= leaveServiceImpl.queryCountInMonth(user.getId());
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
	 * @方法：@param leaveVo
	 * @描述：提交请假申请
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月2日
	 */
	@Transactional
	@RequestMapping(value = "/submit_leave")
	public void submitLeave(HttpServletRequest request,HttpServletResponse response, LeaveVo leaveVo) {
		JSONObject json = new JSONObject();
		json.put("result", false);
		try {
			String Num=MathUtil.randomTwoNumber();
			String approvalTypeKey="2";//请假
			leaveVo.setApprovalNumber(DateUtil.getYYMMDDHHMMSS2(approvalTypeKey+Num));
			leaveVo.setUserId(this.getSessionUser().getId());
			if(StringUtils.isNotBlank(leaveVo.getStartTimeStr())){
				leaveVo.setStartTime(DateUtil.format(leaveVo.getStartTimeStr(), "yyyy-MM-dd HH:mm:ss"));
			}
			if(StringUtils.isNotBlank(leaveVo.getStartTimeStr())){
				leaveVo.setEndTime(DateUtil.format(leaveVo.getEndTimeStr(), "yyyy-MM-dd HH:mm:ss"));
			}
			leaveVo.setApprovalStatus(0);
			Leave leave=new Leave();
			BeanUtils.copyProperties(leaveVo, leave);
			this.leaveServiceImpl.saveOrUpdate(leave);
			
			//查询此人当月已请假次数，如2次内（含2次）由值班站长审批，2次以上由站长审批
			String approvalUserId="";
			int countMonth= this.leaveServiceImpl.queryCountInMonth(this.getSessionUser().getId());
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
				json.put("id", leave.getId());
			}else{
				if(approvalUserId.indexOf(",")>=0){
					//角色包括多个人，如有当前部门有两个值班站长
					String[] ids= approvalUserId.split(",");
					//默认使用第一个id
					approvalUserId=ids[0];
				}
				//同时新增一条空的审批记录，记录审批人ID
				AttendanceApproval attendanceApproval=new AttendanceApproval();
				attendanceApproval.setApprovalRecordId(leave.getId());
				attendanceApproval.setApprovalType(2);//请假类型
				attendanceApproval.setApprovalUserId(approvalUserId);
				approvalServiceImpl.saveOrUpdate(attendanceApproval);
				json.put("result", true);
				json.put("msg", "");
				json.put("id", leave.getId());
				
				/********发送通知 start*********/
				String noticeTitle=Common.msgTitle_KQ_qj;
				String content="收到一条请假申请";
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
	
	@RequestMapping(value = "/confirm_leave")
	public void confirmLeave(HttpServletRequest request,HttpServletResponse response, LeaveVo leaveVo) {
		JSONObject json = new JSONObject();
		try {
			Leave leave=this.leaveServiceImpl.getEntityById(Leave.class, leaveVo.getId());
			if(leave!=null){
				//取请假类型
				StringBuffer leaveTypeValue=new StringBuffer();
				String leaveType=leave.getLeaveType();
				if(leaveType.indexOf(",")>0){
					String[] typeKeys=leaveType.split(",");
					for (int i = 0; i < typeKeys.length; i++) {
						String typeVal=DictUtils.getDictValue("Leave_Type", typeKeys[i]);
						leaveTypeValue.append(typeVal);
						leaveTypeValue.append(",");
					}
					leaveTypeValue=leaveTypeValue.delete(leaveTypeValue.length()-1, leaveTypeValue.length());
					leave.setLeaveType(leaveTypeValue.toString());
				}else{
					String typeVal=DictUtils.getDictValue("Leave_Type", leaveType);
					leave.setLeaveType(typeVal);
				}
				
				BeanUtils.copyProperties(leave, leaveVo);
				User createUser= userServiceImpl.getEntityById(User.class, leave.getCreatorId());
				OrgFrame org=this.orgFrameServiceImpl.getEntityById(OrgFrame.class, createUser.getOrgFrame().getId());
				//获取申请人部门
				if(org!=null){
					leaveVo.setCreatorOrgName(org.getOrgFrameName());
					leaveVo.setCreatorOrgId(org.getId());
				}
				//获取申请人头像
				List<Attach> listAttach = this.attachServiceImpl.queryAttchByFormIdAndOnlyPicture(createUser.getId());
				if(listAttach!=null){
					if(listAttach.size()>0){
						leaveVo.setCreatorAvatar(listAttach.get(0).getPathUpload());
					}
				}
				//取审批流程记录
				AttendanceApprovalVo avo=new AttendanceApprovalVo();
				avo.setApprovalRecordId(leaveVo.getId());
				List<AttendanceApproval> list= approvalServiceImpl.queryAttendanceApproval(avo);
				if(list!=null){
					String approvalUserId =list.get(0).getApprovalUserId();//取第一条记录
					User approvalUser= userServiceImpl.getEntityById(User.class, approvalUserId);
					leaveVo.setApprovalUserId(approvalUserId);
					leaveVo.setApprovalUserName(approvalUser.getUserName());
					leaveVo.setReadTime(list.get(0).getReadTime());
					leaveVo.setApprovalTime(list.get(0).getApprovalTime());
					leaveVo.setApprovalContent(list.get(0).getApprovalContent());
					List<Attach> attachList = attachServiceImpl.queryAttchListByFormId(approvalUserId);
					String avatarUrl = null;
					if(attachList!=null){
						for (int i = 0; i < attachList.size(); i++) {
							avatarUrl = attachList.get(0).getPathUpload();
						}
					}
					leaveVo.setApprovalUserAvatar(avatarUrl);
					
					JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
					config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
					String[] excludes = new String[] {}; // 列表排除信息内容字段，减少传递时间
					config.setExcludes(excludes);
					json.put("content", JSONObject.fromObject(leaveVo, config));
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
	 * @方法：@param leaveVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：请假列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月4日
	 */
	@RequestMapping(value="/leave_list")
	public void list(HttpServletRequest request,HttpServletResponse response,LeaveVo leaveVo,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			Pager pager=null;
			if(StringUtils.isBlank(leaveVo.getApprovalUserId())){
				//查看
				pager = this.leaveServiceImpl.queryEntityList(page, rows, leaveVo);
			}else{
				pager = this.leaveServiceImpl.queryLeaveApproval(page, rows, leaveVo);
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
	 * @方法：@param leaveVo
	 * @描述：审批请假申请
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月5日
	 */
	@RequestMapping(value="/leave_approval")
	public void approval(HttpServletRequest request,HttpServletResponse response,AttendanceApprovalVo attendanceApprovalVo){
		JSONObject json = new JSONObject();
		json.put("result", false);
		try {
			Leave leave=this.leaveServiceImpl.getEntityById(Leave.class, attendanceApprovalVo.getApprovalRecordId());
			if(leave!=null){
				if(leave.getApprovalStatus()==2){
					json.put("msg","已经被审批通过");
				}else{
					if(attendanceApprovalVo.getApprovalResult()==1){
						//同意
						leave.setApprovalStatus(2);
					}else if(attendanceApprovalVo.getApprovalResult()==0){
						//不同意
						leave.setApprovalStatus(3);
					}
					this.leaveServiceImpl.saveOrUpdate(leave);
					
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
							String noticeTitle=Common.msgTitle_KQ_qj;
							String content="收到一条请假审批回复通知";
							String userIds=leave.getCreatorId();
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
