var LayerCache = function() {
	this.id = null;
	this.name = null;
	this.data = [];
	this.layer = null;
};

var LayerManager = function(map){
	this.map = map;
	this.html = "";
	this.layerCaches = [];
	this.init();
};

LayerManager.prototype.getLayerCache = function(layerId) {
	for(var i=0;i<this.layerCaches.length;i++){
		var layerCache = this.layerCaches[i];
		if(layerCache.id == layerId){
			return layerCache;
		}
	}
	return null;
};

LayerManager.prototype.getFeature = function(layerId,featureId) {
	var layerCache = this.getLayerCache(layerId);
	if(layerCache == null || layerCache.layer == null){
		return null;
	}else {
		for(var i=0;i<layerCache.layer.features.length;i++){
			var feature = layerCache.layer.features[i];
			if(feature.attributes.id == featureId){
				return feature;
			}
		}
		return null;
	}
};

LayerManager.prototype.init = function() {
};

LayerManager.prototype.getLayerCache = function(id,callback) {
	var me = this;
	for(var i=0;i<this.layerCaches.length;i++){
		var layerCache = this.layerCaches[i];
		if(layerCache.id == id){
			callback(layerCache);
		}
	}
	
	$.ajax({
        url: "/front/layer/layerLabelList.shtml?act=list", data: { layerId: id }, dataType: "json", success: function (layerData) {
            var layerCache = new LayerCache();
            layerCache.id = id;
            layerCache.data = layerData;
            me.layerCaches.push(layerCache);
        	callback(layerCache);
        }
    }); 
};

LayerManager.prototype.renderLayer = function(id) {
	//获取图层数据
	var me = this;
	this.getLayerCache(id,function(layerCache) {
		me.drawLayer(layerCache);
	});
};

LayerManager.prototype.hideLayer = function(id){
	this.getLayerCache(id,function(layerCache) {
		if(layerCache.layer == null){
			return;
		}
		
		layerCache.layer.removeAllFeatures();
	});
};

LayerManager.prototype.drawLayer = function(layerCache) {
	var layer = null;
	if(layerCache.layer == null){
		layer = new OMAP.Layer.Vector(layerCache.data.name);
		layerCache.layer = layer;
		this.map.addLayer(layer);
	}else {
		layer = layerCache.layer;
	}
	
	layer.removeAllFeatures();
	for(var i=0;i<layerCache.data.features.length;i++){
		var r = layerCache.data.features[i];
		var geom = null;
		if(layerCache.data.type == 1){
			//点
			geom = new OMAP.Geometry.Point(r.x,r.y);
		}else if(layerCache.data.type == 2){
			geom = OMAP.MapLib.createPolyline(r.xs,r.ys);
		}else if(layerCache.data.type == 3) {
			geom = OMAP.MapLib.createPolygon(r.xs,r.ys);
		}
		var style = this.createStyleFromLayerName(layerCache.data.name);
		var feature = new OMAP.Feature.Vector(geom,r,style);
		layer.addFeatures([feature]);
	}
};

LayerManager.prototype.createStyleFromLayerName = function() {
	var style = {
		graphicWidth : 24,
		graphicHeight : 33,
		graphicYOffset : -33,
		graphicXOffset : -12,
		externalGraphic : "/images/map/flag2.png",
		strokeColor:'red',
		fillColor:'green'
	};
	return style;
};