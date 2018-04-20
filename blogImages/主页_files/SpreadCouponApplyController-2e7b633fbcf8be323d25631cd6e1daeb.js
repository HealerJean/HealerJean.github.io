var SpreadCouponApplyController = app.controller('SpreadCouponApplyController', function($rootScope,$scope,$http,$routeParams,$location,ngDialog) {
    // adUrlFind('/business#/coupon/apply',window.location.origin);

    $scope.pageResult = {};
    $scope.processing = false;

    $('.configDobleTime').click(function () {
        daterangebtnChange()
    })

    $scope.adDate={
        startDate:moment().subtract(1, 'year').startOf('day').format("YYYY-MM-DD HH:mm:ss"),
        endDate:moment().endOf('day').format("YYYY-MM-DD HH:mm:ss"),
        startDateStr : moment().subtract(1, 'year').startOf('day').format("YYYY.MM.DD"),
        endDateStr: moment().endOf('day').format("YYYY.MM.DD"),
        index:1
    }
    $scope.query = {
        startDate:$scope.adDate.startDate,
        endDate:$scope.adDate.endDate,
    };

    $scope.adOpts = {
        timePicker:true,
        timePicker24Hour: true,
        ranges: {
            '今天': [moment().startOf('day'), moment().endOf('day')],
            '昨天': [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')],
            '过去7天': [moment().subtract(6, 'days').startOf('day'), moment().endOf('day')],
            '过去30天': [moment().subtract(29, 'days').startOf('day'), moment().endOf('day')],
            '这个月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
            '近三月': [moment().subtract(2, 'month').startOf('month'), moment().endOf('day')],
            '近一年': [moment().subtract(1, 'year').startOf('day'), moment().endOf('day')]
        },
        alwaysShowCalendars: true,
        startDate:moment().subtract(1, 'year').startOf('day'),
        endDate:moment().endOf('day'),
        maxDate: moment().endOf('day'),
        applyClass: "btn btn-primary",
        opens: "right",
        // dateNav:1
    };

    $scope.$watch('adDate', function(newDate) {
        if(newDate.index!=1){
            var start_d=newDate.startDate;
            var end_d=newDate.endDate;
            $scope.adOpts.startDate=start_d;
            $scope.adOpts.endDate=end_d;
            $scope.adDate.startDate = start_d;
            $scope.adDate.startDateStr = start_d.format("YYYY.MM.DD");
            $scope.adDate.endDate = end_d;
            $scope.adDate.endDateStr = end_d.format("YYYY.MM.DD");
            $scope.query.startDate=start_d.format("YYYY-MM-DD HH:mm:ss");
            $scope.query.endDate=end_d.format("YYYY-MM-DD HH:mm:ss");
            $scope.couponData(0);
        }
    }, false);


    $scope.userList = [];

    $scope.outputModel = {outputDataUser: []};

    $scope.coupon = {};

    $scope.adChangePageSize=function () {
        $scope.couponData(0);
    };

    $scope.couponData = function(page){
        $scope.processing = true;
        $scope.query.page = page;

        $scope.query.userIdList = [];

        angular.forEach( $scope.outputModel.outputDataUser, function( value, key ) {
            $scope.query.userIdList.push(value.id);
        });
        $http.post(ctx + '/business/spread/coupon/apply/data',$scope.query)
            .success(function(data, status) {
                $scope.processing = false;
                if(data.success){
                    $scope.coupon = undefined;
                    $scope.pageResult = data.result;
                    $scope.query.pageSize=$scope.pageResult.size;
                }else {
                    ngDialog.alert(data.message);
                }

            })
            .error(function(data, status) {
                $scope.processing = false;
                ngDialog.alert("加载推广代金券申请记录列表失败");
            });
    };

    $scope.couponData(0);

    $scope.clearStartDate = function(event){
        $scope.query.startDate = '';
        event.stopPropagation();
    };
    $scope.clearEndDate = function(event){
        $scope.query.endDate = '';
        event.stopPropagation();
    };

    $scope.withDraw = function(){
        if(!$scope.processing){
            $scope.processing = true;
            $http.post(ctx + '/business/spread/coupon/apply/withdraw',{
                id:$scope.coupon.id
            })
                .success(function(data, status) {
                    $scope.processing = false;
                    if(data.success){
                        ngDialog.close();
                        $scope.couponData(0);
                    }else {
                        ngDialog.alert(data.message);
                    }
                })
                .error(function(data, status) {
                    $scope.processing = false;
                    ngDialog.alert("作废推广代金券申请失败");
                });
        }
    };

    //撤回操作
    $scope.withDrawInfo=function(item){
        $scope.coupon = item;
        ngDialog.open({
            template: $('#withDrawInfo').html(),
            width:'450px',
            plain:true,
            cache:false,
            scope: $scope
        });
    };

    //通过取消
    $scope.withDrawCancel=function(){
        ngDialog.close();
    };

    $scope.localLang = {
        selectAll       : "全选",
        selectNone      : "清空",
        reset           : "重置",
        search          : "搜索...",
        nothingSelected : "全部"         //default-label is deprecated and replaced with this.
    };

    //设置客户下拉列表
    $scope.getUserList = function(){
        $http.post(ctx + '/business/spread/coupon/apply/getUserListByAdmId',{})
            .success(function(data, status) {
                if(data.success){
                    $scope.userList = data.result;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('获取广告主下拉列表数据失败');
            });
    };

    $scope.$on('ngRepeatFinished',function(){
        //父控制器执行操作
        $('[data-toggle="popover"]').popover({ trigger: "hover" });
    });

    $scope.getUserList();
});