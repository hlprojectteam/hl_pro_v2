/**
 * 前台地图搜索js
 * zengcong
 * 2018/04/05 15:08
 **/


$(function() {

    $("#button_search").click(function () {
        getDatas();
    });

    /*左菜单按钮鼠标接触事件*/
    $("#menu-button").hover(function() {
        $(this).find("span").show(300);
    }, function() {
        $(this).find("span").hide(300);
    });
   //搜索类目切换
    $('#icon-box').click(function(event) {
        $('#Modular-nav').show(300);
    });
    /*搜索框类目切换*/
    $('#Modular-nav ul li').click(function(){
        /*alert("hello word!"); */
        $('#icon-box').children().hide();
        $('#icon-box').children().eq($(this).index()).show();
        $('#Modular-nav').hide(300);
    });
    $('#Modular-nav ul li').click(function(event) {
        $(this).css('background', '#3385ff').siblings().css('background', '#fff');
        $(this).find('span').css('color', '#fff');
        $(this).find('i').css('color', '#fff');
        $(this).siblings().find('span').css('color', '#333');
        $(this).siblings().find('i').css('color', '#333');
    });

    /* 菜单内容收缩展开*/
    $("#menu-button").click(function(event) {
        $(".menu-box").addClass('animated slideInLeft').show();
        $(".search-wrap").css("height","100%");

    });



    $("#shrink-icon").click(function(event) {
        $("#categoryCode").val("");//清除菜单编码
        $(".menu-box").removeClass("slideInLeft").addClass("slideOutLeft");
        setTimeout(function(){
            $('.menu-box').removeClass("animated slideOutLeft").hide();
            $(".search-wrap").css("height","auto");
        }, 300);

    });
    /*行政区划、网格列表切换*/
    $('#menu-nav span').click(function(){
        $('#menu-content').children().hide();
        $('#menu-content').children().eq($(this).index()).show();


    });
    /*行政区划、网格列表按钮切换样式*/
    $("#menu-nav span#administrative").click(function(event) {
        $(this).css('background', '#3385ff');
        $(this).find('em').css('color', '#fff');
        $(this).find('i').css('color', '#fff');
        $("#Grid").css('background', '#eee');
        $("#Grid").find('em').css('color', '#333');
        $("#Grid").find('i').css('color', '#333');
    });
    $("#menu-nav span#Grid").click(function(event) {
        $(this).css('background', '#3385ff');
        $(this).find('em').css('color', '#fff');
        $(this).find('i').css('color', '#fff');
        $("#administrative").css('background', '#eee');
        $("#administrative").find('em').css('color', '#333');
        $("#administrative").find('i').css('color', '#333');
    });

    /*按钮导航切换开始*/
    $(".button-eve li").hover(function() {
        $(this).addClass('button-current');
        $(this).find('a').css('color', '#fff');
        /* $(this).find('span').css('z-index', '1');*/
        if($(".button-eve li").length - 1 == $(this).index()){
            $(this).find(".button-box").stop().animate({
                    opacity: '1',
                    visibility: 'visible',
                    left:'-100%',
                    right:'0'
                },
                300);
            $(this).find('.button-box em a').css('color', '#3385ff');
        }else{
            $(this).find(".button-box").stop().animate({
                    opacity: '1',
                    visibility: 'visible',
                    left:'1%'
                },
                300);
            $(this).find('.button-box em a').css('color', '#3385ff');
        }
    }, function() {
        $(this).removeClass('button-current');
        $(this).find('a').css('color', '#333');

        if($(".button-eve li").length - 1 == $(this).index()){
            $(this).find(".button-box").stop().animate({
                    opacity: '0',
                    visibility: 'hidden',
                    left:'1%'
                },
                300);
            $(this).find('.button-box em a').css('color', '#333');
        }else{
            $(this).find(".button-box").stop().animate({
                    opacity: '0',
                    visibility: 'hidden',
                    left:'-100%'
                },
                300);
            $(this).find('.button-box em a').css('color', '#333');
        }
    });


    $(".button-box em").hover(function() {
        $(this).css('background', '#3385ff');
        $(this).find('a').css('color', '#fff');
    }, function() {
        $(this).css('background', '#fff');
        $(this).find('a').css('color', '#3385ff');
    });
    /*按钮导航切换结束*/


    /*工具栏事件*/

    var res =$('#tools-box');
    res.hide();
    $("#toolsbar").click(function(){
        if(res.is(":hidden")){
            $(this).css('color', '#3385ff');
            $(this).find("em").css({"background-position":"-86px -9px"});
            res.slideDown(300);
        }else{
            $(this).css('color', '#333');
            $(this).find("em").css({"background-position":"-86px -34px"});
            res.slideUp(300);
        }
    });

    //搜索列表关闭
    $("#search_result_close").click(function(){
        $("#search_result").hide().animate({left: "0px"}, 500);
        $(".search-wrap").css("height","auto");
        $("#resultDatas").html("");
        $("#search-input").val("");
        popomap.removeAllPop();
    });

    $(".tools ul li a").hover(function() {
        $(this).css('background', '#eee');
        $(this).find('i').css('color', '#3385ff');
        $(this).find('.distance').css('background-position', '0px 0px');
        $(this).find('.area').css('background-position', '-42px 0px');
        $(this).find('.clear').css('background-position', '-42px -62px');
    }, function() {
        $(this).css('background', '#fff');
        $(this).find('i').css('color', '#333');
        $(this).find('.distance').css('background-position', '0px -29px');
        $(this).find('.area').css('background-position', '-42px -29px');
        $(this).find('.clear').css('background-position', '0px -62px');
    });


    /*加减号切换效果开始*/
    $(".symbol span").hover(function() {
        $(this).css('background', '#3385ff');
        $(this).find('em.plus').css('background-position', '-148px -4px');
        $(this).find('em.less').css('background-position', '-148px -30px');
    }, function() {
        $(this).css('background', '#fff');
        $(this).find('em.plus').css('background-position', '-115px -4px');
        $(this).find('em.less').css('background-position', '-115px -30px');
    });

    //注册事件
    $("#search-input").bind({
        keyup:function(){
            var keyValue = $("#search-input").val();
            var searchtype = $("#searchType").val();
            baseInfoSearch(keyValue,searchtype);
        },
        keypress:function(event){
            if(13==event.keyCode){
                getDatas();
            }
        }
    });

    //获得焦点时关闭行政区划选择
    $("#search-input").focus(function() {
        $(".menu-box").removeClass("slideInLeft").addClass("slideOutLeft");
        setTimeout(function () {
            $('.menu-box').removeClass("animated slideOutLeft").hide();
            $(".search-wrap").css("height", "auto");
        }, 300);
    });

    //点击选中关键字
    $("#oul").click(function(event){
        $("#search-input").val(event.target.innerText); // 回填
        $("#bot_box").hide();
        $("#oul").empty();
        getDatas(); // 查询选中的关键字
    });



});

