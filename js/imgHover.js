
$(function(){
    $("#jnBrandList li").each(function(){
        var img_w = $(this).width();
        var img_h = $(this).height();
        var createSpan ='<span style="position:absolute;top:0;left:5px;width:'+img_w+'px;height:'+img_h+'px" class="imageMask"></span>';
        $(createSpan).appendTo(this);
    })
    $("#jnBrandList").find(".imageMask").hover(function(){
        $(this).toggleClass("imageOver");
    })
})