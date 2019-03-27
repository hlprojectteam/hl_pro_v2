package com.common.attach.service;



import java.util.List;

import com.common.attach.module.Attach;
import com.common.base.service.IBaseService;

public interface IAttachService extends IBaseService{
	
	/**
	 * @intruduction 凭FormID获得附件集合
	 * @param formId 表单id
	 * @return 附件集合
	 * @author Dic
	 * @Date 2016年1月21日下午12:01:42
	 */
	public List<Attach> queryAttchListByFormId(String formId);
	
	/**
	 * @intruduction 凭FormID和附件类型获得附件集合
	 * @param formId
	 * @param type
	 * @return
	 * @author Dic
	 * @Date 2016年6月24日上午10:14:50
	 */
	public List<Attach> queryAttchByFormIdAndType(String formId,String type);
	
	/**
	 * 
	 * @ClassName:IAttachService.java
	 * @Description:根据FORMID只获取为图片的附件
	 * @param formId
	 * @return
	 * @author qinyongqian
	 * @date 2016-8-8
	 */
	public List<Attach> queryAttchByFormIdAndOnlyPicture(String formId);
	
	/**
	 * @intruduction 删除附件
	 * @param ids
	 * @author Dic
	 * @Date 2016年1月26日下午4:38:07
	 */
	public void deleteAttach(String ids);
	
	/**
	 * @intruduction 删除附件by表单主见ID
	 * @param formId
	 * @author Dic
	 * @Date 2016年1月27日上午8:52:16
	 */
	public void deleteAttachByFormId(String formId);
}
