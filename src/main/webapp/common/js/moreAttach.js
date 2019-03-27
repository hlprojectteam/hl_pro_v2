//----------------------------------------视频上传---------------------------------------
//var photoSuffix = "bmp,jpg,png,jpeg,gif,JPG,BMP,PNG,JPEG,GIF";
var $ = jQuery,
$list1 = $('#thelistFile1'),
$btn1 = $('#ctlBtn1'),
state1 = 'pending',
uploaderFile1;

var $list2 = $('#thelistFile2'),
$btn2 = $('#ctlBtn2'),
state2 = 'pending',
uploaderFile2;

var $list3 = $('#thelistFile3'),
$btn3 = $('#ctlBtn3'),
state3 = 'pending',
uploaderFile3;

jQuery(function() {
	/**********************uploaderFile1*********************************/
    uploaderFile1 = WebUploader.create({
        // 不压缩image
        resize: false,
        // swf文件路径
        swf: '/common/plugins/webUploader/Uploader.swf',
        // 文件接收服务端。
        server: '/attach_upload',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#pickerFile1',
        //上传文件数量
        fileNumLimit:1
    });
   
    // 当有文件添加进来的时候
    uploaderFile1.on( 'fileQueued', function( file ) {
        $list1.append( '<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<button class="state" onclick="delMore(\'' + file.id + '\',1)">移除队列</button>' +
        '</div>' );
    });

    // 文件上传过程中创建进度条实时显示。
    uploaderFile1.on( 'uploadProgress', function( file, percentage ) {
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

    uploaderFile1.on( 'uploadSuccess', function( file ) {
        $( '#'+file.id ).find('p.state').text('已上传');
    });

    uploaderFile1.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('上传出错');
    });

    uploaderFile1.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').fadeOut();
    });

    uploaderFile1.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
            state1 = 'uploading';
        } else if ( type === 'stopUpload' ) {
            state1 = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state1 = 'done';
        }

        if ( state1 === 'uploading' ) {
            $btn1.text('暂停上传');
        } else {
            $btn1.text('开始上传');
        }
    });

    $btn1.on( 'click', function() {
        if ( state1 === 'uploading' ) {
            uploaderFile1.stop();
        } else {
            uploaderFile1.upload();
        }
    });
    
    /**********************uploaderFile2*********************************/
    uploaderFile2 = WebUploader.create({
        // 不压缩image
        resize: false,
        // swf文件路径
        swf: '/common/plugins/webUploader/Uploader.swf',
        // 文件接收服务端。
        server: '/attach_upload',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#pickerFile2',
        //上传文件数量
        fileNumLimit:1
    });
   
    // 当有文件添加进来的时候
    uploaderFile2.on( 'fileQueued', function( file ) {
        $list2.append( '<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<button class="state" onclick="delMore(\'' + file.id + '\',2)">移除队列</button>' +
        '</div>' );
    });

    // 文件上传过程中创建进度条实时显示。
    uploaderFile2.on( 'uploadProgress', function( file, percentage ) {
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

    uploaderFile2.on( 'uploadSuccess', function( file ) {
        $( '#'+file.id ).find('p.state').text('已上传');
    });

    uploaderFile2.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('上传出错');
    });

    uploaderFile2.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').fadeOut();
    });

    uploaderFile2.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
            state2 = 'uploading';
        } else if ( type === 'stopUpload' ) {
            state2 = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state2 = 'done';
        }

        if ( state2 === 'uploading' ) {
            $btn2.text('暂停上传');
        } else {
            $btn2.text('开始上传');
        }
    });

    $btn2.on( 'click', function() {
        if ( state2 === 'uploading' ) {
            uploaderFile2.stop();
        } else {
            uploaderFile2.upload();
        }
    });
    
    /**********************uploaderFile3*********************************/
    uploaderFile3 = WebUploader.create({
        // 不压缩image
        resize: false,
        // swf文件路径
        swf: '/common/plugins/webUploader/Uploader.swf',
        // 文件接收服务端。
        server: '/attach_upload',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#pickerFile3',
        //上传文件数量
        fileNumLimit:1
    });
   
    // 当有文件添加进来的时候
    uploaderFile3.on( 'fileQueued', function( file ) {
        $list3.append( '<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<button class="state" onclick="delMore(\'' + file.id + '\',3)">移除队列</button>' +
        '</div>' );
    });

    // 文件上传过程中创建进度条实时显示。
    uploaderFile3.on( 'uploadProgress', function( file, percentage ) {
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

    uploaderFile3.on( 'uploadSuccess', function( file ) {
        $( '#'+file.id ).find('p.state').text('已上传');
    });

    uploaderFile3.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('上传出错');
    });

    uploaderFile3.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').fadeOut();
    });

    uploaderFile3.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
            state3 = 'uploading';
        } else if ( type === 'stopUpload' ) {
            state3 = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state3 = 'done';
        }

        if ( state3 === 'uploading' ) {
            $btn3.text('暂停上传');
        } else {
            $btn3.text('开始上传');
        }
    });

    $btn3.on( 'click', function() {
        if ( state3 === 'uploading' ) {
            uploaderFile3.stop();
        } else {
            uploaderFile3.upload();
        }
    });
});


