package com.datacenter.ql;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月21日
 *
 */
public class datacenterQl {

	public static final class MySql {
		
		//
		public static final String operatingData="SELECT * FROM `dc_operatingdata` t";
		
		//计算营运数据合计SQL
		public static final String operatingTotal="SELECT SUM(t.total_Traffic),SUM(t.ytk_Traffic),SUM(t.mobile_Payment_Traffic),"+
				"ROUND(SUM(t.general_Income),2),ROUND(SUM(t.ytk_Income),2),ROUND(SUM(t.mobile_Payment_Income),2)"+
				" FROM `dc_operatingdata` t";
		
		//计算营运数据合计SQL-按收费站分组统计
		public static final String operatingTotalGroupByTollGate="SELECT t.toll_Gate,sum(t.total_Traffic),sum(t.ytk_Traffic), sum(t.mobile_Payment_Traffic),"+
				" sum(t.general_Income),sum(t.ytk_Income),sum(t.mobile_Payment_Income)"+
				" AS counts from `dc_operatingdata` t where 1=1 ";
		
		
	}
}
