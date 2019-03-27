<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>开始考试</title>
</head>
<body>
<div class="ibox-content">
<input type="hidden" id="examManageId" name="examManageId" value="${examManageVo.id }">
<input type="hidden" id="consumeTime" name="consumeTime" value="${examPersonVo.consumeTime }">
<div class="well container">
	<div id="" class="text-center" style="font-size: 22px;">${examManageVo.subject }</div>
</div>
<div class="well" style="position:fixed; margin-left: 10%;width: 12%;">
	<div id="showTime" class="" style="font-size: 15px;"></div>
	<br/>
	<button type="button" class="btn btn-default btn-lg btn-block" id="pause" onclick="on_pause()">暂停</button>
	<br/>
	<div style="font-size: 15px;"><input type="checkbox" value="" name="" id="singleTitle"  onchange="on_singleTitle()">&nbsp;单题模式</div>
	<br/>
	<c:if test="${!empty examManageVo.examTime}">
		<div id="showTime" style="font-size: 15px;">时长：${examManageVo.examTime }分钟</div>
	</c:if>
	<c:if test="${empty examManageVo.examTime}">
		<div id="showTime" style="font-size: 15px;">不限时</div>
	</c:if>
</div>
<div style="padding-left: 25%;width: 89%">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
	<%-- 单选 --%>
	<div id="single" style="display: none;">
		<div class="well" >单选题 ${examManageVo.single }题 每小题 ${examManageVo.singleScore }分 共${examManageVo.singleTotalScore }分 </div>
		<div id="singleProblem"></div>
	</div>
	<%-- 多选 --%>
	<div id="many" style="display: none;">
		<div class="well" >多选题 ${examManageVo.many }题 每小题 ${examManageVo.manyScore }分 共${examManageVo.manyTotalScore }分 </div>
		<div id="manyProblem"></div>
	</div>
	<%-- 判断 --%>
	<div id="judge" style="display: none;">
		<div class="well" >判断 ${examManageVo.judge }题 每小题 ${examManageVo.judgeScore }分 共${examManageVo.judgeTotalScore }分 </div>
		<div id="judgeProblem"></div>
	</div>
	<%-- 填空 --%>
	<div id="fill" style="display: none;">
		<div class="well" >填空 ${examManageVo.fill }题 每小题 ${examManageVo.fillScore }分 共${examManageVo.fillTotalScore }分 </div>
		<div id="fillProblem"></div>	
	</div>
	<input type="hidden" name="examManageId" value="${examManageVo.id }">
	</form>
	<!-- 显示分页 -->	
	<div id="laypage" align="center"></div>	
	</br></br>
</div>	
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-primary " id="submit" type="button" onclick="on_submit()"><i class="fa fa-check"></i>&nbsp;提交</button>
<button class="btn btn-danger " type="button" onclick="on_closeWin()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
<%-- 基本表单js css --%>
<script type="text/javascript" src="/common/index/js/iCheck/icheck.min.js"></script>
<link rel="stylesheet" href="/common/index/css/iCheck/custom.css" type="text/css">
<%-- 分页控件start --%>
<script type="text/javascript" src="/common/plugins/laypage/laypage.js"></script>
<link rel="stylesheet" type="text/css" href="/common/plugins/laypage/skin/laypage.css" />
<%-- 分页控件end --%>
	
<script type="text/javascript"> 
var initSign = false;
var mark = '${mark}';//1：不显示答案  2：显示答案
var sign = '${sign}';//1：只显示错题
var chk = '${chk}';//生成试题是否成功
var redo = '${redo}';//1:在线考试 2:我的考试
var personId = '${personId}';//考试人员id
var intClock;
$(document).ready(function(){
	if(mark!=2)
		intClock = window.setInterval("showTime()","1000");//显示耗时
	if(chk!="true"){
		parent.layer.alert("试题提取失败，请检查试题库配置是否正确！", {
			closeBtn: 0,
			icon: 5
		},function(){
			parent.layer.closeAll(); //关闭当前弹出框
		});
	}
});

//显示耗时
var ss = 0;//秒
var consumeTime = '${examPersonVo.consumeTime}';//耗时
if(consumeTime!=null&&consumeTime!=""){
	ss = parseInt(consumeTime);
}else{
	ss = 0;
}
var examTime = '${examManageVo.examTime }';//限制时间
function showTime(){
	var html = "";
	if(ss<60){
		html = ss;
	}else{
		html = Math.floor(ss/60)+"分"+(ss%60)+"秒";
	}
	$("#consumeTime").val(ss);
	$("#showTime").html("<i class=\"fa fa-clock-o\"></i> 用时: "+html);
	ss +=1;
	if(examTime!=null&&examTime!=""){//如果you限制时间
		var time = parseInt(examTime)*60;
		if(ss==time){//限制时间到 自动 提交
			saveExam();//提交保存试卷
			submitExam();//提交试卷 计算分数
		}
	}
}