/**搜索结果显示*/
function showSearchResult() {
    $("#search_result").show().animate({top: "50px"}, 500);
    $(".search-wrap").css("height","100%");
}

//搜索类别切换
function changeSearchType(type) {
    if(1==type){
        $("#searchType").val("population");
        $("#search-input").attr("placeholder","姓名/身份证");
    }
    if(2==type){
        $("#searchType").val("building");
        $("#search-input").attr("placeholder","建筑名称");
    }
    if(3==type){
        $("#searchType").val("resident");
        $("#search-input").attr("placeholder","户主/房间号");
    }
    if(4==type){
        $("#searchType").val("seller");
        $("#search-input").attr("placeholder","商家名称");
    }

}
function hideInfoBox(){
    $("#bot_box").hide();

}

function showBaseInfoBox(){
    $("#bot_box").show();
}
// 模糊查询
var keywords = "";
function baseInfoSearch(newKeywords,searchType){
    newKeywords = $.trim(newKeywords);
    if(newKeywords==keywords){
        return;
    }else{
        keywords = newKeywords;
        $("#bot_box").hide();
    }
    if(keywords == '') {
        $("#bot_box").hide();
        return; // 不输入东西不进行搜索
    }
    var dataAreaCode = $("#dataAreaCode").val();
    $.ajax({
        url:"/gis/search_findName",
        type:"post",
        data:{
                keywords:newKeywords,
                dataAreaCode:dataAreaCode,
                searchType:searchType
        },
        sync:true,
        dataType:"json",
        success:function(result){
            $("#oul").empty();
            var html="";
            if(result){
                $("#bot_box").show();
                for(var i=0;i<result.length;i++){
                    html +="<li><i class=\"icon iconfont icon-sousuo5\"></i>"+result[i]+"</li>";
                }
            }
            $("#oul").html(html);
        }
    });
}

