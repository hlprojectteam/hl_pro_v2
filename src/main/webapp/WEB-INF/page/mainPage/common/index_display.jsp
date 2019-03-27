<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" import="com.common.utils.Common" %>
<!DOCTYPE html>
<html class="no-js css-menubar" lang="zh-cn">
<head>
    <title>显示颜色</title>
	<meta charset="utf-8">
	<meta name="keywords" content="cloudting,cloudting官网,云汀后台管理系统" />
	<!-- 样式 -->
	<link rel="stylesheet" href="/common/newIndexFrame/css/themes/classic/skins/site.css" id="siteStyle">
    <!-- 图标 CSS-->
    <link rel="stylesheet" href="/common/newIndexFrame/fonts/font-awesome/font-awesome.css">
    <link rel="stylesheet" href="/common/newIndexFrame/fonts/web-icons/web-icons.css">
    <!-- 插件 CSS -->
    <link rel="stylesheet" href="/common/newIndexFrame/vendor/animsition/animsition.css">
    <link rel="stylesheet" href="/common/newIndexFrame/vendor/toastr/toastr.css">
    <link rel="stylesheet" href="/common/newIndexFrame/css/demo.css">
    
    <script src="/common/newIndexFrame/vendor/jquery/jquery.min.js"></script>
    <script src="/common/newIndexFrame/vendor/bootstrap/bootstrap.min.js"></script>
    
    <!-- 插件 -->
    <script src="/common/newIndexFrame/vendor/modernizr/modernizr.min.js"></script>
    <script src="/common/newIndexFrame/vendor/breakpoints/breakpoints.min.js"></script>
    <script src="/common/newIndexFrame/vendor/artTemplate/template.min.js"></script>
    <script src="/common/newIndexFrame/vendor/toastr/toastr.min.js"></script>
    <!-- 核心  -->
    <script src="/common/newIndexFrame/themes/classic/global/js/core.js"></script>
    <script src="/common/newIndexFrame/themes/classic/base/js/site.js"></script>
    <script src="/common/newIndexFrame/themes/classic/global/js/configs/site-configs.js"></script>
    <script src="/common/newIndexFrame/themes/classic/global/js/components.js"></script>
    
    <script>
    	
	</script>
</head>
<body class="site-contabs-open site-menubar-unfold site-menubar-keep ">
    <div class="page-container" id="admui-pageContent">
	<link rel="stylesheet" href="/common/newIndexFrame/css/system/settings/display.css">
	<link rel="stylesheet" href="/common/newIndexFrame/fonts/material-design/material-design.css">
	<link rel="stylesheet" href="/common/newIndexFrame/vendor/bootstrap-select/bootstrap-select.css">
	<link rel="stylesheet" href="/common/newIndexFrame/vendor/switchery/switchery.css">
