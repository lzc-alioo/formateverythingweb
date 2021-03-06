$(function () {

    var param={
        id:$("#id").val()
    };

    var target=$(".detailDiv")
    $.ajax({
        type: 'POST',
        url: '/article/detail.json',
        data: param,
        dataType: 'json',
        success: function (result) {
            if (result == null) {
                target.html("系统故障，请稍后重试");
                return false;
            }
            if (result.success != 1) {
                target.html(result.msg + "[000600]");
                return false;
            }

            var tmpHtml="";
            if(result.article && result.article.content){
                tmpHtml=result.article.content;
            }else{
                tmpHtml="<p class='result-title'>查询结果为空</p>";
            }

            target.html(tmpHtml);

        },
        error: function (xhr, type, exception) {
            target.html("操作异常" + exception);
        }


    });



});