// 根据基础信息类型和关键字搜索
function doSearch(page,rows){
    if(page == null) page = 1;
    if(rows == null) rows = 10;
    popomap.removeAllPop();
    $("#bot_box").hide();
    $("#oul").empty();
    $("#resultDatas").empty();
    var inputkeywords = $("#search-input").val();
    var searchType = $("#searchType").val();
    var dataAreaCode = $("#dataAreaCode").val();
	if(inputkeywords == '') return;
    $("#resultDatas").html("<div style=\"height: 284px;padding-top: 180px; text-align: center;\"><i class=\"layui-icon layui-anim layui-anim-rotate layui-anim-loop\" style=\"font-size: 50px; color: #1E9FFF;\">&#xe63d;</i></div>");
    showSearchResult();
    $.ajax({
        url:'/gis/search_doSearch',
        data:{
            keywords:inputkeywords,
            searchType:searchType,
            dataAreaCode:dataAreaCode,
            page:page,
            rows: rows
        },
        type:'post',
        success:function(data) {
            $("#resultDatas").html("");
            var list = data.pageList;
            if(list != null && list.length>0){
                var html = "<colgroup><col width=\"70\"><col width=\"100\"><col width=\"170\"><col></colgroup><tbody style=\"text-align: center;\">";
                var index = 1;
                var callback = "";
                if("building" == searchType) {
                    html = "<colgroup><col width=\"70\"><col width=\"270\"><col></colgroup><tbody style=\"text-align: center;\">";
                }
                for(var i=0; i<list.length; i++){
                    if("building" == searchType){
                        if(i<500){
                            callback = "onclick=openInfo(\""+searchType+"\",\""+list[i].entityId+"\",\""+list[i].buiName+"\")";
                            showPosition(list[i].x,list[i].y,list[i].id,list[i].buiName,callback); //建筑
                        }
                      html +=  "<tr class=\"data_list\" onclick=\"go2label("+list[i].x+","+list[i].y+");changeImage('"+list[i].id+"')\" onmousemove=\"changeImage('"+list[i].id+"')\" onmouseout=\"restoreImage('"+list[i].id+"')\">"+"<td>"+index+"</td><td>"+list[i].buiName+"</td>"
                            +"<td class=\"icon iconfont icon-dingwei\"></td></tr>";

                    }else if("resident" == searchType){
                        if(i<500) {
                            callback = "onclick=openInfo(\""+searchType+"\",\""+list[i][0]+"\",\""+list[i][1]+"\")";
                            showPosition(list[i][4],list[i][5], list[i][0], list[i][1], callback); //户
                        }
                        html +=  "<tr class=\"data_list\" onclick=\"go2label("+list[i][4]+","+list[i][5]+");changeImage('"+list[i][0]+"')\" onmousemove=\"changeImage('"+list[i][0]+"')\" onmouseout=\"restoreImage('"+list[i][0]+"')\">"+"<td>"+index+"</td><td>"+list[i][1]+"</td><td>"
                            +list[i][3]+"</td><td class=\"icon iconfont icon-dingwei\"></td></tr>";


                    }else {
                        if(i<500){
                            callback = "onclick=openInfo(\""+searchType+"\",\""+list[i].id+"\",\""+list[i].name+"\")";
                            showPosition(list[i].x,list[i].y,list[i].id,list[i].name,callback); //人口
                        }
                        html +=  "<tr class=\"data_list\" onclick=\"go2label("+list[i].x+","+list[i].y+");changeImage('"+list[i].id+"')\" onmousemove=\"changeImage('"+list[i].id+"')\" onmouseout=\"restoreImage('"+list[i].id+"')\">"+"<td>"+index+"</td><td>"+list[i].name+"</td><td>"
                            +list[i].idNumber+"</td><td class=\"icon iconfont icon-dingwei\"></td></tr>";
                    }

                    index++;
                }
                html += "</tbody>";
                $("#resultDatas").html(html);

            }
        },
        error : function() {

        }
    });
}

