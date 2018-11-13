$(function () {

    $(document).keydown(function (event) {
        console.log(event.ctrlKey + "==" + event.which)
        if (event.ctrlKey && (event.which == 69 || event.which == 81 )) {  //Ctrl+E
            $("#formatBtn").click();
            return false;
        }
    });

    var BootstrapTable = function () {
        var myTable = new Object();
        //初始化Table
        myTable.init = function () {
            $('#articleTableList').bootstrapTable('destroy');

            $('#articleTableList').bootstrapTable({
                url: "/article/list.json",        //请求后台的URL（*）
                contentType: "application/x-www-form-urlencoded", //form-data
                method: 'post',                      //请求方式（*）
                queryParams: myTable.queryParams,//传递参数（*）
                showToggle:false,                     //是否显示 切换试图（table/card）按钮
                showExport:true,
                exportDataType:'all',               //basic all
                toolbar: '#toolbar',                //工具按钮用哪个容器
                striped: false,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                sortable: false,                    //是否启用排序
                sortOrder: "asc",                   //排序方式
                sidePagination: "server",          //分页方式：client客户端分页，server服务端分页（*）
                pageNumber: 1,                      //初始化加载第一页，默认第一页
                pageSize: 20,                       //每页的记录行数（*）
                pageList: [20, 50, 100],        //可供选择的每页的行数（*）
                strictSearch: true,
                clickToSelect: true,               //是否启用点击选中行
//              height: 540,                   //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                uniqueId: "id",                    //每一行的唯一标识，一般为主键列
                cardView: false,                  //是否显示详细视图
                detailView: false,                //是否显示父子表
                formatLoadingMessage:function(){   //加载中
                    return "加载中，请稍后......";
                },
                formatShowingRows:function(pageFrom, pageTo, totalRows){   //	'Showing %s to %s of %s rows'
                    return "当前显示第 "+pageFrom+" 个到第 "+pageTo+" 个，共 "+totalRows+" 个，";
                },
                formatRecordsPerPage:function(pageNumber){   //'%s records per page'
                    return '每页显示数量设置 ' + pageNumber + ' ';
                },
                formatNoMatches: function () {     //没有数据时显示的内容
                    var tmp='';
                    tmp +='<div class="noresult">';
                    // tmp +='	<div class="logo"><img style="width: 68px;" src="/static/imgs/noresult.png" ></div>';
                    tmp +='	<hr class="std-hr">';
                    tmp +='	<div class="tip">';
                    tmp +='		<div class="tip1">搜索无结果</div>';
                    tmp +='	</div>';
                    tmp +='</div>';
                    return tmp;
                },
                columns: [{
                    field : 'Number',
                    title : '行号',
                    formatter : function(value, row, index) {
                        return index + 1;
                        //var page = $('#articleTableList').bootstrapTable("getPage");
                        //return page.pageSize * (page.pageNumber - 1) + index + 1;
                    }
                }, {
                    field: 'title',
                    title: '标题',
                    formatter:function(value,row,index){
                        var contentType=row.contentType;
                        // var contentTypeStr=row.contentType==1?"[原]":"[转]";
                        var contentTypeStr="<span class='content_type_"+row.contentType+"'></span>"
                        var htmltmp=
                            "<div class='title-box'>"+
                            contentTypeStr+
                            "<a href='/article/detail.html?id="+row.id+"' target='_blank'>"+row.title+"</a>"+
                            "<span> ("+row.updateTime+")</span>"+
                            "</div>" ;
                        return htmltmp;
                    }
                }, {
                    field: 'readCount',
                    title: '阅读量'
                }, {
                    field: 'commentCount',
                    title: '评论量'
                }, {
                    field: 'csdnLink',
                    title: 'csdn中的原始链接',
                    formatter:function(value,row,index){
                        var htmltmp="<a class='toAdd' href='"+value+"' target='_blank'>"+value+"</a>";
                        return htmltmp;
                    }
                }, {
                    field: 'graspTime',
                    title: '抓取时间戳',
                    formatter:function(value,row,index){
                        // var uniqueCode=row.uniqueCode;
                        // var htmltmp="<a class='toAdd' data-id='"+row.uniqueCode+"' thirdCategoryId='"+row.thirdCategoryId+"'"+
                        //     "href='#'>生成商品"+
                        //     "</a>";
                        return value;
                    }

                }],
                onLoadSuccess: function (data) {  //加载成功时执行
                    //console.log("data="+data);
                    // var operTag=data.operTag;
                    // $("#operTag").val(operTag);
                    // var source=data.source||"";
                    // $("#source").val(source);
                    // //如果是搜索，且搜索的结果集有内容就展示筛选条件
                    // if($("#productName").val().trim()!="" || $("#productUpc").val().trim()!=""){
                    //     if(data.total>0 && $("#myTabDiv2").hasClass("none")){
                    //         $("#myTabDiv2").removeClass("none");
                    //     }
                    // }


                    // 未选择商品分类时；商品数量结果集<2页的，始终显示，商品数量结果集>=2页，从第2页开始显示
                    // 已选择商品分类，始终不显示 自主创建
                    // if($("#subtype").val()==""||$("#subtype").val()=="0"){
                    //     if(data.total<20){
                    //         appendCreateBtn();
                    //     }else{
                    //         var pageNumber = $('#articleTableList').bootstrapTable('getOptions').pageNumber;
                    //         console.log("pageNumber="+pageNumber);
                    //         if(pageNumber>=3){
                    //             appendCreateBtn();
                    //         }else{
                    //             hideCreateBtn();
                    //         }
                    //     }
                    // }



                },
                onLoadError: function () {  //加载失败时执行
                    //layer.msg("加载数据失败", {time : 1500, icon : 2});
                }
            });
        };

        //得到查询的参数
        myTable.queryParams = function (params) {
            //console.log(params);
            // bootstrapTable分页时使用的参数是 limit:每页显示条数；offset:从第几条开始显示(下标从0开始)
            //转换成后台可识别的 pageSize,pageNumber(下标从1开始，即第1页时pageNumber=1)
            var pageSize = 10000;
            var page = 1;
            if(params && params.limit){
                pageSize = params.limit;
                page = params.offset/params.limit +1 ;
            }
            var temp = {
                page: page,
                pageSize: pageSize,

                upcCode: $("#productUpc").val()
            };
            console.log("params="+JSON.stringify(temp));
            return temp;
        };

        return myTable;
    };

    function appendCreateBtn(){
        if($(".noresultDiv").length==0){
            var htmltmp='<div class="noresultDiv">'+
                '<div>'+
                '如果以上还没有符合您需求的商品，您可以 <a class="create2 createproduct2">自主创建商品</a>'+
                '</div>'+
                '</div>';
            $(".fixed-table-pagination").before(htmltmp);
        }else  if($(".noresultDiv").hasClass("none")){
            $(".noresultDiv").removeClass("none");
        }
    }
    function hideCreateBtn(){
        $(".noresultDiv").addClass("none");
    }

    //1.初始化Table
    var oTable = new BootstrapTable();
    oTable.init();



});



