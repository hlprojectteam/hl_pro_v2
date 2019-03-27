(function($, window, document, undefined) {
	var pub = {};
	window.openCourseDetail = pub;
	pub.init = function() {
		bindEvent();
	};
	
	// 绑定事件
	function bindEvent() {
		$(".reportCourse").bind("click", reportCourse);
		$(".course-detail-video-list li").bind("click", toOpenDiv);
		$("#commentBtn").bind("click", openCommentDiv);
	}
	
	/**
	 * 加入我的课表
	 */
	function reportCourse(){
		if(Login.checkLogin(true)){
			var courseId = $(this).attr("courseId");
			Sender.ajax({"url":"/open/reportCourse.action", "data":{"courseId":courseId}, "fn":function(data){
				if(data == null || data == ''){
					Tips.showSuccessMsg({"info":"加入我的课表成功", "callFn":function(){
						window.location.href=window.location.href;
					}});
				}else{
					Tips.showErrorMsg({"info":data});
				}
			}});
		}
	}
	
	function toOpenDiv() {
		if(Login.checkLogin(true)){
			$("#a1").empty();
			$("#videoName").html("请选择视频进行播放");
			var courseId = $(this).attr("data-value");
			var params = {
				"div" : "#videoLayer",
				"closeObject" : ".close"
			};
			Sender.openDiv(params);
			$("#videoLayer .close").bind("click", function() {
				$("#a1").empty();
			});
			Sender.load({
				"div" : "#playVideoList",
				"url" : Domain.domain_path + "/open/playVideoList.action?courseId=" + courseId
			});
		}
	}
	
	/**
	 * 打开评论框
	 */
	 $("#commentBtn").click(function(event) {
	 	$("#gray").show();
	 	$("#commentDiv").show();
	 });
	 /*关闭评论框*/
	 $("#evaluate-close").on('click', function(event) {
        $("#gray").hide();
        $("#commentDiv").hide();
    });
	
})(jQuery, window, document);