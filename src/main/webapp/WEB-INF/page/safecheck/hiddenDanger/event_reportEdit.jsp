<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>上报事件</title>
</head>
<!-- 图片上传start -->
<link href="/common/plugins/webUploader/user/user_photo.css" rel="stylesheet">
<link href="/common/plugins/webUploader/webuploader.css" rel="stylesheet">
<script src="/common/plugins/webUploader/webuploader.js"></script>
<script type="text/javascript">
    var photoNum = 4;
    var attachType = "/event"; //附件类型,业务系统
</script>
<script src="/common/plugins/webUploader/photoByMany.js"></script>
<body>
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
   <input type="hidden" id="id" name="id" value="${eventInfoVo.id}" />
   <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${eventInfoVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
   <input type="hidden" id="sysCode" name="sysCode" value="${eventInfoVo.sysCode}" />
   <input type="hidden" id="creatorId" name="creatorId" value="${eventInfoVo.creatorId}" /> 
   <input type="hidden" id="creatorName" name=creatorName value="${eventInfoVo.creatorName}" /> 
   
   <input type="hidden" id="contactPhone" name="contactPhone" value="${eventInfoVo.contactPhone}" />
   <input type="hidden" id="reporterOrgId" name="reporterOrgId" value="${eventInfoVo.reporterOrgId}" />
   <input type="hidden" id="eventGPSX" name="eventGPSX" value="${eventInfoVo.eventGPSX}" />
   <input type="hidden" id="eventGPSY" name="eventGPSY" value="${eventInfoVo.eventGPSY}" />
   <input type="hidden" id="dataSource" name="dataSource" value="1" />
   
   <!-- 上报事件 start -->
   <div class="ibox-content">
       <div class="form-group">
           <label class="col-sm-2 control-label">隐患标题</label>
           <div class="col-sm-8">
               <input type="text" class="form-control" id="eventTitle" name="eventTitle" value="${eventInfoVo.eventTitle}"  data-rule-required="true" data-rule-rangelength="[1,50]" />
           </div>
       </div>
       <div class="form-group">
           <label class="col-sm-2 control-label">隐患类别</label>
           <div class="col-sm-3">
               <opt:select dictKey="event_Urgency" classStyle="form-control" name="eventurgency" id="eventurgency" value="1" />
           </div>
           <%-- <label class="col-sm-2 control-label">事件类型</label>
           <div class="col-sm-3">
               <opt:select dictKey="event_Type" classStyle="form-control" name="eventType" id="eventType" value="${eventInfoVo.eventType}" isDefSelect="true" />
           </div> --%>
       </div>
       <div class="form-group">
           <label class="col-sm-2 control-label">隐患地址</label>
           <div class="col-sm-8">
               <input type="text" class="form-control" id="eventAddress" name="eventAddress" value="${eventInfoVo.eventAddress}" data-rule-required="true" data-rule-rangelength="[1,100]" />
           </div>
       </div>
       <div class="form-group">
           <label class="col-sm-2 control-label">隐患详情</label>
           <div class="col-sm-8">
           	<textarea class="form-control" rows="3" id="eventContent" name="eventContent" data-rule-required="true" data-rule-rangelength="[1,250]">${eventInfoVo.eventContent}</textarea>
           </div>
       </div>
       <div class="form-group">
           <label class="col-sm-2 control-label">现场照片</label>
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
       </div>
    </div>   
    <!-- 上报事件 end -->
    
    <br><br>     
          
    <!-- 底部区域 -->
    <div class="footer edit_footer">
        <!-- 底部按钮 start-->
	        <div class="pull-right">
	            <button class="btn btn-primary " type="button"  id="onSave" onclick="on_save()">
	                <i class="fa fa-check"></i>&nbsp;提交
	            </button>
	            <button class="btn btn-danger " type="button" onclick="on_close()">
	                <i class="fa fa-close"></i>&nbsp;取消
	            </button>
	        </div>
        <!-- 底部按钮 end-->
    </div>
</form>
</body>
<script type="text/javascript">
	var winName = "${param.winName}";

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
	        url : '/safecheck/hiddenDanger/event_addReportEvent',
	        data : $('#baseForm').serialize(),
	        success : function(data) {
                if(uploader.getFiles().length>0){
                    uploader.options.server = '/attach_upload?formId='+data.id +'&attachType=event';
                    uploader.upload();//上传
                    uploader.on('uploadFinished', function() {
                        if (data.result) {
                            autoMsg("提交成功！", 1);
                            parent.frames[winName].$("#grid").bootstrapTable("refresh", {url : "/safecheck/hiddenDanger/event_loadReportList"});
                            parent.layer.close(index);
                        } else {
                            autoAlert("提交失败，请检查！", 5);
                        }
                    });
                }else{
                    if (data.result) {
                        autoMsg("提交成功！", 1);
                        parent.frames[winName].$("#grid").bootstrapTable("refresh", {url : "/safecheck/hiddenDanger/event_loadReportList"});
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