var SupperTaskerController = app.controller('SupperTaskerController', function($scope,$rootScope,$http,$location,ngDialog,$routeParams) {
    // adUrlFind('/business#'+$location.path(),window.location.origin);

    $('.configDobleTime').click(function () {
        daterangebtnChange();
    });

    $scope.adDate={
        startDate:moment().startOf("day").format("YYYY-MM-DD HH:mm:ss"),
        endDate:moment().endOf('day').format("YYYY-MM-DD HH:mm:ss"),
        startDateStr : moment().format('YYYY.MM.DD'),
        endDateStr: moment().format('YYYY.MM.DD'),
        index:1
    };

    $scope.query = {
        page:0,
        startDate: $scope.adDate.startDate,
        endDate : $scope.adDate.endDate,
        statusDesc:"全部",
        userId:"",
        userIds:"",
        admIds:"",
        userParam:"",
        appParam:"",
        trackId:"",
        newAppFlag:"0",
        orderByPassRate:"",
        name:""
    };

    $scope.pageResult = {};
    $scope.checkedItems = [];
    $scope.checkList = [];
    $scope.query.appInfo={trackName:'全部'};




    $scope.adOpts = {
        // timePicker:true,
        // timePicker24Hour: true,
        ranges: {
            '今天': [moment().startOf('day'), moment().endOf('day')],
            '昨天': [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')],
            // '前天': [moment().subtract(2, 'days').startOf('day'), moment().subtract(2, 'days').endOf('day')],
            '过去7天': [moment().subtract(6, 'days').startOf('day'), moment().endOf('day')],
            '过去30天': [moment().subtract(29, 'days').startOf('day'), moment().endOf('day')],
            '这个月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
            '近一年': [moment().subtract(1, 'year').startOf('day'), moment().endOf('day')],
        },
        alwaysShowCalendars: true,
        startDate: moment().startOf('day'),
        endDate: moment().endOf('day'),
        maxDate: moment().endOf('day'),
        applyClass: "btn btn-primary",
        opens: "right"
    };

    $scope.$on('ngRepeatFinished',function(){
        //父控制器执行操作
        $('[data-toggle="popover"]').popover({ trigger: "hover" });
    });

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
        $http.post(ctx + '/business/spread/getBusinessManagerList',{})
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


    $scope.getBmList();



    $scope.spreadData = function(page){

        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });
        // $http.post(ctx + '/business/supperTasker/json',$scope.query)
        $http.post(ctx + '/business/supperTasker/data',$scope.query)
            .success(function(data, status){
                $scope.pageResult = data;
            })
            .error(function(data, status){
                $('.processing').hide();
                toastr['error']('加载应用推广信息失败', '提示');
            });
    };

    $scope.spreadData();

});

