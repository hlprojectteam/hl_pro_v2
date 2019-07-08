<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>考勤班次列表</title>
    <!-- ztree -->
    <link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
</head>
<body>
<div class="ibox-content" style="padding-top:5px;">
    <!-- 工具条 -->
    <menu:definition menuCode="${menuCode }"/>
    <div class="row ibox-content" style="padding:5px 0 5px 0;">
        <form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
            <div class="row">
                <label class="col-sm-1 control-label">班次名称</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" id="shiftName" name="shiftName" value="" />
                </div>
                <button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
            </div>
        </form>
    </div>

    <input type="hidden" id="orgFrameId">
    <div class="row ibox-content">
        <div class="col-sm-2">
            <ul id="tree" class="ztree"></ul>
        </div>
        <div class="col-sm-10">
            <table id="grid"></table>
        </div>
    </div>
</div>
<script type="text/javascript">
    /* 行政区划树 相关 ↓ */
    var zTreeObj;
    $(document).ready(function(){
        zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
        loadGrid();//加载列表
    });

    //初始化树
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
            url: "/urms/orgFrame_loadTreeByOrgFrameType?orgFrameType=2",
            autoParam: ["id"]
        }
    };

    //点击树后回调
    function zTreeOnClick(event, treeId, treeNode) {
        $("#orgFrameId").val(treeNode.id);
        $("#grid").bootstrapTable("refresh",{url:"/attendance/shifts_load?orgFrameId="+treeNode.id});//加载树下的列表
    }
    /* 行政区划树 相关 ↑ */


    //加载列表
    function loadGrid() {
        $("#grid").bootstrapTable({
            url: "/attendance/shifts_load",
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
            columns: [{checkbox:true},
                {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
                        return index+1;
                    }},
                {title: "班次名称",field: "shiftName",width: 100,align:"center"},
                {title: "上班打卡时间",field: "workTimeStart",width: 100,align:"center",formatter:function(value,row,index){
                        return value.substr(11,5);
                    }},
                {title: "下班打卡时间", field: "workTimeEnd", width: 100,align:"center",formatter:function(value,row,index){
                        return value.substr(11,5);
                    }},
                {title: "下班是否不用打卡", field: "isNotClockOut", width: 100,align:"center",formatter:function(value,row,index){
                        return changeDataDictByKey("isNot",value);
                    }},
                {title: "操作", field: "", width: 120,align:"center",formatter:function(value,row,index){
                        var json = JSON.stringify(row);
                        var tb = table_button.split("&nbsp;&nbsp;");
                        var html = "";
                        for (var i = 0; i < tb.length; i++) {
                            html+=tb[i]+"&nbsp;&nbsp;";
                        }
                        return html.replace(new RegExp("row_id_","gm"),""+json+"");
                    }}]
        });
    }

    //新增
    function on_add(){
        var orgFrameId = $("#orgFrameId").val();
        if(orgFrameId=="" || orgFrameId==null){
            autoAlert("请选择一个收费站!",5);
        }else{
            parent.layer.open({
                type: 2,
                title: "新增",
                shadeClose: true,//打开遮蔽
                shade: 0.6,
                maxmin: true, //开启最大化最小化按钮
                area: ["80%", "80%"],
                content: "/attendance/shifts_edit?orgFrameId="+orgFrameId
            });
        }
    }

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
                    url: "/attendance/shifts_delete?ids="+ids,
                    success : function(data){
                        if(data.result){
                            $("#grid").bootstrapTable("refresh",{url:"/attendance/shifts_load"});//加载树下的列表
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

    //编辑
    function on_edit(obj){
        parent.layer.open({
            type: 2,
            title: "编辑",
            shadeClose: true,//打开遮蔽
            shade: 0.6,
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: "/attendance/shifts_edit?id="+obj.id
        });
    }

    //查询
    function on_search(){
        $("#grid").bootstrapTable("refresh");
    }

</script>
</body>
</html>