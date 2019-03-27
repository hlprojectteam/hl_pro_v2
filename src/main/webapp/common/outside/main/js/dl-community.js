/* 
* @Author: anchen
* @Date:   2018-04-26 18:48:26
* @Last Modified by:   anchen
* @Last Modified time: 2018-04-27 09:46:19
*/

$(document).ready(function(){
    
/*按钮切换效果*/
    var menuCls = ["sq","jk","gx","zw","yd"];
    var liList = $(".DB_menuSet li");
   $(".DB_menuSet li").hover(function() {
        var mainIndex = $(this).index();
        for(var index = 0; index < menuCls.length; index++){
            if(mainIndex == index && !$(this).hasClass(menuCls[index])){
                $(this).find("span").addClass(menuCls[index]);
            }else{
                $(liList[index]).find("span").removeClass(menuCls[index]);
            }
        }
    }, function() {
      // var mainIndex = $(this).index();
       //$(this).find('span').removeClass(menuCls[mainIndex]);
    });


/*背景图元素出场效果*/
   $('.DB_tab25').DB_tabMotionBanner({
    key:'b28551',
    autoRollingTime:10000,                            
    bgSpeed:500,
    motion:{
        DB_1_1:{left:-50,opacity:0,speed:1000,delay:500},
        DB_1_2:{left:-50,opacity:0,speed:1000,delay:1000},
        DB_1_3:{left:100,opacity:0,speed:1000,delay:1500},
        DB_2_1:{top:50,opacity:0,speed:1000,delay:500},
        DB_2_2:{top:50,opacity:0,speed:1000,delay:1000},
        DB_2_3:{top:100,opacity:0,speed:1000,delay:1500},
        DB_3_1:{left:-50,opacity:0,speed:1000,delay:500},
        DB_3_2:{top:50,opacity:0,speed:1000,delay:1000},
        DB_3_3:{top:0,opacity:0,speed:1000,delay:1500},
        DB_4_1:{top:50,opacity:0,speed:1000,delay:500},
        DB_4_2:{top:0,opacity:0,speed:1000,delay:1000},
        DB_4_3:{top:0,opacity:0,speed:1000,delay:1500},
        DB_4_4:{top:30,opacity:0,speed:1000,delay:2000},
        DB_4_5:{top:100,opacity:0,speed:1000,delay:3000},
        DB_5_1:{top:50,opacity:0,speed:1000,delay:500},
        DB_5_2:{top:0,opacity:0,speed:1000,delay:1000},
        DB_5_3:{top:0,opacity:0,speed:1000,delay:1500},
        DB_5_4:{top:30,opacity:0,speed:1000,delay:2000},
        DB_5_5:{top:100,opacity:0,speed:1000,delay:3000},
        end:null
    }
})
});