$(function(){
    $("#jnBrandTab li a").click(function(){
        //是给li添加样式，而不是给a添加，这里点击的是a。
        $(this).parent().addClass("chos").siblings().removeClass("chos");
    //   索引*800,为显示的值。position的值
        var ind = $("#jnBrandTab li a").index(this);
        showList(ind);
        return false ;  //这里是a标签，要阻止跳转
    })
});
function showList(index){
    var $list = $("#jnBrandList");
    var liWidth = $list.find("li").outerWidth();  //每个li的width+padding
    liWidth = liWidth * 4 ;                        //一列li的宽
    $list.stop(true,false).animate({ left:-liWidth * index },1000); //左正右负
}