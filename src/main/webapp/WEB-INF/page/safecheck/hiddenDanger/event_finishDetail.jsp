<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fns" uri="/WEB-INF/taglib/dict.tld"%>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <title>办结明细</title>
    <link rel="stylesheet" href="/common/event/my/css/event_details.css" />
    <link href="/common/index/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link rel="stylesheet" href="/common/event/font/iconfont.css" />
    <link rel="stylesheet" href="/common/event/my/css/base.css" />
    <!-- 图片放大插件样式 -->
    <link rel="stylesheet" href="/common/gis/event/css/viewer.min.css">
    <script src="/common/index/js/jquery-2.1.1.min.js"></script>
    <script src="/common/event/my/js/event_details.js"></script>
    <script type="text/javascript" src="/common/event/my/js/divscroll.js"></script>
    <script src="/common/plugins/layui/layui.js" charset="utf-8"></script>
    <script src="/common/js/base.js"></script>
    <script src="/common/js/form2Json.js"></script>
    <script src="/common/js/alert_tip.js"></script>
    <script src="/common/js/utils.js"></script>

</head>
<body>
<div class="align">
    <!--头部开始-->
    <header class="header">
        <div class="row">
            <div class="incident">
                <div class="particulars s_one">
                    <i class="icon iconfont icon-wenjian1 big-font"></i>
                    <span>隐患详情</span>
                </div>
                <div class="particulars s_two">
                    <i class="icon iconfont icon-2_2event_process_management big-font"></i>
                    <span>流程图</span>
                </div>
                <div class="line"></div>
            </div>
            <div class="font-color-gray">
                <div>
                    <i class="icon iconfont icon-Fill"></i>
                    <span>总用时间:</span>
                    <span id="tottleTime"></span>
                </div>
                <div>
                    <i class="icon iconfont icon-bianhao"></i>
                    <span>隐患编号:</span>
                    <span>${eventInfoVo.eventCode}</span>
                </div>
            </div>
        </div>
    </header>
    <!--头部结束-->

    <!--主体开始-->

    <!--隐患详情页面开始-->
    <div class="main">
        <div class="main-center">
            <div class="left-side">
                <!--左边开始-->
                <ul class="left-ul">
                    <li>
                        <div>
                            <span>隐患标题：</span>
                            <p>${eventInfoVo.eventTitle}</p>
                        </div>
                    </li>
                    <li class="lines">
                        <div>
                            <span>紧急程度：</span>
                            <p>${fns:getDictValue("event_Urgency",eventInfoVo.eventurgency)}</p>
                        </div>
                    </li>
                    <li>
                        <div class="site">
                            <span>地点：</span>
                            <p>${eventInfoVo.eventAddress}</p>
                        </div>
                    </li>
                    <li>
                        <div>
                            <span>隐患内容：</span>
                            <p class="description">${eventInfoVo.eventContent}</p>
                        </div>
                    </li>
                    <c:if test="${not empty eventInfoVo.imgUrls}">
                        <li class="block-slider">
                            <div>
                                <span>附件：</span>
                                <div class="accessory-img">
                                    <ul class="xianshiw dowebok" id="slider_list1" >
                                        <c:forEach items="${eventInfoVo.imgUrls}" var="imgUrl" varStatus="vstatus">
                                            <li><img data-original="${imgUrl}" src="${imgUrl}" alt="" /></li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </li>
                    </c:if>
                    
                    
                    <li style="display: none;">
                        <div>
                            <button class="btn btn-primary " type="button" onclick="look_CAR()"><i class="fa fa-search"></i>&nbsp;安全隐患整改报告</button>
                        </div>
                    </li>
                </ul>
                <!--左边结束-->


                <!--右边开始-->
                <ul class="right-ul">
                    <c:forEach items="${processes}" var="proce" varStatus="vstatus" end="${processes.size()-2}">

                        <li class="rightli_three">
                            <div class="sequence_two">
                                <span>${processes.size()-vstatus.index}</span>
                            </div>

                            <c:choose>
                                <c:when test="${vstatus.first}">
                                    <div class="sum">
                                        <c:choose>
                                            <c:when test="${eventInfoVo.epNowNode eq 9}">
                                                <div class="matter_two">
                                                    <h3>已办结</h3>
                                                    <div>
                                                        <span>上报时间：</span>
                                                        <lable><fmt:formatDate value='${eventInfoVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/></lable>
                                                    </div>
                                                    <div>
                                                        <span>办结时间：</span>
                                                        <lable><fmt:formatDate value='${proce.epNowNodeArriveTime}' pattern='yyyy-MM-dd HH:mm:ss'/></lable>
                                                        <input type="hidden" id="endTime" value="${proce.epNowNodeArriveTime}">
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="matter_two">
                                                    <h3>待处理</h3>
                                                    <div>
		                                                <span>当前节点：</span>
		                                                <lable>${proce.epNowRoleName}</lable>
		                                            </div>
		                                            <div>
		                                                <span>接收时间：</span>
		                                                <lable><fmt:formatDate value='${proce.epNowNodeArriveTime}' pattern='yyyy-MM-dd HH:mm:ss'/></lable>
		                                            </div>
                                                </div>
                                                <c:if test="${not empty proce.epNowPersonName}">
		                                            <div class="matter_two">
		                                                <div>
		                                                    <span>处理人：</span>
		                                                    <lable>${proce.epNowPersonName}(<i>${proce.epNowPersonDpName}</i>)</lable>
		                                                </div>
		                                            </div>
		                                        </c:if>
                                                <div class="matter_two">
                                                    <div>
                                                        <span>已停留：</span>
                                                        <lable>${fns:getLengthTime(proce.epNowNodeArriveTime,proce.epNowNodeLeavleTime )}</lable>
                                                    </div>
                                                    <c:if test="${proce.epNowNode == '6' || proce.epNowNode == '7'}">
                                                        <c:if test="${not empty proce.epNowNodeLeavleTime}">
                                                            <div>
                                                                <span>超时时限：</span>
                                                                <lable style="color: red"><fmt:formatDate value='${proce.epNowNodeLeavleTime}' pattern='yyyy-MM-dd HH:mm:ss'/></lable>
                                                            </div>
                                                        </c:if>
                                                    </c:if>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="sum">
                                        <div class="matter_two">
                                            <div>
                                                <span>当前节点：</span>
                                                <lable>${proce.epNowRoleName}</lable>
                                            </div>
                                        </div>
                                        <div class="matter_two">
                                            <div>
                                                <span>处理人：</span>
                                                <lable>${proce.epNowPersonName}(<i>${proce.epNowPersonDpName}</i>)</lable>
                                            </div>
                                            <div>
                                                <span>接收时间：</span>
                                                <lable><fmt:formatDate value='${proce.epNowNodeArriveTime}' pattern='yyyy-MM-dd HH:mm:ss'/></lable>
                                            </div>
                                        </div>
                                        <div class="matter_two">
                                            <div>
                                                <span>本站用时：</span>
                                                <lable>${fns:getLengthTime(proce.epNowNodeArriveTime,proce.epNowNodeLeavleTime )}</lable>
                                            </div>
                                            <div>
                                                <span>办完时间：</span>
                                                <lable><fmt:formatDate value='${proce.epNowNodeLeavleTime}' pattern='yyyy-MM-dd HH:mm:ss'/></lable>
                                            </div>
                                        </div>
                                        <c:choose>
                                            <%-- 完结的 --%>
                                            <c:when test="${proce.epWhetherFinish == 1}">
                                                <div class="details">
	                                                <div>
	                                                    <span>${(proce.epNowNode == '8') ? "评价：" : "处理意见："}</span>
	                                                    <lable>${proce.epDealContent}</lable>
	                                                </div>
	                                            </div>
	                                            <c:if test="${proce.epWhetherFinish == '8'}">
	                                                <div class="matter_two">
	                                                    <div>
	                                                        <span>满意度：</span>
	                                                        <lable>${fns:getDictValue("event_Appraise", proce.epAppraise)}</lable>
	                                                    </div>
	                                                </div>
	                                            </c:if>
                                            </c:when>
                                            <%-- 退回的 --%>
                                            <c:when test="${proce.epIsReturned == 1}">
                                                <div class="details">
                                                    <div>
                                                        <span>退回理由：</span>
                                                        <lable>${proce.epReturnReason}</lable>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <%-- 其他的 --%>
                                            <c:otherwise>
                                                <div class="details">
                                                    <div>
                                                        <span>${(proce.epNowNode != '8') ? "处理意见：" : "退回理由："}</span>
                                                        <lable>${(proce.epNowNode != '8') ? proce.epDealContent : proce.epReturnReason}</lable>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="picture">
                                            <div class="accessory-img_two">
                                                <div class="accessory-img collagen">
                                                    <ul class="xianshiw dowebok" id="slider_list2" >
                                                        <c:forEach items="${proce.imgUrls}" var="imgUrl">
                                                            <li><img src="${imgUrl}" alt="" /></li>
                                                        </c:forEach>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </c:otherwise>
                            </c:choose>
                        </li>
                    </c:forEach>

                    <li class="rightli_two">
                        <div class="sequence">
                            <span>1</span>
                        </div>
                        <div class="matter">
                            <div>
                                <span>上报人：</span>
                                <lable>${eventInfoVo.creatorName} <i>(${eventInfoVo.contactPhone})</i></lable>
                            </div>
                            <div>
                                <span>上报时间：</span>
                                <lable><fmt:formatDate value='${eventInfoVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/></lable>
                            </div>
                        </div>
                    </li>
                </ul>
                <!--右边介绍 -->
            </div>
        </div>
    </div>
    <!--隐患详情页面结束-->
    <!--主体结束-->
