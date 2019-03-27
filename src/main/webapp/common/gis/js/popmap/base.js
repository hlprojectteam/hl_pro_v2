$(function(){
    /*第一部分 楼宇信息 单元下拉框*/
    $('#res').change(function(){
        $('.data').hide();
        var p1=$(this).children('option:selected').val();
        $("."+p1+"").show();
    });



    /*中间部分导航*/
    $('.center_nav ul li').click(function(){
            $(this).addClass("current").siblings().removeClass("current");
            $('.nav_content').children().hide();
            $('.nav_content').children().eq($(this).index()).show();
        }
    );

    /*第一部分关闭其他弹窗关闭*/
    $('.center_closed').click(function() {
        $('.center').hide();
        $('.box_right').hide();
    });

    /*更多按钮*/
    var res =$('.add_more') ;
    res.hide();
    $('.center_more').on('click',function() {
        if(res.is(":hidden")){
            $(this).css({"background":"#eafcde","border":"1px solid #4dac00"});
            res.show();
        }else{
            $(this).css({"background":"","border":""});
            res.hide();
        }
    });



    /*信息框展示效果*/

    $('#address').click(function() {
        var addressShow = $(".center").hasClass('addressShow');
        if(!addressShow){
            $(".center").animate({
                    opacity: '1',
                    visibility: 'visible',
                    top:'52px'
                },
                300);
            $(".center").addClass('addressShow');
        }else{
            $(".center").animate({
                opacity: '0',
                visibility: 'hidden'
            },function(){
                $(".center").css('top', '30%');
            });

            $(".center").removeClass('addressShow');

        }
    });


});