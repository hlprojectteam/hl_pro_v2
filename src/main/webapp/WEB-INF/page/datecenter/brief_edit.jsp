<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>工作简报编辑</title>
</head>
<body>
<div class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${briefVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${briefVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${briefVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${briefVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${briefVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${briefVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-01" />
		<input type="hidden" id="status" name="status" value="${briefVo.status}" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty briefVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${briefVo.title}" data-rule-="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty briefVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${briefVo.dutyDate}' pattern='yyyy年MM月dd日环龙运营控制指挥中心工作简报'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="" name="" value="<fmt:formatDate value='${briefVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true" disabled="disabled"  />
	            <input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${briefVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
	        </div>
        </div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>常务副总经理</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="cwfzjl" name="cwfzjl" value='${(empty briefVo.cwfzjl) ? "刘勇" : briefVo.cwfzjl}' data-rule-required="true" data-rule-rangelength="[1,10]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>分管领导</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="zgfzjl" name="zgfzjl" value='${(empty briefVo.zgfzjl) ? "李锦豪" : briefVo.zgfzjl}' data-rule-required="true" data-rule-rangelength="[1,10]" />    
			</div>
		</div>
	 
	    <div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>中心主任</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="zxfzr" name="zxfzr" value='${(empty briefVo.zxfzr) ? "黄佑平" : briefVo.zxfzr}' data-rule-required="true" data-rule-rangelength="[1,10]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>复核人员</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="fhry" name="fhry" value='${briefVo.fhry}' data-rule-required="true" data-rule-rangelength="[1,10]" />    
			</div>
		</div>
		
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>营运数据</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="4" cols="" id="operatingData" name="operatingData" data-rule-required="true" data-rule-rangelength="[1,500]" >${briefVo.operatingData}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>交通运行情况</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="12" cols="" id="trafficOperation" name="trafficOperation" data-rule-required="true" data-rule-rangelength="[1,800]" >${briefVo.trafficOperation}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>设备运行情况</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="15" cols="" id="equipmentOperation" name="equipmentOperation" data-rule-required="true" data-rule-rangelength="[1,1000]" >${briefVo.equipmentOperation}</textarea>
		    </div>
	  	</div>
		<br><br><br>
	</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
	<div class="pull-right">
	<button class="btn btn-primary " type="button" onclick="on_reload()"><i class="fa fa-refresh"></i>&nbsp;重新加载数据</button>
	<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
	<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
	</div>
</div>
</body>
<script type="text/javascript">
	var winName = "${winName}";
	var URLStr = "/datecenter/brief/brief_";

    var sign = "${sign}";

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

        let url = "";
	    if(sign == "brief"){
            url = URLStr;
		}else{
            url = "/datecenter/totalTable/totalTable_";
		}


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
                        url : url + "load"
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


    //重新加载数据
    function on_reload(){
        parent.layer.confirm("重新加载数据会将原有数据清除，确定重新加载吗？", {
            btn: ["确定","取消"] //按钮
        }, function(index, layero){
            $.ajax({
                type:"post",
                async:false,
                dataType : "json",
                url: URLStr + "reload?ttId="+ $("#ttId").val(),
                success : function(data){
                    if(data.result){
                        $("#trafficOperation").val(data.trafficOperation);
                        $("#equipmentOperation").val(data.equipmentOperation);
                    }else{
                        autoMsg("数据重新加载失败！",5);
                    }
                },
                error : function(result){
                    autoAlert("系统出错",5);
                }
            });
            parent.layer.close(index);
        });
    }

</script>
</html>