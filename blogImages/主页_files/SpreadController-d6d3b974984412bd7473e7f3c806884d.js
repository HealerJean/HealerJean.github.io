var SpreadController = app.controller('SpreadController', function($scope,$rootScope,$http,$location,ngDialog,$routeParams) {
    // adUrlFind('/business#'+$location.path(),window.location.origin);

    $('.configDobleTime').click(function () {
        daterangebtnChange();
    });

    $scope.adDate={
        // startDate:'2017-01-01 00:00:00',
        // endDate:'2017-01-31 23:59:59',
        // startDateStr : '2017-01-01',
        // endDateStr: '2017-01-31',
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

    //设置全部
    $scope.loadMyApps = function(){
        $http.get(ctx + '/business/spread/myApps')
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
        $http.get(ctx + '/business/spread/loadMyUsers')
            .success(function(data, status) {
                $scope.myUserList = data;
            })
            .error(function(data, status) {
                ngDialog.alert('加载客户列表失败');
            });
    };
    
    $scope.adShowAll=function () {
        if($scope.pageResult.content.length>0){
            for(var i=0;i<$scope.pageResult.content.length;i++){
                $scope.pageResult.content[i].selected=true;

                angular.forEach($scope.pageResult.content[i].keywords, function( value, key ) {
                    if (value.keywordRank == undefined || value.keywordRank == null || value.keywordRank == '') {
                        $scope.getKeywordSpreadPartData(value,$scope.pageResult.content[i]);
                    }
                });
            }
            $scope.pageResult.allSelected=true;
        }
    };

    $scope.adChangePageSize=function () {
        $scope.spreadData(0);
    };

    $scope.notSelectItem=function () {
        for(var i=0;i<$scope.pageResult.content.length;i++){
            $scope.pageResult.content[i].selected=undefined;
        }
        $scope.pageResult.allSelected=undefined;
    };

    $scope.caculateSelectAll=function () {
        var j=0;
        for(var i=0;i<$scope.pageResult.content.length;i++){
            if($scope.pageResult.content[i].selected==undefined){
                j=j+1;
            }
        }
        if(j==$scope.pageResult.content.length-1){
            $scope.pageResult.allSelected=undefined;
        }
    };
    $scope.selectPlanItem=function (item) {
        if(item.selected==undefined){
            // $scope.notSelectItem();
            item.selected=true;

            angular.forEach(item.keywords, function( value, key ) {
                if (value.keywordRank == undefined || value.keywordRank == null || value.keywordRank == '') {
                    $scope.getKeywordSpreadPartData(value,item);
                }
            });

        }else{
            $scope.caculateSelectAll();
            item.selected=undefined;
        }
    };

    $scope.getKeywordSpreadPartData = function(keywordItem,item){
        $http.post(ctx + '/business/spread/getKeywordSpreadPartData',{keywordId:keywordItem.id,keyword:keywordItem.keyword,trackId:item.trackId})
            .success(function(data, status) {
                if(data.success){
                    keywordItem.keywordRankBefore = data.result.keywordRankBefore;
                    keywordItem.keywordRankAfter = data.result.keywordRankAfter;
                    keywordItem.keywordRankBeforeDate = data.result.keywordRankBeforeDate;
                    keywordItem.keywordRankAfterDate = data.result.keywordRankAfterDate;

                    keywordItem.keywordRank = data.result.keywordRank;
                    keywordItem.remoteBackNum = data.result.remoteBackNum;
                    if(data.result.remoteBackNum>0){
                        if(item.adTotalRomoteBackNum==undefined){
                            item.adTotalRomoteBackNum=0;
                        }
                        item.adTotalRomoteBackNum=item.adTotalRomoteBackNum+data.result.remoteBackNum;
                    }
                    //预约量
                    keywordItem.reservationNum = data.result.reservationNum;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('获取订单部分数据失败');
            });
    };

    $scope.refeshList=function (page) {
        $scope.selectedList=[];
        for(var i=0;i<$scope.pageResult.content.length;i++){
            if($scope.pageResult.content[i].selected==true){
                $scope.selectedList.push($scope.pageResult.content[i]);
            }
        }
        $scope.spreadData(page);
    };

    $scope.orderByFinfishRate=function () {
        if($scope.query.orderByPassRate==undefined || $scope.query.orderByPassRate==null || $scope.query.orderByPassRate=='' ){
            $scope.query.orderByPassRate=1;
        }else if($scope.query.orderByPassRate==1){
            $scope.query.orderByPassRate=2;
        }else if($scope.query.orderByPassRate==2){
            $scope.query.orderByPassRate='';
        }
        $scope.spreadData($scope.query.page);
    };

    $scope.spreadData = function(page){
        $('.processing').show();
        $scope.query.page = page;
        $scope.adSingleAppTotal=false;
        if($scope.query.appInfo!=undefined){
            $scope.query.trackId = $scope.query.appInfo.trackId;
            if($scope.query.trackId!=undefined){
                $scope.adSingleAppTotal=true;
            }
        }
        $scope.query.admIdList = [];
        angular.forEach( $scope.outputModelAdm.outputData, function( value, key ) {
            $scope.query.admIdList.push(value.id);
        });
        $scope.query.userIdList = [];
        angular.forEach( $scope.outputModel.outputData, function( value, key ) {
            $scope.query.userIdList.push(value.id);
        });

        $http.post(ctx + '/business/spread/data',$scope.query)
            .success(function(data, status){
                $('.processing').hide();

                var len=data.content.length;
                var ad_total_release=0;
                var ad_total_display=0;
                var ad_total_download=0;
                var ad_total_pass=0;
                var ad_total_balance=0;
                for(var i=0;i<len;i++){
                    var totalNumbers = 0;
                    var totalNumbersBalance = 0;
                    var totalNumbersNoBalance = 0;
                    var totalDisplay=0;
                    var clickNum = 0;
                    var clickCleanNum = 0;
                    var remoteBackNum = 0;
                    var passNum = 0;
                    var passNumNoBalance = 0;
                    var totalBalance=0;// 特惠结算量之和
                    var hasBalance=false;// 特惠结算量之和
                    var playPassNum = 0;
                    var downloadNum = 0;
                    var totalQueryCount=0;
                    var totalReservation=0;
                    var totalClickCount=0;
                    var totalQueryCountUniq=0;
                    var totalClickCountUniq=0;
                    var lenSon=data.content[i].keywords.length;
                    var newAppSpreadFlag = 0;
                    for(var j=0;j<lenSon;j++){
                        if(data.content[i].keywords[j].downloadNum>0){
                            totalNumbers = totalNumbers + data.content[i].keywords[j].number;
                        }
                        if (newAppSpreadFlag == 0 && data.content[i].keywords[j].newAppFlag == 1){
                            newAppSpreadFlag = 1;
                        }
                        if(data.content[i].keywords[j].status==9){
                            if(data.content[i].keywords[j].passNum<data.content[i].keywords[j].number){
                                totalDisplay = totalDisplay + data.content[i].keywords[j].number-data.content[i].keywords[j].passNum;
                            }

                        }
                        if(data.content[i].keywords[j].reservationNum>0){
                            totalReservation=totalReservation+data.content[i].keywords[j].reservationNum;
                        }
                        clickNum = clickNum + data.content[i].keywords[j].clickNum;
                        clickCleanNum = clickCleanNum + data.content[i].keywords[j].clickCleanNum;
                        if (data.content[i].keywords[j].remoteBackNum != undefined && data.content[i].keywords[j].remoteBackNum != null && data.content[i].keywords[j].remoteBackNum > 0) {
                            remoteBackNum = remoteBackNum + data.content[i].keywords[j].remoteBackNum;
                        }
                        downloadNum = downloadNum + data.content[i].keywords[j].downloadNum;
                        passNum = passNum + data.content[i].keywords[j].passNum;
                        if(data.content[i].keywords[j].newAppFlag==1){
                            if(data.content[i].keywords[j].passNum>data.content[i].keywords[j].number){
                                data.content[i].keywords[j].balance=data.content[i].keywords[j].number;
                                hasBalance=true;
                            }else{
                                data.content[i].keywords[j].balance=data.content[i].keywords[j].passNum;

                            }
                            totalNumbersBalance=totalNumbersBalance+data.content[i].keywords[j].number;
                            totalBalance=totalBalance+data.content[i].keywords[j].balance;
                        }else{
                            passNumNoBalance=passNumNoBalance+data.content[i].keywords[j].passNum;
                            totalNumbersNoBalance=totalNumbersNoBalance+data.content[i].keywords[j].number;
                        }
                        playPassNum = playPassNum + data.content[i].keywords[j].playPassNum;
                        var date3=data.content[i].keywords[j].spreadDateEnd-data.content[i].keywords[j].spreadDateStart;
                        //计算出相差天数
                        var days=Math.floor(date3/(24*3600*1000));
                        //计算出小时数
                        var leave1=date3%(24*3600*1000);    //计算天数后剩余的毫秒数
                        var hours=Math.round(10*leave1/(3600*1000))/10;
                        var totalHours=24*days+hours;
                        var time=''+totalHours+'H';
                        data.content[i].keywords[j].duration=time;

                        //时间合并
                        var adStart_time=data.content[i].keywords[j].spreadDateStart;
                        var adEnd_time=data.content[i].keywords[j].spreadDateEnd;
                        var adS=0;
                        if(data.content[i].keywords[j].adCaculated==undefined){
                            for(var k=j;k<lenSon;k++){
                                if(adStart_time==data.content[i].keywords[k].spreadDateStart&&adEnd_time==data.content[i].keywords[k].spreadDateEnd){
                                    adS=adS+1;
                                    if(adS>1){
                                        data.content[i].keywords[k].adCaculated=true;
                                    }
                                }else{
                                    break;
                                }
                            }
                        }
                        data.content[i].keywords[j].adTimeSum=adS;

                        if(data.content[i].keywords[j].queryCount>0){
                            totalQueryCount=totalQueryCount+data.content[i].keywords[j].queryCount;
                        }
                        if(data.content[i].keywords[j].clickCount>0){
                            totalClickCount=totalClickCount+data.content[i].keywords[j].clickCount;
                        }
                        if(data.content[i].keywords[j].queryCountUniq>0){
                            totalQueryCountUniq=totalQueryCountUniq+data.content[i].keywords[j].queryCountUniq;
                        }
                        if(data.content[i].keywords[j].clickCountUniq>0){
                            totalClickCountUniq=totalClickCountUniq+data.content[i].keywords[j].clickCountUniq;
                        }

                    }
                    data.content[i].newAppSpreadFlag=newAppSpreadFlag;
                    data.content[i].totalNumbers=totalNumbers;
                    // data.content[i].totalNumbersBalance=totalNumbersBalance;
                    // data.content[i].totalNumbersNoBalance=totalNumbersNoBalance;
                    data.content[i].totalDisplay=totalDisplay;
                    data.content[i].totalReservation=totalReservation;
                    data.content[i].clickNum=clickNum;
                    data.content[i].clickCleanNum=clickCleanNum;
                    data.content[i].remoteBackNum=remoteBackNum;
                    data.content[i].passNum=passNum;
                    if(passNumNoBalance>totalNumbersNoBalance){
                        data.content[i].totalBalance=totalNumbersNoBalance+totalBalance;
                    }else{
                        data.content[i].totalBalance=passNumNoBalance+totalBalance;
                    }
                    data.content[i].hasBalance=hasBalance;
                    data.content[i].playPassNum=playPassNum;
                    data.content[i].downloadNum=downloadNum;

                    data.content[i].totalQueryCount=totalQueryCount;
                    data.content[i].totalClickCount=totalClickCount;
                    data.content[i].totalQueryCountUniq=totalQueryCountUniq;
                    data.content[i].totalClickCountUniq=totalClickCountUniq;

                    if(data.content[i].totalNumbers>0){
                        ad_total_release=data.content[i].totalNumbers+ad_total_release;
                    }
                    if(data.content[i].totalDisplay>0){
                        ad_total_display=data.content[i].totalDisplay+ad_total_display;
                    }
                    if(data.content[i].downloadNum>0){
                        ad_total_download=data.content[i].downloadNum+ad_total_download;
                    }
                    if(data.content[i].passNum>0){
                        ad_total_balance=data.content[i].totalBalance+ad_total_balance;
                        ad_total_pass=data.content[i].passNum+ad_total_pass;
                    }
                }

                data.ad_total_release=ad_total_release;
                data.ad_total_display=ad_total_display;
                data.ad_total_download=ad_total_download;
                data.ad_total_pass=ad_total_pass;
                data.ad_total_balance=ad_total_balance;

                if($scope.selectedList!=undefined&&$scope.selectedList!=''){
                    for(var k=0;k<$scope.selectedList.length;k++){
                        if($scope.selectedList[k].selected==true){
                            for(var m=0;m<data.content.length;m++){
                                if($scope.selectedList[k].trackId==data.content[m].trackId&&$scope.selectedList[k].ymd==data.content[m].ymd){
                                    data.content[m].selected=true;
                                    angular.forEach(data.content[m].keywords, function( value, key ) {
                                        if (value.keywordRank == undefined || value.keywordRank == null || value.keywordRank == '') {
                                            $scope.getKeywordSpreadPartData(value,data.content[m]);
                                        }
                                    });
                                }
                            }
                        }
                    }
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
        // dateNav:1
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

    //todo 现在的问题是，在js中写的ng-click() 怎么能生效 或者说在js中写其对应的方法呢？
    $scope.adSelectTime=function (val) {
        // console.log(val);
        // console.log(121212)
    };

    //双日历选择器
    /*$('.configDobleTime').daterangepicker({
        "timePicker": true,
        "timePicker24Hour": true,
        "ranges": {
            '今天': [moment().startOf('day'), moment().endOf('day')],
            '昨天': [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')],
            '过去7天': [moment().subtract(6, 'days').startOf('day'), moment().endOf('day')],
            '过去30天': [moment().subtract(29, 'days').startOf('day'), moment().endOf('day')],
            '这个月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        // "alwaysShowCalendars": true,
        "startDate": moment().startOf('day'),
        "endDate": moment().endOf('day'),
        "maxDate": moment().endOf('day'),
        "opens": "right",
        "applyClass": "btn btn-primary",
        // "dateNav":1
    }, function(start, end, label) {
        $scope.query.startDate = start.format("YYYY-MM-DD HH:mm:ss");
        $scope.query.startDateStr = start.format('YYYY.MM.DD');
        $scope.query.endDate = end.format("YYYY-MM-DD HH:mm:ss");
        $scope.query.endDateStr = end.format('YYYY.MM.DD');
        $scope.spreadData(0);
    });*/


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
        $http.post(ctx + '/business/spread/getUserListByAdmId',{})
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

    $scope.loadCustomHeaderMap = function(){
        $http.post(ctx + '/business/spread/loadCustomHeaderMap',{})
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
        $http.post(ctx + '/business/spread/loadCustomHeaderList',{})
            .success(function(data, status) {
                if (data.success){
                    $scope.customHeaderList = data.result;
                    /*var n = 0;
                    angular.forEach( $scope.customHeaderList, function( value, key ) {
                        if (value.isShow == 1){
                            n = n + 1;
                        }
                    });
                    $scope.noDataColspan = n;*/
                }else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                ngDialog.alert('获取列表项失败');
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

    $scope.saveCustomHeader = function(){
        $scope.checkHeaderList = [];
        angular.forEach($scope.customHeaderList, function(re){
            if(re.isShow == 1){
                $scope.checkHeaderList.push(re.id);
            }
        });
        $http.post(ctx + '/business/spread/saveCustomHeader',{headerIds:$scope.checkHeaderList.join(',')})
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


    $scope.loadCustomHeaderMap();
    $scope.loadCustomHeaderList();


    $scope.getBmList();
    // $scope.loadMyApps();
    $scope.spreadData($scope.query.page);
    // $scope.getUserList();



    //试客推广下载IDFA
    $scope.toggleCheckAll = function(){
        if($scope.checkAll){
            for(var i = 0 ; i < $scope.pageResult.content.length ; i ++ ){
                $scope.pageResult.content[i].checked = true;
                $scope.checkList.push($scope.pageResult.content[i].id);
                $scope.checkedItems.push($scope.pageResult.content[i]);
            }
        } else {
            for(var i = 0 ; i < $scope.pageResult.content.length ; i ++ ){
                $scope.checkedItems.splice($scope.pageResult.content[i]);
                $scope.pageResult.content[i].checked = false;
            }
        }
    };

    $scope.toggleCheck = function(item){
        if (item.checked){
            $scope.checkedItems.push(item);
            $scope.checkList.push(item.id);
        } else {
            $scope.checkedItems.splice($scope.checkedItems.indexOf(item),1);
            $scope.checkList.push(item.id);

        }
    };

    $scope.downloadIdfa = function(word,ymd){
        var now =  moment(new Date()).format('YYYY-MM-DD');

        if(ymd == now){
            toastr.warning("今天IDFA数据还未生成，请选择其他时间",'提示');
            return;
        }
        //var startDate = ymd + " " + moment(word.spreadDateStart).format('HH:mm');
        //var endDate = ymd + " " +  moment(word.spreadDateEnd).format('HH:mm');
        //console.log("startDate:"+startDate+","+endDate);
        window.open(ctx + '/business/spread/downloadIdfa?ymd=' + ymd
                +'&type=1'
                +'&keyword='+word.keyword
                + '&spreadId=' + word.id
        );
    };

    $scope.batchDownloadIdfa = function(){
        var now =  moment(new Date()).format('YYYY-MM-DD');
        var ids = [];
        for(var i = 0 ; i < $scope.checkedItems.length ; i ++){
            for(var j = 0 ; j < $scope.checkedItems[i].keywords.length ; j ++){
                ids.push($scope.checkedItems[i].ymd+"_"+$scope.checkedItems[i].keywords[j].id);
            }
        }
        if($scope.checkedItems.length > 0){
            // console.log($scope.checkedItems[0].ymd+","+now);
            if($scope.checkedItems[0].ymd == now){
                toastr.warning("今天IDFA数据还未生成，请选择其他时间",'提示');
                return;
            }
        }

        window.open(ctx + '/business/spread/batchDownloadIdfa?type=1&ids=' + ids.join(','));
    };

    $scope.export = function(){

        $scope.adSingleAppTotal=false;
        if($scope.query.appInfo!=undefined){
            $scope.query.trackId = $scope.query.appInfo.trackId;
            if($scope.query.trackId!=undefined){
                $scope.adSingleAppTotal=true;
            }
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
        if ($scope.query.orderByPassRate == undefined){
            $scope.query.orderByPassRate = '';
        }
        window.open(ctx + '/business/spread/export?userId='+$scope.query.userId
            +'&admIds='+$scope.query.admIdList.join(',')
            +'&userIds='+$scope.query.userIdList.join(',')
            +'&userParam='+$scope.query.userParam
            +'&appParam='+$scope.query.appParam
            +'&trackId='+$scope.query.trackId
            +'&orderByPassRate='+$scope.query.orderByPassRate
            +'&newAppFlag='+$scope.query.newAppFlag
            +'&startDate='+$scope.query.startDate
            +'&endDate='+$scope.query.endDate
        );
    };

    $scope.downloadClickCleanIdfa = function(word){
        window.open(ctx + '/business/spread/downloadClickCleanIdfa?keywordId=' + word.id);
    };

});

// angular.bootstrap(document, ['app']);