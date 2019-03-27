package com.urms.menu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.urms.menu.dao.IMenuDao;
import com.urms.menu.module.Menu;
import com.urms.menu.module.MenuDefinition;
import com.urms.menu.ql.MenuQL;
import com.urms.menu.service.IMenuService;
import com.urms.menu.vo.MenuVo;
import com.urms.roleRight.module.RoleRight;

@Repository("menuServiceImpl")
public class MenuServiceImpl extends BaseServiceImpl implements IMenuService{
	
	@Autowired
	public IMenuDao menuDaoImpl;

	@Override
	public void saveOrUpdate(Menu menu){
		if(StringUtils.isBlank(menu.getId())){
			this.save(menu);
		}else{
			Menu m = this.getEntityById(Menu.class, menu.getId());
			menu.setSubsystems(m.getSubsystems());
			BeanUtils.copyProperties(menu,m);
			this.update(m);	
		}
	}
	
	public void save(Menu menu){
		Menu pMenu = this.getEntityById(Menu.class, menu.getpId());
		menu.setCreateTime(new Date());
		menu.setIsLeaf(1);//叶子节点
		if(StringUtils.isNotBlank(pMenu.getpIds())){
			menu.setpIds(pMenu.getpIds()+"/"+pMenu.getId());			
			menu.setLevel(menu.getpIds().split("/").length);//层数
		}else{
			menu.setpIds(pMenu.getId());	
			menu.setLevel(1);//层数
		}
		if(StringUtils.isNotBlank(pMenu.getpNames()))
			menu.setpNames(pMenu.getpNames()+"/"+pMenu.getMenuName());			
		else
			menu.setpNames(pMenu.getMenuName());	
		this.menuDaoImpl.save(menu);
		if(pMenu.getIsLeaf()==1){
			pMenu.setIsLeaf(0);
			this.menuDaoImpl.update(pMenu);
		}
	}

	@Override
	public Pager queryEntityList(int page,int rows,MenuVo menuVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(menuVo.getId())){
			Menu menu = this.getEntityById(Menu.class, menuVo.getId());
			//criterionsList.add(Restrictions.ge("level", menu.getLevel()));//大于等于当前等级
			//criterionsList.add(Restrictions.like("pIds", "%"+menu.getId()+"%")); //旧的，把下面所有的都查出来了
			criterionsList.add(Restrictions.eq("pId", menu.getId()));  // Mr.Wang 只查下一级的数据， 2018年5月31日 14:21:41
		}else{
			// Mr.Wang 如果为空，第一次进入菜单管理界面，只加载“菜单”下面对应的一级菜单数据
			criterionsList.add(Restrictions.eq("pId", "0"));  
		}
		if(StringUtils.isNotBlank(menuVo.getMenuName())){
			criterionsList.add(Restrictions.like("menuName", "%"+menuVo.getMenuName()+"%"));
		}
		if(StringUtils.isNotBlank(menuVo.getMenuCode())){
			criterionsList.add(Restrictions.like("menuCode", "%"+menuVo.getMenuCode()+"%"));
		}
		return this.menuDaoImpl.queryList(page, rows, criterionsList, Order.desc("order") ,Menu.class); //Mr.Wang 改了排序  原来是createTime
	}
	
	@Override
	public List<Menu> queryEntityListByPId(String pid){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("pId", pid));
		criterionsList.add(Restrictions.eq("state", 1));//状态为可用状态
		return this.menuDaoImpl.queryList(criterionsList, Order.asc("order") ,Menu.class);
	}
	
	@Override
	public List<Menu> queryMenuList(MenuVo menuVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(menuVo.getId()))
			criterionsList.add(Restrictions.eq("id", menuVo.getId()));
		if(StringUtils.isNotBlank(menuVo.getpId()))
			criterionsList.add(Restrictions.eq("pId", menuVo.getpId()));
		if(menuVo.getLevel()!=null)
			criterionsList.add(Restrictions.eq("level", menuVo.getLevel()));
		if(menuVo.getMenuCode()!=null)
			criterionsList.add(Restrictions.eq("menuCode", menuVo.getMenuCode()));
		if(menuVo.getMenuType()!=null){
			if(menuVo.getMenuType()==12){
				Object[] menuType = new Object[]{1,2};
				criterionsList.add(Restrictions.in("menuType", menuType));
			}else{
				criterionsList.add(Restrictions.eq("menuType", menuVo.getMenuType()));
			}
		}
		if(menuVo.getState()!=null)//状态
			criterionsList.add(Restrictions.eq("state", menuVo.getState()));
		return this.menuDaoImpl.queryList(criterionsList,Order.asc("order"), Menu.class);
	}
	
	
	@Override
	public void deleteTree(String ids) {
		String[] idsZ = ids.split(",");
		for (int i = 0; i < idsZ.length; i++) {
			Menu menu = this.menuDaoImpl.getEntityById(Menu.class, idsZ[i]);
			String pid = menu.getpId();//父id
			//先删除菜单权限表单数据
			for (RoleRight rr : menu.getRoleRights()) {
				this.menuDaoImpl.delete(rr);
			}
			//先删除功能按钮表单数据
			for (MenuDefinition md : menu.getMenuDefinitions()) {
				this.menuDaoImpl.delete(md);
			}
			this.menuDaoImpl.delete(menu);
			//修改父节点  
			MenuVo menuVo = new MenuVo();
			menuVo.setpId(pid);
			List<Menu> list = this.queryMenuList(menuVo);
			if(list.isEmpty()){
				Menu pmenu = this.getEntityById(Menu.class,pid);
				pmenu.setIsLeaf(1);
				this.menuDaoImpl.update(pmenu);
			}
		}
	}
	
	@Override
	public void saveOrUpdateDefinition(MenuDefinition menuDefinition) {
		if(StringUtils.isBlank(menuDefinition.getId()))
			this.menuDaoImpl.save(menuDefinition);
		else
			this.menuDaoImpl.update(menuDefinition);
	}

	@Override
	public Pager queryMenuDefinitionList(int page,int rows,MenuVo menuVo){
		List<Object> attr = new ArrayList<Object>();
		attr.add(menuVo.getId());
		return menuDaoImpl.queryEntityHQLList(page, rows, MenuQL.HQL.MenuDefinitionList, attr);
	}
	
	@Override
	public List<MenuDefinition> queryMenuDefinitionList(MenuVo menuVo){
		List<Object> attr = new ArrayList<Object>();
		attr.add(menuVo.getId());
		return menuDaoImpl.queryEntityHQLList(MenuQL.HQL.MenuDefinitionList, attr,MenuDefinition.class);
	}
	
	/**
	 * 凭code查询菜单
	 */
	public Menu getMenuByCode(String menuCode){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("menuCode", menuCode));
		List<Menu> menuList = menuDaoImpl.queryList(criterionsList, null ,Menu.class);
		Menu menu = null;
		if(!menuList.isEmpty())
			menu = menuList.get(0);
		return menu;
	}
	
	public IMenuDao getMenuDaoImpl() {
		return menuDaoImpl;
	}

	public void setMenuDaoImpl(IMenuDao menuDaoImpl) {
		this.menuDaoImpl = menuDaoImpl;
	}

}
