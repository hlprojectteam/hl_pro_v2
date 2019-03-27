/*
 * 公用地图展示方法类
 * author:tangxuefeng
 * date:2015年7月3日09:47:16
 */
var MapManager = function(map,styleMapOpt) {
	this.map = map;
	this.markerOption = null;
	this.markers = null;
	this.showlayer = null; // 显示图层:名称为showLayer
	this.styleMapOption = null;
	this.setStyleMapOption(styleMapOpt);
	this.init();
};
MapManager.prototype.setMarkerOption = function($markerOption) {
	this.markerOption = $.extend({
		id:'' ,//id
		title: '', // 标注名称
		x: '', // 标注x坐标
		y: '', // 标注y坐标
		iconUrl: '', // 图标资源地址
		width: 50, // 图标宽度
		height: 50 // 图标高度
	}, $markerOption || {});
}

MapManager.prototype.getMarkerOption = function() {
	return this.markerOption;
}

MapManager.prototype.setStyleMapOption = function($StyleMapOption) {
	this.styleMapOption = $.extend({
		strokeColor: "#ff0000",
        strokeOpacity: 1,
        strokeWidth: 1,
        fillColor: "#000000",
        fillOpacity: 0.1,
        graphicName:"star",
        pointRadius: 0,
        label : "${id}",
	    fontColor: "#000000",
	    fontSize: "20px",
	    labelAlign:"lb"
	}, $StyleMapOption || {});
}

MapManager.prototype.getStyleMapOption = function() {
	return this.styleMapOption;
}

MapManager.prototype.init = function() {
	if (this.showlayer == null) {
		this.showlayer = new OMAP.Layer.Vector("标注文字图层", {
	           styleMap: new OMAP.StyleMap({'default':this.styleMapOption})
	        	           }
	 );
		this.map.addLayer(this.showlayer);
	}
	if (this.markers == null) {
		this.markers = new OMAP.Layer.Markers("标注");
		this.map.addLayer(this.markers);
	}
};

MapManager.prototype.drawMarkerByOption = function($markerOption) {
	this.setMarkerOption($markerOption);
	var size = new OMAP.Size(this.markerOption.width,this.markerOption.height);
	var offset = new OMAP.Pixel(-(size.w/2), -size.h);
	var jz = new OMAP.Icon(this.markerOption.iconUrl,size,offset);
	feature = new OMAP.Feature(this.markers,new OMAP.LonLat(this.markerOption.x,this.markerOption.y),{'icon': jz});
	marker = feature.createMarker();
	this.markers.addMarker(marker);
	var point = new OMAP.Geometry.Point(this.markerOption.x+50,this.markerOption.y);
    feature = new OMAP.Feature.Vector(point);
       feature.attributes.id = this.markerOption.title;
       feature=[feature];
    this.showlayer.addFeatures(feature);
}

MapManager.prototype.drawText = function(x,y,title) {
	var point = new OMAP.Geometry.Point(x,y);
    feature = new OMAP.Feature.Vector(point);
       feature.attributes.id = title;
       feature=[feature];
    this.showlayer.addFeatures(feature);
}

MapManager.prototype.zoomAndCenter = function(x, y, lvl) {
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

MapManager.prototype.drawMarker = function(x,y,iconUrl) {
	if(iconUrl==null){
		iconUrl = '/gis/images/front/common/flag2.png';
	}
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
			externalGraphic:iconUrl
	};
	var f = new OMAP.Feature.Vector(pt,null,style);
	this.showlayer.addFeatures([f]);
};
