var chart;
$(function(){

   var vm  = new Vue({
       el: '#dimension-type',
       data: {
           dimension: 'fatal',
           percentMap:{},
           summeryinfo:{}
       },
       methods:{
           switchDimension:function(d){
               this.dimension = d;
               visualize(d);
               // tableList(d);

               var self = this;
               postJson(baseUrl+"-fatal-percent?version="+currentVersion+"&duration="+currentDuration+"&type="+d,{},function(data){
                   self.percentMap = Object.assign({}, data.data);
               });
           }
       },
       created:function(){
            var self = this;

           postJson(baseUrl+"-fatal-percent?version="+currentVersion+"&duration="+currentDuration+"&type="+this.dimension,{},function(data){
               self.percentMap = Object.assign({}, data.data);
           });

            postJson(baseUrl+"-fatal-show?version="+currentVersion+"&duration="+currentDuration+"&type="+this.dimension,{},function(data){
                self.summeryinfo = Object.assign({}, self.summeryinfo,data.data);
            });
       },
       mounted:function(){
           visualize(this.dimension);
       }
   });

    tableList();

});

function tableList(dimension) {
    var url = baseUrl+"-fatal-list?version="+currentVersion+"&duration="+currentDuration+"&type="+dimension;
    var columns =  [
        { data: 't_bug_attach',
          "render": function (data,type,row) {
            var html = '<a id="t_bug_attach" href="'+baseUrl  + '/' + row.index+  '/' + row.id +'" target = "_blank">' + data + '</a>';
            return html;
          }
        },
        { data: 't_bug_desc' },
        { data: 'initInfo.t_app_ver'},
        { data: 't_time' }
    ];
    var dataTable = $("#table_local").dataTable(tableParam(url,columns,"sf"));
}

function visualize(dimension) {
    postJson(baseUrl+"-fatal-visualize?version="+currentVersion+"&duration="+currentDuration+"&type="+dimension,{},function(data){
        var data1 = data.data.dataList;
        var ds = new DataSet();
        var dv = ds.createView();
        if(chart)chart.destroy();
         chart = new G2.Chart({
            container: 'bugchart',
            forceFit: true,
            height: 300
        });
        const defs = {
            't_time': {
                type: 'time', // 指定 time 类型
                mask: 'YYYY-MM-DD HH:mm:ss' // 指定时间的输出格式
            }
        };
        chart.source(dv, defs);

        chart.tooltip({
            crosshairs: {
                type: 'line'
            }
        });
        chart.line().position('t_time*v_count').color('dimension');
        chart.point().position('t_time*v_count').color('dimension').size(4).shape('circle').style({
            stroke: '#fff',
            lineWidth: 1
        });
        chart.render();
        dv.transform({
            type: 'fold',
            fields:  data.data.fieldList, // 展开字段集
            key: 'dimension', // key字段
            value: 'v_count', // value字段
        });


        dv.source(data1);
    });
}
