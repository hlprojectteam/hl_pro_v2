/**
 * 后台管理界面地图js
 */

var mode=0;
var actionSet='';
var mapInfoIfr=parent.mapinfo;
var isLoadBusStation=false;
var zoomValue=3;
//以某个形状作为参考点坐标
var snapPointxs=[];
var snapPointys=[];

function $g(id){
	return document.getElementById(id);
}

function calSnapDistance(px,py){
	mapZoom = popomap.getZoom();
	var distance,minDis=1000,pointIndex=0;
	for(var i=0;i<snapPointxs.length;i++){
		var xdiff = parseInt(snapPointxs[i]) - px;
		var ydiff = snapPointys[i] - py;
		distance = Math.pow((xdiff * xdiff + ydiff * ydiff), 0.5)
		if(distance < minDis){
			minDis = distance;
			pointIndex = i;
		}
	}
	if(minDis<mapZoom*15){
		return pointIndex;
	}else{
		return "out of limit"
	}
}

function doAction(action){
	clear();
	popomap.zoomto(2);
	
	switch(action){
		case 'addArea'://新增实体
			mode=101;
			actionSet='addArea';
//			mapInfoIfr.location='/map/entity.shtml?act=edit';
			mapInfoIfr.location='/gis/entityConsole_edit';
			popomap.selectMode(mode);
			popomap.setMapCursor(0);
			break;
		case 'addUnit':
			mode=102;
			actionSet='addUnit';
			mapInfoIfr.location='map/unit.shtml?act=add';
			popomap.selectMode(mode);
			break;
		case 'editUnit':
			mode=102;
			actionSet='editUnit';
			mapInfoIfr.location='map/unit.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'listHouse':
			mode=102;
			actionSet='listHouse';
			mapInfoIfr.location='house.shtml?act=entitylist';
			popomap.selectMode(mode);
			break;
		case 'addPopulation':
			mode=102;
			actionSet='addPopulation';
			mapInfoIfr.location='population.shtml?act=edit';
			//var url='/dataCollection.shtml?act=editDynaTableData&tableId=8a8a895e3fe6796c013fe77b60610010';
			popomap.selectMode(mode);
			break;
		case 'listPopulation':
			mode=102;
			actionSet='listPopulation';
			mapInfoIfr.location='population.shtml?act=poplist';
			popomap.selectMode(mode);
			break;
		case 'addCommuntiyGrid':
			mode=102;
			actionSet='addCommuntiyGrid';
			mapInfoIfr.location='communtiygrid.shtml?act=edit';
			popomap.selectMode(mode);
			break;
		case 'listCommuntiyGrid':
			mode=102;
			actionSet='listCommuntiyGrid';
			mapInfoIfr.location='communtiygrid.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'addHousingEstate':
			mode=102;
			actionSet='addHousingEstate';
			mapInfoIfr.location='housingestate.shtml?act=edit';
			popomap.selectMode(mode);
			break;
		case 'listHousingEstate':
			mode=102;
			actionSet='listHousingEstate';
			mapInfoIfr.location='housingestate.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'addLot':
			mode=102;
			actionSet='addLot';
			mapInfoIfr.location='lot.jspa?_flowId=edit';
			popomap.selectMode(mode);
			break;
		case 'listLot':
			mode=102;
			actionSet='listLot';
			mapInfoIfr.location='lot.jspa?_flowId=list';
			popomap.selectMode(mode);
			break;
		case 'addStation':
			mode=102;
			actionSet='addStation';
			mapInfoIfr.location='traffic/busstation.shtml?act=edit';
			popomap.selectMode(mode);
			//loadStation();
			break;
		case 'editStation':
			mode=102;
			actionSet='editStation';
			mapInfoIfr.location='traffic/busstation.shtml?act=list';
			popomap.selectMode(mode);
			//loadStation();
			break;
		case 'addSubwayStation':
			mode=102;
			actionSet=action;
			mapInfoIfr.location='traffic/subway_station.shtml?act=edit';
			popomap.selectMode(mode);
			//loadStation();
			break;
		case 'editSubwayStation':
			mode=102;
			actionSet=action;
			mapInfoIfr.location='traffic/subway_station.shtml?act=list';
			popomap.selectMode(mode);
			//loadStation();
			break;
		case 'addSubwayLine':
			mode=103;
			actionSet=action;
			mapInfoIfr.location='traffic/subway.shtml?act=edit';
			//loadStation();
			lastStationId=0;
			index=-1;
			popomap.selectMode(mode);
			break;
		case 'editSubwayLine':
			mode=104;
			actionSet=action;
			mapInfoIfr.location='traffic/subway.shtml?act=list';
			//loadStation();
			lastStationId=0;
			index=-1;
			popomap.selectMode(mode);
			break;
		case 'addLine':
			mode=105;
			actionSet='addLine';
			mapInfoIfr.location='map/simple.shtml?act=edit';
			popomap.selectMode(mode);
			break;
		case 'addBusLine':
			mode=103;
			actionSet='addBusLine';
			mapInfoIfr.location='traffic/busline.shtml?act=edit';
			//loadStation();
			lastStationId=0;
			index=-1;
			popomap.selectMode(mode);
			break;
		case 'editLine':
			actionSet='editLine';
			mapInfoIfr.location='map/simple.shtml?act=list';
			break;
		case 'editbusline':
			mode=104;
			actionSet=action;
			mapInfoIfr.location='traffic/busline.shtml?act=list';
			//loadStation();
			lastStationId=0;
			index=-1;
			popomap.selectMode(mode);
			break;
		case 'viewArea':
			mode=108;
			actionSet='viewArea';
			popomap.selectMode(mode);
			break;
		case 'modifyArea':  //修改实体
			mode=109;
			actionSet='modifyArea';
			mapInfoIfr.location='/gis/mapEntity_list';
			popomap.selectMode(mode);
			break;
		case 'viewMap':  //浏览地图
			actionSet='viewMap';
//			mode=0;
//			popomap.selectMode(mode);
//			popomap.removeSign('BusStation');
//			isLoadBusStation=false;
			var url = "/gis/mapEntity_listJson";
			$.post(url,function(data){
				var listentity=eval(data);
					for(var i=0;i<listentity.length;i++){
						var item=listentity[i];
						popomap.createPop(
								'et_' + item[0],
								'<div class="ibox"><div _child="body" class="red"><a href="javascript:void(0)"><span _child="title" onclick="">'
										+ item[0]
										+ '</span></a></div><div class="ipoint"><img _child="point" src="/api/images/skin5/txtRedAw.gif"/></div></div>',
										item[1], item[2], 0, 0, 101);
						//popomap.createPop(item.id,'<div style="color:#FFFFE0;width:auto;height: auto;background-color: rgb(0, 0, 255);">'+item.id+'</div>',item.x,item.y,0,0,100);
					}
			});
			
			//popomap.createPop('11281','<div style="color:red;width:auto;height: auto;background-color: rgb(0, 0, 255);">' + '11281' + '</div>',1085772,1072087,0,0,100);
			
			break;
		case 'showArea': //显示热区
			popomap.drawAllArea();
			popomap.getMap().mapApi.switchHotspotArea(true);
			mode=0;
			popomap.selectMode(mode);
			break;
		case 'addAd':
			mode=102;
			actionSet='addAd';
			mapInfoIfr.location='gugao/mapad.shtml?act=edit';
			popomap.selectMode(mode);
			break;
		case 'editAd':
			mode=102;
			actionSet='editAd';
			mapInfoIfr.location='gugao/mapad.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'approveAd':
			mode=102;
			actionSet='editAd';
			mapInfoIfr.location='gugao/mapad.shtml?act=checked';
			popomap.selectMode(mode);
			break;
		case 'notApproveAd':
			mode=102;
			actionSet='editAd';
			mapInfoIfr.location='gugao/mapad.shtml?act=uncheck';
			popomap.selectMode(mode);
			break;
		case 'adTimeout':
			mode=102;
			actionSet='editAd';
			mapInfoIfr.location='gugao/mapad.shtml?act=timeout';
			popomap.selectMode(mode);
			break;
		case 'mistake':
			actionSet='mistake';
			mapInfoIfr.location='map/mistake/message.shtml?act=list';
			mode=0;
			popomap.selectMode(mode);
			break;
		case 'assistant':
			actionSet='assistant';
			mapInfoIfr.location='map/assistant.shtml?act=list';
			mode=0;
			popomap.selectMode(mode);
			break;
		case 'buildGroup':
			actionSet='buildGroup';
			break;
		case 'addLayerLabel':
			mode=102;
			actionSet='addLayerLabel';
			mapInfoIfr.location='/gis/layerLabel.shtml?act=edit';
			popomap.selectMode(mode);
			break;
		case 'editLayerLabel':
			mode=102;
			actionSet='editLayerLabel';
			mapInfoIfr.location='/gis/layerLabel.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'editPartyStudio':
			mode=102;
			actionSet='editPartyStudio';
			mapInfoIfr.location='map/partystudio.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'editPartyNews':
			mode=102;
			actionSet='editPartyNews';
			mapInfoIfr.location='/news/partyNewsManager.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'editSimple':
			mode=102;
			actionSet='editSimple';
			mapInfoIfr.location='/map/simple.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'addDzzLabel':
			mode=102;
			actionSet='addDzzLabel';
			mapInfoIfr.location='layerLabel.shtml?act=editDzz';
			popomap.selectMode(mode);
			break;
		case 'editDzzLabel':
			mode=102;
			actionSet='editDzzLabel';
			mapInfoIfr.location='layerLabel.shtml?act=listDzz';
			popomap.selectMode(mode);
			break;
		case 'addBulletin':
			mode=102;
			actionSet='addBulletin';
			mapInfoIfr.location='map/bulletin.shtml?act=edit';
			popomap.selectMode(mode);
			break;
		case 'listBulletin':
			mode=102;
			actionSet='listBulletin';
			mapInfoIfr.location='map/bulletin.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'listWindow':
			mode=102;
			actionSet='listWindow';
			mapInfoIfr.location='map/window.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'editWindow':
			mode=102;
			actionSet='editWindow';
			mapInfoIfr.location='map/window.shtml?act=edit';
			popomap.selectMode(mode);
			break;
		case 'addGrid':  //增加网络
			mode=102;
			actionSet='addGrid';
			mapInfoIfr.location='/gis/gridManager_edit';
			//popomap.removeAllPop();
			popomap.selectMode(mode);
			break;
		case 'editGrid': //修改网络
			mode=102;
			actionSet='editGrid';
			mapInfoIfr.location='/map/gridManager.shtml?act=edit';
			//popomap.removeAllPop();
			popomap.selectMode(mode);			
			break;
		case 'listGrid':
			mode=109;
			actionSet='listGrid';
			mapInfoIfr.location='/map/gridManager.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'showGrids':
			mode=0;
			actionSet='showGrids';
			popomap.removeAllPop();
			popomap.selectMode(mode);
			showGridsOnMap();
			break;
		case 'addGridPersons':
			mode=102;
			actionSet='addGridPersons';	
			popomap.removeAllPop();
			popomap.selectMode(mode);
			showGridLinesOnMap();
			mapInfoIfr.location='/map/wanggemanager.shtml?act=edit';
			break;
		case 'listGridPersons':
			mode=102;
			actionSet='listGridPersons';			
			popomap.selectMode(mode);
			mapInfoIfr.location='/map/wanggemanager.shtml?act=list';
			break;
		case 'addFangChan':
			mode=102;
			actionSet='addFangChan';
			mapInfoIfr.location='fangChanManager.shtml?act=editDynaTableData&tableId=8a8a7ea0406ccaed014071b46d870702';
			popomap.removeAllPop();
			popomap.selectMode(mode);
			break;
		case 'listFangChan':
			mode=102;
			actionSet='listGrid';
			mapInfoIfr.location='fangChanManager.shtml?act=listDynaTableData&tableId=8a8a7ea0406ccaed014071b46d870702';
			popomap.selectMode(mode);
			break;
		case 'addDygenPopulation':
			mode=102;
			actionSet='addDygenPopulation';
			mapInfoIfr.location='population.shtml?act=editDynaTableData&tableId=8a8a7ea0406ccaed014071b46d870702';
			popomap.removeAllPop();
			popomap.selectMode(mode);
			break;
		case 'listDygenPopulation':
			mode=102;
			actionSet='listDygenPopulation';
			mapInfoIfr.location='population.shtml?act=dygen_poplist&tableId=8a8a7ea040617e1e01406243b72e0137';
			popomap.selectMode(mode);
			break;
		case 'addBuilding':
			mode=102;
			actionSet='addBuilding';
			mapInfoIfr.location='building.shtml?act=edit';
			popomap.removeAllPop();
			popomap.selectMode(mode);
			break;
		case 'listBuilding':
			mode=102;
			actionSet='listBuilding';
			mapInfoIfr.location='building.shtml?act=list';
			popomap.selectMode(mode);
			break;
		case 'addApartment':
			mode=102;
			actionSet='addApartment';
			mapInfoIfr.location='apartment.shtml?act=edit';
			popomap.removeAllPop();
			popomap.selectMode(mode);
			break;
		case 'listApartment':
			mode=102;
			actionSet='listApartment';
			mapInfoIfr.location='apartment.shtml?act=list';
			popomap.selectMode(mode);
			break;
		default:
			break;
	}
	doSign(action);
}

