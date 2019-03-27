package com.urms.sysConfig.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.urms.sysConfig.dao.ISuperPasswordDao;
import com.urms.sysConfig.module.SuperPassword;
import com.urms.sysConfig.service.ISuperPasswordService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengcong
 * @Description:
 * @date 2018/8/29 10:34
 */
@Repository("superPasswordImpl")
public class SuperPasswordImpl extends BaseServiceImpl implements ISuperPasswordService {

    @Autowired
    ISuperPasswordDao superPasswordDao;
    @Override
    public Pager queryEntityList(int page, int rows, SuperPassword superPassword) {
        List<Criterion> criterionsList = new ArrayList<Criterion>();
        return superPasswordDao.queryList(page, rows, criterionsList, Order.desc("createTime") ,SuperPassword.class);
    }

    @Override
    public String getSuperPassword() {
        String superPassword = "";
        String sql = "select ENCRYPTED_TEXT from UM_SUPER_PASSWORD sp where sp.IS_EENABLED = 1";
        List<Object> list = superPasswordDao.queryBySql(sql);
        if(list != null && list.size() > 0){
            superPassword = list.get(0).toString();
        }
        return superPassword;
    }

    @Override
    public void saveOrUpdate(SuperPassword superPassword) {
        if(StringUtils.isNotBlank(superPassword.getId())){
            this.update(superPassword);
        }else {
            this.save(superPassword);
        }
    }
}
