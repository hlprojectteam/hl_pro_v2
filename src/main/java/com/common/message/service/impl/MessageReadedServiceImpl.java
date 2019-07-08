package com.common.message.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

	public static Logger logger = Logger.getLogger(MessageReadedServiceImpl.class);
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

	@Override
	public void updateCleanMessageReaded() {
		//定义消息的最大长度
		int maxL=50;
		int updateCount=0;
		List<MessageReaded> list= messageReadedDaoImpl.queryAllEntity(MessageReaded.class, Order.desc("createTime"));
		for (MessageReaded messageReaded : list) {
			String msg=messageReaded.getReadMsgs();
			if(msg.indexOf(",")>0){
				String[] msgArr= msg.split(",");
				int lmsg=msgArr.length;
				if(lmsg>maxL){
					//长度大于50个id，进行删减
					//index 多出的个数
					int index=lmsg-maxL;
					for (int i = 0; i < index; i++) {
						msg= msg.substring(msg.indexOf(",")+1, msg.length()); 
					}
					messageReaded.setReadMsgs(msg);
//					System.out.println(messageReaded.getId()+":"+messageReaded.getReadMsgs());
					this.saveOrUpdate(messageReaded);
					updateCount++;
				}
			}
		}
		logger.info("--"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"-清除已经读消息数目为："+updateCount);
		
	}

}
