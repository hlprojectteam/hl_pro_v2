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
<title>用户选择</title>
</head>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>

<!-- eCharts -->
<script type="text/javascript" src="/common/plugins/Echarts/echarts4.0.js"></script>

<body style="overflow:scroll;overflow-y:hidden">
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="" >
	<div class="form-group">
	<!-- 
		<div class="col-sm-5">
			<input type="text" class="form-control" id="userName" name="userName" value="" />
		</div>
		<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
	 -->
	</div>
	<div class="form-group">
		<div class="col-sm-3">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div class="col-sm-3">
			  <label class="col-sm-2 control-label">已阅</label>
			<select id="rangeSelect" style="width: 65%" multiple="multiple" class="form-control">
			</select>
		</div>
		<div class="col-sm-3 text-left">
		<label class="col-sm-2 control-label">未阅</label>
			<select id="chooseUser"  multiple="multiple" style="width: 65%" class="form-control">		
			</select>
		</div>
	</div>
	<div  id="fontDIV" style="top:5%;left:75%;position: absolute;"><h3>已阅：</h3><br/><h3>未阅：</h3></div>
	 <div id="main" style="position:absolute;width: 300px;height:300px;top:20%;left:70%;"></div><!-- 报表 -->
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<!-- <button class="btn btn-primary " type="button" onclick="on_sure()"><i class="fa fa-check"></i>&nbsp;确定</button> -->
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script>
var winName = '${winName}';//上一ifame name
var id = '${id}';//回填id
var name = '${name}';//回填name
var selectNum = '${selectNum}';//单选还是多选
var zTreeObj;
var institutionVo_id = '${institutionVo.id}';//记录ID

var hadReadedCount;
var hadNotReadedCount;

$(document).ready(function(){
	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
	
	getNodesUser();//获取节点用户		
	$("#rangeSelect").css("height",document.body.clientHeight-130);//select高度
	$("#chooseUser").css("height",document.body.clientHeight-130);//select高度
	$("#buttonDiv").css("margin-top",document.body.clientHeight/2-180);
	//getUser("");//获得用户
	reSelectUser();//回调显示已选用户
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	

	////////////////////////////////////////////////////////////////////////////////////////////////
	
});
var setting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onClick: zTreeOnClick
		},
		async: {//异步加载
			enable: true,
			type: "post",
			dataType: "text",
			contentType:"application/x-www-form-urlencoded",
			url: "/urms/orgFrame_loadTree",
			autoParam: ["id"]
		}
	};


