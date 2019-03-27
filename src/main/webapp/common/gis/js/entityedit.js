var mapInfoIfr = parent.mapinfo;
var EntityManager = function(map) {
	this.map = map;
	this.editlayer = null; // 正在编辑的图层
	this.showlayer = null; // 显示图层
	this.ptDrawHandler = null;
	this.areaDrawHandler = null;
	this.modifyControl = null;
	this.selectHover = null;
	this.markerLayer = null; //网格编号图层
	this.init();
};

EntityManager.prototype.init = function() {
	if (this.editlayer == null) {
		this.editlayer = new OMAP.Layer.Vector("drawLayer");
		this.map.addLayer(this.editlayer);
	}

	if (this.showlayer == null) {
		this.showlayer = new OMAP.Layer.Vector("showLayer",{
			style :  {
				fill : true,
				fillColor : 'green',
				fillOpacity : 0.5,
				strokeOpacity : 1,
				stroke : true,
				strokeColor : 'green',
				graphic : false
			}
		});
		this.map.addLayer(this.showlayer);
	}
	this.initDraw();
};

EntityManager.prototype.initDraw = function() {
	if (this.areaDrawHandler == null) {
		this.areaDrawHandler = new OMAP.Control.DrawFeature(
				this.editlayer, OMAP.Handler.Polygon);
		this.map.addControl(this.areaDrawHandler);
	}
	if (this.ptDrawHandler == null) {
		this.ptDrawHandler = new OMAP.Control.DrawFeature(
				this.editlayer, OMAP.Handler.Point);
		this.map.addControl(this.ptDrawHandler);
	}
};

EntityManager.prototype.removeEditLayer = function() {
	if (this.editlayer == null) {
		this.map.removeLayer(this.editlayer);
	}
};

EntityManager.prototype.deactiveDraw = function() {
	if (this.ptDrawHandler) {
		this.ptDrawHandler.deactivate();
	}

	if (this.areaDrawHandler) {
		this.areaDrawHandler.deactivate();
	}
};

EntityManager.prototype.deactiveAll = function() {
	this.deactiveDraw();
	if (this.modifyControl != null && this.modifyControl.active) {
		this.modifyControl.deactivate();
	}
};

EntityManager.prototype.createMarkers = function() {
	if (this.markerLayer == null) {
		this.markerLayer = new OMAP.Layer.Vector("markerLayer");
		this.map.addLayer(this.markerLayer);
		var url = "/map/entity.shtml?act=listJson";
		$.post(url,function(data){
			var listentity=eval(data);
				for(var i=0;i<20;i++){
					var item=listentity[i];
//					 var point = new OpenLayers.Geometry.Point(item[1],item[2]);
//					    feature = new OpenLayers.Feature.Vector(point);
//					       feature.attributes.id= item[0];
//					       feature=[feature];
//					    tower.addFeatures(feature);
//					var item=listentity[i];
//					popomap.createPop(
//							'et_' + item[0],
//							'<div class="ibox"><div _child="body" class="red"><a href="javascript:void(0)"><span _child="title" onclick="">'
//									+ item[0]
//									+ '</span></a></div><div class="ipoint"><img _child="point" src="/api/images/skin5/txtRedAw.gif"/></div></div>',
//									item[1], item[2], 0, 0, 101);
//					popup1=new OMAP.Popup(item[0],
//	                new OMAP.LonLat(item[1],item[2]),
//	                new OMAP.Size(60,20),
//	                '<div class="ibox"><div _child="body" class="red" style="background:blue;"><a href="javascript:void(0)"><span style="color:white;font-weight:bold;"_child="title" onclick="">'
//					+ item[0]
//					+ '</span></a></div><div class="ipoint"><img _child="point" src="/api/images/skin5/txtRedAw.gif"/></div></div>',
//	                false);
//				    popup1.setBackgroundColor("transparent");
//				    map.addPopup(popup1);
				    
//					 var styleOption ={
//								strokeColor: "#000000",
//						        strokeOpacity: 1,
//						        strokeWidth: 2,
//						        fillColor: "#ffffff",
//						        fillOpacity: 1,
//						        graphicName:"star",
//						        pointRadius: 0,
//						        label : "${id}",
//							    fontColor: "#2121ff",
//							    fontSize: "25px",
//							    labelAlign:"lb"
//							}
//				      var mapManager = new MapManager(map,styleOption);
//				      var markerOpt = {id:item[0],title: item[0], // 标注名称
//				    			x: item[1], // 标注x坐标
//				    			y: item[2], // 标注y坐标
//				    			iconUrl: '/gis/images/front/common/pin.png', // 图标资源地址
//				    			width: 20, // 图标宽度
//				    			height: 20 // 图标高度
//				    			}
//				      mapManager.drawText(item[1],item[2],item[0]);
//				      mapManager.drawMarkers2(markerOpt);
					var idstr = item[0]+'';
						popup1=new OMAP.Popup(idstr,
				                new OMAP.LonLat(item[1],item[2]),
				                new OMAP.Size(50,20),
				                '<div class="ibox"><div _child="body" class="red" style="background:blue;"><a href="javascript:void(0)"><span style="color:white;font-weight:bold;"_child="title" onclick="">'
								+ idstr
								+ '</span></a></div><div class="ipoint"><img _child="point" src="/img/txtRedAw.gif"/></div></div>',
				                false);
				     popup1.setBackgroundColor("transparent");
//				     popup1.setBorder("1px solid #d91f12");
				      map.addPopup(popup1);
				}
		});

	}	
};



