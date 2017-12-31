/**
 * Created by liuzhichong on 2017/1/2.
 */
$(function(){
    var uploadImages=$("#mypic");
    var imageBox = $(".picul");
    var uploadFileList = [];
    var buttonBox = $(".button-box");

    var i=1;
    $(".picul li").bind("click",function(){
       //alert("aa");
       //msg("bb",{auto:false})
       console.log("val=="+$("#mypic").val() );
       $("#mypic").click();
    }) ;
    /**
     * [bind 上传图片监听事件]
     * @method bind
     * @param  {[type]} "change" [description]
     * @param  {[type]} function (event        [description]
     * @return {[type]}          [description]
     */
    uploadImages.bind("change",function (event) {
        //if(!orderId) return false;
        var list = event.target.files;
        if((list.length + uploadFileList.length) > 3){
            msg("选择的图片不能超过三张");
            return ;
        }
        for(var i = 0, len = list.length; i < len; i++){
            file2DataUrl(list[i], serverUploadImg);
        }

        event.target.value = "";
    });

    function serverUploadImg(imgData){
        var pictureBase64 = imgData.replace(/data:.*,/i, "");
        var data = {
            functionId: 'chufang/medicine/uploadPicture',
            body: '{}',
            // body: '{pictureBase64:' + pictureBase64 +'}',
            pictureBase64: pictureBase64,
            platCode:"H5",
            appName:"paidaojia"
        };
        data.body = JSON.stringify(data.body);
        // console.log(data.body);
        $.ajax({
            type: 'POST',//沟通方式
            //url: '/client',//请求地址
            url: 'http://usecarp.jd.com/uploadTest1Controller/uploadImg',//请求地址
            // contentType: "multipart/form-data",
            contentType: "application/x-www-form-urlencoded",
            data: data,//数据
            dataType: 'json',
            success: function (data) {
                data = data || {};
                buttonBox.removeClass("sending");
                if(data.result){
                    if(uploadFileList.length < 3){
                        appendImage(imgData, data.result.pictureUrl);
                        uploadFileList.push(data.result.pictureUrl);
                        // console.log(uploadFileList);
                    }
                }else if(/^[1|2|7|8|9](0)?$/g.test(data.code)){
                    msg("网络请求超时，请重试！");
                }else if(data.code == 4){
                    msg("图片过大，无法上传");
                }else if(/^[3|5|6|1](1)?$/g.test(data.code)){
                    msg("图片格式不正确，请检查后重试");
                }else{
                    msg(data.msg || "网络请求超时，请重试！");
                }
            },
            error: function (error) {
                buttonBox.removeClass("sending");
                msg("网络请求超时，请重试！");
            }
        });
    }
    //添加预览文件
    function appendImage(url, filename){
        //创建预览图片容器
        var li = document.createElement("li");
        //imageBox.prepend(li);
        //改进一下，当单击某个li进行上传图片时，形成给人的感觉就是在当前li上添加了图片，而不是添加到最前面
        imageBox.find("li").last().before(li);

        //添加预览图片
        var image = new Image();
        li.appendChild(image);
        var remove = document.createElement("i");
        li.appendChild(remove);
        remove.className = "remove";
        remove.id = filename;
        $(remove).click(removeHandler);
        image.src = url;

        if(imageBox.find("li").length >= 4){
            imageBox.find(".add").hide();
        }
    }
    /**
     * [removeHandler 删除图片处理]
     * @method removeHandler
     * @param  {[type]}      event [description]
     * @return {[type]}            [description]
     */
    function removeHandler(event){
        var $remove = $(event.target),
            filename = event.target.id;
        $remove.parent("li").remove();
        for (var i = uploadFileList.length - 1; i >= 0; i--) {
            if(uploadFileList[i] == filename){
                uploadFileList.splice(i, 1);
                imageBox.find(".add").show();
            }
        }
    }

    /**
     * [file2DataUrl 图片转换Base64]
     * @method file2DataUrl
     * @param  {[type]}     file     [上传的图片文件]
     * @param  {Function}   callback [调用添加图片函数]
     * @return {[type]}              [null]
     */
    function file2DataUrl(file, callback){
        if(!window.FileReader){
            msg("浏览器不支持, 请更换其它方式!");
        }
        if (file.size > 5242880) {
            msg('图片过大，无法上传');
        } else if (file.type.indexOf("image") > -1){
            buttonBox.addClass("sending");
            var fileReader = new FileReader();
            fileReader.readAsDataURL(file);
            fileReader.onload = function(event){
                callback(event.target.result);
            };
        }else{
            msg("图片格式不正确，请检查后重试");
        }
    }




});