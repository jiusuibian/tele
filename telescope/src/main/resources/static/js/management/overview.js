$(document).ready(function(){
    $("#app_add").click(function(){
        layer.open({
            title: '添加应用',
            type: 2,
            area: ['640px', '320px'],
            fixed: false, //不固定
            maxmin: true,
            content: $(this).attr("link")+'/app/add/view'
        });
    });
    $(".app_update").click(function () {
        var url =  $("#app_add").attr("link")+"/app/update/view?appid=" + $(this).attr("appid");
        layer.open({
            title: '更新状态',
            type: 2,
            area: ['700px', '480px'],
            fixed: false, //不固定
            maxmin: true,
            content: url,
            end:function() {
                if(layer.isEndLoad){
                    layer.msg("更新成功", {
                        icon: 1,
                        time:500,
                        end:function () {
                            parent.location.reload();
                        }});
                }
            }
        });
        
    });

    $(".app_delete").click(function () {
        var appname=$(this).attr("appname");
        var appid=$(this).attr("appid");
        var url =  $("#app_add").attr("link")+"/app/delete";
        //询问框
        layer.confirm('是否删除应用' + appname + '?', {
            btn: ['确定','取消'] //按钮
        }, function(){
            postJson(url,appid,function(data){
                layer.msg("删除成功", {icon: 1,
                    time:500,
                    end:function () {
                    parent.location.reload();
                }});
            });
        }, function(){

        });
    });
});