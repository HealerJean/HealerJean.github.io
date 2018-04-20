var SpecialMoneyApplyController = app.controller('SpecialMoneyApplyController', function($rootScope,$scope,$http,$routeParams,$location,ngDialog) {
    // adUrlFind('/business#/money/apply',window.location.origin);

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
        status:"1"
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
        endDate: moment().endOf('day'),
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
            $scope.specialMoneyData(0);
        }
    }, false);

    $scope.pageResult = {};
    $scope.processing = false;

    $scope.specialMoney = {invalid:"1"};

    $scope.adChangePageSize=function () {
        $scope.specialMoneyData(0);
    };

    $scope.specialMoneyData = function(page){
        $scope.processing = true;
        $scope.query.page = page;
        $http.post(ctx + '/business/special/money/apply/data',$scope.query)
            .success(function(data, status) {
                $scope.processing = false;
                if(data.success){
                    $scope.specialMoney = undefined;
                    $scope.pageResult = data.result;
                    $scope.query.pageSize=$scope.pageResult.size;
                }else {
                    ngDialog.alert(data.message);
                }

            })
            .error(function(data, status) {
                $scope.processing = false;
                ngDialog.alert("加载特批额度申请记录列表信息失败");
            });
    };

    $scope.specialMoneyData(0);

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
            $http.post(ctx + '/business/special/money/apply/withdraw',{
                id:$scope.specialMoney.id
            })
                .success(function(data, status) {
                    $scope.processing = false;
                    if(data.success){
                        ngDialog.close();
                        $scope.specialMoneyData(0);
                    }else {
                        ngDialog.alert(data.message);
                    }
                })
                .error(function(data, status) {
                    $scope.processing = false;
                    ngDialog.alert("撤回特批额度申请记录失败");
                });
        }
    };

    $scope.endApply = function(){
        if(!$scope.processing){
            $scope.processing = true;
            $http.post(ctx + '/business/special/money/apply/endApply',{
                id:$scope.specialMoney.id
            })
                .success(function(data, status) {
                    $scope.processing = false;
                    if(data.success){
                        ngDialog.close();
                        $scope.specialMoneyData(0);
                    }else {
                        ngDialog.alert(data.message);
                    }
                })
                .error(function(data, status) {
                    $scope.processing = false;
                    ngDialog.alert("结束特批额度申请记录失败");
                });
        }
    };




    //撤回操作
    $scope.withDrawInfo=function(item){
        if(item == undefined){
            ngDialog.alert("请选择需要撤回的记录");
            return;
        }
        /*if($scope.specialMoney == undefined){
            ngDialog.alert("请选择需要撤回的记录");
            return;
        }*/
        $scope.specialMoney = item;
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

    //结束操作
    $scope.endApplyInfo=function(item){
        if(item == undefined){
            ngDialog.alert("请选择需要结束的记录");
            return;
        }
        $scope.specialMoney = item;
        ngDialog.open({
            template: $('#endApplyInfo').html(),
            width:'450px',
            plain:true,
            cache:false,
            scope: $scope
        });
    };

    //结束取消
    $scope.endApplyCancel=function(){
        ngDialog.close();
    };

});