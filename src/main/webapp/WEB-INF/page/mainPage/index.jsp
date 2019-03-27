<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" import="com.common.utils.Common" %>
<!DOCTYPE html>
<html class="no-js css-menubar" lang="zh-cn">
<head>
    <title>环龙营运通平台</title>
	<charset="utf-8">
	<meta name="keywords" content="cloudting,cloudting官网,云汀后台管理系统" />
	<meta name="description" content="云汀后台管理系统，云汀后台管理系统是一个基于最新Web技术的企业级通用管理系统快速开发框架，可以帮助企业极大的提高工作效率，节省开发成本，提升品牌形象。" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<!-- 移动设备 viewport -->
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui">
	<meta name="author" content="www.cloudting.cn">
	<!-- 360浏览器默认使用Webkit内核 -->
	<meta name="renderer" content="webkit">
	<!-- 禁止百度SiteAPP转码 -->
	<meta http-equiv="Cache-Control" content="no-siteapp">
	<meta name="mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-title" content="cloudting">
	<!-- 样式 -->
	<link rel="stylesheet" href="/common/newIndexFrame/css/themes/classic/skins/site.css" id="siteStyle">					

    <!-- 图标 CSS-->
    <!-- <link rel="stylesheet" href="/common/newIndexFrame/fonts/font-awesome/font-awesome.css"> -->
    <link href="/common/index/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
    
    <link rel="stylesheet" href="/common/newIndexFrame/fonts/web-icons/web-icons.css">
    <!-- 插件 CSS -->
    <link rel="stylesheet" href="/common/newIndexFrame/vendor/animsition/animsition.css">
    <link rel="stylesheet" href="/common/newIndexFrame/vendor/toastr/toastr.css">
    <link rel="stylesheet" href="/common/newIndexFrame/css/demo.css">
    
    <!-- 自定义 CSS -->
    <link rel="stylesheet" href="/common/newIndexFrame/themes/classic/base/css/index_defined.css">
    
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
    
    <link rel="stylesheet" href="/common/newIndexFrame/fonts/material-design/material-design.css">
	<link rel="stylesheet" href="/common/newIndexFrame/vendor/bootstrap-select/bootstrap-select.css">
	<link rel="stylesheet" href="/common/newIndexFrame/vendor/switchery/switchery.css">
    <!-- 即时通讯start 包含 layer弹框插件-->
	<script src="/common/plugins/layui/layui.js" charset="utf-8"></script>
	<script src="/common/js/alert_tip.js"></script>
    <style>
    	/* 头像样式 */
    	.user-info{max-width:100px;display:inline-block;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;text-align:left;vertical-align:top;line-height:15px;position:relative;top:6px}
    	.user-info small{display:block}
    </style>
    
    <script>
        Breakpoints();
    </script>
</head>

<body class="site-contabs-open site-menubar-unfold site-menubar-keep ">

