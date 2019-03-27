<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>营运数据编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${operatingDataVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${operatingDataVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${operatingDataVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${operatingDataVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${operatingDataVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${operatingDataVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-06" />
		
		<br><br><br><br>
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty operatingDataVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${operatingDataVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty operatingDataVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${operatingDataVo.dutyDate}' pattern='yyyy年MM月dd日各站营运数据'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${operatingDataVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true" disabled="disabled"  />
	            <input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${operatingDataVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
	        </div>
	        <label class="col-sm-2 control-label"><span style="color: red">*</span>收费站</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_tollGate" classStyle="form-control required" name="tollGate" id="tollGate" value="${operatingDataVo.tollGate}" isDefSelect="true"  />
			</div>
        </div>
	  	
	  	<div class="form-group">
	  		<label class="col-sm-2 control-label"><span style="color: red">*</span>出口车流量_总车流</label>
	        <div class="col-sm-3">
				<input type="text" class="form-control" id="totalTraffic" name="totalTraffic" value="${operatingDataVo.totalTraffic}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,8]" />    
	        </div>
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>出口车流量_粤通卡车流 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="ytkTraffic" name="ytkTraffic" value="${operatingDataVo.ytkTraffic}" data-rule-required="true" data-rule-digits="true" data-rule-rangelength="[1,8]" />    
			</div>
		</div>
	 
	    <div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>收费额_总收费额</label>
	        <div class="col-sm-3">
				<input type="text" class="form-control" id="generalIncome" name="generalIncome" value="${operatingDataVo.generalIncome}" data-rule-required="true" data-rule-num2="true" data-rule-rangelength="[1,10]" />    
	        </div>
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>收费额_其中粤通卡收入</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="ytkIncome" name="ytkIncome" value="${operatingDataVo.ytkIncome}" data-rule-required="true" data-rule-num2="true" data-rule-rangelength="[1,10]" />    
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
	var winName = "${winName}";
	var URLStr = "/datecenter/operatingData/operatingData_";

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