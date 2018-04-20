var RedeemController = app.controller('RedeemController', function($rootScope,$scope,$http,$routeParams,ngDialog) {
    // adUrlFind('/business#redeem',window.location.origin);
    $scope.query = {};

    $scope.clearStartDate = function(event){
        $scope.query.startDate = '';
        event.stopPropagation();
    };

    $scope.clearEndDate = function(event){
        $scope.query.endDate = '';
        event.stopPropagation();
    };


    $scope.newRedeemCode = function(){
        $scope.code = {faceValue:10000};
        $scope.addCode = true;
    };

    $scope.adChangePageSize=function () {
        $scope.redeemCodeData(0);
    };

    $scope.redeemCodeData = function(page){
        $scope.query.page = page;
        $http.post(ctx + '/business/redeem/data',$scope.query)
            .success(function(data, status) {
                $scope.pageResult = data;
                $scope.query.code = undefined;
                $scope.query.pageSize=$scope.pageResult.size;
            })
            .error(function(data, status) {
                ngDialog.alert('加载兑换码数据失败');
            });
    };

    if($routeParams.code != undefined){
        $scope.query.code = $routeParams.code;
    }

    $scope.createRedeemCode = function(){
        $scope.processing = true;
        $http.post(ctx + '/business/redeem/create',$scope.code)
            .success(function(data, status) {
                $scope.processing = false;
                if(data.success){
                    toastr.success('创建兑换码成功', '提示');
                    $scope.addCode = false;
                    $scope.redeemCodeData(0);
                    $scope.calendarData();
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                $scope.processing = false;
                ngDialog.alert('申请兑换码失败');
            });
    };

    $scope.redeemCodeData(0);

    /* config object */
    $scope.uiConfig = {
        calendar:{
            height: 700,
            editable: true,
            header:{
                left: '',
                center: 'title',
                right: 'today prev,next'
            }
        }
    };

    $scope.uiConfig.calendar.dayNames = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
    $scope.uiConfig.calendar.monthNames = ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"];
    $scope.uiConfig.calendar.dayNamesShort = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];

    $scope.eventSources = [];

    $scope.calendarData = function(){
        $scope.eventSources.shift();
        $http.post(ctx + '/business/redeem/calendarData',{})
            .success(function(data, status) {
                if(data.success){
                    angular.forEach(data.result, function(re){
                        re.start = new Date(re.start);
                        re.end = new Date(parseInt(re.end) + 1000); // 加1秒
                    });
                    $scope.eventSources.push(data.result);
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('加载日历兑换码数据失败');
            });
    };
    $scope.calendarData();

});