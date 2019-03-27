/* 
* @Author: anchen
* @Date:   2017-03-02 11:05:56
* @Last Modified by:   anchen
* @Last Modified time: 2018-04-04 00:02:11
*/

$(document).ready(function(){
    var navL = $("#nav").offset().left;

    $("#nav a").mouseover(function() {
  var $thisL = $(this).offset().left-$("#nav").offset().left;
  var $thisW = $(this).width();
   
    $("#nav_line").stop().animate({"left": $thisL, "width": $thisW,"opacity":1}, 500);
 
    
    });
    $("#nav a").mouseout(function() {

    $("#nav_line").css({"opacity":1});
 
    });
  
//滚动条
            $('#rol').niceScroll({
                cursorcolor: "#ccc",//#CC0071 光标颜色
                cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
                touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                cursorwidth: "5px", //像素光标的宽度
                cursorborder: "0", //   游标边框css定义
                cursorborderradius: "5px",//以像素为光标边界半径
                autohidemode: false //是否隐藏滚动条
            });

            /*导航切换*/
             $('#CourseNav li').hover(function(){     
                $(this).find("a").addClass("on");
                $(this).siblings().find("a").removeClass("on");
             $('#CourseWrap').children().hide();
             $('#CourseWrap').children().eq($(this).index()).show();
              }
                   );  
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

/*个人中心*/ 
$("#people-center").hover(function() {
  $("#PCenter").css('display', 'inherit');
}, function() {
  $("#PCenter").css('display', 'none');
});

 

