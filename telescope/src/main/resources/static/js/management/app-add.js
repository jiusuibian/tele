$.validator.setDefaults({
    submitHandler: function() {
        var form = new FormData(document.getElementById("af"));
        var url = $("#sub").attr("link")+"/app/add";
        postFrom(url,form,function (data) {
            parent.location.reload();
            parent.layer.close(index);
        });
    }
});

$(document).ready(function(){
    // 在键盘按下并释放及提交后验证提交表单
    $("#af").validate({
        rules: {
            appname:"required"
        },
        messages: {
            appname: {
                required: "请输入应用名"
            }
        }
    });
});