function setMapZoom(curZoom,nowZoom){
	if(curZoom!=nowZoom){
		popomap.zoomto(nowZoom);
	}
}

function showSnapGridLinesOnMap(uuid,lastGrid){
	if(lastGrid!="none"&&lastGrid!=""){
		 popomap.removePop("pop_"+lastGrid);
		 popomap.destroyG("linemyline_"+lastGrid); 
		}
	setMapZoom(popomap.getZoom(),zoomValue);
	$.ajax({
		type:"post",
		url:"/map/gridManager.shtml?act=gridJson&uuid="+uuid,
		dataType:"JSON",
		success:function(result){
			if(result.success){
				var array = result.data;
				if(array.length>0){
					snapPointxs = array[0].xs.split(",");
					snapPointys = array[0].ys.split(",");
					//myDrawLineOnly(array[0].uuid,array[0].name,array[0].x,array[0].y,array[0].xs,array[0].ys,array[0].strokeWeight,array[0].strokeColor);
				}
			}
		}
	});
}

function showOtherGridLinesOnMap(gridId){
	setMapZoom(popomap.getZoom(),zoomValue);
	$.ajax({
		type:"post",
		url:"/map/gridManager.shtml?act=jsonList",
		dataType:"JSON",
		success:function(result){
			popomap.removePop("gridPersonsPoint_");
			if(result.success){
				var array = result.data;
				if(array.length>0){
					for(var i=0;i<array.length;i++){
						if(gridId != array[i].uuid)
						{
							myDrawLineOnly(array[i].uuid,array[i].name,array[i].x,array[i].y,array[i].xs,array[i].ys,array[i].strokeWeight,array[i].strokeColor);
						}
						
					}
				}
			}
		}
	});
}


