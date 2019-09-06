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
			+" a.APPLY_MEN_SHIFT,a.BE_APPLY_MEN_SHIFT,a.CHANGE_DATE,a.CHANGE_TYPE FROM `kq_change_shifts` a,kq_attendance_approval b where a.ID=b.APPROVAL_RECORD_ID ";
		
		//请假记录查询
		public static final String leaveRecord="SELECT a.ID,a.CREATE_TIME,a.APPROVAL_NUMBER,a.CREATOR_ID,a.CREATOR_NAME,a.START_TIME,a.END_TIME,a.TIME_LENGTH,a.LEAVE_TYPE, "
				+"a.APPROVAL_STATUS,d.APPROVAL_USER_ID,b.JOB_NUMBER,c.ORG_FRAME_NAME,a.LEAVE_REASON,a.OUT_ADDRESS,d.APPROVAL_TIME,d.APPROVAL_CONTENT FROM `kq_leave` a,um_user b,um_orgframe c,kq_attendance_approval d  "
				+"where a.CREATOR_ID=b.ID and b.ORGFRAME_ID=c.ID and a.ID=d.APPROVAL_RECORD_ID ";
		
		//请假统计查询
		public static final String leaveStatistics="SELECT COUNT(t.ID) as count_,t.CREATOR_ID, u.USER_NAME,u.JOB_NUMBER,o.ORG_FRAME_NAME  "
				+"FROM `kq_leave` t,um_user u,um_orgframe o where t.CREATOR_ID=u.ID and u.ORGFRAME_ID=o.ID   "
				+"and t.APPROVAL_STATUS=2 ";
		
		//调班记录查询
		public static final String changeShiftsRecord="SELECT s.ID,s.CREATE_TIME,s.APPROVAL_NUMBER,s.CHANGE_DATE,o.ORG_FRAME_NAME,u.USER_NAME,u.JOB_NUMBER,s.APPLY_MEN_SHIFT,  "
				+"s.BE_APPLY_MEN_ID,s.BE_APPLY_MEN_SHIFT,s.CHANGE_TYPE,s.APPROVAL_STATUS,aa.APPROVAL_USER_ID,s.CHANGE_REASON,aa.APPROVAL_TIME,aa.APPROVAL_CONTENT  "
				+"FROM `kq_change_shifts` s,um_user u,um_orgframe o,kq_attendance_approval aa  WHERE s.APPLY_MEN_ID=u.ID and u.ORGFRAME_ID=o.ID and s.ID=aa.APPROVAL_RECORD_ID ";
	
		//调班统计查询
		public static final String changeShiftsStatistics="SELECT COUNT(t.ID) as count_,t.CREATOR_ID, u.USER_NAME,u.JOB_NUMBER,o.ORG_FRAME_NAME  "
				+"FROM `kq_change_shifts` t,um_user u,um_orgframe o where t.CREATOR_ID=u.ID and u.ORGFRAME_ID=o.ID   "
				+"and t.APPROVAL_STATUS=2 ";
	
	}
}
