package com.mobile.datacenter;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.datacenter.module.Brief;
import com.datacenter.service.IBriefService;
import com.datacenter.service.ITotalTableService;
import com.datacenter.vo.BriefVo;
import com.datacenter.vo.TotalTableVo;

/**
 * 
 * @Description 数据中心接口
 * @author qinyongqian
 * @date 2019年6月13日
 *
 */
@Controller
@RequestMapping(value="/dataCenterMobile")
public class MobileDataCenterController extends BaseController{
	
	@Autowired
	private ITotalTableService totalTableServiceImpl;
	
	@Autowired
	private IBriefService briefServiceImpl;
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param totalTableVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：报表列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月13日
	 */
	@RequestMapping(value="/totalTable_list")
	public void totalTableList(HttpServletRequest request,HttpServletResponse response,
			TotalTableVo totalTableVo,Integer page,Integer rows){

		Pager pager = this.totalTableServiceImpl.queryEntityList(page, rows, totalTableVo);
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
		String[] excludes = new String[] { "createTime","","creatorName","creatorId","sysCode" }; // 列表排除信息内容字段，减少传递时间
		config.setExcludes(excludes);
		json.put("total", pager.getRowCount());
		json.put("curPageSize", pager.getPageList().size());
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		json.put("result", true);
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param briefVo
	 * @描述：简报明细
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月13日
	 */
	@RequestMapping(value="/totalTable_detail")
	public void totalTableDetail(HttpServletRequest request,HttpServletResponse response,BriefVo briefVo){
		JSONObject json = new JSONObject();
		json.put("result", false);
		if(briefVo.getTtId()!=null){
			List<Brief> briefList = this.briefServiceImpl.queryEntityList(briefVo);

			if(briefList != null && briefList.size() > 0){
				Brief brief = briefList.get(0);
				
				JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
				config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
				String[] excludes = new String[] {"creatorName","creatorId","createTime","sysCode",
						"formNumber"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				
				json.put("result", true);
				json.put("object", JSONObject.fromObject(brief, config));
			}
		}
		this.print(json);
	}

}