function showGridLinesOnMap(){
	setMapZoom(popomap.getZoom(),zoomValue);
	$.ajax({
		type:"post",
		url:"/map/gridManager.shtml?act=jsonList",
		dataType:"JSON",
		success:function(result){
			popomap.removePop("gridPersonsPoint_");
			if(result.success){
				var array = result.data;
				if(array.length>0){
					for(var i=0;i<array.length;i++){
						/*if(i==0)
						{
							popomap.go2xy(array[i].x,array[i].y);
						}*/
						myDrawLineOnly(array[i].uuid,array[i].name,array[i].x,array[i].y,array[i].xs,array[i].ys,array[i].strokeWeight,array[i].strokeColor);
					}
				}
			}
		}
	});
}

function showGridsOnMap(){
	setMapZoom(popomap.getZoom(),zoomValue);
	$.ajax({
		type:"post",
		url:"/map/gridManager.shtml?act=jsonList",
		dataType:"JSON",
		success:function(result){
			popomap.removePop("gridPersonsPoint_");
			if(result.success){
				var array = result.data;
				if(array.length>0){
					for(var i=0;i<array.length;i++){
						//popomap.go2xy(array[i].x,array[i].y);
						myDrawLine(array[i].uuid,array[i].name,array[i].x,array[i].y,array[i].xs,array[i].ys,array[i].strokeWeight,array[i].strokeColor,array[i].fillColor);
						var gridPersonses = array[i].gridPersonses;
						if(gridPersonses.length>0){
							for(var p=0;p<gridPersonses.length;p++){
								if(gridPersonses[p].deleted!=true){
									popomap.removePop("gridPersonsPoint_"+gridPersonses[p].uuid);
									myCreatePop(gridPersonses[p].uuid,gridPersonses[p].name,gridPersonses[p].x,gridPersonses[p].y,"/images/wangge/ico_dt_01_zc.png");
								}
							}
						}
					}
				}
			}
		}
	});
}

