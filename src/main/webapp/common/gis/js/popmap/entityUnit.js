$(function(){
	/*第二部分导航*/
	$("#content").show();
	$('.second_nav ul li').click(function(){  
		$(this).addClass("second_current").siblings().removeClass("second_current"); 
		$('.second_nav_content').children().hide();
		$('.second_nav_content').children().eq($(this).index()).show();
	}); 
});