//初始化饼状图
function loadPieReport(orgFrameID){
	$.ajax({
		type:"post",
		async:false,
		dataType : "text",
		contentType:"application/x-www-form-urlencoded",
		url: '/institution/load_sonOrg_list?orgFrameID='+orgFrameID+'&institutionVo_id='+institutionVo_id,
		success : function(data){
					var count = eval("("+data+")");
					var myChart = echarts.init(document.getElementById('main')); 
					document.getElementById('fontDIV').innerHTML="<h3>已阅："+count.hadReadedCount+"</h3><br/><h3>未阅："+count.notReadCount+"</h3>";
					option = { 
					    tooltip: { 
					        trigger: 'item', 
					        formatter: "{a} <br/>{b}: {c} ({d}%)" 
					    }, 
					    series: [ 
					        { 
					            name:'阅读统计', //内环 
					            type:'pie', 
					            selectedMode: 'single', //单一选中模式 
					 
					            label: { 
					                normal: { 
					                    position: 'inner' //内置文本标签 
					                } 
					            }, 
					            labelLine: { 
					                normal: { 
					                    show: false     //不需要设置引导线 
					                } 
					            }, 
					           // data:[
					              //  {value:count.hadReadedCount, name:'已阅'}, 
					               //{value:count.notReadCount, name:'未阅'}, 
					             // ]
					          	data: (function(){
                                var res = [];
                                	if(count.notReadCount==0&&count.hadReadedCount!=0){
										  res.push({
	                              		  name: "已阅",
	                             	  	  value: count.hadReadedCount
	                             	 	  });
                             	 	 }else if(count.hadReadedCount==0&&count.notReadCount==0){
                             	 		  res.push({
	                              		  name: "此节点没有用户",
	                             	  	  value: 0
	                             	 	  });
                             	 	 }else if(count.hadReadedCount==0&&count.notReadCount!=0){
										 res.push({
                              	 	 	 name: "未阅",
                              		 	 value: count.notReadCount
                              		 	 });
									 }else{
										  res.push({
	                              	 	  name: "未阅",
	                              		  value: count.notReadCount
	                              		  });
	                              		  res.push({
	                              		  name: "已阅",
	                             	  	  value: count.hadReadedCount
	                             	 	  });
									 }
//                                 res.push({
//                                 name: "未阅",
//                                 value: count.notReadCount
//                                 });
                                return res;
                                })()
                                 
					        }, 
					    ] 
					}; 
					 
					// 使用刚指定的配置项和数据显示图表。 
					myChart.setOption(option); 
							
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});


}

//回掉饼状图
function loadNodesPieReport(orgFrameID){
	$.ajax({
		type:"post",
		async:false,
		dataType : "text",
		contentType:"application/x-www-form-urlencoded",
		//url: '/institution/pie?orgFrameID='+orgFrameID+'&institutionVo_id='+institutionVo_id,
		url: '/institution/click_tree_data?orgFrameID='+orgFrameID+'&institutionVo_id='+institutionVo_id,
		success : function(data){
					var count = eval("("+data+")");
					$("#rangeSelect").html("");
					$("#chooseUser").html("");
					 for (var i=0;i<count.notReadName.length;i++){
				 	// if(data.notReadName[i]!="undefined"&&data.notReadName[i]!=null)
					 $("#chooseUser").append("<option value='"+count.notReadName[i]+"' >"+count.notReadName[i]+"</option>");
					 }
				 
					 for(var i=0;i<count.hadReadedName.length;i++){
					//if(data.hadReadedName[i]!="undefined"&&data.hadReadedName[i]!=null)
					$("#rangeSelect").append("<option value='"+count.hadReadedName[i]+"' >"+count.hadReadedName[i]+"</option>");
					 }
					
					
					var myChart = echarts.init(document.getElementById('main')); 
					document.getElementById('fontDIV').innerHTML="<h3>已阅："+count.hadReadedCount+"</h3><br/><h3>未阅："+count.notReadCount+"</h3>";
					option = { 
					    tooltip: { 
					        trigger: 'item', 
					        formatter: "{a} <br/>{b}: {c} ({d}%)" 
					    }, 
					    series: [ 
					        { 
					            name:'阅读统计', //内环 
					            type:'pie', 
					            selectedMode: 'single', //单一选中模式 
					 
					            label: { 
					                normal: { 
					                    position: 'inner' //内置文本标签 
					                } 
					            }, 
					            labelLine: { 
					                normal: { 
					                    show: false     //不需要设置引导线 
					                } 
					            }, 
					           // data:[
					              //  {value:count.hadReadedCount, name:'已阅'}, 
					               //{value:count.notReadCount, name:'未阅'}, 
					             // ]
					          	data: (function(){
                                var res = [];
                                	if(count.notReadCount==0&&count.hadReadedCount!=0){
										  res.push({
	                              		  name: "已阅",
	                             	  	  value: count.hadReadedCount
	                             	 	  });
                             	 	 }else if(count.hadReadedCount==0&&count.notReadCount==0){
                             	 		  res.push({
	                              		  name: "此节点没有用户",
	                             	  	  value: 0
	                             	 	  });
                             	 	 }else if(count.hadReadedCount==0&&count.notReadCount!=0){
										 res.push({
                              	 	 	 name: "未阅",
                              		 	 value: count.notReadCount
                              		 	 });
									 }else{
										  res.push({
	                              	 	  name: "未阅",
	                              		  value: count.notReadCount
	                              		  });
	                              		  res.push({
	                              		  name: "已阅",
	                             	  	  value: count.hadReadedCount
	                             	 	  });
									 }
//                                 res.push({
//                                 name: "未阅",
//                                 value: count.notReadCount
//                                 });
                                return res;
                                })()
                                 
					        }, 
					    ] 
					}; 
					 
					// 使用刚指定的配置项和数据显示图表。 
					myChart.setOption(option); 
							
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});


}

//点击树后回调
function zTreeOnClick(event, treeId, treeNode) {
	//getUser(treeNode.id);
	//getNodesReportUser(treeNode.id);
	//getNodesReportNotReadUser(treeNode.id);
	loadNodesPieReport(treeNode.id);
}
//获取用户
function getNodesUser(){
	$.ajax({
		type:"post",
		async:false,
		dataType : "text",
		contentType:"application/x-www-form-urlencoded",
		url: '/institution/get_user',
		success : function(orgFrameID){
			loadUserNoder(orgFrameID);
			//$("#grib").bootstrapTable("refresh",{url:"/urms/orgFrame_load?id="+orgFrameID});
		    loadPieReport(orgFrameID);//初始化报表
			//getNodesReportUser(orgFrameID);//在初始化报表在当前用户组织架构下的所有 已阅所有用户
			//getNodesReportNotReadUser(orgFrameID);//在初始化报表在当前用户组织架构下的未阅读的所有用户
			
			firstTimeLoad(orgFrameID);//初始化所有页面数据
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});
}
//初始化所有页面数据
function firstTimeLoad(pId){
	$("#rangeSelect").html("");
	$("#chooseUser").html("");
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		url: '/institution/load_sonOrg_list?orgFrameID='+pId+'&institutionVo_id='+institutionVo_id,
		success : function(data){
			if(data!=null){
				 for (var i=0;i<data.notReadName.length;i++){
				 	// if(data.notReadName[i]!="undefined"&&data.notReadName[i]!=null)
					 $("#chooseUser").append("<option value='"+data.notReadName[i]+"' >"+data.notReadName[i]+"</option>");
				 }
				 
				 for(var i=0;i<data.hadReadedName.length;i++){
					//if(data.hadReadedName[i]!="undefined"&&data.hadReadedName[i]!=null)
					$("#rangeSelect").append("<option value='"+data.hadReadedName[i]+"' >"+data.hadReadedName[i]+"</option>");
				 }
			}
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});
}

//获取节点
function loadUserNoder(orgFrameID){
	$.ajax({
		type:"post",
		async:false,
		dataType : "text",
		contentType:"application/x-www-form-urlencoded",
		url: "/urms/orgFrame_loadTree?id="+orgFrameID,
		success : function(data){
			zTreeObj = $.fn.zTree.init($("#tree"), setting,eval(data));//初始化ztree
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});
}
//点击数显示相应的阅读人数
function getNodesReportUser(pId){
	$("#rangeSelect").html("");
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		//url: '/institution/nodes_user_list?orgFrameID='+pId+'&institutionVo_id='+institutionVo_id,
		url: '/institution/click_tree_data?orgFrameID='+pId+'&institutionVo_id='+institutionVo_id,
		success : function(data){
			if(data!=null){
				 for (var i in data) {
				 //	$("#rangeSelect").append("<option value='"+data[i]+"' >"+data[i]+"</option>");
				 	 $("#rangeSelect").append("<option value='"+data.notReadName[i]+"' >"+data.notReadName[i]+"</option>");
				 	
				 }
			}
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});
}
//点击数显示相应的未阅读人数
function getNodesReportNotReadUser(pId){
	$("#chooseUser").html("");
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		//url: '/institution/nodes_NotReaduser_list?orgFrameID='+pId+'&institutionVo_id='+institutionVo_id,
		url: '/institution/click_tree_data?orgFrameID='+pId+'&institutionVo_id='+institutionVo_id,
		success : function(data){
			if(data!=null){
				 for (var i in data) {
				 //	$("#chooseUser").append("<option value='"+data[i]+"' >"+data[i]+"</option>");
				 	 $("#chooseUser").append("<option value='"+data.notReadName[i]+"' >"+data.notReadName[i]+"</option>");
				 	
				 }
			}
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});
}

//获得用户
function getUser(pId){
	$("#rangeSelect").html("");
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		url: '/urms/user_getUserList?orgFrame.id='+pId,
		success : function(data){
			if(data!=null){
				 for (var i in data) {
				 	$("#rangeSelect").append("<option value='"+data[i].id+"' >"+data[i].userName+"</option>");
				 }
			}
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});
}

