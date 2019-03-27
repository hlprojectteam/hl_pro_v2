$(function(){



    //点击展开
    $(".details").off('click').on('click','.pack', function () {
        var thispa = $(this).parent()

        thispa.animate({height:  $('.details-hid').height()});
        $(this).removeClass('icon iconfont icon-jiantou-top').addClass('icon iconfont icon-shangxiajiantou-copy1');
        jQuery(this).text("收起")

    }).on('click','.icon-shangxiajiantou-copy1', function () {
        $(".desc").animate({height:$(this).height()});
        $(this).removeClass('icon icon-shangxiajiantou-copy1').addClass('icon iconfont icon-jiantou-top');
        $(this).text("展开")

    });


    var flage = false;
    $(".testswitch").on("change",function(){

        if(flage==false){
        	$("#epIsSite").val(1);		//选择本站处理,epIsSite的值为1
            $(".opiniondb").css({
                display:"none"
            })
            return flage = true;
        }else{							
        	$("#epIsSite").val(0);		//选择其他相关部门处理,epIsSite的值为0
            $(".opiniondb").css({
                display:"block"
            })
            return flage = false;
        }
    });

//点击事件流程 和事件详情，切换页面

    $('.s_one').bind("click",function(){
        $(".main").css({
            display:"block"
        })

        $(".flow").css({
            display:"none"
        })
        $("#ascrail2000").css({
            display:"block"
        })
        $(".line").animate({
            left:"0"
        })

    });

    $('.s_two').bind("click",function(){

        $(".flow").css({
            display:"block"
        })

        $(".main").css({
            display:"none"
        })
        $("#ascrail2000").css({
            display:"none"
        })
        $(".line").animate({
            left:"38%"
        })

    });

});

