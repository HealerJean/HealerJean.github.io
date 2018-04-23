var SpreadCooperateController = app.controller('SpreadCooperateController', function($rootScope,$scope,$http,$routeParams,$location,ngDialog) {
    // adUrlFind('/business#'+$location.path(),window.location.origin);

    $('.configDobleTime').click(function () {
        daterangebtnChange();
    });

    $scope.adDate={
        startDate:moment().subtract(1, 'months').startOf("day").format("YYYY-MM-DD HH:mm:ss"),
        endDate:moment().endOf('day').format("YYYY-MM-DD HH:mm:ss"),
        startDateStr : moment().subtract(1, 'months').format('YYYY.MM.DD'),
        endDateStr: moment().format('YYYY.MM.DD'),
        index:1
    };

    $scope.query = {
        page:0,
        startDate: $scope.adDate.startDate,
        endDate : $scope.adDate.endDate,
        admIds:"",
        userParam:"",
        appParam:""
    };

    $scope.adOpts = {
        timePicker:true,
        timePicker24Hour: true,
        ranges: {
            '今天': [moment().startOf('day'), moment().endOf('day')],
            '昨天': [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')],
            '前天': [moment().subtract(2, 'days').startOf('day'), moment().subtract(2, 'days').endOf('day')],
            '过去6天': [moment().subtract(7, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')],
            '过去30天': [moment().subtract(29, 'days').startOf('day'), moment().endOf('day')],
            '这个月': [moment().startOf('month'), moment().subtract(1, 'days')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
            '近三月': [moment().subtract(2, 'month').startOf('month'), moment().endOf('day')],
            '近一年': [moment().subtract(1, 'year').startOf('day'), moment().endOf('day')]
        },
        alwaysShowCalendars: true,
        startDate:moment().subtract(1, 'months').startOf("day"),
        endDate:moment().endOf('day'),
        maxDate: moment().endOf('day'),
        applyClass: "btn btn-primary",
        opens: "right"
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
            $scope.spreadData(0);
        }
    }, false);


    $scope.adChangePageSize=function () {
        $scope.spreadData(0);
    };

    $scope.spreadData = function(page){
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });

        $http.post(ctx + '/business/spread/cooperate/data',$scope.query)
            .success(function(data, status) {
                if (data.success) {
                    var adTotalNum=0;
                    var adTotalPassNum=0;
                    var adTotalMoney=0;

                    if(data.result.content!=undefined){
                        angular.forEach(data.result.content,function (value,key) {
                            if(value.number>0){
                                adTotalNum=adTotalNum+value.number;
                            }
                            if(value.passNumber>0){
                                adTotalPassNum=adTotalPassNum+value.passNumber;
                            }
                            if(value.settleMoney>0){
                                adTotalMoney=adTotalMoney+value.settleMoney;
                            }
                        });
                        data.result.adTotalNum=adTotalNum;
                        data.result.adTotalPassNum=adTotalPassNum;
                        data.result.adTotalMoney=adTotalMoney;
                    }

                    $scope.pageResult = data.result;
                    $scope.query.pageSize=$scope.pageResult.size;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('加载数据失败');
            });
    };

    $scope.localLang = {
        selectAll       : "全选",
        selectNone      : "清空",
        reset           : "重置",
        search          : "搜索...",
        nothingSelected : "全部"         //default-label is deprecated and replaced with this.
    };

    $scope.bmList = [];

    $scope.outputModelAdm = {outputData: []};

    $scope.getBmList = function(){
        $http.post(ctx + '/business/spread/cooperate/getBusinessManagerList',{})
            .success(function(data, status) {
                if(data.success){
                    $scope.bmList = data.result;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('获取商务经理列表失败');
            });
    };

    $scope.export = function(){
        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });

        window.open(ctx + '/business/spread/cooperate/export?admIds='+$scope.query.admIdList.join(',')
            +'&userParam='+$scope.query.userParam
            +'&appParam='+$scope.query.appParam
            +'&startDate='+$scope.query.startDate
            +'&endDate='+$scope.query.endDate
        );
    };



    $scope.spreadData(0);
    $scope.getBmList();
});