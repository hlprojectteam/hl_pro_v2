<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>所有隐患列表</title>
    <script src="/common/plugins/bootstrapTable/js/bootstrap-table-fixed-columns.js"></script>
    <link rel="stylesheet" type="text/css" href="/common/plugins/bootstrapTable/css/bootstrap-table-fixed-columns.css">
    <link rel="stylesheet" href="/common/baseLib/css/baseCount.css" type="text/css">
    <!-- 数字增长显示 -->
    <script type="text/javascript" src="/common/js/utils.js"></script>
</head>
<body>
<div class="ibox-content" style="padding-top: 5px;">
    <!-- 基础数据统计 开始-->
    <div id="baseCount">
    </div>
    <!-- 基础数据统计 结束-->
    
    <!-- 顶部按钮 start-->
    <!-- <div class="row" style="padding:0px 10px 5px 10px;">
        <button onclick="on_del()" class="btn btn-danger"><i class="fa fa-remove"></i>&nbsp;删除</button>
    </div> -->
    <!-- 顶部按钮 end-->
    
    <!-- 搜索栏 start-->
    <menu:definition menuCode="${menuCode }"/>
    <div class="row ibox-content" style="padding:5px 0 5px 0;">
        <form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
        	 <input type="hidden" id="epNowPersonId" name="epNowPersonId" value="${epNowPersonId}" />
          <div class="row">
          	<label class="col-sm-1 control-label">起始日期</label>
			<div class="col-sm-2">
				<input type="text" class="form-control" id="eventDateStart" name="eventDateStart" value="" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
			</div>
			<label class="col-sm-1 control-label">终止日期</label>
			<div class="col-sm-2">
				<input type="text" class="form-control" id="eventDateEnd" name="eventDateEnd" value="" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
			</div>
            
            
          </div>
          
          <div class="row" style="margin-top: 10px;">
          	<label class="col-sm-1 control-label">隐患标题</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" id="eventTitle" name="eventTitle" value="" />
            </div>
           
			<label class="col-sm-1 control-label">是否办结</label>
			<div class="col-sm-2">
				<opt:select dictKey="event_WhetherFinish" classStyle="form-control" id="epWhetherFinish" name="epWhetherFinish" isDefSelect="true"/>
			</div>
			
			<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
            <button class="btn btn-primary" type="button" onclick="on_export()"><i class="fa fa-file-excel-o"></i>&nbsp;导出Excle</button>
		</div>
          
          
        </form>
    </div>
    <!-- 搜索栏 end -->
    
    <!-- 表格列表 start -->
    <div class="row ibox-content">
        <div class="col-sm-12">
            <table id="grid"></table>
        </div>
    </div>
    <!-- 表格列表 end -->
