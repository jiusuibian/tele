$(document).ready(function(){
//注意：parent 是 JS 自带的全局对象，可用于操作父页面
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    $("#sub").click(function(){
        var form = new FormData(document.getElementById("af"));
        var url = $(this).attr("link")+"/system/user/authorize";
        postFrom(url,form,function (data) {
            parent.layer.close(index);
            parent.layer.msg("角色分配成功", {icon: 1});
        });
    });
});