<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>用户选择列表</title>
    <!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
</head>
<body>
<div class="row ibox-content" style="padding:5px 0 5px 0;">
    <form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
        <div class="row">
            <label class="col-sm-1 control-label">姓名</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" id="name" name="name" value="" />
            </div>
            <!-- <label class="col-sm-1 control-label">身份证号</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" id="idNumber" name="idNumber" value="" />
            </div> -->
            <button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
        </div>
    </form>
</div>
<div class="ibox-content" style="padding-top: 5px;">
    <table id="grid"></table>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_sure()"><i class="fa fa-check"></i>&nbsp;确定</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
<script type="text/javascript"> 
var dataAreaCode = '${dataAreaCode}';
var winName = '${winName}';

$(document).ready(function(){
    loadGrid(dataAreaCode);//加载列表
});
    
//加载列表
function loadGrid(dataAreaCode) {
    $("#grid").bootstrapTable({
        url: "/urms/user_load?sign=2&dataAreaCode="+dataAreaCode,
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
        columns: [{radio:true},
                  {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
                    return index+1;
            }},
                  {title: "姓名",field: "userName", width: 100,align:"center"},
                  {title: "性别", field: "sex", width: 100,align:"center",formatter:function(value,row,index){
                      return changeDataDictByKey("population_Sex",value);
                  }}]
//                ,
//                {title: "操作", field: "", width: 100,align:"center",formatter:function(value,row,index){
//                  return "<a href='#' onclick='on_edit(\""+row.id+"\")'>编辑</a>";
//            }}]
     });
}
    
    //确定
    function on_sure(){
        var rows = $("#grid").bootstrapTable("getSelections");
        if(rows.length>0){
            var id;
            var name;
            var sex;
            var mobile;
            var phone;
            var dataAreaCode;
            $.each(rows, function(index, item){
                id=(item.id);
                name=(item.userName);
                sex=(item.sex);
                mobile=(item.mobilePhone);
                phone=(item.telephone);
                dataAreaCode=(item.dataAreaCode);
            });
            parent.frames[winName].$("#userId").val(id);
            parent.frames[winName].$("#name").val(name);
            parent.frames[winName].$("#sex").val(sex);
            parent.frames[winName].$("#mobile").val(mobile);
            parent.frames[winName].$("#phone").val(phone);
            parent.frames[winName].$("#dataAreaCode").val(dataAreaCode);
            parent.layer.close(index);
        }else{
            autoMsg("请选择用户！",1);
        }
    }

//查询
function on_search(){
    $("#grid").bootstrapTable("refresh");
}
</script>

</body>
</html>