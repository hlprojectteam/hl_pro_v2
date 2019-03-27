/* 
* @Author: anchen
* @Date:   2017-02-27 17:33:37
* @Last Modified by:   anchen
* @Last Modified time: 2017-03-16 20:07:42
*/

$(document).ready(function(){
    
      var mySwiper = new Swiper('.index-banner .swiper-container',{
    pagination: '.index-banner .pagination',
    grabCursor: true,
    paginationClickable: true,
    autoplay:5000,
    loop:true,
    autoplayDisableOnInteraction:false,
    onSlideChangeStart: function(swiper){
  $(".banner-ad article").eq(mySwiper.activeLoopIndex).addClass('flip_back').removeClass('flip_front').show().siblings().removeClass('flip_back').addClass('flip_front').hide();
    }


  })  
      var mySwiper1 = new Swiper('.case-banner .swiper-container',{
    grabCursor: true,
    paginationClickable: true,
    loop:true
 


  })
  $('.word-footer .arrow-left').on('click', function(e){
    e.preventDefault()
    mySwiper1.swipePrev()
  })
  $('.word-footer .arrow-right').on('click', function(e){
    e.preventDefault()
    mySwiper1.swipeNext()
  })


      var mySwiper2= new Swiper('.service-banner .swiper-container',{
    grabCursor: true,
    paginationClickable: true,
    loop:true,
    autoplay:5000
   })
  $('.service-banner .arrow-left').on('click', function(e){
    e.preventDefault()
    mySwiper2.swipePrev()
  })
  $('.service-banner .arrow-right').on('click', function(e){
    e.preventDefault()
    mySwiper2.swipeNext()
  })

      var mySwiper3= new Swiper('.app-ban .swiper-container',{
    grabCursor: true,
     pagination: '.app-ban .pagination',
    paginationClickable: true,
    loop:true,
    autoplayDisableOnInteraction:false,
 onSwiperCreated: function(swiper){
       $(".app-ban .swiper-slide-active").find(".app-img-1").show().addClass('bounceInLeft').siblings(".app-article").show().addClass('bounceInRight');

    },
    onSlideChangeStart: function(swiper){
  $(".app-ban .swiper-slide").find('.app-img-1').removeClass('bounceInLeft').hide();
  $(".app-ban .swiper-slide").find('.app-article').removeClass('bounceInRight').hide();
 
  $(".app-ban .swiper-slide-active").find(".app-img-1").show().addClass('bounceInLeft').siblings(".app-article").show().addClass('bounceInRight');


}

   })



  $('.app-ban .arrow-left').on('click', function(e){
    e.preventDefault()
    mySwiper3.swipePrev()
  })
  $('.app-ban .arrow-right').on('click', function(e){
    e.preventDefault()
    mySwiper3.swipeNext()
  })


 var mySwiper4 = new Swiper('.serve-flow .swiper-container',{
    paginationClickable: true,
    slidesPerView: 4
  })
  $('.serve-container .arrow-left').on('click', function(e){
    e.preventDefault()
    mySwiper4.swipePrev()
  })
  $('.serve-container .arrow-right').on('click', function(e){
    e.preventDefault()
    mySwiper4.swipeNext()
  })
      var mySwiper5= new Swiper('.special-ban .swiper-container',{
     pagination: '.special-ban .pagination',
    grabCursor: true,
    paginationClickable: true,
    loop:true,
    autoplay:5000
   })
  $('.special-ban .arrow-left').on('click', function(e){
    e.preventDefault()
    mySwiper5.swipePrev()
  })
  $('.special-ban .arrow-right').on('click', function(e){
    e.preventDefault()
    mySwiper5.swipeNext()
  })

});