var e = app.controller('SpreadCouponApplyAddController', function($rootScope,$scope,$http,$routeParams,$location,ngDialog,FileUploader) {
    // adUrlFind('/business#/coupon/apply',window.location.origin);

    $scope.pageResult = {};
    $scope.processing = false;

    $scope.selected = {};
    $scope.coupon = {};

    $scope.loadUserList = function(){
        $scope.processing = true;
        $http.post(ctx + '/business/spread/coupon/apply/userList')
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

    $scope.saveCoupon = function(){
        $scope.processing = true;
        if($scope.selected.user == undefined){
            ngDialog.alert("请选择广告主!");
            $scope.processing = false;
            return;
        }
        if($scope.coupon.type == undefined || $scope.coupon.type == ''){
            ngDialog.alert("请选择代金券类型!");
            $scope.processing = false;
            return;
        }
        if($scope.coupon.inureDate == undefined ){
            ngDialog.alert("请填写生效日期!");
            $scope.processing = false;
            return;
        }

        if($scope.coupon.expireDate == undefined ){
            ngDialog.alert("请填写失效日期!");
            $scope.processing = false;
            return;
        }

        var attachs = [];
        for(var i = 0 ; i < $scope.attachs.length ; i ++ ){
            if($.trim($scope.attachs[i].attachPic) != ''){
                attachs.push($scope.attachs[i].attachPic);
            }
        }

        // if (attachs.length == 0) {
        //     $scope.processing = false;
        //     toastr.warning("请至少上传一个附件", '提示');
        //     return;
        // }

        if(attachs.length>0){
            $scope.coupon.attachs = attachs.join(",");
        }

        $scope.coupon.userId = $scope.selected.user.userId;

        var typeDesc = '';
        if($scope.coupon.type == '1'){
            typeDesc = '应用推广代金券';
        } else if ($scope.coupon.type == '2'){
            typeDesc = '交互广告代金券';
        }

        ngDialog.confirm('您申请的是【'+typeDesc+'】，确定提交吗？').then(function () {
            $http.post(ctx + '/business/spread/coupon/apply/save',$scope.coupon)
                .success(function(data, status) {
                    $scope.processing = false;
                    if(data.success){
                        $location.path("/coupon/apply").replace();
                    }else {
                        ngDialog.alert(data.message);
                    }

                })
                .error(function(data, status) {
                    $scope.processing = false;
                    ngDialog.alert("保存代金券申请失败");
                });
        }, function () {
            $scope.processing = false;
        });
    };

    $scope.loadUserList();

    //通过取消
    $scope.cancelCoupon=function(){
        $location.path("/coupon/apply").replace();
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
        url: ctx + '/business/spread/coupon/apply/upload'
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