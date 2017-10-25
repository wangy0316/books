$(function(){
    $("#inputSearch").focus(function(){    //获得焦点
        if( $(this).val() == this.defaultValue){
            $(this).val("") ;
        }
    }).blur(function(){                     //失去焦点
        $(this).removeClass("focus");
        if( $(this).val() == ""){
            $(this).val(this.defaultValue) ;
        }
    })
})
//$(function(){
//    var $li = $("#skin li");
//    $li.click(function(){
//        var id = this.id ;
//        alert(id);
//        $.cookie("myCssSkin",this.id,{ path:'/' , expires:10}) ;
//    });
//    var cookie_skin = $.cookie("myCssSkin");
//    if(cookie_skin){
//        $("#cssFile").attr("href","style/"+id+".css");
//        $.cookie("myCssSkin",mySkin,{ path:'/' , expires:10}) ;
//    }
//})