<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>信息通传编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${infoThroughVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${infoThroughVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${infoThroughVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${infoThroughVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${infoThroughVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${infoThroughVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-11" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty infoThroughVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${infoThroughVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty infoThroughVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${infoThroughVo.dutyDate}' pattern='yyyy年MM月dd日信息通传'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${infoThroughVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true"  />
				<%--disabled="disabled"
				<input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${infoThroughVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
				--%>
	        </div>
        </div>
	  	
	  	<div class="form-group">
	  		<label class="col-sm-2 control-label"><span style="color: red">*</span>通报时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="throughTime" name="throughTime" value="<fmt:formatDate value='${infoThroughVo.throughTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" data-rule-required="true"  />
	        </div>
	        <label class="col-sm-2 control-label"><span style="color: red">*</span>报告人员 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="reportedPerson" name="reportedPerson" value="${infoThroughVo.reportedPerson}" data-rule-required="true" data-rule-rangelength="[1,20]" />    
			</div>
		</div>
	 
	    <div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>信息类型</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_infoType" classStyle="form-control required" name="infoType" id="infoType" value="${infoThroughVo.infoType}" isDefSelect="true" />
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>值班员 </label>
			<div class="col-sm-3">
				<input type="text" class="form-control" id="watcher" name="watcher" value="${infoThroughVo.watcher}" data-rule-required="true" data-rule-rangelength="[1,20]" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>信息来源</label>
			<div class="col-sm-3">
				<opt:select dictKey="dc_infoSource" classStyle="form-control required" name="infoSource" id="infoSource" value="${infoThroughVo.infoSource}" isDefSelect="true" />
			</div>
			<label class="col-sm-2 control-label dictValue"  style="display: none;"><span style="color: red">*</span>请输入要添加的信息来源</label>
			<div class="col-sm-3 dictValue"  style="display: none;">
				<input type="text" class="form-control" id="dictValue" name="dictValue" value="${infoThroughVo.dictValue}" data-rule-rangelength="[1,15]" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>通传方式</label>
			<div class="col-sm-3">
				<opt:select dictKey="dc_throughWay" classStyle="form-control required" name="throughWay" id="throughWay" value="${infoThroughVo.throughWay}" isDefSelect="true" />
			</div>
			<label class="col-sm-2 control-label dictValue2"  style="display: none;"><span style="color: red">*</span>请输入要添加的通传方式</label>
			<div class="col-sm-3 dictValue2"  style="display: none;">
				<input type="text" class="form-control" id="dictValue2" name="dictValue2" value="${infoThroughVo.dictValue2}" data-rule-rangelength="[1,15]" />
			</div>
		</div>
		
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>信息内容</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="10" cols="" id="infoContent" name="infoContent" data-rule-required="true" data-rule-rangelength="[1,800]" >${infoThroughVo.infoContent}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>通传情况</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="8" cols="" id="throughSituation" name="throughSituation" data-rule-required="true" data-rule-rangelength="[1,300]" >${infoThroughVo.throughSituation}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label">备注</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="remark" name="remark" data-rule-rangelength="[0,250]" >${infoThroughVo.remark}</textarea>
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
	var URLStr = "/datecenter/infoThrough/infoThrough_";

	//新增或编辑
	function on_save(){
		if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
	        on_submit();//提交表单.  
	    }else{
            autoMsg("信息提交不正确，请检查！", 5);
        }
	}


    $("#infoSource").change(function(){
        var infoSource = $("#infoSource").val();
        if(infoSource == 2){
            $(".dictValue").show();
        }else{
            $(".dictValue").hide();
            $(".dictValue").val(null)
        }
    });

    $("#throughWay").change(function(){
        var throughWay = $("#throughWay").val();
        if(throughWay == 4){
            $(".dictValue2").show();
        }else{
            $(".dictValue2").hide();
            $(".dictValue2").val(null)
        }
    });

	//提交表单
	function on_submit(){
        if($("#infoSource").val() == 2 && ($("#dictValue").val() == null || $("#dictValue").val() == "")){
            autoMsg("新添加的字典类型不能为空", 5);
        }else if($("#throughWay").val() == 4 && ($("#dictValue2").val() == null || $("#dictValue2").val() == "")){
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