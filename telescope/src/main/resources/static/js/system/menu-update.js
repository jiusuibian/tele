$.validator.setDefaults({
    submitHandler: function() {
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        var form = new FormData(document.getElementById("uf"));
        form.set("menu_id",parent.$("#menu_id").val());
        form.set("icon",$("#icon").val());//隐域new FormData获取不到
        var url = $("#sub").attr("link")+"/system/menu/update";
        postFrom(url,form,function (data) {
            parent.layer.isEndLoad = true;
            parent.layer.close(index);
        });
    }
});
$(function(){
    $("#chooseIcon").click(function (){
        parent.$(".layui-layer-title")[0].innerText = '选择图标';
        layer.open({
            type: 2,
            title: false,
            closeBtn: 0,
            area: ['700px', '460px'],
            shade: 0.8,
            closeBtn: 0,
            shadeClose: true,
            content: $(this).attr("link")+'/system/icon/view',
            end:function(){
                parent.$(".layui-layer-title")[0].innerText = '编辑菜单';
            }
        });
    });

    $("#uf").validate({
        rules: {
            name: {
                required: true,
            },
            url: {
                required: true,
            }
        },
        messages: {
            name: {
                required: "请输入名称",
            },
            url: {
                required: "请输入url",
            }
        }
    });
});