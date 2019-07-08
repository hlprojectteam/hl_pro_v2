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
		
		
		//查某人当月的调班次数，已经审批通过的
		public static final String changeShiftsMonthNum="SELECT k.ID FROM `kq_change_shifts` k WHERE DATE_FORMAT(k.CREATE_TIME, '%Y%m' ) = DATE_FORMAT(CURDATE( ), '%Y%m' ) "
			+" and k.APPLY_MEN_ID='?' and k.APPROVAL_STATUS=2";
		
		//请假，审批表联合查询
		public static final String leaveApprovalList="SELECT a.ID,a.CREATE_TIME,a.APPROVAL_NUMBER,a.CREATOR_NAME,a.APPROVAL_STATUS,a.START_TIME,a.END_TIME  "
			+" FROM `kq_leave` a,kq_attendance_approval b where a.ID=b.APPROVAL_RECORD_ID ";
		
		//调班，审批表联合查询
		public static final String changeShiftsApprovalList="SELECT a.ID,a.CREATE_TIME,a.APPROVAL_NUMBER,a.CREATOR_NAME,a.APPROVAL_STATUS,a.APPLY_MEN_ID,a.BE_APPLY_MEN_ID,  "
			+" a.APPLY_MEN_SHIFT,a.BE_APPLY_MEN_SHIFT,a.CHANGE_DATE FROM `kq_change_shifts` a,kq_attendance_approval b where a.ID=b.APPROVAL_RECORD_ID ";
		
	}
}
