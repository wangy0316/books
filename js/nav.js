//$(function(){
//    $(".nav li").hover(function(){
//        $(".jnNav").show() ;
//    }.function(){
//        $(".jnNav").hide();
//    });
//})

$(function(){
    $("#nav li").hover(function(){
        $(this).find(".jnNav").show();
    },function(){
        $(this).find(".jnNav").hide();
    });
})