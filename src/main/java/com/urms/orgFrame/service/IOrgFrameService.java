package com.urms.orgFrame.service;



import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.user.module.User;
import com.urms.user.vo.UserVo;

public interface IOrgFrameService extends IBaseService{
	
	/**
	 * 说明：保存or更新
	 * 输入：@param orgFrame
	 * 输出：void
	 * 创建时间:2016-1-5 下午5:03:07
	 */
	public void saveOrUpdate(OrgFrame orgFrame);
	
	/**
	 * 说明：分页
	 * 输入：@param page
	 * 输入：@param rows
	 * 输入：@param orgFrameVo
	 * 输入：@param user
	 * 输入：@return
	 * 输出：Pager
	 * 创建时间:2016-1-5 下午4:46:02
	 */
	public Pager queryEntityList(int page,int rows,OrgFrameVo orgFrameVo,User user);
	
	/**
	 * 说明：获得组织架构集合
	 * 输入：@param orgFrameVo
	 * 输入：@return
	 * 输出：List<OrgFrame>
	 * 创建时间:2016-1-5 下午4:57:54
	 */
	public List<OrgFrame> queryOrgFrameList(OrgFrameVo orgFrameVo);

	/**
	 * 说明：获得当前节点下所有下级节点集合
	 * 输入：@param orgFrameId 节点Id
	 * 输入：@return
	 * 输出：List<OrgFrame>
	 * 创建时间:2016-1-5 下午4:57:54
	 */
	public List<OrgFrame> getOrgFrameChildren(String orgFrameId);
	
	/**
	 * 说明：凭父ID获得菜单集合
	 * 输入：@param pid
	 * 输入：@return
	 * 输出：List<OrgFrame>
	 * 创建时间:2016-1-5 下午4:48:56
	 */
	public List<OrgFrame> queryEntityListByPId(String pid);

	/**
	 * 说明：删除菜单树
	 * 输入：@param ids
	 * 输出：void
	 * 创建时间:2016-1-5 下午4:51:36
	 */
	public void deleteTree(String ids);

	/**
	 * 凭系统编码获得组织架构的根节点
	 * @intruduction
	 * @param sysCode 系统编码
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年10月26日下午5:17:56
	 */
	public OrgFrame getOrgFrameRootBySysCode(String sysCode);
	
	/**
	 * 
	 * @方法：@param userVo
	 * @方法：@return
	 * @描述：通过人，获取部门
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月11日
	 */
	public OrgFrame getOgrFrameByUser(UserVo userVo);
}
