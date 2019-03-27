$(function(){
	function winRe(){
		var winH=$(window).height();
		var headerH=$('#header').outerHeight();
		var footerH=$('#footer').outerHeight();
		var bannerH=$('#centerBanner').outerHeight();
		var containerH=winH-headerH-footerH-bannerH-80;
		$('#container').css('min-height',containerH);
	};
	winRe();
	$(window).resize(function(){
		winRe();
	});
	
	//页面结构，异步调用
	//$('#header').load('global.html .header-inner',function(){
		var $search=$('.ui-search');
		var $select=$search.children('.selected');
		$select.click(function(){
			$(this).siblings('ul').show();
		});
		$search.find('ul li a').click(function(e){
			e.preventDefault();
			var a_txt=$(this).text();
			var sel_txt=$select.text();
			$search.children('ul').hide();
			$(this).text(sel_txt);
			$select.children('.select-name').text(a_txt);
		});
		$('.down-app-outer').hover(function(){
			$(this).addClass('down-app-outer-hover');
		},function(){
			$(this).removeClass('down-app-outer-hover');
		});
	//});
//	$('#footer').load('global.html .footer-inner');
//	$('#centerBanner').load('global.html .center-banner-inner');
//	$('#centerSidebar').load('global.html .center-sidebar');
	
	//tab
	$('.ui-tab li').click(function(){
		$(this).addClass('current').siblings('li').removeClass('current');
	});
	$('.course-tabs li').click(function(){
		$(this).addClass('current').siblings('li').removeClass('current');
		$('.course-tabs-wrap .course-tabs-item:eq('+$(this).index()+')').show().siblings('.course-tabs-item').hide();
	});
	$('.ui-label-opt span').click(function(){
		$(this).toggleClass('current');
	});
	//筛选信息
	$('.ui-filter').on('click','.item-radio a',function(e){
		e.preventDefault();
		var $inner=$(this).parents('.ui-filter-inner');
		if($inner.hasClass('ui-filter-checkbox')){
			if(!$(this).hasClass('current')){
				$(this).addClass('current');
			}else{
				$(this).removeClass('current');
			};
		}else{
			$(this).addClass('current').siblings('a').removeClass('current');
		};
	});
	//一级选二级
	$('.ui-filter').on('click','.item-tier1 a',function(e){
		var sort=parseInt($(this).attr('data-sort'));
		if (sort == 0) {
			$(this).parents('.item').children('.item-tier2').hide();
		} else{
			$(this).parents('.item').children('.item-tier2[data-sort='+sort+']').show().siblings('.item-tier2').hide().find('a:eq(0)').addClass('current').siblings('a').removeClass('current');
		};
	});
	//选择学段
	$('.ui-filter').on('click','.item-stage a',function(e){
		var sort=parseInt($(this).attr('data-sort'));
		var txt=$(this).text();
		if (sort == 0 && $(this).parent('.item-radio').hasClass('item-tier1')) {
			$('.ui-filter-cur .item-stage').hide();
		} else if (sort == 0 && $(this).parent('.item-radio').hasClass('item-tier2')) {
			var txt=$(this).parents('.item').find('.item-tier1 a.current').text();
			$('.ui-filter-cur .item-stage').show().text(txt);
		}else{
			$('.ui-filter-cur .item-stage').show().text(txt);
		};
	});
	//选择学科
	$('.ui-filter').on('click','.item-subject a',function(e){
		var sort=parseInt($(this).attr('data-sort'));
		var txt=$(this).text();
		if (sort == 0) {
			$('.ui-filter-cur .item-subject').hide();
		}else{
			$('.ui-filter-cur .item-subject').show().text(txt);
		};
	});
	//选择地区
	$('.ui-filter').on('click','.item-area a',function(e){
		var sort=parseInt($(this).attr('data-sort'));
		var txt=$(this).text();
		if ($(this).parent('.item-radio').hasClass('item-tier1')) {
			if (sort == -1) {
				$('.ui-filter-cur .item-area').hide();
			} else{
				$('.ui-filter-cur .item-area').show().attr('data-pro',txt).text(txt);
			};
		}else if ($(this).parent('.item-radio').hasClass('item-tier2')) {
			if (sort != -1) {
				var pro=$('.ui-filter-cur .item-area').attr('data-pro');
				var txt=$(this).text();
				$('.ui-filter-cur .item-area').show().attr('data-city',txt);
				txt=pro+txt;
				$('.ui-filter-cur .item-area').text(txt);
			}else{
				$('.item-area .item-tier3').hide();
			};
		}else if ($(this).parent('.item-radio').hasClass('item-tier3')) {
			if (sort != -1) {
				var pro=$('.ui-filter-cur .item-area').attr('data-pro');
				var city=$('.ui-filter-cur .item-area').attr('data-city');
				var txt=$(this).text();
				txt=pro+city+txt;
				$('.ui-filter-cur .item-area').show().text(txt);
			};
		};
	});
	//学校、教师更多
	$('.ui-filter').on('click','.item .show-more',function(e){
		e.preventDefault();
		if(!$(this).parents('.item').hasClass('item-more')){
			$(this).children('.fa').addClass('fa-rotate-180').siblings('span').text('收起').parents('.item').addClass('item-more');
		}else{
			$(this).children('.fa').removeClass('fa-rotate-180').siblings('span').text('更多').parents('.item').removeClass('item-more');
		};
	});
	//学校、教师多选
	$('.ui-filter').on('click','.item .sel-chk',function(e){
		e.preventDefault();
		$(this).parents('.item').addClass('item-chk').removeClass('item-more');
	});
	$('.ui-filter').on('click','.item-opt a',function(e){
		e.preventDefault();
		$(this).parents('.item').removeClass('item-chk');
		$(this).parents('.item-left').find('.item-radio a:eq(0)').addClass('current').siblings('a').removeClass('current');
	});
	$('.ui-filter').on('click','.item-opt a.submit',function(e){
		e.preventDefault();
		var chr='';
		$(this).parent('.item-opt').prev('.item-check').find('.ui-checkbox-current').each(function(){
			chr=chr+$(this).parent('label').text()+'、';
		});
		if(chr.length > 0){
			chr = chr.substr(0, chr.length-1);
		}
		if($(this).parents('.item').hasClass('item-school')){
			$('.ui-filter-cur .item-school').show().text(chr);
		}else{
			$('.ui-filter-cur .item-teacher').show().text(chr);
		};
		$(this).parents('.item-left').find('.item-radio a').removeClass('current');
	});
	//选择学校
	$('.ui-filter').on('click','.item-school .item-radio a',function(e){
		var sort=parseInt($(this).attr('data-sort'));
		var txt=$(this).text();
		if (sort == 0) {
			$('.ui-filter-cur .item-school').hide();
		}else{
			$('.ui-filter-cur .item-school').show().text(txt);
		};
	});
	//选择教师
	$('.ui-filter').on('click','.item-teacher .item-radio a',function(e){
		var sort=parseInt($(this).attr('data-sort'));
		var txt=$(this).text();
		if (sort == 0) {
			$('.ui-filter-cur .item-teacher').hide();
		}else{
			$('.ui-filter-cur .item-teacher').show().text(txt);
		};
	});
	//清空筛选
	$('.ui-filter-empty').click(function(e){
		e.preventDefault();
		//$(this).hide();
		$('.ui-filter-cur a').hide();
	});
	//清除单项
	$('.ui-filter-cur a').click(function(e){
		e.preventDefault();
		var cls=$(this).attr('class');
		$(this).hide();
		$('.'+cls).find('.item-radio a:eq(0)').addClass('current').siblings('a').removeClass('current');
		$('.'+cls).find('.item-tier2,.item-tier3-outer').hide();
	});
	
	//下拉筛选信息
	$('.ui-droplist').hover(function(){
		$(this).addClass('active');
	},function(){
		$(this).removeClass('active');
	});
	$('.ui-droplist').not('.ui-droplist-chk').on('click','ul li',function(e){
		e.preventDefault();
		$(this).parents('.ui-droplist').removeClass('active').find('.trigger .txt').text($(this).text());
	});
	//下拉筛选信息-多选
	$('.ui-droplist-chk .submit').click(function(e){
		e.preventDefault();
		$(this).parents('.ui-droplist').removeClass('active');
		var week='';
		$(this).parents('.ui-droplist-chk').find('.week .ui-checkbox-current').each(function(){
			week=week+$(this).parent('label').text();
		});
		var time='';
		$(this).parents('.ui-droplist-chk').find('.time .ui-checkbox-current').each(function(){
			time=time+$(this).parent('label').text()+'、';
		});
		if (week.length !=0) {
			var chr=week;
			if(time.length != 0){
				chr += '(' + time.substr(0, time.length-1)+')';
			}
			$(this).parents('.ui-droplist').find('.trigger .txt').text(chr);
		}else{
			$(this).parents('.ui-droplist').find('.trigger .txt').text("上课时间");
		}
	});
	$('.ui-droplist-chk .clear').click(function(e){
		e.preventDefault();
		$(this).parents('.ui-droplist-chk').find('.ui-checkbox').removeClass('ui-checkbox-current').children('input:checkbox').attr('checked',false);
	});
	
	//首页
	$('.openClass-list li').hover(function(){
		$(this).addClass('hover');
	},function(){
		$(this).removeClass('hover');
	});
	
	//个人中心
	$('.time-line-course li,.plan-list li').hover(function(){
		$(this).addClass('hover');
	},function(){
		$(this).removeClass('hover');
	});
//	$('.time-line-course li .close').click(function(e){
//		e.preventDefault();
//		$(this).parents('li').remove();
//	});
	$('.time-line-item .show-hide').click(function(e){
		e.preventDefault();
		var $item=$(this).parents('.time-line-item');
		if ($item.hasClass('time-line-item-open')) {
			$item.removeClass('time-line-item-open');
			$(this).children('span').text('展开').next('.fa').removeClass('fa-angle-up').addClass('fa-angle-down');
		} else{
			$item.addClass('time-line-item-open');
			$(this).children('span').text('收起').next('.fa').removeClass('fa-angle-down').addClass('fa-angle-up');
		}
	});
	
	
	
	
	
});


$('input:text').focus(function(){
	$(this).addClass('input-focus');
});
$('input:text').blur(function(){
	$(this).removeClass('input-focus');
});
$('.login-item p.user .txt,.login-item p.password .txt,.login-item p.code .txt').focus(function(){
	$(this).parent('p').addClass('focus');
});
$('.login-item p.user .txt,.login-item p.password .txt,.login-item p.code .txt').blur(function(){
	$(this).parent('p').removeClass('focus');
});
//placeholder：兼容IE password
// $.getScript(Domain.static_path + '/open/static/js/jquery.PlaceHolder.js',function(){
// 	placeholder.init();
// });