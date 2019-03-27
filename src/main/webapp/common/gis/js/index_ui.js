function $g(id){
	return document.getElementById(id);
}


var mapAction = {
	action : '',
	argu : []
};

/* 地图尺寸，自适应宽高,侧边栏显示 */
$(function() {
	$('#left_box_close').click(function() {
		if ($(this).css('left') != '48px' && $(this).css('left') != '0px' && $(this).css('left') != '') {
			openCloseLeftPanel(false);
		} else {
			openCloseLeftPanel(true);
		}
	});
	ui.init();
	ui.resize2();
	/*$('#right_box_close').click(function() {
		if (parseInt($(this).css('right')) != 0) {
			openCloseRightPanel(false);
		} else {
			openCloseRightPanel(true);
		}
	});

	$('#btn_full').click(function() {
		ui.showFull();
		$(this).hide();
		$('#btn_full_exit').css('display', 'inline-block');
	});
	$('#btn_full_exit').click(function() {
		ui.exitFull();
		$(this).hide();
		$('#btn_full').css('display', 'inline-block');
	});
	$('#addNoteBtn').click(addNoteOpen);
	$('#addErrorBtn').click(addErrOpen);
	$('#doScaleBtn').click(doScale);
	$('#doAreaBtn').click(doArea);
	$('#downloadMapBtn').click(downloadMap);
	$('#busSign').click(loadSign); $('#subwaySign').click(loadSign);
	$('#entitySign').click(loadSign); $('#areaSign').click(loadSign);
	$('#divMapClear').click(mapClear);*/
	// smallMapSearch.init('liangshanzhou','1','smallSearchDiv',{itemClick:function(item){showResult(item.id,1);}});

	// getBulletin();

//	// 切换平面、三维地图
//	function changeMapType() {
//		var btn = $(this);
//		if (btn.attr('class') == 'btn_w_cur')
//			return;
//		$('#d45').removeClass('btn_w_cur').addClass('btn_w');
//		$('#d85').removeClass('btn_w_cur').addClass('btn_w');
//		btn.removeClass('btn_w').addClass('btn_w_cur');
//		var id = btn.attr('id');
//		var x = omapApi.getCenterX();
//		var y = omapApi.getCenterY();
//		if (id == 'd45') {
//			var pt = calPoint(x, y, 1);
//			popomap.go2xy(pt.x, pt.y);
//			popomap.changeMapType(0);
//		} else if (id == 'd85') {
//			var pt = calPoint(x, y, 7);
//			popomap.go2xy(pt.x, pt.y);
//			popomap.changeMapType(7);
//		}
//		return false;
//	}
//	$('#d45').click(changeMapType);
//	$('#d85').click(changeMapType);
});

function openLeftIfr(url){
	ui.openTarget(url);
	ui.showLeft();
}

function openCloseRightPanel(open) {
	if (open) {
		if (parseInt($('#right_box_close').css('right')) == 0) {
			ui.showRight(ui.preRightW);
			$(".shrink_btn2").css({
				"background-image" : "url('/gis/images/front/index/shrink_icon_2.png')"
			});
		}
	} else {
		if (parseInt($('#right_box_close').css('right')) != 0) {
			ui.hideRight();
			$(".shrink_btn2").css({
				"background-image" : "url('/gis/images/front/index/shrink_icon.png')"
			});
		}
	}
}

//弹出或隐藏GIS信息页面左边栏
function openCloseLeftPanel(open) {
	if (open) {
		if ($('#left_box_close').css('left') == '48px' || $('#left_box_close').css('left') == '0px' ||$('#left_box_close').css('left') == '') {
			ui.showLeft(ui.preLeftW);
			$(".shrink_btn").css({
				"background-image" : "url('/gis/images/front/index/shrink_icon.png')"
			});
		}
	} else {
		if ($('#left_box_close').css('left') != '48px' ||$('#left_box_close').css('left') != '0px' ||$('#left_box_close').css('left') != '') {
			ui.hideLeft();
			$(".shrink_btn").css({
				"background-image" : "url('/gis/images/front/index/shrink_icon_2.png')"
			});
		}
	}
}
//隐藏GIS信息页面左边栏（从IFrame内部调用外部的元素时）
function closeLeftPanel(){
	ui.init();
	ui.resize2();
	openCloseLeftPanel2(false);
}
//弹出或隐藏GIS信息页面左边栏 （从IFrame内部调用外部的元素时）
function openCloseLeftPanel2(open) {
	var left = top.left_box_close.style.left;
	if (open) {
		if (left == '48px' || left == '0px' || left == '') {
			ui.showLeft2(ui.preLeftW);
			top.$(".shrink_btn").css({
				"background-image" : "url('/gis/images/front/index/shrink_icon.png')"
			});
		}
	} else {
		if (left != '48px' || left != '0px' ||left != '') {
			ui.hideLeft2();
			top.$(".shrink_btn").css({
				"background-image" : "url('/gis/images/front/index/shrink_icon_2.png')"
			});
		}
	}
}


