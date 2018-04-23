var PlaySpreadController = app.controller('PlaySpreadController', function($scope,$rootScope,$http,$location,ngDialog,$routeParams) {
    // adUrlFind('/business#'+$location.path(),window.location.origin);

    $('.configDobleTime').click(function () {
        daterangebtnChange()
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
        userId:"",
        userIds:"",
        admIds:"",
        userParam:"",
        appParam:"",
        trackId:"",
        name:""
    };

    $scope.pageResult = {};
    $scope.query.appInfo={trackName:'全部'};

    //设置全部
    $scope.loadMyApps = function(){
        $http.get(ctx + '/business/play/spread/myApps')
            .success(function(data, status) {
                data.unshift({trackName:'全部'});
                $scope.myAppList = data;
            })
            .error(function(data, status) {
                ngDialog.alert('加载应用列表失败');
            });
    };

    //设置客户下拉列表
    $scope.loadMyUsers = function(){
        $http.get(ctx + '/business/play/spread/loadMyUsers')
            .success(function(data, status) {
                $scope.myUserList = data;
            })
            .error(function(data, status) {
                ngDialog.alert('加载客户列表失败');
            });
    };

    $scope.adChangePageSize=function () {
        $scope.playSpreadData(0);
    };

    $scope.playSpreadData = function(page){
        $('.processing').show();
        $scope.query.page = page;
        if($scope.query.appInfo!=undefined){
            $scope.query.trackId = $scope.query.appInfo.trackId;
        }
        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });
        $scope.query.userIdList = [];
        angular.forEach( $scope.outputModel.outputData, function( value, key ) {
            $scope.query.userIdList.push(value.id);
        });

        $http.post(ctx + '/business/play/spread/data',$scope.query)
            .success(function(data, status){
                $('.processing').hide();

                var len=data.content.length;
                for(var i=0;i<len;i++){
                    var totalNumbers = 0;
                    var clickNum = 0;
                    var passNum = 0;
                    var lenSon=data.content[i].details.length;
                    for(var j=0;j<lenSon;j++){

                        totalNumbers = totalNumbers + data.content[i].details[j].number;
                        clickNum = clickNum + data.content[i].details[j].clickNum;
                        passNum = passNum + data.content[i].details[j].passNum;
                        var date3=data.content[i].details[j].spreadDateEnd-data.content[i].details[j].spreadDateStart;
                        //计算出相差天数
                        var days=Math.floor(date3/(24*3600*1000));
                        //计算出小时数
                        var leave1=date3%(24*3600*1000);    //计算天数后剩余的毫秒数
                        var hours=Math.round(10*leave1/(3600*1000))/10;
                        var totalHours=24*days+hours;
                        var time=''+totalHours+'H';
                        data.content[i].details[j].duration=time;

                        //时间合并
                        var adStart_time=data.content[i].details[j].spreadDateStart;
                        var adEnd_time=data.content[i].details[j].spreadDateEnd;
                        var adS=0;
                        if(data.content[i].details[j].adCaculated==undefined){
                            for(var k=j;k<lenSon;k++){
                                // console.log(i+'-'+j+'-'+k)
                                if(adStart_time==data.content[i].details[k].spreadDateStart&&adEnd_time==data.content[i].details[k].spreadDateEnd){
                                    adS=adS+1;
                                    if(adS>1){
                                        data.content[i].details[k].adCaculated=true;
                                    }
                                }else{
                                    break;
                                }
                            }
                        }
                        data.content[i].details[j].adTimeSum=adS;
                    }
                    data.content[i].totalNumbers=totalNumbers;
                    data.content[i].clickNum=clickNum;
                    data.content[i].passNum=passNum;
                }
                $scope.pageResult = data;
                $scope.query.pageSize=$scope.pageResult.size;
            })
            .error(function(data, status){
                $('.processing').hide();
                toastr['error']('加载应用推广信息失败', '提示');
            });
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
            '近一年': [moment().subtract(1, 'year').startOf('day'), moment().endOf('day')],
        },
        alwaysShowCalendars: true,
        startDate: moment().startOf('day'),
        endDate: moment().endOf('day'),
        maxDate: moment().endOf('day'),
        applyClass: "btn btn-primary",
        opens: "right",
        // dateNav:1
    };

    $scope.$watch('adDate', function(newDate) {
        // console.log(newDate);
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
            $scope.playSpreadData(0);
        }
    }, false);


    $scope.userList = [];

    $scope.outputModel = {outputData: []};

    $scope.localLang = {
        selectAll       : "全选",
        selectNone      : "清空",
        reset           : "重置",
        search          : "搜索...",
        nothingSelected : "全部"         //default-label is deprecated and replaced with this.
    };

    $scope.getUserList = function(){
        $http.post(ctx + '/business/play/spread/getUserListByAdmId',{})
            .success(function(data, status) {
                if(data.success){
                    $scope.userList = data.result;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('获取客户下拉列表数据失败');
            });
    };

    $scope.bmList = [];

    $scope.outputModelAdm = {outputData: []};

    $scope.localLang = {
        selectAll       : "全选",
        selectNone      : "清空",
        reset           : "重置",
        search          : "搜索...",
        nothingSelected : "全部"         //default-label is deprecated and replaced with this.
    };

    $scope.getBmList = function(){
        $http.post(ctx + '/business/play/spread/getBusinessManagerList',{})
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

    $scope.loadCustomHeaderMap = function(){
        $http.post(ctx + '/business/play/spread/loadCustomHeaderMap',{})
            .success(function(data, status) {
                if (data.success){
                    $scope.customHeader = data.result;
                }else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('自定义显示列表项出错');
            });
    };

    $scope.loadCustomHeaderList = function(){
        $http.post(ctx + '/business/play/spread/loadCustomHeaderList',{})
            .success(function(data, status) {
                if (data.success){
                    $scope.customHeaderList = data.result;
                    var n = 0;
                    angular.forEach( $scope.customHeaderList, function( value, key ) {
                        if (value.isShow == 1){
                            n = n + 1;
                        }
                    });
                    $scope.noDataColspan = n;
                }else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('获取列表项失败');
            });
    };

    $scope.saveCustomHeader = function(){
        $scope.checkList = [];
        angular.forEach($scope.customHeaderList, function(re){
            if(re.isShow == 1){
                $scope.checkList.push(re.id);
            }
        });
        $http.post(ctx + '/business/play/spread/saveCustomHeader',{headerIds:$scope.checkList.join(',')})
            .success(function(data, status) {
                if(data.success){
                    toastr.success('操作成功','提示');
                    ngDialog.close();
                    $scope.loadCustomHeaderMap();
                    $scope.loadCustomHeaderList();
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('自定义列表项失败');
            });
    };

    $scope.deleteSelectedItem=function(item){
        item.isShow = 0;
    };

    $scope.addSelectedItem=function(item){
        item.isShow = 1;
    };

    $scope.setCustomHeader=function(){
        ngDialog.open({
            template: $('#customHeader').html(),
            width:'650px',
            plain:true,
            scope: $scope,
            cache:false
        });
    };

    $scope.export = function(){

        if($scope.query.appInfo!=undefined){
            $scope.query.trackId = $scope.query.appInfo.trackId;
        }
        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });
        $scope.query.userIdList = [];
        angular.forEach( $scope.outputModel.outputData, function( value, key ) {
            $scope.query.userIdList.push(value.id);
        });
        if ($scope.query.trackId == undefined){
            $scope.query.trackId = '';
        }
        window.open(ctx + '/business/play/spread/export?userId='+$scope.query.userId
            +'&admIds='+$scope.query.admIdList.join(',')
            +'&userIds='+$scope.query.userIdList.join(',')
            +'&userParam='+$scope.query.userParam
            +'&appParam='+$scope.query.appParam
            +'&trackId='+$scope.query.trackId
            +'&startDate='+$scope.query.startDate
            +'&endDate='+$scope.query.endDate
        );
    };

    $scope.loadCustomHeaderMap();
    $scope.loadCustomHeaderList();

    $scope.getBmList();
    $scope.loadMyApps();
    $scope.playSpreadData($scope.query.page);
    $scope.getUserList();
});