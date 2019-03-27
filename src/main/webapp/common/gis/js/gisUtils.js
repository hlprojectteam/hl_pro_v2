/**
 * 地图工具js 依赖地图api  请在地图环境下使用
 * zengcong
 * 2018/04/06 20:40
 */


/**
 * 画图像标注
 * @param id 标注id
 * @param imageUrl 图标url
 * @param backgroundColor 标题背景色
 * @param fontColor 标题文字颜色
 * @param title 标题
 * @param callback 标题绑定事件
 * @param x
 * @param y
 * @param xs
 * @param ys
 */
function drawLabel(id,imageUrl,backgroundColor,fontColor,title,x,y,xs,ys,callback) {

    // html = '<div class="imageBox">\
		// 				<div class="imageBox-title" style="background-color:'+backgroundColor+'">\
		// 			 		<a href="'+href+'" '+callback+' class="imageBox-a" style="color:'+fontColor+'">'+arr[i].content+'</a>\
		// 			 	</div>\
		// 			 	<img class="imageBox-icon" src="'+imageUrl+'">\
		// 			 </div>';
    var html = '<div class="imageBox">\
						<div class="imageBox-title" id="title_'+id+'" style="background-color:'+backgroundColor+'">\
					 		<a '+callback+' class="imageBox-a" style="color:'+fontColor+'">'+title+'</a>\
					 	</div>\
					 	<img class="imageBox-icon" id="img_'+id+'" width="25px" height="30px" src="'+imageUrl+'">\
					 </div>';
     popomap.createPop(id ,html,x, y,xs,ys,1200);
}

/**
 * 根据x,y定位
 * @param x,y
 */
function go2label(x,y){
    popomap.go2xy(x,y);
}

/**
 * 根据entityId_定位
 * @param entityId_
 */
function go2labelForEntityId(entityId_){
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
                popomap.go2xy(label.x,label.y);
            }
        }
    });
}
