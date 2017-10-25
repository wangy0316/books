
$(function() {
    var index = 0;
    var $imgralls = $("#jnImagerall div a"); //导航中的a
    var len = $imgralls.length;   //有多少个图片
    $imgralls.mouseover(function () {
        var index = $(this).index();
        showImg(index);
    }).eq(0).mouseover();
    $('#jnImagerall').hover(function () {
            if (adTimer) {
                clearInterval(adTimer);
            }
        }, function () {
            adTimer = setInterval(function () {
                showImg(index);
                index++;
                if (index == len) {
                    index = 0;
                }
            }, 3000);  //每3秒执行一次showImg函数
        }).trigger("mouseleave");
})
//如果移动过快，会产生很多滞待动作，我们要删除中间的滞待动作，执行最后一个
function showImg(index){
    var $rollobj = $("#jnImagerall");
    var $rolllist = $rollobj.find("div a");
    var newhref = $rolllist.eq(index).attr("href");
    $("#JS_imgWrap").attr("href",newhref)   //取a
        .find("img").eq(index).stop(true,true).fadeIn().siblings().fadeOut();
    $rolllist.removeClass("chos").css("opacity","0.7")
        .eq(index).addClass("chos").css("opacity","1");
}