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
    <title>待办明细</title>
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
    <script src="/common/plugins/validate/jquery.validate.js" type="text/javascript"></script>
    <script src="/common/js/base.js"></script>
    <script src="/common/js/form2Json.js"></script>
    <script src="/common/js/alert_tip.js"></script>
    <script src="/common/js/utils.js"></script>
    <!-- 日期插件 -->
    <script type="text/javascript" src="/common/plugins/My97DatePicker/WdatePicker.js"></script>
    <!-- 图片上传start -->
    <link href="/common/plugins/webUploader/user/user_photo.css" rel="stylesheet">
    <link href="/common/plugins/webUploader/webuploader.css" rel="stylesheet">
    <script src="/common/plugins/webUploader/webuploader.js"></script>
    <script type="text/javascript">
        var photoNum = 4;
    </script>
    <script src="/common/plugins/webUploader/photoByMany.js"></script>
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
                    
                    <!--上一节点处理情况-->
                    <li class="dispose">
                        <div class="dispose_tit">
                            <i class="icon iconfont icon-shijianchuli"></i><b>上一节点：${fns:getDictValue("event_EventNode",processes.get(1).epNowNode)} &nbsp;&nbsp; ${processes.get(1).epNowPersonName}</b>
                        </div>
                        <br>
                        <c:if test="${processes.get(0).epNowNode != '2'}">
	                        <div class="details">
	                            <c:if test="${not empty processes.get(1).epDealContent}">
		                            <span>处理情况：</span>
		                            <lable><b style="color: red;">${processes.get(1).epDealContent}</b></lable>
	                            </c:if>
	                            <c:if test="${empty processes.get(1).epDealContent}">
                                    <span>退回理由：</span>
                                    <lable><b style="color: red;">${processes.get(1).epReturnReason}</b></lable>
                                </c:if>
	                        </div>
                        </c:if>
                    </li>
                    
                    <!--隐患处理开始-->
                    <li class="dispose">
                        <div class="dispose_tit">
                            <i class="icon iconfont icon-shijianchuli"></i><b>隐患处理：</b>(当前： <b>${fns:getDictValue("event_EventNode",eventInfoVo.epNowNode)}</b>)
                        </div>
                    </li>

                    <form id="baseForm" method="post" name="baseForm" action="">
                        <input type="hidden" id="eventInfoId" name="id" value="${eventInfoVo.id}" />
                        <input type="hidden" id="eventTitle" name="eventTitle" value="${eventInfoVo.eventTitle}" />
                        <input type="hidden" id="creatorId" name="creatorId" value="${eventInfoVo.creatorId}" />
                        <input type="hidden" id="creatorName" name="creatorName" value="${eventInfoVo.creatorName}" />
                        <input type="hidden" id="id" name="epId" value="${eventInfoVo.epId}" />
                        <input type="hidden" id="epNowNode" name="epNowNode" value="${eventInfoVo.epNowNode}" />
                        <input type="hidden" id="epNowPersonId" name="epNowPersonId" value="${eventInfoVo.epNowPersonId}">
                        <input type="hidden" id="epNowPersonName" name="epNowPersonName" value="${eventInfoVo.epNowPersonName}">
                        <input type="hidden" id="epNowRole" name="epNowRole" value="${eventInfoVo.epNowRole}">
                        <input type="hidden" id="epNowRoleName" name="epNowRoleName" value="${eventInfoVo.epNowRoleName}">
                        
                        <input type="hidden" id="epNextRole" name="epNextRole" value="${eventInfoVo.epNextRole}" />
                        <input type="hidden" id="epNextRoleName" name="epNextRoleName" value="${eventInfoVo.epNextRoleName}" />
                        <input type="hidden" id="epNextPersonId" name="epNextPersonId" value="${eventInfoVo.epNextPersonId}" />
                        <input type="hidden" id="epNextPersonName" name="epNextPersonName" value="${eventInfoVo.epNextPersonName}" />
                        <input type="hidden" id="dataSource" name="dataSource" value="1" />
                        
                        
                        <%-- 当前节点     为 6、7、8(处理部门负责人、处理人、安保办安全员), 则显示该隐患是否退回,默认隐患不退回  --%>
                        <c:if test="${eventInfoVo.epNowNode == '6' || eventInfoVo.epNowNode == '7'}">
		                    <div class="form-group whether">
			                    <label class="control-label whetherReturn">是否退回：</label>
			                    <div class="col-sm-3 whetherReturn">
			                        <opt:select dictKey="isNot" classStyle="form-control" id="epIsReturned" name="epIsReturned" value="0"  isDefSelect="false" />
			                    </div>
		                    </div>
	                    </c:if>
	                    
	                    <%-- 当前节点     为5、10(安保办主任、分管领导), 则显示该隐患是否向上级请示,默认不请示   --%>
	                    <c:if test="${eventInfoVo.epNowNode == '5' || eventInfoVo.epNowNode == '10'}">
		                    <div class="form-group whether goUpstairs">
	                            <label class="control-label" style="width: 20%;">是否向上级请示：</label>
	                            <div class="col-sm-3">
	                                <opt:select dictKey="isNot" classStyle="form-control" id="isGoUpstairs" name="isGoUpstairs" value="0" isDefSelect="false" />
	                            </div>
	                        </div>
                        </c:if>
                        
                        <%-- 当前节点是  2、3、4、5、8(部门安全员、部门负责人、安保办安全员、安保办主任)时,都可将隐患直接完结, 默认未办结    --%>
                        <c:if test="${eventInfoVo.epNowNode == '2' || eventInfoVo.epNowNode == '3' || eventInfoVo.epNowNode == '4' || eventInfoVo.epNowNode == '5' || eventInfoVo.epNowNode == '8'}">
                            <div class="form-group whether isWhetherFinish">
	                            <label class="control-label" style="float: left;">是否办结：</label>
	                            <div class="col-sm-3">
	                                <opt:select dictKey="event_WhetherFinish" classStyle="form-control" id="epWhetherFinish" name="epWhetherFinish" value="2"  isDefSelect="false" />
	                            </div>
	                            <%-- 当前节点是  8,且隐患为已办结,才会显示满意度    --%>
	                            <c:if test="${eventInfoVo.epNowNode == '8'}">
		                            <label class="control-label satisficing" style="float: left; display: none;">满意度：</label>
	                                <div class="col-sm-3 satisficing" style="display: none;">
	                                    <opt:select dictKey="event_Appraise" classStyle="form-control" id="epAppraise" name="epAppraise" value="1"  isDefSelect="false" />
	                                </div>
	                            </c:if>
	                        </div>
                        </c:if>
                        
                        <%-- 当前节点为       2-5、10、11只有处理意见; 6、7 有处理意见和退回理由可选;  8 有退回理由和评价可选.  --%>
                        <li class="opinion">
                            <c:if test="${eventInfoVo.epNowNode != '8'}">
	                            <div class="opinion_one dealContent">
	                                <span class="epDealContent">处理意见：</span>
	                                <textarea name="epDealContent" id="epDealContent" maxlength="300" placeholder="请填写您的处理意见" cols="115" rows="10" style="height: 120px;width: 87%;resize: none;padding: 8px;text-indent: 2em;"></textarea>
	                            </div>
                            </c:if>
                            <c:if test="${eventInfoVo.epNowNode == '6' || eventInfoVo.epNowNode == '7'}">
                                <div class="opinion_one returnReason" style="display: none;">
                                    <span>退回理由：</span>
                                    <textarea name="epReturnReason" id="epReturnReason" maxlength="300" placeholder="请填写您的退回理由" cols="115" rows="10" style="height: 120px;width: 87%;resize: none;padding: 8px;text-indent: 2em;"></textarea>
                                </div> 
                            </c:if>
                            <c:if test="${eventInfoVo.epNowNode == '8'}">
                                <div class="opinion_one dealContent" style="display: none;">
                                    <span class="epDealContent">评价：</span>
                                    <textarea name="epDealContent" id="epDealContent" maxlength="300" placeholder="请填写您的评价" cols="115" rows="10" style="height: 120px;width: 87%;resize: none;padding: 8px;text-indent: 2em;"></textarea>
                                </div>
                                <div class="opinion_one returnReason">
                                    <span>退回理由：</span>
                                    <textarea name="epReturnReason" id="epReturnReason" maxlength="300" placeholder="请填写您的退回理由" cols="115" rows="10" style="height: 120px;width: 87%;resize: none;padding: 8px;text-indent: 2em;"></textarea>
                                </div> 
                            </c:if>
                        </li>
                        
                        <%-- 当前节点是   5(安保办主任) 且   不向上级请示, 则显示该隐患的分派信息(部门负责人  和  处理时限) --%>
                        <c:if test="${eventInfoVo.epNowNode == '5'}">
                            <li class="opiniondb paiqian1">
                                <div>
                                    <span>部门负责人：</span>
                                    <button type="button" class="btn btn-info" onclick="on_selectHandDepart()">选择部门负责人</button>
                                    <span class="role">已选择部门负责人(所在部门)：</span><em id="nextPersonName"></em>
                                </div>
                                <div class="process-limited">
                                    <span>处理时限：</span>
                                    <input style="display: inline; width: auto;" type="text" class="form-control" id="epLimitTime" name="epLimitTime" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                                </div>
                            </li>
                        </c:if>
                        
                        <%-- 当前节点是   6(部门负责人) 且  隐患不退回, 则显示该隐患的分派信息(处理人  和  处理时限) --%>
                        <c:if test="${eventInfoVo.epNowNode == '6'}">
                            <li class="opiniondb paiqian2">
                                <div>
                                    <span>处理人：</span>
                                    <button type="button" class="btn btn-info" onclick="on_selectEventHandler()">选择处理人</button>
                                    <span class="role">已选择处理人(所在部门)：</span><em id="nextPersonName"></em>
                                </div>
                                <div class="process-limited">
                                    <span>处理时限：</span>
                                    <input style="display: inline; width: auto;" type="text" class="form-control" id="epLimitTime" name="epLimitTime" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                                </div>
                            </li>
                        </c:if>
                        
                        
                        <%-- 附件是每一个隐患过程都存在的     --%>
                        <li class="opinion">
	                        <div class="mui-input texta">
	                            <span>附件：</span>
	
	                            <div id="uploaderDiv" class="wu-example">
	                                <div class="queueList" style="height: 160px;width: 100%">
	                                    <div id="dndArea" class="placeholder">
	                                        <div id="filePicker" style="margin-bottom: 0"></div>
	                                        <p>最多可选4张</p>
	                                        <!--图片列表 -->
	                                    </div>
	                                </div>
	                                <div id="statusShow" class="statusBar" style="border:none;">
	                                    <div class="info"></div>
	                                    <div class="btns">
	                                        <div id="filePickerAdd" class="fileAddBtn"></div>
	                                    </div>
	                                </div>
	                            </div>
	                        </div>
	                    </li>
                    <!--隐患处理结束-->
                    </form>
                    <br><br>
                </ul>
                <!--左边结束-->


                <!--右边开始-->
                <ul class="right-ul">
                    <!--2-->
                    <c:forEach items="${processes}" var="proce" varStatus="vstatus" end="${processes.size()-2}">

                        <li class="rightli_three">
                            <div class="sequence_two">
                                <span>${processes.size()-vstatus.index}</span>
                            </div>

                            <c:choose>
                                <c:when test="${vstatus.first}">
                                    <div class="sum">
                                        <div class="matter_two">
                                            <h3>待处理</h3>
                                            <div>
                                                <span>当前节点：</span>
                                                <lable>${fns:getDictValue("event_EventNode",proce.epNowNode)}</lable>
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
                                            <c:if test="${eventInfoVo.epNowNode == '6' || eventInfoVo.epNowNode == '7'}">
                                                <c:if test="${not empty proce.epLimitTime}">
                                                    <div>
                                                        <span>超时时限：</span>
                                                        <lable style="color: red"><fmt:formatDate value='${proce.epLimitTime}' pattern='yyyy-MM-dd HH:mm:ss'/></lable>
                                                    </div>
                                                </c:if>
                                            </c:if>
                                        </div>
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
<!-- 底部区域 -->
<div class="footer edit_footer">
    <!-- 底部按钮 start-->
    <div class="pull-right">
        <button class="btn btn-primary " type="button" onclick="on_save()">
            <i class="fa fa-check"></i>&nbsp;提 交
        </button>&nbsp;&nbsp;&nbsp;
        <button class="btn btn-danger " type="button" onclick="on_close()">
            <i class="fa fa-close"></i>&nbsp;取 消
        </button>
    </div>
    <!-- 底部按钮 end-->