function myDrawLineOnly(id,name,x,y,xss,yss,strokeWeight,strokeColor){
	//alert("id="+id+"  name="+name+"   x="+x+"   y="+y+"   xss="+xss+"   yss="+yss+"   strokeWeight="+strokeWeight+"   strokeColor="+strokeColor);
	if(x&&y&&xss&&yss){
		var start_x,start_y;
		if(xss.indexOf(",")!=-1){
			start_x = xss.split(",")[0];
			start_y = yss.split(",")[0];
		}else{
			start_x = xss;
			start_y = yss;
		}
		var o={id:'myline_'+id,xs:xss+","+start_x,ys:yss+","+start_y,strokecolor:strokeColor,strokeweight:strokeWeight,autoZoom:true,endarrow:'classic'}
		// 调用画线功能
		popomap.removePop("pop_"+id);
		popomap.destroyG("linemyline_"+id);
		popomap.drawLine(o);
		popomap.createPop(
				'pop_' + id,
				'<div class="ibox" onmousemove="this.parentNode.style.zIndex=200" onmouseout="this.parentNode.style.zIndex=100"><div _child="body" class="red"><a href="javascript:void(0)"><span _child="title" onclick="">'
						+ name
						+ '</span></a></div><div class="ipoint"><img _child="point" src="/api/images/skin5/txtRedAw.gif"/></div></div>',
				x, y, 0, 0, 101);
		//popomap.go2xy(x,y);
	}
}
/**
 * 画线并填充
 * @param id
 * @param name
 * @param x
 * @param y
 * @param xss
 * @param yss
 * @param strokeWeight
 * @param strokeColor
 * @param fillColor
 */
