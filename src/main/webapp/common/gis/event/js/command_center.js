 

$(document).ready(function(){
     $('#Commandwrap_left_nav a').click(function(){
       /*alert("hello word!"); */
    $('#Commandwrap_list_box').children().hide();
    $('#Commandwrap_list_box').children().eq($(this).index()).show(); 
}); 
    $('#Commandwrap_left_nav a').click(function(){
     $(this).addClass('title_current').siblings().removeClass('title_current');

    });
    $('#header_nav a').click(function(){
        /*$(this).css('color', '#00fcff');
        $(this).find('span').addClass('rect');
        $("#header-nav a").siblings().css('color', '#fff');
        $("#header-nav a").siblings().find('span').removeClass('rect');*/
       $("#header_nav a span").removeClass('rect');
       $(this).css('color', '#00fcff');
        $(this).find('span').addClass('rect');
        $(this).siblings().css('color', '#fff');


    });


    /*弹窗关闭按钮*/
    $("#event_close_btn").click(function(event) {
        $("#command_event_windows").hide();
        popomap.removeAllPop();
    });
    /*视频窗口打开按钮*/
    $("#command_video").click(function(event) {
      $("#command_video_windows").show();

    });
     $("#video_state").click(function(event) {
       $("#command_video_windows").hide();
    });
    /*视频窗口关闭*/

}); 