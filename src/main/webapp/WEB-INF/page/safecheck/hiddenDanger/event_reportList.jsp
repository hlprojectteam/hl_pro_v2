<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>上报隐患列表</title>
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
    
    <!-- 搜索栏 start-->
    <menu:definition menuCode="${menuCode }"/>
    <div class="row ibox-content" style="padding:5px 0 5px 0;">
        <form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
          <div class="row">
            <label class="col-sm-1 control-label">隐患标题</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" id="eventTitle" name="eventTitle" value="" />
            </div>
            <input type="hidden" id="epNowPersonId" name="epNowPersonId" value="${epNowPersonId}" />
            <button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
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
    
    // 初始化页面参数(各种隐患类型的上报隐患数量)
    function pageInit() {
        var epNowPersonId = $("#epNowPersonId").val();
        
        $.ajax({
            type : 'post',
            async : true,
            dataType : 'json',
            data: {epNowPersonId:epNowPersonId},
            url : '/safecheck/hiddenDanger/event_getReportBaseCount',
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
            url: "/safecheck/hiddenDanger/event_loadReportList",
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
                      {title: "隐患标题", field: "eventTitle",align: "center",width: "27%",formatter:function(value,row,index){
                          var url = "<a href='#' style='color:blue;text-decoration:none;' onclick=\"td_click('" + row.id + "','" + row.epNowNode + "','" + row.epDealState + "')\">" + value + "</a>";
                          return url; 
                      }},
                      /* {title: "隐患编码",field: "eventCode",align: "center",width: "12%",formatter:function(value,row,index){
                          var url = '<a href="#" style="color:blue;text-decoration:none;" onclick="td_click(\'' + row.id + '\')">' + value + '</a>'; 
                          return url; 
                      }}, */
                      {title: "紧急程度", field: "eventurgency",align: "center",width: "8%",formatter:function(value,row,index){
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
                      /* {title: "隐患类型", field: "eventType",align: "center",width: "8%",formatter:function(value,row,index){
                          return changeDataDictByKey("event_Type",value);
                      }}, */
                      /* {title: "上报人", field: "creatorName",align:"center"}, */
                      {title: "上报时间", field: "createTime",align:"center",width: "15%"},
                      {title: "当前节点", field: "epNowNode",align: "center",width: "15%",formatter:function(value,row,index){
                          return changeDataDictByKey("event_EventNode",value);
                      }},
                      {title: "处理角色", field: "epNowRoleName",align: "center",width: "8%"},
                      {title: "处理状态", field: "epDealState",align: "center",width: "8%",formatter:function(value,row,index){
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
    
    // 新增
    function on_add(){
        parent.layer.open({
            type: 2,
            title: "上报隐患",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["80%", "80%"],
            content: "/safecheck/hiddenDanger/event_reportEdit?winName="+winName
        });
    }
    
    // 查询
    function on_search(){
        $("#grid").bootstrapTable("refresh");
    }
    
    // 单元格点击隐患(隐患编码,隐患标题两个单元格)
    function td_click(id,epNowNode,epDealState) {
        parent.layer.open({
            type: 2,
            title: '上报隐患信息',
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["100%", "100%"],
            content: "/safecheck/hiddenDanger/event_reportDetail?id="+id+"&epNowNode="+epNowNode+"&epDealState="+epDealState
          });
    }
</script>
</body>
</html>