<nav class="site-navbar navbar navbar-default navbar-fixed-top navbar-inverse " role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle hamburger hamburger-close navbar-toggle-left hided" data-toggle="menubar">
            <span class="sr-only">切换菜单</span> <span class="hamburger-bar"></span>
        </button>
        <button type="button" class="navbar-toggle collapsed" data-target="#admui-navbarCollapse" data-toggle="collapse">
            <i class="icon wb-more-horizontal" aria-hidden="true"></i>
        </button>
        <div class="navbar-brand navbar-brand-center site-gridmenu-toggle" data-toggle="gridmenu">
            <img class="navbar-brand-logo visible-lg visible-xs navbar-logo" src="/common/newIndexFrame/images/logo-white.png" title="环龙营运通平台">
            <img class="navbar-brand-logo hidden-xs hidden-lg navbar-logo-mini" src="/common/newIndexFrame/images/logo-white-min.svg" title="环龙营运通平台">
        </div>
    </div>
    <div class="navbar-container container-fluid">
        <div class="collapse navbar-collapse navbar-collapse-toolbar" id="admui-navbarCollapse">
            <ul class="nav navbar-toolbar navbar-left">
                <li class="hidden-float">
                    <a data-toggle="menubar" class="hidden-float" href="javascript:;" role="button" id="admui-toggleMenubar">
                        <i class="icon hamburger hamburger-arrow-left"> 
                      		<span class="sr-only">切换目录</span>
                            <span class="hamburger-bar"></span> 
                        </i>
                    </a>
                </li>
                <li class="navbar-menu nav-tabs-horizontal nav-tabs-animate is-load" id="admui-navMenu">
                    <ul class="nav navbar-toolbar nav-tabs" role="tablist">
                        <!-- 顶部菜单 一级菜单 -->
                    	<c:forEach items="${firstList}" var="first" varStatus="status">
                    		<li role="presentation" class="active">
                            <a data-toggle="tab" href="#admui-navTabsItem-${status.index+1 }" aria-controls="admui-navTabsItem-${status.index+1 }" role="tab" aria-expanded="false">
                                <i class="fa fa-${first.icon }"></i> <span>${first.menuName}</span>
                            </a>
                        	</li>
                    	</c:forEach>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-toolbar navbar-right navbar-toolbar-right" title="最新消息">
                <li class="dropdown" id="admui-navbarMessage">
                    <a data-toggle="dropdown" href="javascript:;" class="msg-btn" aria-expanded="false" data-animation="scale-up" role="button">
                        <i class="icon wb-bell" aria-hidden="true"></i> <span class="badge badge-danger up msg-num"></span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-right dropdown-menu-media" role="menu">
                        <li class="dropdown-menu-header" role="presentation">
                            <h5>最新消息</h5>
                            <span class="label label-round label-danger"></span>
                        </li>
                        <li class="list-group" role="presentation">
	                        <div id="admui-messageContent" data-height="270px" data-plugin="slimScroll">
	                            <p class="text-center height-150 vertical-align">
	                                <small class="vertical-align-middle opacity-four">没有新消息</small>
	                            </p>
	                            <script type="text/html" id="admui-messageTpl">
                                        
                                 </script>
	                      	</div>
                        </li>
                        <li class="dropdown-menu-footer" role="presentation">
                            <a href="/system/account/message" target="_blank" data-pjax>
                            	<i class="icon fa-navicon"></i> 所有消息
                            </a>
                        </li>
                    </ul>
                </li>
                <li class="hidden-xs" id="admui-navbarDisplay" data-toggle="tooltip" data-placement="bottom" title="设置主题与布局等">
                    <a class="J_menuItem icon wb-layout" href="/index_display" data-index="2">	
                        <span class="sr-only">主题与布局</span>
                    </a>
                </li>
                <li class="hidden-xs" id="admui-navbarFullscreen" data-toggle="tooltip" data-placement="bottom" title="全屏">
                    <a class="icon icon-fullscreen" data-toggle="fullscreen" href="#" role="button">
                        <span class="sr-only">全屏</span></a>
                </li>
				<li>
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" style="height: 60px;padding-top: 10px;">
						<c:if test="${!empty sessionScope.user.avatarPathUpload}">
							<img class="img-circle" src="${sessionScope.user.avatarPathUpload}" alt="user Photo" style="margin:0px 8px 0 0;border-radius:100%;border:2px solid #FFF;max-width:40px"/>
						</c:if>
						<c:if test="${empty sessionScope.user.avatarPathUpload}">
							<img class="img-circle" src="/common/newIndexFrame/images/user.png" alt="user Photo"/>
						</c:if>
						<span class="user-info">
							<small>欢迎光临</small>
							<small>${sessionScope.user.userName }</small>
						</span>
						<i class="fa fa-caret-down"></i>
					</a>
					<ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
						<li>
							<a href="javascript:void(0);" onclick="on_avatar()"><i class="icon-cog"></i>修改头像</a>
						</li>
						<li>
							<a href="javascript:void(0);" onclick="on_edit('${sessionScope.user.id }')"><i class="icon-user"></i>个人资料</a>
						</li>
						<!-- <li>
							<a href="/outside/index" title="公众网"><i class="icon-off"></i>公众网</a>
						</li>
						<li>
							<a href="/indexMain" title="返回主页"><i class="icon-off"></i>返回主页</a>
						</li> -->
					</ul>
				</li>	
				<li title="退出">
                    <a class="icon fa fa-sign-out" id="admui-signOut" data-ctx="" data-user="783" href="/loginOut" role="button">
                    	<span class="sr-only">退出</span></a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<nav class="site-menubar site-menubar-dark">
    <div class="site-menubar-body">
        <div class="tab-content height-full" id="admui-navTabs">
			<c:forEach items="${firstList}" var="first" varStatus="status_first">    
			    <c:if test="${ status_first.index==0}">
			    	<div class="tab-pane animation-fade height-full active" id="admui-navTabsItem-${ status_first.index+1}" role="tabpanel">
			    </c:if>
			    <c:if test="${ status_first.index!=0}">
			    	<div class="tab-pane animation-fade height-full " id="admui-navTabsItem-${ status_first.index+1}" role="tabpanel">
			    </c:if>
                <div>
                    <ul class="site-menu">
                    	<li class="site-menu-category"><i class="fa fa-${first.icon }"></i> <strong>${first.menuName}</strong></li>
                    	<c:forEach items="${secondList}" var="second" varStatus="status_second">
                    		<c:if test="${first.id == second.pId }">
                    			<%-- 二级菜单 --%>
                    			<c:if test="${second.isLeaf == 1 }">
                    				<li class="site-menu-item ">
                    					<c:if test="${fn:indexOf(second.url,'?')>0}">
                    						<a class="J_menuItem" data-pjax href="${second.url}&menuCode=${second.menuCode}" data-index="${ status_first.index+1}${ status_second.index+1}">
                    					</c:if>
                    					<c:if test="${fn:indexOf(second.url,'?')<0}">
                    						<a class="J_menuItem" data-pjax href="${second.url}?menuCode=${second.menuCode}" data-index="${ status_first.index+1}${ status_second.index+1}">
                    					</c:if>
			                            	<i class="site-menu-icon fa fa-${second.icon}" aria-hidden="true"></i><span class="site-menu-title">${second.menuName}</span>
			                            </a>
			                        </li>
                    			</c:if>
                    			<c:if test="${second.isLeaf == 0 }">
                    				<li class="site-menu-item has-sub ">
			                            <a href="javascript:;"><i class="site-menu-icon fa fa-${second.icon}" aria-hidden="true"></i><span class="site-menu-title">${second.menuName}</span><span class="site-menu-arrow"></span></a>
			                            <ul class="site-menu-sub">
			                            	<c:forEach items="${thirdList}" var="third" varStatus="status_third">
			                            		<c:if test="${second.id == third.pId }">
													<%--三级菜单--%>
													<c:if test="${third.isLeaf == 1 }">
														<li class="site-menu-item ">
															<c:if test="${fn:indexOf(third.url,'?')>0}">
																<a class="J_menuItem" data-pjax href="${third.url}&menuCode=${third.menuCode}" data-index="${ status_first.index+1}${ status_second.index+1}${ status_third.index+1}">
															</c:if>
															<c:if test="${fn:indexOf(third.url,'?')<0}">
																<a class="J_menuItem" data-pjax href="${third.url}?menuCode=${third.menuCode}" data-index="${ status_first.index+1}${ status_second.index+1}${ status_third.index+1}">
															</c:if>
																<i class="site-menu-icon fa fa-${third.icon}" aria-hidden="true"></i><span class="site-menu-title">${third.menuName}</span>
															</a>
														</li>
													</c:if>
													<c:if test="${third.isLeaf == 0 }">
														<li class="site-menu-item has-sub ">
															<a href="javascript:;"><i class="site-menu-icon fa fa-${third.icon}" aria-hidden="true"></i><span class="site-menu-title">${third.menuName}</span><span class="site-menu-arrow"></span></a>
															<ul class="site-menu-sub">
																<c:forEach items="${fourList}" var="four" varStatus="status_four">
																	<c:if test="${third.id == four.pId }">
																		<%--四级菜单--%>
																		<li class="site-menu-item ">
																			<c:if test="${fn:indexOf(four.url,'?')>0}">
																				<a class="J_menuItem" data-pjax href="${four.url}&menuCode=${four.menuCode}" data-index="${ status_first.index+1}${ status_second.index+1}${ status_third.index+1}${ status_four.index+1}">
																			</c:if>
																			<c:if test="${fn:indexOf(four.url,'?')<0}">
																				<a class="J_menuItem" data-pjax href="${four.url}?menuCode=${four.menuCode}" data-index="${ status_first.index+1}${ status_second.index+1}${ status_third.index+1}${ status_four.index+1}">
																			</c:if>
																			<i class="site-menu-icon fa fa-${four.icon}" aria-hidden="true"></i><span class="site-menu-title">${four.menuName}</span>
																			</a>
																		</li>
																	</c:if>
																</c:forEach>
															</ul>
														</li>
													</c:if>
			                            		</c:if>
			                            	</c:forEach>
			                            </ul>
			                        </li>
                    			</c:if>
                    		</c:if>
                    	</c:forEach>
                    </ul>
                </div>
            </div>
        	</c:forEach>
        </div>
    </div>
