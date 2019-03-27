var mapInfoIfr = parent.mapinfo;
var GridManager = function(map) {
	this.map = map;
	this.editlayer = null; // 正在编辑的图层
	this.showlayer = null; // 显示图层
	this.snaplayer = null; //捕捉图层
	this.ptDrawHandler = null;
	this.plDrawHandler = null;
	this.areaDrawHandler = null;
	this.modifyControl = null;
	this.snapControl = null;
	this.selectHover = null;
	this.markerLayer = null; //网格编号图层
	this.geometry = null;//绘制要素
	this.drawControls = null;
	this.init();
};

GridManager.prototype.init = function() {
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
	if(this.snaplayer == null){
		var context = {  
                getFillColor: function (feature) {  
                    return feature.attributes["fillcolor"];  
                },  
                getStrokeWidth: function (feature) {  
                    return feature.attributes["strokeweight"];  
                },
                getStrokeColor: function (feature) {  
                    return feature.attributes["strokecolor"];  
                }
            }; 
        var template = {  
                strokeWidth: "${getStrokeWidth}",  
                fillColor: "${getFillColor}",
                strokeColor: "${getStrokeColor}",
                fillOpacity : 0.3,
                strokeOpacity : 1,
                label : "${pname}",
                fontColor: "#000000"
            }; 
        var style = new OMAP.Style(template, { context: context });  
         this.snaplayer = new OMAP.Layer.Vector("snapLayer", {  
             styleMap: new OMAP.StyleMap(style)
         });
        map.addLayer(this.snaplayer);  
	}
	this.initDraw();
};

GridManager.prototype.showGridsOnMap = function() {
	var me = this;
	$.ajax({
		type:"post",
		url:"gridLabels_jsonList.html",
		dataType:"JSON",
		success:function(result){
			if(result.success){
				var array = result.data;
				var features = new Array(array.length);
	            for (var i = 0; i < features.length; i++) {  
	            	var polygon = OMAP.MapLib.createPolygon(array[i].xs, array[i].ys);

	                	features[i] = new OMAP.Feature.Vector(  
	    	                	polygon  
	    	                    , {  
	    	                        strokeweight: array[i].strokeWeight,
	    	                        fillcolor: array[i].fillColor,
	    	                        strokecolor: array[i].strokeColor,
	    	                        pname: array[i].name,
	    	                        pid: array[i].uuid
	    	                    }  
	    	                );
						var gridPersonses = array[i].gridPersonses;	
						if(gridPersonses.length>0){
							for(var p=0;p<gridPersonses.length;p++){
								if(gridPersonses[p].deleted!=true){
									me.drawMarker(gridPersonses[p].x,gridPersonses[p].y,"/gis/images/ico_dt_01_dc.png");
//									myCreatePop(gridPersonses[p].uuid,gridPersonses[p].name,gridPersonses[p].x,gridPersonses[p].y,"/images/wangge/ico_dt_01_zc.png");
								}
							}
						}
	                
	            }
	            me.snaplayer.addFeatures(features);

			}
		}
	});
}

GridManager.prototype.showGridLinesOnMap = function() {
	var me = this;
	$.ajax({
		type:"post",
		url:"gridLabels_jsonList.html",
		dataType:"JSON",
		success:function(result){
			if(result.success){
				var array = result.data;
				var features = new Array(array.length);
	            for (var i = 0; i < features.length; i++) {  
	            	var polygon = OMAP.MapLib.createPolygon(array[i].xs, array[i].ys);

	                	features[i] = new OMAP.Feature.Vector(  
	    	                	polygon  
	    	                    , {  
	    	                        strokeweight: array[i].strokeWeight,
	    	                        fillcolor: array[i].fillColor,
	    	                        strokecolor: array[i].strokeColor,
	    	                        pname: array[i].name,
	    	                        pid: array[i].uuid
	    	                    }  
	    	                );	                
	            }
	            me.snaplayer.addFeatures(features);
			}
		}
	});
}