//增加
function on_add(){
	var so = $("#chooseUser option");
	if(selectNum=="single"){//单选
		if(so.length>0){
			autoMsg("只能选择一个用户",2);
			return false;
		}else{
			add_user();
		}
	}else{//多选
		if(so.length>29){//最多只能30个用户
			return false;
		}else{
			add_user();
		}
	}
}

//增加用户
function add_user(){
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
			   	$("#chooseUser").append("<option value='"+ro[i].value+"' >"+ro[i].text+"</option>");
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

//删除全部
function on_removeAll(){
	var o = $("#chooseUser option");
	 for(var i=0;i<o.length;i++){     
	     $(o[i]).remove();
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
	parent.frames[winName].$("#"+id).val(ids.substring(0,ids.length-1));
	parent.frames[winName].$("#"+name).val(names.substring(0,names.length-1));
	parent.layer.close(index);//关闭
}


//回调显示已选用户
function reSelectUser(){
	var ids =parent.frames[winName].$("#"+id).val();
	var names =parent.frames[winName].$("#"+name).val();
	if(ids!=null&&ids!=""){
		var idz = ids.split(",");
		var namez = names.split(",");
		for(var i=0;i<idz.length;i++){   
			$("#chooseUser").append("<option value='"+idz[i]+"' >"+namez[i]+"</option>");
		}
	}
}

//搜索查询
function on_search(){
	$("#rangeSelect").html("");
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		data:$('#baseForm').serialize(),
		url: '/urms/user_getUserList',
		success : function(data){
			if(data!=null){
				 for (var i in data) {
					 $("#rangeSelect").append("<option value='"+data[i].id+"' >"+data[i].userName+"</option>");
				 }
			}
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});
}

function on_close(){
	parent.layer.close(index);
}
</script>
</html>