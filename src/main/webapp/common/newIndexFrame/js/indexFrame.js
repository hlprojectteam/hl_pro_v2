/**
 * 主页框架自定义js
 */
var navigationBarColour = localStorage.getItem("navigationBarColour");//导航栏蓝色
if(navigationBarColour!=null&&navigationBarColour!=""){
	$(".navbar-fixed-top").attr("class","site-navbar navbar navbar-default navbar-fixed-top navbar-inverse "+navigationBarColour);
}
var navbarDisplay = localStorage.getItem("navbarDisplay");//通栏显示
if(navbarDisplay!=null&&navbarDisplay!=""){
	if(navbarDisplay=="true"){
		$(".navbar-fixed-top").addClass("navbar-inverse");
	}else{
		$(".navbar-fixed-top").removeClass("navbar-inverse");
	}
}
var skintoolsSidebar = localStorage.getItem("skintoolsSidebar");//菜单主题颜色
if(skintoolsSidebar!=null&&skintoolsSidebar!=""){
	$(".site-menubar-dark").attr("class","site-menubar site-menubar-light");
}
//是否展开  收起
var menuDisplay = localStorage.getItem("menuDisplay");
if(menuDisplay=="close"){
	setTimeout('$("#admui-toggleMenubar").click()',100);
	setTimeout('$(".rightcontent").css("left","90px")',100);
}
//切换目录
$("#admui-toggleMenubar").click(function(){
	if($(".rightcontent").css("left")=="220px"){
		$(".rightcontent").css("left","90px");
	}else{
		$(".rightcontent").css("left","220px");
	}
});
//主题颜色
var themeColor = localStorage.getItem("themeColor");
if(themeColor!=null&&themeColor!=""&&themeColor!="null"){
	setTimeout('$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/'+themeColor+'.css")',100);
}
//Tab页签
var tabFlag = localStorage.getItem("tabFlag");
if(tabFlag=="close"){
	parent.$(".content-tabs").hide();	
	parent.$(".J_mainContent").css("top","0px");	
}