</div>
<script type="text/javascript"> 
    var winName = window.name;
    
    $(function(){
        //pageInit();//初始化页面参数
        loadGrid();//加载列表
    });
    
    // 初始化页面参数(各种隐患类型的所有隐患数量)
    function pageInit() {
        var epNowPersonId = $("#epNowPersonId").val();
        
        $.ajax({
            type : 'post',
            async : true,
            dataType : 'json',
            data: {epNowPersonId:epNowPersonId},
            url : '/subSystem/event/event_getAllBaseCount',
            success : function(data) {
                var total = 0;      
                var htm = "";
                /* div格式:
                        <div class="num_x">
                            <p>xxx</p>
                            <h1>xxx</h1>
                        </div>
                */
                for(var k in data){
                    htm += "<div class='num_" + k + "'><p>" + changeDataDictByKey("event_Type",k) + "</p><h1 id='eventType" + k + "'>" + 0 + "</h1></div>"
                    total += data[k];
                }
                var primateHtm = "<div class='num_0'><p>隐患总数</p><h1 id='eventTypeAll'>" + 0 + "</h1></div>"
                $("#baseCount").html(primateHtm + htm);
                
                
                // 数字增长显示
                countUp(document.getElementById("eventTypeAll"), total, 0, 1000, 0);
                for(var k in data){
                    countUp(document.getElementById("eventType"+k), data[k], 0, 1000, 0);
                }
            },
        });
    }
    
    //加载列表
    function loadGrid() {
        $("#grid").bootstrapTable({
            url: "/safecheck/hiddenDanger/event_loadAllList",
            dataType:"json",
            method:"post",
            queryParamsType: "limit",//服务器分页必须
            striped:true,//条纹行
            contentType: "application/x-www-form-urlencoded",
            pageSize:10,//每页大小
            pageNumber:1,//开始页
            pageList:[10,20,50],
            pagination:true,//显示分页工具栏
            sidePagination: "server", //服务端请求
            queryParams: queryParams,//发送请求参数
            /* fixedColumns: true,      //冻结列
            fixedNumber: 3,          //冻结前3列 */
            columns: [{checkbox:true,width: "3%"},
                      {title: "序号",field: "id",align: "center",width: "3%",formatter:function(value,row,index){
                            return index+1;
                      }},
                      {title: "隐患标题", field: "eventTitle",align: "center",width: "20%",formatter:function(value,row,index){
                          var url = "<a href='#' style='color:blue;text-decoration:none;' onclick=\"td_click('" + row.id + "','" + row.epNowNode + "','" + row.epDealState + "')\">" + value + "</a>";
                          return url; 
                      }},
                      {title: "序列号", field: "eventCode",align:"center",width: "8%"},
                      /* {title: "隐患编码",field: "eventCode",align: "center",width: "12%",formatter:function(value,row,index){
                          var url = '<a href="#" style="color:blue;text-decoration:none;" onclick="td_click(\'' + row.id + '\')">' + value + '</a>'; 
                          return url; 
                      }}, */
                      {title: "紧急程度", field: "eventurgency",align: "center",width: "6%",formatter:function(value,row,index){
                          if(value == 1 ){
                              return "<span class=\"label label-success\">一般</span>";
                          }
                          if(value == 2){
                              return "<span class=\"label label-warning\">较大</span>";
                          }
                          if(value == 3) {
                              return "<span class=\"label label-danger\">重大</span>";
                          }
                         // return changeDataDictByKey("event_Urgency",value);
                      }},
                      {title: "上报人", field: "creatorName",align:"center",width: "8%"},
                      {title: "上报时间", field: "createTime",align:"center",width: "12%"},
                      {title: "当前节点", field: "epNowNode",align: "center",width: "12%",formatter:function(value,row,index){
                          return changeDataDictByKey("event_EventNode",value);
                      }},
                      {title: "处理角色", field: "epNowRoleName",align: "center",width: "10%"},
                      {title: "处理状态", field: "epDealState",align: "center",width: "6%",formatter:function(value,row,index){
                          if(value == 1 ){
                              return "<i class=\"fa fa-circle\" style=\"color: #F96868\"></i> "+changeDataDictByKey("event_DealState",value);
                          }
                          if(value == 2){
                              return "<i class=\"fa fa-circle\" style=\"color: #F2A654\"></i> "+changeDataDictByKey("event_DealState",value);
                          }
                          if(value == 3) {
                              return "<i class=\"fa fa-circle\" style=\"color: #46BE8A\"></i> "+changeDataDictByKey("event_DealState",value);
                          }
                         // return changeDataDictByKey("event_DealState",value);
                      }}],
         });
    }
    

    // 删除
//    function on_del(){
//        var table = "grid";
//        var url = "/subSystem/event/event_delete";
//        var refreshUrl = "/subSystem/event/event_loadAllList";
//        delete_tableData(table, url, refreshUrl);
//    }
    
    //删除
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
					url: "/safecheck/hiddenDanger/event_delete?ids="+ids,
					success : function(data){
						if(data.result){
							$("#grid").bootstrapTable("refresh",{url: "/safecheck/hiddenDanger/event_loadAllList"});//加载树下的列表
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
			autoAlert("请选择删除的记录",5);
		}
  	}
    
    // 查询
    function on_search(){
        $("#grid").bootstrapTable("refresh");
    }
    
    // 单元格点击隐患(隐患编码,隐患标题两个单元格)
    function td_click(id,epNowNode,epDealState) {
        parent.layer.open({
            type: 2,
            title: '隐患明细信息',
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["100%", "100%"],
            content: "/safecheck/hiddenDanger/event_allDetail?id="+id+"&epNowNode="+epNowNode+"&epDealState="+epDealState
          });
    }
    
  //导出Excel
    function on_export(){
        let star = $("#eventDateStart").val();
        let end = $("#eventDateEnd").val();
        if(star == null || star == "" || end == null || end == ""){
            autoAlert("注意：导出Excel时筛选条件不能为空!",5);
        }else if((new Date(end) - new Date(star))/(24*60*60*1000) > 92){
            autoAlert("注意：导出Excel时筛选时间区间必须三个月以内，以防数据量过大!",5);
        }else{
            window.location.href = "/safecheck/hiddenDanger/event_export?eventDateStart="+$("#eventDateStart").val()+"&eventDateEnd="+$("#eventDateEnd").val();
        }
    }
</script>
</body>
</html>