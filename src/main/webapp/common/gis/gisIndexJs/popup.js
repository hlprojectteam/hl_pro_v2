/**
 * 根据具体内容，弹出窗体 
 * Mr.Joker 
 * 2016年7月7日 15:20:38 
 * type : 弹窗类型 
 * id: entityId 实体ID, 也可以是buildingId  ，houseId 等情况 
 * name: 弹窗的名字
 */
function popupViewWin(type, id, name) {
	$("#popupViewWin").hide();
	var url, title;
	// 获得上一个弹出窗口的位置
	var top = $("#popup_viewBuildingInfo_ifr").offset().top;
	var left = $("#popup_viewBuildingInfo_ifr").offset().left;
	$("#popupViewWin").css("top", top);
	$("#popupViewWin").css("left", left + 450); // 在上一个窗口的左边 450个像素位置
	if (type == 'viewEntityPopulationUnit') { // 单元人口信息
		title = name+ "室";
		url = "/gis/entityManagement_viewEntityPopulationUnit?id=" + id;
	} else if (type == 'viewEntityEnterpriseUnit') {// 单元企业信息
		title = name+ "室";
		url = "/gis/entityManagement_viewEntityEnterpriseUnit?id=" + id;
	} else if (type == 'viewEntityPopulationList') {// 实体人口列表
		title = name;
		url = "/gis/entityManagement_toViewEntityPopulationList?id=" + id;
	}
	$("#popupViewWinTitle").html(title);
	$("#popupIframe").attr("src", url);
	$("#popupViewWin").show();
}
/**
 * 关闭二级弹出的窗体
 * 
 */
function closeDiv() {
	$("#popupViewWin").hide();
}
/**
 * 根据具体内容，弹出窗体 Mr.Joker 2016年7月7日 15:20:38 type : 弹窗类型 id: populationId 实体ID name: 弹窗的名字
 */
function popupViewPopulationWin(type, populationId) {
	$("#popupViewPopulationWin").hide();
	var url, title;
	// 获得上一个弹出窗口的位置
	var top = $("#popupViewWin").offset().top;
	var left = $("#popupViewWin").offset().left;
	$("#popupViewPopulationWin").css("top", top);
	$("#popupViewPopulationWin").css("left", left + 50); // 在上一个窗口的左边 450个像素位置
	if (type == 'viewEntityPopulationDetail') {
		title = "人口详情";
		url = "/gis/entityManagement_viewEntityPopulationDetail?id=" + populationId;
	}
	$("#popupViewPopulationWinTitle").html(title);
	$("#popupPopulationIframe").attr("src", url);
	$("#popupViewPopulationWin").show();
}
/**
 * 关闭二级弹出的窗体
 * 
 */
function closePopulationDiv() {
	//关闭人口的弹窗
	$("#popupViewPopulationWin").hide();
}
// 打开产品VR
function popupVRWin(type, id, name) {
	$("#popupViewProductDetailVR").hide();
	if(type=='viewEntityEnterpriseProductDetailVR'){
		title = "《"+name+"》的详情介绍";
		url = "/baseLib/productDisplay_view?id="+id+"&type=2";
	}
	$("#popupViewProductDetailWinTitle").html(title);
	$("#popupProductDetailIframe").attr("src", url);
	$("#popupViewProductDetailVR").show();
}	

// 打开标注 -视频监控
function popupVideoMedia(type, id, name){
	$("#popupViewProductDetailVR").hide();
	if(type=='viewVideoMedia'){
		title = "《"+name+"》的详情介绍";
		url = "/baseLib/productDisplay_view?id="+id+"&type=2";
	}
	$("#popupViewProductDetailWinTitle").html(title);
	$("#popupProductDetailIframe").attr("src", url);
	$("#popupViewProductDetailVR").show();
	
}
/**
 * 关闭二级弹出的窗体
 * 
 */
function closeVRDiv() {
	//关闭人口的弹窗
	$("#popupViewProductDetailVR").hide();
}
/*
 * 判断页面是否含有下面两个ID中的某一个，如果有则改变它的样式 Mr.Joker 2016年7月13日 16:28:28
 */
function exist() {
	var oneId = "OMAP_Control_PanZoomBar_67";
	var twoId = "OMAP_Control_PanZoomBar_84";
	// 说明：这个地方有点问题，就是API会根据不同系统，不同浏览器生成出来的ID编码不相同，所以这里判断一下.
	if ($("#" + oneId).length > 0) {
		$("#" + oneId).hide();
		$("#" + oneId).removeAttr("style");
		$("#" + oneId).addClass("newClass");
		$("#" + oneId).show();
	} else if ($("#" + twoId).length > 0) {
		$("#" + towId).hide();
		$("#" + towId).removeAttr("style");
		$("#" + towId).addClass("newClass");
		$("#" + towId).show();
	}
}
