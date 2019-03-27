/**
 * 只选择一张照片
 */
// ------------增加图片start----------------------------
var uploader;// Web Uploader实例
jQuery(function() {
	var id = $("#id").val();
	if(id!=null&&id!=""){
		getAttach(id);// 获得照片
	}
	var uploaderDiv = $("#uploaderDiv"),// 整个上传区域
	photoList = $('<ul class="filelist"></ul>').appendTo(uploaderDiv.find(".queueList")),
	statusBar = uploaderDiv.find(".statusBar"),  // 状态显示条
    info = statusBar.find(".info"),// 信息提示
    uploadBtn = uploaderDiv.find(".uploadBtn"),// 上传按钮
    placeholder = uploaderDiv.find(".placeholder"),// 选择图片输入区
    // progress = statusBar.find(".progress").hide(),//进度条
	sign = "",// 状态标记
	num = 0,// 文件数量
	sizes = 0;// 文件总大小
	uploader = WebUploader.create({// 初始化Web Uploader
	    auto: false,// 自动上传。
	    threads :10,// 并非上传文件数
	    fileNumLimit:photoNum,// 上传文件数量
        // fileSizeLimit:, 验证文件总大小是否超出限制, 超出则不允许加入队列。
        // fileSingleSizeLimit:, 验证单个文件大小是否超出限制, 超出则不允许加入队列。
	    swf: '/common/plugins/webUploader/Uploader.swf',// swf文件路径
	    pick: {// 选择文件的按钮。可选。内部根据当前运行是创建，可能是input元素，也可能是flash.
            id: "#filePicker",
            label: "选择图片"
        },
	    accept: { // 只允许选择文件，可选。
	        title: 'Images',
	        extensions: 'gif,jpg,jpeg,bmp,png',
	        mimeTypes: 'image/*'
	    },
	    compress: false,// 不压缩上传
	});
  	uploader.addButton({
        id: "#filePickerAdd",
        label: "继续添加"
    });
	// 如果文件已经存在
  	if(attachList.length>0){
  		areaState("ready");
  		for (var i = 0; i < attachList.length; i++) {
  			num++;
  			sizes += attachList[i].size;
  			addFile(null,attachList[i],attachList[i].pathUpload,attachList[i].id);
		}
  		photoInfo();// 文件个数 大小 信息
  	}
	var ad = 0;
	// 当有文件添加进来的时候
	uploader.on('fileQueued', function(file){
		if(num<photoNum){
			num++;// 图片+1
			sizes +=  file.size;
			addFile(selectFiles[ad],file);
			ad++;
			if(ad==selectFiles.length){
				ad = 0;
			}
			areaState("ready");
		}else{
			autoMsg("图片添加已满！",5);
		}
	});
	// 当有文件添加进来的时候
    uploader.on('fileDequeued', function(file){// 当文件被移除队列后触发
    	num--;// 图片-1
    	sizes -=  file.size;
    	if(photoList.children().length==0){// 所有文件移除后 显示上传文件
    		areaState("pedding");	
    	}
    	photoInfo();
    });
    uploader.on("all", function(state) {
    	 switch (state) {
         case "uploadFinished":// 完成上传
        	 areaState("confirm");
             break;
         case "startUpload":
             // t("uploading");
             break;
         case "stopUpload":// 停止上传
             // t("paused");
         }
    });
    // 各状态表单改变
    function areaState(state){
    	sign = state;
    	switch (state){
	    	case "pedding":
	            placeholder.removeClass("element-invisible"),
                // photoList.parent().removeClass("filled"),
	            photoList.hide(),
	            statusBar.addClass("element-invisible"),
	            $("#filePickerAdd").show();// 显示 继续上传按钮
	            uploader.refresh();
	            break;
	    	 case "ready":// 开始上传的状态
	    		 placeholder.addClass("element-invisible"),// 隐藏图片选择区域
	    		 $("#statusShow").show();// 显示图片张数状态
	    		 $("#filePickerAdd").removeClass("element-invisible"),
                 // photoList.parent().addClass("filled"),
	    		 photoList.show(),
	    	     statusBar.removeClass("element-invisible"),
	    	     uploader.refresh();
	             break;
    		case "confirm":// 完成上传
    			uploadBtn.text("开始上传").addClass("disabled");// 置灰开始上传
            	break;
    	}
    	photoInfo();
    }
    function photoInfo() {// 文件个数 大小 信息
        var e, a = "";
        "ready" === sign ? a = "选中" + num + "张图片，共" + WebUploader.formatSize(sizes) + "。": "confirm" === sign ? (e = uploader.getStats(), e.uploadFailNum && (a = "已成功上传" + e.successNum + "张照片至XX相册，" + e.uploadFailNum + '张照片上传失败，<a class="retry" href="#">重新上传</a>失败图片或<a class="ignore" href="#">忽略</a>')) : (e = uploader.getStats(), a = "共" + num + "张（" + WebUploader.formatSize(sizes) + "），已上传" + e.successNum + "张", e.uploadFailNum && (a += "，失败" + e.uploadFailNum + "张")),
        info.html(a);
    }
	// 增加图片
    function addFile(selectFile,file,src,id){
    	var li = $('<li><a href="#" id="pShow"><p class="imgWrap"></p></a></li>'),
	    pd = $('<div class="file-panel"><span class="cancel">删除</span><span class="rotateRight">下载</span></div>').appendTo(li),
	    imgP = li.find("p.imgWrap"),
    	imgShow = li.find("#pShow");
	    if(src==null||src==""){
	    	var objUrl = getObjectURL(selectFile) ;
			if (objUrl) {
				 imgP.empty().append($('<img src="'+objUrl+'" onload="RePicByWidth(this,100);"/>'));
			}
			file.on("statuschange", function(imgP, uploader) {
				 if(imgP=="complete")
					 li.append('<span class="success"></span>');
			});
			pd.on("click", "span", function() {
		         if($(this).index()==0){// 删除
		        	 $(this).parent().parent().remove();
		        	 uploader.removeFile(file,true);
		         }
			});
	    }else{// 图片加载
	    	imgShow.attr("title",file.fileName);
	    	imgShow.attr("data-gallery","");
	    	imgShow.attr("href",src);
	    	imgShow.attr("style","cursor:pointer");
	    	imgP.empty().append($('<img src="'+src+'" onload="RePicByWidth(this,100);"/>'));
	    	imgShow.append('<span class="success"></span>');
	    	pd.on("click", ".cancel", function() {// 删除
		         if($(this).index()==0){// 删除
		        	 parent.layer.confirm("确定删除照片？", {btn: ["确定","取消"]	}, function(){
		        	 	if(delAttach(id)){
		        	 		num--;
		        	 		sizes -= file.size;
		        	 		li.remove();
		        	 		if(num>0)
		        	 			areaState("ready");
		        	 		else
		        	 			areaState("pedding");
		        	 	}
		        	 });
		         }
	    	});
	    	pd.on("click", ".rotateRight", function() {// 下载
	    		location.href = "/attach_download?attachId="+id;
	    	});
	    	photoInfo();
	    }
		li.on("mouseenter", function() {
			pd.stop().animate({
	             height: 25
	         });
	    }),
		li.on("mouseleave", function() {
			pd.stop().animate({
	             height: 0
	         });
	     }),
		photoList.append(li);// 图片加入到显示区域
    }
});

// 删除附件
function delAttach(id){
	var chk = false;
	$.ajax({
		type:'post',
		async:false,
		dataType : 'json',
		url: '/attach_delete?ids='+id,
		success : function(data){
			if(data.result){
				autoMsg("删除图片成功！",1);				
				chk = true;
			}else{
				autoMsg("删除失败！",1);				
			}
		},
	});
	return chk;
}
// 获得照片
var attachList = new Array();
function getAttach(id){
	$.ajax({
		type:'post',
		async:false,
		dataType : 'json',
		url: '/queryAttachList?formId='+id,
		success : function(data){
			for (var i = 0; i < data.attachList.length; i++) {
				attachList.push(data.attachList[i]);
			}
		},
	});
}
// ------------增加图片end----------------------------
