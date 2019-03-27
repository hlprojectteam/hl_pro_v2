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
						<span>总建筑数</span><span><i id="buildingCount">0</i><b>栋</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/jcfx.png" alt="" />
					</dt>
					<dd>
						<span>总户数</span><span><i id="residentCount">0</i><b>户</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/sjfx.png" alt="" />
					</dt>
					<dd>
						<span>总人口数</span><span><i id="populationCount">0</i><b>位</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/qzjb.png" alt="" />
					</dt>
					<dd>
						<span>服务人次</span><span><i>0</i><b>次</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/zd.png" alt="" />
					</dt>
					<dd>
						<span>重点关爱人群</span><span><i id="serviceCount">0</i><b>位</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/dys.png" alt="" />
					</dt>
					<dd>
						<span>辖区重点监控区</span><span><i>0</i><b>个</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/dfc.png" alt="" />
					</dt>
					<dd>
						<span>辖区重点人群</span><span><i id="stabilityCount">0</i><b>位</b></span>
					</dd>
				</dl>
				<dl>
					<dt>
						<img src="/common/indexFrame/images/dg.png" alt="" />
					</dt>
					<dd>
						<span>证件失效提醒</span><span><i>0</i><b>家</b></span>
					</dd>
				</dl>
			</div>
			<div class="En_first_right">
				<dl>
					<dt>
						<span>重大提醒</span><span><i>10</i><b>个</b></span>
					</dt>
					<dd>
						<img src="/common/indexFrame/images/zjgq.png" alt="" />
					</dd>

				</dl>
				<dl>
					<dt>
						<span>园区通知</span><span><i>10</i><b>个</b></span>
					</dt>
					<dd>
						<img src="/common/indexFrame/images/sxj.png" alt="" />
					</dd>
				</dl>
			</div>
		</div>
		<div class="En_second">
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

			<%-- 服务信息统计 柱状图  开始 --%>
			<div class="Statistics-first" id="chart_service"></div>
			<%-- 服务信息统计 柱状图  结束 --%>


			<%-- 稳控信息统计 玫瑰图  开始--%>
			<div class="Statistics-second" id="chart_stability"></div>
			<%-- 稳控信息统计 玫瑰图  结束--%>

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
		</div>
		<!--   <div class="En_four">   
                <dl>
                    <dt><img src="/common/indexFrame/images/bdb.png"   alt="" /></dt> 
                    <dd><span>总建筑数</span><span><i>10</i><b>栋</b></span></dd> 
                </dl> 
                <dl>
                    <dt><img src="/common/indexFrame/images/jcfx.png"   alt="" /></dt> 
                    <dd><span>总户数</span><span><i>10</i><b>户</b></span></dd>  
                </dl> 
                <dl>
                    <dt><img src="/common/indexFrame/images/sjfx.png"   alt="" /></dt> 
                    <dd><span>总人口数</span><span><i>10</i><b>位</b></span></dd> 
                </dl>
                 <dl>
                    <dt><img src="/common/indexFrame/images/qzjb.png"   alt="" /></dt> 
                    <dd><span>服务人次</span><span><i>10</i><b>次</b></span></dd> 
                </dl>
                <dl style="margin-right:0;">
                    <dt><img src="/common/indexFrame/images/zd.png"   alt="" /></dt> 
                    <dd><span>重点关爱人群</span><span><i>10</i><b>位</b></span></dd> 
                </dl> 
                <dl>
                    <dt><img src="/common/indexFrame/images/dys.png"   alt="" /></dt> 
                    <dd><span>辖区重点监控区</span><span><i>2</i><b>个</b></span></dd>  
                </dl> 
                <dl>
                    <dt><img src="/common/indexFrame/images/dfc.png"   alt="" /></dt> 
                    <dd><span>辖区重点人群</span><span><i>120</i><b>位</b></span></dd> 
                </dl>
                 <dl>
                    <dt><img src="/common/indexFrame/images/dg.png"   alt="" /></dt> 
                    <dd><span>证件失效提醒</span><span><i>20</i><b>家</b></span></dd> 
                </dl>
                <dl>
                    <dt><img src="/common/indexFrame/images/dfc.png"   alt="" /></dt> 
                    <dd><span>辖区重点人群</span><span><i>120</i><b>位</b></span></dd> 
                </dl>
                 <dl style="margin-right:0;">
                    <dt><img src="/common/indexFrame/images/dg.png"   alt="" /></dt> 
                    <dd><span>证件失效提醒</span><span><i>20</i><b>家</b></span></dd> 
                </dl>
            </div>   -->
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
			url : '/report/baseLibReport/indexFrame',
			success : function(data) {
				indexData = data[0];
				// 数字增长显示
				countUp(document.getElementById("buildingCount"),
						indexData.buildingCount, 0, 1000, 0);
				countUp(document.getElementById("residentCount"),
						indexData.residentCount, 0, 1000, 0);
				countUp(document.getElementById("populationCount"),
						indexData.populationCount, 0, 1000, 0);
			},
		});
	}
