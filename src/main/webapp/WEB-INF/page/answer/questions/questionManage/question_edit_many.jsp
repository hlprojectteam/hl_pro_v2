<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>题目</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${questionVo.id}" />
<input type="hidden" id="questionManageId" name="questionManage.id" value="${questionManageId}" />
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value="${questionVo.createTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>"/>
<%-- 题目类型 2：多选 --%>
<input type="hidden" id="type" name="type" value="2" />	
    <%-- 第1行 --%>
	<div class="form-group">
   		<label class="col-sm-1 control-label"><span style="color: red">*</span>题目</label>
    	<div class="col-sm-11">
    		<textarea rows="6" class="form-control" id="title" name="title" data-rule-required="true" data-rule-rangelength="[1,2048]">${questionVo.title }</textarea>
   		</div>
	</div>
	<%-- 第2行 --%>
   	<div class="form-group">
   		<label class="col-sm-1 control-label"><span style="color: red">*</span>状态</label>
   		<div class="col-sm-1">
   			<opt:select dictKey="state" isDefSelect="false" id="state" name="state" value="${questionVo.state }" classStyle="form-control m-b required"/>
   		</div>
		<label class="col-sm-2 control-label"><span style="color: red">*</span>是否随机</label>
    	<div class="col-sm-1">
    		<opt:select dictKey="isNot" id="isRandom" name="isRandom" value="${questionVo.isRandom }" classStyle="form-control"/>
   		</div>
	</div>
	
	<div class="form-group">
   		<button class="btn btn-primary " id="addOption" type="button" onclick="on_add()"><i class="fa fa-plus"></i>&nbsp;增加选项</button>
   </div>
   <div class="form-group">
   		<table class="table table-bordered">
		        <tr>
		            <th width="4%">序号</th>
		            <th width="5%"><span style="color: red">*</span>题号</th>
		            <th><span style="color: red">*</span>选项</th>
		            <th>是否答案</th>
		            <th>操作</th>
		        </tr>
		        <c:forEach var="questionProblem" items="${questionVo.questionProblems}" varStatus="status">
		        	<tr>
		        		<td width="4%">${status.index+1}<input type="hidden" id="" name="problems[${status.index}].id" value="${questionProblem.id}"/></td>
		        		<td width="5%"><input id="" type="text" name="problems[${status.index}].no" value="${questionProblem.no}" class="form-control" data-rule-required="true" data-rule-rangelength="[1,1]" data-msg-rangelength="请输入大写字母" data-rule-englishbig="true"/></td>
		        		<td><input id="" type="text" name="problems[${status.index}].option" value="${questionProblem.option}" class="form-control" data-rule-required="true" data-rule-rangelength="[1,1024]"/></td>
		        		<td width="10%"><opt:select dictKey="isNot" id="" name="problems[${status.index}].answer" value="${questionProblem.answer}" classStyle="form-control" /></td>
		        		<td><a href="javaScript:void(0);" onclick="on_del(this)"><i class="fa fa-remove"></i>删除</a></td>
		        	</tr>   
		        </c:forEach>
   		</table>
   </div>
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript">
var winName = '${winName}';
$(function(){
	var id = $("#id").val();
	if(id!=""&&id!=null){
		$("#addOption").attr("disabled",true);//置灰
		$(".table-bordered tr").find("a").each(function(){
			$(this).attr("onclick","");
		});
	}
});

//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
        on_submit();//提交表单.  
    } 
}

//增加一行 增加行
function on_add(){
	var romNum = $(".table-bordered tr").length-1;//行数
	var html = '<tr>';
	html += '<td width="4%">'+(romNum+1)+'<input type="hidden" id="" name="problems['+romNum+'].id" /></td>';
	html += '<td width="5%"><input id="" type="text" name="problems['+romNum+'].no" value="'+changeNo(romNum+1)+'" class="form-control" data-rule-required="true" data-rule-rangelength="[1,1]" data-msg-rangelength="请输入大写字母" data-rule-englishbig="true"/></td>';
	html += '<td><input id="" type="text" name="problems['+romNum+'].option" value="" class="form-control" data-rule-required="true" data-rule-rangelength="[1,1024]"/></td>';
	html += '<td width="8%"><select id="" class="form-control" name="problems['+romNum+'].answer"><option selected="selected" value="0">否</option><option value="1">是</option></select></td>';
	html += '<td><a href="javaScript:void(0);" onclick="on_del(this)"><i class="fa fa-remove"></i>删除</a></td>';
	html += '</tr>';
	$(".table-bordered").append(html);
}
//删除一行
function on_del(obj){
	$(obj).parent().parent().remove();//删除当前行
	var i = 0;
	$(".table-bordered tr").each(function(){
		if($(this).children().get(0).innerText!="序号"){
			$(this).children().get(0).innerText=i+1;
			$(this).find("td:eq(1) input").attr("name","problems["+i+"].no");
			$(this).find("td:eq(2) input").attr("name","problems["+i+"].option");
			$(this).find("td:eq(3) input").attr("name","problems["+i+"].answer");
			i++;
		}
	});
}

function on_submit(){
	var num = 0;
	$(".table-bordered tr").each(function(){
		if($(this).children().get(0).innerText!="序号"){
			num += parseInt($(this).find("td:eq(3) select option:selected").val());
		}
	});
	if(num==0){
		autoAlert("请选择一个或多个答案选项！",5);
	}else{
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/answer/question_saveOrUpdate',
			data:$('#baseForm').serialize(),
			success : function(data){
				if(data.result){
					autoMsg("保存成功！",1);
					parent.frames[winName].$("#gridMany").bootstrapTable("refresh",{url:"/answer/question_load?type=2&questionManageId=${questionManageId}"});//加载树下的列表
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
}

//输入数据返回英文
function changeNo(num){
	switch(num){
		case 1:return "A"; break;
		case 2:return "B"; break;
		case 3:return "C"; break;
		case 4:return "D"; break;
		case 5:return "E"; break;
		case 6:return "F"; break;
		case 7:return "G"; break;
		case 8:return "H"; break;
		case 9:return "I"; break;
		case 10:return "J"; break;
		case 11:return "K"; break;
		case 12:return "L"; break;
		default:return "";
	}
}
</script>
</html>