GridManager.prototype.DrawSnapLayer = function() {
	$.ajax({
		type:"post",
		url:"gridLabels_jsonList.html",
		dataType:"JSON",
		success:function(result){
			if(result.success){
				if (this.snaplayer == null) {
					this.snaplayer = new OMAP.Layer.Vector("snapLayer",{
						style :  {
							fill : true,
							fillColor : 'white',
							fillOpacity : 0.1,
							strokeOpacity : 1,
							stroke : true,
							strokeColor : '#3333aa',
							graphic : false
						}
					});
					map.addLayer(this.snaplayer);
				}
				var array = result.data;
				if(array.length>0){
					for(var i=0;i<array.length;i++){
//							alert(array[i].xs);
							var polygon = OMAP.MapLib.createPolygon(array[i].xs, array[i].ys);
							var f = new OMAP.Feature.Vector(polygon);
							this.snaplayer.addFeatures([ f ]);
//							myDrawLineOnly(array[i].uuid,array[i].name,array[i].x,array[i].y,array[i].xs,array[i].ys,array[i].strokeWeight,array[i].strokeColor);
					}
					if(array[0].x != '' && array[0].x != 0 && array[0].x != null){
						var center = new OMAP.LonLat(array[0].x, array[0].y);
						map.setCenter(center, 5, false, false);
					}
				}
			}
		}
	});
};

GridManager.prototype.randomColor = function() {
    var rand = Math.floor(Math.random( ) * 0xFFFFFF).toString(16);
    if(rand.length == 6){
        return rand;
    }else{
        return randomColor();
    }
};

GridManager.prototype.DrawPolygonByValue = function(id,name,x,y,xs,ys,sweight,scolor,fcolor,gp) {
	var polygon = OMAP.MapLib.createPolygon(xs, ys);

	if(fcolor==null||fcolor==''){
		fcolor='#'+this.randomColor();
	}
	if(scolor==null||scolor==''){
		scolor='#'+this.randomColor();
	}
	if(sweight==null||sweight==''){
		sweight=2;
	}
	
	var features= new OMAP.Feature.Vector(  
        	polygon  
            , {  
                strokeweight: sweight,
                fillcolor: fcolor,
                strokecolor: scolor,
                pname: name,
                pid: id
            }
        );
	var center = new OMAP.LonLat(x,y);
	map.setCenter(center, 2, false, false);  // 里面的数字是，设置地图加载的级别   Mr.Wang  
	var gridPersonses = gp;	
	if(gridPersonses.length>0){
		for(var p=0;p<gridPersonses.length;p++){
			if(gridPersonses[p].deleted!=true){
				this.drawMarker(gridPersonses[0].x, gridPersonses[0].y,"/gis/iamges/ico_dt_01_dc.png");
			}
		}
	}
	 this.snaplayer.addFeatures(features);
}



GridManager.prototype.initDraw = function() {
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
	if (this.plDrawHandler == null) {
		this.plDrawHandler = new OMAP.Control.DrawFeature(
				this.editlayer, OMAP.Handler.Path);
		this.map.addControl(this.plDrawHandler);
	}
};

GridManager.prototype.removeEditLayer = function() {
	if (this.editlayer == null) {
		this.map.removeLayer(this.editlayer);
	}
};

GridManager.prototype.deactiveDraw = function() {
	if (this.ptDrawHandler) {
		this.ptDrawHandler.deactivate();
	}
	if (this.plDrawHandler) {
		this.plDrawHandler.deactivate();
	}

	if (this.areaDrawHandler) {
		this.areaDrawHandler.deactivate();
	}
};

GridManager.prototype.deactiveAll = function() {
	this.deactiveDraw();
	if (this.modifyControl != null && this.modifyControl.active) {
		this.modifyControl.deactivate();
	}
};

