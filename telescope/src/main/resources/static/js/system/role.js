$(function(){
    //添加角色
    $("#role_add").click(function(){
        layer.open({
            title: '添加角色',
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true, //开启最大化最小化按钮
            content: $(this).attr("link")+'/system/role/add/view',
            end:function() {
                if(layer.isEndLoad){
                    layer.msg("添加成功", {
                        icon: 1,
                        time:500,
                        end:function () {
                            parent.location.reload();
                        }});
                }
            }
        });
    });

    //创建时间（起，止）
    datetimepicker('start');
    datetimepicker('end');

    var url = '/telescope/system/role/list';
    var columns =  [
            { data: 'role_id' },
            { data: 'role_name' },
            { data: 'remark' },
            { data: 'create_time' },
            { data: 'modify_time' },
            { data: 'creator' },
            { data: 'modifier' },
            {
                "render": function () {
                    var html = '<a class="glyphicon glyphicon-tasks" title="分配菜单" id="role-authorize"></a>';

                    return html;
                }
            },
            {
                "render": function () {
                    var html = '<a class="glyphicon glyphicon-edit" title="修改" id="role-update"></a>';

                    return html;
                }
            },
            {
                "render": function () {
                    var html = '<a title="删除" id="role-del" class="glyphicon glyphicon-trash"></a>';

                    return html;
                }
            },

        ];
    var dataTable = $("#table_local").dataTable(tableParam(url,columns,"sf"));
    /**
     * 删除
     */
    $('#table_local').on( 'click', 'a#role-del', function () {
        //先拿到点击的行号
        var rowIndex = $(this).parents("tr").index();
        var data=$("#table_local").DataTable().row(rowIndex).data();//获取值的对象数据
        var url =  $('#table_local').attr("link")+"/app/delete";
        //询问框
        layer.confirm('是否删除角色' + data.role_name + '?', {
            btn: ['确定','取消'] //按钮
        }, function(){
            postJson(url,data.role_id,function(data){
                layer.msg("删除成功", {icon: 1,end:function () {
                    parent.location.reload();
                }});
            });
        }, function(){

        });
    });

    /**
     * 更新
     */
    $('#table_local').on( 'click', 'a#role-update', function () {
        //先拿到点击的行号
        var rowIndex = $(this).parents("tr").index();
        var data=$("#table_local").DataTable().row(rowIndex).data();//获取值的对象数据
        layer.open({
            title: '更新角色',
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: $('#table_local').attr("link")+'/system/role/update/view?role_id=' + data.role_id +
            "&role_name=" + data.role_name + "&remark=" + data.remark,
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

    /**
     * 分配菜单
     */
    $('#table_local').on( 'click', 'a#role-authorize', function () {
        //先拿到点击的行号
        var rowIndex = $(this).parents("tr").index();
        var data=$("#table_local").DataTable().row(rowIndex).data();//获取值的对象数据
        layer.open({
            title: "当前角色：" + data.role_name,
            type: 2,
            area: ['700px', '360px'],
            fixed: false, //不固定
            maxmin: true,
            content: $('#table_local').attr("link")+'/system/role/authorize/view?role_id=' + data.role_id
        });
    });
    /**
     * 条件搜索
     */

    $("#role_search").click(function () {
        dataTable.fnDraw();
    })
});