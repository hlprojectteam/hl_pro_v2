<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>用户编辑</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="" >
<input type="hidden" id="id" name="id" value="${userVo.id}"/>
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value="${userVo.createTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>"/>
<input type="hidden" id="orgFrameId" name="orgFrame.id" value="${userVo.orgFrame.id}"/>
<input type="hidden" id="creatorId" name="creatorId" value="${userVo.creatorId}"/><%-- 创建人id --%>
<input type="hidden" id="creatorName" name="creatorName" value="${userVo.creatorName}"/><%-- 创建人 --%>
  <div class="form-group">
    <label class="col-sm-1 control-label"><span style="color: red">*</span>登录账号</label>
    <div class="col-sm-3">
      	<input type="text" class="form-control" id="loginName" name="loginName" value="${userVo.loginName }" autocomplete="off" data-rule-required="true" data-rule-numbersEnglish="false" data-rule-rangelength="[1,30]" data-rule-remote="/urms/user_checkUserLoginName?id=${userVo.id}" data-msg-remote="登录名称已经存在，请重新输入"/>
    </div>
    <label class="col-sm-1 control-label"><span style="color: red">*</span>状态</label>
    <div class="col-sm-3">
    	<opt:select dictKey="state" isDefSelect="false" id="state" name="state" value="${userVo.state }" classStyle="form-control m-b required"/>
    </div>
    <label class="col-sm-1 control-label"><span style="color: red">*</span>用户类型</label>
    <div class="col-sm-3">
      	<select id="type" name="type" class="form-control m-b required">
      		<option value="3" selected="selected">后台普通用户</option>
      	</select>
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-1 control-label"><span style="color: red">*</span>密码</label>
    <div class="col-sm-3">
      	<input type="password" class="form-control" id="password" name="password" value="${userVo.password }" autocomplete="off" data-rule-required="true" data-rule-minlength="6" data-msg-required="密码不能为空" data-msg-minlength="至少设置6位密码"/>
    </div>
    <label class="col-sm-1 control-label"><span style="color: red">*</span>确认密码</label>
    <div class="col-sm-3">
      	<input type="password" class="form-control" id="password2" name="" value="${userVo.password }" autocomplete="off" data-rule-equalTo="#password" data-msg-equalTo="两次密码不一致"/>
    </div>
    <label class="col-sm-1 control-label"><span style="color: red">*</span>所属子系统</label>
    <div class="col-sm-3">
      	<select id="subsystemSelect" name="sysCode" class="form-control m-b required">
      	</select>
    </div>
  </div>
  <%-- 分割线 --%>
  <div class="hr-line-dashed"></div>
  	<div class="form-group">
  	<div class="col-sm-10">
	<div class="form-group">
		<label class="col-sm-1 control-label"><span style="color: red">*</span>姓名</label>
	    <div class="col-sm-3">
	      	<input type="text" class="form-control" id="userName" name="userName" value="${userVo.userName}" data-rule-required="true" data-rule-minlength="2" data-msg-required="姓名不能为空" data-rule-rangelength="[1,5]"/>
	    </div>
	    <label class="col-sm-1 control-label"><span style="color: red">*</span>手机</label>
	    <div class="col-sm-3">
   	      	<input type="text" class="form-control" id="mobilePhone" name="mobilePhone" value="${userVo.mobilePhone }" data-rule-required="true" data-rule-minlength="11" data-msg-required="手机号不能为空" autocomplete="off" data-rule-mobilephone="true" data-rule-remote="/urms/user_checkMobilePhone?id=${userVo.id}" data-msg-remote="手机号码已经存在，请重新输入"/>
	    </div>
	    <label class="col-sm-1 control-label"><span style="color: red">*</span>工号</label>
	    <div class="col-sm-3">
   	      	<input type="text" class="form-control" id="jobNumber" name="jobNumber" value="${userVo.jobNumber }" data-rule-required="true" data-rule-minlength="4" data-msg-required="工号不能为空" autocomplete="off" />
	    </div>
	  
	</div>
	<div class="form-group">
		<label class="col-sm-1 control-label">性别</label>
	    <div class="col-sm-3">
	      	<opt:select dictKey="sex" isDefSelect="true" id="sex" name="sex" value="${userVo.sex }" classStyle="form-control m-b"/>
	    </div>
	    <label class="col-sm-1 control-label">年龄</label>
	    <div class="col-sm-3">
	      	<input type="text" class="form-control" id="age" name="age" value="${userVo.age}" data-rule-rangelength="[1,3]"  data-rule-digits="true"/>
	    </div>
		<label class="col-sm-1 control-label">民族</label>
	    <div class="col-sm-3">
	    	<opt:select dictKey="national" isDefSelect="true" id="nation" name="nation" value="${userVo.nation }" classStyle="form-control m-b"/>
	    </div>
	    	
	   
	</div>
	<div class="form-group">
		<label class="col-sm-1 control-label">证件号码</label>
	    <div class="col-sm-3">
	    	<input type="text" class="form-control" id="certificatesNum" name="certificatesNum" value="${userVo.certificatesNum}" data-rule-rangelength="[1,22]"/>
	    </div>
	    <label class="col-sm-1 control-label">固话</label>
	    <div class="col-sm-3">
	      	<input type="text" class="form-control" id="telephone" name="telephone" value="${userVo.telephone}" data-rule-telephone="true"/>
	    </div>
	    <label class="col-sm-1 control-label">QQ</label>
	    <div class="col-sm-3">
	      	<input type="text" class="form-control" id="qq" name="qq" value="${userVo.qq}" data-rule-rangelength="[1,20]"/>
	    </div>
	</div>
	</div>
	<div class="col-sm-2">
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
	<div class="form-group">
	<label class="col-sm-1 control-label">Email</label>
	    <div class="col-sm-3">
   		    <input type="text" class="form-control" id="email" name="email" value="${userVo.email }" autocomplete="off" data-rule-email="true" data-rule-remote="/urms/user_checkEmail?id=${userVo.id}" data-msg-remote="Email已经存在，请重新输入"/>
	    </div>
	    <label class="col-sm-1 control-label">地址</label>
	    <div class="col-sm-7">
	      	<input type="text" class="form-control" id="address" name="address" value="${userVo.address}" data-rule-rangelength="[1,64]"/>
	    </div>
	 </div> 
	 <div class="form-group">
	    <label class="col-sm-1 control-label">备注</label>
	    <div class="col-sm-11">
			<textarea class="form-control" id="memo" name="memo" data-rule-rangelength="[1,256]">${userVo.memo}</textarea>
	    </div>
	</div>
	<br><br>
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

