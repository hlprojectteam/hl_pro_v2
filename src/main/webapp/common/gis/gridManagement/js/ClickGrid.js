 $(document).ready(function(){
 
/*导航切换*/
$('.clickgrid_nav ul li').click(function(){     
   $(this).addClass("gridcurrent").siblings().removeClass("gridcurrent");
$('.clickgrid_box').children().hide();
$('.clickgrid_box').children().eq($(this).index()).show();
 }
      ); 

});