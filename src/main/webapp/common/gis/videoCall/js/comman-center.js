/* 
* @Author: anchen
* @Date:   2018-05-02 15:30:45
* @Last Modified by:   anchen
* @Last Modified time: 2018-05-02 22:29:55
*/

$(document).ready(function(){
     //多选check切换
    $('input[name="check-box"]').wrap('<div class="check-box"><i></i></div>');
    $.fn.toggleCheckbox = function () {
        this.attr('checked', !this.attr('checked'));
    }
    $('.check-box').on('click', function () {
        $(this).find(':checkbox').toggleCheckbox();
        $(this).toggleClass('checkedBox');
    });
    /*语音视频通话按钮*/
    $(".left-commandpc-button ul li.voice-call").click(function(event) {
       $(this).css('background', '#169bd5');
    });
    $(".left-commandpc-button ul li.video-call").click(function(event) {
       $(this).css('background', '#169bd5');
    });
   /* 开始/停止录音  */
    $(".recording2").click(function(event) { 
        $(".recording2").hide();
       $(".recording3").show();
    });
    $(".recording3").click(function(event) { 
        $(".recording3").hide();
       $(".recording2").show();
    });
    /*开始/停止录像*/
    $(".videotape2").click(function(event) { 
        $(".videotape2").hide();
       $(".videotape3").show();
    });
    $(".videotape3").click(function(event) { 
        $(".videotape3").hide();
       $(".videotape2").show();
    });
    /*视频窗口关闭*/
    $(".right-commandpc ul li i.command-close").click(function(event) {
       $(this).parents("li").hide(); 
    });
    /*收缩按钮*/
   /* $("#pack-up").click(function(event) {
      $(this).find('i').removeClass('icon-weibiaoti26').addClass('icon-zhankai');
       $("#left-commandpc").removeClass("slideInLeft").addClass("slideOutLeft"); 
            setTimeout(function(){
                $('#left-commandpc').removeClass("animated slideOutLeft").hide();
                $(".search-wrap").css("height","auto");
                $(".right-commandpc").css('width', '100%');
                $(".right-commandpc ul li").css('margin', '0 1% 1% 1%');
            }, 300); 

   });  */
 /*收缩按钮*/
    $("#pack-up").click(function(event) {
      if($(this).find("i").hasClass('icon-weibiaoti26')){
        $(this).find('i').removeClass('icon-weibiaoti26').addClass('icon-zhankai');
        $("#left-commandpc").removeClass("slideInLeft").addClass("slideOutLeft"); 
            setTimeout(function(){
                $('#left-commandpc').removeClass("animated slideOutLeft").hide(); 
                $(".right-commandpc").css('width', '100%');
                $(".right-commandpc ul li").css('margin', '0 1% 1% 1%');
                $(".pack-up").css('left', '2%');
            }, 300); 
      }else{
        $(this).find('i').removeClass('icon-zhankai').addClass('icon-weibiaoti26');
        $("#left-commandpc").removeClass("slideOutLeft").addClass("slideInLeft"); 
        setTimeout(function(){
                $('#left-commandpc').removeClass("animated slideInLeft").show(); 
                $(".right-commandpc").css('width', '85%');
                $(".right-commandpc ul li").css('margin', '0 0 1% 2%'); 
                 $(".pack-up").css('left', '13%');
        }, 300);
      }

   });  
});