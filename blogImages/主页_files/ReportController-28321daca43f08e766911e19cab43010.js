var ReportController = app.controller('ReportController', function($rootScope,$scope,$http,ngDialog) {
    // adUrlFind('/business#report',window.location.origin);

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
        page:0
    };

    $scope.adOpts = {
        timePicker:true,
        timePicker24Hour: true,
        ranges: {
            '今天': [moment().startOf('day'), moment().endOf('day')],
            '昨天': [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')],
            '前天': [moment().subtract(2, 'days').startOf('day'), moment().subtract(2, 'days').endOf('day')],
            '过去7天': [moment().subtract(6, 'days').startOf('day'), moment().endOf('day')],
            '过去30天': [moment().subtract(29, 'days').startOf('day'), moment().endOf('day')],
            '这个月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
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
            $scope.reportData(0);
        }
    }, false);

    $scope.setQueryUser = function(item){
        $scope.query.userName = item.userName;
        $scope.query.userId = item.userId;
        $scope.reportData(0);
    };

    $scope.removeQueryUser = function(){
        $scope.query.userName = undefined;
        $scope.query.userId = undefined;
        $scope.reportData(0);
    };

    $scope.setQuerySpread = function(item){
        $scope.query.spreadId = item.spreadId;
        $scope.query.spreadName = item.spreadName;
        $scope.reportData(0);
    };
    $scope.removeQuerySpread = function(){
        $scope.query.spreadId = undefined;
        $scope.query.spreadName = undefined;
        $scope.reportData(0);
    };

    $scope.setQueryGroup = function(item){
        $scope.query.groupId = item.groupId;
        $scope.query.groupName = item.groupName;
        $scope.reportData(0);
    };
    $scope.removeQueryGroup = function(){
        $scope.query.groupId = undefined;
        $scope.query.groupName = undefined;
        $scope.reportData(0);
    };

    $scope.setQueryAd = function(item){
        $scope.query.adId = item.adId;
        $scope.query.adName = item.adName;
        $scope.reportData(0);
    };
    $scope.removeQueryAd = function(){
        $scope.query.adId = undefined;
        $scope.query.adName = undefined;
        $scope.reportData(0);
    };

    $scope.checkDay = function(item){
        $scope.dateQuery = {ymd:item.ymd,adName:item.adName,adId:item.adId,userId:item.userId};
        ngDialog.open({
            template: ctx + '/business/report/day',
            width:'650px',
            scope: $scope,
            cache:false
        });
    };

    $scope.adChangePageSize=function () {
        $scope.reportData(0);
    }

    $scope.reportData = function(page){
        $scope.query.page = page;
        $http.post(ctx + '/business/report/data',$scope.query)
            .success(function(data, status) {
                $scope.pageResult = data;
                $scope.query.pageSize=$scope.pageResult.size;
            })
            .error(function(data, status) {
                toastr['error']('加载报表数据失败', '提示');
            });
    };

    $rootScope.$on('ngDialog.opened', function (e, $dialog) {
        $http.post(ctx + '/business/report/day/data',$scope.dateQuery)
            .success(function(data, status) {
                $scope.dayResult = data;
            })
            .error(function(data, status) {
                toastr['error']('加载报表数据失败', '提示');
            });
    });

    $scope.reportData(0);

});