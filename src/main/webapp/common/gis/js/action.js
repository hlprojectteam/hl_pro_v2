function doAction(action) {
	switch (action) {
	case "addArea":
		entityManager.activeDrawArea();
		mapInfoIfr.location = 'map/entity.shtml?act=edit';
		break;
	case "modifyArea":
		entityManager.deactiveDraw();
		mapInfoIfr.location = '/map/entity.shtml?act=list';
		break;
	case "viewMap":
//		entityManager.deactiveAll();
		entityManager.createMarkers();
		break;
	case "showAroudArea":
		entityManager.showAroundHotspot();
		break;
	case "hideAroudArea":
		entityManager.hideAroundHotspot();
		break;
	case "addGrid":
		gridManager.DrawSnapLayer();
		gridManager.activeDrawArea();
		mapInfoIfr.location='/map/gridManager.shtml?act=edit';
		break;
	case 'listGrid':
		mapInfoIfr.location='/map/gridManager.shtml?act=list';
		break;
	case 'showGrids':
		gridManager.showGridsOnMap();
		break;
	case 'addGridPersons':	
		gridManager.showGridLinesOnMap();
//		gridManager.setDrawControls();
		gridManager.activeDrawPoint();
		mapInfoIfr.location='/map/wanggemanager.shtml?act=edit';
		break;
	case 'listGridPersons':
		mapInfoIfr.location='/map/wanggemanager.shtml?act=list';
		break;
	case 'editUnit':
		mapInfoIfr.location='map/unit.shtml?act=list';
		break;
	case "addUnit":
		mapInfoIfr.location = 'map/unit.shtml?act=add';
		unitManager.activeSelectArea();
		unitManager.showAroundHotspot();
		break;
	case "addLayerLabel":
		mapInfoIfr.location='/gis/layerLabel.shtml?act=edit';
		break;
	case "mistake":
		mapInfoIfr.location = 'map/mistake/message.shtml?act=list';
		break;
	case "editLayerLabel":
		mapInfoIfr.location = '/gis/layerLabel.shtml?act=list';
		break;
	case "addStation":
		mapInfoIfr.location = 'traffic/busstation.shtml?act=edit';
		// 清除站点
		trafficManager.clearAllStation();
		// 加载站点
		trafficManager.showAllStation();
		// 画站点
		trafficManager.activeDrawStation();
		break;
	case "addBusLine":
		mapInfoIfr.location = 'traffic/busline.shtml?act=edit';
		// 清除站点
		trafficManager.clearAllStation();
		// 加载站点
		trafficManager.showAllStation();
		// 重新加载线路
		trafficManager.clearAllLine();
		trafficManager.showAllLine();
		// 画线路
		trafficManager.activeDrawLine();
		break;
	case "editStation":
		mapInfoIfr.location = '/traffic/busstation.shtml?act=list';
		// 加载站点
		trafficManager.showAllStation();
		break;
	case "editbusline":
		// 清除站点
		trafficManager.clearAllStation();
		// 加载站点
		trafficManager.showAllStation();
		// 重新加载线路
		trafficManager.clearAllLine();
		trafficManager.showAllLine();
		mapInfoIfr.location = '/traffic/busline.shtml?act=list';
		break;
	case "addVr":
		mapInfoIfr.location = '/vr.shtml?act=edit';
		vrManager.activeSelectPos();
		break;
	case "editVr":
		mapInfoIfr.location = '/vr.shtml?act=list';
		break;
	default:
		break;
	}
	;
}

var mapInfoIfr = parent.mapinfo;
var VrManager = function(map) {
	this.map = map;
	this.layer = null;
	this.init();
};

VrManager.prototype.init = function() {
	var style = new OMAP.Style({
            graphicWidth: 24,
            graphicHeight: 33,
            graphicYOffset: -33,
            graphicXOffset: -12,
            externalGraphic: "/images/map/flag2.png"
        }
    );
	this.layer = new OMAP.Layer.Vector("schoolActivityPos",{
		styleMap:new OMAP.StyleMap(style)
	});
	this.map.addLayer(this.layer);
	
	if (this.ptDrawHandler == null) {
		this.ptDrawHandler = new OMAP.Control.DrawFeature(
				this.layer, OMAP.Handler.Point);
		this.map.addControl(this.ptDrawHandler);
	}
};

VrManager.prototype.activeSelectPos = function(){
	this.layer.removeAllFeatures();
	this.ptDrawHandler.activate();
	var me = this;
	this.ptDrawHandler.events.on({
		"featureadded" : function(eventArgs) {
			var geometry = eventArgs.feature.geometry;
			var feature = new OMAP.Feature.Vector(geometry,null,null);
			me.layer.addFeatures([feature]);
			me.ptDrawHandler.deactivate();
			// 更新mapinfo里面的坐标
			mapInfoIfr.setCoord(geometry.x,geometry.y);
			me.activeModify();
		}
	});
};

VrManager.prototype.activeModify = function() {
	if (this.modifyControl == null) {
		this.modifyControl = new OMAP.Control.ModifyFeature(
				this.layer);
		this.map.addControl(this.modifyControl);

		this.modifyControl.mode = OMAP.Control.ModifyFeature.DRAG;

		this.layer.events.on({
			"afterfeaturemodified" : function(evt) {
				var geom = evt.feature.geometry;
				mapInfoIfr.setCoord(geom.x,geom.y);
			}
		});
	}
	if (!this.modifyControl.active) {
		this.modifyControl.activate();
	}
};

VrManager.prototype.editPos = function(x,y){
	this.layer.removeAllFeatures();
	var pt = new OMAP.Geometry.Point(x,y);
	var f = new OMAP.Feature.Vector(pt);
	this.layer.addFeatures([f]);
	
	this.activeModify();
};

VrManager.prototype.endModify = function() {
	if (this.modifyControl && this.modifyControl.active) {
		this.modifyControl.deactivate();
	}
	this.layer.removeAllFeatures();
};