function myDrawLine(id,name,x,y,xss,yss,strokeWeight,strokeColor,fillColor){
	if(x&&y&&xss&&yss){
		var start_x,start_y;
		if(xss.indexOf(",")!=-1){
			start_x = xss.split(",")[0];
			start_y = yss.split(",")[0];
		}else{
			start_x = xss;
			start_y = yss;
		}
		var o={id:'myline_'+id,xs:xss+","+start_x,ys:yss+","+start_y,autoZoom:true,strokecolor:strokeColor,strokeweight:strokeWeight,fillcolor:fillColor,opacity:0.5,clickFun:showD}
		// 调用画线功能
		popomap.removePop("pop_"+id);
		popomap.destroyG("linemyline_"+id);
		popomap.drawLine(o);
		
		//网格名称弹出框		
		popomap.createPop(
				'pop_' + id,
				'<div class="ibox" onmousemove="this.parentNode.style.zIndex=200" onmouseout="this.parentNode.style.zIndex=100"><div _child="body" class="red"><a href="javascript:void(0)"><span _child="title" onclick="parent.wanggeClick({id:\''
						+ id
						+ '\',url:\'/map/gridManager.shtml?act=view&id='
						+ id
						+ '\',x:'
						+ x 
						+ ',y:'
						+ y
						+'});">'
						+ name
						+ '</span></a></div><div class="ipoint"><img _child="point" src="/api/images/skin5/txtRedAw.gif"/></div></div>',
				x, y, 0, 0, 101);
//		popomap.createPop(
//				'pop_' + id,
//				'<div class="ibox" onmousemove="this.parentNode.style.zIndex=200" onmouseout="this.parentNode.style.zIndex=100"><div _child="body" class="red"><a href="javascript:void(0)"><span _child="title" onclick="parent.personsPointClick({id:\''
//						+ id
//						+ '\',url:\'/gridManager.shtml?act=view&id='
//						+ id
//						+ '\'});">'
//						+ name
//						+ '</span></a></div><div class="ipoint"><img _child="point" src="/api/images/skin5/txtRedAw.gif"/></div></div>',
//				x, y, 0, 0, 101);
	}
}

//网格名称点击事件
function wanggeClick(obj)
{
	var title = '网格详情';
	var url = "/map/gridManager.shtml?act=view&id=" + obj.id;
	var _ws = 345;
	var _hs = 205;
	popomap.removePop("requ_pic_div");
	//omapApi.createIframePop("requ_pic_div", title, url, _ws, _hs, x, y);
	omapApi.map.mapApi.createIframePop("requ_pic_div", title, url, _ws, _hs, obj.x, obj.y);
}

function showD(){}
/**
 * 网格员图标点击后响应事件的方法
 * @param obj
 */
function personsPointClick(obj){
	if(obj.id!=null && obj.id!=""){
		mapInfoIfr.location = obj.url;
	}
}
/**
 * 创建网格员图标
 * @param id
 * @param title
 * @param x
 * @param y
 * @param image
 */
function myCreatePop(id,title,x,y,image){
	var str,personsId;
	if(id.indexOf("gridPersonsPoint_")!=-1){
		personsId = id.replace("gridPersonsPoint_","");
	}else{
		personsId = id;
	}
	if(title!=""){
		str = '<p title="'+title+'" style="cursor:pointer;"><img src="' + image + '" onclick="parent.personsPointClick({id:\''+ id+ '\',url:\'/map/gridPersons.shtml?act=view&id='+ personsId+ '\'});" /></p>';
	}else{
		str = '<img src="' + image + '" onclick="" />';
	}
	popomap.createPop(id, str,x, y, 12, 35, 100);
}

