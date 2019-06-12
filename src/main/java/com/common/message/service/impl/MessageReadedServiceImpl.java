package com.common.message.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.message.dao.IMessageReadedDao;
import com.common.message.module.MessageReaded;
import com.common.message.service.IMessageReadedService;
import com.common.message.vo.MessageReadedVo;
import com.common.utils.helper.Pager;

@Repository("messageReadedServiceImpl")
public class MessageReadedServiceImpl extends BaseServiceImpl implements IMessageReadedService{

	@Autowired
	public IMessageReadedDao messageReadedDaoImpl;
	
	@Override
	public void saveOrUpdate(MessageReaded messageReaded) {
		if(StringUtils.isNotBlank(messageReaded.getId())){
			this.update(messageReaded);
		}else{
			this.save(messageReaded);
		}
		
	}

	@Override
	public Pager queryEntityList(Integer page, Integer rows, MessageReadedVo messageReadedVo) {
		return null;
	}

	
	
	@Override
	public void deleteMessageReaded(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.delete(MessageReaded.class, idz[i]);
		}
		
	}

	@Override
	public MessageReaded queryEntityById(String id) {

		MessageReaded messageReaded = this.baseDaoImpl.getEntityById(MessageReaded.class, id);
		return messageReaded;
	}

	@Override
	public MessageReaded queryEntityByUserId(String userId) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("userId", userId));
		List<MessageReaded> list=messageReadedDaoImpl.queryEntityList(criterionsList, Order.desc("createTime"), MessageReaded.class);
		if(list!=null){
			if(list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}

}
