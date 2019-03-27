package com.dangjian.ql;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月21日
 *
 */
public class DangjianQl {

	public static final class MySql {
		
		//查党建活动开展情况，以每个月为基准，看活动是否开展，开展的次数
		public static final String activitiesMonthNum="SELECT  al.TITLE,al.FREQUENCY,al.al_count,uc.CALENDAR_NAME,uc.CALENDAR_NUM from um_calendar_month uc LEFT JOIN "
                     +" (SELECT a.TITLE,a.FREQUENCY,count(t.ID) as al_count,date_format(t.LAUNCH_DATE, '%m') as mm FROM p_dj_activities_launch t,p_dj_activities a "
                     +" WHERE t.ACTIVITY_ID = a.ID AND t.BRANCH_ID = '@@' AND t.ACTIVITY_ID = '?' "
                     +" AND DATE_FORMAT(t.LAUNCH_DATE, '%Y') = '##' GROUP BY date_format(t.LAUNCH_DATE, '%m')) al ON (uc.CALENDAR_NUM=al.mm) ORDER BY uc.CALENDAR_NUM";
		
		//查党建活动开展情况，以季度为基准，看活动是否开展，开展的次数
		public static final String activitiesQuarterNum="SELECT  al.TITLE,al.FREQUENCY,al.al_count,uc.CALENDAR_NAME,uc.CALENDAR_NUM from um_calendar_quarter uc LEFT JOIN "
                     +" (SELECT a.TITLE,a.FREQUENCY,count(t.ID) as al_count,FLOOR((date_format(t.LAUNCH_DATE, '%m ')+2)/3) as mm FROM p_dj_activities_launch t,p_dj_activities a "
                     +" WHERE t.ACTIVITY_ID = a.ID AND t.BRANCH_ID = '@@' AND t.ACTIVITY_ID = '?' "
                     +" AND DATE_FORMAT(t.LAUNCH_DATE, '%Y') = '##' GROUP BY FLOOR((date_format(t.LAUNCH_DATE, '%m ')+2)/3)) al ON (uc.CALENDAR_NUM=al.mm) ORDER BY uc.CALENDAR_NUM";
		
		//查党建活动开展情况，以半年为基准，看活动是否开展，开展的次数
		public static final String activitiesHalfYearNum="SELECT  al.TITLE,al.FREQUENCY,al.al_count,uc.CALENDAR_NAME,uc.CALENDAR_NUM from um_calendar_halfyear uc LEFT JOIN "
                     +" (SELECT a.TITLE,a.FREQUENCY,count(t.ID) as al_count,sign(date_format(t.LAUNCH_DATE, '%m')-6.1) as mm FROM p_dj_activities_launch t,p_dj_activities a "
                     +" WHERE t.ACTIVITY_ID = a.ID AND t.BRANCH_ID = '@@' AND t.ACTIVITY_ID = '?' "
                     +" AND DATE_FORMAT(t.LAUNCH_DATE, '%Y') = '##' GROUP BY sign(date_format(t.LAUNCH_DATE, '%m')-6.1)) al ON (uc.CALENDAR_NUM=al.mm) ORDER BY uc.CALENDAR_NUM";
		
		//查党建活动开展情况，以全年为基准，看活动是否开展，开展的次数
		public static final String activitiesYearNum="SELECT a.TITLE,a.FREQUENCY,count(t.ID) as al_count,'##年',##"
					+" FROM p_dj_activities_launch t,p_dj_activities a WHERE t.ACTIVITY_ID = a.ID AND t.BRANCH_ID = '@@' AND t.ACTIVITY_ID = '?'"
					+" AND DATE_FORMAT(t.LAUNCH_DATE, '%Y') = '##'";
		
		//
		public static final String collectActivitiesLanuch="SELECT al.atName,al.alId,al.alTime, uc.CALENDAR_NAME,uc.CALENDAR_NUM from um_calendar_month uc LEFT JOIN "
					+" (SELECT a.TITLE as atName,t.ID as alId,t.LAUNCH_DATE as alTime,date_format(t.LAUNCH_DATE, '%m') as mm FROM p_dj_activities_launch t, "
				    +" p_dj_activities a WHERE t.ACTIVITY_ID = a.ID AND t.BRANCH_ID = '@@' AND DATE_FORMAT(t.LAUNCH_DATE, '%Y') = '##' "  
				    +" AND a.ID = '?') al ON (uc.CALENDAR_NUM=al.mm) ORDER BY uc.CALENDAR_NUM";
		