<div class="page animation-fade page-display">
    <div class="page-content">
        <form id="displayForm" class="form-horizontal padding-vertical-30">
        
            <div class="form-group">
                <label class="col-sm-2 control-label">导航条颜色：</label>
                <div class="col-sm-10" id="skintoolsNavbar">
                    <ul class="list-unstyled list-inline color-radio">
                        <li>
                            <div class="radio-custom radio-primary">
                                <input type="radio" checked name="navigationColor" value=""> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-brown">
                                <input type="radio"  name="navigationColor" value="bg-brown-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-cyan">
                                <input type="radio"  name="navigationColor" value="bg-cyan-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-green">
                                <input type="radio"  name="navigationColor" value="bg-green-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-grey">
                                <input type="radio"  name="navigationColor" value="bg-grey-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-indigo">
                                <input type="radio"  name="navigationColor" value="bg-indigo-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-orange">
                                <input type="radio"  name="navigationColor" value="bg-orange-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-pink">
                                <input type="radio"  name="navigationColor" value="bg-pink-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-purple">
                                <input type="radio"  name="navigationColor" value="bg-purple-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-red">
                                <input type="radio"  name="navigationColor" value="bg-red-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-teal">
                                <input type="radio"  name="navigationColor" value="bg-teal-600"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-yellow">
                                <input type="radio"  name="navigationColor" value="bg-yellow-700"> <label></label>
                            </div>
                        </li>
                    </ul>
                    <div class="checkbox-custom checkbox-primary margin-top-10">
                        <input type="checkbox" id="navbarDisplay" name="acrossFlag" value="navbar-inverse">
                        <label for="navbarDisplay">通栏显示</label>
                    </div>
                </div>
            </div>
            <hr>
            <div class="form-group">
                <label class="col-sm-2 control-label">菜单主题：</label>
                <div class="col-sm-10">
                    <select data-plugin="selectpicker" id="skintoolsSidebar" name="menuTheme">
                        <option value="site-menubar-dark">深色主题</option>
                        <option value="site-menubar-light">浅色主题</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">菜单显示：</label>
                <div class="col-sm-10">
                    <div class="radio-inline radio-custom radio-primary">
                        <input type="radio" id="menuUnfold" name="menuDisplay" value="site-menubar-unfold">
                        <label for="menuUnfold">展开(默认)</label>
                    </div>
                    <div class="radio-inline radio-custom radio-primary">
                        <input type="radio" id="menuFold"  name="menuDisplay" value="site-menubar-fold">
                        <label for="menuFold">收起</label>
                    </div>
                    <div class="margin-top-10 hidden" id="menuFoldSetting">
                        <!--<span>鼠标经过菜单时显示：</span>
                        <div class="btn-group btn-group-xs" data-toggle="buttons">
                            <label class="btn  btn-outline btn-dark active" for="textIconKeep">
                                <input type="radio" id="textIconKeep" autocomplete="off" hidden="hidden" checked name="menuTxtIcon" value="site-menubar-keep"> 文字
                            </label> <label class="btn btn-outline btn-dark" for="textIconAlt">
                            <input type="radio" id="textIconAlt" autocomplete="off" hidden="hidden"  name="menuTxtIcon" value="site-menubar-fold-alt"> 图标
                        </label>
                        </div>-->
                    </div>
                    <span class="help-block">仅在可视区域宽度大于768px生效</span>
                </div>
            </div>
            <hr>
            <div class="form-group">
                <label class="col-sm-2 control-label">主题颜色：</label>
                <div class="col-sm-10" id="skintoolsPrimary">
                    <ul class="list-unstyled list-inline color-radio">
                        <li>
                            <div class="radio-custom radio-primary">
                                <input type="radio" name="themeColor" value=""> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-brown">
                                <input type="radio"  name="themeColor" value="brown"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-cyan">
                                <input type="radio"  name="themeColor" value="cyan"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-green">
                                <input type="radio"  name="themeColor" value="green"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-grey">
                                <input type="radio"  name="themeColor" value="grey"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-indigo">
                                <input type="radio"  name="themeColor" value="indigo"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-orange">
                                <input type="radio"  name="themeColor" value="orange"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-pink">
                                <input type="radio"  name="themeColor" value="pink"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-purple">
                                <input type="radio"  name="themeColor" value="purple"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-red">
                                <input type="radio"  name="themeColor" value="red"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-teal">
                                <input type="radio"  name="themeColor" value="teal"> <label></label>
                            </div>
                        </li>
                        <li>
                            <div class="radio-custom radio-yellow">
                                <input type="radio"  name="themeColor" value="yellow"> <label></label>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <hr>
            <div class="form-group">
                <label class="col-sm-2 control-label">Tab 页签：</label>
                <div class="col-sm-10">
                    <div class="radio-inline radio-custom radio-primary">
                        <input type="radio" id="tabDisplayShow" name="tabFlag" value="open">
                        <label for="tabDisplayShow">开启</label>
                    </div>
                    <div class="radio-inline radio-custom radio-primary">
                        <input type="radio" id="tabDisplayHide"  name="tabFlag" value="close">
                        <label for="tabDisplayHide">关闭</label>
                    </div>
                    <!--<span class="help-block">Tab 页签必须保存以后才能看到效果</span>-->
                </div>
            </div>
            <hr>
            <div class="form-group">
                <div class="col-sm-10 col-sm-offset-2 margin-top-20">
                    <button type="button" class="btn btn-primary" id="save" name="save" value="true">保存</button>
                    <button type="button" class="btn btn-outline btn-default" name="reset" value="reset" id="skintoolsReset">恢复默认</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="/common/newIndexFrame/vendor/switchery/switchery.min.js"></script>
<script src="/common/newIndexFrame/vendor/bootstrap-select/bootstrap-select.min.js" data-name="select"></script>
<!-- 设置主题颜色 -->
<!--<script src="/common/newIndexFrame/js/system/settings/display.js" data-deps="select"></script>-->
	
	
    </div>
    <div class="page-loading vertical-align text-center">
        <div class="page-loader loader-default loader vertical-align-middle" data-type="default"></div>
    </div>


<!-- 布局 -->
<script src="/common/newIndexFrame/themes/classic/base/js/sections/menu.js"></script>
<script src="/common/newIndexFrame/themes/classic/base/js/sections/media-menu.js"></script>