</script>
<script type="text/javascript">
	// 基于准备好的div容器，初始化echarts实例
	var myChart1 = echarts.init(document.getElementById('chart_service'));

	option = {
			grid : {
				top : '15%'
			},
			title : {
				text : '重点关爱人群数据统计',
				left : 'center',
				textStyle : {
					color : '#666',
					fontSize : '14',
				},
			},
			xAxis : {
				data : [ '低五保', '残疾人', '空巢老人', '帮扶救助', '医保社保', '医疗救助', '兵役状况',
						'教育信息' ],
				axisLine : {
					lineStyle : {
						color : '#4397e6'
					}
				},
				axisLabel : {
					color : '#4397e6',
					fontSize : 12,
					rotate : '0',
					formatter : function(value) {
						return value.split("").join("\n");
					}
				}
			},
			yAxis : {
				name : "(人)",
				nameTextStyle : {
					color : '#4397e6',
					fontSize : 12
				},
				axisLine : {
					lineStyle : {
						color : '#4397e6'
					}
				},
				axisLabel : {
					color : '#4397e6',
					fontSize : 12
				},
				splitLine : {
					show : false,
					lineStyle : {
						color : '#4397e6'
					}
				}
			},
			series : [ {
				type : 'bar',
				barWidth : 10,
				itemStyle : {
					normal : {
						color : new echarts.graphic.LinearGradient(0, 0, 0, 1, [ {
							offset : 0.5,
							color : '#00b0ff'
						}, {
							offset : 1,
							color : '#7052f4'
						} ], false),
						label: {		    // 柱条上标签
		                    show: true,         // 是否将数据项对应的数值显示在柱条上
		            		textStyle: {        // 柱条上标签字体样式
		            			color: '#4397e6',      // 柱条上标签字体颜色
		            			fontSize: 10        // 柱条上标签字体大小
		            		},
		                    position: 'top',    // 标签位置,'top'(柱条顶部),'center'(柱条内部),'bottom'(柱条底部)
		                    formatter: '{c}'    // 标签内容
		                }
					}
				},
				data : []
			} ]
		};
	
	// 使用刚指定的配置项和数据(加载)显示图表。
	myChart1.setOption(option);		
	
	// 数据加载完之前先显示一段简单的loading动画
	myChart1.showLoading();	
	
	$.ajax({
		type : "post",
		async : true, 		//异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
		url : "/report/baseLibReport/getServiceCount",
		dataType : "json",
		success : function(data) {
			if (data) {
				myChart1.hideLoading(); //隐藏加载动画
				myChart1.setOption({ 	//加载数据图表
					series : [ {
						data : [ data.serviceAl, data.serviceDb,
								data.serviceEd, data.serviceEm,
								data.serviceHp, data.serviceIns,
								data.serviceMa, data.serviceMs]
					} ]
				});
			}

		},
		error : function(errorMsg) {
			//请求失败时执行该函数
	        console.log("图表请求数据失败!");
			myChart1.hideLoading();
		}
	});