var ui = {
		init : function() {
			this.topH = 111;
			//content框左边margin
			this.marginLeft=0;
			this.leftW = 48;
			this.rightW = 0;
			this.docW = 0;
			this.docH = 0;
			this.minW = 960;
			this.allShow = false;
			this.leftShow = true;
			this.rightShow = true;
//			this.hideLeft();
			this.preLeftW = 0;
			this.leftPadding = true;
		},
	    openTarget : function(url) {
	        $g('ifrLeft').src = url;
	    },
	    openOrg : function(url) {
	        $g('ifrOrg').src= url;
	    },
		showFull : function(o) {
			// if(!o) o=MainMenu.getMenuItemById('fullScreen');
			if (!this.allShow) {
				$g('head_box').style.display = 'block';
				if (this.leftShow)
					$g('lefbar').style.display = 'block';
				$g('left_box_close').style.display = 'block';
				this.allShow = true;
				this.topH = 105;
				if (this.leftShow)
					this.leftW = 326;
				this.adjust_left_close();
				this.adjust_map();
				$g('btn_fullscreen').className = 'btn_qp';
			} else {
				$g('head_box').style.display = 'none';
				$g('lefbar').style.display = 'none';
				$g('left_box_close').style.display = 'none';
				this.allShow = false;
				this.topH = 0;
				this.leftW = 0;
				this.adjust_left_close();
				this.adjust_map();
				$g('btn_fullscreen').className = 'btn_fanhui';
			}
			this.resize();
		},
		showMap : function() {
			$g('content').style.display = 'block';
//			$g('mapDiv').style.display = 'block';
			$g('left_box_close').style.display = 'block';
			//this.adjust_right_close();
			this.adjust_left_close();
			this.adjust_map();

		},
		showMap2 : function() {

//			$g('mapDiv').style.display = 'block';
			$g('left_box_close').style.display = 'block';
			this.adjust_left_close2();
			this.adjust_map2();
		},
		showMapRight : function()
		{
//			$g('mapDiv').style.display = 'block';
			$g('right_box_close').style.display = 'block';
			this.adjust_right_close();
			this.adjust_map();
		},
		showMapNav : function() {
			this.showMap();
			this.hideLeft();
		},
		showLeft : function(left) {
			if(this.leftPadding == true){
				this.leftW = left ? left :326;

			}else{
				this.leftW = left ? left :278;
			}

			//this.leftW = 278;
			this.leftShow = true;
			$('#left_box_close').css({
				left : this.leftW
			});
			$("#mapInfoPanel").css({"left":this.leftW + $('#left_box_close').width()});
			$g('lefbar').style.display = 'block';
			this.resize();
			this.showMap();
			$g('left_box_close_btn').className = 'close_btn';
		},
		showRight : function(right) {
			this.rightW = right ? right :326;
			this.leftW = 0;
			this.rightShow = true;
			$('#right_box_close').css({
				right : this.rightW
			});
			$g('rightbar').style.display = 'block';
			this.resize();
			this.showMapRight();
			$g('right_box_close_btn').className = 'close_btn';
			//按钮显示
			$g('right_box_close_btn').style.display = 'block';
		},
		showLeft2 : function(left) {
			this.leftW = left ? left :326;
			//this.leftW = 278;
			this.leftShow = true;
			top.left_box_close.style.left = this.leftW;
			top.lefbar.style.display = 'block';
			this.resize2();
			this.showMap2();
			top.left_box_close_btn.className = 'close_btn';
		},
		hideLeft : function() {
			this.preLeftW = this.leftW;
			if(this.leftPadding == true){
				this.leftW = 48 ;
			}else{
				this.leftW = 0 ;
			}
			this.leftShow = false;
			$('#left_box_close').css({
				left : this.leftW
			});
			$("#mapInfoPanel").css({"left":this.leftW + $('#left_box_close').width()});
			$g('lefbar').style.display = 'none';
			this.resize();
			this.showMap();
			$g('left_box_close_btn').className = 'open_btn';
		},
		hideRight : function() {
			this.preRightW = this.rightW;
			this.rightW = 0;
			this.rightShow = false;
			$('#right_box_close').css({
				right : this.rightW
			});
			$g('rightbar').style.display = 'none';
			this.resize();
			this.showMap();
			$g('rightbar_box_close_btn').className = 'open_btn';
		},
		hideLeft2 : function() {
			this.preLeftW = this.leftW;
			this.leftW = 48;
			this.leftShow = false;
			top.left_box_close.style.left = this.leftW;
			top.lefbar.style.display ='none';

			this.resize2();
			this.showMap2();
			top.$g('left_box_close_btn').className = 'open_btn';
		},
		adjust_map : function() {
			try{
//				$g('content').style.height = (this.docH - this.topH) + 'px';
//				$g('content').style.width = (this.docW - this.leftW) + 'px';
//				$g('content').style.top = this.topH + 'px';
//				$g('content').style.left = this.leftW + 'px';
//				$g('mapDiv').style.height = (this.docH - this.topH) + 'px';
//				$g('mapDiv').style.width = (this.docW - this.leftW) + 'px';
//				$g('mapDiv').style.top = this.topH + 'px';
//				$g('mapDiv').style.left = this.leftW + 'px';
//				$g('map1').style.height = (this.docH - this.topH) + 'px';
//				$g('map1').style.width = (this.docW - this.leftW) + 'px';
//				$g('map1').style.top = this.topH + 'px';
//				$g('map1').style.left = this.leftW + 'px';
//				$g('map2').style.height = (this.docH - this.topH) + 'px';
//				$g('map2').style.width = (this.docW - this.leftW) + 'px';
//				$g('map2').style.top = this.topH + 'px';
//				$g('map2').style.left = this.leftW + 'px';
			}catch(e){}
		},
		adjust_map2 : function() {
			/*$g('menuContent').style.width = (this.docW - this.leftW - this.marginLeft)
					+ 'px';
			$g('menuContent').style.top = this.topH + 'px';
			$g('menuContent').style.left = this.leftW + this.marginLeft + 'px';*/
//			top.$g('content').style.height = (this.docH - this.topH) + 'px';
//			top.$g('content').style.width = (this.docW - this.leftW- this.marginLeft) + 'px';
//			top.$g('content').style.top = this.topH + 'px';
//			top.$g('content').style.left = this.leftW + 'px';
//			top.$g('mapDiv').style.height = (this.docH - this.topH) + 'px';
//			top.$g('mapDiv').style.width = (this.docW - this.leftW) + 'px';
//			top.$g('mapDiv').style.top = this.topH + 'px';
//			top.$g('mapDiv').style.left = this.leftW + 'px';
//			top.$g('map1').style.height = (this.docH - this.topH) + 'px';
//			top.$g('map1').style.width = (this.docW - this.leftW) + 'px';
//			top.$g('map1').style.top = this.topH + 'px';
//			top.$g('map1').style.left = this.leftW + 'px';
//			top.$g('map2').style.height = (this.docH - this.topH) + 'px';
//			top.$g('map2').style.width = (this.docW - this.leftW) + 'px';
//			top.$g('map2').style.top = this.topH + 'px';
//			top.$g('map2').style.left = this.leftW + 'px';
		},
		adjust_left_close : function() {
			try{
				$g('left_box_close').style.height = (this.docH - this.topH) + 'px';
				$g('left_box_close_line').style.height = (this.docH - this.topH) + 'px';
				// $g('left_box_close').style.width='20px';
				$g('left_box_close_btn').style.top = Math
						.floor((this.docH - this.topH) / 2)
						+ 'px';
				$g('left_box_close').style.left = this.leftW + 'px';
				$g('left_box_close').style.zIndex = 1000;
			}catch(e){}
		},
		adjust_left_close2 : function() {
			top.left_box_close.style.height = (this.docH - this.topH) + 'px';
			top.left_box_close_line.style.height = (this.docH - this.topH) + 'px';
			// $g('left_box_close').style.width='20px';
			top.left_box_close_btn.style.top = Math
					.floor((this.docH - this.topH) / 2)
					+ 'px';
			top.left_box_close.style.left = this.leftW + 'px';
			top.left_box_close.style.zIndex = 100;
		},
		adjust_right_close : function() {
			$g('right_box_close').style.height = (this.docH - this.topH) + 'px';
			$g('right_box_close_line').style.height = (this.docH - this.topH) + 'px';
			// $g('left_box_close').style.width='20px';
			$g('right_box_close_btn').style.top = Math
					.floor((this.docH - this.topH) / 2)
					+ 'px';
			$g('right_box_close').style.right = this.rightW + 'px';
			$g('right_box_close').style.zIndex = 100;
		},
		adjust_left : function() {
			this.adjust_left_close();
			$g('lefbar').style.height = (this.docH - this.topH) + 'px';
			$g('lefbar').style.width = 278 + 'px';
			//$(window.frames['leftIfr'].document).find('.left_box_con').css('height',$g('sy_box').style.height);

		},
		adjust_right : function() {
			this.adjust_right_close();
			$g('rightbar').style.height = (this.docH - this.topH) + 'px';
			$g('rightbar').style.width = this.rightW + 'px';
//			$g('sy_box').style.height = (this.docH - this.topH) + 'px';
////			$g('sy_right_box').style.width = (this.docW - 380) + 'px';
//			var bottom_height = $('#sy_right_box').height()-$('.sy_right_top').height();
//			var bottom_c_height = bottom_height - $('.sy_right_bottom_title').height();
//			$('.sy_right_bottom,.sy_right_bottom_l,.sy_right_bottom_r').height(bottom_height);
//			$('.sy_right_bottom_l_c,.sy_right_bottom_r_c').height(bottom_c_height);
//			$(window.frames['leftIfr'].document).find('.left_box_con').css('height',$g('sy_box').style.height);
		},
		adjust_left2 : function() {
			this.adjust_left_close2();
			top.lefbar.style.height = (this.docH - this.topH) + 'px';
			top.lefbar.style.width = this.leftW-48 + 'px';
//			top.sy_box.style.height =
//				(this.docH - this.topH) + 'px';
//			$g('sy_right_box').style.width = (this.docW - 380) + 'px';
//			var sy_right_top_height = top.sy_right_top.style.height == "" ? 0 : top.sy_right_top.style.height;
//			var bottom_height = parseInt(top.sy_right_box.style.height) - parseInt(sy_right_top_height);

//			var bottom_c_height = bottom_height - top.$('.sy_right_bottom_title').height();
//			top.$('.sy_right_bottom,.sy_right_bottom_l,.sy_right_bottom_r').height(bottom_height);
//			top.$('.sy_right_bottom_l_c,.sy_right_bottom_r_c').height(bottom_c_height);
//			top.$(top.window.frames['leftIfr'].document).find('.left_box_con').css('height',top.$g('sy_box').style.height);
		},
		resize : function() {
			var docW = document.documentElement.clientWidth;
			var docH = document.documentElement.clientHeight;
			this.docW = docW;
			this.docH = docH;
			this.adjust_left();
			//this.adjust_right();
			//this.adjust_map();
		},
		resize2 : function() {
			var docW = top.document.documentElement.clientWidth;
			var docH = top.document.documentElement.clientHeight;
			this.docW = docW;
			this.docH = docH;
			this.adjust_left2();
			this.adjust_map2();
		},
		changePage : function(id){
			if(id == 'navBtn_1'){
				$('#content').hide();
				$('#left_box_close').hide();
				$('#lefbar').hide();

			}else{
				$('#content').show();
				$('#left_box_close').show();
				$('#lefbar').show();

			}

		}
	};

	var tool_ui = {
		head_nav_num : 1,
		head_nav_sub_num : 0,
		init : function(){
			this.f_adjust_range();
		},
		changePanel : function(index) {
			var curIndex = this.curIndex || 1;
			$g('navBtn_' + curIndex).className = '';
			$g('navBtn_' + index).className = 'sel';
			this.curIndex = index;
		},
		changeToolBtn : function(id) {
			if ($g('tool_btn_' + id).className == 'git_btn_on') {
				$g('tool_btn_' + id).className = 'git_btn_off';
			} else {
				$g('tool_btn_' + id).className = 'git_btn_on';
			}
		},
		resetAllToolBtn : function(index)
		{
			for(var i = 1; i < index; i++)
			{
				$g('tool_btn_' + i).className = 'git_btn_off';
			}
		},
		showToolNavBar : function(id) {
			if ($g(id).style.display == 'none')
				$g(id).style.display = 'block';
			else
				$g(id).style.display = 'none';

		},
		out_menu : function(id) {
			console.log(id);
			$g(id).style.display = 'none';
		},
		menu_over : function(obj) {
			var left = -1;
			if (typeof (obj) != 'object') {
				left = $("#"+obj).offset().left;
				obj = obj.replace('navBtn_', '');
				obj = $g('panel_menu' + obj);

			}
			if (!obj)
				return;
			if(left !== -1){
				obj.style.left = left + "px";
			}
			obj.style.display = 'block';

		},
		menu_out : function(obj) {
			if (typeof (obj) != 'object') {
				obj = obj.replace('navBtn_', '');
				obj = $g('panel_menu' + obj);
			}
			if (!obj)
				return;
			obj.style.display = 'none';
		},
		// 通过Nav 和sub得到Nav 对象
		f_getNavByNum : function(navNum) {
			var obj = $g("head_nav_" + navNum);
			return obj;
		},
		f_getSubByNavAndNum : function(navNum, num) {
			var obj = $g("head_nav_" + navNum + "_sub_" + num);
			return obj;
		},
		f_selected_nav_mark : function(currentNav, currentNavSub) {
			var objCurrentSub;
			var objLastSub;
			if (currentNavSub) {
				var objLastNav = this.f_getNavByNum(this.head_nav_num);
				// 非同一级子菜单,更改上次点击的子菜单所在的父菜单
				var objCurrentNav = this.f_getNavByNum(currentNav);
				if (currentNav != this.head_nav_num) {
					objLastNav.className = "head_nav_off";
					objCurrentNav.className = "head_nav_on";
					if (currentNavSub != 0) {
						objCurrentSub = this.f_getSubByNavAndNum(currentNav,
								currentNavSub);
						objCurrentSub.className = "subnav_box_current";
					}
					if (this.head_nav_sub_num != 0) {
						objLastSub = this.f_getSubByNavAndNum(this.head_nav_num,
								this.head_nav_sub_num);
						objLastSub.className = "subnav_box_off";
					}
				}
				if (currentNav == this.head_nav_num) {
					if (currentNavSub != this.head_nav_sub_num) {
						if (currentNavSub != 0) {
							objCurrentSub = this.f_getSubByNavAndNum(currentNav,
									currentNavSub);
							objCurrentSub.className = "subnav_box_current";
						}
						if (this.head_nav_sub_num != 0) {
							objLastSub = this.f_getSubByNavAndNum(
									this.head_nav_num, this.head_nav_sub_num);
							objLastSub.className = "subnav_box_off";
						}
					}
				}
				this.head_nav_num = currentNav;
				this.head_nav_sub_num = currentNavSub;
			} else if (currentNav) {
				if (this.head_nav_sub_num != 0) {
					objLastSub = this.f_getSubByNavAndNum(this.head_nav_num,
							this.head_nav_sub_num);
					objLastSub.className = "subnav_box_off";
					this.head_nav_sub_num = 0;
				}
				var t_nav = this.f_getNavByNum(this.head_nav_num);
				t_nav.className = "head_nav_off";
				var t_nav = this.f_getNavByNum(currentNav);
				t_nav.className = "head_nav_on";
				this.head_nav_num = currentNav;
			}
		},
		f_change_leftBar_src : function(url_value) {
			$g('ifrLeft').src = url_value;
			$g('ifrLeft').style.width = ui.leftW + 'px';
		},
		f_adjust_range : function(){
			var showSum = parseInt($('#head_box').width() / 76) + 1;
			if(showSum < 18){
				for(var i = showSum ;i < 19; i++){
					$('#head_nav_'+i).hide();
					$('#navImg_'+i).hide();
				}
				$('#navOthers').show();
				$('#navOthersImg').show();
			}
		},
		f_change_range : function(){
			if(!$('#head_nav_18').is(":visible")){
				$('#navBtnOthers').text("<<");
			}else{
				$('#navBtnOthers').text(">>");
			}
			for(var i=1;i<19;i++){
				$('#head_nav_'+i).toggle();
				$('#navImg_'+i).toggle();
			}


		}
	};

	var dialog_ui = {
			odata : {},
			showDialogMoveBox : function(o) {
				// {id:'test',title:'测试',url:'http://www.baidu.com',t:120,l:340,w:400,h:400,closefunc}
				if (!o.id || !o.title || !o.url)
					return;
				this.odata = o;
				if (!o.t)
					o.t = 120;
				if (!o.l)
					o.l = 340;
				if (!o.w)
					o.w = 400;
				if (!o.h)
					o.h = 400;

				var fatherDiv = $g('multiDialogBox');
				fatherDiv.style.display = 'block';

				if ($g('multiDialog_' + o.id)) {
					var dldiv = $g('multiDialog_' + o.id);
					if (dldiv.style.display == 'none')
						dldiv.style.display = 'block';

					$g('multiDialogtitle_' + o.id).innerHTML = o.title;
					$g('multiDialogIfr_' + o.id).src = o.url;

				} else {

					var subNode = document.createElement("div");
					var subId = "multiDialog_" + o.id;
					subNode.id = subId;
					subNode.className = 'o_newbox';
					subNode.style.top = o.t + 'px';
					subNode.style.left = o.l + 'px';
					subNode.style.display = 'block';

					var dwidth = o.w - 8;
					var inhtml = '<div class="ocn_vp o_subst"><div class="o_newtitle"><div class="o_s01_allt o_s01tl"><img src="/images/wangge/substance_bk.png" class="o_png o_newtitleleft"></div>';
					inhtml += '<div class="o_subst_tit o_s03tm" style="width:'
							+ dwidth
							+ 'px;"><span class="tsd" id="multiDialogtitle_'
							+ o.id
							+ '">'
							+ o.title
							+ '</span><span class="o_subst_close" style="cursor: pointer" onclick="top.dialog_ui.closeDialogMoveBox(\''
							+ subId + '\',' + o.closefunc + ');">';
					inhtml += '</span><span class="o_newtitlemove" style="cursor: pointer" onmousedown="utils.html.drag('
							+ subId
							+ ',this.parentNode)"></span></div><div class="o_s01_allt o_s03tr" style="left:'
							+ o.w + 'px;">';
					inhtml += '<img src="/images/wangge/substance_bk.png" class="o_png o_newtitleright"></div></div><div class="o_subst_cont" style="width: '
							+ o.w + 'px; height: ' + o.h + 'px;">';
					inhtml += '<iframe src="'
							+ o.url
							+ '" scrolling="no" id="multiDialogIfr_'
							+ o.id
							+ '" frameborder="0" style="width: 100%; height: 100%;"></iframe></div><div class="o_subst_bot o_c_b"><div class="o_s01_allb o_s01bl"><img src="/images/wangge/substance_bk.png" class="o_png o_newbottonleft"></div>';
					inhtml += '<div class="o_s01_allb o_s03bm" style="width: '
							+ dwidth
							+ 'px;"></div><div class="o_s01_allb o_s03br" style="left: '
							+ o.w
							+ 'px;"><img src="/images/wangge/substance_bk.png" class="o_png o_newbottonright"></div></div></div>';

					subNode.innerHTML = inhtml;
					fatherDiv.appendChild(subNode);

				}
			},
			closeDialogMoveBox : function(id) {
				$g(id).style.display = 'none';
				if (this.odata.closefunc) {
					this.odata.closefunc();
				}
			},
			closeSelfDialogMoveBox : function(id) {
				$g("multiDialog_" + id).style.display = 'none';
				if (this.odata.closefunc) {
					this.odata.closefunc();
				}
			},
			showSelftDialogMoveBox : function(id) {
				$g("multiDialog_" + id).style.display = 'block';
			}
		};

	/*
	 * 用 jQuery 的onload 替换window的onload，
	 * jQuery的onload可以执行多个，但是window的onload只会执行最后一个，前面的会被覆盖，
	 * 所以jsp里面window的onload可能会覆盖掉这里window的onload，或者是被这里window的onload覆盖掉。
	 * 在JS里面请尽量用 jQuery 的onload.
	 */
	// window.onload = function() {
	$(function(){
		ui.init();
		ui.resize();
		//setTimeout(pushletEvent,6000);
	});


	window.onresize = function() {
		ui.resize();
	};


