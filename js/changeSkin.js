//网站换肤
$(function(){
		var $li =$("#skin li");
		$li.click(function(){
			switchSkin( this.id );
		});
		var cookie_skin = $.cookie("MyCssSkin");   //读取
		if (cookie_skin) {
			switchSkin( cookie_skin );
		}
});

function switchSkin(skinName){
		$("#"+skinName).addClass("selected")                //当前<li>元素选中
					   .siblings().removeClass("selected");  //去掉其他同辈<li>元素的选中
	    $("#cssfile").attr("href","style/"+ skinName +".css"); //设置不同皮肤
		$.cookie( "MyCssSkin" ,  skinName , { path: '/', expires: 10 });
//	myCssSkin：保存在cookie中的值，某个元素的id名称
//	skinName：获取的当前元素的id
//	路径和cookie保存时间
}