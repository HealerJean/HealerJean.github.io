var RedeemApplyController = app.controller('RedeemApplyController', function($rootScope,$scope,$http,$location,ngDialog,$compile,uiCalendarConfig) {
    // adUrlFind('/business#redeem',window.location.origin);

    //兑换面值金额
    $scope.adExchange=[
        {name:'3000',value:1},
        {name:'6000',value:2},
        {name:'9000',value:3}
    ];

    $scope.query = {};

    $scope.clearStartDate = function(event){
        $scope.query.startDate = '';
        event.stopPropagation();
    };

    $scope.clearEndDate = function(event){
        $scope.query.endDate = '';
        event.stopPropagation();
    };


    $scope.newRedeemCode = function(date){
        $scope.code = {oneMoney:"3000",dayNum:3,faceValue:9000,startDate:date};
        $scope.addCode = true;
    };

    $scope.createRedeemCode = function(){
        $scope.processing = true;
        $http.post(ctx + '/business/redeem/create',$scope.code)
            .success(function(data, status) {
                $scope.processing = false;
                if(data.success){
                    toastr.success('创建兑换码成功', '提示');
                    $scope.addCode = false;
                    $scope.calendarData();
                    $scope.closeMouseDialog();
                    $scope.getCouponQuotaList(0);
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function(data, status) {
                $scope.processing = false;
                ngDialog.alert('申请兑换码失败');
            });
    };

    //$scope.startDate = '';

    //点击日历中的某一天
    $scope.dayClick = function(date, jsEvent, view){
        //获取点击的日期
        //var adToday=date.format("YYYY-MM-DD");

        //console.log(jsEvent);
        //$(adT).addClass("adApplyStyle");

        //设置弹框宽度
        var dialogWidth=285;
        //获取点击对象
        var adT=jsEvent.target;
        //获取点击对象的类
        var adClickClass=$(adT).attr("class");
        //获取当前点击对象的宽度
        var adClickWidth=$(adT).width()+5;
        var adWidth=$('.fc-view-container').width();
        var adOneWidth=Math.floor(adWidth/7);
        //前五个框宽度
        var adFiveWidth=5*adOneWidth;
        var adSixWidth=6*adOneWidth;
        //点击的位置距左边框的位置
        var adClickLeftWidth=jsEvent.pageX-212;
        //弹窗的最终显示位置
        var dialogPageX;
        var dialogPageY;
        //定义偏移量
        var dialogOffsetX;
        var dialogOffsetY;
        dialogOffsetY=jsEvent.offsetY;

        if(adClickClass=='fc-day-number'){
            dialogOffsetX=adOneWidth-adClickWidth+jsEvent.offsetX;
        }else{
            if(jsEvent.offsetX<adOneWidth){
                dialogOffsetX=jsEvent.offsetX;
            }else{
                dialogOffsetX=jsEvent.offsetX+2;
                for(var i=0;i<7;i++){
                    if(dialogOffsetX>adOneWidth){
                        dialogOffsetX=dialogOffsetX-adOneWidth;
                    }else{
                        break;
                    }
                }
            };
        }


        if(adClickLeftWidth<adFiveWidth){
            //前五个框的位置判断
            dialogPageX=jsEvent.pageX-dialogOffsetX+adOneWidth;
            dialogPageY=jsEvent.pageY-dialogOffsetY;
        }else{
            if(dialogWidth>adOneWidth){
                //弹窗宽度大于一个框的宽度,则弹框显示在左边
                dialogPageX=jsEvent.pageX-dialogOffsetX-dialogWidth;
                dialogPageY=jsEvent.pageY-dialogOffsetY;
            }else{
                //小于六个大于五个框的位置
                if(adClickLeftWidth<adSixWidth){
                    dialogPageX=jsEvent.pageX-dialogOffsetX+adOneWidth;
                    dialogPageY=jsEvent.pageY-dialogOffsetY;
                }else{
                    dialogPageX=jsEvent.pageX-dialogOffsetX-dialogWidth;
                    dialogPageY=jsEvent.pageY-dialogOffsetY;
                }
            }
        }
        jsEvent.pageX=dialogPageX;
        jsEvent.pageY=dialogPageY;

        if ($scope.eventClickT != undefined) {
            $scope.eventClickT.css('border-color', '#337ab7');
        }

        var adNow=date.format("YYYY-MM-DD HH:mm:ss");
        var adBefore=moment().subtract(1, 'days').endOf('day').format("YYYY-MM-DD HH:mm:ss");
        if(adNow>adBefore){
            $scope.newRedeemCode(date.format());
            ngDialog.mouseDialog(jsEvent,$scope,{title:'申请代金券<span style="color: red;">[起始:'+date.format()+']</span>',content:$('#redeemApply').html(),width:''+dialogWidth+''});
        }else{
            toastr['error']('不能申请今天之前的代金券', '提示');
        }
    };

    //点击兑换面值
    $scope.clickExchange=function(val){
        $scope.code.oneMoney=val;
        $scope.numChange();
    };

    //修改兑换天数和面值时 更新总金额
    $scope.numChange = function() {
        $scope.code.faceValue = $scope.code.dayNum * parseFloat($scope.code.oneMoney);
    };

    $scope.couponQuotaShow = function(){
        ngDialog.open({
            template: $('#couponQuotaShow').html(),
            width:'700px',
            plain:true,
            scope: $scope,
            cache:false
        });
    };

    $scope.transferCodeShow = function(){
        //全部取消选中
        angular.forEach($scope.bmList, function(bm){
            bm.checked = false;
        });
        ngDialog.open({
            template: $('#transferCodeAdmUser').html(),
            width:'650px',
            plain:true,
            scope: $scope,
            cache:false
        });
    };

    $scope.toggleCheck = function(item){
        angular.forEach($scope.bmList, function(bm){
            if (bm.id != item.id) {
                bm.checked = false;
            }
        });
        $scope.admUserChecked = {};

        if(item.checked){
            $scope.admUserChecked = item;
        }
    };


    $scope.admUserChecked = {};  //选中转让的商务
    //转让
    $scope.transferCode = function() {
        if ($.isEmptyObject($scope.admUserChecked)){
            toastr['error']("您要转给谁?", '提示');
            return;
        }
        ngDialog.confirm('确定要转让给【' + $scope.admUserChecked.name + '】吗？').then(function () {
            $http.post(ctx + '/business/redeem/transfer', {redeemCode: $scope.eventCode,admIdNew:$scope.admUserChecked.id})
                .success(function (data, status) {
                    if (data.success) {
                        $scope.calendarData();
                        ngDialog.close();
                        toastr['success']("操作成功!", '提示');
                        $scope.getCouponQuotaList(0);
                    } else {
                        ngDialog.alert(data.message);
                    }
                })
                .error(function (data, status) {
                    ngDialog.alert('操作失败');
                });
            $scope.eventClickT.css('border-color', '#337ab7');
        }, function () {
            $scope.eventClickT.css('border-color', '#337ab7');
        });
    };

    //删除
    $scope.delCode = function() {
        ngDialog.confirm('确定要删除此代金券吗？').then(function () {
            $http.post(ctx + '/business/redeem/delete', {redeemCode: $scope.eventCode})
                .success(function (data, status) {
                    if (data.success) {
                        $scope.calendarData();
                        $scope.closeMouseDialog();
                        toastr['success']("操作成功!", '提示');
                        $scope.getCouponQuotaList(0);
                    } else {
                        ngDialog.alert(data.message);
                    }
                })
                .error(function (data, status) {
                    ngDialog.alert('操作失败');
                });
            $scope.eventClickT.css('border-color', '#337ab7');
        }, function () {
            $scope.eventClickT.css('border-color', '#337ab7');
        });
    };

    //点击日历中的某一日程
    $scope.eventClick = function(calEvent, jsEvent, view) {
        if (calEvent.editable) {
            if ($scope.eventClickT != undefined) {
                $scope.eventClickT.css('border-color', '#337ab7');
            }
            $(this).css('border-color', 'red');
            //删除那里使用
            $scope.eventCode = calEvent.id;
            $scope.eventClickT = $(this);

            ngDialog.mouseDialog(jsEvent,$scope,{title:'代金券查看',content:$('#redeemShowDel').html(),width:'285'});

        } else {
            toastr['error']('只能处理自己申请的<br>并且状态是[未激活]的代金券', '提示');
        }
    };

    //移动日历中的某一日程 当拖拽完成并且时间改变时触发
    $scope.eventDrop = function(event, delta, revertFunc) {
        if (event.editable) {
            var endDate = new Date(event.end.format());

            ngDialog.confirm('确定要改变此代金券的日期吗？').then(function () {
                $http.post(ctx + '/business/redeem/update', {
                        redeemCode: event.id,
                        startDate: event.start.format(),
                        endDate: endDate.getFullYear() + "-" + (endDate.getMonth() + 1) + "-" + (endDate.getDate() - 1)
                    })  // 暂时无法解决日期多出一天的问题
                    .success(function (data, status) {
                        if (data.success) {
                            //$scope.calendarData();
                            toastr['success']("操作成功!", '提示');
                            //$scope.getCouponQuotaList(0);
                        } else {
                            ngDialog.alert(data.message);
                            revertFunc();
                        }
                    })
                    .error(function (data, status) {
                        ngDialog.alert('操作失败');
                        revertFunc();
                    });
            }, function () {
                revertFunc();
            });
        } else {
            toastr['error']('只能修改自己申请的<br>并且状态是[未激活]的代金券', '提示');
        }
    };

    //在日程改变大小并成功后调用
    $scope.eventResize = function(event, delta, revertFunc) {
        if (event.editable) {
            var endDate = new Date(event.end.format());

            ngDialog.confirm('确定要改变此代金券的日期吗？').then(function () {
                $http.post(ctx + '/business/redeem/update', {
                        redeemCode: event.id,
                        startDate: event.start.format(),
                        endDate: endDate.getFullYear() + "-" + (endDate.getMonth() + 1) + "-" + (endDate.getDate() - 1)
                    })  // 暂时无法解决日期多出一天的问题
                    .success(function (data, status) {
                        if (data.success) {
                            //$scope.calendarData();
                            toastr['success']("操作成功!", '提示');
                            $scope.getCouponQuotaList(0);
                        } else {
                            ngDialog.alert(data.message);
                            revertFunc();
                        }
                    })
                    .error(function (data, status) {
                        ngDialog.alert('操作失败');
                        revertFunc();
                    });
            }, function () {
                revertFunc();
            });
        } else {
            toastr['error']('只能修改自己申请的<br>并且状态是[未激活]的代金券', '提示');
        }
    };

    $scope.closeMouseDialog=function(){
        if ($scope.eventClickT != undefined) {
            $scope.eventClickT.css('border-color', '#337ab7');
        }
        ngDialog.closeMouseDialog();
    };

    $scope.toDataList=function(){
        $scope.closeMouseDialog();
        $location.path("/redeemShowOne/"+$scope.eventCode).replace();
    };

    /* config object */
    $scope.uiConfig = {
        calendar:{
            height: 650,
            editable: true,  //确定日历上的事件是否可以修改。
            firstDay:1,
            //weekNumbers:true, //是否显示一年内的第几周
            buttonText: {
                today: "今天"
            },
            header:{
                left: '',
                center: 'title',
                right: 'today prev,next'
            },
            eventDrop: $scope.eventDrop,
            eventResize: $scope.eventResize,
            dayClick: $scope.dayClick,
            eventClick: $scope.eventClick
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
    $scope.bmList = [];
    $scope.getBmList = function(){
        $http.post(ctx + '/business/redeem/getBusinessManagerList',{})
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
    $scope.quotaBalance = 0;
    $scope.getCouponQuotaList = function(page){
        $http.post(ctx + '/business/redeem/getCouponQuotaList',page)
            .success(function(data, status) {
                $scope.quotaBalance = 0;
                $scope.pageResult = data;
                angular.forEach(data.content, function(re){
                    $scope.quotaBalance =  $scope.quotaBalance + re.balance;
                });
            })
            .error(function(data, status) {
                ngDialog.alert('加载额度数据失败');
            });
    };

    $scope.calendarData();
    $scope.getCouponQuotaList(0);
    $scope.getBmList();
});

/*

function adTest(val){
    console.log($(this));
}*/
