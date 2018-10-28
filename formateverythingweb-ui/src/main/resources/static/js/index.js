$(function () {
    $(".tool-box li").click(function () {
       var url=$(this).find("span").attr("data-url");
       window.location.href=url;
    });
});



