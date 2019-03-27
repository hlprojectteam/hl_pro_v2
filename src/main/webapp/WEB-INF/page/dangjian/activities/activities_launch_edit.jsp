<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>党建活动编辑</title>
</head>
<!-- 图片上传start -->
<link href="/common/plugins/webUploader/user/user_photo.css" rel="stylesheet">
<link href="/common/plugins/webUploader/webuploader.css" rel="stylesheet">
<script src="/common/plugins/webUploader/webuploader.js"></script>
<script type="text/javascript">
    var photoNum = 4;
</script>
<script src="/common/plugins/webUploader/photoByMany.js"></script>
<body>
<div id="" class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${activitiesLaunchVo.id}" />		
<input type="hidden" id="createTime" name="createTime"  value='${activitiesLaunchVo.createTime}'/>
<input type="hidden" id="creatorId" name="creatorId" value="${activitiesLaunchVo.creatorId}" />	
<input type="hidden" id="sysCode" name="sysCode" value="${activitiesLaunchVo.sysCode}" />	
<input type="hidden" id="branchId" name="branchId" value="${activitiesLaunchVo.branchId}" />	
	<%-- 第1行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>选择活动</label>
	    <div class="col-sm-3">
	       <select id="activityId" name="activityId" value='${activitiesLaunchVo.activityId}' class="form-control m-b required"></select>
	    </div>
	    <label class="col-sm-2 control-label"><span style="color: red">*</span>活动开展时间</label>
	     <div class="col-sm-3">
            <input type="text" class="form-control" id="launchDate" name="launchDate" value="<fmt:formatDate value='${activitiesLaunchVo.launchDate}' pattern='yyyy-MM-dd HH:mm:ss'/>" data-rule-required="true" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>    
        </div>
  	</div>
  	<%-- 第2行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动开展地点</label>
	    <div class="col-sm-10">
	      <input type="text" class="form-control" id="launchAddress" name="launchAddress" placeholder="" value='${activitiesLaunchVo.launchAddress}' data-rule-rangelength="[1,100]"/>
	    </div>
  	</div>

  	
  	<%-- 第4行 --%>
  	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>上报人</label>
	    <div class="col-sm-3">
	    	<input type="text" class="form-control" id="creatorName" name="creatorName" value='${activitiesLaunchVo.creatorName}' data-rule-rangelength="[1,20]"/>  
	    </div>
	    
	    <label class="col-sm-2 control-label"><span style="color: red">*</span>上报人支部</label>
	    <div class="col-sm-3">
			<input type="text" class="form-control" id="order" name="branchName" value='${activitiesLaunchVo.branchName}' readonly="readonly"/>    
		</div>
    </div>
 
    <%-- 第6行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>活动描述</label>
	    <div class="col-sm-10">
	       <textarea class="form-control" rows="4" cols="" id="launchContent" name="launchContent" >${activitiesLaunchVo.launchContent}</textarea>
	    </div>
  	</div>
  	
  	<%-- 第8行 --%>
    <label class="col-sm-2 control-label">图片</label>
     <div class="col-sm-8">
         <div id="uploaderDiv" class="wu-example">
             <div class="queueList" style="height: 160px;width: 100%">
                 <div id="dndArea" class="placeholder">
                     <div id="filePicker"></div>
                     <p>最多可选4张</p>
                     <!--图片列表 -->
                 </div>
             </div>
             <div id="statusShow" class="statusBar">
                 <div class="info"></div>
                 <div class="btns">
                     <div id="filePickerAdd" class="fileAddBtn"></div>
                 </div>
             </div>
         </div>
         <%-- 照片预览 --%>
         <div id='blueimp-gallery' class='blueimp-gallery'><div class='slides'></div>
             <h3 class='title'>
             </h3><a class='prev'>‹</a><a class='next'>›</a><a class='close'>×</a><a class='play-pause'></a><ol class='indicator'></ol>
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
<button class="btn btn-primary " type="button" id="onSave" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<!-- 图片放大插件 -->
<script src="/common/gis/event/js/viewer.min.js"></script>
<script src="/common/gis/event/js/viewer-jquery.min.js"></script>
<script type="text/javascript">
$().ready(function() {
	
	getActivities();
});

//获得党支部下拉选项
function getActivities(){
	$.ajax({
		type:'post',
		async:false,
		dataType : 'json',
		url: '/dangjian/activities_load?page=1&&rows=30',
		success : function(data){
			$("#activityId").append("<option value=''>------请选择------</option>");
			if(data.rows!=null){
				 for (var i = 0; i < data.rows.length; i++) {
					$("#activityId").append("<option value='"+data.rows[i].id+"'>"+data.rows[i].title+"</option>");
				 }
			}
		},
	});
}

/**保存*/
function on_save() {
	if($("#baseForm").valid()){
	    $("#onSave").attr("disabled",true);
	    on_submit();
    }
}

/**提交表单*/
function on_submit() {
    $.ajax({
        type : 'post',
        async : false,
        dataType : 'json',
        url: '/dangjian/activitiesLauch_saveOrUpdate',
        data : $('#baseForm').serialize(),
        success : function(data) {
            if(uploader.getFiles().length>0){
                uploader.options.server = '/attach_upload?formId='+data.id +'&attachType=dj_activitiesLaunch';
                uploader.upload();//上传
                uploader.on('uploadFinished', function() {
                    if (data.result) {
                        autoMsg("提交成功！", 1);
                        parent.frames[winName].$("#grid").bootstrapTable("refresh", {url : "/dangjian/activitiesLauch_myload"});
                        parent.layer.close(index);
                    } else {
                        autoAlert("提交失败，请检查！", 5);
                    }
                });
            }else{
                if (data.result) {
                    autoMsg("提交成功！", 1);
                    parent.frames[winName].$("#grid").bootstrapTable("refresh", {url : "/dangjian/activitiesLauch_myload"});
                    parent.layer.close(index);
                } else {
                    autoAlert("提交失败，请检查！", 5);
                }
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            autoAlert("系统出错，请检查！", 5);
        }
    });
}

</script>
</html>