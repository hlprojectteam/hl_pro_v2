<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>选择隐患处理人</title>
</head>
<body>

<div class="ibox-content">
    <form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="" >
	    <input type="hidden" id="dpId" name="dpId" value="${dpId}" />
        <input type="hidden" id="sign" name="sign" value="yhclrPage" />
        <div class="form-group">
            <div class="col-sm-3" style="display: inline-block;">
                <input type="text" class="form-control" id="userName" name="userName" value="" />
            </div>
            <button class="btn btn-primary" type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
        </div>
        
        <div class="form-group">
            <!-- 用户列表 start-->
            <div class="col-sm-3" style="display: inline-block;">
                <select id="rangeSelect" style="width: 95%;" multiple="multiple" class="form-control">
                </select>
            </div>
            <!-- 用户列表 end-->
            
			<div class="col-sm-1" id="buttonDiv" style="display: inline-block;">
				<button class="btn btn-primary" type="button" onclick="on_add()" id="add">添加</button>
				</br>
				<button class="btn btn-primary" type="button" onclick="on_remove()" id="remove">删除</button>
				</br></br>
			</div>
			
			<!-- 已选中用户 start -->
			<div class="col-sm-3 text-left" style="display: inline-block;">
				<select id="chooseUser" multiple="multiple" style="width: 95%;" class="form-control">
				</select>
			</div>
			<!-- 已选中用户 end -->
		</div>
    </form>
    
</div>

<!-- 底部按钮 -->
<div class="footer edit_footer">
    <div class="pull-right">
        <button class="btn btn-primary " type="button" onclick="on_sure()"><i class="fa fa-check"></i>&nbsp;确定</button>
        <button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
    </div>
</div>

</body>
<script type="text/javascript">
	var winName = '${winName}';//上一ifame name
	
	$(document).ready(function(){
	    loadUserList();   //加载隐患处理人列表
	    $("#rangeSelect").css("height",document.body.clientHeight-120);//select高度
	    $("#rangeSelect").css("width",document.body.clientHeight/2+20);//select高度
	    $("#chooseUser").css("height",document.body.clientHeight-120);//select高度
	    $("#chooseUser").css("width",document.body.clientHeight/2-20);//select高度
	    $("#buttonDiv").css("line-height","100px");
	    reSelectUser();    //回调显示已选隐患处理人
	});
	
	//根据输入的用户名模糊查询相关隐患处理人信息
	function on_search(){
		loadUserList();
	}

	//加载隐患处理人信息
    function loadUserList(){
    	$("#rangeSelect").html("");
        $.ajax({
            type:"post",
            async:false,
            dataType : "json",
            data:$('#baseForm').serialize(),
            url: '/safecheck/hiddenDanger/loadUserList',
            success : function(data){
                if(data!=null){
                	var userList = data.userList;
                    for (var i in userList) {
                        $("#rangeSelect").append("<option value='"+userList[i].id+"' ondblclick='on_add()'>"+userList[i].userName+"("+userList[i].orgFrameName+")"+"</option>");
                    }
                }else{
                    autoAlert("查无此隐患处理人，请重新输入关键字。谢谢！");
                }
            },
            error : function(result){
                autoAlert("系统出错",5);
            }
        });
    }  
	
	
	
	 //增加
	function on_add(){
	    var map = new Map();
	    var so = $("#chooseUser option");
	    for(var i=0;i<so.length;i++){
	        map.put(so[i].value,so[i].text);
	    }
	    var ro = $("#rangeSelect option");
	    for(var i=0;i<ro.length;i++){
	        if(ro[i].selected){
	            if(map.get(ro[i].value)==ro[i].text){//排除出重复选择
	                continue;//出现重复就打断
	            }else{
	                $("#chooseUser").empty().html("<option value='"+ro[i].value+"' ondblclick='on_remove()'>"+ro[i].text+"</option>");
	            }
	        }
	     }
	}
	
	//删除
	function on_remove(){
	     var o = $("#chooseUser option");
	     for(var i=0;i<o.length;i++){
	         if(o[i].selected){
	             $(o[i]).remove();
	         }
	     }
	}
	
	//保存
	function on_sure(){
	    var o = $("#chooseUser option");
	    var ids = "";
	    var names = "";
	    for(var i=0;i<o.length;i++){
	        ids += o[i].value+",";
	        names += o[i].text+",";
	    }
	    
	    parent.frames[winName].$("#epNextPersonId").val(ids.substring(0,ids.indexOf(",")));
	    parent.frames[winName].$("#epNextPersonName").val(names.substring(0,names.indexOf("(")));
	    parent.frames[winName].$("#nextPersonName_yhclr").text(names.substring(0,names.indexOf(",")));
	    parent.layer.close(index);//关闭
	}
	
	
	//回调显示已选隐患处理人
	function reSelectUser(){
		var ids = "";
		var names = "";
		if(parent.frames[winName].$("#epNextPersonId").val() != null && parent.frames[winName].$("#epNextPersonId").val() != ""){
			ids = parent.frames[winName].$("#epNextPersonId").val();
		}
		if(parent.frames[winName].$("#nextPersonName_yhclr").text() != null && parent.frames[winName].$("#nextPersonName_yhclr").text() != ""){
			names = parent.frames[winName].$("#nextPersonName_yhclr").text();
		}
	    if(ids!=null&&ids!=""){
	        var idz = ids.split(",");
	        var namez = names.split(",");
	        for(var i=0;i<idz.length;i++){
	            $("#chooseUser").append("<option value='"+idz[i]+"' ondblclick='on_remove()'>"+namez[i]+"</option>");
	        }
	    }
	}
	
	
	//关闭
	function on_close(){
	    parent.layer.close(index);
	}
</script>
</html>