//获取上下文文档对象
var clock = document.getElementById('clock');
var ctx = clock.getContext('2d');

//仪表盘中心点坐标及半径
var pointX = 400;
var pointY = 400;
var R = 300;

/***
 * 画指针
 * @param linewidth 画线的宽度，即粗细
 * @param linecolor 画线的颜色
 * @param angle 角度
 * @param idx   角度基数，angle*idx为本次真实需要旋转的角度
 * @param startx
 * @param starty
 * @param endx
 * @param endy
 */
function drawPointer(linewidth, linecolor, angle, idx, startx, starty, endx, endy) {
    ctx.save();                  //先保存当前画布
    ctx.lineWidth = linewidth;      //设置画笔的宽度
    ctx.strokeStyle = linecolor;    //设置画笔的颜色
    ctx.translate(pointX, pointY);     //重置异次元空间的原点坐标
    ctx.rotate(angle * idx * Math.PI / 180);  //设置旋转的角度，参数是弧度
    // console.log(angle,idx,angle * idx * Math.PI / 180);
    ctx.beginPath();
    ctx.moveTo(startx, starty);
    ctx.lineTo(endx, endy);
    ctx.closePath();            //先闭合路径，再画线
    ctx.stroke();               //开始画线
    ctx.restore();              //将旋转后的线段返回给画布
}

/***
 * 画刻度
 * @param linesize  总共画linesize条线
 * @param linewidth 线的宽度，即粗细
 * @param linecolor 画线的颜色
 * @param angle     角度
 * @param startx
 * @param starty
 * @param endx
 * @param endy
 */
function drawScale(linesize, linewidth, linecolor, angle, startx, starty, endx, endy) {
    for (var idx = 0; idx < linesize; idx++) {
        drawPointer(linewidth, linecolor, angle, idx, startx, starty, endx, endy);
    }
}

//画表盘的中心点, 黑实心，红色边框
function drawFill() {
    ctx.save();
    ctx.lineWidth = 2;      //设置画笔的宽度
    ctx.strokeStyle = "#ff0000";    //设置画笔的颜色
    ctx.beginPath();
    ctx.arc(pointX, pointY, 4, 0, Math.PI * 2, false); //arc方法：画圆或者画弧
    ctx.closePath();
    ctx.fillStyle = "#000000";
    ctx.fill();
    ctx.stroke();
    ctx.restore();
}

/**
 * 绘制仪表盘上的数字
 */
function drawNumber() {
    //绘制仪表盘上的数字
    for (var i = 1; i <= 12; i++) {
        var text = i * 5;
        var radian = (i * 30) * (Math.PI * 2 / 360);
        var R = 240;
        var offsetX = -17;
        var offsetY = 4;
        var x = Math.sin(radian) * R + offsetX; //不加上offset会发现所有内容均向在偏移了一些，可能是文字下标定位有关
        var y = -Math.cos(radian) * R + offsetY;

        ctx.save();
        ctx.translate(pointX, pointY);     //重置异次元空间的原点坐标
        //设置字体样式
        ctx.font = "600 30px Courier New";
        // //设置字体填充颜色
        ctx.fillStyle = "white";
        //从坐标点(50,50)开始绘制文字
        ctx.fillText("" + text, x, y);
        ctx.restore();
    }
}

/**
 * 绘制仪表盘上的数字
 */
function drawData(formatObj) {
    var text=formatObj.mm+":"+formatObj.ss+"."+formatObj.sss;

    //绘制仪表盘上的数字
    var x = -60;
    var y = 100;

    ctx.save();
    ctx.translate(pointX, pointY);     //重置异次元空间的原点坐标
    //设置字体样式
    ctx.font = "600 25px Courier New";
    // //设置字体填充颜色
    ctx.fillStyle = "white";
    //从坐标点(50,50)开始绘制文字
    ctx.fillText("" + text, x, y);
    ctx.restore();
}

//画时钟
function drawClock(startTime, endTime, formatObj) {
    ctx.clearRect(0, 0, 800, 800);  //清空整个画布

    var diffTime = endTime - startTime;
    var sec = Math.floor(diffTime % 60000 / 1000);
    var milliSec = diffTime % 1000;
    var milliSecFix = sec * 5 + milliSec / 200; //仪表盘每200ms一个刻度，每1s五个刻度
    // console.log(diffTime, sec, milliSec, milliSecFix);

    drawNumber(); //刻度上面的数字
    drawScale(300, 2, "#4c4c4c", 1.2, 0, 270, 0, 280);      //画1/5秒刻度，每秒再分5档
    drawScale(60, 2, "#ffffff", 6, 0, 260, 0, 280);         //画秒刻度

    drawPointer(2, "red", 1.2, milliSecFix, 0, 30, 0, -280);       //画秒针

    drawFill(); //画表盘的中心点
    drawData(formatObj); //计时器启动后使用的时间
}



/***************定时器相关********************************/
var jishiqiTimer;  //timer
var startTime;     //记录每次启动计时器的开始时间
var formatObj = {};//格式化对象
var formatObjArr=[];
var formatObjArr=[];

