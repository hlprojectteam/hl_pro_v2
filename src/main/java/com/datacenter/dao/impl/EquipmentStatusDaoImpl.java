package com.datacenter.dao.impl;

import com.common.base.dao.impl.BaseDaoImpl;
import com.datacenter.dao.IEquipmentStatusDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @author xuezb
 * @date 2019年2月18日
 */
@SuppressWarnings("unchecked")
@Component("equipmentStatusDaoImpl")
@Scope("prototype")
public class EquipmentStatusDaoImpl extends BaseDaoImpl implements IEquipmentStatusDao {
}
