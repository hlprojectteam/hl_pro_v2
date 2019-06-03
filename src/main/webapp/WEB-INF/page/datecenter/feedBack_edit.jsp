<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>顾客意见反馈编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${feedBackVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${feedBackVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${feedBackVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${feedBackVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${feedBackVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${feedBackVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-12" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty feedBackVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${feedBackVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty feedBackVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${feedBackVo.dutyDate}' pattern='yyyy年MM月dd日顾客意见反馈'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${feedBackVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true" />
				<%--disabled="disabled"
				<input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${feedBackVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
				--%>
	        </div>
        </div>
	  	
	  	<div class="form-group">
	  		<label class="col-sm-2 control-label"><span style="color: red">*</span>接报时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="receiptTime" name="receiptTime" value="<fmt:formatDate value='${feedBackVo.receiptTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" data-rule-required="true"  />
	        </div>
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>报告人员 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="reportedPerson" name="reportedPerson" value="${feedBackVo.reportedPerson}" data-rule-required="true" data-rule-rangelength="[1,30]" />    
			</div>
		</div>
	 
	    <div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>性别</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="sex" classStyle="form-control required" name="customerSex" id="customerSex" value="${feedBackVo.customerSex}" isDefSelect="true" />
			</div>
			<label class="col-sm-2 control-label">车辆号牌 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="plateNum" name="plateNum" value="${feedBackVo.plateNum}" data-rule-rangelength="[0,12]" />
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>联系电话 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="customerPhone" name="customerPhone" value="${feedBackVo.customerPhone}" data-rule-required="true" data-rule-phone="true" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>反馈类型</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_fbType" classStyle="form-control required" name="fbType" id="fbType" value="${feedBackVo.fbType}" isDefSelect="true" />
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>值班员 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="watcher" name="watcher" value="${feedBackVo.watcher}" data-rule-required="true" data-rule-rangelength="[1,20]" />    
			</div>
		</div>
		
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>情况概述</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="situationDesc" name="situationDesc" data-rule-required="true" data-rule-rangelength="[1,250]" >${feedBackVo.situationDesc}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>处理情况</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="disposalSituation" name="disposalSituation" data-rule-required="true" data-rule-rangelength="[1,250]" >${feedBackVo.disposalSituation}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label">备注</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="remark" name="remark" data-rule-rangelength="[0,250]" >${feedBackVo.remark}</textarea>
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
	var URLStr = "/datecenter/feedBack/feedBack_";

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