//初始化
formatObj = formatTime(0, 0);
drawClock(0, 0,formatObj);

function mystart() {
    var endTime = new Date().getTime();
    //format
    var formatObj = formatTime(startTime, endTime);
    console.log(formatObj)
    drawClock(startTime, endTime,formatObj);

    // jishiqiDiv.html(str);
}
function formatTime(startTime, endTime) {
    var difftime = endTime - startTime;
    return doFormatTime(difftime);
}
function doFormatTime(difftime) {
    if (difftime > 3600000) { //此计时器最多只算处理60分钟
        formatObj.mm = "60";
        formatObj.ss = "00";
        formatObj.sss = "000";
        formatObj.difftime=3600000;
        return formatObj ;
    }
    var mm;
    var ss;
    var sss;
    if (difftime >= 60000) {
        mm = Math.floor(difftime / 60000) + ""; //Math.floor()目的是为了向下取整, +""目的为了转成字符串
        ss = Math.floor(difftime % 60000 / 1000) + "";
        sss = Math.floor(difftime % 1000) + "";
    } else if (difftime >= 1000) {
        mm = "00";
        ss = Math.floor(difftime % 60000 / 1000) + "";
        sss = Math.floor(difftime % 1000) + "";
    } else {
        mm = "00";
        ss = "00";
        sss = difftime + "";
    }
    mm = buquan(mm, 2);
    ss = buquan(ss, 2);
    sss = buquan(sss, 3);


    formatObj.mm = mm;
    formatObj.ss = ss;
    formatObj.sss = sss;
    formatObj.difftime=difftime;
    return formatObj;
}

function buquan(a, length) {
    while (a.length < length) {
        a = "0" + a;
    }
    return a;
}
function showHistoryData() {
    var historyDataDiv=$(".history-data");
    historyDataDiv.html("");
    $.each(formatObjArr,function (i, obj) {
        var text=obj.mm+":"+obj.ss+"."+obj.sss;
        var str="<div><span class='history-tip'>"+(i+1)+"</span><span class='history-text'>" + text + "</span></div>"
        historyDataDiv.append(str);
    })
}
function addHistory(formatObj) {
    if(formatObj.mm=="00"&&formatObj.ss=="00"&&formatObj.sss=="000"){
        return;
    }
    var tempObj=$.extend({},formatObj);
    formatObjArr.push(tempObj);
    if(formatObjArr.length>20){ //缓存最多只存20条
        formatObjArr.splice(0,formatObjArr.length-20);
    }
}


function statisticHistory() {
    if(formatObjArr.length==0){
        $(".statistic-data .fast span").text("00:00.000");
        $(".statistic-data .slow span").text("00:00.000");
        $(".statistic-data .average span").text("00:00.000");
        return;
    }
    $(".statistic-data").removeClass("none");
    var fast={};
    var slow={};
    var average={};
    var total=0;
    $.each(formatObjArr,function (i, obj) {
        if(!fast.difftime){ //第1次遍历的时候fast、slow均没有初始化值
            fast=obj;
        }
        if(!slow.difftime){
            slow=obj;
        }
        if(obj.difftime<fast.difftime){
            fast=obj;
        }
        if(obj.difftime>slow.difftime){
            slow=obj;
        }
        total+=obj.difftime;

    });
    var averageDifftime=Math.round(total/formatObjArr.length);
    average=doFormatTime(averageDifftime);

    var fasttext=fast.mm+":"+fast.ss+"."+fast.sss;
    var slowtext=slow.mm+":"+slow.ss+"."+slow.sss;
    var averagetext=average.mm+":"+average.ss+"."+average.sss;
    $(".statistic-data .fast span").text(fasttext);
    $(".statistic-data .slow span").text(slowtext);
    $(".statistic-data .average span").text(averagetext);
}

//事件相关
$(".history-data").on("mouseover",".history-text",function(){
    var historyDataOne=$(this);
    if(historyDataOne.find(".tip").length==0){
        historyDataOne.append("<span class='tip'>双击删除我吧</span>");
    }
});
$(".history-data").on("mouseout",".history-text",function(){
    var historyDataOne=$(this);
    if(historyDataOne.find(".tip").length>0){
        historyDataOne.find(".tip").remove();
    }
});
$(".history-data").on("dblclick",".history-text",function(){
    var historyDataOne=$(this);
    if(historyDataOne.find(".tip").length>=0){
        //
        var idx=parseInt(historyDataOne.parent().find('.history-tip').text())-1;
        formatObjArr.splice(idx,1);

        showHistoryData();
        statisticHistory();
    }
});
$(document).keydown(function (event) {
    if (event.keyCode != 32 && event.keyCode != 13) { //空格 或者 回车
        return;
    }
    if (jishiqiTimer != null) {
        clearInterval(jishiqiTimer);
        jishiqiTimer = null;
        addHistory(formatObj);

        showHistoryData();
        statisticHistory();
    } else {
        startTime = new Date().getTime();
        jishiqiTimer = setInterval(mystart, 123);
    }

});