		//活动汇总，查某支部，某年度，某月份的记录
		public static final String collectActivitiesLanuch2="SELECT ats.TITLE,al.LAUNCH_DATE,al.ID,al.LAUNCH_POINTS,ats.FREQUENCY from p_dj_activities ats LEFT JOIN p_dj_activities_launch al "
					+" ON (ats.ID=al.ACTIVITY_ID) WHERE al.BRANCH_ID='@@'  AND DATE_FORMAT(al.LAUNCH_DATE, '%Y') = '##' AND  DATE_FORMAT(al.LAUNCH_DATE, '%m') = '?' ORDER by ats.ORDER_ ";
						
		
		//党员列表
		public static final String partyMemberEntityListSql="SELECT pm.ID,u.USER_NAME,u.SEX_,br.BRANCH_NAME,pm.MEMBER_DUTY,pm.JOININ_TIME "
				+" FROM `p_dj_partymember` pm,p_dj_branch br,um_user u WHERE pm.BRANCH_ID=br.ID and pm.USER_ID=u.ID  ";
		
		
		//查党建活动开展列表，以月范围查询，如2019年1月份内的活动
		public static final String activitiesMonthList="SELECT ats.TITLE, al.LAUNCH_DATE,al.CREATOR_NAME,al.ID FROM `p_dj_activities_launch` al,p_dj_activities ats  "
                     +" where al.ACTIVITY_ID=ats.ID  and al.BRANCH_ID = '@@' AND al.ACTIVITY_ID = '?' "
                     +" AND DATE_FORMAT(al.LAUNCH_DATE, '%Y') = '##' and  date_format(al.LAUNCH_DATE, '%m')= %% ";
		
		//查党建活动开展列表，以季度范围查询，如2019年1季度内的活动
		public static final String activitiesQuarterList="SELECT ats.TITLE, al.LAUNCH_DATE,al.CREATOR_NAME,al.ID FROM `p_dj_activities_launch` al,p_dj_activities ats  "
                     +" where al.ACTIVITY_ID=ats.ID  and al.BRANCH_ID = '@@' AND al.ACTIVITY_ID = '?' "
                     +" AND DATE_FORMAT(al.LAUNCH_DATE, '%Y') = '##' and  FLOOR((date_format(al.LAUNCH_DATE, '%m ')+2)/3)= %% ";
        
		//查党建活动开展列表，以半年范围查询，如2019年上半年内的活动
		public static final String activitiesHalfYearList="SELECT ats.TITLE, al.LAUNCH_DATE,al.CREATOR_NAME,al.ID FROM `p_dj_activities_launch` al,p_dj_activities ats  "
                     +" where al.ACTIVITY_ID=ats.ID  and al.BRANCH_ID = '@@' AND al.ACTIVITY_ID = '?' "
                     +" AND DATE_FORMAT(al.LAUNCH_DATE, '%Y') = '##' and  sign(date_format(al.LAUNCH_DATE, '%m')-6.1)= %% ";
		   
		//查党建活动开展列表，以全年范围查询，如2019年内的活动
		public static final String activitiesYearList="SELECT ats.TITLE, al.LAUNCH_DATE,al.CREATOR_NAME,al.ID FROM `p_dj_activities_launch` al,p_dj_activities ats  "
                     +" where al.ACTIVITY_ID=ats.ID  and al.BRANCH_ID = '@@' AND al.ACTIVITY_ID = '?' "
                     +" AND DATE_FORMAT(al.LAUNCH_DATE, '%Y') = '##'";
		
		//查党建活动积分排名
		public static final String activitiesRanking="select db.ID,db.BRANCH_NAME,alt.points from p_dj_branch db LEFT JOIN(  "
                     +" SELECT al.BRANCH_ID as b_id,SUM(al.LAUNCH_POINTS) AS points from p_dj_activities_launch al  "
                     +" where DATE_FORMAT(al.LAUNCH_DATE, '%Y') = '##' GROUP BY al.BRANCH_ID "
					 +" ) alt on (db.ID=alt.b_id) ORDER BY alt.points desc ";
				                   
				
		
		
	}
}