function initICheck(){
	//自定义样式
	 $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });
}

//初始化分页
var pageSize = 5;
var totalPage = 0;//页数
var page = 1;//当前页
initPager(page);
function initPager(n){
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/getOnlineExam?page='+n+'&rows='+pageSize+'&id=${examManageVo.id}&sign='+sign+'&personId='+personId,
		success : function(data){
			totalPage = Math.ceil(data.total/pageSize);
			examPager(data);//数据分页
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

//调用分页
laypage({
    cont: 'laypage',//显示的id位置
    pages: totalPage,//总页数
    skin: 'molv', //皮肤
    jump: function(obj){
    	if(initSign){
    		saveExam();//保存已经做的题目    	
    		page = obj.curr;
    		initPager(page);
    	}
    	initSign = true;
    }
});

//解析数据
function examPager(data){
	$("#single").hide();//隐藏
	$("#many").hide();//隐藏
	$("#judge").hide();//隐藏
	$("#fill").hide();//隐藏
	var singleHtml = "";//单选
	var manyHtml = "";//多选
	var judgeHtml = "";//判断
	var fillHtml = "";//填空
	for (var i = 0; i < data.rows.length; i++) {
		var problem = data.rows[i];
		if(problem.type==1){//单选
			$("#single").show();
			if(mark==2){
				if(problem.isRight==0){//答题不正确
					singleHtml += '<div class="ibox-title" style="font-size: 18px;background-color: #FFC1C1;">';
				}else{						
					singleHtml += '<div class="ibox-title" style="font-size: 18px;">';
				}
			}else{
				singleHtml += '<div class="ibox-title" style="font-size: 18px;">';
			}
			singleHtml += problem.order + '、' + problem.title;//题目
			singleHtml += '</div>';
			if(mark==2){
				if(problem.isRight==0){//答题不正确
					singleHtml += '<div class="ibox-content" style="font-size: 18px;background-color: #FFC1C1;">';//选项
				}else{
					singleHtml += '<div class="ibox-content" style="font-size: 18px;">';//选项
				}
			}else{
				singleHtml += '<div class="ibox-content" style="font-size: 18px;">';
			}
			for (var j = 0; j < problem.questionProblem.length; j++) {
				var option = problem.questionProblem[j];
				singleHtml += '<div class="radio i-checks">';
				if(problem.personAnswer==option.no){//已经做了的题目 会自动填上
					singleHtml += '<label><input checked="true" type="radio" value="'+option.no+'" name="single_'+problem.questionId+'" id="'+option.id+'">&nbsp;&nbsp;'+option.no+'、'+option.option+'</label>';
				}else{
					singleHtml += '<label><input type="radio" value="'+option.no+'" name="single_'+problem.questionId+'" id="'+option.id+'">&nbsp;&nbsp;'+option.no+'、'+option.option+'</label>';
				}
				singleHtml += '</div>';
			}
			if(mark==2){
				if(problem.isRight==0){//如果答题不正确 侧显示正确答案
					singleHtml += '<div style="font-size: 18px;">正确答案: '+problem.answer+'</div>';
				}
			}
			singleHtml += '</div>';
			singleHtml += '</br>';
		}
		if(problem.type==2){//多选
			$("#many").show();
			if(mark==2){
				if(problem.isRight==0){//答题不正确
					manyHtml += '<div class="ibox-title" style="font-size: 18px;background-color: #FFC1C1;">';
				}else{						
					manyHtml += '<div class="ibox-title" style="font-size: 18px;">';
				}
			}else{
				manyHtml += '<div class="ibox-title" style="font-size: 18px;">';
			}
			manyHtml += problem.order + '、' + problem.title;//题目
			manyHtml += '</div>';
			if(mark==2){
				if(problem.isRight==0){//答题不正确
					manyHtml += '<div class="ibox-content" style="font-size: 18px;background-color: #FFC1C1;">';
				}else{						
					manyHtml += '<div class="ibox-content" style="font-size: 18px;">';
				}
			}else{
				manyHtml += '<div class="ibox-content" style="font-size: 18px;">';
			}
			for (var j = 0; j < problem.questionProblem.length; j++) {
				var option = problem.questionProblem[j];
				manyHtml += '<div class="checkbox i-checks">';
				if(problem.personAnswer.indexOf(option.no)>-1){//已经做了的题目 会自动填上
					manyHtml += '<label><input checked="true" type="checkbox" value="'+option.no+'" name="many_'+problem.questionId+'" id="'+option.id+'">&nbsp;&nbsp;'+option.no+'、'+option.option+'</label>';
				}else{
					manyHtml += '<label><input type="checkbox" value="'+option.no+'" name="many_'+problem.questionId+'" id="'+option.id+'">&nbsp;&nbsp;'+option.no+'、'+option.option+'</label>';
				}
				manyHtml += '</div>';
			}
			if(mark==2){
				if(problem.isRight==0){//如果答题不正确 侧显示正确答案
					manyHtml += '<div style="font-size: 18px;">正确答案: '+problem.answer+'</div>';
				}
			}
			manyHtml += '</div>';
			manyHtml += '</br>';
		}
		if(problem.type==3){//判断
			$("#judge").show();
			if(mark==2){
				if(problem.isRight==0){//答题不正确
					judgeHtml += '<div class="ibox-title" style="font-size: 18px;background-color: #FFC1C1;">';
				}else{						
					judgeHtml += '<div class="ibox-title" style="font-size: 18px;">';
				}
			}else{
				judgeHtml += '<div class="ibox-title" style="font-size: 18px;">';
			}
			judgeHtml += problem.order + '、' + problem.title;//题目
			//如果已经有答案
			judgeHtml += '<span style="float: right;" class="form-inline">(<select class="form-control" name="judge_'+problem.questionId+'"><option>请选择</option>';
			if(problem.personAnswer=='1'){
				judgeHtml += '<option value="1" selected="selected">√</option>';
				judgeHtml += '<option value="0">X</option>';
			}else if(problem.personAnswer=='0'){
				judgeHtml += '<option value="1">√</option>';
				judgeHtml += '<option value="0" selected="selected">X</option>';
			}else{
				judgeHtml += '<option value="1">√</option>';
				judgeHtml += '<option value="0">X</option>';
			}
			judgeHtml += '</select>)</span>';
			if(mark==2){
				if(problem.isRight==0){//答题不正确
					if(problem.answer==1)
						judgeHtml += '<div style="font-size: 18px;">正确答案: √</div>';
					else
						judgeHtml += '<div style="font-size: 18px;">正确答案: X</div>';		
				}
			}
			judgeHtml += '</div>';
			judgeHtml += '</br>';
		}
		if(problem.type==4){//填空
			$("#fill").show();
			if(mark==2){
				if(problem.isRight==0){//答题不正确
					fillHtml += '<div class="ibox-title" style="font-size: 18px;background-color: #FFC1C1;">';
				}else{						
					fillHtml += '<div class="ibox-title" style="font-size: 18px;">';
				}
			}else{
				fillHtml += '<div class="ibox-title" style="font-size: 18px;">';
			}
			fillHtml += problem.order + '、' ;//题目
			//如果已经有答案
// 			var a = '<span style="text-decoration: none;" class="form-inline"><input type="text" name="fill_'+problem.questionId+'" value="'+problem.personAnswer+'" class="form-control" data-rule-rangelength="[1,64]"/></span>';
// 			fillHtml += problem.title.replace(new RegExp(/(##)/g),a);
            var title = problem.title;
        	var titlez = title.split("##");
        	var personAnswerz = problem.personAnswer.split(",");
        	for (var j = 0; j< titlez.length; j++) {
        		var a = "";
        		if(j<titlez.length-1){
        			var pa = personAnswerz[j];
        			if(pa==null||pa=="undefined"||pa=="null")
        				pa = "";
     				a = '<span style="text-decoration: none;" class="form-inline"><input type="text" name="fill_'+problem.questionId+'_'+j+'" value="'+pa+'" class="form-control" data-rule-rangelength="[1,64]"/></span>';
        		}
        		fillHtml += (titlez[j] + a);
        	}
			if(mark==2){
				if(problem.isRight==0){//答题不正确
					var answerFill = problem.answer;
					if(answerFill.indexOf(",")>0){//有多个选项
						var answerFillz = answerFill.split(",");
					    var answerF = "";
						for (var k = 0; k < answerFillz.length; k++) {
							answerF += (k+1)+". "+answerFillz[k]+"&nbsp;&nbsp;&nbsp;";
						}
						fillHtml += '<div style="font-size: 18px;">正确答案: '+answerF+'</div>';
					}else{
						fillHtml += '<div style="font-size: 18px;">正确答案: '+answerFill+'</div>';
					}
				}
			}
			fillHtml += '</div>';
			fillHtml += '</br>';
		}
	}
	$("#singleProblem").html(singleHtml);
	$("#manyProblem").html(manyHtml);
	$("#judgeProblem").html(judgeHtml);
	$("#fillProblem").html(fillHtml);
	initICheck();
	//显示答案 置灰色
	if(mark==2){
		$("#submit").hide();//置灰提交
		$("input[name^='single']").each(function(){
			this.disabled = true;
		});
		$("input[name^='many']").each(function(){
			this.disabled = true;
		});
		$("select").each(function(){
			this.disabled = true;
		});
		$("#pause").hide();//暂停
	}
}


/**
 * 提交前 先保存已经做了的题目 然后检查是否还有没有做的题目 最后是 提交试卷 计算分数
 */
function on_submit(){
	saveExam();//提交保存试卷
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/checkExamIsOver?questionManageIds=${examManageVo.questionManageIds}&examManageId=${examManageVo.id }',
		success : function(data){
			if(!data.result){
				parent.layer.confirm("题目还没完成，是否继续提交？!", {
					btn: ["确定","取消"] //按钮
				}, function(ind){
					parent.layer.close(ind);
					submitExam();//提交试卷 计算分数
				});
			}else{
				parent.layer.confirm("确定提交试卷？！", {
					btn: ["确定","取消"] //按钮
				}, function(ind){
					parent.layer.close(ind);
					submitExam();//提交试卷 计算分数
				});
			}
		}
	});
}

