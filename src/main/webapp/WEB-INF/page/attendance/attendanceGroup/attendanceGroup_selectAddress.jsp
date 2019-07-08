<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>选择地址界面</title>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=2cO9ikrgWWwRObGKRCYchS887w4q40lz"></script>
</head>
<body>
<div class="ibox-content">

    <div style="width: 40%;display: inline-block;">
        <div id="r-result">
            <strong>请输入地址</strong> &nbsp;&nbsp;<input type="text" id="suggestId" size="20" value="" style="width: 80%;" />
        </div>
        <div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
    </div>

    <div style="width: 50%;display: inline-block;">
        <input type="hidden" id="x" value="${attendanceGroupVo.x}" />
        <input type="hidden" id="y" value="${attendanceGroupVo.y}" />
        <strong>选择的地址</strong> &nbsp;&nbsp;<input type="text" id="address" value="${attendanceGroupVo.address}" style="width: 500px;" />
    </div>

    <br><br>

    <!-- 地图容器 -->
    <div id="allmap" style="width: 100%; height: 480px;"></div>


</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
    <div class="pull-right">
        <button class="btn btn-primary " type="button" onclick="on_sure()"><i class="fa fa-check"></i>&nbsp;确定</button>
        <button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
    </div>
</div>
</body>
<script>
    var winName = '${winName}';//上一ifame name
    var x_ = '${shiftsVo.x}';//回填x
    var y_ = '${shiftsVo.y}';//回填y
    var address_ = '${shiftsVo.address}';//回填address

    // 百度地图API功能
    var map = new BMap.Map("allmap");	// 创建Map实例
    map.centerAndZoom("广州",13);  		// 初始化地图,用城市名设置地图中心点和地图级别
    map.enableScrollWheelZoom(true);	// 开启鼠标滚轮缩放

    /* -------------------------------添加比例尺和缩放平移控件 ------------------------------------------------------*/
    var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});	// 左上角，添加比例尺
    var top_left_navigation = new BMap.NavigationControl();  						// 左上角，添加默认缩放平移控件

    //添加比例尺和控件
    map.addControl(top_left_control);
    map.addControl(top_left_navigation);


    /* -------------------------------添加定位控件 ------------------------------------------------------*/
    // 添加带有定位的导航控件
    var navigationControl = new BMap.NavigationControl({
        // 靠左上角位置
        anchor: BMAP_ANCHOR_TOP_LEFT,
        // LARGE类型
        type: BMAP_NAVIGATION_CONTROL_LARGE,
        // 启用显示定位
        enableGeolocation: true
    });
    map.addControl(navigationControl);
    // 添加定位控件
    var geolocationControl = new BMap.GeolocationControl();
    geolocationControl.addEventListener("locationSuccess", function(e){
        // 定位成功事件
        var address = '';
        address += e.addressComponent.province;
        address += e.addressComponent.city;
        address += e.addressComponent.district;
        address += e.addressComponent.street;
        address += e.addressComponent.streetNumber;
        alert("当前定位地址为：" + address);
    });
    geolocationControl.addEventListener("locationError",function(e){
        // 定位失败事件
        alert(e.message);
    });
    map.addControl(geolocationControl);


    /* -------------------------------添加/删除覆盖物(初始化地图标注) ------------------------------------------------------*/
    $(function(){
        //清除覆盖物
        map.clearOverlays();

        if(x_ != "" && y_ != ""){
            $("#x").val(x_);
            $("#y").val(y_);
            $("#address").val(address_);
            var marker = new BMap.Marker(new BMap.Point(x_, y_)); 	// 创建标注
            map.addOverlay(marker);		// 将标注添加到地图中
        }
    });


    /* -------------------------------鼠标点击绘制标注 ------------------------------------------------------*/
    // 创建地址解析器实例
    var geoc = new BMap.Geocoder();

    map.addEventListener("click", function(e){
        map.clearOverlays();	//清除覆盖物

        var m = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat));
        map.addOverlay(m);

        $("#x").val(e.point.lng);
        $("#y").val(e.point.lat);

        geoc.getLocation(e.point, function(rs){
            var addComp = rs.addressComponents;
            $("#address").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber);
        });

    });


    /* -------------------------------关键字提示输入 ------------------------------------------------------*/

    function G(id) {
        return document.getElementById(id);
    }

    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });

    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

        setPlace();
    });

    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
            map.centerAndZoom(pp, 18);
            map.addOverlay(new BMap.Marker(pp));    //添加标注

            $("#x").val(pp.lng);
            $("#y").val(pp.lat);
            $("#address").val(myValue);
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });
        local.search(myValue);
    }


    /* -------------------------------保存并回填 ------------------------------------------------------*/
    //保存
    function on_sure(){
        var x = $("#x").val();
        var y = $("#y").val();
        var address = $("#address").val();
        parent.frames[winName].$("#x").val(x);
        parent.frames[winName].$("#y").val(y);
        parent.frames[winName].$("#address").val(address);
        parent.layer.close(index);//关闭
    }

</script>
</html>