</script>
<script type="text/javascript">
	// 基于准备好的div容器，初始化echarts实例
	var myChart2 = echarts.init(document.getElementById('chart_stability'));
	
	option = {
		backgroundColor : '#fff',
		calculable : true,
		tooltip : {
			trigger : "item",
			formatter : "{c}人"
		},
		title : {
			text : '辖区重点人群数据统计',
			left : 'center',
			textStyle : {
				color : '#666',
				fontSize : '14',
			},
		},
		calculable : true,
		/* legend : {
			icon : "circle",
			x : "10%",
			y : "70%",   //文字高度层次
			data : [ "社区矫正人员", "邪教人员", "吸毒人员", "危险精神病人", "重点青少年人员", "刑释解救人员"],
			textStyle : {
				color : "#666"
			}
		}, */
		series : [ {
			name : "",
			type : "pie",
			radius : [ 10, /*中心空白圆直径*/
			55 /*外边圆直径*/
			],
			avoidLabelOverlap : false,
			startAngle : 180, /*扇形360度所处位置*/
			center : [ "50%", /*数据图形横向位置*/
			"60%" /*数据图形纵向位置*/
			],
			roseType : "area",
			selectedMode : "single",
			label : {
				normal : {
					show : true,
					formatter : "{c}人"
				},
				emphasis : {
					show : true
				}
			},
			labelLine : {
				normal : {
					show : true,
					smooth : false,
					length : 4,
					length2 : 5
				},
				emphasis : {
					show : true
				}
			},
			data : [ {
				/* value : 3, */
				name : "社区矫正人员",
				itemStyle : {
					normal : {
						color : "#f845f1"
					}
				}
			}, {
				/* value : 1, */
				name : "邪教人员",
				itemStyle : {
					normal : {
						color : "#ad46f3"
					}
				}
			}, {
				/* value : 8, */
				name : "吸毒人员",
				itemStyle : {
					normal : {
						color : "#5045f6"
					}
				}
			}, {
				/* value : 2, */
				name : "危险精神病人",
				itemStyle : {
					normal : {
						color : "#4777f5"
					}
				}
			}, {
				/* value : 12, */
				name : "重点青少年人员",
				itemStyle : {
					normal : {
						color : "#44aff0"
					}
				}
			}, {
				/* value : 7, */
				name : "刑释解救人员",
				itemStyle : {
					normal : {
						color : "#45dbf7"
					}
				}
			}, {
				/* value : 0, */
				name : "",
				itemStyle : {
					normal : {
						label : {
							show : false
						},
						labelLine : {
							show : false
						}
					}
				}
			}, {
				/* value : 0, */
				name : "",
				itemStyle : {
					normal : {
						label : {
							show : false
						},
						labelLine : {
							show : false
						}
					}
				}
			}, {
				/* value : 0, */
				name : "",
				itemStyle : {
					normal : {
						label : {
							show : false
						},
						labelLine : {
							show : false
						}
					}
				}
			}, {
				/* value : 0, */
				name : "",
				itemStyle : {
					normal : {
						label : {
							show : false
						},
						labelLine : {
							show : false
						}
					}
				}
			}, {
				/* value : 0, */
				name : "",
				itemStyle : {
					normal : {
						label : {
							show : false
						},
						labelLine : {
							show : false
						}
					}
				}
			}, {
				/* value : 0, */
				name : "",
				itemStyle : {
					normal : {
						label : {
							show : false
						},
						labelLine : {
							show : false
						}
					}
				}
			} ]
		} ]
	};

	//使用刚指定的配置项和数据显示图表。
	myChart2.setOption(option);
	
	// 数据加载完之前先显示一段简单的loading动画
	myChart2.showLoading();	
	
	$.ajax({
		type : "post",
		async : true, 		//异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
		url : "/report/baseLibReport/getStabilityCount",
		dataType : "json",
		success : function(data) {
			if (data) {
				myChart2.hideLoading(); //隐藏加载动画
				myChart2.setOption({ 	//加载数据图表
					series : [ {
						data : [ data.stabilityCc, data.stabilityCu,data.stabilityDa, data.stabilityDp,data.stabilityKy, data.stabilityTr,
								0,0,0,0,0,0]
					} ]
				});
			}

		},
		error : function(errorMsg) {
			//请求失败时执行该函数
	        console.log("图表请求数据失败!");
			myChart2.hideLoading();
		}
	});
</script>
</html>
