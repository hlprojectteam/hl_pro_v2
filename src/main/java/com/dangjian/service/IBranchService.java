package com.dangjian.service;

import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.dangjian.module.Branch;
import com.dangjian.vo.BranchVo;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月1日
 *
 */
public interface IBranchService extends IBaseService{

	/**********以下是 党支部方法************/
	public Pager queryBranchEntityList(Integer page, Integer rows,BranchVo branchVo);
	
	public List<Branch> queryALLEntityList();
	
	public void deleteBranchEntitys(String ids);
	
	public void saveOrUpdateBranch(Branch branch);
	
	public Branch getUserBranch(UserVo userVo);
	
	public List<Branch> queryBranch(BranchVo branchVo);
}
