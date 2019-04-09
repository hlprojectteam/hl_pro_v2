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
import com.common.message.dao.IMessageDao;
import com.common.message.module.Message;
import com.common.message.service.IMessageService;
import com.common.message.vo.MessageVo;
import com.common.utils.helper.Pager;

@Repository("messageServiceImpl")
public class MessageServiceImpl extends BaseServiceImpl implements IMessageService{

	@Autowired
	public IMessageDao messageDaoImpl;
	
	@Override
	public void saveOrUpdate(Message msg) {
		if(StringUtils.isNotBlank(msg.getId())){
			this.update(msg);
		}else{
			this.save(msg);
		}
		
	}

	@Override
	public Pager queryEntityList(Integer page, Integer rows, MessageVo messageVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(messageVo.getTitle())){
			criterionsList.add(Restrictions.like("title", "%" + messageVo.getTitle() + "%"));
		}
		if(messageVo.getType() != null){
			criterionsList.add(Restrictions.eq("type", messageVo.getType()));
		}
//		if(StringUtils.isNotBlank(messageVo.getAlias()) && StringUtils.isNotBlank(messageVo.getTags())){
//			String[] tags = messageVo.getTags().split(",");
//			Criterion[] cris = new Criterion[tags.length + 3];
//			for(int i = 0; i <tags.length;i++){
//				cris[i] = Restrictions.like("tags", tags[i]);
//			}
//			cris[tags.length] = Restrictions.like("alias", "%"+messageVo.getAlias()+"%");
//			cris[tags.length + 1] = Restrictions.like("createUserId", "%"+messageVo.getAlias()+"%");
//			cris[tags.length + 2] = Restrictions.sqlRestriction("ALIAS = '' AND TAGS = ''");
//			criterionsList.add(Restrictions.or(cris));
//		}
		if(StringUtils.isNotBlank(messageVo.getAlias())){
//			criterionsList.add(Restrictions.like("alias", "%" + messageVo.getAlias() + "%"));
			criterionsList.add(Restrictions.sqlRestriction("(ALIAS ='' OR ALIAS IS NULL OR ALIAS LIKE '%"+messageVo.getAlias()+"%')"));
		}
		if(StringUtils.isNotBlank(messageVo.getTags())){
			String sql="(";
			String[] tags = messageVo.getTags().split(",");
			Criterion[] cris = new Criterion[tags.length];
			for(int i = 0; i <tags.length;i++){
//				cris[i] = Restrictions.like("tags", "%" +tags[i]+ "%");
				sql+=" TAGS LIKE '%"+tags[i]+"%' OR" ;
			}
			sql=sql.substring(0, sql.length()-2);//截取掉多余的‘OR’
			sql+=")";
//			criterionsList.add(Restrictions.or(cris));
			criterionsList.add(Restrictions.sqlRestriction(sql));
		}
		return this.messageDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime"), Message.class);
	}

	
	
	@Override
	public void deleteMessage(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.delete(Message.class, idz[i]);
		}
		
	}

	@Override
	public Message queryEntityById(String id) {

		Message msg = this.baseDaoImpl.getEntityById(Message.class, id);
		return msg;
	}

}
