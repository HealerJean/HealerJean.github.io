var AdController = app.controller('AdController', function($rootScope,$scope,$http,ngDialog) {
    // adUrlFind('/business#ad',window.location.origin);

    $('.configDobleTime').click(function () {
        daterangebtnChange()
    })

    $scope.adDate={
        startDate:moment().subtract(29, 'days').startOf('day').format("YYYY-MM-DD HH:mm:ss"),
        endDate:moment().endOf('day').format("YYYY-MM-DD HH:mm:ss"),
        startDateStr : moment().subtract(29, 'days').startOf('day').format("YYYY.MM.DD"),
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
        startDate:moment().subtract(29, 'days').startOf('day'),
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
            $scope.adData(0);
        }
    }, false);

    $scope.setQueryUser = function(item){
        $scope.query.userName = item.nickName;
        $scope.query.userId = item.userId;
        $scope.adData(0);
    };
    $scope.removeQueryUser = function(){
        $scope.query.userName = undefined;
        $scope.query.userId = undefined;
        $scope.adData(0);
    };

    $scope.adChangePageSize=function () {
        $scope.adData(0);
    };

    $scope.adData = function(page){
        $scope.query.page = page;
        $http.post(ctx + '/business/ad/data',{page:$scope.query.page,startDate:$scope.query.startDate,endDate:$scope.query.endDate,status:$scope.query.status,userId:$scope.query.userId,pageSize:$scope.query.pageSize})
            .success(function(data, status) {
                $scope.pageResult = data;
                $scope.query.pageSize=$scope.pageResult.size;
            })
            .error(function(data, status) {
                toastr['error']('加载广告列表失败', '提示');
            });
    };

    $scope.showDetail = function(item){
        $scope.currItem = item;
        ngDialog.open({
            name:'detail',
            template: ctx + '/business/ad/detail',
            width:'650px',
            scope: $scope,
            cache:false
        });
    };

    $scope.$on('ngDialog.opened', function (e, $dialog) {
        if($dialog.name == 'detail'){
            $scope.detail = undefined;
            $http.post(ctx + '/business/ad/load',{adId:$scope.currItem.id})
                .success(function(data, status) {
                    $scope.detail = data;
                })
                .error(function(data, status) {
                    ngDialog.alert('加载广告数据失败');
                });
        }
    });

    $scope.adData(0);
});