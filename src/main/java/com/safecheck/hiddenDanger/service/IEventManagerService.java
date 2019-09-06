package com.safecheck.hiddenDanger.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.safecheck.hiddenDanger.module.EventHandle;
import com.safecheck.hiddenDanger.module.EventProcess;
import com.safecheck.hiddenDanger.vo.EventInfoVo;
import com.safecheck.hiddenDanger.vo.EventProcessVo;
import com.urms.user.module.User;

/**
 * @Description 事件管理service接口
 * @author xuezb
 * @Date 2018年5月23日
 */
public interface IEventManagerService extends IBaseService{

	/**
	 * 事件的保存提交
	 * @param eventInfoVo
	 * @return 事件id
	 * @author xuezb
	 * @Date 2018年5月23日
	 */
	String saveEvent(EventInfoVo eventInfoVo);
	
	/**
	 * 事件编码  组成规则:ET+年月日时分秒+当天的第几个事件(四位,如0001是当天的第一个事件)
	 * @param tableName
	 * @return
	 */
	String buildEventSerialNum(String tableName);
	
	
	
	/**
	 * 上报事件的列表展示		分页
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年5月23日
	 */
	public Pager queryReportList(Integer page, Integer rows, EventInfoVo eventInfoVo);
	
	/**
	 * 待办事件的列表展示		分页
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年5月23日
	 */
	public Pager queryAgendaList(Integer page, Integer rows, EventInfoVo eventInfoVo); 
	
	/**
	 * 经办事件的列表展示		分页
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年5月29日
	 */
	public Pager queryHandleList(Integer page, Integer rows, EventInfoVo eventInfoVo);
	
	/**
	 * 办结事件的列表展示		分页
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年5月29日
	 */
	public Pager queryFinishList(Integer page, Integer rows, EventInfoVo eventInfoVo);
	
	/**
	 * 所有事件的列表展示		分页
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年6月6日
	 */
	public Pager queryAllList(Integer page, Integer rows, EventInfoVo eventInfoVo);
	
	
	
	/**
	 * 事件过程处理状态的修改		(将过程状态由  1:未处理      改为      2:处理中   )
	 * @param epId	事件过程id
	 * @return
	 * @author xuezb
	 * @Date 2018年6月4日
	 */
	public String saveModifyProcessStatus(String epId);
	
	/**
	 * 各部门安全员	将事件上报给	   部门负责人		(2->3)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年月日
	 */
	public String saveReportEventToBMFZR(EventInfoVo eventInfoVo);
	
	/**
	 * 部门负责人	将事件上报给	   安保办安全员	(3->4)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年月日
	 */
	public String saveReportEventToABBAQY(EventInfoVo eventInfoVo);
	
	/**
	 * 安保办安全员	将事件上报给	   安保办主任		(4->5)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年月日
	 */
	public String saveReportEventToABBZR(EventInfoVo eventInfoVo);
	
	/**
	 * 安保办主任	将事件派遣给	  部门负责人		(5->6)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年月日
	 */
	public String saveDispatchEventToBMFZR(EventInfoVo eventInfoVo);
	
	/**
	 * 部门负责人	将事件派遣给	  处理人	(6->7)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年月日
	 */
	public String saveDispatchEventToDealPeople(EventInfoVo eventInfoVo);
	
	/**
	 * 处理人	将事件解决后提交给	安保办安全员   核查		(7->8)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年月日
	 */
	public String saveFeedBackEventToABBAQY(EventInfoVo eventInfoVo);
	