function loadStation(){
	if(!isLoadBusStation){
		popomap.loadSign('BusStation');
		isLoadBusStation=true;
	}
}
var currentSign={};
function doSign(action){
	var signType={
		addStation:'BusStation',editStation:'BusStation',addBusLine:'BusStation',editbusline:'BusStation',
		addSubwayStation:'SubwayStation',editSubwayStation:'SubwayStation',addSubwayLine:'SubwayStation',editSubwayLine:'SubwayStation'
	};
	if(action=='addLayerLabel'||action=='editLayerLabel'){
		document.getElementById('menuDiv').style.display='';
	}
	else{
		document.getElementById('menuDiv').style.display='none';
	}
	if(signType[action]){
		var arr=signType[action].split(',');
		for(var i=0;i<arr.length;i++){
			consoleMenu.setMenuState(arr[i],true);
			popomap.loadSign(arr[i]);
		}
	}

	if(action=='addLayerLabel'||action=='editLayerLabel') return;
	
	var newCurrent={};
	if(currentSign){
		var arr=[];
		if(signType[action])arr=signType[action].split(',');

		for(var i=0;i<arr.length;i++){
			if(currentSign[arr[i]]){
				delete currentSign[arr[i]];
			}
			newCurrent[arr[i]]=true;
		}

		for(var item in currentSign){
			popomap.removeSign(item);
			consoleMenu.setMenuState(item,false);
		}
	}
	currentSign=newCurrent;
}
function loadSign(o){
	var id=o.data.englishName;
	
	if(o.isCheck){
		popomap.loadSign(id);
		currentSign[id]=true;
	}
	else{
		delete currentSign[id];
		popomap.removeSign(id);
	}
}
var consoleMenu={
	signNameIdMap:{},
	setMenuState:function(signName,flag){
		var id=this.signNameIdMap[signName];
		var m=MainMenu.getMenuItemById(id);
		if(m){
			if(flag)m.select();
			else m.unSelect();
		}
	},
	getMain:function(){
		var database2=[];
		//database2.push({id:10001,englishName:'BusStation',name:'公交'});
		//database2.push({id:10002,englishName:'SubwayStation',name:'地铁'});
		var db=database2.concat(database);
		for(var i=0;i<db.length;i++){
			db[i].option={type:'check',clickFun:loadSign};
			this.signNameIdMap[db[i].englishName]=db[i].id;
		}

		var menuData=[];
		menuData.push({id:'sign',name:'图层'});
		menuData[0]['menu']=menuUtil.getMenuDataFromDb(db);

		return menuUtil.getMenu(menuData);
	},
	regSigns:function(){
		if(typeof database=='undefined')return;
		database=support.json.cloneObject(database);
		var dataLoader=popomap.getInnerInstances().dataLoader;
		for(var i=0;i<database.length;i++){
			var sign=database[i];
			var signName=sign.englishName;
			if(sign.ico)sign.ico=domain_config.image+sign.ico;
			if(signName&&!dataLoader.getSignHandler(signName)){
				dataLoader.regSign(signName,null,{viewType:sign.viewType});
				dataLoader.getSignHandler(signName).setSign(signName,'layer',sign.ico,sign.viewType);
			}
		}
	}
};

var lastStationId=0;
var index=-1;
function setBusLine(){
	if(!mapInfoIfr||!mapInfoIfr.insertRow) return;
	var d=popomap.getD(1);
	if(!d||!d.id) return;

	var data=popomap.getD(2);
	data=support.json.cloneObject(data);
	mapInfoIfr.setLineList(data);
	return;
}
function delStation(){
	var d=popomap.getD();
	var busLine=popomap.getValue('busLine');
	if(!d||!d.id)return;
	mapInfoIfr.deleteRowByStationId(d.id);
	window.status='del::'+d.id;

	var data=popomap.getD(2);
	data=support.json.cloneObject(data);
	mapInfoIfr.setLineData(data);
	/*var s=d.s;
	if(s.id){
		if(busLine.ids[s.id]){
			setStationState(s.id,busLine.ids[s.id]);
		}
		else{
			mapInfoIfr.deleteRowByStationId(s.id);
		}
	}*/
}
function editBusLine(data){
	//data=eval(data);
	data=support.json.cloneObject(data);
	var x=data[0].x;
	var y=data[0].y;
	popomap.go2xy(x,y);
	popomap.getMap().mapApi.editBusLine(data);
	/*clear();
	if(!o.x||!o.y) return;
	popomap.go2xy(o.x,o.y);
	if(o.xs)popomap.mdBusLine(o,mdBusLine);
	else{
		mode=103;
		lastStationId=0;
		index=-1;
		popomap.selectMode(mode);
		//actionSet='addBusLine';
	}*/
}
function getBusLine(){
	var data=popomap.getD(2);
	if(!data) return;
	data=support.json.cloneObject(data);
	for(var i=0;i<data.length;i++){
		if(data[i].id) break;
		data.splice(i,1);
		i--;
	}
	for(var i=data.length-1;i>=0;i--){
		if(data[i].id) break;
		data.splice(i,1);
	}
	for(var i=0;i<data.length;i++){
		var d=data[i];
		if(d.id){
			//delete d.name;
			d.type='s';
		}
		else{
			d.id='';
			d.type='n';
		}
	}
	var result=support.json.object2String(data);
	//alert(result);
	return result;
}

function editRoadLine(points){
	mode=105;
	popomap.selectMode(mode);
	actionSet='editLine';
	popomap.go2xy(points[0].x,points[0].y);
	popomap.editPolyline(points);
}
function getLine(){
	var d=popomap.getD(3);
	if(!d)return false;
	if(d.length<2){
		alert('线不能少于两个点');
		return false;
	}
	var arrX=[];
	var arrY=[];
	for(var i=0;i<d.length;i++){
		arrX[i]=d[i].x;
		arrY[i]=d[i].y;
	}
	var xs=arrX.join(',');
	var ys=arrY.join(',');
	return {xs:xs,ys:ys};
}