<!-- 文件上传 -->
<link href="/common/plugins/webUploader/webuploader.css" rel="stylesheet">
<link href="/common/plugins/webUploader/user/user_photo.css" rel="stylesheet">
<script src="/common/plugins/webUploader/webuploader.js"></script>
<script>
$().ready(function() {
	var id = '${userVo.id}';
	if(id!=null&&id!=""){
		getAttach(id);//获得照片
	}
	getSubsystem();
});
//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
		var password = $("#password").val();//加密
		if(password.length>=30){
		   on_submit();
		}else{
		   $("#password").val(md5(password));
		   on_submit();
		}
    } 
}

//提交保存
function on_submit(){
	$.ajax({
		type: 'post',
		async: true,
		dataType: 'json',
		url: '/urms/user_saveOrUpdate',
		data: $('#baseForm').serialize(),
		success: function(data){
			if(data.result){
				if(uploader.getFiles().length>0){//检查上传对列是否有照片
					uploader.options.server = '/attach_upload?formId='+data.id+'&sysCode='+data.sysCode;
					uploader.upload();//上传
					uploader.on( 'uploadSuccess', function( file ) {
						$.ajax({
							type: 'post',
							async: true,
							dataType: 'json',
							url: '/urms/user_updateAvatar?id='+data.id,//更新头像虚拟路径
							success: function(data){
								if(data.result){
									 autoMsg("保存成功！",1);
									 iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/urms/user_load?sign=2"});//加载树下的列表
									 parent.layer.close(index);
								}else{
									autoAlert("保存失败，请检查！",5);
								}
							}
						});
					 });
				}else{
					autoMsg("保存成功！",1);
					iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/urms/user_load?sign=2"});//加载树下的列表
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

//获得子系统
function getSubsystem(){
	$.ajax({
		type:'post',
		async:false,
		dataType : 'json',
		url: '/urms/subsystem_getSubsystem',
		success : function(data){
			var sysCode = '${userVo.sysCode}';
			if(data!=null){
				$("#subsystemSelect").append("<option value=''>------请选择------</option>");
				 for (var i in data) {
				 	if(sysCode==data[i].sysCode)
				 		$("#subsystemSelect").append("<option value='"+data[i].sysCode+"' selected='selected'>"+data[i].sysName+"</option>");
				 	else
				 		$("#subsystemSelect").append("<option value='"+data[i].sysCode+"'>"+data[i].sysName+"</option>");
				 }
			}
		},
	});
}
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
            label: "选择头像"
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
		        	 parent.layer.confirm("确定删除头像？", {btn: ["确定","取消"]	}, function(){
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
				$.ajax({
					type:'post',
					async:false,
					dataType : 'json',
					url: '/urms/user_updateAvatar?id=${userVo.id}',
					success : function(data){
						if(data.result){
							autoMsg("删除头像成功！",1);				
							chk = true;
						}else{
							autoMsg("删除失败！",1);				
						}
					}
				});
			}else{
				autoMsg("删除失败！",1);				
			}
		},
	});
	return chk;
}
</script>
</html>