function loadSign() {
	var id = $(this).attr('id');
	if ($(this).attr('checked') != 'checked')
		return;
	switch (id) {
	case 'busSign':
		popomap.loadSign('BusStation');
		break;
	case 'subwaySign':
		break;
	case 'entitySign':
		break;
	case 'areaSign':
		break;
	}
}

/*function pushletEvent()
{
	PL._init();
	//PL.setDebug(true);

    PL.joinListen('/emergmentAccident/new');
}*/

/*function onData(event)
{
	   var eventType = event.get("eventType");

	   //紧急事故id
	   if(eventType == "accident")
	   {
		   var id = event.get("id")
		   var title = decodeURIComponent(event.get("title"));
		   var x = event.get("x");
		   var y = event.get("y");

		   var html = '<img src="/images/aj/huoqing.png" style="margin-left:-40px;margin-top:-50px"><div class="ibox"><div _child="body" class="ifont"><a href="javascript:parent.showAccidentInfo(\''+title+'\',\'' + id + '\',' + x + ',' + y + ');"><span _child=' + title + '>' + title + '</span></a></div><div class="ipoint"><img _child="point" src="/api/images/skin5/txtGreenAw.gif"/></div></div>';
		   top.popomap.createPop("marker_" + id ,html,x, y,0,30,1200);
		   popomap.go2xy(x, y);
	   }
	   else if(eventType == "ajInspection")
	   {
		 //安全生产企业id
		   var enterprise_id = event.get("enterprise_id");
		   var x = event.get("x");
		   var y = event.get("y");
		   var name = decodeURIComponent(event.get("name"));
		   if(enterprise_id)
		   {
			   var url = "/aj/ajEnterprise.shtml?act=viewEnterpriseTab&id=" + enterprise_id;
			   top.f_removePop("aj_enterprise_"+id);
			   top.f_createPop("aj_enterprise_"+id,
						  "<div onmousemove='this.parentNode.style.zIndex=this.parentNode.style.zIndex+1' onmouseout='this.parentNode.style.zIndex=100'style='width:50px;height:60px;background:transparent;border:0;padding:0;cursor:hand;'>" +
						  "<img src='/images/ajEnterprise/YES.png' width='29px' height='35px' style='background-position:bottom center; background-attachment:fixed' " +
						  " onclick='parent.popomap.createIframePop(\"aj_enterprise_Iframe\",  \""+name+"\", \""+url+"\", 510, 355, "+x+", "+y+")' />" +
						  "</div>",x,y,13, 35, 100);
		   }
	   }
	   else if(eventType == "emergencyCall")
	   {
		    var x = 1070118;
			var y = 1065220;
			var tel = event.get("mobileNum");
			popomap.go2xy(x, y);
			var name = tel+"紧急呼叫";

			var html = '<div onclick="parent.showAgedIfr(\''
				+tel+'\',\''+ "紧急呼叫"+ '\',' + x
				+ ',' + y+');" class="ibox" ><div _child="body" class="ifont"><a href="javascript:void(0)"><span _child="title">'
				+ name + '</span></a></div><div class="ipoint"><img _child="point" src="/api/images/skin5/b_pos.png"/></div></div>';
			//var html = '<div class="ibox"><div _child="body" class="ifont"><a href="javascript:onclick="parent.showAgedIfr(\''
			//	+mobileNum+'\'"><span _child="title">老人紧急呼叫</span></a></div><div class="ipoint"><img _child="point" src="/api/images/skin5/b_pos.png"/></div></div>';
			top.popomap.createPop("marker_" + tel ,html,x, y,0,30,1200);
			showAgedIfr(tel,name,x,y);
	   }
}*/



