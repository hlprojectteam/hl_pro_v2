//----------------------------------------文件上传---------------------------------------
var photoSuffix = "bmp,jpg,png,jpeg,gif,JPG,BMP,PNG,JPEG,GIF";
var $ = jQuery,
//$list = $('#thelist'),
$btn = $('#ctlBtn'),
state = 'pending',
uploaderFile;
jQuery(function() {
	//queryAttach();
    uploaderFile = WebUploader.create({
        // 不压缩image
        resize: true,
        // swf文件路径
        swf: '/common/plugins/webUploader/Uploader.swf',
        // 文件接收服务端。
        server: '/attach_upload',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#pickerFile'
    });
    // 当有文件添加进来的时候
    uploaderFile.on( 'fileQueued', function( file ) {
    	$('#thelist').append( '<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<p class="state">等待上传...</p>' +
        '</div>' );
    });

    // 文件上传过程中创建进度条实时显示。
    uploaderFile.on( 'uploadProgress', function( file, percentage ) {
        var $li = $( '#'+file.id ),
            $percent = $li.find('.progress .progress-bar');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<div class="progress progress-striped active">' +
              '<div class="progress-bar" role="progressbar" style="width: 0%">' +
              '</div>' +
            '</div>').appendTo( $li ).find('.progress-bar');
        }

        $li.find('p.state').text('上传中');

        $percent.css( 'width', percentage * 100 + '%' );
    });

    uploaderFile.on( 'uploadSuccess', function( file ) {
        $( '#'+file.id ).find('p.state').text('已上传');
    });

    uploaderFile.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('上传出错');
    });

    uploaderFile.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').fadeOut();
    });

    uploaderFile.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
            state = 'uploading';
        } else if ( type === 'stopUpload' ) {
            state = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state = 'done';
        }

        if ( state === 'uploading' ) {
            $btn.text('暂停上传');
        } else {
            $btn.text('开始上传');
        }
    });

    $btn.on( 'click', function() {
        if ( state === 'uploading' ) {
            uploaderFile.stop();
        } else {
            uploaderFile.upload();
        }
    });
});

