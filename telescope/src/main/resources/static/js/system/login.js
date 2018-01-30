$(function(){
    $("form").submit(function() {
        var v = $.md5($("#show-password").val());
        $("#password").val(v);
        return true;
    });
});