function showAccidentInfo(title,id,x,y)
{
   popomap.createIframePop("emergerAccidentFrame",title,"/aj/accident.shtml?act=viewAccidentDetailTabV&id=" + id,517,400,x,y);
}


//function mapLoader() {
//	if (!mapConfig)
//		return;
//	var omapApi = new omap.Api('map', mapConfig);
//	// popomap.config.setEvent('onmousedown',mymousedown);
//	// popomap.setEvent('onmousemove',mymousemove);
//	popomap.config.setEvent('onmouseup', mymouseup);
//	popomap.config.setEvent('onAreaClick', myAreaClick_wangge);
//	// popomap.config.setEvent('onUnitClick',myUnitClick);
//	// popomap.config.setEvent('customFun',myCustomFun);
//	// popomap.config.setScreenOption({page:'member/note.shtml?_flowId=mine-search',params:[map]});
//	popomap.config.addStyle('css1', 'css/skin/map.css');
//	popomap.config.addStyle('css2','/ui/css/map.css');
//	omapApi.onMapStatus('ready', onMapReady);
//	omapApi.createMap('mapDiv');
//}

function myAreaClick_wangge() {
	var id = popomap.getAreaUnitInfo().AreaID;
	var title = popomap.getAreaUnitInfo().AreaName;
	var x = popomap.getAreaUnitInfo().x;
	var y = popomap.getAreaUnitInfo().y;
	var url = "/front/map/entity.shtml?act=list&id=" + id;
	var _ws = 345;
	//var _hs = 405;
	var _hs = 180;
	popomap.removePop("requ_pic_div");
	omapApi.createIframePop("requ_pic_div", title, url, _ws, _hs, x, y);
}

