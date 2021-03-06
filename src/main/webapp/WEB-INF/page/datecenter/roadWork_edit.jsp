<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>涉路施工编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${roadWorkVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${roadWorkVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${roadWorkVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${roadWorkVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${roadWorkVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${roadWorkVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-04" />
	    <input type="hidden" id="x" name="x" value="${roadWorkVo.x}" />
	    <input type="hidden" id="y" name="y" value="${roadWorkVo.y}" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty roadWorkVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${roadWorkVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty roadWorkVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${roadWorkVo.dutyDate}' pattern='yyyy年MM月dd日涉路施工'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${roadWorkVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true"  />
	        </div>
        </div>
        
        <div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>施工单位名称</label>
			<div class="col-sm-3">
		    	<opt:select dictKey="dc_ConstructionUnitName" classStyle="form-control required" name="unitName" id="unitName" value="${roadWorkVo.unitName}" isDefSelect="false" />
			</div>
			<div class="dictValue"  style="display: none;">
			  	<label class="col-sm-2 control-label"><span style="color: red">*</span>请输入施工单位名称</label>
			    <div class="col-sm-3">
					<input type="text" class="form-control" id="dictValue" name="dictValue" value="${roadWorkVo.dictValue}" data-rule-rangelength="[1,20]" />
				</div>
			</div>
        </div>
	  	
	  	<div class="form-group">
	  		<label class="col-sm-2 control-label"><span style="color: red">*</span>进场时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="approachTime" name="approachTime" value="<fmt:formatDate value='${roadWorkVo.approachTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" data-rule-required="true"  />
	        </div>
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>撤场时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="departureTime" name="departureTime" value="<fmt:formatDate value='${roadWorkVo.departureTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" data-rule-required="true"  />
	        </div>
		</div>
	 
	    <div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>现场负责人</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="relationPerson" name="relationPerson" value="${roadWorkVo.relationPerson}" data-rule-required="true" data-rule-rangelength="[1,50]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>负责人电话</label>
			<div class="col-sm-3">
				<input type="text" class="form-control" id="relationPhone" name="relationPhone" value="${roadWorkVo.relationPhone}" data-rule-required="true" data-rule-phone="true" />
			</div>

		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>位置属性</label>
			<div class="col-sm-8">
				<opt:checkbox dictKey="dc_positionAttributes"  id="positionAttributes" name="positionAttributes" value="${roadWorkVo.positionAttributes}" />
			</div>
		</div>
		
		<div class="form-group">
			
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>具体位置 </label>
		    <div class="col-sm-8">
				<input type="text" class="form-control" id="specificLocation" name="specificLocation" value="${roadWorkVo.specificLocation}" data-rule-required="true" data-rule-rangelength="[1,2000]" />    
			</div>
		</div>
		
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>施工内容</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="3" cols="" id="constructionContent" name="constructionContent" data-rule-required="true" data-rule-rangelength="[1,2000]" >${roadWorkVo.constructionContent}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>占道情况</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="3" cols="" id="jeevesSituation" name="jeevesSituation" data-rule-required="true" data-rule-rangelength="[1,2000]" >${roadWorkVo.jeevesSituation}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
	  		<label class="col-sm-2 control-label">检查时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="checkTime" name="checkTime" value="<fmt:formatDate value='${roadWorkVo.checkTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" />
	        </div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label">检查人员列表</label>
			<div class="col-sm-8">
				<opt:checkbox dictKey="dc_Inspectors" onclick="OncheckBox(this)"  id="checker" name="checker" value="" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>检查人员</label>
			<div class="col-sm-8">
				<input type="text" class="form-control" id="dictValue2" name="dictValue2" placeholder="输入检查人员，多个用“,”隔开" value="${roadWorkVo.checker}" data-rule-rangelength="[1,20]" data-rule-required="true"/>
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label">施工现场情况简要描述</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="5" cols="" id="description" name="description"  data-rule-rangelength="[1,2000]" >${roadWorkVo.description}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label">整改措施</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="4" cols="" id="rectificationMeasures" name="rectificationMeasures" data-rule-rangelength="[0,2000]" >${roadWorkVo.rectificationMeasures}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>施工报备情况</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="3" cols="" id="reportedSituation" name="reportedSituation" data-rule-required="true" data-rule-rangelength="[1,2000]" >${roadWorkVo.reportedSituation}</textarea>
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
	var URLStr = "/datecenter/roadWork/roadWork_";
	
	$("#unitName").change(function(){
        var unitName = $("#unitName").val();
        if(unitName =='其它'){
            $(".dictValue").show();
		}else{
            $(".dictValue").hide();
            $(".dictValue").val(null)
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
		if($("#unitName").val() == '其它' && ($("#dictValue").val() == null || $("#dictValue").val() == "")){
            autoMsg("请输入施工单位名称", 5);
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