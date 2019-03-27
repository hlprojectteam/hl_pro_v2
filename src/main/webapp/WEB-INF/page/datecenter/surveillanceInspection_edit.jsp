<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>监控巡检编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${surveillanceInspectionVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${surveillanceInspectionVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${surveillanceInspectionVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${surveillanceInspectionVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${surveillanceInspectionVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${surveillanceInspectionVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-03" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty surveillanceInspectionVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${surveillanceInspectionVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty surveillanceInspectionVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${surveillanceInspectionVo.dutyDate}' pattern='yyyy年MM月dd日监控巡检'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${surveillanceInspectionVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true" disabled="disabled"  />
	            <input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${surveillanceInspectionVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
	        </div>
	        <label class="col-sm-2 control-label"><span style="color: red">*</span>天气</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_weather" classStyle="form-control" name="weather" id="weather" value="${surveillanceInspectionVo.weather}" isDefSelect="false" />
			</div>
        </div>
	  	
	  	<div class="form-group">
	  		<label class="col-sm-2 control-label"><span style="color: red">*</span>巡检时间开始</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="inspectionTimeStart" name="inspectionTimeStart" value="<fmt:formatDate value='${surveillanceInspectionVo.inspectionTimeStart}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" data-rule-required="true"  />
	        </div>
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>巡检时间结束</label>
		    <div class="col-sm-3">
	            <input type="text" class="form-control" id="inspectionTimeEnd" name="inspectionTimeEnd" value="<fmt:formatDate value='${surveillanceInspectionVo.inspectionTimeEnd}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" data-rule-required="true"  />
			</div>
		</div>
	 
	    <div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>值班主任</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="shiftSupervisor" name="shiftSupervisor" value="${surveillanceInspectionVo.shiftSupervisor}" data-rule-required="true" data-rule-rangelength="[1,20]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>巡检位置</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_inspectionlocation" classStyle="form-control required" name="inspectionlocation" id="inspectionlocation" value="${surveillanceInspectionVo.inspectionlocation}" isDefSelect="true" />
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>巡检情况描述</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="inspectionDetails" name="inspectionDetails" data-rule-required="true" data-rule-rangelength="[1,500]" >${surveillanceInspectionVo.inspectionDetails}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label">跟进措施</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="3" cols="" id="followMeasure" name="followMeasure" data-rule-required="true" data-rule-rangelength="[0,100]" >${surveillanceInspectionVo.followMeasure}</textarea>
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
	var URLStr = "/datecenter/surveillanceInspection/surveillanceInspection_";

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