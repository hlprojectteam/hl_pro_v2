<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>党建活动明细</title>
	  <!-- 图片放大插件样式 -->
    <link rel="stylesheet" href="/common/gis/event/css/viewer.min.css">
    <script src="/common/index/js/jquery-2.1.1.min.js"></script>
    <script src="/common/event/my/js/event_details.js"></script>
    <script type="text/javascript" src="/common/event/my/js/divscroll.js"></script>
    <script src="/common/plugins/layui/layui.js" charset="utf-8"></script>
    <script src="/common/plugins/validate/jquery.validate.js" type="text/javascript"></script>
    <script src="/common/js/base.js"></script>
    <script src="/common/js/form2Json.js"></script>
    <script src="/common/js/alert_tip.js"></script>
    <script src="/common/js/utils.js"></script>
</head>
<body>
<div id="" class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${alObject.id}" />		
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${alObject.createTime}'  pattern='yyyy-MM-dd HH:mm:ss'/>"/>
<input type="hidden" id="creatorId" name="creatorId" value="${alObject.creatorId}" />	
<input type="hidden" id="sysCode" name="sysCode" value="${alObject.sysCode}" />	
<input type="hidden" id="branchId" name="branchId" value="${alObject.branchId}" />	
<input type="hidden" id="activityId" name="activityId" value="${alObject.activityId}" />
<input type="hidden" id="status" name="status" value="${alObject.status}" />		
	<%-- 第1行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动标题</label>
	    <div class="col-sm-10">
	      <input type="text" class="form-control" id="title" name="title" placeholder="输入活动标题" value='${alObject.title}' data-rule-required="true" data-rule-rangelength="[1,50]" readonly="readonly"/>
	    </div>
  	</div>
  	<%-- 第2行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动开展地点</label>
	    <div class="col-sm-10">
	      <input type="text" class="form-control" id="launchAddress" name="launchAddress" placeholder="" value='${alObject.launchAddress}' readonly="readonly"/>
	    </div>
  	</div>
  	
  	<%-- 第3行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动开展时间</label>
	    <div class="col-sm-10">
	      <input type="text" class="form-control" id="launchDate" name="launchDate" placeholder="" value="<fmt:formatDate value='${alObject.launchDate}'  pattern='yyyy-MM-dd HH:mm:ss'/>"  readonly="readonly"/>
	    </div>
  	</div>
  	
  	<%-- 第4行 --%>
  	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>上报人</label>
	    <div class="col-sm-3">
	    	<input type="text" class="form-control" id="creatorName" name="creatorName" value='${alObject.creatorName}' readonly="readonly"/>  
	    </div>
	    
	    <label class="col-sm-2 control-label"><span style="color: red">*</span>上报人支部</label>
	    <div class="col-sm-3">
			<input type="text" class="form-control" id="branchName" name="branchName" value='${alObject.branchName}' readonly="readonly"/>    
		</div>
    </div>
  	
  	<%-- 第5行 --%>
  	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动开展频率</label>
	    <div class="col-sm-3">
	      <opt:select dictKey="dj_activities_frequency" classStyle="form-control" id="frequency" name="frequency"  value="${alObject.frequency}" disabled="true"/>
	    </div>

    </div>
 
    <%-- 第6行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动描述</label>
	    <div class="col-sm-10">
	       <textarea class="form-control" rows="4" cols="" id="launchContent" name="launchContent"  readonly="readonly">${alObject.launchContent}</textarea>
	    </div>
  	</div>
  	
  	<%-- 第7行 --%>
  	<div class="form-group">
	    <label class="col-sm-2 control-label">图片</label>
	    <div class="col-sm-4" style="width: 80%;left:-10px;margin-bottom: 10px;" >
			<c:if test="${not empty alObject.imgUrls}">
				<div id="uploaderDiv" class="wu-example dowebok">
					<c:forEach items="${alObject.imgUrls}" var="imgUrl" varStatus="">
                        <img data-original="${imgUrl}"  height="100px" width="100px" src="${imgUrl}" alt="" />
                    </c:forEach>
				</div> 
            </c:if>
	    </div>
    </div>
  	
  	<%-- 第9行 --%>
  	<div class="form-group">
	    <label class="col-sm-2 control-label"><span style="color: red">*</span>评审状态</label>
	    <div class="col-sm-3">
			<opt:select dictKey="dj_activity_status" classStyle="form-control" id="status1" name="status1" value="${alObject.status}" disabled="true"/>
		</div>
    </div>
  	
  	
     <%-- 第9行 --%>
  	<div class="form-group" id="div_exOpinion">
	    <label class="col-sm-2 control-label">评审记录</label>
		<div class="col-sm-10">
	       <textarea class="form-control" rows="4" cols="" id="exOpinion" name="exOpinion" readonly="readonly">${alObject.exOpinion}</textarea>
	    </div>
    </div>
    <%-- 第9行 --%>
  	<div class="form-group" id="div_opinion">
	    <label class="col-sm-2 control-label">评审意见</label>
	    <div class="col-sm-10">
			<input type="text" class="form-control" id="opinion" name="opinion" value='${alObject.opinion}' data-rule-rangelength="[1,100]"/>    
		</div>
    </div>
    <%-- 第9行 --%>
  	<div class="form-group" id="div_points">
	    <label class="col-sm-2 control-label"><span style="color: red">*</span>活动最终得分</label>
	    <div class="col-sm-1">
			<input type="text" class="form-control" id="points" name="points" value='${alObject.points}' data-rule-required="true" data-rule-rangelength="[1,2]"/>    
		</div>
    </div>
  	
  	
    <br>
  
  	<div class="form-group">
	</div>
	<div class="form-group">
	</div>
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_pass()"><i class="fa fa-check"></i>&nbsp;同意</button>
<button class="btn btn-danger " type="button" onclick="on_notPass()"><i class="fa fa-close"></i>&nbsp;不同意</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<!-- 图片放大插件 -->
<script src="/common/gis/event/js/viewer.min.js"></script>
<script src="/common/gis/event/js/viewer-jquery.min.js"></script>
<script type="text/javascript">
var _isPass;
$().ready(function() {
	var _status = '${alObject.status}';
	if(_status==0){
		//未评审
		document.getElementById("div_exOpinion").style.display="none"; 
		document.getElementById("div_points").style.display="none"; 
	}else if(_status==1){
		//初审通过
		document.getElementById("div_points").style.display="none"; 
	}else if(_status==2){
		//复审通过
		document.getElementById("div_opinion").style.display="none"; 
	}
	
});
//同意
function on_pass(){
	_isPass=1;
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
		parent.layer.confirm("操作是否确定？", {
				btn: ["确定","取消"] //按钮
			}, function(){
			on_submit();//提交表单.  
		});
       
    } 
}
//不同意
function on_notPass(){
	_isPass=0;
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
        parent.layer.confirm("操作是否确定？", {
				btn: ["确定","取消"] //按钮
			}, function(){
			on_submit();//提交表单.  
		});
    } 
}

function on_submit(){  
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/dangjian/activitiesLauchReview_Save?isPass='+_isPass,
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
					autoMsg("保存成功！",1);
					iframeIndex.$("#grid").bootstrapTable("refresh",{url: "/dangjian/activitiesLauch_reviewload?points=0"});//加载树下的列表
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
$(function(){
    /*附件图片预览放大*/
    $('.dowebok').viewer({
        url: 'data-original',
    });
    
});

</script>
</html>