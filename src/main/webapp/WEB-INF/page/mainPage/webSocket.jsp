<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" import="com.common.utils.Common" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
</head>

<body>

</body>
    <!-- sockjs通信 -->
	<script src="/common/js/sockjs-0.3.min.js" ></script>
    <script>
    <%
	String basePath = request.getServerName()+":"+request.getServerPort();
%>
<%-- 登录后建立websocket通讯信道 --%>
var websocket;
$(function(){
    if ('WebSocket' in window) {<%-- 支持火狐，Google，ie11以上浏览器--%>
    	websocket = new WebSocket("ws://<%=basePath%>/echo");
    } else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://echo");
    } else {
  //      attoMsg("该系统不支持此浏览器，请更换火狐、IE11、谷歌等浏览器使用",2);
          websocket = new SockJS("http://<%=basePath%>/sockjs/echo");
    }
    websocket.onopen = function (evnt) {
    	console.info("链接服务器成功!");
    };
    <%-- 返回消息--%>
    websocket.onmessage = function (evnt) {
    	doMsg(JSON.parse(evnt.data));//返回消息处理
    };
    websocket.onerror = function (evnt) {
    	console.info(evnt);
    };
    websocket.onclose = function (evnt) {
    	console.info("与服务器断开了链接!");
    };
});
	
function doMsg(data){
	if(data!=""){
		var html = "";
		if(data.type==1){//待办条数
			if(data.dbCount>0)
				html += '<div style="padding-left:50px;padding-top:30px;"><a href="javaScript:void(0);" onclick="on_dbList()"><h4>您有'+data.dbCount+'条待办事件, 请及时处理!</h4></a></div>';
		}else if(data.type==2){//待办提醒
			html += '<div style="padding-left:100px;padding-top:15px;"><h3>您新的待办</h3></a></div></br>';
			html += '<div style="padding-left:50px;padding-top:1px;"><h4><a href="javaScript:void(0);" onclick="on_dbList()">'+data.title+'</a></h4></div>';
		}else if(data.type==3){//消息通知
			html += '<div style="padding-left:100px;padding-top:15px;"><h3>消息通知</h3></a></div></br>';
			html += '<div style="padding-left:50px;padding-top:1px;"><h5>'+data.msg+'</h5></a></div>';
		}else if(data.type==4){//当前人数
			$("#onlinePerson").html(data.onlinePerson);
		}else if(data.type=='layim'){//即时通讯
// 			layimCall.getMessage(data.chatMessage);
		}else if(data.type=='layim_addGroup'){//即时通讯 增加组群
// 			layimCall.addList(data.addGroup);
		}else if(data.type=='layim_deleteGroup'){//即时通讯 删除组群
// 			layimCall.removeList(data.deleteGroup);
		}
		if(html!=""){
		 	layer.open({
		        type: 1, //此处以iframe
		        title: false,
		        closeBtn: 0, //不显示关闭按钮
		        shade: 0,//不遮罩
		        area: ['300px', '120px'],
		        shift:2,//动画
		        time: 8000,//自动关闭
		        move: false,//禁止拖动
		        offset: [//右下角
		            $(window).height()-160,$(window).width()-300
		        ],
				content: html
		    });
		}
	}
}

//页面在框内打开
function on_dbList(){
	document.getElementById('open_dbList').click();//流程办理
}

var currentLoginId = '${sessionScope.user.id}';	
var layimCall;	
/*  
layui.use('layim', function(layim){
	layimCall = layim;
	//基础配置
	layim.config({
		//获取主面板列表信息
		init: {
			url: '/personal/getCommunicPerson' //接口地址（返回的数据格式见下文）
		    ,type: 'post' //默认get，一般可不填
		    ,data: {} //额外参数
		}
		//查看群员接口 
		,members: {
			url: '/personal/getMembers'
		    ,type: 'post' //默认get，一般可不填
		    ,data: {}
		}
		,uploadImage: {
		  url: '/attach_upload?formId=ssss' //（返回的数据格式见下文）
		  ,type: '' //默认post
		}
		,chatLog: '/personal/chatlog' //聊天记录地址
		// ,uploadFile: {
		//   url: '' //（返回的数据格式见下文）
		//   ,type: '' //默认post
		// }
		// ,skin: ['aaa.jpg'] //新增皮肤
		// ,isfriend: false //是否开启好友
		// ,isgroup: false //是否开启群组
		// ,find: '/layimdemo/find.html'
	});
	  
	//监听信息发送
	layim.on('sendMessage', function(res){
		var type = {type:'layim'};
		var json = (JSON.stringify(type)+JSON.stringify(res)).replace(/}{/,',');
		websocket.send(json);
	});

	//初始化后触发
	layim.on('ready', function(options){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/personal/queryUnSendMsg',
			success : function(data){
				
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检d查！",5);
			}
		});
	});
});

//点击头像 打开用户资料
function on_editUser(id){
parent.layer.open({
    type: 2,
    title: "用户资料",
    shadeClose: true,//打开遮蔽
    shade: 0.6, 
    maxmin: true, //开启最大化最小化按钮
    area: ["65%", "85%"],
    content: "/urms/user_edit_user?id="+id
});
}

//添加群组人员
//function on_addUser(){

//}

//组群管理
function on_groupManage(){
parent.layer.open({
    type: 2,
    title: "我的组群管理",
    shadeClose: true,//打开遮蔽
    shade: 0.6, 
    maxmin: true, //开启最大化最小化按钮
    area: ["65%", "85%"],
    content: "/personal/groupManage"
});
}

//鼠标移动到图片上时 显示删除按钮
function on_memberShowDel(obj){
	$(obj).find(".badge").css("right","-10px");
	$(obj).find(".badge").css("top","-10px");
	$(obj).find(".badge").show();
}

function on_memberShowOut(obj){
	$(obj).find(".badge").hide();
}

//删除成员
function on_delMember(obj,id,groupId){
	layer.confirm("确定删除当前成员？", {
		btn: ["确定","取消"] //按钮
	}, function(index){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/personal/deleteMember?id='+id+'&groupId='+groupId,
			success : function(data){
				var num = $(".layim-chat-members").html();
				num = num.replace("人","");
				$(".layim-chat-members").html((num-1)+"人");
				layer.close(index);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	});
}
*/
</script>

</html>
