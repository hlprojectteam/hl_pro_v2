<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>批量新增考试人员</title>
</head>
<body>
<div class="ibox-content" style="padding-top:5px;">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
	<input type="hidden" name="id" value="${examManageId}">
   	<input type="hidden" id="userIds" name="userIds" value=""/>
   	<input type="hidden" id="userNames" name="userNames" value=""/>
</form>
<div class="row ibox-content" style="padding:5px 0 5px 0;">
	<form id="" method="post" class="form-horizontal" name="" action="">
	<div class="row">
    <label class="col-sm-1 control-label">姓名</label>
    <div class="col-sm-2">
      	<input type="text" class="form-control" id="personName" name="personName" value="" data-rule-rangelength="[1,10]"/>
    </div>
    <label class="col-sm-1 control-label">工号</label>
    <div class="col-sm-2">
      	<input type="text" class="form-control" id="jobNumber" name="jobNumber" value="" data-rule-rangelength="[1,10]"/>
    </div>
    <button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
   </div>
   </form>
</div>  
<div class="row ibox-content" style="padding:5px 0 5px 15px;">
	<table class="table table-bordered">
        <tr>
        	<th width="5%" style="text-align: center;"><input type="checkbox" onchange="on_selectAll(this)"></th>
            <th width="5%" style="text-align: center;">序号</th>
            <th width="18%" style="text-align: center;">姓名</th>
            <th width="17%" style="text-align: center;">登录名</th>
            <th width="10%" style="text-align: center;">工号</th>
            <th width="20%" style="text-align: center;">所属部门(站)</th>
            <th width="15%" style="text-align: center;">注册时间</th>
<!--             <th width="10%" style="text-align: center;">操作</th> -->
        </tr>
   	</table>
   </div>	
   	</br>
   	</br>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;批量保存新增考试人员</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript">
var examManageId = '${examManageId}';
var winName = '${winName}';
$(function(){
	on_search();
});

function on_search(){
	var personName = $("#personName").val();
	var jobNumber = $("#jobNumber").val();
	$(".table-bordered tr").each(function(){
		if($(this).children().get(1).innerText!="序号"){
			$(this).remove();//先删除所有行
		}
	});
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/examOnline_batchPerson?examManageId='+examManageId+'&personName='+personName+'&jobNumber='+jobNumber,
		success : function(data){
			var html = "";
			for (var i = 0; i < data.length; i++) {
				var user = data[i];
				html+='<tr>';
				html+='<td style="text-align: center;"><input type="checkbox"></td>';
				html+='<td style="text-align: center;">'+(i+1)+'</td>';
				html+='<td style="text-align: center;">'+user.userName+'<input type="hidden" id="" name="" value="'+user.id+'"/></td>';
				html+='<td style="text-align: center;">'+user.loginName+'</td>';
				html+='<td style="text-align: center;">'+user.jobNumber+'</td>';
				html+='<td style="text-align: center;">'+user.orgFrameNames+'</td>';
				html+='<td style="text-align: center;">'+user.createTime+'</td>';
// 				html+='<td style="text-align: center;"><a href="javaScript:void(0);" onclick="on_del(this)"><i class="fa fa-remove"></i>删除</a></td>';
				html+='</tr>';
			}
			$(".table-bordered").append(html);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

//选择所有
function on_selectAll(obj){
	$("input[type='checkbox']").prop('checked', $(obj).prop('checked'));
}

//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
        on_submit();//提交表单.  
    } 
}

function on_submit(){  
	var userIds = new Array();
	var userNames = new Array();
	var i = 0;
	$(".table-bordered tr").each(function(){
		if($(this).children().get(1).innerText!="序号"){
			if ($(this).find("td:eq(0) input").is(':checked')) {//只有选上
				userIds[i] = $(this).find("td:eq(2) input").val();
				userNames[i] = $(this).children().get(2).innerText;
				i++;
			}
		}
	});
	$("#userIds").val(userIds);
	$("#userNames").val(userNames);
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/examManage_saveAddPerson',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
				autoMsg("保存成功！",1);
				parent.frames[winName].$("#grid").bootstrapTable("refresh",{url:"/answer/examPerson_load?examManage.id="+examManageId});//加载树下的列表
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

//删除一行
function on_del(obj){
	$(obj).parent().parent().remove();//删除当前行
	var i = 0;
	$(".table-bordered tr").each(function(){
		if($(this).children().get(0).innerText!="序号"){
			$(this).children().get(0).innerText=i+1;
			i++;
		}
	});
}
</script>
</html>