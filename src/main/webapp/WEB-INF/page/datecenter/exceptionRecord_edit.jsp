<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>营运异常记录编辑</title>

	<style>
		.dictValue{
			display: none;
		}
	</style>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${exceptionRecordVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${exceptionRecordVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${exceptionRecordVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${exceptionRecordVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${exceptionRecordVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${exceptionRecordVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-09" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty exceptionRecordVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${exceptionRecordVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty exceptionRecordVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${exceptionRecordVo.dutyDate}' pattern='yyyy年MM月dd日营运异常记录'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${exceptionRecordVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true"  />
				<%--disabled="disabled"
				<input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${exceptionRecordVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
				--%>
	        </div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>异常类型</label>
			<div class="col-sm-3">
				<opt:select dictKey="dc_exceptionType" classStyle="form-control" name="exceptionType" id="exceptionType" value="${exceptionRecordVo.exceptionType}" isDefSelect="false" />
			</div>
        </div>
        
        <div class="form-group  isNotShow">
	  		<label class="col-sm-2 control-label">接报时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="receiptTime" name="receiptTime" value="<fmt:formatDate value='${exceptionRecordVo.receiptTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" />
	        </div>
		</div>
	  	
	  	<div class="form-group  isNotShow">
			<label class="col-sm-2 control-label">报告方式</label>
			<div class="col-sm-3">
				<opt:select dictKey="dc_reportedWay_ER" classStyle="form-control" name="reportedWay" id="reportedWay" value="${exceptionRecordVo.reportedWay}" isDefSelect="false" />
			</div>
			<div class="dictValue"  style="display: none;">
				<label class="col-sm-2 control-label">请输入要添加的报告方式</label>
				<div class="col-sm-3">
					<input type="text" class="form-control" id="dictValue" name="dictValue" value="${rescueWorkVo.dictValue}" data-rule-rangelength="[1,15]" />
				</div>
			</div>
		</div>
	 
	    <div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>报告部门 </label>
			<div class="col-sm-3">
		    	<opt:select dictKey="dc_reportingDepartment" classStyle="form-control required" name="reportedDp" id="reportedDp" value="${exceptionRecordVo.reportedDp}" isDefSelect="false"/>
			</div>
			<div class="dictValue1"  style="display: none;">
			  	<label class="col-sm-2 control-label"><span style="color: red">*</span>请输入报告部门</label>
			    <div class="col-sm-3">
					<input type="text" class="form-control" id="dictValue1" name="dictValue1" value="${exceptionRecordVo.dictValue1}" data-rule-rangelength="[1,20]" />
				</div>
			</div>
		</div>
		
		<div class="form-group">
	        <label class="col-sm-2 control-label"><span style="color: red">*</span>报告人员列表</label>
			<div class="col-sm-8">
				<opt:checkbox dictKey="dc_reportingPerson" onclick="OncheckBox(this)"  id="reportedPerson" name="reportedPerson" value="" />
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>报告人员</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" id="dictValue2" name="dictValue2" placeholder="输入报告人员，多个用“,”隔开" value="${exceptionRecordVo.reportedPerson}" data-rule-rangelength="[1,20]" data-rule-required="true"/>
			</div>
		</div>
		
		
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>通行路段 </label>
		    <div class="col-sm-8">
				<input type="text" class="form-control" id="trafficRoad" name="trafficRoad" value="${exceptionRecordVo.trafficRoad}" data-rule-required="true" data-rule-rangelength="[1,30]" />    
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>通知处理部门 </label>
			<div class="col-sm-3">
		    	<opt:select dictKey="dc_NotificationDepartment" classStyle="form-control required" name="processingDp" id="processingDp" value="${exceptionRecordVo.processingDp}" isDefSelect="false"/>
			</div>
			<div class="dictValue3"  style="display: none;">
			  	<label class="col-sm-2 control-label"><span style="color: red">*</span>请输入通知处理部门</label>
			    <div class="col-sm-3">
					<input type="text" class="form-control" id="dictValue3" name="dictValue3" value="${exceptionRecordVo.dictValue3}" data-rule-rangelength="[1,20]" />
				</div>
			</div>
		</div>
		
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>情况简述</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="8" cols="" id="briefIntroduction" name="briefIntroduction" data-rule-required="true" data-rule-rangelength="[1,800]" >${exceptionRecordVo.briefIntroduction}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>处理结果</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="8" cols="" id="result" name="result" data-rule-required="true" data-rule-rangelength="[1,800]" >${exceptionRecordVo.result}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label">备注</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="remark" name="remark" data-rule-rangelength="[0,500]" >${exceptionRecordVo.remark}</textarea>
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
	var URLStr = "/datecenter/exceptionRecord/exceptionRecord_";


	$(function(){
		changeReportedWay();
		changeExceptionType();
	});


    $("#reportedWay").change(function(){
		changeReportedWay();
    });
	//报告类型切换
    function changeReportedWay(){
		var reportedWay = $("#reportedWay").val();
		if(reportedWay == '其它'){
			$(".dictValue").show();
		}else{
			$(".dictValue").hide();
			$("#dictValue").val(null)
		}
	}
    $("#reportedDp").change(function(){
        var reportedDp = $("#reportedDp").val();
        if(reportedDp =='其它'){
            $(".dictValue1").show();
		}else{
            $(".dictValue1").hide();
            $(".dictValue1").val(null)
		}
	});
	
    function OncheckBox(index) {
		if ($(index).attr("tag") == undefined
				|| $(index).attr("tag") == "unChecked") {
			//alert("选中");
			$(index).attr("tag", "checked");
			var val='';
			var valtemp = $.trim($("#dictValue2").val());
			if(valtemp==''||valtemp==undefined||valtemp==null){
				val=$(index).val();
			}else{
				val=$("#dictValue2").val()+','+$(index).val();
			}
			$("#dictValue2").val(val);
		} else {
			//取消
			//alert("取消");
			$(index).attr("tag", "unChecked");
		}
	}
    
	$("#processingDp").change(function(){
        var processingDp = $("#processingDp").val();
        if(processingDp =='其它'){
            $(".dictValue3").show();
		}else{
            $(".dictValue3").hide();
            $(".dictValue3").val(null)
		}
	});

	//异常类型切换
    $("#exceptionType").change(function(){
        changeExceptionType();
    });
    function changeExceptionType(){
        var exceptionType = $("#exceptionType").val();      //异常类型( 1:营运异常; 2:其他异常)
        if(exceptionType != 1){
			$(".isNotShow").show();		//显示 接报时间、报告方式
			$("#title").val("<fmt:formatDate value='${exceptionRecordVo.dutyDate}' pattern='yyyy年MM月dd日其它异常记录'/>");
			changeReportedWay();
        }else{
            $(".isNotShow").hide();		//隐藏 接报时间、报告方式
			$("#title").val("<fmt:formatDate value='${exceptionRecordVo.dutyDate}' pattern='yyyy年MM月dd日营运异常记录'/>");
			$(".dictValue").hide();
			$("#dictValue").val(null)
        }
    }


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
        if($("#reportedWay").val() == '其它' && ($("#dictValue").val() == null || $("#dictValue").val() == "")){
            autoMsg("请输入报告方式", 5);
        }else if($("#reportedDp").val() == '其它' && ($("#dictValue1").val() == null || $("#dictValue1").val() == "")){
            autoMsg("请输入报告部门", 5);
        }else if($("#processingDp").val() == '其它' && ($("#dictValue3").val() == null || $("#dictValue3").val() == "")){
            autoMsg("请输入通知部门", 5);
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
