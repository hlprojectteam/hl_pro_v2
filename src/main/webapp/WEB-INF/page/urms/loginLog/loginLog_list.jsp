<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
	<title>用户列表</title>
</head>
<script src="/common/js/utils.js"></script>

<body>
<div class="ibox-content" style="padding-top: 5px;">
<c:if test="${userType ne 3}">
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
		    <label class="col-sm-1 control-label">登录账号</label>
		    <div class="col-sm-2">
		      	<input type="text" class="form-control" id="loginAccount" name="loginAccount" value="" />
		    </div>
			  <label class="col-sm-1 control-label">登录方式</label>
			  <div class="col-sm-2">
				  <select class="form-control" name="loginSource">
					  <option value="">--请选择--</option>
					  <option value="1">电脑</option>
					  <option value="2">APP</option>
					  <option value="3">微信</option>
					  <option value="4">其他</option>
				  </select>
			  </div>
			  <%--<label class="col-sm-1 control-label">登录时间(开始)</label>--%>
			  <%--<div class="col-sm-2">--%>
				  <%--<input type="text" class="form-control" name="loginTime" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />--%>
			  <%--</div>--%>
			  <%--<label class="col-sm-1 control-label">登录时间(结束)</label>--%>
			  <%--<div class="col-sm-2">--%>
				  <%--<input type="text" class="form-control" name="loginTime" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />--%>
			  <%--</div>--%>
			<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
		  </div>
		</form>
	</div>
</c:if>
	<div class="row ibox-content">

		<div class="col-sm-12">
			<table id="grid"></table>
		</div>
	</div>

</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	loadGrid();//加载列表
});


//加载列表
function loadGrid() {
	$("#grid").bootstrapTable({
		url: "/urms/loginLog_load",
		dataType:"json",
		method:"post",
		queryParamsType: "limit",//服务器分页必须
		striped:true,//条纹行
		contentType: "application/x-www-form-urlencoded",
		pageSize:20,//每页大小
		pageNumber:1,//开始页
		pageList:[10,20,50],
		pagination:true,//显示分页工具栏
		sidePagination: "server", //服务端请求
		queryParams: queryParams,//发送请求参数
	    columns: [//{checkbox:true},
	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	        	  		return index+1;
	              }},
	              {title: "登录账号",field: "loginAccount",width:120,align:"center"},
	              {title: "姓名",field: "loginName",width:120,align:"center"},
	              {title: "登录ip",field: "loginIp",align:"center"},
	              {title: "登录地址", field: "loginAddress",align:"center"},
	              {title: "登录时间", field: "loginTime",align:"center",formatter:function(value,row,index){
                      if(value != null){
                          return changeDateFormat(value.time);
                      }else{
                          return "-";
                      }
                  }},
	              {title: "退出时间", field: "logOutTime",align:"center",formatter:function(value,row,index){
	                  if(value != null){
                          return changeDateFormat(value.time);
					  }else{
	                      return "-";
					  }

                  }},
					{title: "在线时长", field: "onlineTime", align:"center",formatter:function(value,row,index){
						return format(value);
					}},
	              {title: "登录方式", field: "loginSource", width: 100,align:"center",formatter:function(value,row,index){
                      if(value == 1){
                          if(row.loginState == 1){
                              return "<i class=\"fa fa-desktop\" aria-hidden=\"true\" style=\"color: #46BE8A\"></i> 电脑";
						  }else{
                              return "<i class=\"fa fa-desktop\" aria-hidden=\"true\"></i> 电脑";
                          }
                      }
                      if(value == 2){
                          if(row.loginState == 1){
                              return "<i class=\"fa fa-tablet\" aria-hidden=\"true\" style=\"color: #46BE8A\"></i> APP";
                          }else{
                              return "<i class=\"fa fa-tablet\" aria-hidden=\"true\"></i> APP";
                          }
                      }
                      if(value == 3){
                          if(row.loginState == 1){
                              return "<i class=\"fa fa-weixin\" aria-hidden=\"true\" style=\"color: #46BE8A\"></i> 微信";
                          }else{
                              return "<i class=\"fa fa-weixin\" aria-hidden=\"true\"></i> 微信";
                          }
                      }
                      if(value == 4){
                          return "其它";
                      }
	              }},

				  {title: "当前状态", field: "loginState", width: 100,align:"center",formatter:function(value,row,index){
				      if(value == 1){
                          return "<i class=\"fa fa-circle\" style=\"color: #46BE8A\"></i> 在线";
					  }else{
                          return "<i class=\"fa fa-circle\" style=\"color: #a8bbc2\"></i> 离线";
					  }

				  }}]
	 });
}



//查询
function on_search(){
	$("#grid").bootstrapTable("refresh");
}

function format(a){
    if(a<=0){
        return "-";
	}else{
        return MillisecondToDate(a);
	}
}
</script>
</html>