</nav>
	<link href="/common/plugins/jtab/jtab.css" rel="stylesheet">
	<script src="/common/plugins/jtab/jtab.min.js" type="text/javascript"></script>
	<div class="rightcontent">
        <div class="content-tabs">
            <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i></button>
            <div class="page-tabs J_menuTabs">
                <div class="page-tabs-content" style="margin-left: 0px;">
                    <a href="javascript:;" class="J_menuTab active" data-id="indexFrame">首页</a>
               </div>
            </div>
            <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i></button>
        </div>
        <div class="J_mainContent">
        	<iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="/indexFrame" frameborder="0" data-id="indexFrame" seamless></iframe>
        </div>
        <div class="edit_footer">
            <div align="right" style="padding-right: 20px;">技术支持 &copy; <a href="http://www.cloudting.cn/" target="_blank">云汀信息</a></div>
        </div>
	</div>
<script>
	<%-- 标题 --%>
	var index_title = "环龙营运通平台";
</script>	
<!-- 布局 -->
<script src="/common/newIndexFrame/themes/classic/base/js/sections/menu.js"></script>
<script src="/common/newIndexFrame/themes/classic/base/js/sections/media-menu.js"></script>
<script src="/common/newIndexFrame/themes/classic/base/js/sections/content-tabs.js"></script>

