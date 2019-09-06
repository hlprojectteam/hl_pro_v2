<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>拯救作业编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${rescueWorkVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${rescueWorkVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${rescueWorkVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${rescueWorkVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${rescueWorkVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${rescueWorkVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-07" />
	    <input type="hidden" id="x" name="x" value="${rescueWorkVo.x}" />
	    <input type="hidden" id="y" name="y" value="${rescueWorkVo.y}" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty rescueWorkVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${rescueWorkVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty rescueWorkVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${rescueWorkVo.dutyDate}' pattern='yyyy年MM月dd日拯救作业'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${rescueWorkVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true"  />
				<%--disabled="disabled"
				<input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${rescueWorkVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
				--%>
	        </div>
        </div>
	  	
	  	<div class="form-group">
	  		<label class="col-sm-2 control-label"><span style="color: red">*</span>接报时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="receiptTime" name="receiptTime" value="<fmt:formatDate value='${rescueWorkVo.receiptTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm',onpicked:figueUsedTime1})" data-rule-required="true"  />
	        </div>
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>到达时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="arrivalTime" name="arrivalTime" value="<fmt:formatDate value='${rescueWorkVo.arrivalTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm',onpicked:figueUsedTime2})" data-rule-required="true"  />
	        </div>
		</div>
	 
	    <div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>到场用时 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="usedTime" name="usedTime" value="${rescueWorkVo.usedTime}" data-rule-required="true" readonly="readonly"  />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>清场时间</label>
		    <div class="col-sm-3">
	            <input type="text" class="form-control" id="evacuationTime" name="evacuationTime" value="<fmt:formatDate value='${rescueWorkVo.evacuationTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" data-rule-required="true"  />
	        </div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>地点 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="site" name="site" value="${rescueWorkVo.site}" data-rule-required="true" data-rule-rangelength="[1,100]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>故障车牌</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="faultPlates" name="faultPlates" value="${rescueWorkVo.faultPlates}" data-rule-required="true" data-rule-rangelength="[1,100]" />    
			</div>
		</div>
		
		<div class="form-group">
	        <label class="col-sm-2 control-label"><span style="color: red">*</span>车辆类型列表</label>
			<div class="col-sm-8">
				<opt:checkbox dictKey="dc_carType" onclick="OncheckBox(this)"  id="carType" name="carType" value="" />
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>车辆类型</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" id="dictValue" name="dictValue" placeholder="输入车辆类型，多个用“,”隔开" value="${rescueWorkVo.carType}" data-rule-rangelength="[1,80]" data-rule-required="true"/>
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>缴费单号 </label>
		    <div class="col-sm-8">
				<input type="text" class="form-control" id="paymentOrder" name="paymentOrder" value="${rescueWorkVo.paymentOrder}" data-rule-required="true" data-rule-rangelength="[1,40]" />    
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>拯救费 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="rescueCharge" name="rescueCharge" value="${rescueWorkVo.rescueCharge}" data-rule-required="true" data-rule-num2="true" data-rule-rangelength="[1,6]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>拖车里程</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="trailerMileage" name="trailerMileage" value="${rescueWorkVo.trailerMileage}" data-rule-required="true" data-rule-num2="true" data-rule-rangelength="[1,6]" />    
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>车辆去向</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_whereabouts" classStyle="form-control required" name="whereabouts" id="whereabouts" value="${rescueWorkVo.whereabouts}" isDefSelect="true" />
			</div>
			<div class="dictValue2"  style="display: none;">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>请输入要添加的车辆去向</label>
			<div class="col-sm-3">
				<input type="text" class="form-control" id="dictValue2" name="dictValue2" value="${rescueWorkVo.dictValue2}" data-rule-rangelength="[1,20]" />
			</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>拯救车牌 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="rescuePlates" name="rescuePlates" value="${rescueWorkVo.rescuePlates}" data-rule-required="true" data-rule-rangelength="[1,60]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>司机电话 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="driverPhone" name="driverPhone" value="${rescueWorkVo.driverPhone}" data-rule-required="true" data-rule-phone="true" data-rule-rangelength="[1,40]"/>    
			</div>
		</div>
		
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label">备注</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="remark" name="remark" data-rule-rangelength="[0,500]" >${rescueWorkVo.remark}</textarea>
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
	var URLStr = "/datecenter/rescueWork/rescueWork_";

	function OncheckBox(index) {
		if ($(index).attr("tag") == undefined
				|| $(index).attr("tag") == "unChecked") {
			//alert("选中");
			$(index).attr("tag", "checked");
			var val='';
			var valtemp = $.trim($("#dictValue").val());
			if(valtemp==''||valtemp==undefined||valtemp==null){
				val=$(index).val();
			}else{
				val=$("#dictValue").val()+','+$(index).val();
			}
			$("#dictValue").val(val);
		} else {
			//取消
			//alert("取消");
			$(index).attr("tag", "unChecked");
		}
	}

    $("#whereabouts").change(function(){
        var whereabouts = $("#whereabouts").val();
        if(whereabouts == '其它'){
            $(".dictValue2").show();
        }else{
            $(".dictValue2").hide();
            $(".dictValue2").val(null)
        }
    });


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
	    if($("#whereabouts").val() == '其它' && ($("#dictValue2").val() == null || $("#dictValue2").val() == "")){
            autoMsg("新添加的方向不能为空", 5);
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
	
	
	//到场用时计算
	function figueUsedTime1(){
		var receiptTime = $dp.cal.newdate.H + ":" + $dp.cal.newdate.m;
		var arrivalTime = $("#arrivalTime").val();
		figueResult(receiptTime,arrivalTime);
	}
	
	function figueUsedTime2(){
		var receiptTime = $("#receiptTime").val();
		var arrivalTime = $dp.cal.newdate.H + ":" + $dp.cal.newdate.m;
		figueResult(receiptTime,arrivalTime);
	}
	
	function figueResult(receiptTime,arrivalTime){
		if(receiptTime != null && receiptTime != "" && arrivalTime != null && arrivalTime != ""){
			var time1 = Date.parse("2019/02/28 "+ receiptTime +":00");
			var time2 = Date.parse("2019/02/28 "+ arrivalTime +":00"); 
			var diffTime = time2 - time1;
			var hour=Math.floor(diffTime/1000/60/60);
			var minute=Math.floor(diffTime/1000/60-hour*60);
			var second=diffTime/1000-hour*60*60-minute*60;
			if(hour<10 && hour>=0){
				hour = "0"+hour;
			}
			if(minute<10){
				minute = "0"+minute;
			}
			$("#usedTime").val(hour+":"+minute);
		}
	}
	
</script>
</html>