<!-- 插件 -->
<script src="/common/newIndexFrame/vendor/jquery-pjax/jquery.pjax.js"></script>
<script src="/common/newIndexFrame/themes/classic/global/js/plugins/responsive-tabs.js"></script>
<script src="/common/newIndexFrame/vendor/ashoverscroll/jquery-asHoverScroll.min.js"></script>
<script src="/common/newIndexFrame/vendor/slimscroll/jquery.slimscroll.min.js"></script>

<script>
	$(function(){
		//改变导航栏颜色-----------begin------------------------------------------------------
		$("input[name='navigationColor']").click(function(){
			if(parent.$(".navbar-fixed-top").attr("class").indexOf("navbar-inverse")>-1){
				parent.$(".navbar-fixed-top").attr("class","site-navbar navbar navbar-default navbar-fixed-top navbar-inverse "+$(this).val());
			}else{
				parent.$(".navbar-fixed-top").attr("class","site-navbar navbar navbar-default navbar-fixed-top "+$(this).val());
			}
		});
		//导航条读取记录
		var navigationBarColour = localStorage.getItem("navigationBarColour");//导航栏蓝色
		if(navigationBarColour!=null&&navigationBarColour!=""){
			$("input[name='navigationColor']").each(function(){
				if($(this).val()==navigationBarColour){
					$(this).attr("checked",true);
				}
			});
		}else{
			var themeColor = localStorage.getItem("themeColor");//菜单显示读取记录
			$("input[name='navigationColor']").each(function(){
				if($(this).val().indexOf(themeColor)>-1){
					$(this).attr("checked",true);
				}
			});
		}
		//改变导航栏颜色-----------end------------------------------------------------------

		//是否通栏显示-------------begin------------------------------------------------------------
		$("#navbarDisplay").click(function(){
			if($(this).is(":checked")){
				parent.$(".navbar-fixed-top").addClass("navbar-inverse");
			}else{
				localStorage.setItem("navbarDisplay",false);//保存通栏显示
				parent.$(".navbar-fixed-top").removeClass("navbar-inverse");
			}
		});
		var navbarDisplay = localStorage.getItem("navbarDisplay");//通栏显示读取记录
		if(navbarDisplay!=null&&navbarDisplay!=""){
			if(navbarDisplay=="true")
				$("#navbarDisplay").attr("checked",true);
			else
				$("#navbarDisplay").attr("checked",false);
		}else{
			$("#navbarDisplay").attr("checked",true);//默认情况下显示通栏
		}
		//是否通栏显示---------end----------------------------------------------------------------------------
		
		//菜单主题------------begin---------------------------------------------------------------
		$("#skintoolsSidebar").change(function(){
			if(parent.$(".site-menubar-light").length>0&&$(this).val()=="site-menubar-dark"){
				parent.$(".site-menubar-light").attr("class","site-menubar site-menubar-dark");
			}else{
				parent.$(".site-menubar-dark").attr("class","site-menubar site-menubar-light");
			}
		});
		var skintoolsSidebar = localStorage.getItem("skintoolsSidebar");
		if(skintoolsSidebar=="site-menubar-light"){
			$("#skintoolsSidebar").val("site-menubar-light");
		}		
		//菜单主题------------end---------------------------------------------------------------
		
		//菜单显示------------begin-----------------------------------------------------------------
		$("input[name='menuDisplay']").change(function(){
			if($("input[name='menuDisplay']:checked").val()=="site-menubar-fold"){
				$("#menuFoldSetting").attr("class","margin-top-10");//收起
				parent.$("#admui-toggleMenubar").click();
				parent.$(".rightcontent").css("left","90px");
			}else{
				$("#menuFoldSetting").attr("class","margin-top-10 hidden");//展开
				parent.$("#admui-toggleMenubar").click();
				parent.$(".rightcontent").css("left","220px");
			}
		});
		var menuDisplay = localStorage.getItem("menuDisplay");//菜单显示读取记录
		if(menuDisplay=="close"){
			$("input[name='menuDisplay']:last").attr("checked",true);			
		}else{
			$("input[name='menuDisplay']:first").attr("checked",true);
		}
		//菜单显示------------end-----------------------------------------------------------------
		
		//主题颜色--------------begin--------------------------------------------------------------
		$("input[name='themeColor']").click(function(){
			if(this.value!=null&&this.value!=""){
				var color = this.value;
				$("input[name='navigationColor']").each(function(i){
					if(this.value.indexOf(color)>-1){
						$("input[name='navigationColor']").get(i).checked=true; 
						if(parent.$(".navbar-fixed-top").attr("class").indexOf("navbar-inverse")>-1){
							parent.$(".navbar-fixed-top").attr("class","site-navbar navbar navbar-default navbar-fixed-top navbar-inverse "+$(this).val());
						}else{
							parent.$(".navbar-fixed-top").attr("class","site-navbar navbar navbar-default navbar-fixed-top "+$(this).val());
						}
					}
				});
				$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/"+this.value+".css");
				parent.$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/"+this.value+".css");	
			}else{
				$("input[name='navigationColor']").get(0).checked=true; 
				$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/site.css");
				parent.$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/site.css");	
				if(parent.$(".navbar-fixed-top").attr("class").indexOf("navbar-inverse")>-1){
					parent.$(".navbar-fixed-top").attr("class","site-navbar navbar navbar-default navbar-fixed-top navbar-inverse ");
				}else{
					parent.$(".navbar-fixed-top").attr("class","site-navbar navbar navbar-default navbar-fixed-top ");
				}
			}
		});
		var themeColor = localStorage.getItem("themeColor");
		if(themeColor!=null&&themeColor!=""){
			$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/"+themeColor+".css");
			$("input[name='themeColor']").each(function(){
				if($(this).val()==themeColor){
					$(this).attr("checked",true);
				}
			});
		}else{
			$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/site.css");
			$("input[name='themeColor']:first").attr("checked",true);
		}
		//主题颜色--------------end--------------------------------------------------------------
		
		//Tab页签---------------begin-------------------------------------------
		$("input[name='tabFlag']").change(function(){
			if($(this).val()=="open"){
				parent.$(".content-tabs").show();	
				parent.$(".J_mainContent").css("top","36px");	
			}else{
				parent.$(".content-tabs").hide();	
				parent.$(".J_mainContent").css("top","0px");	
			}
		});
		var tabFlag = localStorage.getItem("tabFlag");
		if(tabFlag=="close"){
			$("input[name='tabFlag']:last").attr("checked",true);	
		}else{
			$("input[name='tabFlag']:first").attr("checked",true);	
		}
		//Tab页签---------------end---------------------------------------------
		
		//----------------------------------------------------------------------------------------------------------------
		//保存
		$("#save").click(function(){
			localStorage.setItem("navigationBarColour", $("input[name='navigationColor']:checked").val());//保存导航栏颜色
			if($("#navbarDisplay").is(":checked"))
				localStorage.setItem("navbarDisplay",true);//保存通栏显示
			else
				localStorage.setItem("navbarDisplay",false);//保存通栏显示
			//菜单主题
			var skintoolsSidebar = $("#skintoolsSidebar").val();
			if(skintoolsSidebar=="site-menubar-light"){
				localStorage.setItem("skintoolsSidebar","site-menubar-light");
			}else{
				localStorage.setItem("skintoolsSidebar","");
			}
			//菜单显示
			if($("input[name='menuDisplay']:checked").val()=="site-menubar-fold"){
				localStorage.setItem("menuDisplay","close");
			}else{
				localStorage.setItem("menuDisplay","open");
			}
			//主题颜色
			localStorage.setItem("themeColor", $("input[name='themeColor']:checked").val());
			//tab页签
			localStorage.setItem("tabFlag", $("input[name='tabFlag']:checked").val());
		});
		
		//恢复默认
		$("#skintoolsReset").click(function(){
			localStorage.clear();//清空浏览器记录
			//导航条 通栏
			$("input[name='navigationColor']").get(0).checked=true; 
			$("#navbarDisplay").get(0).checked=true; 
			parent.$(".navbar-fixed-top").attr("class","site-navbar navbar navbar-default navbar-fixed-top navbar-inverse");
			//菜单主题
			if(parent.$(".site-menubar-light").length>0&&$("#skintoolsSidebar").val()=="site-menubar-light"){
				parent.$(".site-menubar-light").attr("class","site-menubar site-menubar-dark");
			}
			$("#skintoolsSidebar").prev().prev().attr("title","深色主题");
			$("#skintoolsSidebar").prev().prev().children().eq(0).text("深色主题");
			$("#skintoolsSidebar").prev().children().children().each(function(){
				if($(this).text()=="深色主题"){
					$(this).attr("class","selected");
				}
				if($(this).text()=="浅色主题"){
					$(this).attr("class","");
				}
			});
			//菜单显示
			if($("input[name='menuDisplay']:checked").val()=="site-menubar-fold"){
				$("input[name='menuDisplay']").get(0).checked=true; 
				$("#menuFoldSetting").attr("class","margin-top-10 hidden");//展开
				parent.$("#admui-toggleMenubar").click();
				parent.$(".rightcontent").css("left","220px");
			}
			//主题颜色
			$("input[name='themeColor']").get(0).checked=true; 
			$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/site.css");
			parent.$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/site.css");	
			//Tab页签
			parent.$(".content-tabs").show();	
			parent.$(".J_mainContent").css("top","36px");	
		});
	});
</script>

</body>
</html>

