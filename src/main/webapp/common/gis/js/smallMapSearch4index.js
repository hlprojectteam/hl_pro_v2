//地图搜索(小)
var smallMapSearch = {
	init : function(map,id, parent, option) {
		this.keyword = '';
		this.totalPage = 0;
		this.pagesize = 8;
		this.page = 1;
		this.id = id;
		this.map = map;
		option = option || {};
		this.itemClick = option.itemClick;
		this.initUi(parent, option.left || 0, option.top || 0);
		this.createdPop = '';
		
		this.layer = new OMAP.Layer.Markers("searchResult");
		this.map.addLayer(this.layer);
		
		var size = new OMAP.Size(24,33);
		var offset = new OMAP.Pixel(-(size.w/2), -size.h);
		this.icon = new OMAP.Icon('/images/map/flag2.png', size, offset);
	},
	initUi : function(parent, left, top) {
		var htmlStr = '\
			<div id="searchDiv_!{id}" class="tips" style="display:none" >\
				<div class="closeBar">查询结果<img src="/images/bbs_close.gif" style="position: absolute;top:-3px;right: 4px;cursor:pointer" onclick="smallMapSearch.hiddenSearchDiv()"/></div><ul id="result_!{id}">\
				</ul>\
			</div>\
		';
		htmlStr = htmlStr.replace(/!{id}/g, this.id);
		if (typeof (parent) == 'string')
			parent = $g(parent);
		parent.innerHTML = parent.innerHTML + htmlStr;
		this.input = $g('keyword_' + this.id);
		this.searchDiv = $g('searchDiv_' + this.id);
		this.result = $g('result_' + this.id);
	},
	enterKeydown : function(event) {
		var keyCode = 0;
		if (window.event)
			keyCode = event.keyCode;
		else if (event.which)
			keyCode = e.which;

		if (keyCode == 13) {
			smallMapSearch.dosearch();
		}
	},
	dosearch : function(p) {
		// 当前指向的方向
		if (typeof (p) == 'undefined') {
			this.keyword = this.input.value.replace(/^\s*|\s*$/g, '');
			this.page = 1;
		} else {
			this.page = p;
		}

		if (this.keyword == '' || this.keyword == '楼房实体/道路/河流'
				|| $g("keyword_" + this.id).value == '') {
			alert('请输入关键字');
			return;
		}

		var url = "/front/search/search.shtml?act=smallSearch" + "&keyword="
				+ encodeURIComponent(this.keyword) + "&pageSize="
				+ this.pagesize + "&pageNo=" + this.page;
		var me = this;
		OMAP.Request.GET({
			url : url,
			headers : {
				"Content-Type" : "application/Json"
			},
			data : {},
			callback : function(data) {
				me.callback(data.responseText);
			},
			scope : this
		});
	},
	callback : function(data) {
		data = eval('(' + data + ')');
		var result = {
			total : data.totalCount,
			list : data.result
		};
		if (result) {
			this.searchDiv.style.display = '';
			this.result.style.display = '';
		} else {
			return;
		}
		var total = result.total;
		this.totalPage = Math.floor(total / this.pagesize);
		if (total % this.pagesize != 0)
			this.totalPage += 1;
		var list = result.list;

		var parent = this.result;
		parent.innerHTML = '';
		for ( var i = 0; i < list.length; i++) {
			parent.appendChild(this.createResultItem(list[i]));
		}
		var pager = parent.appendChild(document.createElement('div'));
		pager.className = 'tpages';
		this.page = parseInt(this.page);
		var str = '<a href="#" class="b2" onclick="smallMapSearch.go2page('
				+ (this.page - 1)
				+ ');return false;">上一页</a> <a href="#" class="b2" onclick="smallMapSearch.go2page('
				+ (this.page + 1) + ');return false;">下一页</a> ' + this.page
				+ '/' + this.totalPage + ' ' + total + '条信息';
		pager.innerHTML = str;
	},
	createResultItem : function(item) {
		var div1 = document.createElement('li');
		var a1 = div1.appendChild(document.createElement('a'));
		a1.href = '#';
		a1.innerHTML = item.name;
		a1._id = item.id;
		var me = this;
		a1.onclick = function() {
			me.clearMarker();
			var x = item.x;
			var y = item.y;
			var pt = new OMAP.LonLat(x, y);
			me.layer.addMarker(new OMAP.Marker(pt,me.icon.clone()));
			me.map.setCenter(pt, 4, false, false);
			return false;
		};
		return div1;
	},
	go2page : function(page) {
		if (page <= 0 || page > this.totalPage)
			return;
		this.dosearch(page);
	},
	hiddenSearchDiv : function(flag) {
		this.searchDiv.style.display = flag ? '' : 'none';
		this.clearMarker();
	},
	clearMarker : function() {
		if(this.layer == null){
			return;
		}
		
		for(var i=0;i<this.layer.markers.length;i++){
			var marker = this.layer.markers[i];
			this.layer.removeMarker(marker);
		}
	},
	hiddenSearchResult : function(flag) {
		this.result.style.display = flag ? '' : 'none';
	},
	text_focus : function(obj, blur) {
		if (!obj._value)
			obj._value = obj.value;
		var initValue = obj._value;
		if (blur && obj.value == '')
			obj.value = initValue;
		else if (obj.value == initValue)
			obj.value = '';
	},
	text_foucs_hi : function(obj) {
		var name = obj.id.split("_")[1];
		if (name == 'qlr')
			this.qlr = obj.value;
		if (name == 'sjsyr')
			this.sjsyr = obj.value;
		if (name == 'zdbh')
			this.zdbh = obj.value;
		if (name == 'qlrzh')
			this.qlrzh = obj.value;
		if (name == 'tdzlx')
			this.tdzlx = obj.value;
	}
};