//获得附件
function queryAttach(){
	var id = $("#id").val();
	if(id!=null&&id!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			contentType:'application/json;charset=utf-8',
			url: '/queryAttachList?formId='+id,
			success : function(data){
				var html = "";
				/*var photoHtml = "";*/
				for (var i = 0; i < data.attachList.length; i++) {
					html+="<tr>";
					html+="<td style='text-align: center;'>"+(i+1)+"</td>";
					html+="<td style='text-align: center;'>"+data.attachList[i].fileName+"</td>";//名称
					html+="<td style='text-align: center;'>"+renderSize(data.attachList[i].size)+"</td>";//文件大小
					html+="<td style='text-align: center;'>"+changeDataDictByKey("uploadSource",data.attachList[i].source)+"</td>";//来源
					html+="<td style='text-align: center;'>"+changeDate(data.attachList[i].createTime.time)+"</td>";//上传时间
					html+="<td style='text-align: center;'>";//操作
					html+="<a style='cursor:pointer' name='downloadAttach' id='downloadAttach' href='/attach_download?attachId="+data.attachList[i].id+"'><i class='fa fa-download'></i></a>&nbsp;&nbsp;";//下载
					if(photoSuffix.indexOf(data.attachList[i].suffix)>-1){	//如果文件的后缀表明该文件是图片格式的话,则显示图片标记,并可通过点击图片标记全屏显示图片
						/*photoHtml += "<a name='show_photosA' data-gallery='' title='"+data.attachList[i].fileName+"' href='"+data.attachList[i].pathUpload+"'/>";
						html+="<a style='cursor:pointer' name='seeAttach' id='seeAttach' title='"+data.attachList[i].fileName+"' href='javaScript:showPhoto(\""+data.attachList[i].pathUpload+"\")'><i class='fa fa-photo'></i></a>&nbsp;&nbsp";*/
						html+="<a style='cursor:pointer' name='seeAttach' id='seeAttach' title='"+data.attachList[i].fileName+"' data-gallery='' href='"+data.attachList[i].pathUpload+"'><i class='fa fa-photo'></i></a>&nbsp;&nbsp";					}
					if($("#noDelPhotoSign").length<1)//是否显示删除
						html+="<a style='cursor:pointer' name='delAttach' id='delAttach' href='javaScript:void(0)' onclick='on_delAttach(this,\""+data.attachList[i].id+"\")'><i class='fa fa-close'></i></a>";//删除
					html+="</td>";
					html+="</td>";
					html+="</tr>";
				}
				$("#attachTable").append(html);
				//在框架页面全屏显示图片
				/*window.parent.$("#show_photoDiv").html(photoHtml);*/
				//页面内显示图片	 	class属性添加  blueimp-gallery-controls , 则会显示图片导航。
				$("#attachTable").after("<div id='blueimp-gallery' class='blueimp-gallery '><div class='slides'></div><h3 class='title'>"+
                "</h3><a class='prev'>‹</a><a class='next'>›</a><a class='close'>×</a><a class='play-pause'></a><ol class='indicator'></ol></div>");
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}

//获得附件
function queryAttachByType(type){
	var id = $("#id").val();
	if(id!=null&&id!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			contentType:'application/json;charset=utf-8',
			url: '/queryAttachListByType?formId='+id +'&type='+type,
			success : function(data){
				var html = "";
				for (var i = 0; i < data.attachList.length; i++) {
					/*document.getElementById("pickerFile").style.display="none"; */
					html+="<tr>";
					html+="<td style='text-align: center;'>"+(i+1)+"</td>";
					html+="<td style='text-align: center;'>"+data.attachList[i].fileName+"</td>";//名称
					html+="<td style='text-align: center;'>"+renderSize(data.attachList[i].size)+"</td>";//文件大小
					html+="<td style='text-align: center;'>"+changeDataDictByKey("uploadSource",data.attachList[i].source)+"</td>";//来源
					html+="<td style='text-align: center;'>"+changeDate(data.attachList[i].createTime.time)+"</td>";//上传时间
					html+="<td style='text-align: center;'>";//操作
					html+="<a style='cursor:pointer' name='downloadAttach' id='downloadAttach' href='/attach_download?attachId="+data.attachList[i].id+"'><i class='fa fa-download'></i></a>&nbsp;&nbsp;";//下载
					if(photoSuffix.indexOf(data.attachList[i].suffix)>-1){	//如果文件的后缀表明该文件是图片格式的话,则显示图片标记,并可通过点击图片标记全屏显示图片
						html+="<a style='cursor:pointer' name='seeAttach' id='seeAttach' title='"+data.attachList[i].fileName+"' data-gallery='' href='"+data.attachList[i].pathUpload+"'><i class='fa fa-photo'></i></a>&nbsp;&nbsp";
					}
					if($("#noDelPhotoSign").length<1)//是否显示删除
						html+="<a style='cursor:pointer' name='delAttach' id='delAttach' href='javaScript:void(0)' onclick='on_delAttach(this,\""+data.attachList[i].id+"\")'><i class='fa fa-close'></i></a>";//删除
					html+="</td>";
					html+="</td>";
					html+="</tr>";
				}
				$("#attachTable").append(html);
				//页面内显示图片	 	class属性添加  blueimp-gallery-controls , 则会显示图片导航。
				$("#attachTable").after("<div id='blueimp-gallery' class='blueimp-gallery '><div class='slides'></div><h3 class='title'>"+
                "</h3><a class='prev'>‹</a><a class='next'>›</a><a class='close'>×</a><a class='play-pause'></a><ol class='indicator'></ol></div>");
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}

//点击全屏显示图片
function showPhoto(url){
	window.parent.$("a[name='show_photosA']").each(function(){
		if($(this).attr("href")==url){
			$(this)[0].click();
		}
	});
}
//---------------------------------------------------------------

//删除附件
function on_delAttach(obj,attachId){
	parent.layer.confirm("确定删除附件吗？", {
		btn: ["确定","取消"] //按钮
	}, function(){
		autoMsg("删除成功！",1);
		$.ajax({
			type:'post',
			async:false,
			dataType : 'json',
			contentType:"application/json;charset=utf-8",
			url: '/attach_delete?ids='+attachId,
			success : function(data){
				if(data.result){
					$(obj).parent().parent().remove();  //删除当前行
					//刷新序号
					var i = 0;
					$("#attachTable tr").each(function(){
						if($(this).children().get(0).innerText!="序号"){
							$(this).children().get(0).innerText=i+1;
							i++;
						}
					});
					autoMsg("删除成功！",1);
				}
			},
			error : function(result){
				autoMsg("系统出错",2);	
			}
		});
	});
}

//---------------工具方法--------------------------------------------------------------
/*附件大小格式处理*/
function renderSize(value, p, record){
  if(null==value||value==''){
    return "0 Bytes";
  }
  var unitArr = new Array("Bytes","KB","MB","GB","TB","PB","EB","ZB","YB");
  var index=0;

  var srcsize = parseFloat(value);
  var quotient = srcsize;
  while(quotient>1024){
    index +=1;
   quotient=quotient/1024;
  }
  return roundFun(quotient,2)+" "+unitArr[index];
}

/*
四舍五入保留小数位数
numberRound 被处理的数
roundDigit  保留几位小数位
*/
function  roundFun(numberRound,roundDigit){   //处理金额 -by hailang  
	if(numberRound>=0) {   
		var   tempNumber   =   parseInt((numberRound * Math.pow(10,roundDigit)+0.5))/Math.pow(10,roundDigit);   
        return   tempNumber;   
    }else{   
        numberRound1=-numberRound;
        var  tempNumber   =   parseInt((numberRound1 * Math.pow(10,roundDigit)+0.5))/Math.pow(10,roundDigit);   
        return   -tempNumber;   
    }  
}

//时间格式化
function changeDate(time){
 	var date = new Date();
 	date.setTime(time);  //value通过截取字符串只取数字。
 	var hh = "0"+date.getHours();
 	var mm = "0"+date.getMinutes();
 	var dd = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+hh.substring(hh.length-2,hh.length)+":"+mm.substring(mm.length-2,mm.length);
 	return dd;
}