<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>超级密码配置列表</title>
</head>
<body>
<div class="ibox-content" style="padding-top: 5px;">
	<!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<div class="row ibox-content">
		<div class="col-sm-12">
			<table id="grid"></table>
		</div>
	</div>
</div>
<script type="text/javascript"> 
    $(document).ready(function(){
    	loadGrid();//加载列表
    });
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: "/urms/superPassword_load",
    		dataType:"json",
    		method:"post",
    		queryParamsType: "limit",//服务器分页必须
    		striped:false,//条纹行
    		contentType: "application/x-www-form-urlencoded",
    		pageSize:10,//每页大小
    		pageNumber:1,//开始页
    		pageList:[10,20,50],
    		pagination:true,//显示分页工具栏
    		sidePagination: "server", //服务端请求
    		queryParams: queryParams,//发送请求参数
    	    columns: [{checkbox:true},
    	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
		           	  		return index+1;
		              }},
    	              {title: "加密密码",field: "encryptedText",align:"center"},
//    	              {title: "明文",field: "plaintext",align:"center"},
    	              {title: "创建时间",field: "createTime",align:"center",width:160},
    	              {title: "状态",field: "isEenabled",align:"center",width:120,formatter:function(value,row,index){
                          if(value ==1){
                              return "<i class=\"fa fa-circle\" style=\"color: #46BE8A\"></i> 已启用";
                          }else{
                              return "<i class=\"fa fa-circle\" style=\"color: #a8bbc2\"></i> 已停用";
                          }
                      }},
    				  {title: "操作", field: "", width: 120,align:"center",formatter:function(value,row,index){
		           	  		return "<a href='#' onclick='on_edit(\""+row.id+"\")'>编辑</a>";
		              }}]
    	 });
    }
    
  	//新增
  	function on_add(){
        var htmlCollection = document.getElementsByClassName("no-records-found");
        if(htmlCollection.length > 0){
			parent.layer.open({
				type: 2,
				title: "新增",
				shadeClose: true,//打开遮蔽
				shade: 0.6,
				maxmin: true, //开启最大化最小化按钮
				area: ["60%", "80%"],
				content: "/urms/superPassword_edit"
			})
        }else{
            autoAlert("已存在记录,请直接编辑!");
        }
  	}
  	
  	//删除菜单
  	function on_del(){
  		var rows = $("#grid").bootstrapTable("getSelections");
  		if(rows.length>0){
  			parent.layer.confirm("确定删除所选数据？", {
  				btn: ["确定","取消"] //按钮
  			}, function(){
		  		var ids = [];
				$.each(rows, function(index, item){
					ids.push(item.id);
				});
				$.ajax({
					type:"post",
					async:false,
					dataType : "json",
					url: '/urms/workDayConfig_delete?ids='+ids,
					success : function(data){
						if(data.result=="success"){
							$("#grid").bootstrapTable("refresh",{url:"/urms/workDayConfig_load"});//加载树下的列表
							autoMsg("删除成功！",1);
						}else{
							autoMsg("删除失败！",1);
						}
					},
					error : function(result){
						autoAlert("系统出错",5);
					}
				});
  			});
		}else{
			autoAlert("请选择删除的配置",5);
		}
  	}
  	
  	//编辑菜单
  	function on_edit(id){
  		parent.layer.open({
            type: 2,
            title: "编辑",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["60%", "80%"],
            content: "/urms/superPassword_edit?id="+id
        });
  	}



</script>
</body>
</html>