<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>环龙营运通平台-主页</title>
<meta name="keywords"
	content="云汀后台管理系统,后台bootstrap框架,会员中心主题,后台HTML,响应式后台">
<meta name="description"
	content="云汀后台管理系统是一个完全响应式，基于Bootstrap3最新版本开发的扁平化主题，她采用了主流的左右两栏式布局，使用了Html5+CSS3等现代技术">
<link href="/common/indexFrame/css/base.css" rel="stylesheet" />
<link href="/common/indexFrame/css/index_Enterprise.css"
	rel="stylesheet">
<!-- 数字增长显示 -->
<script type="text/javascript" src="/common/js/utils.js"></script>

<script src="/common/plugins/Echarts/echarts4.0.js"></script>
</head>
<body>
	<div class="ibox-content">
		<div class="En_first">
			<div class="En_first_left">
				<dl>
					<dt>
						<img src="/common/indexFrame/images/bdb.png" alt="" />
					</dt>
					<dd>
						<span>我的上报</span><span><i id="reportCount">0</i><b>件</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/jcfx.png" alt="" />
					</dt>
					<dd>
						<span>我的待办</span><span><i id="agendaCount">0</i><b>件</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/sjfx.png" alt="" />
					</dt>
					<dd>
						<span>我的经办</span><span><i id="handleCount">0</i><b>件</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/qzjb.png" alt="" />
					</dt>
					<dd>
						<span>我的办结</span><span><i id="finishCount">0</i><b>件</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/zd.png" alt="" />
					</dt>
					<dd>
						<span>营运动态</span><span><i id="serviceCount">  </i><b> </b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/dys.png" alt="" />
					</dt>
					<dd>
						<span>考试通知</span><span><i>    </i><b> </b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/dfc.png" alt="" />
					</dt>
					<dd>
						<span>    </span><span><i id="stabilityCount">    </i><b> </b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/dg.png" alt="" />
					</dt>
					<dd>
						<span>    </span><span><i>    </i><b> </b></span>
					</dd>
				</dl>
			</div>
			<div class="En_first_right">
				<dl>
					<dt>
						<span>重大隐患</span><span><i id="majorAgendaCount">0</i><b>件</b></span>
					</dt>
					<dd>
						<img src="/common/indexFrame/images/zjgq.png" alt="" />
					</dd>

				</dl>
				<dl>
					<dt>
						<span>制度通报</span><span><i>0</i><b>件</b></span>
					</dt>
					<dd>
						<img src="/common/indexFrame/images/sxj.png" alt="" />
					</dd>
				</dl>
			</div>
		</div>
		<%-- <div class="En_second">
			<div class="En_second_left">
				<dl>
					<dt>
						<img src="/common/indexFrame/images/dt.png" alt="" />
					</dt>
					<dd>
						<span>今日待处理事件</span><span><i>10</i><b>件</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/bz.png" alt="" />
					</dt>
					<dd>
						<span>逾期未处理事件</span><span><i>10</i><b>件</b></span>
					</dd>
				</dl>
			</div>

			服务信息统计 柱状图  开始
			<div class="Statistics-first" id="chart_service"></div>
			服务信息统计 柱状图  结束


			稳控信息统计 玫瑰图  开始
			<div class="Statistics-second" id="chart_stability"></div>
			稳控信息统计 玫瑰图  结束

		</div>
		<div class="En_thrid">
			<h1>最新群众上报需求</h1>
			<table>
				<tr>
					<th>上传用户</th>
					<th>上报时间</th>
					<th>需求类别</th>
					<th>紧急程度</th>
					<th>操作</th>
				</tr>
				<tr>
					<td>李昊</td>
					<td>2018/03/30</td>
					<td>求职</td>
					<td>一般</td>
					<td>查看</td>
				</tr>
				<tr>
					<td>秦辉</td>
					<td>2018/03/28</td>
					<td>帮扶</td>
					<td>紧急</td>
					<td>查看</td>
				</tr>
				<tr>
					<td>张丽影</td>
					<td>2018/03/27</td>
					<td>求职</td>
					<td>特急</td>
					<td>查看</td>
				</tr>
			</table>
		</div> --%>
	</div>
</body>
<script src="/common/index/js/jquery-2.1.1.min.js"></script>
<script type='text/javascript'>
	$(document).ready(function() {
		pageInit(); // 页面初始化
	});

	var indexData = null;

	function pageInit() {
		$.ajax({
			type : 'post',
			async : true,
			dataType : 'json',
			url : '/safecheck/hiddenDanger/queryEventCount',
			success : function(data) {
				//数字增长显示
				countUp(document.getElementById("reportCount"),
						data.reportCount, 0, 1000, 0);
				countUp(document.getElementById("agendaCount"),
						data.agendaCount, 0, 1000, 0);
				countUp(document.getElementById("handleCount"),
						data.handleCount, 0, 1000, 0);
				countUp(document.getElementById("finishCount"),
						data.finishCount, 0, 1000, 0);
				
				countUp(document.getElementById("majorAgendaCount"),
                        data.majorAgendaCount, 0, 1000, 0);
			},
		});
	}
</script>
</html>
