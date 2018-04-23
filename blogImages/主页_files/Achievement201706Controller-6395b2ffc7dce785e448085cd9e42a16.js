var Achievement201706Controller = app.controller('Achievement201706Controller', function($rootScope,$scope,$http,ngDialog) {
    // adUrlFind('/business#/achievement',window.location.origin);
    $scope.ad_start=moment().subtract(1, 'month').format('YYYY-MM');
    $scope.ad_end='2017-06';

    $scope.query = {
        page:0,
        appParam:'',
        admParam:'',
        userParam:'',
        admIds:'',
        userIds:'',
        ym: $scope.ad_start
    };

    $scope.adPageSize=[
        {name:15,value:15},
        {name:30,value:30},
        {name:50,value:50},
        {name:100,value:100},
        {name:200,value:200},
        {name:500,value:500}
    ];

    $scope.monthUp = function(){
        $scope.query.ym=moment($scope.query.ym+'-01').add(1, 'month').format('YYYY-MM');
        $scope.achievementData(0);
    };

    $scope.monthDown=function () {
        // $scope.query.ym=moment('2017-01-01').subtract(1, 'month').format('YYYY-MM');
        $scope.query.ym=moment($scope.query.ym+'-01').subtract(1, 'month').format('YYYY-MM');
        $scope.achievementData(0);
    };

    function adVerifyDate(val) {
        if(val>$scope.ad_start){
            toastr.warning('您选择的年月超过了可查询月份','提示');
        }

        if(val<$scope.ad_end){
            toastr.warning('您选择的年月无效，只能查询201706及以后月份的业绩数据','提示');
        }
    }

    $scope.$watch('query.ym',function (newdate) {
        adVerifyDate(newdate);
    });

    $scope.achievementData = function(page){
        if ($scope.query.ym == undefined || $scope.query.ym == null || $scope.query.ym == ''){
            toastr.warning('请选择业绩月份','提示');
            return;
        }
        $scope.achiData(page);
        $scope.spreadData(page);
        $scope.arrearsData(page);
        $scope.couponPayData(page);
    };

    //应用推广明细是1，客户欠款是2，代金券支付金额是3
    $scope.adChangePageSize0=function () {
        $scope.achiData(0);
    };
    $scope.adChangePageSize1=function () {
        $scope.spreadData(0);
    };
    $scope.adChangePageSize2=function () {
        $scope.arrearsData(0);
    };
    $scope.adChangePageSize3=function () {
        $scope.couponPayData(0);
    };


    $scope.achiData = function(page){
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });
        $scope.query.userIdList = [];
        angular.forEach( $scope.outputModel.outputData, function( value, key ) {
            $scope.query.userIdList.push(value.id);
        });

        $http.post(ctx + '/business/achievement/achievementData',$scope.query)
            .success(function(data, status) {
                if(data.success){
                    $scope.pageResult0 = data.result;
                    $scope.query.pageSize0 = data.result.size;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('加载业绩数据失败');
            });
    };

    $scope.spreadData = function(page){
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });
        $scope.query.userIdList = [];
        angular.forEach( $scope.outputModel.outputData, function( value, key ) {
            $scope.query.userIdList.push(value.id);
        });

        $http.post(ctx + '/business/achievement/spreadData',$scope.query)
            .success(function(data, status) {
                if(data.success){
                    var len=data.result.content.length;
                    var totalSettleMoney=0;
                    var total_spread=0;
                    var total_pass=0;
                    var totalSum=0;
                    for(var i=0;i<len;i++){
                        if(data.result.content[i].settleMoney!=undefined&&data.result.content[i].settleMoney!=''){
                            totalSettleMoney=totalSettleMoney+data.result.content[i].settleMoney;
                        }
                        if(data.result.content[i].spreadNum!=undefined&&data.result.content[i].spreadNum!=''){
                            total_spread=total_spread+data.result.content[i].spreadNum;
                        }
                        if(data.result.content[i].passNum!=undefined&&data.result.content[i].passNum!=''){
                            total_pass=total_pass+data.result.content[i].passNum;
                        }
                        if(data.result.content[i].settleNum!=undefined&&data.result.content[i].settleNum!=''){
                            totalSum=totalSum+data.result.content[i].settleNum;
                        }
                    }

                    $scope.pageResult1 = data.result;
                    $scope.query.pageSize1 = data.result.size;
                    $scope.pageResult1.totalSettleMoney=totalSettleMoney;
                    $scope.pageResult1.totalSpread=total_spread;
                    $scope.pageResult1.totalPass=total_pass;
                    $scope.pageResult1.totalSum=totalSum;

                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('加载推广数据失败');
            });
    };

    $scope.arrearsData = function(page){
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });
        $scope.query.userIdList = [];
        angular.forEach( $scope.outputModel.outputData, function( value, key ) {
            $scope.query.userIdList.push(value.id);
        });

        $http.post(ctx + '/business/achievement/arrearsData',$scope.query)
            .success(function(data, status) {
                if(data.success){

                    var len=data.result.content.length;
                    var totalArrearsMoney=0;
                    for(var i=0;i<len;i++){
                        if(data.result.content[i].arrearsMoney!=undefined && data.result.content[i].arrearsMoney!=''){
                            totalArrearsMoney=totalArrearsMoney+data.result.content[i].arrearsMoney;
                        }
                    }

                    $scope.pageResult2 = data.result;
                    $scope.query.pageSize2 = data.result.size;
                    $scope.pageResult2.totalArrearsMoney=totalArrearsMoney;

                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('加载客户欠款数据失败');
            });
    };

    $scope.couponPayData = function(page){
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });
        $scope.query.userIdList = [];
        angular.forEach( $scope.outputModel.outputData, function( value, key ) {
            $scope.query.userIdList.push(value.id);
        });

        $http.post(ctx + '/business/achievement/couponPayData',$scope.query)
            .success(function(data, status) {
                if(data.success){
                    var len=data.result.content.length;
                    var totalCouponPayMoney=0;
                    for(var i=0;i<len;i++){
                        if(data.result.content[i].couponPayMoney!=undefined && data.result.content[i].couponPayMoney!=''){
                            totalCouponPayMoney=totalCouponPayMoney+data.result.content[i].couponPayMoney;
                        }
                    }

                    $scope.pageResult3 = data.result;
                    $scope.query.pageSize3 = data.result.size;
                    $scope.pageResult3.totalCouponPayMoney=totalCouponPayMoney;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('加载代金券支付数据失败');
            });
    };

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
        $http.post(ctx + '/business/achievement/getUserListByAdmId',{})
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
        $http.post(ctx + '/business/achievement/getBusinessManagerList',{})
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
        $scope.query.userIdList = [];
        angular.forEach( $scope.outputModel.outputData, function( value, key ) {
            $scope.query.userIdList.push(value.id);
        });

        window.open(ctx + '/business/achievement/export?userParam='+$scope.query.userParam
            +'&admIds='+$scope.query.admIdList.join(',')
            +'&userIds='+$scope.query.userIdList.join(',')
            +'&admParam='+$scope.query.admParam
            +'&appParam='+$scope.query.appParam
            +'&ym='+$scope.query.ym
        );
    };

    $scope.getUserList();
    $scope.getBmList();
    $scope.achievementData(0);
});