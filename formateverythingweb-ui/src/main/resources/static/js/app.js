/**
 * Created by liuzhichong on 2017/8/7.
 */
$(function () {
    function getDateObj() {
        var weekArr = new Array("日", "一", "二", "三", "四", "五", "六");
        var date = new Date();
        var weekIdx = date.getDay();

        var currenthour = date.getHours()<10?"0"+date.getHours():""+date.getHours();
        var currentminute = date.getMinutes()<10?"0"+date.getMinutes():""+date.getMinutes();
        var currentdate = currenthour + ":" + currentminute;
        var currentweek = weekArr[weekIdx];

        var dateobj = new Object();
        dateobj.currentdate = currentdate;
        dateobj.currentweek = "星期"+currentweek;
        return dateobj;
    }

    function showDate() {
        var dateobj = getDateObj();
        $(".header-right .updown .updown-time").html(dateobj.currentdate);
        $(".header-right .updown .updown-week").html(dateobj.currentweek);
    }

    showDate();
    setInterval(showDate, 30000);

});