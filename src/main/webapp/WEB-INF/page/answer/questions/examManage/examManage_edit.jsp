<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>考试管理</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${examManageVo.id}" />	
<input type="hidden" id="sysCode" name="sysCode" value="${examManageVo.sysCode}" />	
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value="${examManageVo.createTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>"/>

    <%-- 第1行 --%>
	<div class="form-group">
   		<label class="col-sm-1 control-label"><span style="color: red">*</span>主题</label>
    	<div class="col-sm-9">
      		<input type="text" class="form-control" id="subject" name="subject" value="${examManageVo.subject }" data-rule-required="true" data-rule-rangelength="[1,128]"/>
   		</div>
   		<%-- 此选择暂时停用 --%>
<!--    		<label class="col-sm-2 control-label">提交后显示成绩</label> -->
<!--    		<div class="col-sm-1"> -->
<!--    			<opt:select dictKey="isNot" classStyle="form-control" id="isShow" name="isShow" value="${examManageVo.isShow }"/> -->
<!--    		</div> -->
	</div>
	<div class="form-group">
   		<label class="col-sm-1 control-label">考试类型</label>
   		<div class="col-sm-2">
   			<opt:select dictKey="exam_type" classStyle="form-control" id="type" name="type" value="${examManageVo.type }"/>
   		</div> 
		<label class="col-sm-1 control-label">做题时长(分钟)</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="examTime" name="examTime" value="${examManageVo.examTime}" data-rule-number="true" data-rule-rangelength="[1,3]"/>
		</div>
		<label class="col-sm-1 control-label"><span style="color: red">*</span>状态</label>
		<div class="col-sm-2">
			<opt:select dictKey="answer_state" id="state" name="state" value="${examManageVo.state}" classStyle="form-control"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-1 control-label">单选(道)</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="single" name="single" value="${examManageVo.single}" data-rule-digits="true" data-rule-rangelength="[1,3]" data-rule-max="200" onchange="on_changeSource()"/>
		</div>
		<label class="col-sm-1 control-label">单选每题分值</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="singleScore" name="singleScore" value="${examManageVo.singleScore}" data-rule-number="true" data-rule-rangelength="[1,4]" onchange="on_changeSource()"/>
		</div>
		<label class="col-sm-1 control-label">多选(道)</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="many" name="many" value="${examManageVo.many}" data-rule-digits="true" data-rule-rangelength="[1,3]" data-rule-max="200" onchange="on_changeSource()"/>
		</div>
		<label class="col-sm-1 control-label">多选每题分值</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="manyScore" name="manyScore" value="${examManageVo.manyScore}" data-rule-number="true" data-rule-rangelength="[1,4]" onchange="on_changeSource()"/>
		</div>
	</div>	
	<div class="form-group">		
		<label class="col-sm-1 control-label">判断(道)</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="judge" name="judge" value="${examManageVo.judge}" data-rule-digits="true" data-rule-rangelength="[1,3]" data-rule-max="200" onchange="on_changeSource()"/>
		</div>
		<label class="col-sm-1 control-label">判断每题分值</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="judgeScore" name="judgeScore" value="${examManageVo.judgeScore}" data-rule-number="true" data-rule-rangelength="[1,4]" onchange="on_changeSource()"/>
		</div>
		<label class="col-sm-1 control-label">填空(道)</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="fill" name="fill" value="${examManageVo.fill}" data-rule-digits="true" data-rule-rangelength="[1,3]" data-rule-max="200" onchange="on_changeSource()"/>
		</div>
		<label class="col-sm-1 control-label">填空每题分值</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="fillScore" name="fillScore" value="${examManageVo.fillScore}" data-rule-number="true" data-rule-rangelength="[1,4]" onchange="on_changeSource()"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-1 control-label"><span style="color: red">*</span>总分</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="score" name="score" value="${examManageVo.score}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,3]" onfocus="this.blur()" data-msg-digits="总分必须是整数，请检查！"/>
		</div>
		<label class="col-sm-1 control-label"><span style="color: red">*</span>及格分数</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="passScore" name="passScore" value="${examManageVo.passScore}" data-rule-required="true" data-rule-number="true" data-rule-rangelength="[1,3]" />
		</div>
		<label class="col-sm-1 control-label"><span style="color: red">*</span>统计分段</label>
		<div class="col-sm-2">
			<input type="text" class="form-control" id="subsection" name="subsection" value="${examManageVo.subsection}" data-rule-required="true" data-rule-number="true" data-rule-rangelength="[1,1]" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"><span style="color: red">*</span>开始时间（考试时间范围）</label>
		<div class="col-sm-3">
			<input type="text" class="form-control" id="beginTime" name="beginTime" value="<fmt:formatDate value='${examManageVo.beginTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" onfocus="this.blur();chkDate();" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-rule-required="true"/>
		</div>
		<label class="col-sm-2 control-label"><span style="color: red">*</span>结束时间（考试时间范围）</label>
		<div class="col-sm-3">
			<input type="text" class="form-control" id="endTime" name="endTime" value="<fmt:formatDate value='${examManageVo.endTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" onfocus="this.blur();chkDate();" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" data-rule-required="true" />
		</div>
    </div> 
    <div class="form-group">
    	<label class="col-sm-2 control-label"><span style="color: red">*</span>题库</label>
    	<div class="col-sm-6">
    		<input type="hidden" id="questionManageIds" name="questionManageIds" value="${examManageVo.questionManageIds }"/>
    		<input type="text" id="questionManageNames" name="questionManageNames" value="${examManageVo.questionManageNames }" class="form-control" data-rule-required="true" onfocus="this.blur()"/>
    	</div>
    	<div class="col-sm-1">
	    	<button class="btn btn-primary " id="chooseRuestion" type="button" onclick="choose_question('questionManageIds','questionManageNames')"><i class="fa fa-external-link"></i>&nbsp;选择题库</button>
	   </div>
	   <div class="col-sm-1">
	    	<button class="btn btn-primary " id="chooseClaen" type="button" onclick="choose_claen()"><i class="fa fa-trash-o"></i>&nbsp;清除题库</button>
    	</div>
    </div>
	<div class="form-group" id="selectUser">
	    <label class="col-sm-2 control-label">人员选择</label>
	    <div class="col-sm-8">
	    	<input type="hidden" id="userIds" name="userIds" value="${examManageVo.userIds }"/>
	    	<textarea class="form-control" rows="4" id="userNames" name="userNames" onfocus="this.blur()">${examManageVo.userNames }</textarea>
	    </div>
	    <div class="col-sm-2">
	    	<button class="btn btn-primary " id="chooseUser" type="button" onclick="choose_user('userIds','userNames','many')"><i class="fa fa-external-link"></i>&nbsp;选择人员</button>
	    </div>
	</div>
	<div class="form-group" id="selectRole">
	    <label class="col-sm-2 control-label">角色选择</label>
	    <div class="col-sm-8">
	    	<input type="hidden" id="roleIds" name="roleIds" value="${examManageVo.roleIds }"/>
	    	<textarea class="form-control" rows="4" id="roleNames" name="roleNames" onfocus="this.blur()">${examManageVo.roleNames }</textarea>
	    </div>
	    <div class="col-sm-2">
	    	<button class="btn btn-primary " id="chooseRole" type="button" onclick="choose_role('roleIds','roleNames')"><i class="fa fa-external-link"></i>&nbsp;选择角色</button>
		</div>
	</div>
	<div class="form-group" id="selectOrgFrame">
	    <label class="col-sm-2 control-label">组织机构选择</label>
	    <div class="col-sm-8">
	    	<input type="hidden" id="orgFrameIds" name="orgFrameIds" value="${examManageVo.orgFrameIds }"/>
	    	<textarea class="form-control" rows="3" id="orgFrameNames" name="orgFrameNames" onfocus="this.blur()">${examManageVo.orgFrameNames }</textarea>
	    </div>
	    <div class="col-sm-2">
	    	<button class="btn btn-primary " id="chooseOrgFrame" type="button" onclick="choose_orgFrame('orgFrameIds','orgFrameNames')"><i class="fa fa-external-link"></i>&nbsp;选择组织机构</button>
		</div>
	</div>
	</br></br>
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<c:if test="${count<1}">
	<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