EntityManager.prototype.showAroundHotspot = function(style) {
	var me = this;
	var extent = this.map.getExtent();
	var extparam = "xmin=" + extent.left + "&ymin=" + extent.bottom + "&xmax="
			+ extent.right + "&ymax=" + extent.top;
	var url = "/front/search/search.shtml?act=rectSearch&" + extparam;
	$.ajax(url).then(function(response) {
		if (me.showlayer == null) {
			return;
		}
		me.showlayer.removeAllFeatures();
		me.showlayer.display(true);
		for ( var i = 0; i < response.length; i++) {
			var r = response[i];
			var pg = OMAP.MapLib.createPolygon(r.xs, r.ys);
			if (pg == null) {
				continue;
			}

			var f = new OMAP.Feature.Vector(pg, {
				name : r.name,
				id : r.id
			}, style);
			me.showlayer.addFeatures([ f ]);
		}
	});
};

EntityManager.prototype.hideAroundHotspot = function() {
	this.showlayer.removeAllFeatures();
};

EntityManager.prototype.activeDrawArea = function() {
	// 将绘制的对象移动到周边图层中
	OMAP.MapLib.moveLayerFeature2Layer(this.editlayer, this.showlayer);
	this.editlayer.removeAllFeatures();
	this.areaDrawHandler.activate();
	var me = this;
	this.areaDrawHandler.events.on({
		"featureadded" : function(eventArgs) {
			var geometry = eventArgs.feature.geometry;
			me.areaDrawHandler.deactivate();
			// 更新mapinfo里面的坐标
			mapInfoIfr.setCoord(geometry);
			me.activeModify();
		}
	});
};

EntityManager.prototype.activeSelectArea = function() {
	var style = OMAP.Util.extend({},
			OMAP.Feature.Vector.style['default']);
	this.selectHover = new OMAP.Control.SelectFeature(this.showlayer,
			{
				multiple : false,
				highlightOnly : false,
				box : false,
				hover : false,
				selectStyle : style,
				onSelect : function(feature) {
					mapInfoIfr.setCoord(feature);
				},
				toggleKey : "ctrlKey", // ctrl key removes from
				// selection
				multipleKey : "shiftKey" // shift key adds to
			});
	this.map.addControl(this.selectHover);
	this.selectHover.activate();
};

EntityManager.prototype.activeModify = function() {
	if (this.modifyControl == null) {
		this.modifyControl = new OMAP.Control.ModifyFeature(
				this.editlayer);
		this.map.addControl(this.modifyControl);

		this.modifyControl.mode = OMAP.Control.ModifyFeature.RESHAPE
				| OMAP.Control.ModifyFeature.DRAG;

		this.editlayer.events.on({
			"afterfeaturemodified" : function(evt) {
				mapInfoIfr.setCoord(evt.feature.geometry);
			}
		});
	}
	if (!this.modifyControl.active) {
		this.modifyControl.activate();
	}
};

EntityManager.prototype.endModify = function() {
	if (this.modifyControl.active) {
		this.modifyControl.deactivate();
	}
	OMAP.MapLib.moveLayerFeature2Layer(this.editlayer, this.showlayer);
	this.editlayer.removeAllFeatures();
};

EntityManager.prototype.editPolygon = function(xss, yss) {
	this.editlayer.removeAllFeatures();
	var polygon = OMAP.MapLib.createPolygon(xss, yss);
	var f = new OMAP.Feature.Vector(polygon);
	this.editlayer.addFeatures([ f ]);
	this.activeModify();
};

EntityManager.prototype.zoomAndCenter = function(x, y, lvl) {
	x = parseFloat(x);
	y = parseFloat(y);
	if (lvl == null) {
		lvl = 2;
	} else {
		lvl = parseInt(lvl);
	}
	var center = new OMAP.LonLat(x, y);
	this.map.setCenter(center, lvl, false, false);
};

EntityManager.prototype.drawMarker = function(x,y) {
	this.showlayer.removeAllFeatures();
	x = parseFloat(x);
	y = parseFloat(y);
	var pt = new OMAP.Geometry.Point(x,y);
	var style = {
			graphic:true,
			graphicWidth:24,
			graphicHeight:33,
			graphicOpacity:1,
			graphicXOffset:-12,
			graphicYOffset:-33,
			graphicTitle:name,
			externalGraphic:'/gis/images/front/common/pin.png'
	};
	var f = new OMAP.Feature.Vector(pt,null,style);
	this.showlayer.addFeatures([f]);
};
