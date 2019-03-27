<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>值班汇总表编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${totalTableVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${totalTableVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${totalTableVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${totalTableVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${totalTableVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <br><br>
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-5">
		      <input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value='${(empty totalTableVo.title) ? "运营控制指挥中心值班报表" : totalTableVo.title}' data-rule-required="true" data-rule-rangelength="[1,50]" />
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-5">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${totalTableVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',autoShowQS:true,onpicked:titleChange})" required="required" />
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
	var URLStr = "/datecenter/totalTable/totalTable_";

	//新增或编辑
	function on_save(){
		if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
			isExist();//先判断日期是否已存在
	    }else{
            autoAlert("信息提交不正确，请检查！", 5);
        }
	}
	
	//判断日期是否已存在(判断在数据库中是否已有该天的记录)
	function isExist(){
		var dutyDateStr = $("#dutyDate").val();
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: URLStr + 'isExist',
			data: {dutyDateStr:dutyDateStr},
			success : function(data) {
                if (data.result) {
                	autoAlert("所选日期已存在，请重新选择日期！", 5);
                } else {
                	on_submit();//提交表单.  
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                autoAlert("系统出错，请检查！", 5);
            }
		});
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
                    iframeIndex.$("#grid").bootstrapTable("refresh", {
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
	
	//日期改变时触发的事件
	/* $("#dutyDate").change(function(){
		alert("hehe");
		var dutyDateStr = $("#dutyDate").val();
		if(dutyDateStr != null && dutyDate != ""){
			var oldTitle = $("#title").val();
			var dutyDateArr = dutyDateStr.split("-");
			var newTitle = dutyDateArr[0] + "年" + dutyDateArr[1] + "月" + dutyDateArr[2] + "日" + oldTitle;
			$("#title").val(newTitle);
		}
	}); */
	function titleChange(){
		var newTitle = $dp.cal.newdate.y + "年" + $dp.cal.newdate.M + "月" + $dp.cal.newdate.d + "日" + "运营控制指挥中心值班报表";
		$("#title").val(newTitle);
	}

</script>
</html>