</c:if>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript">
$(function(){
	var count = '${count}';
	if(count>0){
		autoMsg("试题已经生成，不能修改！",1);
	}
});

//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。
		if(chkDate()){
			if(chkPerson())
	        	on_submit();//提交表单.  		
		}
    } 
}

//检查时间
function chkDate(){
    var startDate = $("#beginTime").val();
    var	endDate = $("#endTime").val();
    if(startDate!=""&&endDate!=""){
	    var startDateTemp = startDate.split(" ");   
	    var endDateTemp = endDate.split(" ");   
	    var arrStartDate = startDateTemp[0].split("-");   
	    var arrEndDate = endDateTemp[0].split("-");   
	    var arrStartTime = startDateTemp[1].split(":");   
	    var arrEndTime = endDateTemp[1].split(":");   
		var allStartDate = new Date(arrStartDate[0], arrStartDate[1], arrStartDate[2], arrStartTime[0], arrStartTime[1], arrStartTime[2]);   
		var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2], arrEndTime[0], arrEndTime[1], arrEndTime[2]);   
		if (allStartDate.getTime() >= allEndDate.getTime()) {   
			autoMsg("开始时间不能大于结束时间！",5);
	    	return false;
	    }else{
	    	return true;	
	    }
    }
}

//检查人员选择 3选1
function chkPerson(){
	var userIds = $("#userIds").val();
	var roleIds = $("#roleIds").val();
	var orgFrameIds = $("#orgFrameIds").val();
	if(userIds==""&&roleIds==""&&orgFrameIds==""){
		autoMsg("请选择人员、角色或者组织（3选1）",5);
		return false;
	}else{
		return true;
	}
}

function on_submit(){  
	layer.load(0, {shade: 0.6});//加载层
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/examManage_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
				autoMsg("保存成功！",1);
				iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/answer/examManage_load"});//加载树下的列表
				parent.layer.close(index);
			}else{
				autoAlert("保存失败，请检查！",5);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

//题库选择
function choose_question(id,name){
	parent.layer.open({
        type: 2,
        title: "题库选择",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["70%", "100%"],
        content: "/answer/questionManage_choose?winName="+window.name+"&id="+id+"&name="+name
    });
}
//清除题库
function choose_claen(){
	$("#questionManageIds").val("");
	$("#questionManageNames").val("");
}

//算总分
function on_changeSource(){
	var score = parseInt(0);//总分
	var single = $("#single").val();
	var singleScore = $("#singleScore").val();
	var many = $("#many").val();
	var manyScore = $("#manyScore").val();
	var judge = $("#judge").val();
	var judgeScore = $("#judgeScore").val();
	var fill = $("#fill").val();
	var fillScore = $("#fillScore").val();
	if(single!=""&&singleScore!="")
		score += parseFloat(single*singleScore);
	if(many!=""&&manyScore!="")
		score += parseFloat(many*manyScore);
	if(judge!=""&&judgeScore!="")
		score += parseFloat(judge*judgeScore);
	if(fill!=""&&fillScore!="")
		score += parseFloat(fill*fillScore);
	$("#score").val(score);
}

</script>
</html>