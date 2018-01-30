$(document).ready(function () {
    // 选中父节点时，选中所有子节点
    function getChildNodeIdArr(node) {
        var ts = [];
        if (node.nodes) {
            for (x in node.nodes) {
                ts.push(node.nodes[x].nodeId);
                if (node.nodes[x].nodes) {
                    var getNodeDieDai = getChildNodeIdArr(node.nodes[x]);
                    for (j in getNodeDieDai) {
                        ts.push(getNodeDieDai[j]);
                    }
                }
            }
        } else {
            ts.push(node.nodeId);
        }
        return ts;
    }

    // 选中所有子节点时，选中父节点 取消子节点时取消父节点
    function setParentNodeCheck(node) {
        var parentNode = $("#menu-tree").treeview("getNode", node.parentId);
        if (parentNode.nodes) {
            $("#menu-tree").treeview("checkNode", parentNode.nodeId);
        }
    }

    $.ajax({
        type: "post",
        url: $('#sub').attr("link") + "/system/role/authorize/menus",
        data: {
            "role_id": $('#role_id').val()
        },
        success: function (result) {
            $('#menu-tree').treeview({
                data: result,// 数据源
                levels: 1,
                color: "#428bca",
                showCheckbox: true,   //是否显示复选框

                onNodeChecked: function (event, node) {//选中节点
                    // nodes.push(node.nodeId);
                    setParentNodeCheck(node);
                },

                onNodeUnchecked: function (event, node) {//取消选中节点
                    // 取消父节点 子节点取消
                    var childNodes = getChildNodeIdArr(node);    //获取所有子节点
                    $('#menu-tree').treeview('uncheckNode', [childNodes, {silent: true}]);

                }
            });
        },
        error: function () {
            layer.msg("树形结构加载失败！", {icon: 1})
        }
    });

//注意：parent 是 JS 自带的全局对象，可用于操作父页面
    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    $("#sub").click(function () {
        var nodes = $('#menu-tree').treeview('getChecked');
        var menu_ids = new Array();
        if(undefined !=nodes && nodes.length > 0){
            $.each(nodes, function(index,node){
                menu_ids.push(node.nodeId + 1)
            });
        }
        var url = $(this).attr("link") + "/system/role/authorize";
        var postData = {
            role_id:$("#role_id").val(),
            menu_ids:menu_ids
        };
        $.post(url,postData,
            function(data){
                parent.layer.close(index);
                parent.layer.msg("菜单分配成功", {icon: 1});
            },
            "json");
    });

});