//加载多附件列表
function queryMoreList(type){
	var id = $("#id").val();
	if(id!=null&&id!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			contentType:'application/x-download;charset=utf-8',
			url: '/queryAttachListByType?formId='+$("#id").val() +'&type='+type,
			success : function(data){
				var html = "";
				for (var i = 0; i < data.attachList.length; i++) {
					html+="<tr>";
					html+="<td style='text-align: center;'>"+(i+1)+"</td>";
					html+="<td style='text-align: center;'>"+data.attachList[i].fileName+"</td>";//名称
					html+="<td style='text-align: center;'>"+renderSize(data.attachList[i].size)+"</td>";//文件大小
					html+="<td style='text-align: center;'>"+chanegSource(data.attachList[i].source)+"</td>";//来源
					html+="<td style='text-align: center;'>"+changeDate(data.attachList[i].createTime.time)+"</td>";//上传时间
					html+="<td style='text-align: center;'>";//操作
					html+="<a style='cursor:pointer' name='downloadAttach' id='downloadAttach' href='/attach_download?attachId="+data.attachList[i].id+"'><i class='fa fa-download'></i></a>&nbsp;&nbsp;";//下载
					if(photoSuffix.indexOf(data.attachList[i].suffix)>-1){
						html+="<a style='cursor:pointer' name='seeAttach' id='seeAttach' title='"+data.attachList[i].fileName+"' data-gallery='' href='"+data.attachList[i].pathUpload+"'><i class='fa fa-photo'></i></a>&nbsp;&nbsp";
					}
					html+="<a style='cursor:pointer' name='delAttach' id='delAttach' href='javaScript:void(0)' onclick='on_delAttach(this,\""+data.attachList[i].id+"\")'><i class='fa fa-close'></i></a>";//删除
					html+="</td>";
					html+="</td>";
					html+="</tr>";
				}
				$("#attachTableMore").append(html);
				$("#attachTableMore").after("<div id='blueimp-gallery' class='blueimp-gallery'><div class='slides'></div><h3 class='title'>"+
                "</h3><a class='prev'>‹</a><a class='next'>›</a><a class='close'>×</a><a class='play-pause'></a><ol class='indicator'></ol></div>");
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}

function delMore(id,index){
	switch (index) {
	case 1:
		uploaderFile1.removeFile(id);
		$("#"+id).remove();
		break;
	case 2:
		uploaderFile2.removeFile(id);
		$("#"+id).remove();
		break;
	case 3:
		uploaderFile3.removeFile(id);
		$("#"+id).remove();
		break;
	default:
		break;
	}
}