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
import com.dangjian.dao.IIntroductionDao;
import com.dangjian.module.Introduction;
import com.dangjian.service.IIntroductionService;
import com.dangjian.vo.IntroductionVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月22日
 * 
 */
@Repository("introductionServiceImpl")
public class IntroductionServiceImpl extends BaseServiceImpl implements
		IIntroductionService {

	@Autowired
	public IIntroductionDao introductionDaoImpl;
	@Autowired
	public IAttachService attachServiceImpl;

	@Override
	public Pager queryEntityList(Integer page, Integer rows,
			IntroductionVo introductionVo) {
		// TODO Auto-generated method stub
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(introductionVo.getBranchId())) {
			criterionsList.add(Restrictions.eq("branchId", introductionVo.getBranchId()));
		}
		if (StringUtils.isNotBlank(introductionVo.getMainTitle())) {
			criterionsList.add(Restrictions.like("mainTitle", "%"
					+ introductionVo.getMainTitle() + "%"));
		}
		if (StringUtils.isNotBlank(introductionVo.getAuthorName())) {
			criterionsList.add(Restrictions.like("authorName", "%"
					+ introductionVo.getAuthorName() + "%"));
		}
		if (introductionVo.getCategory() != null) {
			criterionsList.add(Restrictions.eq("category",
					introductionVo.getCategory()));
		}
		if (introductionVo.getStatus() != null) {
			criterionsList.add(Restrictions.eq("status",
					introductionVo.getStatus()));
		}
		if (introductionVo.getNewSource() != null) {
			criterionsList.add(Restrictions.eq("newSource",
					introductionVo.getNewSource()));
		}

		if (introductionVo.getShowPlace() != null) {
			criterionsList.add(Restrictions.eq("showPlace",
					introductionVo.getShowPlace()));
		}
		if (introductionVo.getIsTop() != null) {
			criterionsList.add(Restrictions.eq("isTop",
					introductionVo.getIsTop()));
		}
		if (introductionVo.getLable() != null) {
			criterionsList.add(Restrictions.eq("lable",
					introductionVo.getLable()));
		}
		return this.introductionDaoImpl.queryEntityList(page, rows,
				criterionsList, Order.desc("releaseDate"), Introduction.class);
	}

	@Override
	public Introduction queryEntityById(String id) {
		Introduction introduction = introductionDaoImpl.getEntityById(
				Introduction.class, id);
		return introduction;
	}

	@Transactional
	@Override
	public void deleteIntroduction(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			this.delete(Introduction.class, idz[i]);
		}
		for (int i = 0; i < idz.length; i++) {
			// 删除附件
			this.attachServiceImpl.deleteAttachByFormId(idz[i]);
		}
	}

	@Override
	public String saveChangeState(String id) {
		Introduction introduction = this.getEntityById(Introduction.class, id);
		String sign = "";
		if (introduction.getStatus() == null)
			introduction.setStatus(0);// 默认未发布
		if (introduction.getStatus() == 1) {
			introduction.setStatus(0);
			sign = "down";
		} else {
			introduction.setStatus(1);
			sign = "up";
		}
		this.update(introduction);
		return sign;
	}

	@Override
	public void saveOrUpdate(Introduction introduction) {

		if (StringUtils.isBlank(introduction.getId())) {
			this.save(introduction);
		} else {
			this.update(introduction);
		}
	}

}