//根据坐标创建标注
function showPosition(x,y,id_,title,callback){
    if(typeof(x)=="number" && typeof(y) =="number"){
        var imageUrl = "/common/gis/images/front/position_blue.png";
        var backgroundColor = "#3385ff";
        var fontColor = "#fff";
        drawLabel(id_,imageUrl,backgroundColor,fontColor,title,x,y,-12,-60,callback);
    }

}

//根据实体ID地图显示位置
function showPositionByEntityId(entityId_,id_,title,callback){
    if(entityId_=="null"||entityId_==null || entityId_ == "") return;
    var url = $("#map_url").val()+"/gis/gisInfo_mapSearchJson?areaCode=520622&entityId=" + entityId_;
    $.ajax({
        url:url,
        type:'post',
        dataType:"jsonp",  //数据格式设置为jsonp
        jsonp:"callback",  //Jquery生成验证参数的名称
        success:function(data) {
            var lisLabel = data;
            if(lisLabel.length >0){
                var label = lisLabel[0];
                var imageUrl = "/common/gis/images/front/position_blue.png";
                var backgroundColor = "#3385ff";
                var fontColor = "#fff";
                drawLabel(id_,imageUrl,backgroundColor,fontColor,title,label.x,label.y,-12,-60,callback);
            }
        },
        error : function() {

        }
    });
}
function getDatas(){
    $("#bot_box").hide();
    $("#oul").empty();
    var keywords = $("#search-input").val();
    var searchType = $("#searchType").val();
    var dataAreaCode = $("#dataAreaCode").val();
    var dataAreaName = $("#dataAreaName").val();
    if(keywords == '') return;
    $.ajax({
        url:'/gis/search_getCount',
        data:{
            keywords:keywords,
            dataAreaCode:dataAreaCode,
            searchType:searchType
        },
        type:'post',
        success:function(data) {
            layui.use(['laypage', 'layer'], function(){
                if('' == dataAreaName){
                    $("#result_count").html("共找到"+data+"条相关信息");
                }else{
                    $("#result_count").html("在"+dataAreaName+"共找到"+data+"条相关信息");
                }

                var laypage = layui.laypage,
                    layer = layui.layer;
                //调用分页
                laypage.render({
                    elem: 'infor-page',
                    count: data,
                    groups: 2,
                    limit:15,
                    theme: '#1E9FFF',
                    jump: function(obj){
                        doSearch(obj.curr,15);
                    }
                });

            });
        },
        error : function() {

        }
    });
}


/**
 * 搜索结果弹窗
 */