function click_show_wangge_entity(id, x, y, title) {
	var url = "/front/map/entity.shtml?id=" + id;
	var _ws = 345;
	var _hs = 405;
	popomap.removePop("requ_pic_div");
	omapApi.createIframePop("requ_pic_div", title, url, _ws, _hs, x, y);
}


function onMapReady() {
	regSigns();
}
function doParams() {
	var params = utils.web.getRequestParameters();
	if (params.id) {
		mapAction = {
			action : 'showResult',
			argu : [ params.id, 1 ]
		};
	} else if (params.cid) {
		mapAction = {
			action : 'showResult',
			argu : [ params.cid, 2 ]
		};
	} else if (params.noteid) {
		mapAction = {
			action : 'showNote',
			argu : [ params.noteid ]
		};
	} else if (params.right) {
		mapAction = {
			action : 'doRight',
			argu : [ params.right, params.unit, params.param1 ]
		};
	} else if (params.action) {
		mapAction = {
			action : 'action',
			argu : [ params.action, params.param1, params.param2, params.param3 ]
		};
	}
}

function viewMapClear(flag) {
	$g('divMapClear').style.display = flag == false ? 'none' : 'block';
}
function mapClear() {
	omapApi.destroyAllG();
	omapApi.removeAllPop();
	$('#divMapClear').hide();
}

