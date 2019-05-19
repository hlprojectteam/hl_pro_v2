package com.common.utils.tld;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.base.dao.IBaseDao;
import com.common.utils.helper.SpringUtils;
import com.urms.menu.module.MenuDefinition;
import com.urms.role.module.Role;
import com.urms.user.module.User;


/**
 * @日期 2015-3-17下午02:02:16
 * @描述 菜单功能权限
 */
@SuppressWarnings("serial")
public class ListMenuDefinition extends TagSupport {
	
	private static final Logger logger = LoggerFactory.getLogger(ListMenuDefinition.class);  
	
	private String menuCode;//菜单编码
	
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		User user = (User)this.pageContext.getSession().getAttribute("user");
		IBaseDao baseDaoImpl = (IBaseDao)SpringUtils.getBean("baseDaoImpl");
		List<MenuDefinition> mdList = new ArrayList<MenuDefinition>();
		StringBuffer hql = new StringBuffer();
		hql.append("select md from MenuDefinition md ");
		if(user.getType()!=1&&user.getType()!=2){//不是超级管理员或子系统管理员
			if(user.getRoles() != null && user.getRoles().size() > 0){
				hql.append(" left join md.roles r right join md.menu m where m.menuCode = '"+menuCode+"'");
				if(user.getRoles().size() == 1){
					for (Role role : user.getRoles()) {//普通角色
						hql.append(" and r.id='"+role.getId()+"'");
					}
				}else{
					hql.append(" and (");
					for (Role role : user.getRoles()) {//普通角色
						hql.append("r.id='"+role.getId()+"' or ");
					}
					hql = hql.delete(hql.length()-4, hql.length()-1);
					hql.append(")");
				}
				mdList.addAll(baseDaoImpl.queryEntityHQLList(hql.toString(), null, MenuDefinition.class));
			}
		}else{//超级管理员或子系统管理员
			hql.append(" right join md.menu m where m.menuCode = '"+menuCode+"'");
			mdList.addAll(baseDaoImpl.queryEntityHQLList(hql.toString(), null, MenuDefinition.class));
		}
		ComparatorMd comparator = new ComparatorMd();
		Collections.sort(mdList, comparator);
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer tableButton = new StringBuffer();
		tableButton.append("<script type=\"text/javascript\"> ");
		tableButton.append("var table_button = \"");
		try {
			stringBuffer.append("<div class='row' style='padding:0px 10px 5px 10px;'>");
			if(!mdList.isEmpty()){
				mdList = removeDuplicate(mdList);
				for (MenuDefinition md : mdList) {
					if(md!=null){
						if (md.getPageType().equals("list")) {//列表表单
							if(StringUtils.isNotBlank(md.getColour()))
								stringBuffer.append("<button class='btn "+md.getColour()+" ' id='"+md.getDefinitionCode()+"' style='margin-left: 5px;' type='button' onclick='"+md.getDefinitionMethod()+"'>");
							else
								stringBuffer.append("<button class='btn btn-primary ' id='"+md.getDefinitionCode()+"' style='margin-left: 5px;' type='button' onclick='"+md.getDefinitionMethod()+"'>");
							stringBuffer.append("<i class='fa fa-"+md.getIcon()+"'></i>&nbsp;");
							stringBuffer.append(md.getDefinitionName());
							stringBuffer.append("</button>");
						}else if(md.getPageType().equals("list_table")){//表格按钮
							tableButton.append("<a href='#' onclick='"+md.getDefinitionMethod()+"'>"+md.getDefinitionName()+"</a>");
						}
					}
				}
			}
			stringBuffer.append("</div>");
			tableButton.append("\";");
			tableButton.append("</script> ");
			out.println(stringBuffer.toString()+""+tableButton.toString().replaceAll("</a><a", "</a>&nbsp;&nbsp;<a"));
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	 }

	
	//菜单按钮排序
	public class ComparatorMd implements Comparator<Object>{
		public int compare(Object arg0, Object arg1) {
			MenuDefinition md0=(MenuDefinition)arg0;
			MenuDefinition md1=(MenuDefinition)arg1;
			return md0.getOrder().compareTo(md1.getOrder());
		}
	}
	
	/**
	 * @intruduction list去重
	 * @param list
	 * @return
	 * @author Mr.joker
	 * @Date 2016年7月12日 15:43:17
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List removeDuplicate(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}
	
	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

}