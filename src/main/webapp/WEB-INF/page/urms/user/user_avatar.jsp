<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>用户</title>
</head>
<body>
<div class="ibox-content">
<div style="width:100%;margin: 0 auto;">
            <div>
                <p id="swfContainer">
                    本组件需要安装Flash Player后才可使用，请从
                    <a href="http://www.adobe.com/go/getflashplayer">这里</a>
                    下载安装。
                </p>
            </div>
        </div>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-danger" type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>

<!-- 照相 -->
<script type="text/javascript" src="/common/plugins/fullAvatarEditor/scripts/swfobject.js"></script>
<script type="text/javascript" src="/common/plugins/fullAvatarEditor/scripts/fullAvatarEditor.js"></script>
<script type="text/javascript">
<%
String basePath = request.getServerName()+":"+request.getServerPort();
%>
swfobject.addDomLoadEvent(function () {
    var swf = new fullAvatarEditor("/common/plugins/fullAvatarEditor/fullAvatarEditor.swf", "/common/plugins/fullAvatarEditor/expressInstall.swf", "swfContainer", {
            id : "swf",
            upload_url : "/upload_avatar",//上传路径
            method : "post",
            button_visible : true,
            src_upload : 0 //是否上传原图片的选项，有以下值：0-不上传；1-上传；2-显示复选框由用户选择
        }, function (json) {
            switch(json.code){
                case 1 : 
                	break;
                case 2 : //已成功加载图片到编辑面板 显示按钮
                	break;
                case 3 :
                    if(json.type == 0){
                        //摄像头正常使用
                    }else if(msg.type == 1){
                        autoAlert("摄像头已准备就绪但用户未允许使用！",5);
                    }else{
                        autoAlert("摄像头被占用！",5);
                    }
                break;
                case 5 : 
                    if(json.type == 0){
                    	$.ajax({
							type: 'post',
							async: true,
							dataType: 'json',
							url: '/urms/user_updateAvatar?id=${sessionScope.user.id}',//更新头像虚拟路径
							success: function(data){
								if(data.result){
									autoMsg("保存成功！",1);
									parent.location.reload();//刷新页面
			                    	on_close();//关闭窗口
								}else{
									autoAlert("保存失败，请检查！",5);
								}
							}
						});
                    }else{
                    	autoAlert("头像上传失败",5);
                    	console.log(json.content.msg);
                    }
                break;
            }
        }
    );
});
</script>
</html>