function setUnit(){
	var d=popomap.getD(1);
	if(!d) return;
	try{
		mapInfoIfr.document.forms[0].x.value=Math.floor(d.x);
		mapInfoIfr.document.forms[0].y.value=Math.floor(d.y);
		if(d.id){
			mapInfoIfr.document.getElementById('entity').value=d.id;
			mapInfoIfr.document.getElementById('entityName').innerHTML=d.name;
		}
		else{return;
			mapInfoIfr.document.getElementById('entity').value=0;
			mapInfoIfr.document.getElementById('entityName').innerHTML='';
		}
	}
	catch(e){
	}
}
function setCoord(){
	var d=popomap.getD(1);
	if(!d) return;
	try{
		mapInfoIfr.mapCallback(d);
	}
	catch(e){
	}
}
function setAreaCoord(){
	var d=getLayerData(); 
	if(!d) return;
	try{
		mapInfoIfr.mapAreaCallback(d);
	}
	catch(e){
	}
}

function setStation(){
	var d=popomap.getD(1);
	if(!d) return;
	try{
		mapInfoIfr.document.forms[0]['x'].value=d.x;
		mapInfoIfr.document.forms[0]['y'].value=d.y;
	}
	catch(e){
	}
}

function setWindow(){
	var d=popomap.getD(1);
	if(!d) return;
	try{
		mapInfoIfr.document.forms[0]['x'].value=d.x;
		mapInfoIfr.document.forms[0]['y'].value=d.y;
	}
	catch(e){
	}
}

