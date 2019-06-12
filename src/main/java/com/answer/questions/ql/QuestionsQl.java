package com.answer.questions.ql;

public class QuestionsQl {
	
	public static final class HQL {
		//获得考试试卷
		public static final String queryOnlineExam = "from Question as q ,ExamPersonQuestion as epq where q.id=epq.question.id and epq.personId=? and epq.examManage.id =? ## order by q.type asc,epq.order asc ";
		//获得考试人员
		public static final String queryExamPerson = "from ExamPerson where examManage.id = ? ";
		//错题人员
		public static final String worryPerson = "from ExamPersonQuestion as epq, ExamPerson as ep where epq.personId=ep.personId and  epq.examManage.id= ep.examManage.id "
				+ "and epq.question.id=? and epq.isRight=? and epq.examManage.id=? ";
		
		public static final String queryExamPersonCount ="from ExamPersonQuestion where examManage.id=?";
	}
	
	public static final class MySql {
		//生成试题的数目 检查是否已经生成试题目
		public static final String examPersonQuestionNum  = "select count(*) from P_ANSWER_EXAM_PERSON_QUESTION AS epq INNER JOIN P_ANSWER_QUESTION AS q "+
				"where epq.question_id = q.id and epq.person_id = '?' AND q.questionManage_id in ('##') and epq.examManage_id='@@'";

		//检查试题是否做完
		public static final String checkExamIsOver  = "select count(*) from P_ANSWER_EXAM_PERSON_QUESTION AS epq INNER JOIN P_ANSWER_QUESTION AS q "+
				"where epq.question_id = q.id and epq.person_id = '?' AND q.questionManage_id in ('##') and epq.examManage_id='@@' and epq.person_answer is null ";
		
		//试题做对的条数 type题型
		public static final String examRight  = "select count(*) from P_ANSWER_EXAM_PERSON_QUESTION AS epq INNER JOIN P_ANSWER_QUESTION AS q "+
				"where epq.question_id = q.id and epq.person_id = '?' and epq.examManage_id='&&' AND q.questionManage_id in ('##') and epq.is_right = '1'  and q.type_='@@' ";

		
		//查询是否可以考试  --查询做题权限
		//状态 1 时间在规定时间内
		public static final String queryExamOnline = "SELECT id,subject_,begin_time,end_time,exam_Time,type_ FROM (SELECT * from p_answer_exam_manage WHERE state_='1' AND NOW()>= BEGIN_TIME AND NOW()<=END_TIME ##) as paem "+
		"WHERE (paem.ORGFRAME_IDS = '' AND paem.ROLE_IDS = '' AND paem.USER_IDS = '') OR paem.ID in (@@)  order by paem.create_time desc ";
		
		//随机出题 状态1 正常 ；isRandom是随机 1
		public static final String randomExamQuestion = "SELECT ID,TITLE_,NUM_ FROM p_answer_question WHERE type_='@@' and state_='1' and is_Random='1' and "+
				"questionManage_Id in ('##') and NUM_ >= ("+
				"(SELECT MAX(NUM_) FROM p_answer_question WHERE type_='@@' and state_='1' and is_Random='1' and questionManage_Id in ('##'))-"+
				"(SELECT MIN(NUM_) FROM p_answer_question WHERE type_='@@' and state_='1' and is_Random='1' and questionManage_Id in ('##'))) * RAND() + "+
				"(SELECT MIN(NUM_) FROM p_answer_question WHERE type_='@@' and state_='1' and is_Random='1' and questionManage_Id in ('##')) order by NUM_ ASC LIMIT ?";
		
		//随机出题 状态1 正常 ；isRandom是随机 1(此方法如果上万条数据 再进行随机查询 会出现效率问题，由于上面随机查询小条数出现概率问题)
		public static final String randomExamQuestion2 = "SELECT t.ID,t.TITLE_,t.NUM_ FROM ("+
				"SELECT ID,TITLE_,NUM_ FROM p_answer_question WHERE type_='@@' and state_='1' and is_Random='1' and "+
				"questionManage_Id in ('##') ORDER BY RAND() LIMIT ?) as t ORDER BY t.NUM_ ASC";
		
		//我的已经考试的试卷
		public static final String myOnlineExam = "select paem.id,paem.subject_ ,paep.total_source,paep.consume_time,paem.TYPE_ from P_ANSWER_EXAM_PERSON as paep INNER JOIN p_answer_exam_manage as paem where paep.exammanage_id = paem.id and paep.person_id = ? and paep.is_exam='1' ORDER BY paem.CREATE_TIME DESC ";

		//易错题排行
		public static final String worryTitle = "select COUNT(*),epq.QUESTION_ID,aq.title_,aq.type_ "
				+ "from p_answer_exam_person_question AS epq,p_answer_question as aq,p_answer_exam_person as aep "+
				"WHERE epq.QUESTION_ID=aq.id and epq.person_Id=aep.PERSON_ID and epq.EXAMMANAGE_ID = aep.EXAMMANAGE_ID and epq.EXAMMANAGE_ID = ? AND epq.IS_RIGHT = '0' "
				+ " @@ "
				+ "GROUP BY  epq.QUESTION_ID ORDER BY COUNT(*) DESC";
		
		//删除人员答案
		public static final String cleanExamPersonQuestion="DELETE from p_answer_exam_person_question where EXAMMANAGE_ID='@@' and PERSON_ID='##'";
		
	}
	
}