GridManager.prototype.showAroundHotspot = function(style) {
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

GridManager.prototype.hideAroundHotspot = function() {
	this.showlayer.removeAllFeatures();
};

GridManager.prototype.activeDrawArea = function() {
	// 将绘制的对象移动到周边图层中
	OMAP.MapLib.moveLayerFeature2Layer(this.editlayer, this.showlayer);
	this.editlayer.removeAllFeatures();
	this.areaDrawHandler.activate();
	var me = this;
	this.areaDrawHandler.events.on({
		"featureadded" : function(eventArgs) {
			this.geometry = eventArgs.feature.geometry;
			me.areaDrawHandler.deactivate();
			// 更新mapinfo里面的坐标
			setCoord(this.geometry);
			me.activeModify();
		}
	});
};

GridManager.prototype.activeDrawPoint = function() {
	// 将绘制的对象移动到周边图层中
	OMAP.MapLib.moveLayerFeature2Layer(this.editlayer, this.showlayer);
	this.editlayer.removeAllFeatures();
	this.ptDrawHandler.activate();
	var me = this;
	this.ptDrawHandler.events.on({
		"featureadded" : function(eventArgs) {
			this.geometry = eventArgs.feature.geometry;
			me.ptDrawHandler.deactivate();
			// 更新mapinfo里面的坐标
			setCoord(this.geometry);
			me.activeModify();
		}
	});
};

GridManager.prototype.activeDrawLine = function() {
	// 将绘制的对象移动到周边图层中
	OMAP.MapLib.moveLayerFeature2Layer(this.editlayer, this.showlayer);
	this.editlayer.removeAllFeatures();
	this.plDrawHandler.activate();
	var me = this;
	this.plDrawHandler.events.on({
		"featureadded" : function(eventArgs) {
			this.geometry = eventArgs.feature.geometry;
			me.plDrawHandler.deactivate();
			// 更新mapinfo里面的坐标
			setCoord(this.geometry);
			me.activeModify();
		}
	});
};

GridManager.prototype.activeSelectArea = function() {
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
					setCoord(feature);
				},
				toggleKey : "ctrlKey", // ctrl key removes from
				// selection
				multipleKey : "shiftKey" // shift key adds to
			});
	this.map.addControl(this.selectHover);
	this.selectHover.activate();
};

GridManager.prototype.activeModify = function() {
	if (this.modifyControl == null) {
		this.modifyControl = new OMAP.Control.ModifyFeature(
				this.editlayer);
		this.map.addControl(this.modifyControl);

		this.modifyControl.mode = OMAP.Control.ModifyFeature.RESHAPE
				| OMAP.Control.ModifyFeature.DRAG;

		this.editlayer.events.on({
			"afterfeaturemodified" : function(evt) {
				setCoord(evt.feature.geometry);
			}
		});
	}
	if (!this.modifyControl.active) {
		this.modifyControl.activate();
	}
//	this.snapControl = new OMAP.Control.Snapping({
//		layer: this.snaplayer,
//		targets:this.showlayer,
//		defaults:{
//		nodeTolerance:30,
//		vertexTolerance:30,
//		edgeTolerance:20
//		},
//		greedy: false
//		});
//	map.addControl(this.snapControl);
//	this.snapControl.activate();
};

GridManager.prototype.endModify = function() {
	if (this.modifyControl.active) {
		this.modifyControl.deactivate();
	}
//	if (this.snapControl.active) {
//		this.snapControl.deactivate();
//	}
	OMAP.MapLib.moveLayerFeature2Layer(this.editlayer, this.showlayer);
	this.editlayer.removeAllFeatures();
};

GridManager.prototype.editPolygon = function(xs,ys,sweight,scolor,fcolor) {
	this.editlayer.removeAllFeatures();
	var polygon = YMAP.MapLib.createPolygon(xss, yss);
	var f = new YMAP.Feature.Vector(polygon, {
        strokeweight: sweight,
        fillcolor: fcolor,
        strokecolor: scolor
    });
	this.editlayer.addFeatures([ f ]);
	this.activeModify();
};

GridManager.prototype.editPolyline = function(xss, yss) {
	this.editlayer.removeAllFeatures();
	var polyline = OMAP.MapLib.createPolyline(xss, yss);
	var f = new OMAP.Feature.Vector(polyline);
	this.editlayer.addFeatures([ f ]);
	this.activeModify();
};

