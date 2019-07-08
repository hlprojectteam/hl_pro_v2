<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>考勤组编辑</title>
</head>
<body>
<div id="" class="ibox-content">
    <form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
        <input type="hidden" id="id" name="id" value="${attendanceGroupVo.id}" />
        <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${attendanceGroupVo.createTime}'  pattern='yyyy-MM-dd HH:mm:ss'/>"/>
        <input type="hidden" id="creatorId" name="creatorId" value="${attendanceGroupVo.creatorId}" />
        <input type="hidden" id="creatorName" name="creatorName" value="${attendanceGroupVo.creatorName}" />
        <input type="hidden" id="orgFrameId" name="orgFrameId" value="${attendanceGroupVo.orgFrameId}" />

        <%-- 第1行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>考勤组名称</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="groupName" name="groupName" value='${attendanceGroupVo.groupName}' data-rule-required="true" data-rule-rangelength="[1,20]" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>考勤组类型</label>
            <div class="col-sm-3">
                <opt:select dictKey="Attendance_GroupType" classStyle="form-control" id="groupType" name="groupType" value="${attendanceGroupVo.groupType}" />
            </div>
        </div>
        <%-- 第2行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>参与考勤人员</label>
            <div class="col-sm-8">
                <textarea class="form-control" rows="3" id="memberUserNames" name="memberUserNames" data-rule-required="true" readonly>${attendanceGroupVo.memberUserNames}</textarea>
                <input type="hidden" id="memberUserIds" name="memberUserIds" value="${attendanceGroupVo.memberUserIds}" data-rule-rangelength="[1,400]" />
            </div>
            <div class="col-sm-2">
                <button class="btn btn-primary " onclick="choose_user('memberUserIds','memberUserNames')" type="button" style="margin-left: 5px;"><i class="fa fa-external-link">&nbsp;选择人员</i></button>
            </div>
        </div>
        <%-- 第3行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>考勤组负责人</label>
            <div class="col-sm-8">
                <textarea class="form-control" rows="3" id="principalUserNames" name="principalUserNames" data-rule-required="true" readonly>${attendanceGroupVo.principalUserNames}</textarea>
                <input type="hidden" id="principalUserIds" name="principalUserIds" value="${attendanceGroupVo.principalUserIds}" data-rule-rangelength="[1,400]" />
            </div>
            <div class="col-sm-2">
                <button class="btn btn-primary " onclick="choose_user('principalUserIds','principalUserNames')" type="button" style="margin-left: 5px;"><i class="fa fa-external-link">&nbsp;选择人员</i></button>
            </div>
        </div>
        <%-- 第4行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>考勤组班次</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="shiftsName" name="shiftsName" value='${attendanceGroupVo.shiftsName}' data-rule-required="true" readonly />
                <input type="hidden" id="shiftsId" name="shiftsId" value="${attendanceGroupVo.shiftsId}" />
            </div>
            <div class="col-sm-2">
                <button class="btn btn-primary " onclick="on_selectShifts()" type="button" style="margin-left: 5px;"><i class="fa fa-external-link">&nbsp;选择班次</i></button>
            </div>
        </div>
        <%-- 第5行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>添加考勤地址</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="address" name="address" value='${attendanceGroupVo.address}' readonly data-rule-required="true" data-rule-rangelength="1,50" />
                <input type="hidden" id="x" name="x" value="${attendanceGroupVo.x}" />
                <input type="hidden" id="y" name="y" value="${attendanceGroupVo.y}" />
            </div>
            <div class="col-sm-2">
                <button class="btn btn-primary " onclick="on_selectAddress()" type="button" style="margin-left: 5px;"><i class="fa fa-external-link">&nbsp;选择地址</i></button>
            </div>
        </div>
        <%-- 第6行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>有效范围</label>
            <div class="col-sm-3">
                <opt:select dictKey="Effective_Range" classStyle="form-control" id="effectiveRange" name="effectiveRange" value="${attendanceGroupVo.effectiveRange}" />
            </div>
        </div>
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

<script type="text/javascript">
    //新增保存更新
    function on_save(){
        if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。
            on_submit();//提交表单.
        }
    }

    function on_submit(){
        $.ajax({
            type : 'post',
            async:false,
            dataType : 'json',
            url: '/attendance/attendanceGroup_saveOrUpdate',
            data:$('#baseForm').serialize(),
            success : function(data){
                if(data.result){
                    autoMsg("保存成功！",1);
                    iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/attendance/attendanceGroup_load"});//加载树下的列表
                    parent.layer.close(index);
                }else{
                    autoAlert("保存失败，请检查！",5);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                autoAlert("系统出错，请检查！",5);
            }
        });
    }


    //选择班次
    function on_selectShifts(){
        parent.layer.open({
            type: 2,
            title: "选择班次",
            shadeClose: true,//打开遮蔽
            shade: 0.6,
            maxmin: true, //开启最大化最小化按钮
            area: ["80%", "80%"],
            content: "/attendance/attendanceGroup_selectShifts?winName="+window.name
        });
    }

    //选择地址
    function on_selectAddress(){
        var x = $("#x").val();
        var y = $("#y").val();
        var address = $("#address").val();
        parent.layer.open({
            type: 2,
            title: "选择地址",
            shadeClose: true,//打开遮蔽
            shade: 0.6,
            maxmin: true, //开启最大化最小化按钮
            area: ["90%", "90%"],
            content: "/attendance/attendanceGroup_selectAddress?winName="+window.name +"&x="+x +"&y="+y +"&address="+address
        });
    }
</script>
</html>