package com.answer.questions.service;

import com.answer.questions.vo.ExamReportVo;
import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;


public interface IExamReportService extends IBaseService{
	
	/**
	 * 考试成绩报表
	 * @intruduction
	 * @author Dic
	 * @param examManageId 
	 * @param orgFrameId 
	 * @return 
	 * @Date 2016年9月23日上午11:57:18
	 */
	public String getExamSourceReport(String examManageId, String orgFrameId);
	
	/**
	 * 完成率
	 * @intruduction
	 * @param examManageId
	 * @return
	 * @author Dic
	 * @Date 2016年10月14日上午8:58:35
	 */
	public ExamReportVo getBaseReport(String examManageId,String orgFrameId);
	
	/**
	 * 易错题
	 * @intruduction
	 * @param page
	 * @param rows
	 * @param examManageId
	 * @return
	 * @author Dic
	 * @Date 2016年10月14日上午10:27:12
	 */
	public Pager getWorryReport(Integer page, Integer rows,String examManageId,String orgFrameId);

	/**
	 * 做题人员
	 * @intruduction
	 * @param page
	 * @param rows
	 * @param id
	 * @return
	 * @author Dic
	 * @Date 2016年10月14日上午11:48:46
	 */
	public Pager getWorryPerson(Integer page, Integer rows, String id,String examManageId);
	
}
