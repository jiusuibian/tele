$.validator.setDefaults({
    submitHandler: function() {
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引

        var old_pwd = $("#password").attr("old_pwd");
        var new_pwd = $("#password").val();
        if(old_pwd != new_pwd){
            $("#password").val($.md5(new_pwd));
        }
        var form = new FormData(document.getElementById("uf"));
        var url = $("#sub").attr("link")+"/system/user/update";
        postFrom(url,form,function (data) {
            parent.layer.isEndLoad = true;
            parent.layer.close(index);
        });
    }
});
$(document).ready(function(){
    // 在键盘按下并释放及提交后验证提交表单
    $("#uf").validate({
        rules: {
            password: {
                required: true,
                minlength: 5
            },
            confirm_password: {
                required: true,
                minlength: 5,
                equalTo: "#password"
            },
            userdesc: {
                maxlength: 50
            }
        },
        messages: {
            password: {
                required: "请输入密码",
                minlength: "密码长度不能小于 5 个字符"
            },
            confirm_password: {
                required: "请输入密码",
                minlength: "密码长度不能小于 5 个字符",
                equalTo: "两次密码输入不一致"
            }
        }
    });
});