function changeMapType() {
	var id = $(this).attr('id');
	if (id == 'd45') {
		popomap.changeMapType(0);
	} else if (id == 'd85') {
		popomap.changeMapType(7);
	}
}

function getNavStatus() {
	return false;
	if ($g('divMapNav').style.display == '')
		return true;
	else
		return false;
}

function doScale(o) {
	popomap.selectMode(1);
}
function doArea(o) {
	popomap.selectMode(7);
}

function getBulletin() {
	postData("/front/bulletin/bulletin.shtml?act=getBulletin", null, function(
			ret) {
		var data = eval('(' + ret + ')');
		var html = new Array();
		for ( var i = 0, len = data.length; i < len; i++) {
			var b = data[i];
			html.push('<li><a href="#">' + b.title + '</a></li>');
		}
		$('.o_notice').html(html.join(''));
	});
}


function downloadMap() {
	var bounds = omapApi.getInnerInstances().map.getBounds();
	var params = {
		startX : bounds.minX,
		startY : bounds.minY,
		endX : bounds.maxX,
		endY : bounds.maxY
	};
	params.mapLevel = omapApi.getZoom();
	params.mapId = getMapId();
	params.mapType = 3;
	var url = urlManager.getUrl('downMap', params);
	location.href = url;
}

function getAroundAreaPosOpen(fun) {
	actionCallBack['getAroundAreaPos'] = fun;
	setActionSet('getAroundAreaPos');
	popomap.setMapTip('<img src="/images/map/rightTip2.gif">');
}
function getAroundAreaPos(x, y) {
	var fun = actionCallBack['getAroundAreaPos'];
	var center = {
		x : x,
		y : y
	};
	cleanActionSet();
	popomap.setMapTip();
	getAroundAreaOpen(fun, center, 100);
	popomap.go2xy(x, y);
}
function getSelectAreaOpen(fun) {
	actionCallBack['SelectArea'] = fun;
	omapApi.selectMode(6, {
		callback : getSelectArea
	});
}
function getSelectArea() {
	if (confirm('确定此区域吗?')) {
		var data = omapApi.getD();
		omapApi.selectMode(0);
		var fun = actionCallBack['SelectArea'];
		if (typeof fun == 'function') {
			fun(data);
		} else if (typeof fun == 'string') {
		}
	}
	return false;
}
function getAroundAreaOpen(fun, center, distance, targetZ) {
	distance = parseInt(distance);
	var z = 3;
	if (!targetZ) {
		if (distance >= 1200)
			z = 5;
		else if (distance >= 600)
			z = 4;
	} else
		z = targetZ;
	omapApi.zoomto(z);
	omapApi.selectMode(31, {
		callback : getAroundArea,
		center : center,
		radius : distance
	});
	actionCallBack['SelectArea'] = fun;
}
function getAroundArea(data) {
	if (confirm('确定此范围吗?')) {
		var radius = Math.round(data.distance / mapConfig.mpx);
		var range = {
			startX : data.center.x - radius,
			startY : data.center.y - radius,
			endX : data.center.x + radius,
			endY : data.center.y + radius
		};
		data.range = range;
		omapApi.selectMode(0);
		var fun = actionCallBack['SelectArea'];
		if (typeof fun == 'function') {
			fun(data);
		} else if (typeof fun == 'string') {
			crossApi.setCfg('callback', fun);
			crossApi.execute(data);
		}
	}
	return false;
}
//-----------注册图层
var isloadSign = {};
function loadSign(id, selected) {
	isloadSign[id] = selected;
	if (selected) {
		popomap.loadSign(id);
		viewMapClear(true);
	} else
		popomap.removeSign(id);
}
function regSigns() {
	var db = "";
	if(window.database){
		db = database;
	}

	var dataLoader = popomap.getInnerInstances().dataLoader;
	//dataLoader.initDataLoader(omapApi);

	/*var signName = 'numberSign';

	if (signName && !dataLoader.getSignHandler(signName)) {
		dataLoader.regSign(signName, null, {
			isLoad : false,
			viewType : 1,
			z : 6
		});
		dataLoader.getSignHandler(signName).setSign(signName, 'layer',
				'' || 'yp_1.gif', 1);*/

	/*for ( var i = 0; i < db.length; i++) {
		var sign = db[i];
		var signName = sign.englishName;
		if (sign.ico)
			sign.ico = '/image' + sign.ico;
		if (signName && !dataLoader.getSignHandler(signName)) {
			dataLoader.regSign(signName, null, {
				isLoad : sign.display == 'true' ? true : false,
				viewType : sign.viewType,
				z : 6
			});
			dataLoader.getSignHandler(signName).setSign(signName, 'layer',
					sign.ico || 'yp_1.gif', sign.viewType);
		}
		if (sign.display == 'true')
			omapApi.loadSign(signName);
	}*/
}