/**
 * 提交保存已经做了的题目
 */
function saveExam(){
	var dataJson = form2Json('baseForm');
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		data:encodeURI(dataJson),
		url: '/answer/saveOnlineExam',
		success : function(data){
			
		}
	});
}

/**
 * 提交试卷
 */
function submitExam(){
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/submitOnlineExam?examManageId=${examManageVo.id }&consumeTime='+ss,
		success : function(data){
			if(data.result){
				openResult();
			}else{
				autoAlert("提交失败，请检查！",5);
			}
		}
	});
}

//关闭前先记录下耗时
function on_closeWin(){
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/saveOnlineExamConsumeTime?examManageId=${examManageVo.id }&consumeTime='+ss,
		success : function(data){
			if(data.result){
				if(data.isExam){//已经考试就显示成绩
					openResult();
				}else{//只打开试卷 然后关闭 就不能显示成绩 直接关闭
					parent.layer.closeAll();					
				}
			}
		}
	});
}

//查看成绩
function openResult(){
	var indexP = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.open({
        type: 2,
        title: "考试成绩",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["50%", "60%"],
        content: "/answer/examOnline_result?id=${examManageVo.id }",
        success: function(layero, index){
        	//刷新页面
			if(redo==2){//我的考试成绩打开页面
				iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/answer/examOnline_myExamload"});//我的考试加载树下的列表
			}else{
				iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/answer/examOnline_load"});//加载树下的列表
			}
        	parent.layer.close(indexP);
        }
    });
}

