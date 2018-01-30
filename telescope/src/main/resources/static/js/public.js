$(function(){
    G2.track(false);
    if($.fn.dataTable)
    {
        var tableDefaults = {
            //lengthMenu: [5, 10, 20, 30],//这里也可以设置分页，但是不能设置具体内容，只能是一维或二维数组的方式，所以推荐下面language里面的写法。
            serverSide: true,//如果是服务器方式，必须要设置为true
            processing: true,//设置为true,就会有表格加载时的提示
            paging: true,//分页
            pagingType: "full_numbers",//分页样式的类型
            ordering: false,//是否启用排序
            searching: false,//搜索
            dom:"tlipr",
            language: {
                lengthMenu: '每页显示 <select class="form-control">' + '<option value="10">10</option>' + '<option value="20">20</option>' + '<option value="30">30</option>' + '<option value="40">40</option>' + '<option value="50">50</option>' + '</select> 条记录',
                // search: '<span class="label label-success">搜索：</span>',//右上角的搜索文本，可以写html标签
                paginate: {//分页的样式内容。
                    previous: "上一页",
                    next: "下一页",
                    first: "第一页",
                    last: "最后"
                },

                zeroRecords: "无数据记录",//table tbody内容为空时，tbody的内容。
                //下面三者构成了总体的左下角的内容。
                info: "显示第 _START_ 到第 _END_ 条，总共 _PAGES_ 页",//左下角的信息显示，大写的词为关键字。
                infoEmpty: "无数据记录",//筛选为空时左下角的显示。
                infoFiltered: ""//筛选之后的左下角筛选提示，
            }
        };
        $.extend($.fn.dataTable.defaults,tableDefaults)
    }
});
function postFrom(url,form,callback)
{
    $.ajax({
        url:url,
        type:"post",
        data:form,
        processData:false,//用于对data参数进行序列化处理
        contentType:false,//application/x-www-form-urlencoded; charset=UTF-8 这是表单格式会再次拼装。如果已经是表单就不需要格式了
        success:function(data){
            if(data.code == 0)
            {
                callback(data);
            }
            else
            {
                layer.alert(data.message, {
                    skin: 'layui-layer-lan',
                    icon:2
                    ,closeBtn: 0
                    ,anim: 0 //动画类型
                });
            }
        },
        error:function(e){
            layer.alert('错误', {
                skin: 'layui-layer-lan'
                ,closeBtn: 0
                ,anim: 0 //动画类型
            });
        }
    });
}
function postJson(url,data,callback)
{
    $.ajax({
        url:url,
        type:"post",
        data:data,
        processData:true,//用于对data参数进行序列化处理
        contentType:"application/json; charset=utf-8",//application/x-www-form-urlencoded; charset=UTF-8 这是表单格式会再次拼装。如果已经是表单就不需要格式了
        success:function(data){
            if(data.code == 0)
            {
                if(callback) callback(data);
            }
            else
            {
                layer.alert(data.message, {
                    skin: 'layui-layer-lan',
                    icon:2
                    ,closeBtn: 0
                    ,anim: 0 //动画类型
                });
            }
        },
        error:function(e){
            layer.alert('错误', {
                skin: 'layui-layer-lan'
                ,closeBtn: 0
                ,anim: 0 //动画类型
            });
        }
    });
}
function tableParam(url,columns,id) {
    var tableDefaults = {
        ajax: {
            type: 'post',
            url: url,
            dataType:"json",
            processData: false,
            contentType: false,
            data: function (d) {
;               var condition= new FormData(document.getElementById(id));
                condition.append("page",d.start / d.length);
                condition.append("size",d.length);
                condition.append("draw",d.draw);
                // var obj = {};
                // obj.page = d.start / d.length;
                // obj.size = d.length;
                // obj.draw = d.draw;
                return condition;
            }
        },
        //使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
        //data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
        columns: columns
    }
    return tableDefaults;
}

function datetimepicker(id) {
    $('#'+id).datetimepicker({
        autoclose: true,
        todayHighlight: true,
        language:"zh-CN",
        minView: 0,
        minuteStep:1,
        format:"yyyy-mm-dd hh:ii:ss"
    });
}