GridManager.prototype.editPoint = function(xss, yss) {
	var center = new OMAP.LonLat(xss,yss);
	map.setCenter(center, 5, false, false);
	this.editlayer.removeAllFeatures();
	var pt = new OMAP.Geometry.Point(xss, yss);
	var f = new OMAP.Feature.Vector(pt);
	this.editlayer.addFeatures([ f ]);
	this.activeModify();
};

GridManager.prototype.zoomAndCenter = function(x, y, lvl) {
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

GridManager.prototype.drawMarker = function(x,y) {
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
GridManager.prototype.destroyAllG = function(){
	this.removeAllFeatures();
}

GridManager.prototype.allClean = function() {
	this.removeAllPopups();
	this.removeAllFeatures();
}

/** 移除所有弹窗 */
GridManager.prototype.removeAllPopups = function() {
	if(this.map != null && typeof(this.map) != 'undefined'){
		var popups = this.map.popups;
		if(popups != null && popups.length >0){
			for(var p = popups.length-1; p >= 0; p--){
//				popups[p].destroy();
				this.map.removePopup(popups[p]);
			}
		}
		
//		this.map.popups = [];
//		this.map.Renderer.Canvas.clear();
	}
	
	// 调用外部自定义清理坐标信息方法
	if(typeof cleanPopup != 'undefined' && cleanPopup instanceof Function) {        
		cleanPopup(); 
	}
}

/** 移除所有Feature标注 */
GridManager.prototype.removeAllFeatures = function() {
	if(this.editlayer != null && typeof(this.editlayer) != 'undefined'){
		var features = this.editlayer.features;
		if(features != null && features.length>0){
			for(var index in features){
				features[index].destroy();
			}
		}
		this.editlayer.removeAllFeatures();
	}
	if(this.showlayer != null && typeof(this.showlayer) != 'undefined'){
		var features = this.showlayer.features;
		if(features != null && features.length>0){
			for(var index in features){
				features[index].destroy();
			}
		}
		this.showlayer.removeAllFeatures();
	}
	if(this.markerLayer != null && typeof(this.markerLayer) != 'undefined'){
		var features = this.markerLayer.features;
		if(features != null && features.length>0){
			for(var index in features){
				features[index].destroy();
			}
		}
		this.markerLayer.removeAllFeatures();
	}
	if(this.snaplayer != null && typeof(this.snaplayer) != 'undefined'){
		var features = this.snaplayer.features;
//		this.drawlayer.removeFeatures(features);
		if(features != null && features.length>0){
			for(var i =features.length-1;i>=0;i--){
				features[i].destroy();
			}
//			for(var index in features){
//				features[index].destroy();
//			}
		}
//		this.drawlayer.removeAllFeatures();
		
	}
	
	// 调用外部自定义清理坐标信息方法
	if(typeof cleanFeature != 'undefined' && cleanFeature instanceof Function) {        
		cleanFeature(); 
	}
}

GridManager.prototype.go2xy = function (x, y){
	if(this.map == null || typeof(this.map) == 'undefined') return;
    var zoom = this.map.getZoom();
    var center = new OMAP.LonLat(x, y);
	this.map.setCenter(center, zoom, false, false);
}

GridManager.prototype.createPop = function (id, html, x, y, xp, yp, zIndex){
	if(this.map == null || typeof(this.map) == 'undefined') return;
    var popup = new OMAP.Popup(id,
            new OMAP.LonLat(x, y),
            new OMAP.Size(100, 100),
            html,
            false,'',xp,yp);
    popup.setBackgroundColor("transparent");
    popup.autoSize=1;
    //popup.padding = new OMAP.Bounds(-50,0,0,-50);
    this.map.addPopup(popup);
    return popup;
}

/** 设置地图的缩放级别和中心点 */
GridManager.prototype.zoomAndCenter = function(x, y, lvl) {
	if(this.map == null || typeof(this.map) == 'undefined' || x == '' || y == '') return;
	
	x = parseFloat(x);
	y = parseFloat(y);
	if (lvl == null) {
		lvl = 4;
	} 
	else {
		lvl = parseInt(lvl);
	}
	var center = new OMAP.LonLat(x, y);
	this.map.setCenter(center, lvl, false, false);
};