</div>
<!--视频弹出层开始-->
<div id="video-contaner" style="display:none">
    <div id="blueimp-video-carousel" class="blueimp-gallery blueimp-gallery-controls blueimp-gallery-carousel">
        <div class="slides"></div>
        <a class="prev">‹</a>
        <a class="next">›</a>
        <a class="icon iconfont icon-cuowu1 closes"></a>
    </div>
</div>
<!--视频弹出层结束-->

<!--隐患流程开始-->

<div class="flow">
    <img src="/common/wf/images/event_${eventInfoVo.epNowNode}.jpg">

</div>
<!--隐患流程结束-->


<script type="text/javascript">

    var old = parseFloat(Date.parse('${eventInfoVo.createTime}'));
    var endTime = parseFloat(Date.parse($("#endTime").val()));
    $(function() {
        $('.right-ul').perfectScrollbar();
        gettottleTime();
        
        /*附近图片预览放大*/
        $('.dowebok').viewer({
            url: 'data-original',
        });
    });

    function gettottleTime(){
        var t = MillisecondToDate(endTime-old);
        $("#tottleTime").text(t);
    }
    
    var eventInfoId = "${eventInfoVo.id}";
    //查看    安全隐患整改报告
    function look_CAR(){
    	window.open("/safecheck/hiddenDanger/goToReportPage?eventInfoId="+eventInfoId);
    }
</script>
<!-- 图片放大插件 -->
<script src="/common/gis/event/js/viewer.min.js"></script>
<script src="/common/gis/event/js/viewer-jquery.min.js"></script>
</body>
</html>