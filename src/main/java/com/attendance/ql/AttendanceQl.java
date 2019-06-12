package com.attendance.ql;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月21日
 *
 */
public class AttendanceQl {

	public static final class MySql {
		
		//查某人当月的请假次数，已经审批通过的
		public static final String leaveMonthNum="SELECT k.ID FROM `kq_leave` k WHERE DATE_FORMAT(k.CREATE_TIME, '%Y%m' ) = DATE_FORMAT(CURDATE( ), '%Y%m' ) "
			+" and k.USER_ID='?' and k.APPROVAL_STATUS=2";
		
	}
}
