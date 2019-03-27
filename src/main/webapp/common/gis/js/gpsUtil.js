/**
 * 实时定位刷新及轨迹
 *
 */

/**
 * 轨迹信息对象
 * @param id 轨迹ID
 * @param title 轨迹名称
 * @param x 起始点X坐标
 * @param y 起始点Y坐标
 * @param xs X坐标集
 * @param ys Y坐标集
 * @param ct 定位时间集
 * @param show 当前显示状态
 * @param isRecord 当前记录状态
 */
function trackCoor(id,title,x,y,xs,ys,cts,show,isRecord){
    this.id=id;//暂时使用“设备号+时间”
    this.title=title;
    this.x=x;
    this.y=y;
    this.xs=xs;
    this.ys=ys;
    this.cts=cts;
    this.show=show;//显示状态is
    this.isRecord=isRecord;

}

var gps_position = (function(){
    //定位缓存
    var deviceIds=new Array();
    var dataCodes = new Array();
    var titles = new Array();
    var urls = new Array();
    var deviceType='WORKER_CARD';
    var timmer;
    var idIndex = -1;
    //轨迹缓存
    var trackIds=new Array();
    var trackObjs=new Array();
    //轨迹回放
    var playObj_;
    var step_;
    var popp_;
    var playtimer_;
    var play_xs;
    var play_ys;
    var cts_;//定位创建时间
    if(!Array.indexOf){
        Array.prototype.indexOf=function(item){
            for(var i=0;i<this.length;i++){
                if(this[i]==item){
                    return i;
                }
            }
            return -1;
        }
    }
    return {
        getDeviceType:function(){
            return deviceType;
        },
        setDeviceType:function(dtype){
            deviceType=dtype;
        },
        /**
         * 获取GPS设备集
         * @returns {string}
         */
        getDeviceIds: function () {
            return deviceIds.toString();
        },
        getDataCodes: function () {
            return dataCodes.toString();
        },
        /**
         * 新增GPS设备号
         * @param addid
         */
        addDeviceId: function (addid, dataCode, title, url) {
            if (deviceIds.indexOf(addid) == -1) {
                deviceIds.push(addid);
                dataCodes.push(dataCode);
                titles.push(title);
                urls.push(url);
            }
        },
        /**
         * 从数组中移除GPS设备号
         * @param removeid
         */
        removeDeviceId: function (removeid) {
            idIndex = deviceIds.indexOf(removeid);
            if (idIndex > -1) {
                deviceIds[idIndex] = deviceIds[deviceIds.length - 1];
                dataCodes[idIndex] = dataCodes[dataCodes.length - 1];
                titles[idIndex] = titles[titles.length - 1];
                urls[idIndex] = urls[urls.length - 1];
                deviceIds.pop();
                dataCodes.pop();
                titles.pop();
                urls.pop();
            }
        },
        /**
         * 清除定位点
         */
        removeAllPosition:function(){
            if(deviceIds.length>0){
                for(var i=0;i<deviceIds.length;i++){
                    top.popomap.removePop(deviceIds[i]);
                }
            }
        },
        /**
         * 清空GPS设备号
         */
        removeAllDeviceId: function () {
            deviceIds = new Array();
            dataCodes = new Array();
            titles = new Array();
            urls = new Array();
        },
        /**
         * 新增轨迹对象
         */
        addTrack:function(id,trackCoor){
            if(trackIds.indexOf(id) == -1){
                trackIds.push(id);
                trackObjs.push(trackCoor);
            }
        },
        /**
         * 根据轨迹id获取轨迹对象信息
         * @param trackId 轨迹ID
         * @returns 轨迹对象
         */
        getTrackObj:function(trackId){
            var i = trackIds.indexOf(trackId);
            if(i<0){
                return null;
            }
            return trackObjs[i];
        },
        /**
         * 根据轨迹ID删除轨迹缓存
         * @param trackId 轨迹ID
         */
        removeTrack:function(trackId){
            var n = trackIds.indexOf(trackId);
            if(n>-1){
                //trackIds.splice(n,1);
                //trackObjs.splice(n,1);
                trackIds[n]=trackIds[trackIds.length-1];
                trackObjs[n]=trackObjs[trackObjs.length-1];
                trackIds.pop();
                trackObjs.pop();
            }
        },
        /**
         * 根据设备号删除轨迹及轨迹缓存
         * @param deviceCode
         */
        removeTrackByDeviceCode:function(deviceCode){
            var deltrack=new Array();
            clearTimeout(playtimer_);
            for(var j=0;j<trackIds.length;j++){
                if(trackIds[j].indexOf(deviceCode)>-1){
                    var obj = trackObjs[j];
                    if(obj.x!=""&&obj.show){
                        //console.log("删除线！"+obj.id);
                        popomap.removePop("pop_"+obj.id);
                        popomap.destroyG("linetrack_"+obj.id);
                    }
                    //停止轨迹回放timer
                    //console.log(playObj_.id+";;"+obj.id);
                    if(playObj_!=null && playObj_.id==obj.id){
                        popomap.removePop("play_"+obj.id);
                    }
                    deltrack.push(obj.id);
                }
            }
            for(var k=0;k<deltrack.length;k++){
                gps_position.removeTrack(deltrack[k]);
            }
            deltrack=[];
        },
        /**
         * 清除页面所有轨迹
         */
        removeAllTrackLine:function(){
            for(var i=0;i<trackIds.length;i++){
                var obj = trackObjs[i];
                if(obj.show){
                    obj.show=false;
                    popomap.removePop("pop_"+obj.id);
                    popomap.destroyG("linetrack_"+obj.id);
                }
                //停止轨迹回放timer
                if(playObj_ && playtimer_ && playObj_.id==obj.id){
                    clearTimeout(playtimer_);
                    popomap.removePop("play_"+obj.id);
                }
            }
        },
        /**
         * 删除轨迹页面上轨迹记录
         * @param trackId 轨迹ID
         */
        delTrackRecordById:function(trackId){
            var i = trackIds.indexOf(trackId);
            if(i>-1){
                var obj = trackObjs[i];
                obj.isRecord = false;
            }
            //停止轨迹回放timer
            if(playObj_!=null && playObj_.id==trackId){
                clearTimeout(playtimer_);
                popomap.removePop("play_"+trackId);
            }
        },
        /**
         * 清除所有轨迹信息缓存
         */
        removeAllTrackObj:function(){
            trackIds=new Array();
            trackObjs=new Array();
        },
        /**
         * 刷新定位
         * @param gpsData
         */
        refreshPosition: function (gpsData) {
            var indexurl = deviceIds.indexOf(gpsData.deviceId);
            var jurl = urls[indexurl];
            gpsData.ename = titles[indexurl];
            var dataCode = dataCodes[indexurl];
            //首先清除原先gps标注
            var html = '';
            popomap.removePop(gpsData.deviceId);
            //console.log(gpsData);
            if (gpsData.appCode != null && gpsData.appCode != '') {
            	 if (jurl == '' || jurl == null || jurl == 'undefined') {
                     html = '<div><div style="width:100px;"><font size="4" color="red"><strong>' + gpsData.ename + '</strong></font></div><div><a href="javascript:showGpsDeviceInfo(\'' + gpsData.deviceId + '\',\'' + gpsData.appCode + '\');">';
                 } else {
                     html = '<div><div style="width:100px;"><font size="4" color="red"><a class="color" onclick="viewDetails2(\'' + jurl + '\',\'\',\'' + gpsData.ename + '\',700,400)">' + gpsData.ename + '</a></font></div><div><a href="javascript:showGpsDeviceInfo(\'' + gpsData.deviceId + '\',\'' + gpsData.appCode + '\');">';
                 }
                var ImgObj = new Image(); //判断图片是否存在
                ImgObj.src = '/gis/images/front/index/gpsuser/' + gpsData.appCode + '.png';
                if (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {
                    html = '<div><div style="width:100px;"><font size="4" color="red"><strong>' + gpsData.ename + '</strong></font></div><div><a href="javascript:showGpsDeviceInfo(\'' + gpsData.deviceId + '\',\'' + gpsData.appCode + '\');"><img src="/gis/images/front/index/gpsuser/' + gpsData.appCode + '.png"/></a></div></div>';
                } else if (gpsData.appCode == 'WORKER_CARD') {
                    if (dataCode && dataCode.indexOf('zhonghe') > -1) {//综合执法车辆
                        if (gpsData.timeOut > 180000) {
                            html = html + '<img src="/gis/images/front/layerlabel/gps/car_32_gray.png"/></a></div></div>';
                        } else {
                            html = html + '<img src="/gis/images/front/layerlabel/gps/car_32_red.png"/></a></div></div>';
                        }
                    } else if ( dataCode && dataCode.indexOf('minjing') > -1) {//民警定位
                        if (gpsData.timeOut > 180000) {
                            html = html + '<img src="/gis/images/front/index/offline.png"/></a></div></div>';
                        } else {
                            html = html + '<img src="/gis/images/front/index/online.png"/></a></div></div>';
                        }
                    } else if(dataCode && dataCode.indexOf('wgygj') > -1){
                    	if (gpsData.timeOut > 180000) {
                            html = html + '<img src="/gis/images/front/index/offline.png"/></a></div></div>';
                        } else {
                            html = html + '<img src="/gis/images/front/index/online.png"/></a></div></div>';
                        }
                    }else {
                        if (gpsData.timeOut > 180000) {//3分钟，环卫人员
                            if (gpsData.isInPolygon == 'true') {//是否在面内
                                //console.log("png");"src/main/webapp/gis/images/front/layerlabel/gps/car_32_gray.png"
                                html = html + '<img src="/gis/images/front/index/offline.png"/></a></div></div>';
                            } else {//不在面内则闪烁
                                //console.log("gif");
                                html = html + '<img src="/gis/images/front/index/offline.gif"/></a></div></div>';
                            }
                        } else {
                            if (gpsData.isInPolygon == 'true') {//是否在面内
                                html = html + '<img src="/gis/images/front/index/online.png"/></a></div></div>';
                            } else {//不在面内则闪烁
                                html = html + '<img src="/gis/images/front/index/online.gif"/></a></div></div>';
                            }
                        }
                    }
                } else if(gpsData.appCode == 'wgyGPSData'){
                	
                    if(dataCode && dataCode.indexOf('zfry') > -1){//综合执法人员
                        var zfryid=dataCode.substring(4);
//                        html = '<div><div style="width:100px;"><a href="javascript:top.viewZFDDetails(\'lawEnforcementOfficials_detailNoMap.html?ids=\',\''+zfryid+'\');"><font size="8" color="#FFFF00">'+gpsData.ename+'</font></div>';
                        if (gpsData.timeOut > 180000) {
//                            html = html + '<img src="/gis/images/front/index/offline.png"/></a></div></div>';
                            html = html+'<img src=\"/gis/images/front/layerlabel/zhifa/'+zfryid+'-1.png\"/></a></div>';
                        } else {
                        	html = html+'<img src=\"/gis/images/front/layerlabel/zhifa/'+zfryid+'.png\"/></a></div>';
                        }
                    }else if(dataCode && dataCode.indexOf("wgygj") > -1){
                    	var wgygj=dataCode.substring(5);
                    	var sex = gpsData.sex;
                    	var img = "gridPersonMan.png";
                    	if(!sex){
                    		img = "gridPersonWoman.png";
                    	}
                        if(gpsData.timeOut > 180000){
//					     	html = '<div><div style="width:100px;"><a href="javascript:top.viewWGYDetails(\'gridPerson_detailNoMap.html?ids=\',\''+wgygj+'\',\'网格员\',700,250);"><font size="8" color="#FFFF00">'+gpsData.deviceId+gpsData.ename+'</font></div>';
					    	html = html+'<img src=\"/gis/images/front/gridperson/offLine.png\"/></a></div>';
						}else{
//					    	html = '<div><div style="width:100px;"><a href="javascript:top.viewWGYDetails(\'gridPerson_detailNoMap.html?ids=\',\''+wgygj+'\',\'网格员\',700,250);"><font size="8" color="#FF0000">'+gpsData.deviceId+gpsData.ename+'</font></div>';
					    	html = html+'<img src=\"/gis/images/front/gridperson/'+img+'\"/></a></div>';
						}
//				    	 html = html+'<img src=\"/gis/images/front/gridperson/'+wgygj+'.png\"/></a></div>';
                    }
                }else if(gpsData.appCode == 'VEHICLE'){
                	var car=dataCode.substring(7);
                	if (dataCode && dataCode.indexOf('zhonghe') > -1) {//综合执法车辆
                        if (gpsData.timeOut > 180000) {
//                        	html = '<div><div style="width:100px;"><a href="javascript:top.viewWGYDetails(\'gridPerson_detailNoMap.html?ids=\',\''+car+'\',\'网格员\',700,250);"><font size="8" color="#FFFF00">'+gpsData.deviceId+gpsData.ename+'</font></div>';
                            html = html + '<img src="/gis/images/front/layerlabel/gps/car_32_gray.png"/></a></div></div>';
                        } else {
//                        	html = '<div><div style="width:100px;"><a href="javascript:top.viewWGYDetails(\'gridPerson_detailNoMap.html?ids=\',\''+car+'\',\'网格员\',700,250);"><font size="8" color="#FFFF00">'+gpsData.deviceId+gpsData.ename+'</font></div>';
                            html = html + '<img src="/gis/images/front/layerlabel/gps/car_32_red.png"/></a></div></div>';
                        }
                    }else{
                    	
                    }
                }else {
                    html = '<div><div style="width:100px;"><font size="4" color="red"><strong>' + gpsData.ename + '</strong></font></div><div><a href="javascript:showGpsDeviceInfo(\'' + gpsData.deviceId + '\',\'' + gpsData.appCode + '\');"><img src="/gis/images/front/index/offline.png"/></a></div></div>';
                }
            } else {
                html = '<div><div style="width:100px;"><font size="4" color="red"><strong>' + gpsData.ename + '</strong></font></div><div><a href="javascript:showGpsDeviceInfo(\'' + gpsData.deviceId + '\',\'' + gpsData.appCode + '\');"><img src="/gis/images/front/index/GPS.gif"/></a></div></div>';
            }
                            //console.log(gpsData.deviceId+";"+html+";"+gpsData.x+";"+gpsData.y);
            if(deviceIds.indexOf(gpsData.deviceId) > -1){      //连点判断,防止出现undefined错误
            	popomap.createPop(gpsData.deviceId, html, gpsData.x, gpsData.y, -16, -55, 0);
            }
            if(deviceIds.length == 1 && trackIds.length==0){
                top.go2xy(gpsData.x,gpsData.y);
            }
        },


        /**
         * 获取最新的位置信息(多个)
         */
        getLastestPszts: function () {
            //console.log(deviceIds.length + ";" + this.getDeviceIds());
            if (deviceIds.length < 1) {
                return true;
            }
            var url = '/gpsEquipment_getLastGpsDataVos.html';
            var data={"deviceId":this.getDeviceIds(),"dataCode":this.getDataCodes(),"deviceType":deviceType};
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                dataType: 'json',
                sync:true,
                success: function (gpsDatas) {
                    for (var key in gpsDatas) {
                        var ixf =  deviceIds.indexOf(key);
                        gpsDatas[key].url = urls[ixf];
                        gpsDatas[key].ename = titles[ixf];
                        gps_position.refreshPosition(gpsDatas[key]);
                    }
                }
            });
        },
        startTimer: function () {
            if (!timmer) {
                timmer = setInterval("gps_position.getLastestPszts()", 15000);
            }
        },

        stopTimer: function () {
            if (timmer) {
                window.clearInterval(timmer);
            }
        },
        /**
         * 获取最新的位置信息(单个)
         */
        getLastestPszt: function (deviceId, dataCode, titleName, jurl, checked) {
            //加入缓存
            if (checked) {
                this.addDeviceId(deviceId, dataCode, titleName, jurl);
            } else {
                this.removeDeviceId(deviceId);
                //首先清除原先gps标注
                popomap.removePop(deviceId);
                return true;
            }
            var url = '/gpsEquipment_getNewGpsData.html?deviceId=' + deviceId + '&dataCode=' + dataCode+'&deviceType='+deviceType;
            $.ajax({
                type: 'post',
                url: url,
                dataType: 'json',
                sync:true,
                success: function (gpsData) {
                    gps_position.refreshPosition(gpsData);
                    top.go2xy(gpsData.x,gpsData.y);
                }
            });
        },

        /**
         * GPS轨迹选择时间窗口
         * @param deviceCode 设备编号
         * @param deviceType 设备类型：（手机app:wgyGPSData，车载北斗：VEHICLE，电子工牌：WORKER_CARD）
         * @param title 轨迹名称
         * @param img 轨迹起始点坐标图片，默认为红色小旗
         */
        gpsTrack:function(deviceCode, deviceType,title,img){
            var url = 'gpsEquipment_goGpsTrack.html?deviceCode='+deviceCode+'&deviceType='+deviceType+'&title='+title+'&img='+img;
            var dod=$.dialog({
                title : title+" 轨迹",
                content: 'url:' + url,
                width: 700,
                height: 300,
                zIndex: 19999,
                left:'78%',
                close: function(){
                    //console.log("删除回调："+deviceCode);
                    gps_position.removeTrackByDeviceCode(deviceCode);
                }
            });
        },
        /**
         * 获取随机颜色
         * @returns 颜色值
         */
        randomColor:function() {
        //红色、黄色、橙色、蓝色、绿色、紫色
        var randString = "FF0000,FFFF00,FF6100,0000FF,00FF00,A020F0";   //原始数据
        var randArray = randString.split(",");           //转化为数组
        var rand = randArray[Math.round(Math.random()*(randArray.length-1))];  //随机抽取一个值
        return rand;
        },
        /**
         * 生成轨迹线ID
         * @param deviceCord
         * @param beginTime
         * @param endTime
         * @returns {string}
         */
        createTrackId:function(deviceCord,beginTime,endTime){
            var bt='';
            if(beginTime!=null||beginTime!=""){
                bt=beginTime.replace(/[^0-9]/g,"");
            }
            var et='';
            if(endTime!=null||endTime!=""){
                et=endTime.replace(/[^0-9]/g,"");
            }
            return deviceCord+bt+"_"+et;
        },
        /**
         * 播放样式（暂时未用）
         * @param domObj
         */
        playStyle:function(domObj){
            $(domObj).text("播放轨迹");
        },
        /**
         * 轨迹回放
         */
        playTrack2:function(){
            //console.log(step_);
            popomap.removePop("play_"+playObj_.id);
            //console.log(playtimer_+":"+trackIds.length);
            if(!trackIds || trackIds.length<1 || step_>=play_xs.length){
                //console.log("定时结束！~");
                clearTimeout(playtimer_);
                return;
            }
            //popomap.go2xy(play_xs[step_],play_ys[step_]);
            popp_ = popomap.createPop("play_"+playObj_.id,'<div class="ipoint" style="width:119px"><span style="background-color: snow;color:black">'+cts_[step_]+'</span><br><img _child="point" src="/common/gis/images/front/position_blue.png"/></div>',play_xs[step_],play_ys[step_],-6,-52,101);
            step_++;
            playtimer_= setTimeout("top.gps_position.playTrack2()",900);
        },
        /**
         * 轨迹回放
         * @param trackId 轨迹ID
         */
        playTrack:function(trackId){
            if(playObj_){
                popomap.removePop("play_"+playObj_.id);
            }
            playObj_ = trackObjs[trackIds.indexOf(trackId)];
            if(playObj_.xs==""){
                return false;
            }
            play_xs=playObj_.xs.split(",");
            play_ys=playObj_.ys.split(",");
            cts_=playObj_.cts.split(",");
            step_=0;
            clearTimeout(playtimer_);
            top.gps_position.playTrack2();
        },
        /**
         * 停止回放计时器
         */
        stopPlayTimer: function(){
            if(playtimer_){
                clearTimeout(playtimer_);
            }
        },
        /**
         * 画轨迹线
         * @param id 线条ID
         * @param name 线条名称
         * @param x 起始点X
         * @param y 起始点Y
         * @param xss x轴点集
         * @param yss y轴点集
         * @param strokeWeight 线条粗细
         * @param strokeColor 线条颜色
         */
        myDrawLineOnly:function(id,name,x,y,xss,yss,strokeWeight,strokeColor,img){
            //alert("id="+id+"  name="+name+"   x="+x+"   y="+y+"   xss="+xss+"   yss="+yss+"   strokeWeight="+strokeWeight+"   strokeColor="+strokeColor);
            if(img==null||img==""){
                img="/img/txtRedAw.gif";
            }
            if(x&&y&&xss&&yss){
                var start_x,start_y;
                if(xss.indexOf(",")!=-1){
                    start_x = xss.split(",")[0];
                    start_y = yss.split(",")[0];
                }else{
                    start_x = xss;
                    start_y = yss;
                }
                var o={id:'track_'+id,xs:xss+",",ys:yss+",",strokecolor:strokeColor,strokeweight:strokeWeight,autoZoom:true,endarrow:'classic'}
                // 调用画线功能onmousemove="this.parentNode.style.zIndex=200" onmouseout="this.parentNode.style.zIndex=100"
                popomap.removePop("pop_"+id);
                popomap.destroyG("linetrack_"+id);
                popomap.drawLine(o);
                popomap.createPop(
                    'pop_' + id,
                    '<div class="ibox"><div style="width:217px" class="playTrack_3" onclick="top.gps_position.playTrack(\''+id+'\');"><a href="javascript:void(0)"><span class="playTrack_1" style="color: #fff">轨迹回放</span><span class="playTrack_2">'
                    + name
                    + '</span></a></div><div class="ipoint"><img _child="point" src="'+img+'"/></div></div>',
                    x, y, -5, -40, 111);
                //popomap.go2xy(x,y);
            }
        },
        /**
         * 在缓存中查找轨迹
         * @param trackId
         */
        checkExtTrack:function(trackId){
            if(trackIds.indexOf(trackId) > -1){
                return true;
            }
            return false;
        },
        /**
         * 获取GPS轨迹并显示
         * @param entityId 对象id
         * @param title 轨迹名称
         * @param beginTime 查询起始时间 默认为当天0点开始
         * @param endTime 查询结束时间 默认为到当前时间
         * @param img 轨迹起始点坐标图片，默认为红色小旗
         * @param callback 数据加载完成后回调方法
         */
        showGpsTrack:function(entityId,title,beginTime,endTime,img,callback){
            var url = '/gps/getGpsTrack?entityId=' + entityId;
            if(beginTime != null){
                url = url + '&beginTime=' + beginTime;
            }
            if(endTime != null){
                url = url + '&endTime=' + endTime;
            }
            $.ajax({
                type:'post',
                url:url,
                dataType:'json',
                success:function(result){
                    if(result[0].success){
                        var line = result[0].data;
                        var distatce = result[0].distatce;
                        var xs="",ys="";
                        var x="",y="";
                        var cellX = "";
                        var cellY = "";
                        var preX = "";
                        var preY = "";
                        var cts="";
                        var ct="";
                        //var gpsPonitArray = [];
                        //var gpsPonitArrayX = [];
                        //var gpsPonitArrayY = [];
                        if(line.length>0){
                            for(var i = 0; i < line.length; i++)
                            {
                                cellX = line[i].x;
                                cellY = line[i].y;
                                ct = top.changeDateFormat(line[i].createTime.time);
                                    if(x == ""&& y == "")
                                    {
                                        x = cellX;
                                        y = cellY;
                                        xs = x;
                                        ys = y;
                                        preX=cellX;
                                        preX=cellY;
                                        cts = ct;
                                        ////通过匹配点转换
                                    }
                                    //xs = line[i][0] + "," + xs;
                                    //ys = line[i][1] + "," + ys;

                                if(cellX!=preX||cellY!=preY){
                                    xs = cellX + "," + xs;
                                    ys = cellY + "," + ys;
                                    cts = ct + "," + cts;
                                    preX = cellX;
                                    preY = cellY;
                                }
                            }
                            //画GPS轨迹线
                            //gpsLinePoint.put(deviceCord+type,gpsPonitArray);
                            //将轨迹信息保存进缓存
                            //var trackObj = new trackCoor(entityId,title+" 总距离:"+distatce+"米",x,y,xs,ys,cts,true,true);
                            var trackObj = new trackCoor(entityId,title,x,y,xs,ys,cts,true,true);
                            gps_position.addTrack(entityId,trackObj);
                         //   gps_position.myDrawLineOnly(entityId,title+" 总距离:"+distatce+"米",x,y,xs,ys,5,'#'+gps_position.randomColor(),img);
                            gps_position.myDrawLineOnly(entityId,title,x,y,xs,ys,5,'#'+gps_position.randomColor(),img);
                            popomap.go2xy(x,y);
                            title="";
                        }else{
                            //console.log(title);
                            gps_position.addTrack(tid,new trackCoor(entityId,title,"","","","","",true,true));
                            title = "无定位数据！";
                        }
                        //回调函数
                        if(callback)
                        {
                            callback(entityId,title,beginTime,endTime);
                        }
                    }else{
                        autoMsg("暂无轨迹记录",5);
                    }
                }
            });
        },
        /**
         * 根据轨迹ID查询缓存轨迹信息并显示或跳转
         * @param trackId 轨迹ID
         */
        showGPSTrackById:function(trackId){
            var i = trackIds.indexOf(trackId);
            if(i<0){
                return;
            }
            var obj = trackObjs[i];
            if(!obj.show){
                obj.show=true;
                gps_position.myDrawLineOnly(obj.id,obj.title,obj.x,obj.y,obj.xs,obj.ys,3,'#'+gps_position.randomColor(),obj.img);
            }
            popomap.go2xy(obj.x,obj.y);
        },

        /**
         * 根据ID清除轨迹
         * @param trackId
         */
        hideGPSTrackById:function(trackId){
            var i = trackIds.indexOf(trackId);
            if(i<0){
                return;
            }
            var obj = trackObjs[i];
            obj.show=false;
            popomap.destroyG("linetrack_"+trackId);
            popomap.removePop("pop_"+trackId);
            //停止轨迹回放timer
            if(playObj_!=null && playObj_.id==trackId){
                clearTimeout(playtimer_);
                popomap.removePop("play_"+trackId);
            }
        }
    }
})();


