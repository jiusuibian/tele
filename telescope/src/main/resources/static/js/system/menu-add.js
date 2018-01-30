$.validator.setDefaults({
    submitHandler: function() {
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        var form = new FormData(document.getElementById("uf"));
        form.set("parent_id",parent.$("#menu_id").val());
        form.set("order_num",0);
        form.set("icon",$("#icon").val());//隐域new FormData获取不到
        var url = $("#sub").attr("link")+"/system/menu/add";
        postFrom(url,form,function (data) {
            parent.layer.isEndLoad = true;
            parent.layer.close(index);
        });
    }
});
$(function(){
    var type = parent.$("#type").val();
    if(type=='F')
    {
        $("#type").append("<option value='F'>目录</option>");
        $("#type").append("<option value='M'>菜单</option>");
        $("#type").append("<option value='B'>按钮</option>");
    }
    else if(type=='M')
    {
        $("#type").append("<option value='M'>菜单</option>");
        $("#type").append("<option value='B'>按钮</option>");
    }
    else
    {
        $("#type").append("<option value='F'>目录</option>");
        $("#type").append("<option value='M'>菜单</option>");
        $("#type").append("<option value='B'>按钮</option>");
    }
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
                parent.$(".layui-layer-title")[0].innerText = '添加菜单';
            }
        });
    });

    $("#uf").validate({
        rules: {
            name: "required",
            url: "required"
        },
        messages: {
            name: {
                required: "请输入名称"
            },
            url: {
                required: "请输入url"
            }
        }
    });
});