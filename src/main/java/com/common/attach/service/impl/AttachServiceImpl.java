package com.common.attach.service.impl;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.attach.dao.IAttachDao;
import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.service.impl.BaseServiceImpl;

@Repository("attachServiceImpl")
public class AttachServiceImpl extends BaseServiceImpl implements IAttachService{
	
	@Autowired
	public IAttachDao attachDaoImpl;
	
	@Override
	public List<Attach> queryAttchListByFormId(String formId){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("formId", formId));
		return attachDaoImpl.queryEntityList(criterionsList, null ,Attach.class);
	}
	
	@Override
	public List<Attach> queryAttchByFormIdAndType(String formId,String type){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(formId))
			criterionsList.add(Restrictions.eq("formId", formId));
		if(StringUtils.isNotBlank(type))
			criterionsList.add(Restrictions.eq("attachType", type));
		return attachDaoImpl.queryEntityList(criterionsList, null ,Attach.class);
	}
	
	@Override
	public List<Attach> queryAttchByFormIdAndOnlyPicture(String formId) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("formId", formId));
		criterionsList.add(
		Restrictions.or(Restrictions.eq("suffix", "jpg"), Restrictions.eq("suffix", "gif"),Restrictions.eq("suffix", "png"),
				Restrictions.eq("suffix", "bmp"),Restrictions.eq("suffix", "jpeg"),Restrictions.eq("suffix", "tif"),Restrictions.eq("suffix", "JPG"), 
				Restrictions.eq("suffix", "GIF"),Restrictions.eq("suffix", "PNG"),
				Restrictions.eq("suffix", "BMP"),Restrictions.eq("suffix", "JPEG"),Restrictions.eq("suffix", "TIF")));
		return attachDaoImpl.queryEntityList(criterionsList, null ,Attach.class);
	}
	
	@Override
	public void deleteAttach(String ids) {
		String[] idsZ = ids.split(",");
		for (int i = 0; i < idsZ.length; i++) {
			Attach attach = attachDaoImpl.getEntityById(Attach.class, idsZ[i]);
			File file = new File(attach.getPath());  
		    // 路径为文件且不为空则进行删除  
		    if (file.isFile() && file.exists()) {  
		        file.delete();  
		    }  
			attachDaoImpl.delete(attach);
		}
	}
	
	@Override
	public void deleteAttachByFormId(String formId) {
		List<Attach> list = this.queryAttchListByFormId(formId);
		for (int i = 0; i < list.size(); i++) {
			this.deleteAttach(list.get(i).getId());
		}
	}
	
	public IAttachDao getAttachDaoImpl() {
		return attachDaoImpl;
	}

	public void setAttachDaoImpl(IAttachDao attachDaoImpl) {
		this.attachDaoImpl = attachDaoImpl;
	}

	


}