</div>

<script type="text/javascript">
    var lastWinName = '${winName}';     //上一个ifame name
    var winName = window.name;  //当前ifame name
    
    var id = '${eventInfoVo.id}';   //获取隐患id
    var epNowNode = '${eventInfoVo.epNowNode}';     //当前节点
    var old = parseFloat(Date.parse('${eventInfoVo.createTime}'));


    $(function(){
        $('.right-ul').perfectScrollbar();
        /*附件图片预览放大*/
        $('.dowebok').viewer({
            url: 'data-original',
        });
        
        gettottleTime();    //总用时间
        var int=self.setInterval("gettottleTime()", 1000);
    });


    //总用时间
    function gettottleTime(){
        var now = parseFloat(Date.parse(new Date()));
        var t = MillisecondToDate(now-old);
        $("#tottleTime").text(t);
    }
    
    
    //(节点5:安保办主任) 是否向上级请示   (默认不请示)
    //根据是否向上级请示来决定是否显示      该隐患的分派信息(部门负责人 和  处理时限) 和  是否办结
    $("#isGoUpstairs").change(function(){
        changeGoUpstairsStatus();
    });
    function changeGoUpstairsStatus(){
        var isGoUpstairs = $("#isGoUpstairs").val();      //是否向上级请示( 1:请示;  0:不请示)
        if(isGoUpstairs != 1){           //不请示
            $(".paiqian1").show();          //显示分派信息
            $(".isWhetherFinish").show();   //显示是否办结
        }else{                           //请示
            $(".paiqian1").hide();            //隐藏分派信息
            $(".isWhetherFinish").hide();     //隐藏是否办结
        }
    }
   
    
    //根据隐患是否退回来决定是否显示退回理由(退回则显示,否则不显示)
    //根据隐患是否退回来决定是否显示该隐患的分派信息(处理人  和  处理时限)[在当前节点是   6(部门负责人)的情况下]
    $("#epIsReturned").change(function(){
    	changeReturnStatus();
    });
    function changeReturnStatus(){
    	var epWhetherReturn = $("#epIsReturned").val();      //是否退回( 1:退回;  0:不退回)
        if(epWhetherReturn != 1){           //不退回: 隐藏退回理由,显示处理意见
            $(".returnReason").hide();              
            $(".dealContent").show();
            $(".paiqian2").show();   //显示分派信息
        }else{                              //退回: 显示退回理由,隐藏处理意见
            $(".returnReason").show();              
            $(".dealContent").hide();
            $(".paiqian2").hide();   //隐藏分派信息
        }
    }
    
    
    //根据隐患办结状态决定是否显示    是否向上级请示、派遣信息、满意度
    $("#epWhetherFinish").change(function(){
        changeFinishStatus();
    });
    function changeFinishStatus(){
        var epWhetherFinish = $("#epWhetherFinish").val();      //是否办结( 1:已办结;  2:未办结)
        if(epWhetherFinish == 2){       //未办结
        	$(".goUpstairs").show();       //显示请示上级
        	$(".paiqian1").show();         //显示派遣信息
            if(epNowNode == '8'){
            	$(".satisficing").hide();  //隐藏满意度
            	$(".dealContent").hide();
            	$(".returnReason").show();
            }else{
            	$(".satisficing").hide();  //隐藏满意度
            }
        }else{                          //已办结
        	$(".goUpstairs").hide();       //隐藏请示上级
            $(".paiqian1").hide();         //隐藏派遣信息
            $(".dealContent").show();
            if(epNowNode == '8'){
            	$(".satisficing").show();  //显示满意度
            	$(".returnReason").hide();
            }
        }
    }

    
    //选择派遣对象    部门负责人
    function on_selectHandDepart(){
        parent.layer.open({
            type: 2,
            title: "选择派遣对象",
            shadeClose: true,//打开遮蔽
            shade: 0.6,
            maxmin: true, //开启最大化最小化按钮
            area: ["50%", "80%"],
            content: "/safecheck/hiddenDanger/event_selectHandDepart?winName="+winName
        });
    }
    
    var dpId = '${dpId}';   //当前节点处理人所在部门id  
    //选择派遣对象    处理人
    function on_selectEventHandler(){
        parent.layer.open({
            type: 2,
            title: "选择派遣对象",
            shadeClose: true,//打开遮蔽
            shade: 0.6,
            maxmin: true, //开启最大化最小化按钮
            area: ["50%", "80%"],
            content: "/safecheck/hiddenDanger/event_selectEventHandler?dpId="+dpId+"&winName="+winName
        });
    }


    //保存
    function on_save() {
    	if($("#epIsReturned").val() == 0){      //如果隐患   不退回,则处理意见不能为空
    	   if($("#epDealContent").val() == null || $("#epDealContent").val() ==''){
                autoAlert("请填写处理意见!",5);
                return false;
            }
    	}else if($("#epIsReturned").val() == 1){  //如果隐患   退回,则退回理由不能为空
    	   if($("#epReturnReason").val() == null || $("#epReturnReason").val() ==''){
                autoAlert("请填写退回理由!",5);
                return false;
            }
    	}
    	
    	if(epNowNode == '5' && $("#isGoUpstairs").val() == 0 && $("#epWhetherFinish") == 2){     //若当前节点为   5(安保办主任)  且  不向上级请示  且 未办结, 则隐患部门负责人不能为空
            if($("#epNextPersonId").val() == null || $("#epNextPersonId").val() ==''){
                autoAlert("请选择部门负责人!",5);
                return false;
            }
        }
    	
    	if(epNowNode == '6' && $("#epIsReturned").val() == 0){     //若当前节点为   6(部门负责人) 且  不退回, 则隐患处理人不能为空
            if($("#epNextPersonId").val() == null || $("#epNextPersonId").val() ==''){
                autoAlert("请选择隐患处理人!",5);
                return false;
            }
        }
    	
        on_submit();
    }


    //提交表单
    function on_submit() {
    	var url;
    	
    	var epWhetherFinish = $("#epWhetherFinish").val();
    	var epIsReturned = $("#epIsReturned").val();
    	var isGoUpstairs = $("#isGoUpstairs").val();
    	
    	if(epNowNode == '2'){
    		if(epWhetherFinish == 2){
    			url = "/safecheck/hiddenDanger/reportEventToBMFZR";
    		}else{
    			url = "/safecheck/hiddenDanger/confirmEventFinish";
    		}
    	}else if(epNowNode == '3'){
    		if(epWhetherFinish == 2){
                url = "/safecheck/hiddenDanger/reportEventToABBAQY";
            }else{
                url = "/safecheck/hiddenDanger/confirmEventFinish";
            }
    	}else if(epNowNode == '4'){
    		if(epWhetherFinish == 2){
            	url = "/safecheck/hiddenDanger/reportEventToABBZR";
            }else{
            	url = "/safecheck/hiddenDanger/confirmEventFinish";
            }
        }else if(epNowNode == '5'){
            if(epWhetherFinish == 2){
            	if(isGoUpstairs == 0){
            		url = "/safecheck/hiddenDanger/dispatchEventToBMFZR";
            	}else{
            		url = "/safecheck/hiddenDanger/goUpstairsToFGLD";
            	}
            }else {
                url = "/safecheck/hiddenDanger/confirmEventFinish";
            }
        }else if(epNowNode == '6'){
            if(epIsReturned == 0){
                url = "/safecheck/hiddenDanger/dispatchEventToDealPeople";
            }else{
                url = "/safecheck/hiddenDanger/returnToABBZR";
            }
        }else if(epNowNode == '7'){
        	if(epIsReturned == 0){
        		url = "/safecheck/hiddenDanger/feedBackEventToABBAQY";
        	}else{
        		url = "/safecheck/hiddenDanger/returnToBMFZR";
        	}
        }else if(epNowNode == '8'){
            if(epWhetherFinish == 1){
                url = "/safecheck/hiddenDanger/confirmEventFinish";
            }else{
                url = "/safecheck/hiddenDanger/returnToDealPeople";
            }
        }else if(epNowNode == '10'){
        	if(isGoUpstairs == 0){
        		url = "/safecheck/hiddenDanger/feedBackToABBZR";
        	}else{
        		url = "/safecheck/hiddenDanger/goUpstairsToCWFZJL";
        	}
        }else if(epNowNode == '11'){
        	url = "/safecheck/hiddenDanger/feedBackToFGLD";
        }
    	
    	
        $.ajax({
            type : 'post',
            async : false,
            dataType : 'json',
            url : url,
            data : $('#baseForm').serialize(),
            success : function(data) {
                if(uploader.getFiles().length>0){
                	var formId = $("#id").val();   //上报市附件关联的实体id(应是当前最新过程的id)
                    uploader.options.server = '/attach_upload?formId='+formId +'&attachType=event';
                    uploader.upload();//上传
                    uploader.on('uploadFinished', function() {
                        if (data.result) {
                            autoMsg("提交成功！", 1);
                            parent.frames[lastWinName].$("#grid").bootstrapTable("refresh", {url : "/safecheck/hiddenDanger/event_loadAgendaList"});
                            parent.layer.close(index);
                        } else {
                            autoAlert("提交失败，请检查！", 5);
                        }
                    });
                }else{
                    if (data.result) {
                        autoMsg("提交成功！", 1);
                        parent.frames[lastWinName].$("#grid").bootstrapTable("refresh", {url : "/safecheck/hiddenDanger/event_loadAgendaList"});
                        parent.layer.close(index);
                    } else {
                        autoAlert("提交失败，请检查！", 5);
                    }
                }
            },
            error : function (XMLHttpRequest, textStatus, errorThrown) {
                autoAlert("系统出错，请检查！", 5);
            }
        });
    }
</script>
<!-- 图片放大插件 -->
<script src="/common/gis/event/js/viewer.min.js"></script>
<script src="/common/gis/event/js/viewer-jquery.min.js"></script>
</body>
</html>