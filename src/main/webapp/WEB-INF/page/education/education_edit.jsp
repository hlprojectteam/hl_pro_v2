<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>教育培训材料编辑</title>
</head>
<body>
<div id="" class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="delFlag" name="delFlag" value="${educationVo.delFlag}" />
<input type="hidden" id="cover" name="cover" value="${educationVo.cover}" />
<input type="hidden" id="status" name="status" value="${educationVo.status}" />
<input type="hidden" id="visitNum" name="visitNum" value="${educationVo.visitNum}" />
<input type="hidden" id="downloadNum" name="downloadNum" value="${educationVo.downloadNum}" />
<input type="hidden" id="type" name="type" value="${educationVo.type}" />
<input type="hidden" id="id" name="id" value="${educationVo.id}" />		
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${educationVo.createTime}'  pattern='yyyy-MM-dd HH:mm:ss'/>"/>
<input type="hidden" id="createUserId" name="createUserId" value="${educationVo.createUserId}" />	
<input type="hidden" id="creatorId" name="creatorId" value="${educationVo.creatorId}" />	
<input type="hidden" id="creatorName" name="creatorName" value="${educationVo.creatorName}" />	
	<%-- 第1行 --%>
	<div class="form-group">
  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id=title name="title" value="${educationVo.title}" data-rule-required="true" data-rule-rangelength="[1,100]" />
    </div>
  	</div>
  	<%-- 第2行 --%>
  	<div class="form-group">
  	<label class="col-sm-2 control-label"><span style="color: red">*</span>作者</label>
     <div class="col-sm-3">
      <input type="text" class="form-control" id="authorName" name="authorName" value="${educationVo.authorName}" data-rule-required="true" data-rule-rangelength="[1,20]" />
     </div>
  	  <label class="col-sm-2 control-label">分类</label>
    <div class="col-sm-3">
      <opt:select dictKey="Education_Category" classStyle="form-control" id="category" name="category" value="${educationVo.category }" />
    </div>
    </div>
    <%-- 第3行 --%>
  	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>发布时间</label>
	    <div class="col-sm-3">
			<input type="text" class="form-control" id="releaseDateStr" name="releaseDateStr" value="<fmt:formatDate value='${educationVo.releaseDate}' pattern='yyyy-MM-dd'/>" data-rule-required="true" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>    
		</div>
		
		<label class="col-sm-2 control-label">缩略图</label>
	    <div class="col-sm-3">
	    	<%-- 照片 --%>
				<div id="uploaderDiv" class="wu-example">
					<div class="queueList">
						<div id="dndArea" class="placeholder"> 
						    <div id="filePicker"></div>
							<!-- 图片列表 -->
						</div>
					</div>
				</div>
			<%-- 照片 --%>	
	    </div>
    </div>
    <%-- 第4行 --%>
  	<div class="form-group">
  	<label class="col-sm-2 control-label"><span style="color: red">*</span>内容介绍</label>
    <div class="col-sm-10">
    	<textarea class="form-control" id="description" name="description"  data-rule-required="true">${educationVo.description}</textarea>
    </div>
  	</div>
  	
  	
  	<%-- 第5行 --%>
     <div class="form-group">
	    <label class="col-sm-2 control-label">附件</label>
	    <!-- 图书文件 -->
			<div class="col-sm-10">
				<div id="uploaderFile" class="wu-example">
					<div id="thelistFile" class="uploader-list"></div>
						<div>
							<div id="pickerFile">选择文件</div>
							<table class="table table-bordered" id="attachTable">
								<tr>
									<th style="text-align: center;width:5%" />
									<th style="text-align: center;width:25%">文件名称</th>
									<th style="text-align: center;width:13%">文件大小</th>
									<th style="text-align: center;width:12%">来源</th>
									<th style="text-align: center;width:15%">上传时间</th>
									<th style="text-align: center;width:15%">操作</th>
								</tr>
						   </table>
				        </div>
			    </div>
		  </div>
     </div>
  	
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
<!-- 文件上传start -->
<link href="/common/plugins/webUploader/user/user_photo.css" rel="stylesheet">
<link href="/common/plugins/webUploader/webuploader.css" rel="stylesheet">
<script src="/common/plugins/webUploader/webuploader.js"></script>
<script src="/common/js/attach.js"></script>
<script type="text/javascript">
$().ready(function() {
	var id = '${educationVo.id}';
	if(id!=null&&id!=""){
		getAttach(id);//获得照片
		queryAttachByType('educationFile');//获取附件
	}
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
		url: '/education/education_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
			    if(uploader.getFiles().length>0&&uploaderFile.getFiles().length>0){//检查上传对列是否有照片 和 附件
					uploader.options.server = '/attach_upload?formId='+data.id +'&attachType=thumbnail';//自动添加水印
					uploader.upload();//上传
					uploader.on( 'uploadFinished', function( file ) {
					    setEntityImgPath(data.id);
						uploaderFile.options.server = '/attach_upload?formId='+data.id +'&attachType=educationFile';
						uploaderFile.upload();//上传
						uploaderFile.on( 'uploadFinished', function( file ) {
							autoMsg("保存成功！",1);
							iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/education/education_load"});//加载树下的列表
							parent.layer.close(index);
						});
					});
				}else if(uploader.getFiles().length>0){//检查上传对列是否有照片
					uploader.options.server = '/attach_upload?formId='+data.id +'&attachType=thumbnail';
					uploader.upload();//上传
					uploader.on( 'uploadSuccess', function( file ) {
						setEntityImgPath(data.id);
						autoMsg("保存成功！",1);
						iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/education/education_load"});//加载树下的列表
						parent.layer.close(index);
					});
				}else if(uploaderFile.getFiles().length>0){//检查上传对列是否有附件
					uploaderFile.options.server = '/attach_upload?formId='+data.id+'&attachType=educationFile';
					uploaderFile.upload();//上传
					uploaderFile.on( 'uploadFinished', function( file ) {
						autoMsg("保存成功！",1);
						iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/education/education_load"});//加载树下的列表
						parent.layer.close(index);
					});
				}else{
					autoMsg("保存成功！",1);
					iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/education/education_load"});//加载树下的列表
					parent.layer.close(index);
				}
			}else{
				autoAlert("保存失败，请检查！",5);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

//------------增加图片start----------------------------
var uploader;// Web Uploader实例
jQuery(function() {
	var uploaderDiv = $("#uploaderDiv"),//整个上传区域
	photoList = $('<ul class="filelist"></ul>').appendTo(uploaderDiv.find(".queueList")),
	statusBar = uploaderDiv.find(".statusBar"),  //状态显示条
    info = statusBar.find(".info"),//信息提示
    uploadBtn = uploaderDiv.find(".uploadBtn"),//上传按钮
    placeholder = uploaderDiv.find(".placeholder"),//选择图片输入区
    progress = statusBar.find(".progress").hide();//进度条
	uploader = WebUploader.create({// 初始化Web Uploader
	    auto: false,// 自动上传。
	    swf: '/common/plugins/webUploader/Uploader.swf',// swf文件路径
	    pick: {// 选择文件的按钮。可选。内部根据当前运行是创建，可能是input元素，也可能是flash.
            id: "#filePicker",
           //label: "选择图片"
           innerHTML:"选择图片"
        },
	    accept: { // 只允许选择文件，可选。
	        title: 'Images',
	        extensions: 'gif,jpg,jpeg,bmp,png',
	        mimeTypes: 'image/*'
	    },
	    fileNumLimit:1,//上传文件数量
	});
  	uploader.addButton({
        id: "#filePicker2",
        label: "继续添加"
    });
	//如果文件已经存在
  	if(attachList.length>0){
  		areaState("ready");
  		addFile(null,null,attachList[0].pathUpload,attachList[0].id);
  	}
	var ad = 0;
	uploader.on('fileQueued', function(file){
		addFile(selectFiles[ad],file);
		ad++;
		if(ad==selectFiles.length){
			ad = 0;
		}
		areaState("ready");
	});
	// 当有文件添加进来的时候
    uploader.on('fileDequeued', function(file){//当文件被移除队列后触发
    	if(photoList.children().length==0){//所有文件移除后 显示上传文件
    		areaState("pedding");	
    	}
    });
    uploader.on("all", function(state) {
    	 switch (state) {
         case "uploadFinished"://完成上传
        	 areaState("confirm");
             break;
         case "startUpload":
//              t("uploading");
             break;
         case "stopUpload"://停止上传
//              t("paused");
         }
    });
    //各状态表单改变
    function areaState(state){
    	switch (state){
	    	case "pedding":
	            placeholder.removeClass("element-invisible"),
// 	            photoList.parent().removeClass("filled"),
	            photoList.hide(),
	            statusBar.addClass("element-invisible"),
	            uploader.refresh();
	            break;
	    	 case "ready"://开始上传的状态
	    		 placeholder.addClass("element-invisible"),//隐藏图片选择区域
// 	    		 $(".statusBar").show();
	    		 $("#filePicker2").removeClass("element-invisible"),
// 	    		 photoList.parent().addClass("filled"),
	    		 photoList.show(),
	    	     statusBar.removeClass("element-invisible"),
	    	     uploader.refresh();
	             break;
    		case "confirm"://完成上传
    			uploadBtn.text("开始上传").addClass("disabled");//置灰开始上传
            	break;
    	}
    }
	//增加图片
    function addFile(selectFile,file,src,id){
    	var li = $('<li><p class="imgWrap"></p></li>'),
	    pd = $('<div class="file-panel"><span class="cancel">删除</span></div>').appendTo(li),
	    imgP = li.find("p.imgWrap");
	    if(src==null||src==""){
	    	var objUrl = getObjectURL(selectFile) ;
			if (objUrl) {
				 imgP.empty().append($('<img src="'+objUrl+'" onload="RePicByWidth(this,100);"/>'));
			}
			file.on("statuschange", function(imgP, uploader) {
				 if(imgP=="complete")
					 li.append('<span class="success"></span>');
			});
			pd.on("click", "span", function() {
		         if($(this).index()==0){//删除
		        	 $(this).parent().parent().remove();
		        	 uploader.removeFile(file);
		         }
			});
	    }else{
	    	imgP.empty().append($('<img src="'+src+'" onload="RePicByWidth(this,100);"/>'));
	    	li.append('<span class="success"></span>');
	    	pd.on("click", "span", function() {
		         if($(this).index()==0){//删除
		        	 parent.layer.confirm("确定删除图片？", {btn: ["确定","取消"]	}, function(){
		        	 	if(delAttach(id)){
			        	 	areaState("pedding");
			        	 	$(this).parent().parent().remove();
		        	 	}
		        	 });
		         }
			});
	    }
		li.on("mouseenter", function() {
			pd.stop().animate({
	             height: 25
	         });
	    }),
		li.on("mouseleave", function() {
			pd.stop().animate({
	             height: 0
	         });
	     }),
		photoList.append(li);//图片加入到显示区域
    }
});

//删除附件
function delAttach(id){
	var chk = false;
	$.ajax({
		type:'post',
		async:false,
		dataType : 'json',
		url: '/attach_delete?ids='+id,
		success : function(data){
			if(data.result){
				autoMsg("删除图片成功！",1);				
				chk = true;
			}else{
				autoMsg("删除失败！",1);				
			}
		},
	});
	return chk;
}
//获得照片
var attachList = new Array();
function getAttach(id){
	$.ajax({
		type:'post',
		async:false,
		dataType : 'json',
		url: '/queryAttachListByType?formId='+id +'&type=thumbnail',
		success : function(data){
			for (var i = 0; i < data.attachList.length; i++) {
				attachList.push(data.attachList[i]);
			}
		},
	});
}
//上传照片成功后，设置该实体的照片路径
function setEntityImgPath(id){
	$.ajax({
		type:'post',
		async:false,
		dataType : 'json',
		url: '/education/update_img?id='+id,
		success : function(data){
		}
	});
}
</script>
</html>