function openInfo(type,id,title) {
    var src;
    /*if("building" == type){
        src = "/baseLib/building_viewEntityBuilding?entityId="+id; //建筑
    }else if("resident" == type){
        src = "/baseLib/resident_getResident?residentId="+id //户
    }else {
        src = "/baseLib/population_detailPopulationForPop?populationId="+id //人口
    }*/
    switch (type) {
	    case "building": src = "/baseLib/building_viewEntityBuilding?entityId="+id;		//建筑
	    break;
	    case "resident": src = "/baseLib/resident_getResident?residentId="+id;		//户
	    break;
	    case "population": src = "/baseLib/population_detailPopulationForPop?populationId="+id;		//人口
	    break;
	        
	    case "serviceAl": src = "/gis/menu/serviceInfo_detailServiceAlForPop?populationId="+id;		//低(五)保户人员
	    break;
	    case "serviceDb": src = "/gis/menu/serviceInfo_detailServiceDbForPop?populationId="+id;		//残疾人
	    break;
	    case "serviceEm": src = "/gis/menu/serviceInfo_detailServiceEmForPop?populationId="+id;		//空巢老人
	    break;
	    case "serviceHp": src = "/gis/menu/serviceInfo_detailServiceHpForPop?populationId="+id;		//帮扶救助
	    break;
	    case "serviceIns": src = "/gis/menu/serviceInfo_detailServiceInsForPop?populationId="+id;	//医保社保
	    break;
	    case "serviceMa": src = "/gis/menu/serviceInfo_detailServiceMaForPop?populationId="+id;		//城乡医疗救助
	    break;
	    case "serviceMs": src = "/gis/menu/serviceInfo_detailServiceMsForPop?populationId="+id;		//兵役状况
	    break;
	    case "serviceEd": src = "/gis/menu/serviceInfo_detailServiceEdForPop?populationId="+id;		//教育信息
	    break;
	    
	    case "stabilityDa": src = "/gis/menu/stabilityControl_detailStabilityDaForPop?populationId="+id;		//吸毒人员 
	    break;
	    case "stabilityCu": src = "/gis/menu/stabilityControl_detailStabilityCuForPop?populationId="+id;		//邪教人员 
	    break;
	    case "stabilityCc": src = "/gis/menu/stabilityControl_detailStabilityCcForPop?populationId="+id;		//社区矫正人员 
	    break;
	    case "stabilityTr": src = "/gis/menu/stabilityControl_detailStabilityTrForPop?populationId="+id;		//刑释解教人员 
	    break;
	    case "stabilityKy": src = "/gis/menu/stabilityControl_detailStabilityKyForPop?populationId="+id;		//重点青少年 
	    break;
	    case "stabilityDp": src = "/gis/menu/stabilityControl_detailStabilityDpForPop?populationId="+id;		//危险精神病人 
	    break;
			
	    //case "": src = ""; break;
	    default: ;
    }

    parent.layer.open({
        type: 2,
        title: title,
        shadeClose: true,//打开遮蔽
        shade: 0.3,
        maxmin: false, //开启最大化最小化按钮
        area: ["570px", "550px"],
        content: src
    });

}


function changeImage(id) {
    restoreImage(id);
    $("#img_"+id).attr("src","/common/gis/images/front/position_red.png");
    $("#title_"+id).css("background-color","#e92323");
}

function restoreImage(id) {
    $(".imageBox-icon").attr("src","/common/gis/images/front/position_blue.png")
    $(".imageBox-title").css("background-color","#3385ff");
}

/**
 * 多边形搜索
 */
function rangeSearch() {
    var mapManager = new MapManager();
    mapManager.init(map,3);




    //获取 绘制图形坐标极值
    function computerPiont(points ){
        var xMin = 999999.999999;
        var xMax = -999999.999999;
        var yMin = 999999.999999;
        var yMax = -999999.999999;

        for(var index in points.components[0].components){
            var point = points.components[0].components[index];

            if(point.x < xMin){
                xMin = point.x;
            }
            if(point.x > xMax){
                xMax = point.x;
            }
            if(point.y < yMin){
                yMin = point.y;
            }
            if(point.y > yMax){
                yMax = point.y;
            }
        }
        return {xMin:xMin,xMax:xMax,yMin:yMin,yMax:yMax};
    }
}