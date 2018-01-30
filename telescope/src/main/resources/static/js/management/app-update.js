$(document).ready(function(){
    $("#sub").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        var form = new FormData(document.getElementById("af"));
        var url = $(this).attr("link")+"/app/update";
        postFrom(url,form,function (data) {
            parent.layer.isEndLoad = true;
            parent.layer.close(index);
        });
    });

});