//暂停
function on_pause(){
	window.clearInterval(intClock);
	parent.layer.confirm('喝喝水，先休息一下！', {
		closeBtn: 0,  
		btn: ['继续'] //按钮
	}, function(){
		parent.layer.msg('请继续做题....', {icon: 1});
		intClock = window.setInterval("showTime()","1000");
	});
}

//单题模式
function on_singleTitle(){
	if($('#singleTitle').is(':checked')) {//选中为单题模式
		pageSize = 1;
		initPager(1);
		laypage({
		 	cont: 'laypage', //容器。值支持id名、原生dom对象，jquery对象,
		   	pages: totalPage, //总页数
		   	skin: 'molv', //皮肤
		 	groups: 0,
		 	first: false,
		 	last: false,
		 	jump: function(obj){
		 		if(initSign){
		 			saveExam();//保存已经做的题目    	
		 			page = obj.curr;
		 			initPager(page);
		 		}
		 		initSign = true;
		      }
		});
	}else{//多题分页模式
		pageSize = 5;
		initPager(1);
		laypage({
		    cont: 'laypage',//显示的id位置
		    pages: totalPage,//总页数
		    skin: 'molv', //皮肤
		    jump: function(obj){
		    	if(initSign){
		    		saveExam();//保存已经做的题目    	
		    		page = obj.curr;
		    		initPager(page);
		    	}
		    	initSign = true;
		    }
		});
	}
}
</script>
</body>
</html>