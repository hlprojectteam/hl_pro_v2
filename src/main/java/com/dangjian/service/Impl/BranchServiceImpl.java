package com.dangjian.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.common.attach.service.IAttachService;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.dangjian.dao.IBranchDao;
import com.dangjian.module.Branch;
import com.dangjian.module.PartyMember;
import com.dangjian.service.IBranchService;
import com.dangjian.service.IPartyMemberService;
import com.dangjian.vo.BranchVo;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月1日
 *
 */
@Repository("branchServiceImpl")
public class BranchServiceImpl extends BaseServiceImpl implements IBranchService{

	@Autowired
	public IBranchDao branchDaoImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IPartyMemberService partyMemberServiceImpl;
	@Override
	public Pager queryBranchEntityList(Integer page, Integer rows,
			BranchVo branchVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(branchVo.getBranchName())){
			criterionsList.add(Restrictions.like("branchName", "%"+ branchVo.getBranchName()+"%"));
		}
		return this.branchDaoImpl.queryEntityList(page, rows, criterionsList, Order.asc("order"), Branch.class);
	}

	@Transactional
	@Override
	public void deleteBranchEntitys(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			this.delete(Branch.class, idz[i]);
		}
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.attachServiceImpl.deleteAttachByFormId(idz[i]);
		}
		
	}

	@Override
	public void saveOrUpdateBranch(Branch branch) {
		if(StringUtils.isBlank(branch.getId())){
			this.save(branch);
		}else{
			this.update(branch);
		}
		
	}

	@Override
	public Branch getUserBranch(UserVo userVo) {
		PartyMember p= partyMemberServiceImpl.getPartyMemberByUser(userVo);
		if(p!=null){
			return branchDaoImpl.getEntityById(Branch.class, p.getBranchId());
		}
		return null;
	}

	@Override
	public List<Branch> queryALLEntityList() {
		// TODO Auto-generated method stub
		return this.branchDaoImpl.queryAllEntity(Branch.class, Order.asc("order"));
		
	}

	@Override
	public List<Branch> queryBranch(BranchVo branchVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(branchVo.getBranchName())){
			criterionsList.add(Restrictions.eq("branchName", branchVo.getBranchName()));
		}
		return this.branchDaoImpl.queryList(criterionsList, Order.asc("branchName"),  Branch.class);
	}

}
