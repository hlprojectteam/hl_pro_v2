<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>考勤班次编辑</title>
</head>
<body>
<div id="" class="ibox-content">
    <form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
        <input type="hidden" id="id" name="id" value="${shiftsVo.id}" />
        <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${shiftsVo.createTime}'  pattern='yyyy-MM-dd HH:mm:ss'/>"/>
        <input type="hidden" id="creatorId" name="creatorId" value="${shiftsVo.creatorId}" />
        <input type="hidden" id="creatorName" name="creatorName" value="${shiftsVo.creatorName}" />
        <input type="hidden" id="orgFrameId" name="orgFrameId" value="${shiftsVo.orgFrameId}" />

        <%-- 第1行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>班次名称</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="shiftName" name="shiftName" value='${shiftsVo.shiftName}' data-rule-required="true" data-rule-rangelength="[1,10]" />
            </div>
        </div>
        <%-- 第2行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>上班打卡时间</label>
            <div class="col-sm-3">
                <input type="text" class="form-control" id="workTimeStart" name="workTimeStart" value="<fmt:formatDate value='${shiftsVo.workTimeStart}' pattern='HH:mm:ss'/>" data-rule-required="true" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
            </div>
            <label class="col-sm-2 control-label"><span style="color: red"></span>下班打卡时间</label>
            <div class="col-sm-3">
                <input type="text" class="form-control" id="workTimeEnd" name="workTimeEnd" value="<fmt:formatDate value='${shiftsVo.workTimeEnd}' pattern='HH:mm:ss'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
            </div>
        </div>
        <%-- 第3行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red"></span>是否有休息时间</label>
            <div class="col-sm-3">
                <opt:select dictKey="isNot" classStyle="form-control" id="isHaveRestTime" name="isHaveRestTime" value="${shiftsVo.isHaveRestTime}" />
            </div>
        </div>
        <%-- 第4行 --%>
        <div class="form-group  restTimeDiv" style="display: none;">
            <label class="col-sm-2 control-label"><span style="color: red"></span>休息时间开始</label>
            <div class="col-sm-3">
                <input type="text" class="form-control" id="restTimeStart" name="restTimeStart" value="<fmt:formatDate value='${shiftsVo.restTimeStart}' pattern='HH:mm:ss'/>"  onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
            </div>
            <label class="col-sm-2 control-label"><span style="color: red"></span>休息时间结束</label>
            <div class="col-sm-3">
                <input type="text" class="form-control" id="restTimeEnd" name="restTimeEnd" value="<fmt:formatDate value='${shiftsVo.restTimeEnd}' pattern='HH:mm:ss'/>"  onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm:ss'})"/>
            </div>
        </div>

        <%-- 第5行 --%>
        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red"></span>上班打卡前几分钟提醒</label>
            <div class="col-sm-3">
                <opt:select dictKey="Before_Punching" classStyle="form-control" id="remindTimeType" name="remindTimeType" value="${shiftsVo.remindTimeType}" />
            </div>
            <label class="col-sm-2 control-label"><span style="color: red"></span>下班是否不用打卡</label>
            <div class="col-sm-3">
                <opt:select dictKey="isNot" classStyle="form-control" id="isNotClockOut" name="isNotClockOut" value="${shiftsVo.isNotClockOut}" />
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

    $(function () {
        changeRestTime();
    });
    //是否有休息时间状态改变
    $("#isHaveRestTime").change(function () {
        changeRestTime();
    });
    function changeRestTime() {
        if($("#isHaveRestTime").val() == 1){
            $(".restTimeDiv").show();
        }else {
            $(".restTimeDiv").hide();
            $("#restTimeStart").val(null);
            $("#restTimeEnd").val(null);
        }
    }

    //新增保存更新
    function on_save(){
        if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。

            //当有休息时间时,休息时间段不能为空
            if($("#isHaveRestTime").val()==1 && ($("#restTimeStart").val()=="" || $("#restTimeEnd").val()=="")){
                autoMsg("休息时间不能为空！",5);
                return false;
            }

            on_submit();//提交表单.
        }
    }

    function on_submit(){
        $.ajax({
            type : 'post',
            async:false,
            dataType : 'json',
            url: '/attendance/shifts_saveOrUpdate',
            data:$('#baseForm').serialize(),
            success : function(data){
                if(data.result){
                    autoMsg("保存成功！",1);
                    iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/attendance/shifts_load"});//加载树下的列表
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

</script>
</html>