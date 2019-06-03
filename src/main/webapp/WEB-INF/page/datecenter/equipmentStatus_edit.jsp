<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>联网关键设备运行状态日常检查表编辑</title>
</head>
<body>
<div id="" class="ibox-content">
    <form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
        <%-- baseModule start --%>
        <input type="hidden" id="id" name="id" value="${equipmentStatusVo.id}" />
        <input type="hidden" id="creatorId" name="creatorId" value="${equipmentStatusVo.creatorId}" />
        <input type="hidden" id="creatorName" name="creatorName" value="${equipmentStatusVo.creatorName}" />
        <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${equipmentStatusVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
        <input type="hidden" id="sysCode" name="sysCode" value="${equipmentStatusVo.sysCode}" />
        <%-- baseModule end --%>

        <input type="hidden" id="ttId" name="ttId" value="${equipmentStatusVo.ttId}" />
        <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-15" />

        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
            <div class="col-sm-8">
                <c:if test="${not empty equipmentStatusVo.title}">
                    <input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${equipmentStatusVo.title}" data-rule-required-="true" data-rule-rangelength="[1,50]" />
                </c:if>
                <c:if test="${empty equipmentStatusVo.title}">
                    <input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${equipmentStatusVo.dutyDate}' pattern='yyyy年MM月dd日环龙运营控制指挥中心联网关键设备运行状态日常检查表'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
                </c:if>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
            <div class="col-sm-3">
                <input type="text" class="form-control" id="" name="" value="<fmt:formatDate value='${equipmentStatusVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true" />
                <%--disabled="disabled"
                <input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${equipmentStatusVo.dutyDate}' pattern='yyyy-MM-dd'/>" />--%>
            </div>
            <jsp:useBean id="now" class="java.util.Date" scope="page"/>
            <label class="col-sm-2 control-label"><span style="color: red">*</span>记录时间</label>
            <div class="col-sm-3">
                <c:if test="${equipmentStatusVo.checkTime != null}">
                    <input type="text" class="form-control" id="checkTime" name="checkTime" data-rule-required-="true" value="<fmt:formatDate value='${equipmentStatusVo.checkTime}' pattern='HH:mm'/>" onfocus="this.blur()"  onclick="WdatePicker({dateFmt:'HH:mm'})"  />
                </c:if>
                <c:if test="${equipmentStatusVo.checkTime == null}">
                    <input type="text" class="form-control" id="checkTime" name="checkTime" data-rule-required-="true" value="<fmt:formatDate value='${now}' pattern='HH:mm'/>" onfocus="this.blur()"  onclick="WdatePicker({dateFmt:'HH:mm'})"  />
                </c:if>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color: red">*</span>R1</label>
            <div class="col-sm-3">
                <opt:select dictKey="dc_eqStatus" classStyle="form-control" name="eqStatusR1" id="eqStatusR1" value="${equipmentStatusVo.eqStatusR1}" isDefSelect="false" />
            </div>
            <div class="col-sm-2 ">
                <div class="input-group">
                    <input type="text" class="form-control" id="successRateR1" name="successRateR1" value="${(equipmentStatusVo.successRateR1== "" || equipmentStatusVo.successRateR1 == null) ? "100" : equipmentStatusVo.successRateR1}" data-rule-required-="true" data-rule-num2="true" placeholder="标识成功率" />
                    <div class="input-group-addon">%</div>
                </div>
            </div>
            <div class="col-sm-3">
                <input type="text" class="form-control" id="mislabelNumR1" name="mislabelNumR1" value="${(equipmentStatusVo.successRateR1 == "" || equipmentStatusVo.successRateR1 == null) ? "0" : equipmentStatusVo.mislabelNumR1}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,3]" placeholder="误标数量" />
            </div>
        </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><span style="color: red">*</span>R2</label>
                <div class="col-sm-3">
                    <opt:select dictKey="dc_eqStatus" classStyle="form-control" name="eqStatusR2" id="eqStatusR2" value="${equipmentStatusVo.eqStatusR2}" isDefSelect="false" />
                </div>
                <div class="col-sm-2">
                    <div class="input-group">
                        <input type="text" class="form-control" id="successRateR2" name="successRateR2" value="${(equipmentStatusVo.successRateR2 == "" || equipmentStatusVo.successRateR2 == null) ? "100" : equipmentStatusVo.successRateR2}" data-rule-required-="true" data-rule-num2="true" placeholder="标识成功率" />
                        <div class="input-group-addon">%</div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="mislabelNumR2" name="mislabelNumR2" value="${(equipmentStatusVo.successRateR2 == "" || equipmentStatusVo.successRateR2 == null) ? "0" : equipmentStatusVo.mislabelNumR2}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,3]" placeholder="误标数量" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><span style="color: red">*</span>E1</label>
                <div class="col-sm-3">
                    <opt:select dictKey="dc_eqStatus" classStyle="form-control" name="eqStatusE1" id="eqStatusE1" value="${equipmentStatusVo.eqStatusE1}" isDefSelect="false" />
                </div>
                <div class="col-sm-2">
                    <div class="input-group">
                        <input type="text" class="form-control" id="successRateE1" name="successRateE1" value="${(equipmentStatusVo.successRateE1 == "" || equipmentStatusVo.successRateE1 == null) ? "100" : equipmentStatusVo.successRateE1}" data-rule-required-="true" data-rule-num2="true" placeholder="标识成功率" />
                        <div class="input-group-addon">%</div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="mislabelNumE1" name="mislabelNumE1" value="${(equipmentStatusVo.successRateE1 == "" || equipmentStatusVo.successRateE1 == null) ? "0" : equipmentStatusVo.mislabelNumE1}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,3]" placeholder="误标数量" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><span style="color: red">*</span>10306</label>
                <div class="col-sm-3">
                    <opt:select dictKey="dc_eqStatus" classStyle="form-control" name="eqStatusA" id="eqStatusA" value="${equipmentStatusVo.eqStatusA}" isDefSelect="false" />
                </div>
                <div class="col-sm-2">
                    <div class="input-group">
                        <input type="text" class="form-control" id="successRateA" name="successRateA" value="${(equipmentStatusVo.successRateA == "" || equipmentStatusVo.successRateA == null) ? "100" : equipmentStatusVo.successRateA}" data-rule-required-="true" data-rule-num2="true" placeholder="标识成功率" />
                        <div class="input-group-addon">%</div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="mislabelNumA" name="mislabelNumA" value="${(equipmentStatusVo.successRateA == "" || equipmentStatusVo.successRateA == null) ? "0" : equipmentStatusVo.mislabelNumA}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,3]" placeholder="误标数量" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><span style="color: red">*</span>10307</label>
                <div class="col-sm-3">
                    <opt:select dictKey="dc_eqStatus" classStyle="form-control" name="eqStatusB" id="eqStatusB" value="${equipmentStatusVo.eqStatusB}" isDefSelect="false" />
                </div>
                <div class="col-sm-2">
                    <div class="input-group">
                        <input type="text" class="form-control" id="successRateB" name="successRateB" value="${(equipmentStatusVo.successRateB == "" || equipmentStatusVo.successRateB == null) ? "100" : equipmentStatusVo.successRateB}" data-rule-required-="true" data-rule-num2="true" placeholder="标识成功率" />
                        <div class="input-group-addon">%</div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="mislabelNumB" name="mislabelNumB" value="${(equipmentStatusVo.successRateB == "" || equipmentStatusVo.successRateB == null) ? "0" : equipmentStatusVo.mislabelNumB}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,3]" placeholder="误标数量" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><span style="color: red">*</span>10308</label>
                <div class="col-sm-3">
                    <opt:select dictKey="dc_eqStatus" classStyle="form-control" name="eqStatusC" id="eqStatusC" value="${equipmentStatusVo.eqStatusC}" isDefSelect="false" />
                </div>
                <div class="col-sm-2">
                    <div class="input-group">
                        <input type="text" class="form-control" id="successRateC" name="successRateC" value="${(equipmentStatusVo.successRateC == "" || equipmentStatusVo.successRateC == null) ? "100" : equipmentStatusVo.successRateC}" data-rule-required-="true" data-rule-num2="true" placeholder="标识成功率" />
                        <div class="input-group-addon">%</div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="mislabelNumC" name="mislabelNumC" value="${(equipmentStatusVo.successRateC == "" || equipmentStatusVo.successRateC == null) ? "0" : equipmentStatusVo.mislabelNumC}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,3]" placeholder="误标数量" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><span style="color: red">*</span>10309</label>
                <div class="col-sm-3">
                    <opt:select dictKey="dc_eqStatus" classStyle="form-control" name="eqStatusD" id="eqStatusD" value="${equipmentStatusVo.eqStatusD}" isDefSelect="false" />
                </div>
                <div class="col-sm-2">
                    <div class="input-group">
                        <input type="text" class="form-control" id="successRateD" name="successRateD" value="${(equipmentStatusVo.successRateD == "" || equipmentStatusVo.successRateD == null) ? "100" : equipmentStatusVo.successRateD}" data-rule-required-="true" data-rule-num2="true" placeholder="标识成功率" />
                        <div class="input-group-addon">%</div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="mislabelNumD" name="mislabelNumD" value="${(equipmentStatusVo.successRateD == "" || equipmentStatusVo.successRateD == null) ? "0" : equipmentStatusVo.mislabelNumD}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,3]" placeholder="误标数量" />
                </div>
            </div>


            <div class="form-group">
             <label class="col-sm-2 control-label">备注</label>
             <div class="col-sm-8">
                 <textarea class="form-control" rows="6" cols="" id="remark" name="remark" data-rule-rangelength="[0,150]" >${equipmentStatusVo.remark}</textarea>
             </div>
         </div>
        <br><br><br>
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
    var winName = "${winName}";
    var URLStr = "/datecenter/equipmentStatus/equipmentStatus_";

    //新增或编辑
    function on_save(){
        if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。

            on_submit();//提交表单.  
        }else{
            autoMsg("信息提交不正确，请检查！", 5);
        }
    }

    //提交表单
    function on_submit(){
        $.ajax({
            type : 'post',
            async:false,
            dataType : 'json',
            url: URLStr + 'saveOrUpdate',
            data:$('#baseForm').serialize(),
            success : function(data) {
                if (data.result) {
                    autoMsg("保存成功！", 1);
                    parent.frames[winName].$("#grid").bootstrapTable("refresh", {
                        url : URLStr + "load"
                    });//加载树下的列表
                    parent.layer.close(index);
                } else {
                    autoAlert("保存失败，请检查！", 5);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                autoAlert("系统出错，请检查！", 5);
            }
        });
    }

</script>
</html>