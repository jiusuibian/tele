$(function(){
    //添加用户
    $("#user_add").click(function(){
        layer.open({
            title: '添加用户',
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true, //开启最大化最小化按钮
            content: $(this).attr("link")+'/system/user/add/view',
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

    var url = '/telescope/system/user/list';
    var columns =  [
            { data: 'username' },
            // { data: 'password' },
            { data: 'lastlogintime' },
            { data: 'loginattemps' },
            {
                "data": "status",
                "render": function (data) {
                    // 0-允许、1-不允许
                    var html = '';
                    if (data == 0) {
                        html += '正常';
                    } else {
                        html += '锁定';
                    }
                    return html;
                }
            },
            { data: 'userdesc' },
            { data: 'create_time' },
            { data: 'modify_time' },
            { data: 'creator' },
            { data: 'modifier' },
            {
                "render": function () {
                    var html = '<a class="glyphicon glyphicon-transfer" title="分配角色" id="user-authorize"></a>';

                    return html;
                }
            },
            {
                "render": function () {
                    var html = '<a class="glyphicon glyphicon-edit" title="修改" id="user-update"></a>';

                    return html;
                }
            },
            {
                "render": function () {
                    var html = '<a title="删除" id="user-del" class="glyphicon glyphicon-trash"></a>';

                    return html;
                }
            },

        ];
    var dataTable = $("#table_local").dataTable(tableParam(url,columns,"sf"));
    /**
     * 删除
     */
    $('#table_local').on( 'click', 'a#user-del', function () {
        //先拿到点击的行号
        var rowIndex = $(this).parents("tr").index();
        var data=$("#table_local").DataTable().row(rowIndex).data();//获取值的对象数据
        var url =  $('#table_local').attr("link")+"/system/user/delete";
        //询问框
        layer.confirm('是否删除用户' + data.username + '?', {
            btn: ['确定','取消'] //按钮
        }, function(){
            var postData = {
                "username": data.username
            };
            postJson(url,JSON.stringify(postData),function(data){
                layer.msg("删除成功", {
                    icon: 1,
                    time: 500,
                    end:function () {
                    parent.location.reload();
                }});
            });
        }, function(){

        });
    });

    /**
     * 更新
     */
    $('#table_local').on( 'click', 'a#user-update', function () {
        //先拿到点击的行号
        var rowIndex = $(this).parents("tr").index();
        var data=$("#table_local").DataTable().row(rowIndex).data();//获取值的对象数据
        layer.open({
            title: '更新用户',
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: $('#table_local').attr("link")+'/system/user/update/view?username=' + data.username,
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
     * 分配角色
     */
    $('#table_local').on( 'click', 'a#user-authorize', function () {
        //先拿到点击的行号
        var rowIndex = $(this).parents("tr").index();
        var data=$("#table_local").DataTable().row(rowIndex).data();//获取值的对象数据
        layer.open({
            title: "当前用户：" + data.username,
            type: 2,
            area: ['700px', '360px'],
            fixed: false, //不固定
            maxmin: true,
            content: $('#table_local').attr("link")+'/system/user/authorize/view?username=' + data.username
        });
    });
    /**
     * 条件搜索
     */

    $("#user_search").click(function () {
        dataTable.fnDraw();
    })
});