function refreshStation(station,oldPoint){
	popomap.clrData(station.x,station.y);
	if(oldPoint) popomap.clrData(oldPoint.x,oldPoint.y);

	var handler=popomap.getSignHandler('busStation');
	if(station.sign=='busStation') {
		handler.removeBusStation(station.id);
	}
	else if(station.sign=='subwayStation'){
		handler.removeSubwayStation(station.id);
	}
	else if(station.sign=='subwayExit'){
		handler.removeSubwayExit(station.id);
	}
	if(station.x&&station.y)popomap.createSignExt(station.sign,station);
}
function refreshSign(o,oldPoint){
	popomap.destroyAllG();
	popomap.clrData(o.x,o.y);
	if(oldPoint) popomap.clrData(oldPoint.x,oldPoint.y);

	var handler=popomap.getSignHandler(o.sign);
	if(!handler){
		alert(o.sign+'类型没有注册');
		return;
	}
	handler.removeSignIco(o);
	if(o.x&&o.y)popomap.createSignExt(o.sign,o);
}
function setAd(){
	var d=popomap.getD(1);
	if(!d) return;
	try{
		mapInfoIfr.document.forms[0].centerX.value=Math.floor(d.x);
		mapInfoIfr.document.forms[0].centerY.value=Math.floor(d.y);
	}
	catch(e){
	}
}
function editAd(id){
	mapInfoIfr.location='gugao/mapad.shtml?act=edit&id='+id;
}
function previewAd(src,w,h,x,y){
	popomap.invokeFun('createAdPreview',src,w,h,x,y);
}
function viewAreaDetails(){
	var d=popomap.getD(1);
	if(!d) return;
	if(d.name&&mapInfoIfr.location.href.indexOf(d.id)==-1){
		mapInfoIfr.location='/map/entity.shtml?act=view&id='+d.id;
	}
}
function modifyAreaDetails(){
	var d=popomap.getD(1);
	if(!d) return;
	if(d.id){
		var url='/map/entity.shtml?act=edit&id='+d.id;
		if(parent.mapinfo.location.href.indexOf(url)==-1){
			mapInfoIfr.location=url;
			return;
		}
	}
}
function doBuildGroup(){
	var data=popomap.getAreaUnitInfo();
	var moved=popomap.getValue('moved');//临时
	if(moved>3) return;
	try{
		if(data){
			mapInfoIfr.selectOne(data.AreaName,data.AreaID);
			//popomap.setValue('AreaUnitInfo',null);
			popomap.getMap().mapApi.AreaUnitInfo=null;
		}
	}
	catch(e){
	}
}
function mymousemove(evt){
	window.status=popomap.getCurX(evt)+':'+popomap.getCurY(evt);
}
function mymouseup(){
	switch(actionSet){
		case 'addArea':
			setAreaCoord();
			break;
		case 'addUnit':
			setCoord();
			break;
		case 'editUnit':
			setCoord();
			break;
		case 'listHouse':
			break;
		case 'addPopulation':
			setCoord();
			break;
		case 'listPopulation':
			setCoord();
			break;
		case 'editPopulation':
			setCoord();
			break;
		case 'addCommuntiyGrid':
			setAreaCoord();
			break;
		case 'listCommuntiyGrid':
			//setCoord();
			break;
		case 'addHousingEstate':
			setAreaCoord();
			break;
		case 'listHousingEstate':
			//setCoord();
			break;
		case 'addStation':
		case 'editStation':
		case 'addSubwayStation':
		case 'editSubwayStation':
			setStation();
			break;
		case 'addAd':
		case 'editAd':
		case 'editPartyStudio':
		case 'editPartyNews':
			setAd();
			break;
		case 'addLine':
		case 'addBusLine':
		case 'editbusline':
		case 'addSubwayLine':
		case 'editSubwayLine':
			setBusLine();
			break;
		case 'viewArea':
			//viewAreaDetails();
			break;
		case 'modifyArea':
			modifyAreaDetails();
			setAreaCoord();
			break;
		case 'buildGroup':
			doBuildGroup();
			break;
		case 'addLot':
		case 'listLot':
			setCoord();
			break;
		case 'editWindow':
			setWindow();
			break;
		case 'addGrid':
		case 'editGrid':
			setAreaCoord();
			break;
		case 'addGridPersons':
			setAreaCoord();
			break;
		case 'addFangChan':
			setAreaCoord();
			break;
		case 'addBuilding':
			setAreaCoord();
			break;
		case 'addApartment':
			setAreaCoord();
			break;
		case 'editSimple':
		case 'addDzzLabel':
		case 'addLayerLabel':
			setAreaCoord();
			break;
		default:
			break;
	}
}
function mykeydown(evt){
	switch(evt.keyCode){
		case 46:
			del();
			break;
		default:
			break;
	}
}
function del(){
	switch(actionSet){
		case 'addBusLine':
		case 'editbusline':
		case 'addSubwayLine':
		case 'editSubwayLine':
			delStation();
			break;
		default:
			break;
	}
}
function drawHsArea(o,x,y){
	popomap.destroyAllG();
	popomap.clrData(x,y);
	popomap.drawArea(o);
}
function clear(){
	popomap.destroyAllG();
	popomap.getMap().mapApi.clearBusLine();
}
function getD(type){
	switch(type){
		case 'area':
			return getAreaData();
			break;
		default:
			break;
	}
	return false;
}
function getAreaData(){	
	var d=popomap.getD(3);
	if(!d) return false;
	if(d.length<4) return false;

	var arrX=[],arrY=[];
	for(var i=0;i<d.length;i++){
		arrX.push(Math.floor(d[i].x));
		arrY.push(Math.floor(d[i].y));
	}
	var xs=arrX.join(',');
	var ys=arrY.join(',');
	return {xs:xs,ys:ys};	
}
function choiceLayerView(value){	
	if(value==2)popomap.selectMode(106,{closed:false});
	else if(value==3)popomap.selectMode(106);
	else popomap.selectMode(102);
}
function getLayerData(){
	var mode=popomap.getMode();
	var d=null;

	if(mode==102){
		d=popomap.getD(1);
		return d;
	}
	else if(mode==106){
		d=popomap.getD(3);
		if(!d) return;

		var x=d[0].x,y=d[0].y;
		var arrX=[],arrY=[];
		for(var i=1;i<d.length;i++){
			arrX.push(Math.floor(d[i].x));
			arrY.push(Math.floor(d[i].y));
		}
		return {x:x,y:y,xs:arrX,ys:arrY};
	}
	else if(mode==109){
		d=popomap.getD(3);
		if(!d) return;

		var x=d[0].x,y=d[0].y;
		var arrX=[],arrY=[];
		for(var i=1;i<d.length;i++){
			arrX.push(Math.floor(d[i].x));
			arrY.push(Math.floor(d[i].y));
		}
		return {x:x,y:y,xs:arrX,ys:arrY};
	}
}
function onmapload(){
	switch(module){
		case 'ad':
			doAction('editAd');
			popomap.setValue('isLoadAd',true);
			break;
		case 'buildGroup':
			doAction('buildGroup');
			popomap.setValue('isShowAUInfo',false);
			break;
		default:
			break;
	}
}
function waitMap(){
	try{
		if(popomap.getMapStatus()){
			//popomap.setEvent('onmousedown',mymousedown);
			popomap.setEvent('onmousemove',mymousemove);
			popomap.setEvent('onmouseup',mymouseup);
			popomap.setEvent('onkeydown',mykeydown);
			popomap.setValue('isLoadAd',false);
			consoleMenu.regSigns();
			consoleMenu.getMain().render(document.getElementById('menuDiv'));
			onmapload();
			//后台管理标识
			popomap.getConfig().isBackground=true;
			//popomap.setEvent('onAreaClick',myAreaClick);
			//popomap.setEvent('onUnitClick',myUnitClick);
			//popomap.setEvent('customFun',myCustomFun);
			//popomap.setMapTip('<img src="images/rightTip.gif">');
			//document.domain='o.cn';
		}
		else{
			setTimeout('waitMap()',500);
		}
	}
	catch(e){
		setTimeout('waitMap()',500);
	}
}
function go2xy(x,y){
	popomap.go2xy(x,y);
}
waitMap();

function showMapPop(url,x,y,name)
{
}