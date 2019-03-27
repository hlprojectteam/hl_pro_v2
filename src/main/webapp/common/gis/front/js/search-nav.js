/* 
* @Author: anchen
* @Date:   2018-04-09 11:31:37
* @Last Modified by:   anchen
* @Last Modified time: 2018-04-10 10:36:03
*/

$(document).ready(function(){
/*菜单按钮鼠标接触事件*/
    $("#menu-button").hover(function() {
        $(this).find("span").show(300);
    }, function() {
       $(this).find("span").hide(300);
    });


/*搜索框光标触发事件*/
 //    $('#search-input').focus(function(event) {
 //       $('#Modular-nav').show(300);
 //    });
 //   $("#search-input").blur(function(){
 //       setTimeout(function(){
 //         $('#Modular-nav').hide(300);
 //      },300);
 // });
   $('#icon-box').click(function(event) {
       $('#Modular-nav').show(300);
    });


/*搜索框类目切换*/
$('#Modular-nav ul li').click(function(){
   /*alert("hello word!"); */
    $('#icon-box').children().hide();
    $('#icon-box').children().eq($(this).index()).show();
    $('#Modular-nav').hide(300);
});

$('#Modular-nav ul li').click(function(event) {
  $(this).css('background', '#3385ff').siblings().css('background', '#fff');
  $(this).find('span').css('color', '#fff');
  $(this).find('i').css('color', '#fff');
  $(this).siblings().find('span').css('color', '#333');
  $(this).siblings().find('i').css('color', '#333');
});

    /* 菜单内容收缩展开*/
    $("#menu-button").click(function(event) {
        $(".menu-box").addClass('animated slideInLeft').show();
        $(".search-wrap").css("height","100%");
    });
    $("#shrink-icon").click(function(event) {
        $(".menu-box").removeClass("slideInLeft").addClass("slideOutLeft");
        setTimeout(function(){
            $('.menu-box').removeClass("animated slideOutLeft").hide();
            $(".search-wrap").css("height","auto");
        }, 300);

    });
    /*行政区划、网格列表切换*/
    $('#menu-nav span').click(function(){
        $('#menu-content').children().hide();
        $('#menu-content').children().eq($(this).index()).show();


    });
    /*行政区划、网格列表按钮切换样式*/
    $("#menu-nav span#administrative").click(function(event) {
        $(this).css('background', '#3385ff');
        $(this).find('em').css('color', '#fff');
        $(this).find('i').css('color', '#fff');
        $("#Grid").css('background', '#fff');
        $("#Grid").find('em').css('color', '#333');
        $("#Grid").find('i').css('color', '#333');
    });
    $("#menu-nav span#Grid").click(function(event) {
        $(this).css('background', '#3385ff');
        $(this).find('em').css('color', '#fff');
        $(this).find('i').css('color', '#fff');
        $("#administrative").css('background', '#fff');
        $("#administrative").find('em').css('color', '#333');
        $("#administrative").find('i').css('color', '#333');
    });


//单选radio切换
    $(function() {
        $('.Multiple_Choice span label').click(function(){
            var radioId = $(this).attr('name');
            $('.Multiple_Choice span label').removeAttr('class') && $(this).attr('class', 'checked');
            $('.Multiple_Choice span input[type="radio"]').removeAttr('checked') && $('#' + radioId).attr('checked', 'checked');
        });
    });


    $('input[name="radio-btn"]').wrap('<div class="radio-btn"><i></i></div>');
    $(".radio-btn").on('click', function () {
        var _this = $(this),
            block = _this.parent().parent();
        block.find('input:radio').attr('checked', false);
        block.find(".radio-btn").removeClass('checkedRadio');
        _this.addClass('checkedRadio');
        _this.find('input:radio').attr('checked', true);
    });

//下拉框
    $(".select").each(function(){
        var s=$(this);
        var z=parseInt(s.css("z-index"));
        var dt=$(this).children("dt");
        var dd=$(this).children("dd");
        var _show=function(){dd.slideDown(200);dt.addClass("cur");s.css("z-index",z+1);};   //展开效果
        var _hide=function(){dd.slideUp(200);dt.removeClass("cur");s.css("z-index",z);};    //关闭效果
        dt.click(function(){dd.is(":hidden")?_show():_hide();});
        dd.find("a").click(function(){dt.html($(this).html());_hide();});     //选择效果（如需要传值，可自定义参数，在此处返回对应的“value”值 ）
        $("body").click(function(i){ !$(i.target).parents(".select").first().is(s) ? _hide():"";});
    });

});