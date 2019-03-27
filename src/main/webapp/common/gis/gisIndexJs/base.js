function search(){
	autoMsg("别着急，正在研发中···", 1);
}

/**
 * 地图 - 视频监控
 * Mr.Joker
 * 2016年7月25日 15:45:03
 */
function videoMedia(){
	var x=105594.059;
	var y=393422.977;
	var id=1;
	var title="ABC";
	var html = '<img src="/common/gis/images/front/video.png" style="margin-left:-40px;margin-top:-50px"><div class="ibox"><div _child="body" class="ifont" style="background-color:#fff;"><a href="javascript:parent.showLayerLabel(\''+title+'\',\'' + id + '\',' + x + ',' + y + ');"><span _child=' + title + '>' + title + '</span></a></div><div class="ipoint"><img _child="point" src="/common/gis/images/front/video.png"/></div></div>';
	top.popomap.createPop("marker_" + id ,html,x, y,-15,-86,1200);
	popomap.go2xy(x, y);
	
}

//弹出标注框
function showLayerLabel(title,id,x,y){
	popomap.createIframePop("mypop",title,"/gis/gisInfo_videoMedia",520,450,x,y);
}





/************************************   四、专题数据（thematicData）  开始  *********************************/
/**
 * 显示三大园区导航按钮
 * Mr.Wang
 * 2016年8月3日 16:32:40
 */
function thematicData_showSanDiv(){
	var menuCode = $("#menuCode").val();
	switch (menuCode) {
	case "mapMenu_ThematicData":
		$("#SanDiv").show();
		break;
	default:
		$("#SanDiv").hide();
		break;
	};	
}
function thematicData_show(dataAreacode){
	autoMsg("别着急，正在研发中···", 1);
	
	gridManager = new GridManager(map);
	gridManager.DrawPolygonByValue("1","AB",'105768.1498055','393514.3299248','105642.1270000,105645.2790000,105816.2750000,105914.7750000,105854.8870000,105705.9550000,105642.1270000','393557.725,393465.5290000,393415.8850000,393486.8050000,393581.3650000,393610.5210000,393557.7250000',1,'','',2);
}

/************************************   四、专题数据（thematicData）  结束 *********************************/