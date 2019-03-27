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
<input type="hidden" id="id" name="id" value="${activitiesLaunchVo.id}" />		
<input type="hidden" id="createTime" name="createTime"  value='${activitiesLaunchVo.createTime}'/>
<input type="hidden" id="creatorId" name="creatorId" value="${activitiesLaunchVo.creatorId}" />	
<input type="hidden" id="sysCode" name="sysCode" value="${activitiesLaunchVo.sysCode}" />	
<input type="hidden" id="branchId" name="branchId" value="${activitiesLaunchVo.branchId}" />	
<input type="hidden" id="activityId" name="activityId" value="${activitiesLaunchVo.activityId}" />		
	<%-- 第1行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动标题</label>
	    <div class="col-sm-10">
	      <input type="text" class="form-control" id="title" name="title" placeholder="输入活动标题" value='${activitiesLaunchVo.title}' data-rule-required="true" data-rule-rangelength="[1,50]" readonly="readonly"/>
	    </div>
  	</div>
  	<%-- 第2行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动开展地点</label>
	    <div class="col-sm-10">
	      <input type="text" class="form-control" id="launchAddress" name="launchAddress" placeholder="" value='${activitiesLaunchVo.launchAddress}' readonly="readonly"/>
	    </div>
  	</div>
  	
  	<%-- 第3行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动开展时间</label>
	    <div class="col-sm-10">
	      <input type="text" class="form-control" id="launchDate" name="launchDate" placeholder="" value='${activitiesLaunchVo.launchDate}' readonly="readonly"/>
	    </div>
  	</div>
  	
  	<%-- 第4行 --%>
  	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>上报人</label>
	    <div class="col-sm-3">
	    	<input type="text" class="form-control" id="creatorName" name="creatorName" value='${activitiesLaunchVo.creatorName}' readonly="readonly"/>  
	    </div>
	    
	    <label class="col-sm-2 control-label"><span style="color: red">*</span>上报人支部</label>
	    <div class="col-sm-3">
			<input type="text" class="form-control" id="order" name="branchName" value='${activitiesLaunchVo.branchName}' readonly="readonly"/>    
		</div>
    </div>
  	
  	<%-- 第5行 --%>
  	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动开展频率</label>
	    <div class="col-sm-3">
	      <opt:select dictKey="dj_activities_frequency" classStyle="form-control" id="frequency" name="frequency"  value="${activitiesLaunchVo.frequency}" disabled="true"/>
	    </div>

    </div>
 
    <%-- 第6行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动描述</label>
	    <div class="col-sm-10">
	       <textarea class="form-control" rows="4" cols="" id="launchContent" name="launchContent"  readonly="readonly">${activitiesLaunchVo.launchContent}</textarea>
	    </div>
  	</div>
  	
  	<%-- 第7行 --%>
  	<div class="form-group">
	    <label class="col-sm-2 control-label"><span style="color: red">*</span>活动最终得分</label>
	    <div class="col-sm-1">
			<input type="text" class="form-control" id="points" name="points" value='${activitiesLaunchVo.points}' data-rule-required="true" data-rule-rangelength="[1,2]"/>    
		</div>
    </div>
  	
  	<%-- 第8行 --%>
    <label class="col-sm-2 control-label">图片</label>
    <div class="col-sm-4" style="width: 80%;left:-10px;margin-bottom: 10px;" >
			<c:if test="${not empty activitiesLaunchVo.imgUrls}">
				<div id="uploaderDiv" class="wu-example dowebok">
					<c:forEach items="${activitiesLaunchVo.imgUrls}" var="imgUrl" varStatus="">
                        <img data-original="${imgUrl}"  height="200px" width="200px" src="${imgUrl}" alt="" />
                    </c:forEach>
				</div> 
            </c:if>
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
<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<!-- 图片放大插件 -->
<script src="/common/gis/event/js/viewer.min.js"></script>
<script src="/common/gis/event/js/viewer-jquery.min.js"></script>
<script type="text/javascript">
$().ready(function() {
});
//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
        on_submit();//提交表单.  
    } 
}

function on_submit(){  
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/dangjian/activitiesLauch_saveOrUpdate',
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