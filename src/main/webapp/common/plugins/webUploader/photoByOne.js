/**
 * 只选择一张照片
 */
//------------增加图片start----------------------------
var uploader;// Web Uploader实例
jQuery(function() {

	var uploaderDiv = $("#uploaderDiv"),//整个上传区域
	photoList = $('<ul class="filelist"></ul>').appendTo(uploaderDiv.find(".queueList")),
	statusBar = uploaderDiv.find(".statusBar"),  //状态显示条
    info = statusBar.find(".info"),//信息提示
    uploadBtn = uploaderDiv.find(".uploadBtn"),//上传按钮
    placeholder = uploaderDiv.find(".placeholder"),//选择图片输入区
    progress = statusBar.find(".progress").hide();//进度条
	uploader = WebUploader.create({// 初始化Web Uploader
	    auto: false,// 自动上传。
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
	    fileNumLimit:1,//上传文件数量
	    compress: false,//不压缩上传
	});
  	uploader.addButton({
        id: "#filePicker2",
        label: "继续添加"
    });
	//如果文件已经存在
  	// if(attachList.length>0){
  	// 	areaState("ready");
  	// 	addFile(null,null,attachList[0].pathUpload,attachList[0].id);
  	// }
	var ad = 0;
	uploader.on('fileQueued', function(file){
		addFile(selectFiles[ad],file);
		ad++;
		if(ad==selectFiles.length){
			ad = 0;
		}
		areaState("ready");
	});
	// 当有文件添加进来的时候
    uploader.on('fileDequeued', function(file){//当文件被移除队列后触发
    	if(photoList.children().length==0){//所有文件移除后 显示上传文件
    		areaState("pedding");	
    	}
    });
    uploader.on("all", function(state) {
    	 switch (state) {
         case "uploadFinished"://完成上传
        	 areaState("confirm");
             break;
         case "startUpload":
//              t("uploading");
             break;
         case "stopUpload"://停止上传
//              t("paused");
         }
    });
    //各状态表单改变
    function areaState(state){
    	switch (state){
	    	case "pedding":
	            placeholder.removeClass("element-invisible"),
// 	            photoList.parent().removeClass("filled"),
	            photoList.hide(),
	            statusBar.addClass("element-invisible"),
	            uploader.refresh();
	            break;
	    	 case "ready"://开始上传的状态
	    		 placeholder.addClass("element-invisible"),//隐藏图片选择区域
// 	    		 $(".statusBar").show();
	    		 $("#filePicker2").removeClass("element-invisible"),
// 	    		 photoList.parent().addClass("filled"),
	    		 photoList.show(),
	    	     statusBar.removeClass("element-invisible"),
	    	     uploader.refresh();
	             break;
    		case "confirm"://完成上传
    			uploadBtn.text("开始上传").addClass("disabled");//置灰开始上传
            	break;
    	}
    }
	//增加图片
    function addFile(selectFile,file,src,id){
    	var li = $('<li><p class="imgWrap"></p></li>'),
	    pd = $('<div class="file-panel"><span class="cancel">删除</span></div>').appendTo(li),
	    imgP = li.find("p.imgWrap");
	    if(src==null||src==""){
	    	var objUrl = getObjectURL(selectFile) ;
			if (objUrl) {
				 imgP.empty().append($('<img src="'+objUrl+'" onload="RePicByWidth(this,150);"/>'));
			}
			file.on("statuschange", function(imgP, uploader) {
				 if(imgP=="complete")
					 li.append('<span class="success"></span>');
			});
			pd.on("click", "span", function() {
		         if($(this).index()==0){//删除
		        	 $(this).parent().parent().remove();
		        	 uploader.removeFile(file);
		         }
			});
	    }else{
	    	imgP.empty().append($('<img src="'+src+'" onload="RePicByWidth(this,100);"/>'));
	    	li.append('<span class="success"></span>');
	    	pd.on("click", "span", function() {
		         if($(this).index()==0){//删除
		        	 parent.layer.confirm("确定删除照片？", {btn: ["确定","取消"]	}, function(){
		        	 	if(delAttach(id)){
		        	 		$(".filelist").html("");
			        	 	areaState("pedding");
//			        	 	$(this).parent().parent().remove();
		        	 	}
		        	 });
		         }
			});
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
		photoList.append(li);//图片加入到显示区域
    }
});

var uploaderDiv = $("#uploaderDiv"),//整个上传区域
    photoList = $('<ul class="filelist"></ul>').appendTo(uploaderDiv.find(".queueList")),
    statusBar = uploaderDiv.find(".statusBar"),  //状态显示条
    info = statusBar.find(".info"),//信息提示
    uploadBtn = uploaderDiv.find(".uploadBtn"),//上传按钮
    placeholder = uploaderDiv.find(".placeholder"),//选择图片输入区
    progress = statusBar.find(".progress").hide();//进度条
//删除附件
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

//编辑页面显示图片
function addFile(src,id,entityId){
    var li = $('<li><p class="imgWrap"></p></li>'),
        pd = $('<div class="file-panel"><span class="cancel">删除</span></div>').appendTo(li),
        imgP = li.find("p.imgWrap");
    imgP.empty().append($('<img src="'+src+'" onload="RePicByWidth(this,150);"/>'));
    li.append('<span class="success"></span>');
    pd.on("click", "span", function() {
        if($(this).index()==0){//删除
            parent.layer.confirm("确定删除照片？", {btn: ["确定","取消"]	}, function(){
                updateAttach(entityId,"");//更新实体对应关联附件id
                if(delAttach(id)){
                    $(".filelist").html("");
                    areaState2("pedding");
//			        	 	$(this).parent().parent().remove();
                }
            });
        }
    });
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
        placeholder.addClass("element-invisible");
        photoList.append(li);//图片加入到显示区域
}

//各状态表单改变
function areaState2(state){
        switch (state){
        case "pedding":
            placeholder.removeClass("element-invisible"),
// 	            photoList.parent().removeClass("filled"),
                photoList.hide(),
                statusBar.addClass("element-invisible"),
                uploader.refresh();
            break;
        case "ready"://开始上传的状态
            placeholder.addClass("element-invisible"),//隐藏图片选择区域
// 	    		 $(".statusBar").show();
                $("#filePicker2").removeClass("element-invisible"),
// 	    		 photoList.parent().addClass("filled"),
                photoList.show(),
                statusBar.removeClass("element-invisible"),
                uploader.refresh();
            break;
        case "confirm"://完成上传
            uploadBtn.text("开始上传").addClass("disabled");//置灰开始上传
            break;
    }
}

var attachathUpload;
function getAttach(id){
	$.ajax({
		type:'post',
		async:false,
		dataType : 'json',
		url: '/getAttach?attachId='+id,
		success : function(data){
            attachathUpload = data.attach.pathUpload;
		},
	});
}
//------------增加图片end----------------------------
