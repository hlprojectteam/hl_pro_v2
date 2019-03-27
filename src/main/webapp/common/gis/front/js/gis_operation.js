/**
 * 前台地图业务js
 * zengcong
 * 2018/04/13 10:08
 **/

/**
 * 地图菜单点击方法
 * @param code 菜单编码
 */
function categoryClick(code){
    if('undefined' == code || '' == code){
        $("#categoryCode").val("");
    }else{
        $("#categoryCode").val(code);
        $(".menu-box").addClass('animated slideInLeft').show();
        $(".search-wrap").css("height","100%");
    }
}

/**
 * 点击菜单后注册树回调方法
 */
function categoryCallBack() {
    var categoryCode = $("#categoryCode").val();
    switch (categoryCode) {
        case 'gridManager':  //网格管理
          //  showGrids();//显示网格
            break;

    }
}

/**
 * 地图左侧弹窗方法
 * @param url 对应请求url
 * @param title 对应请求title
 */
function dataDistribution(url,title){
    layer.close(layer.index);//关闭之前的窗口
    //layer.closeAll('iframe'); //关闭所有的iframe层
    parent.layer.open({
        type: 2,
        title: title,
        anim: 3,
        shadeClose: false,//打开遮蔽
        offset: ['69px', '4%'],
        shade: 0,
        move: false,
        end: function(){
            top.popomap.removeAllPop();
        },
        maxmin: false, //开启最大化最小化按钮
        area: ['400px','90%'],
        content: url
    });
}

/**
 * 获取对应行政编码下的网格
 * @param dataAreaCode
 */
function getGridByDataAreaCode(dataAreaCode){
    $("#grid_img").show();
    var url = '/gis/findGridByCode?dataAreaCode=' + dataAreaCode;
    $("#grid_list").html("");
    $("#grid_count").text("共 0个网格");
    $.ajax({
        type: 'post',
        url: url,
        dataType: 'json',
        success: function (result) {
            var html = "";
            if(result != null && result.length >0){
                $("#grid_count").text("共 "+result.length+"个网格");
                for(var i=0; i<result.length; i++){
                    html += "<li><input type=\"checkbox\" name=\"grid_name\" value=\""+result[i].id+"\" class=\"grid_checkbox\" onclick=\"getGridsAndShow();\"><p>"+result[i].name+"</p></li>";
                }
                $("#grid_list").html(html);
            }
        }

    });
}


/**
 * 打开网格区域详情页面
 * @param gridId	网格id
 * @param name	网格名称
 * @param x		实体中心坐标x
 * @param y		实体中心坐标y
 */
function gridviewDetails(gridId,dataAreaName,name,x,y){
	
	layer.config({
        skin: 'gridPopups'  //自定义弹出层样式
    });
	
	parent.layer.open({
        type: 2,
        title: dataAreaName + "-" + name,
        shadeClose: true,//打开遮蔽
        shade: 0.3,
        maxmin: false, //开启最大化最小化按钮
        area: ["570px", "550px"],
        content: "/gis/getGridDetail?gridId="+gridId+"&name="+name+"&x="+x+"&y="+y
    });
}


//根据行政区划编码显示网格
function showGrids(){
    var dataAreaCode = $("#dataAreaCode").val();
    var dataAreaName = $("#dataAreaName").val();
    var url = '/gis/findGridByCode?dataAreaCode=' + dataAreaCode;
    $.ajax({
        type: 'post',
        url: url,
        dataType: 'json',
        success: function (result) {
            drawGrid(result,dataAreaCode,dataAreaName)
        }
    });
}

//根据选择获取并显示网格
function getGridsAndShow(){
    popomap.removeAllPop();
    var dataAreaCode = $("#dataAreaCode").val();
    var dataAreaName = $("#dataAreaName").val();
    var selectValue = "";
    var getSelectValue = $("input[name='grid_name']:checked").each(function(j) {
        if (j >= 0) {
            selectValue += $(this).val() + ","
        }
    });
    if(selectValue.length>1) {
        $.get("/gis/gridManagement_findGridByIds?ids="+selectValue,function(data,status) {
            if (data) {
                drawGrid(data,dataAreaCode,dataAreaName);
            }
        })
    }
}
//画网格
function drawGrid(line,orgCode,orgName){
    var colorCode='#ff3350';
    if(orgCode.length>9){
        var frist =orgCode.substring(8,12);
        var val = parseInt(frist)*13579;
        colorCode='#'+val.toString(16);
    }
    for (var i = 0; i < line.length; i++) {
        var lid = line[i].id;
        var start_x, start_y;
        if (line[i].xs.indexOf(",") != -1) {
            start_x = line[i].xs.split(",")[0];
            start_y = line[i].ys.split(",")[0];
        } else {
            start_x = line[i].xs;
            start_y = line[i].ys;
        }

        var o = {
            id: lid,
            closed: true,
            x: line[i].x,
            y: line[i].y,
            xs: line[i].xs,
            ys: line[i].ys,
            strokeweight: line[i].strokeWeight,
            strokecolor: line[i].strokeColor,
            fillcolor: line[i].fillColor,
            autoZoom: true,
            endarrow: 'classic'
        };
        var accountid = '';
        //画网格员
        if (typeof(line[i].gridPersonses) != "undefined" && line[i].gridPersonses.length > 0) {
            drawPoint(line[i].gridPersonses, lid);
            accountid = line[i].gridPersonses[0].accountId;
        }
        popomap.drawLine(o);

        //      var href ='#',callback='';
//        if (("href" in arr[i])&&line[i].href) { //外部链接
//			href = line[i].href+' target="_blank" ';
//		}
//		if(("callback" in arr[i])&&line[i].callback)	//回调函数
//			callback = line[i].callback+'';
        var htm = '<div onmousemove="this.parentNode.style.zIndex=200" onmouseout="this.parentNode.style.zIndex=100" style="border: 1px solid #BFBCBC;padding: 3px 3px 3px 3px;border-radius: 3px;box-shadow: 0 3px 5px rgba(160, 160, 161, 0.6);background-color:'+colorCode
            +'"><a href="#" style="color:#fff;text-decoration: none;"><span style="box-sizing: border-box;white-space:nowrap;" onclick="gridviewDetails(\'' + line[i].id + '\',\'' + orgName + '\',\'' + line[i].name + '\','+line[i].x+','+line[i].y+');">'+ line[i].name
            + '</span></a></div>';

        //画网格名称
        popomap.createPop('pop_wangge_' +lid,htm,line[i].x,line[i].y,0,-25,100);
        if(popomap.getZoom()>4&&mapType==1){
            var center = new YMAP.LonLat(line[i].x, line[i].y);
            popomap.getMap().setCenter(center, 3, false, false);
        }else{
            popomap.go2xy(line[i].x,line[i].y );
        }

    }
}


//视频通话
function showVideoCall(){
    parent.layer.open({
        type: 2,
        id:"map_showVideoCall",
        title: "视频通话",
        anim: 3,
        shadeClose: false,//打开遮蔽
       // offset: ['80px', '4%'],
        shade: 0,
        //move: false,
        // end: function(){
        //     top.popomap.removeAllPop();
        // },
        maxmin: true, //开启最大化最小化按钮
        area: ['80%','80%'],
        content: "/gis/showVideoCall"
    });
}


