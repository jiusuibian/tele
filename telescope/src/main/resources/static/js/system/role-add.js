$.validator.setDefaults({
    submitHandler: function() {
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        var form = new FormData(document.getElementById("rf"));
        var url = $("#sub").attr("link")+"/system/role/add";
        postFrom(url,form,function (data) {
            parent.layer.isEndLoad = true;
            parent.layer.close(index);
        });
    }
});
$(document).ready(function(){
    // 在键盘按下并释放及提交后验证提交表单
    $("#rf").validate({
        rules: {
            role_id:"required",
            role_name: {
                required: true,
                minlength: 2
            },
            remark: {
                maxlength: 50
            }
        },
        messages: {
            role_id: {
                required: "请输入角色id"
            },
            role_name: {
                required: "请输入角色名称",
                minlength: "角色名称长度不能小于 2 个字符"
            }
        }
    });
});