// ----SearchTips相关
function mapKeywordClick(d) {
	var re = /(<font.*?>)|(<\/font.*?>)/g;
	var keyword = d.name.replace(re, '');
	$("#quickSearch").val(keyword);
	$('#quickSearchBtn').click();
}

function tipDataHandler(list) {
	if (!list)
		return;
	for ( var i = 0; i < list.length; i++) {
		list[i].title = hightlightSustr(list[i].name, 20);
	}
}

function hightlightSustr(str, len) {
	var re = /<.*?>(.*?)<\/.*?>/g;
	var num = 0, keyMap = {};
	str = str.replace(re, function(mstr, m1) {
		keyMap[m1] = mstr;
		return m1;
	});
	var strlen = str.length;
	str = str.substr(0, len);
	if (strlen >= len)
		str += '  ...';
	for ( var item in keyMap) {
		str = str.replace(item, keyMap[item]);
	}
	return str;
}

function showDialog(x, y, title, url) {// 显示浮动可拖拽窗口
	var target = '#ocn_dialog';
	var offestLeft;
	var offestTop;
	var moving = false;
	$(target).show().css({
		'left' : x,
		'top' : y
	});
	$('#ocn_dialog_title').html(title);
	$('#ocn_dialog_content').attr('src', url);
	$(target).mousedown(function(e) {
		var x = e.clientX;
		var y = e.clientY;
		var styleLeft = $(target).css("left");
		var styleTop = $(target).css("top");
		offestLeft = x - parseInt(styleLeft);
		offestTop = y - parseInt(styleTop);
		moving = true;
	});

	$(target).mousemove(function(e) {
		if (moving) {
			var nowLeft = e.clientX - offestLeft;
			var nowTop = e.clientY - offestTop;
			$(target).css({
				"left" : nowLeft,
				"top" : nowTop,
				"cursor" : 'move'
			});
		}
	});
	$(target).mouseup(function(e) {
		moving = false;
	});
}

