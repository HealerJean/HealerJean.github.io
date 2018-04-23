var SpecialMoneyApplyAddController = app.controller('SpecialMoneyApplyAddController', function($rootScope,$scope,$http,$routeParams,$location,ngDialog,FileUploader) {
    // adUrlFind('/business#/money/apply',window.location.origin);

    $scope.pageResult = {};
    $scope.processing = false;
    $scope.specialMoney = {};
    $scope.query = {
        status:"1"
    };

    $scope.selected = {};
    $scope.specialMoney = {
        startDate:moment().startOf('year').format("YYYY-MM-DD HH:mm:ss")
        ,endDate:moment().endOf('year').format("YYYY-MM-DD HH:mm:ss")
        ,invalid:1
    };

    $scope.loadUserList = function(){
        $scope.processing = true;
        $http.post(ctx + '/business/special/money/apply/userList')
            .success(function(data, status) {
                $scope.processing = false;
                if(data.success){
                    $scope.userList = data.result;
                }else {
                    ngDialog.alert(data.message);
                }

            })
            .error(function(data, status) {
                $scope.processing = false;
                ngDialog.alert("加载广告主列表失败!");
            });
    };

    $scope.saveSpecialMoney = function(){
        if($scope.selected.special == undefined){
            toastr.warning("请选择广告主",'提示');
            $scope.processing = false;
            return;
        }
        var attachs = [];
        for(var i = 0 ; i < $scope.attachs.length ; i ++ ){
            if($.trim($scope.attachs[i].attachPic) != ''){
                attachs.push($scope.attachs[i].attachPic);
            }
        }

        /*if(attachs.length == 0){
            toastr.warning("请至少上传一个附件",'提示');
            return;
        }*/
        $scope.processing = true;

        $scope.specialMoney.userId = $scope.selected.special.userId;

        if(attachs.length>0){
            $scope.specialMoney.attachs = attachs.join(",");
        }

        $http.post(ctx + '/business/special/money/apply/save',$scope.specialMoney)
            .success(function(data, status) {
                $scope.processing = false;
                if(data.success){
                    $location.path("/money/apply").replace();
                }else {
                    ngDialog.alert(data.message);
                }

            })
            .error(function(data, status) {
                $scope.processing = false;
                ngDialog.alert("保存特批额度申请失败");
            });
    };

    $scope.loadUserList();

    //通过取消
    $scope.cancelSpecialMoney=function(){
        $location.path("/money/apply").replace();
    };

    //============================================================
    //uploader
    //============================================================

    $scope.attachs = [];
    $scope.attachs.push({attachPic:'',btnText:'上传附件'});

    $scope.addTargetAttachs = function(){
        $scope.attachs.push({attachPic:'',btnText:'上传附件'});
    };

    $scope.removeTargetAttachs = function(index){
        $scope.attachs.splice(index,1);
    };

    $scope.target = 0;

    $scope.selectFile = function(target){
        $scope.target = target;
    };

    var uploader = $scope.uploader = new FileUploader({
        autoUpload:true,
        url: ctx + '/business/special/money/apply/upload'
    });

    // FILTERS
    uploader.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            if ('|jpg|png|jpeg|gif|'.indexOf(type) < 0) {
                $scope.errMessage = "只能上传图片文件【jpg|png|jpeg|gif】";
                return false;
            } else {
                $scope.errMessage = undefined;
                return true;
            }
        }
    },{
        name: 'sizeFilter',
        fn: function(fileItem /*{File|FileLikeObject}*/, options) {
            if (fileItem.size > (1024*1024*2)) {
                $scope.errMessage = "文件大小不能超过2MB";
                return false;
            } else {
                $scope.errMessage = undefined;
                return true;
            }
        }
    });

    uploader.onBeforeUploadItem = function(item){
        $scope.uploading = true;
    };

    // CALLBACKS
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        $scope.uploading = false;
        if (response.success){
            $scope.attachs[$scope.target].attachPic = response.result;
            $scope.attachs[$scope.target].btnText = '重新上传';
        } else {
            ngDialog.alert(response.message);
        }
    };


});