<!-- 插件 -->
<script src="/common/newIndexFrame/vendor/jquery-pjax/jquery.pjax.js"></script>
<script src="/common/newIndexFrame/themes/classic/global/js/plugins/responsive-tabs.js"></script>
<script src="/common/newIndexFrame/vendor/ashoverscroll/jquery-asHoverScroll.min.js"></script>
<script src="/common/newIndexFrame/vendor/slimscroll/jquery.slimscroll.min.js"></script>
<script src="/common/newIndexFrame/vendor/screenfull/screenfull.min.js"></script>

<!-- 自定义js -->
<script src="/common/newIndexFrame/js/indexFrame.js"></script>
<jsp:include page="/indexMain_webSocket"></jsp:include>
<script>
//编辑用户
function on_edit(id){
	var url = "";
	if(${sessionScope.user.type}==1)
		url = "/urms/subsystemSuperAdmin_edit?id="+id;//超管及子系统管理员
	else if(${sessionScope.user.type}==2)
		url = "/urms/subsystemAdmin_edit?id="+id;//子系统管理员
	else
		url = "/urms/user_edit_user?id="+id;//普通用户进入新的用户
	layer.open({
        type: 2,
        title: "编辑用户",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["95%", "95%"],
        content: url
    });
}
//修改头像 拍照
function on_avatar(){
	layer.open({
        type: 2,
        title: "修改头像",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["45%", "85%"],
        content: "/urms/user_avatar"
    });
}
</script>
</body>
</html>