var dialog_ui = {
	odata : {},
	showDialogMoveBox : function(o) {
		// {id:'test',title:'����',url:'http://www.baidu.com',t:120,l:340,w:400,h:400,closefunc}
		if (!o.id || !o.title || !o.url)
			return;
		this.odata = o;
		if (!o.t)
			o.t = 120;
		if (!o.l)
			o.l = 340;
		if (!o.w)
			o.w = 400;
		if (!o.h)
			o.h = 400;

		var fatherDiv = $g('multiDialogBox');
		fatherDiv.style.display = 'block';

		if ($g('multiDialog_' + o.id)) {
			var dldiv = $g('multiDialog_' + o.id);
			if (dldiv.style.display == 'none')
				dldiv.style.display = 'block';

			$g('multiDialogtitle_' + o.id).innerHTML = o.title;
			$g('multiDialogIfr_' + o.id).src = o.url;

		} else {

			var subNode = document.createElement("div");
			var subId = "multiDialog_" + o.id;
			subNode.id = subId;
			subNode.className = 'o_newbox';
			subNode.style.top = o.t + 'px';
			subNode.style.left = o.l + 'px';
			subNode.style.display = 'block';

			var dwidth = o.w - 8;
			var inhtml = '<div class="ocn_vp o_subst"><div class="o_newtitle"><div class="o_s01_allt o_s01tl"><img src="/images/wangge/substance_bk.png" class="o_png o_newtitleleft"></div>';
			inhtml += '<div class="o_subst_tit o_s03tm" style="width:'
					+ dwidth
					+ 'px;"><span class="tsd" id="multiDialogtitle_'
					+ o.id
					+ '">'
					+ o.title
					+ '</span><span class="o_subst_close" style="cursor: pointer" onclick="top.dialog_ui.closeDialogMoveBox(\''
					+ subId + '\',' + o.closefunc + ');">';
			inhtml += '</span><span class="o_newtitlemove" style="cursor: pointer" onmousedown="utils.html.drag('
					+ subId
					+ ',this.parentNode)"></span></div><div class="o_s01_allt o_s03tr" style="left:'
					+ o.w + 'px;">';
			inhtml += '<img src="/images/wangge/substance_bk.png" class="o_png o_newtitleright"></div></div><div class="o_subst_cont" style="width: '
					+ o.w + 'px; height: ' + o.h + 'px;">';
			inhtml += '<iframe src="'
					+ o.url
					+ '" scrolling="no" id="multiDialogIfr_'
					+ o.id
					+ '" frameborder="0" style="width: 100%; height: 100%;"></iframe></div><div class="o_subst_bot o_c_b"><div class="o_s01_allb o_s01bl"><img src="/images/wangge/substance_bk.png" class="o_png o_newbottonleft"></div>';
			inhtml += '<div class="o_s01_allb o_s03bm" style="width: '
					+ dwidth
					+ 'px;"></div><div class="o_s01_allb o_s03br" style="left: '
					+ o.w
					+ 'px;"><img src="/images/wangge/substance_bk.png" class="o_png o_newbottonright"></div></div></div>';

			subNode.innerHTML = inhtml;
			fatherDiv.appendChild(subNode);

		}
	},
	closeDialogMoveBox : function(id) {
		$g(id).style.display = 'none';
		if (this.odata.closefunc) {
			this.odata.closefunc();
		}
	},
	closeSelfDialogMoveBox : function(id) {
		$g("multiDialog_" + id).style.display = 'none';
		if (this.odata.closefunc) {
			this.odata.closefunc();
		}
	},
	showSelftDialogMoveBox : function(id) {
		$g("multiDialog_" + id).style.display = 'block';
	}
};

function f_myCoorMarkerUtil(doCallBack) {
	var mx = omapApi.getCenterX() - 80;
	var my = omapApi.getCenterY() + 100;
	MyMarkerUtil.reg();
	try {
		omapApi.go2xy(mx + 100, my - 130);
		// MyMarkerUtil.callBackFunc(doCallBack);
		MyMarkerUtil.addIcon('coor', '/images/map/icon1.png', omapApi
				.getCenterX(), omapApi.getCenterY());
	} catch (e) {
		// alert(e.message);
	}
}
// ɾ���Զ����ע
function f_rmoveMyCoorMarkerUtil() {
	try {
		MyMarkerUtil.removeIcon('coor');
		MyMarkerUtil.closeTips();
	} catch (e) {
		// alert(e.message);
	}
}
