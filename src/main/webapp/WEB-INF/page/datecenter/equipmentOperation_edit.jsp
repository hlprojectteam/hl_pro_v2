<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>设备运行情况统计表编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${equipmentOperationVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${equipmentOperationVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${equipmentOperationVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${equipmentOperationVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${equipmentOperationVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${equipmentOperationVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-05" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty equipmentOperationVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${equipmentOperationVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty equipmentOperationVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${equipmentOperationVo.dutyDate}' pattern='yyyy年MM月dd日各站车道设备运行情况统计表'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="" name="" value="<fmt:formatDate value='${equipmentOperationVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true" disabled="disabled"  />
	            <input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${equipmentOperationVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
	        </div>
        </div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>收费站</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_tollGate" classStyle="form-control" name="tollGate" id="tollGate" value="${equipmentOperationVo.tollGate}" isDefSelect="false" />
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>车道高清抓拍</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_equipmentStatus" classStyle="form-control" name="cdgqzp" id="cdgqzp" value="${equipmentOperationVo.cdgqzp}" isDefSelect="false" />
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>自动发卡机</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_equipmentStatus" classStyle="form-control" name="zdfkj" id="zdfkj" value="${equipmentOperationVo.zdfkj}" isDefSelect="false" />
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>MTC出口车道</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_equipmentStatus" classStyle="form-control" name="mtcckcd" id="mtcckcd" value="${equipmentOperationVo.mtcckcd}" isDefSelect="false" />
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>ETC出口车道</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_equipmentStatus" classStyle="form-control" name="etcckcd" id="etcckcd" value="${equipmentOperationVo.etcckcd}" isDefSelect="false" />
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>MTC入口车道</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_equipmentStatus" classStyle="form-control" name="mtcrkcd" id="mtcrkcd" value="${equipmentOperationVo.mtcrkcd}" isDefSelect="false" />
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>ETC入口车道</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_equipmentStatus" classStyle="form-control" name="etcrkcd" id="etcrkcd" value="${equipmentOperationVo.etcrkcd}" isDefSelect="false" />
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>计重车道</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_equipmentStatus" classStyle="form-control" name="jzcd" id="jzcd" value="${equipmentOperationVo.jzcd}" isDefSelect="false" />
			</div>
		</div>
		
		<div class="form-group">
	  		<label class="col-sm-2 control-label">车道停用时间开始</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="downTimeStart" name="downTimeStart" value="<fmt:formatDate value='${equipmentOperationVo.downTimeStart}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})"  />
	        </div>
		  	<label class="col-sm-2 control-label">车道停用时间结束 </label>
		    <div class="col-sm-3">
	            <input type="text" class="form-control" id="downTimeEnd" name="downTimeEnd" value="<fmt:formatDate value='${equipmentOperationVo.downTimeEnd}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})"  />
			</div>
		</div>
	 
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label">备注</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="remark" name="remark" data-rule-rangelength="[0,250]" >${equipmentOperationVo.remark}</textarea>
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
	var URLStr = "/datecenter/equipmentOperation/equipmentOperation_";

	//新增或编辑
	function on_save(){
		if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
	        on_submit();//提交表单.  
	    }else{
            autoAlert("信息提交不正确，请检查！", 5);
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