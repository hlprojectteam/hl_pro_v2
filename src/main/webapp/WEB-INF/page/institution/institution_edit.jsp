<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>制度编辑</title>
</head>
<!-- 编辑器UEditor start -->
<script type="text/javascript" charset="utf-8" src="/common/plugins/UEditor/ueditor.config.js"></script>
<script type="text/javascript" src="/common/plugins/UEditor/ueditor.all.js"></script>
<script type="text/javascript" src="/common/plugins/UEditor/lang/zh-cn/zh-cn.js"></script>
<!-- 编辑器UEditor end -->
<body>
<div id="" class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="delFlag" name="delFlag" value="${institutionVo.delFlag}" />
<input type="hidden" id="cover" name="cover" value="${institutionVo.cover}" />
<input type="hidden" id="status" name="status" value="${institutionVo.status}" />
<input type="hidden" id="visitNum" name="visitNum" value="${institutionVo.visitNum}" />
<input type="hidden" id="id" name="id" value="${institutionVo.id}" />		
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${institutionVo.createTime}'  pattern='yyyy-MM-dd HH:mm:ss'/>"/>
<input type="hidden" id="createUserId" name="createUserId" value="${institutionVo.createUserId}" />	
<input type="hidden" id="creatorId" name="creatorId" value="${institutionVo.creatorId}" />	
<input type="hidden" id="creatorName" name="creatorName" value="${institutionVo.creatorName}" />	
	<%-- 第1行 --%>
	<div class="form-group">
  	<label class="col-sm-2 control-label"><span style="color: red">*</span>主标题</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="mainTitle" name="mainTitle" value='${institutionVo.mainTitle}' data-rule-required="true" data-rule-rangelength="[1,100]" />
    </div>
  	</div>
  	<%-- 第2行 --%>
  	<div class="form-group">
  	<label class="col-sm-2 control-label">副标题</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="viceTitle" name="viceTitle" value='${institutionVo.viceTitle}' data-rule-rangelength="[1,100]" />
    </div>
  	</div>
  	<%-- 第3行 --%>
  	<div class="form-group">
  	<label class="col-sm-2 control-label">作者</label>
     <div class="col-sm-3">
      <input type="text" class="form-control" id="authorName" name="authorName" value="${institutionVo.authorName}"  data-rule-rangelength="[1,20]" />
     </div>
  	  <label class="col-sm-2 control-label"><span style="color: red">*</span>分类</label>
    <div class="col-sm-3">
      <opt:select dictKey="institution_category" classStyle="form-control" id="category" name="category" value="${institutionVo.category}"/>
    </div>
    </div>
    
    
  	<div class="form-group">
  	<label class="col-sm-2 control-label"><span style="color: red">*</span>发布时间</label>
    <div class="col-sm-3">
		<input type="text" class="form-control" id="releaseDateStr" name="releaseDateStr" value="<fmt:formatDate value='${institutionVo.releaseDate}' pattern='yyyy-MM-dd HH:mm:ss'/>" data-rule-required="true" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>    
	</div>
 
      <label class="col-sm-2 control-label"><span style="color: red">*</span>来源</label>
    <div class="col-sm-3">
      <opt:select dictKey="news_Source" classStyle="form-control" id="newSource" name="newSource" value="${institutionVo.newSource}" />
    </div>
    </div> 
	
   <!--  </div>  -->
    		<%-- 第5行 --%>
     <div class="form-group">
	    <label class="col-sm-2 control-label">附件</label>
	    <!-- 图书文件 -->
			<div class="col-sm-10">
				<div id="uploaderFile" class="wu-example">
					<div id="thelistFile" class="uploader-list"></div>
						<div>
							<div id="pickerFile" style="text-align: left;">选择文件</div>
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
    <%-- 第6行 --%>
  	<div class="form-group">
  	<label class="col-sm-2 control-label"><span style="color: red">*</span>正文</label>
    <div class="col-sm-10">
     		<script id="contentStr" name="contentStr" type="text/plain" style="width:100%;height:450px;" data-rule-required="true">
					<p>这里可以输入主要内容</p>
			</script>
			<script type="text/javascript">
				var option = {
					initialFrameWidth: '100%'  //初始化编辑器宽度
					,initialFrameHeight: 450  //初始化编辑器高度,默认320
				};
			    var editor = UE.getEditor('contentStr', option);  // 括号里填容器的id
			    editor.ready(function() {
			        editor.setContent('${institutionVo.contentStr}');//设置编辑器的内容
			    });
			</script>
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
	var id = '${institutionVo.id}';
	if(id!=null&&id!=""){
		getAttach(id);//获得照片
		queryAttachByType('institutionFile');
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
		url: '/institution/institution_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
			    if(uploader.getFiles().length>0&&uploaderFile.getFiles().length>0){//检查上传对列是否有照片 和 附件
					uploader.options.server = '/attach_upload?formId='+data.id +'&attachType=thumbnail';//自动添加水印
					uploader.upload();//上传
					uploader.on( 'uploadFinished', function( file ) {
					    setEntityImgPath(data.id);
						uploaderFile.options.server = '/attach_upload?formId='+data.id +'&attachType=institutionFile';
						uploaderFile.upload();//上传
						uploaderFile.on( 'uploadFinished', function( file ) {
							autoMsg("保存成功！",1);
							iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/institution/institution_load"});//加载树下的列表
							parent.layer.close(index);
						});
					});
				}else if(uploader.getFiles().length>0){//检查上传对列是否有照片
					uploader.options.server = '/attach_upload?formId='+data.id +'&attachType=thumbnail';
					uploader.upload();//上传
					uploader.on( 'uploadSuccess', function( file ) {
						setEntityImgPath(data.id);
						autoMsg("保存成功！",1);
						iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/institution/institution_load"});//加载树下的列表
						parent.layer.close(index);
					});
				}else if(uploaderFile.getFiles().length>0){//检查上传对列是否有附件
					uploaderFile.options.server = '/attach_upload?formId='+data.id+'&attachType=institutionFile';
					uploaderFile.upload();//上传
					uploaderFile.on( 'uploadFinished', function( file ) {
						autoMsg("保存成功！",1);
						iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/institution/institution_load"});//加载树下的列表
						parent.layer.close(index);
					});
				}else{
					autoMsg("保存成功！",1);
					iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/institution/institution_load"});//加载树下的列表
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
            label: "选择图片"
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
		url: '/queryAttachList?formId='+id,
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
		url: '/institution/update_img?id='+id,
		success : function(data){
		}
	});
}
</script>
</html>