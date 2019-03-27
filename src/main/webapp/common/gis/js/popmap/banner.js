$(function() {
	var $key=0; 
 	
    var timer=setInterval(autoplay, 1500);
	function autoplay(){
		$(".imgbox ul li").eq($key).fadeOut(800); 
		$key++;
		 
		$key=$key%($(".imgbox ul li").length); 

		$(".imgbox ul li").eq($key).fadeIn(800); 
		$(".imgbox ol li").eq($key).addClass('change').siblings().removeClass('change');
	}
 
});