package com.answer.questions.service;


import com.answer.questions.module.ExamPerson;
import com.answer.questions.vo.ExamManageVo;
import com.answer.questions.vo.ExamPersonVo;
import com.answer.questions.vo.QuestionManageVo;
import com.answer.questions.vo.QuestionVo;
import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.user.module.User;

public interface IQuestionsService extends IBaseService{
	
	/**
	 * 说明：保存or更新
	 * 输入：@param menu
	 * 输出：void
	 * 创建时间:2016-1-5 下午5:03:07
	 */
	public void saveOrUpdate(QuestionManageVo questionManageVo);
	
	/**
	 * 说明：分页
	 * 输入：@param page
	 * 输入：@param rows
	 * 输入：@param menuVo
	 * 输入：@return
	 * 输出：Pager
	 * 创建时间:2016-1-5 下午4:46:02
	 */
	public Pager queryEntityList(int page,int rows,QuestionManageVo questionManageVo);

	/**
	 * 删除
	 * @intruduction
	 * @param ids
	 * @author Dic
	 * @Date 2016年9月7日上午9:45:09
	 */
	public void deleteQuestionManage(String ids);

	//---------------------------------------------------
	/**
	 * 保存问题
	 * @intruduction
	 * @param questionVo
	 * @author Dic
	 * @Date 2016年9月8日下午5:32:31
	 */
	public void saveOrUpdateQuestion(QuestionVo questionVo);
	
	/**
	 * 保存判断
	 * @intruduction
	 * @param questionVo
	 * @author Dic
	 * @Date 2016年9月12日下午3:41:35
	 */
	public void saveOrUpdateQuestionJudge(QuestionVo questionVo);
	
	/**
	 * 保存填空
	 * @intruduction
	 * @param questionVo
	 * @author Dic
	 * @Date 2016年9月12日下午3:41:35
	 */
	public void saveOrUpdateQuestionFill(QuestionVo questionVo);
	/**
	 * 问题列表
	 * @intruduction
	 * @param page
	 * @param rows
	 * @param questionVo
	 * @return
	 * @author Dic
	 * @Date 2016年9月9日上午9:31:56
	 */
	public Pager queryQuestionList(Integer page, Integer rows,QuestionVo questionVo);

	/**
	 * 删除题目
	 * @intruduction
	 * @param ids
	 * @author Dic
	 * @Date 2016年9月9日上午10:58:03
	 */
	public void deleteQuestion(String ids);

	//---------------------------------------
	/**
	 * 考试管理分页
	 * @intruduction
	 * @param page
	 * @param rows
	 * @param examManageVo
	 * @return
	 * @author Dic
	 * @Date 2016年9月9日下午2:11:40
	 */
	public Pager queryExamManageList(Integer page, Integer rows,ExamManageVo examManageVo);

	/**
	 * 考试管理
	 * @intruduction
	 * @param examManageVo
	 * @author Dic
	 * @Date 2016年9月9日下午2:18:06
	 */
	public void saveOrUpdateExamManage(ExamManageVo examManageVo);

	/**
	 * 考试管理删除
	 * @intruduction
	 * @param ids
	 * @author Dic
	 * @Date 2016年9月9日下午2:19:54
	 */
	public int deleteExamManage(String ids);

	/**
	 * 改变状态 发布or撤销
	 * @intruduction
	 * @param id
	 * @return
	 * @author Dic
	 * @Date 2016年9月12日下午5:04:42
	 */
	public String saveChangeState(String id);
	/**
	 * 考试人员列表
	 * @intruduction
	 * @param page
	 * @param rows
	 * @param examPersonVo
	 * @return
	 * @author Dic
	 * @param sort 
	 * @param order 
	 * @Date 2016年9月12日上午11:30:57
	 */
	public Pager queryExamPsersonList(Integer page, Integer rows,String sort, String order, ExamPersonVo examPersonVo);

	/**
	 * 删除考试人员
	 * @intruduction
	 * @param ids
	 * @author Dic
	 * @Date 2016年9月12日上午11:52:44
	 */
	public void deleteExamPerson(String ids);

	//-----------------------------------------------------
	/**
	 * 在线考试列表
	 * @intruduction
	 * @param page
	 * @param rows
	 * @param examManageVo
	 * @return
	 * @author Dic
	 * @Date 2016年9月12日下午1:32:34
	 */
	public Pager queryExamOnlineList(Integer page, Integer rows,ExamManageVo examManageVo,User user);

	/**
	 * 试题生成
	 * @intruduction
	 * @param examManageVo
	 * @param user 当前登录人
	 * @author Dic
	 * @Date 2016年9月13日下午2:36:22
	 */
	public boolean submitGenerateExam(ExamManageVo examManageVo,User user);

	/**
	 * 检查试题是否生成
	 * @intruduction
	 * @param examManageVo
	 * @param sessionUser
	 * @author Dic
	 * @Date 2016年9月18日上午9:53:13
	 */
	public boolean checkExam(ExamManageVo examManageVo, User user);

	/**
	 * 获得试题 分页
	 * @intruduction
	 * @param page
	 * @param rows
	 * @param user
	 * @author Dic
	 * @param sign 
	 * @Date 2016年9月14日下午2:31:04
	 */
	public Pager queryOnlineExam(Integer page, Integer rows,ExamManageVo examManageVo, User user, Integer sign);

	/**
	 * 检查是否已经做完题目
	 * @intruduction
	 * @param sessionUser
	 * @author Dic
	 * @param questionManageIds 
	 * @Date 2016年9月18日下午3:59:07
	 */
	public boolean checkExamIsOver(String questionManageIds, String examManageId, User sessionUser);

	/**
	 * 在线考试保存答案
	 * @intruduction
	 * @param requestStr
	 * @author Dic
	 * @Date 2016年9月18日下午4:45:22
	 */
	public void saveOnlineExam(String requestStr,User user);

	/**
	 * 提交分数
	 * @intruduction
	 * @param examManageId
	 * @param sessionUser
	 * @author Dic
	 * @Date 2016年9月19日上午11:39:02
	 */
	public ExamPerson submitOnlineExam(String examManageId, User user, int consumeTime);

	/**
	 * 获得人员成绩
	 * @intruduction
	 * @param id
	 * @param sessionUser
	 * @return
	 * @author Dic
	 * @Date 2016年9月19日下午3:01:09
	 */
	public ExamPerson getExamPersonResult(String examManageId, String userId);

	/**
	 * 我的在线考试
	 * @intruduction
	 * @param page
	 * @param rows
	 * @param examManageVo
	 * @param sessionUser
	 * @return
	 * @author Dic
	 * @Date 2016年9月22日上午9:32:18
	 */
	public Pager queryMyExamOnlineList(Integer page, Integer rows,ExamManageVo examManageVo, User user);

	/**
	 * 保存新增加考试人员
	 * @intruduction
	 * @param examManageVo
	 * @return
	 * @author Dic
	 * @Date 2016年9月22日下午5:04:30
	 */
	public void saveAddPerson(ExamManageVo examManageVo);

	/**
	 * 题库是否被使用
	 * @intruduction
	 * @param id
	 * @return
	 * @author Dic
	 * @Date 2016年10月13日上午9:02:10
	 */
	public boolean checkQMIsUse(String id);

	/**
	 * 是否已经考试
	 * @intruduction
	 * @param examManageVo
	 * @author Dic
	 * @Date 2016年10月18日下午4:32:25
	 */
	public long checkIsExam(ExamManageVo examManageVo);

	
}
