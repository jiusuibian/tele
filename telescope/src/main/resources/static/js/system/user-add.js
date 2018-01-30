$.validator.setDefaults({
    submitHandler: function() {
        $("#password").val($.md5($("#password").val()));
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        var form = new FormData(document.getElementById("uf"));
        var url = $("#sub").attr("link")+"/system/user/add";
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
            username: {
                required: true,
                minlength: 2
            },
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
            username: {
                required: "请输入用户名",
                minlength: "用户名必需由两个字符组成"
            },
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