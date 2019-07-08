<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>外勤作业编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${fieldOperationsVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${fieldOperationsVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${fieldOperationsVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${fieldOperationsVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${fieldOperationsVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${fieldOperationsVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-14" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty fieldOperationsVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${fieldOperationsVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty fieldOperationsVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${fieldOperationsVo.dutyDate}' pattern='yyyy年MM月dd日外勤作业'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${fieldOperationsVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true"  />
				<%--disabled="disabled"
				<input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${fieldOperationsVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
				--%>
	        </div>
        </div>
	  	
	  	<div class="form-group">
	  		<label class="col-sm-2 control-label"><span style="color: red">*</span>接报时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="receiptTime" name="receiptTime" value="<fmt:formatDate value='${fieldOperationsVo.receiptTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" data-rule-required="true"  />
	        </div>
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>报告人员 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="reportedPerson" name="reportedPerson" value="${fieldOperationsVo.reportedPerson}" data-rule-required="true" data-rule-rangelength="[1,20]" />    
			</div>
		</div>
	 
	    <div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>接报方式</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_receiptWay" classStyle="form-control required" name="receiptWay" id="receiptWay" value="${fieldOperationsVo.receiptWay}" isDefSelect="true" />
			</div>
			<div class="dictValue"  style="display: none;">
				<label class="col-sm-2 control-label"><span style="color: red">*</span>请输入要添加的接报方式</label>
				<div class="col-sm-3">
					<input type="text" class="form-control" id="dictValue" name="dictValue" value="${fieldOperationsVo.dictValue}" data-rule-rangelength="[1,15]" />
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>外勤人员 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="outworker" name="outworker" value="${fieldOperationsVo.outworker}" data-rule-required="true" data-rule-rangelength="[1,20]" />    
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>事发地点 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="scene" name="scene" value="${fieldOperationsVo.scene}" data-rule-required="true" data-rule-rangelength="[1,30]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>涉事单位</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="involvedUnits" name="involvedUnits" value="${fieldOperationsVo.involvedUnits}" data-rule-required="true" data-rule-rangelength="[1,30]" />    
			</div>
		</div>
		
		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>违章单号</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="violationOrderNo" name="violationOrderNo" value="${fieldOperationsVo.violationOrderNo}" data-rule-required="true" data-rule-rangelength="[1,30]" />    
			</div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>接报情况</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="8" cols="" id="receiptSituation" name="receiptSituation" data-rule-required="true" data-rule-rangelength="[1,1000]" >${fieldOperationsVo.receiptSituation}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>处置简述</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="8" cols="" id="disposeDesc" name="disposeDesc" data-rule-required="true" data-rule-rangelength="[1,1000]" >${fieldOperationsVo.disposeDesc}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label">备注</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="remark" name="remark" data-rule-rangelength="[0,250]" >${fieldOperationsVo.remark}</textarea>
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
	var URLStr = "/datecenter/fieldOperations/fieldOperations_";

	//新增或编辑
	function on_save(){
		if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
	        on_submit();//提交表单.  
	    }else{
            autoMsg("信息提交不正确，请检查！", 5);
        }
	}


    $("#receiptWay").change(function(){
        var receiptWay = $("#receiptWay").val();
        if(receiptWay == 99){
            $(".dictValue").show();
        }else{
            $(".dictValue").hide();
            $(".dictValue").val(null)
        }
    });

	//提交表单
	function on_submit(){
        if($("#receiptWay").val() == 99 && ($("#dictValue").val() == null || $("#dictValue").val() == "")){
            autoMsg("新添加的字典类型不能为空", 5);
        }else{
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
	}

</script>
</html>