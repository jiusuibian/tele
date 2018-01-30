/**
 * 删除
 */
function remove (menu_id,menu_name) {
    var url =  $('#table_local').attr("link")+"/system/menu/delete";
    //询问框
    layer.confirm('是否删除'+menu_name+'?', {
        btn: ['确定','取消'] //按钮
    }, function(){
        var postData = {
            "menu_id": menu_id
        };
        postJson(url,JSON.stringify(postData),function(data){
            layer.msg("删除成功", {
                icon: 1,
                time:500,
                end:function () {
                    parent.location.reload();
            }});
        });
    }, function(){

    });
}
/**
 * 添加
 */
function add (menu_id,type) {
    $("#menu_id").val(menu_id);
    $("#type").val(type);
    if(type=='B')
    {
        layer.msg("按钮不允许添加子菜单", {
            icon: 2,
            });
        return;
    }
    layer.open({
        title: '添加菜单',
        type: 2,
        area: ['700px', '500px'],
        fixed: false, //不固定
        content: $("#menu_add").attr("link")+'/system/menu/add/view',
        end:function()
        {
            if(layer.isEndLoad)
            {
                layer.msg("添加成功", {
                    icon: 1,
                    time:500,
                    end:function () {
                    parent.location.reload();
                }});
            }
        }
    });
}
/**
 * 编辑
 */
function edit (menu_id) {
    $("#menu_id").val(menu_id);
    layer.open({
        title: '编辑菜单',
        type: 2,
        area: ['700px', '500px'],
        fixed: false, //不固定
        content: $("#menu_add").attr("link")+'/system/menu/update/view?menu_id='+menu_id,
        end:function()
        {
            if(layer.isEndLoad)
            {
                layer.msg("修改成功", {
                    icon: 1,
                    time:500,
                    end:function () {
                    parent.location.reload();
                }});
            }
        }
    });
}
$(function(){
    /**
     * 添加根节点
     */
    $("#menu_add").click(function(){
        add(0,'');
    });
    //创建时间（起，止）
    // datetimepicker('start');
    // datetimepicker('end');

    var url = $('#table_local').attr("link")+'/system/menu/list';
    $('#table_local').bootstrapTreeTable(
            {
                code: 'menu_id',
                parentCode : 'parent_id',
                type: 'post',
                url: url,
                ajaxParams : {}, // 请求数据的ajax的data属性
                expandColumn : '0',// 在哪一列上面显示展开按钮
                striped : true, // 是否各行渐变色
                bordered : true, // 是否显示边框
                expandAll : false, // 是否全部展开
                // toolbar : '#exampleToolbar',
                columns : [
                    // {
                    //     title : '编号',
                    //     field : 'menu_id',
                    //     align : 'center',
                    //     valign : 'middle',
                    //     width : '100px'
                    // },
                    {
                        title : '名称',
                        field : 'name',
                        align : 'center',
                        valign : 'middle'
                    },

                    {
                        title : '图标',
                        field : 'icon',
                        align : 'center',
                        valign : 'middle',
                        formatter : function(item, index) {
                            return item.icon == null ? ''
                                : '<i class="' + item.icon
                                + ' fa-lg"></i>';
                        }
                    },
                    {
                        title : '类型',
                        field : 'type',
                        align : 'center',
                        valign : 'middle',
                        formatter : function(item, index) {
                            if (item.type === 'F') {
                                return '<span class="label label-primary">目录</span>';
                            }
                            if (item.type === 'M') {
                                return '<span class="label label-success">菜单</span>';
                            }
                            if (item.type === 'B') {
                                return '<span class="label label-warning">按钮</span>';
                            }
                        }
                    },
                    {
                        title : '地址',
                        field : 'url',
                        align : 'center',
                        valign : 'middle'
                    },
                    {
                        title : '权限标识',
                        field : 'perms',
                        align : 'center',
                        valign : 'middle'
                    },
                    {
                        title : '创建者',
                        field : 'creator',
                        align : 'center',
                        valign : 'middle'
                    },
                    {
                        title : '创建时间',
                        field : 'create_time',
                        align : 'center',
                        valign : 'middle'
                    },
                    {
                        title : '修改者',
                        field : 'modifier',
                        align : 'center',
                        valign : 'middle'
                    },
                    {
                        title : '修改时间',
                        field : 'modify_time',
                        align : 'center',
                        valign : 'middle'
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(item, index) {
                            var e = '<a class="btn btn-primary btn-sm '
                                + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + item.menu_id
                                + '\')"><i class="fa fa-edit"></i></a> ';
                            var p = '<a class="btn btn-primary btn-sm '
                                + '" href="#" mce_href="#" title="添加下级" onclick="add(\''
                                + item.menu_id+'\',\''+item.type
                                + '\')"><i class="fa fa-plus"></i></a> ';
                            var d = '<a class="btn btn-warning btn-sm '
                                + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + item.menu_id+'\',\''+item.name
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            return e + d + p;
                        }
                    }
                    ]
            });
    /**
     * 条件搜索
     */

    // $("#user_search").click(function () {
    //     dataTable.fnDraw();
    // })
});