	/**
	 * 安保办主任	对此事件向上级	分管领导	请示	(5->10)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	public String saveGoUpstairsToFGLD(EventInfoVo eventInfoVo);
	
	/**
	 * 分管领导	对此事件向上级	常务副总经理		请示	(10->11)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	public String saveGoUpstairsToCWFZJL(EventInfoVo eventInfoVo);
	
	/**
	 * 常务副总经理		审批后将事件反馈给		分管领导	(11->10)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	public String saveFeedBackToFGLD(EventInfoVo eventInfoVo);
	
	/**
	 * 分管领导	审批后将事件反馈给		安保办主任	(10->5)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	public String saveFeedBackToABBZR(EventInfoVo eventInfoVo);
	
	/**
	 * 部门负责人	将事件退回给		安保办主任	(6->5)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	public String saveReturnToABBZR(EventInfoVo eventInfoVo);
	
	/**
	 * 处理人	将事件退回给		部门负责人	(7->6)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	public String saveReturnToBMFZR(EventInfoVo eventInfoVo);
	
	/**
	 * 安保办安全员	审核后觉得处理不到位将事件退回给	处理人		(8->7)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	public String saveReturnToDealPeople(EventInfoVo eventInfoVo);
	
	
	/**
	 * 两种情况: 1、各部门安全员、各部门负责人、安保办安全员、安保办主任 	认为该事件无需处理(达不到隐患级别或已处理完毕) , 事件办结		( 2、3、4、5 -> 9)
	 * 		  2、事件处理经处理人处理后,	安保办安全员	      对事件核查无误后  ,  事件办结	( 8 -> 9)
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年月日
	 */
	public String saveConfirmEventFinish(EventInfoVo eventInfoVo);
	
	
	/**
	 * 新增经办事件
	 * @param eventProcess
	 * @return
	 * @author xuezb
	 * @Date 2019年1月2日
	 */
	public void saveEventHandle(EventProcess eventProcess);
	
	/**
	 * 根据事件id,当前处理人,当前节点编码  获取 经办事件表(Event_Handle)中记录
	 * @param eventId
	 * @param epNowPersonId
	 * @param epNowNode
	 * @return
	 */
	List<EventHandle> getEventHandleByIdAndNode(String eventId, String epNowPersonId, String epNowNode);
	
	/**
	 * 根据事件id 获取 经办事件表(Event_Handle)中关联的事件过程
	 * @param eventId
	 * @return
	 */
	List<EventHandle> getEventHandleByEId(String eventId);
	
	/**
	 * 根据事件id 删除 经办事件表(Event_Handle)中关联的事件过程
	 * @param eventId
	 * @return
	 */
	void deleteEventHandleByEId(String eventId);
	
	
	
	/**
	 * 根据事件id 获取事件过程
	 * @param eventId
	 * @return List<EventProcess>
	 */
	List<EventProcess> getEProcessByEId(String eventId);
	
	/**
	 * 根据事件id 获取事件过程Vo	(比   getEProcessByEId 方法多获取事件过程的附件URL)
	 * @param eventId
	 * @return	List<EventProcessVo>
	 */
	List<EventProcessVo> getEProcessVoByEId(String eventId);
	
	/**
	 * 通过事件id和当前节点编码获取事件过程	(获取的是同节点编码最新的一条事件过程)
	 * @param eventId
	 * @param epNowNode
	 * @return eventProcess
	 * @author xuezb
	 * @Date 2018年5月24日
	 */
	public EventProcess getEProcessByEIdAndEpNowNode(String eventId, String epNowNode);
	
	
	
	/**
	 * 根据  用户角色编码(部门负责人、隐患处理人)、用户所在部门(根据部门id)、用户姓名	查询	用户列表
	 * @param roleCode
	 * @param departmentId
	 * @param userName
	 * @return List<User>
	 * @author xuezb
	 * @Date 2018年5月24日
	 */
	public List<User> getUserListByRoleCode(String roleCode, String departmentId, String userName);
	
	/**
	 * 
	 * @方法：@param eventInfoVo
	 * @方法：@return
	 * @描述：导出事件记录
	 * @return
	 * @author: qinyongqian
	 * @date:2019年8月23日
	 */
	public HSSFWorkbook export(EventInfoVo eventInfoVo);
	
	/**
	 * 
	 * @方法：@param ids
	 * @描述：删除
	 * @return
	 * @author: qinyongqian
	 * @date:2019年8月24日
	 */
	public void deleteEntitys(String ids);
	
}
