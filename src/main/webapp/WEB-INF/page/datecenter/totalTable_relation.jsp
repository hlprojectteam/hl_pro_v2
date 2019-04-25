<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>子表数据</title>
	<style type="text/css">
			.left{
				width: 20%;
				display: inline-block;
				border: none;
			}
			.right{
				width: 79.5%;
				height: 95vh;
				display: inline-block;
				border: none;
				float: right;
			}
			ul{
				padding: 0;
				margin: 0;
				text-align: center;
			}
			li{
				list-style: none;
				border: #eee 1px solid;
				border-bottom: none;
				padding: 2.5%;
			}
			iframe{
				width:100%;
				height:100%;
			}
		</style>
</head>
<body style="overflow-y: hidden">	<!--表示去掉竖直滚动条。 -->
		<div class="left">
			<ul>
				<%--<li class="brief" onclick="pageSwitch('brief')"  style="border-top: none; background-color: lightgray;">工作简报</li>--%>
				<li class="transferRegistration" onclick="pageSwitch('transferRegistration')">交接登记表</li>
				<li class="surveillanceInspection" onclick="pageSwitch('surveillanceInspection')">监控巡检</li>
				<li class="roadWork" onclick="pageSwitch('roadWork')">涉路施工</li>
				<li class="equipmentOperation" onclick="pageSwitch('equipmentOperation')">设备运行情况统计表</li>
				<li class="equipmentStatus" onclick="pageSwitch('equipmentStatus')">联网设备日常检查表</li>
				<li class="operatingData" onclick="pageSwitch('operatingData')">营运数据</li>
				<li class="rescueWork" onclick="pageSwitch('rescueWork')">拯救作业</li>
				<li class="clearing" onclick="pageSwitch('clearing')">清障保洁</li>
				<li class="exceptionRecord" onclick="pageSwitch('exceptionRecord')">营运异常记录</li>
				<li class="trafficAccident" onclick="pageSwitch('trafficAccident')">交通事故</li>
				<li class="infoThrough" onclick="pageSwitch('infoThrough')">信息通传</li>
				<li class="feedBack" onclick="pageSwitch('feedBack')">顾客意见反馈</li>
				<li class="trafficJam" onclick="pageSwitch('trafficJam')">交通阻塞</li>
				<li class="fieldOperations" onclick="pageSwitch('fieldOperations')"  style="border-bottom: #eee 1px solid;">外勤作业</li>
			</ul>
		</div>
		<div class="right">
			<iframe name="iframe_WinName" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" src="/datecenter/transferRegistration/transferRegistration_list?ttId=${ttId}&dutyDateStr=${dutyDateStr}"></iframe>
		</div>
		<!-- frameborder：是否显示边框（0无边框 1有边框）；	scrolling：是否有滚动条（yes有滚动条 no无滚动条）； allowtransparency:背景是否透明（yes透明 no不透明）-->

<script type="text/javascript">
	var ttId = "${ttId}";
	var dutyDateStr = "${dutyDateStr}";

	//切换页面
	function pageSwitch(sign){
		changeliStyle(sign);		//动态修改左侧ul列表的样式

		switch (sign){
			case "brief":
				changeIframeSrc("/datecenter/brief/brief_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "clearing":
				changeIframeSrc("/datecenter/clearing/clearing_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "equipmentOperation":
				changeIframeSrc("/datecenter/equipmentOperation/equipmentOperation_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
            case "equipmentStatus":
                changeIframeSrc("/datecenter/equipmentStatus/equipmentStatus_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
                break;
			case "exceptionRecord":
				changeIframeSrc("/datecenter/exceptionRecord/exceptionRecord_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "feedBack":
				changeIframeSrc("/datecenter/feedBack/feedBack_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "fieldOperations":
				changeIframeSrc("/datecenter/fieldOperations/fieldOperations_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "operatingData":
				changeIframeSrc("/datecenter/operatingData/operatingData_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "infoThrough":
				changeIframeSrc("/datecenter/infoThrough/infoThrough_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "rescueWork":
				changeIframeSrc("/datecenter/rescueWork/rescueWork_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "roadWork":
				changeIframeSrc("/datecenter/roadWork/roadWork_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "surveillanceInspection":
				changeIframeSrc("/datecenter/surveillanceInspection/surveillanceInspection_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "trafficAccident":
				changeIframeSrc("/datecenter/trafficAccident/trafficAccident_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "trafficJam":
				changeIframeSrc("/datecenter/trafficJam/trafficJam_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
			case "transferRegistration":
				changeIframeSrc("/datecenter/transferRegistration/transferRegistration_list?ttId="+ttId+"&dutyDateStr="+dutyDateStr);
				break;
		}
	}

	//动态修改左侧ul列表的样式
	function changeliStyle(sign){
		$(".left ul li").css("background-color","white");
		$("."+sign).css("background-color","lightgray");
	}

	//动态修改iframe的src属性值
	function changeIframeSrc(iframeSrc){
		$("iframe").attr({ src: iframeSrc});
	}
</script>
</body>
</html>