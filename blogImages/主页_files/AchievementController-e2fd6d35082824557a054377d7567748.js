var AchievementController = app.controller('AchievementController', function ($rootScope, $scope, $http, ngDialog) {
    // adUrlFind('/business#/achievement',window.location.origin);
    $scope.ad_start = moment().subtract(1, 'month').format('YYYY-MM');
    $scope.ad_curr = moment().format('YYYY-MM');
    $scope.ad_end = '2017-01';

    $scope.query = {
        page: 0,
        appParam: '',
        admParam: '',
        userParam: '',
        admIds: '',
        userIds: '',
        ym: $scope.ad_start
    };

    $scope.dataStartDateStr = $scope.query.ym + "-01 00:00:00";
    $scope.dataEndDateStr = moment($scope.query.ym + '-01').endOf('month').format('YYYY-MM-DD HH:mm:ss');

    $scope.adPageSize = [
        {name: 15, value: 15},
        {name: 30, value: 30},
        {name: 50, value: 50},
        {name: 100, value: 100},
        {name: 200, value: 200},
        {name: 500, value: 500}
    ];

    $scope.monthUp = function () {
        $scope.query.ym = moment($scope.query.ym + '-01').add(1, 'month').format('YYYY-MM');
        $scope.achievementData(0);
    };

    $scope.monthDown = function () {
        // $scope.query.ym=moment('2017-01-01').subtract(1, 'month').format('YYYY-MM');
        $scope.query.ym = moment($scope.query.ym + '-01').subtract(1, 'month').format('YYYY-MM');
        $scope.achievementData(0);
    };

    function adVerifyDate(val) {
        if (val > $scope.ad_curr) {
            toastr.warning('您选择的年月超过了可查询月份', '提示');
        }

        if (val < $scope.ad_end) {
            toastr.warning('您选择的年月太久远了', '提示');
        }

        $scope.dataStartDateStr = $scope.query.ym + "-01 00:00:00";
        if (moment($scope.query.ym + '-01').endOf('month').isBefore(moment())) {
            $scope.dataEndDateStr = moment($scope.query.ym + '-01').endOf('month').format('YYYY-MM-DD HH:mm:ss');
        } else {
            $scope.dataEndDateStr = moment().format("YYYY-MM-DD") + " 06:00:00";
        }
    }

    $scope.$watch('query.ym', function (newdate) {
        adVerifyDate(newdate);
    });

    $scope.achievementData = function (page) {
        if ($scope.query.ym == undefined || $scope.query.ym == null || $scope.query.ym == '') {
            toastr.warning('请选择业绩月份', '提示');
            return;
        }
        $scope.collectData(page);
        $scope.kpiDirectData(page);
        $scope.kpiAgentData(page);
        $scope.kpiDdbData(page);
        $scope.kpiMediaData(page);
        $scope.shikeDetailsData(page);
        $scope.shikeSpreadDetailsData(page);
        $scope.ddbDetailsData(page);
    };

    $scope.adChangePageSizeCollect = function () {
        $scope.collectData(0);
    };
    $scope.adChangePageSizeKpiDirect = function () {
        $scope.kpiDirectData(0);
    };
    $scope.adChangePageSizeKpiAgent = function () {
        $scope.kpiAgentData(0);
    };
    $scope.adChangePageSizeKpiDdb = function () {
        $scope.kpiDdbData(0);
    };
    $scope.adChangePageSizeKpiMedia = function () {
        $scope.kpiMediaData(0);
    };
    $scope.adChangePageSizeShikeDetails = function () {
        $scope.shikeDetailsData(0);
    };
    $scope.adChangePageSizeShikeSpreadDetails = function () {
        $scope.shikeSpreadDetailsData(0);
    };
    $scope.adChangePageSizeDdbDetails = function () {
        $scope.ddbDetailsData(0);
    };

    $scope.collectData = function (page) {
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach($scope.outputModelAdm.outputData, function (value, key) {
            $scope.query.admIdList.push(value.id);
        });
        // $scope.query.userIdList = [];
        // angular.forEach($scope.outputModel.outputData, function (value, key) {
        //     $scope.query.userIdList.push(value.id);
        // });

        $http.post(ctx + '/business/achievement/collectData', $scope.query)
            .success(function (data, status) {
                if (data.success) {
                    var len = data.result.content.length;

                    var shikeDirectTotalMoney = 0;
                    var monthNewAppTotalMoney = 0;
                    var monthStop90TotalMoney = 0;
                    var monthStop45TotalMoney = 0;
                    var monthOldAppTotalMoney = 0;
                    var oldAgentOldAppTotalMoney = 0;
                    var oldAgentNewAppTotalMoney = 0;
                    var newAgentOldAppTotalMoney = 0;
                    var newAgentNewAppTotalMoney = 0;
                    var monthArrearsTotalMoney = 0;
                    var monthBeforeTotalMoney = 0;
                    var ddbTotalMoney = 0;
                    // var ddbMonthArrearsTotalMoney = 0;
                    var ddbMonthBeforeTotalMoney = 0;

                    for (var i = 0; i < len; i++) {
                        if (data.result.content[i].monthMoney != undefined && data.result.content[i].monthMoney != '') {
                            shikeDirectTotalMoney = shikeDirectTotalMoney + data.result.content[i].monthMoney;
                        }
                        if (data.result.content[i].monthBeforeMoney != undefined && data.result.content[i].monthBeforeMoney != '') {
                            shikeDirectTotalMoney = shikeDirectTotalMoney + data.result.content[i].monthBeforeMoney;
                        }
                        if (data.result.content[i].monthNewAppMoney != undefined && data.result.content[i].monthNewAppMoney != '') {
                            monthNewAppTotalMoney = monthNewAppTotalMoney + data.result.content[i].monthNewAppMoney;
                        }
                        if (data.result.content[i].monthStop90Money != undefined && data.result.content[i].monthStop90Money != '') {
                            monthStop90TotalMoney = monthStop90TotalMoney + data.result.content[i].monthStop90Money;
                        }
                        if (data.result.content[i].monthStop45Money != undefined && data.result.content[i].monthStop45Money != '') {
                            monthStop45TotalMoney = monthStop45TotalMoney + data.result.content[i].monthStop45Money;
                        }
                        if (data.result.content[i].monthOldAppMoney != undefined && data.result.content[i].monthOldAppMoney != '') {
                            monthOldAppTotalMoney = monthOldAppTotalMoney + data.result.content[i].monthOldAppMoney;
                        }
                        if (data.result.content[i].oldAgentOldAppMoney != undefined && data.result.content[i].oldAgentOldAppMoney != '') {
                            oldAgentOldAppTotalMoney = oldAgentOldAppTotalMoney + data.result.content[i].oldAgentOldAppMoney;
                        }
                        if (data.result.content[i].oldAgentNewAppMoney != undefined && data.result.content[i].oldAgentNewAppMoney != '') {
                            oldAgentNewAppTotalMoney = oldAgentNewAppTotalMoney + data.result.content[i].oldAgentNewAppMoney;
                        }
                        if (data.result.content[i].newAgentOldAppMoney != undefined && data.result.content[i].newAgentOldAppMoney != '') {
                            newAgentOldAppTotalMoney = newAgentOldAppTotalMoney + data.result.content[i].newAgentOldAppMoney;
                        }
                        if (data.result.content[i].newAgentNewAppMoney != undefined && data.result.content[i].newAgentNewAppMoney != '') {
                            newAgentNewAppTotalMoney = newAgentNewAppTotalMoney + data.result.content[i].newAgentNewAppMoney;
                        }
                        if (data.result.content[i].monthArrearsMoney != undefined && data.result.content[i].monthArrearsMoney != '') {
                            monthArrearsTotalMoney = monthArrearsTotalMoney + data.result.content[i].monthArrearsMoney;
                        }
                        if (data.result.content[i].monthBeforeMoney != undefined && data.result.content[i].monthBeforeMoney != '') {
                            monthBeforeTotalMoney = monthBeforeTotalMoney + data.result.content[i].monthBeforeMoney;
                        }
                        if (data.result.content[i].ddbMoney != undefined && data.result.content[i].ddbMoney != '') {
                            ddbTotalMoney = ddbTotalMoney + data.result.content[i].ddbMoney;
                        }
                        // if (data.result.content[i].ddbMonthArrearsMoney != undefined && data.result.content[i].ddbMonthArrearsMoney != '') {
                        //     ddbMonthArrearsTotalMoney = ddbMonthArrearsTotalMoney + data.result.content[i].ddbMonthArrearsMoney;
                        // }
                        if (data.result.content[i].ddbMonthBeforeMoney != undefined && data.result.content[i].ddbMonthBeforeMoney != '') {
                            ddbMonthBeforeTotalMoney = ddbMonthBeforeTotalMoney + data.result.content[i].ddbMonthBeforeMoney;
                        }

                    }
                    $scope.pageResultCollect = data.result;
                    $scope.query.pageSizeCollect = data.result.size;

                    $scope.pageResultCollect.shikeDirectTotalMoney = shikeDirectTotalMoney;
                    $scope.pageResultCollect.monthNewAppTotalMoney = monthNewAppTotalMoney;
                    $scope.pageResultCollect.monthStop90TotalMoney = monthStop90TotalMoney;
                    $scope.pageResultCollect.monthStop45TotalMoney = monthStop45TotalMoney;
                    $scope.pageResultCollect.monthOldAppTotalMoney = monthOldAppTotalMoney;
                    $scope.pageResultCollect.oldAgentOldAppTotalMoney = oldAgentOldAppTotalMoney;
                    $scope.pageResultCollect.oldAgentNewAppTotalMoney = oldAgentNewAppTotalMoney;
                    $scope.pageResultCollect.newAgentOldAppTotalMoney = newAgentOldAppTotalMoney;
                    $scope.pageResultCollect.newAgentNewAppTotalMoney = newAgentNewAppTotalMoney;
                    $scope.pageResultCollect.monthArrearsTotalMoney = monthArrearsTotalMoney;
                    $scope.pageResultCollect.monthBeforeTotalMoney = monthBeforeTotalMoney;
                    $scope.pageResultCollect.ddbTotalMoney = ddbTotalMoney;
                    // $scope.pageResultCollect.ddbMonthArrearsTotalMoney = ddbMonthArrearsTotalMoney;
                    $scope.pageResultCollect.ddbMonthBeforeTotalMoney = ddbMonthBeforeTotalMoney;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('加载业绩汇总数据失败');
            });
    };

    $scope.kpiDirectData = function (page) {
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach($scope.outputModelAdm.outputData, function (value, key) {
            $scope.query.admIdList.push(value.id);
        });
        // $scope.query.userIdList = [];
        // angular.forEach($scope.outputModel.outputData, function (value, key) {
        //     $scope.query.userIdList.push(value.id);
        // });

        $http.post(ctx + '/business/achievement/kpiDirectData', $scope.query)
            .success(function (data, status) {
                if (data.success) {

                    var len = data.result.content.length;

                    var settleShikeTotalMoney = 0;
                    var settleShikeNewAppTotalMoney = 0;
                    var settleShikeStop90TotalMoney = 0;
                    var settleShikeStop45TotalMoney = 0;
                    var settleShikeOldAppTotalMoney = 0;
                    var newAppTotalNum = 0;

                    for (var i = 0; i < len; i++) {
                        if (data.result.content[i].settleShikeMoney != undefined && data.result.content[i].settleShikeMoney != '') {
                            settleShikeTotalMoney = settleShikeTotalMoney + data.result.content[i].settleShikeMoney;
                        }
                        if (data.result.content[i].settleShikeNewAppMoney != undefined && data.result.content[i].settleShikeNewAppMoney != '') {
                            settleShikeNewAppTotalMoney = settleShikeNewAppTotalMoney + data.result.content[i].settleShikeNewAppMoney;
                        }
                        if (data.result.content[i].settleShikeStop90Money != undefined && data.result.content[i].settleShikeStop90Money != '') {
                            settleShikeStop90TotalMoney = settleShikeStop90TotalMoney + data.result.content[i].settleShikeStop90Money;
                        }
                        if (data.result.content[i].settleShikeStop45Money != undefined && data.result.content[i].settleShikeStop45Money != '') {
                            settleShikeStop45TotalMoney = settleShikeStop45TotalMoney + data.result.content[i].settleShikeStop45Money;
                        }
                        if (data.result.content[i].settleShikeOldAppMoney != undefined && data.result.content[i].settleShikeOldAppMoney != '') {
                            settleShikeOldAppTotalMoney = settleShikeOldAppTotalMoney + data.result.content[i].settleShikeOldAppMoney;
                        }
                        if (data.result.content[i].newAppNum != undefined && data.result.content[i].newAppNum != '') {
                            newAppTotalNum = newAppTotalNum + data.result.content[i].newAppNum;
                        }
                    }
                    $scope.pageResultKpiDirect = data.result;
                    $scope.query.pageSizeKpiDirect = data.result.size;

                    $scope.pageResultKpiDirect.settleShikeTotalMoney = settleShikeTotalMoney;
                    $scope.pageResultKpiDirect.settleShikeNewAppTotalMoney = settleShikeNewAppTotalMoney;
                    $scope.pageResultKpiDirect.settleShikeStop90TotalMoney = settleShikeStop90TotalMoney;
                    $scope.pageResultKpiDirect.settleShikeStop45TotalMoney = settleShikeStop45TotalMoney;
                    $scope.pageResultKpiDirect.settleShikeOldAppTotalMoney = settleShikeOldAppTotalMoney;
                    $scope.pageResultKpiDirect.newAppTotalNum = newAppTotalNum;

                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('加载直客指标数据失败');
            });
    };

    $scope.kpiAgentData = function (page) {
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach($scope.outputModelAdm.outputData, function (value, key) {
            $scope.query.admIdList.push(value.id);
        });
        // $scope.query.userIdList = [];
        // angular.forEach($scope.outputModel.outputData, function (value, key) {
        //     $scope.query.userIdList.push(value.id);
        // });

        $http.post(ctx + '/business/achievement/kpiAgentData', $scope.query)
            .success(function (data, status) {
                if (data.success) {
                    var len = data.result.content.length;

                    var settleShikeTotalMoney = 0;
                    var settleOldUserOldAppTotalMoney = 0;
                    var settleOldUserNewAppTotalMoney = 0;
                    var settleNewUserOldAppTotalMoney = 0;
                    var settleNewUserNewAppTotalMoney = 0;
                    var newAppTotalNum = 0;

                    for (var i = 0; i < len; i++) {
                        if (data.result.content[i].settleShikeMoney != undefined && data.result.content[i].settleShikeMoney != '') {
                            settleShikeTotalMoney = settleShikeTotalMoney + data.result.content[i].settleShikeMoney;
                        }
                        if (data.result.content[i].settleOldUserOldAppMoney != undefined && data.result.content[i].settleOldUserOldAppMoney != '') {
                            settleOldUserOldAppTotalMoney = settleOldUserOldAppTotalMoney + data.result.content[i].settleOldUserOldAppMoney;
                        }
                        if (data.result.content[i].settleOldUserNewAppMoney != undefined && data.result.content[i].settleOldUserNewAppMoney != '') {
                            settleOldUserNewAppTotalMoney = settleOldUserNewAppTotalMoney + data.result.content[i].settleOldUserNewAppMoney;
                        }
                        if (data.result.content[i].settleNewUserOldAppMoney != undefined && data.result.content[i].settleNewUserOldAppMoney != '') {
                            settleNewUserOldAppTotalMoney = settleNewUserOldAppTotalMoney + data.result.content[i].settleNewUserOldAppMoney;
                        }
                        if (data.result.content[i].settleNewUserNewAppMoney != undefined && data.result.content[i].settleNewUserNewAppMoney != '') {
                            settleNewUserNewAppTotalMoney = settleNewUserNewAppTotalMoney + data.result.content[i].settleNewUserNewAppMoney;
                        }
                        if (data.result.content[i].newAppNum != undefined && data.result.content[i].newAppNum != '') {
                            newAppTotalNum = newAppTotalNum + data.result.content[i].newAppNum;
                        }
                    }
                    $scope.pageResultKpiAgent = data.result;
                    $scope.query.pageSizeKpiAgent = data.result.size;

                    $scope.pageResultKpiAgent.settleShikeTotalMoney = settleShikeTotalMoney;
                    $scope.pageResultKpiAgent.settleOldUserOldAppTotalMoney = settleOldUserOldAppTotalMoney;
                    $scope.pageResultKpiAgent.settleOldUserNewAppTotalMoney = settleOldUserNewAppTotalMoney;
                    $scope.pageResultKpiAgent.settleNewUserOldAppTotalMoney = settleNewUserOldAppTotalMoney;
                    $scope.pageResultKpiAgent.settleNewUserNewAppTotalMoney = settleNewUserNewAppTotalMoney;
                    $scope.pageResultKpiAgent.newAppTotalNum = newAppTotalNum;


                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('加载渠道指标数据失败');
            });
    };

    $scope.kpiDdbData = function (page) {
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach($scope.outputModelAdm.outputData, function (value, key) {
            $scope.query.admIdList.push(value.id);
        });

        $http.post(ctx + '/business/achievement/kpiDdbData', $scope.query)
            .success(function (data, status) {
                if (data.success) {
                    $scope.pageResultKpiDdb = data.result;
                    $scope.query.pageSizeKpiDdb = data.result.size;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('加载多点宝指标数据失败');
            });
    };

    $scope.kpiMediaData = function (page) {
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach($scope.outputModelAdm.outputData, function (value, key) {
            $scope.query.admIdList.push(value.id);
        });
        // $scope.query.userIdList = [];
        // angular.forEach($scope.outputModel.outputData, function (value, key) {
        //     $scope.query.userIdList.push(value.id);
        // });

        $http.post(ctx + '/business/achievement/kpiMediaData', $scope.query)
            .success(function (data, status) {
                if (data.success) {
                    $scope.pageResultKpiMedia = data.result;
                    $scope.query.pageSizeKpiMedia = data.result.size;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('加载媒介指标数据失败');
            });
    };

    $scope.shikeDetailsData = function (page) {
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach($scope.outputModelAdm.outputData, function (value, key) {
            $scope.query.admIdList.push(value.id);
        });
        // $scope.query.userIdList = [];
        // angular.forEach($scope.outputModel.outputData, function (value, key) {
        //     $scope.query.userIdList.push(value.id);
        // });

        $http.post(ctx + '/business/achievement/shikeDetailsData', $scope.query)
            .success(function (data, status) {
                if (data.success) {

                    var len = data.result.content.length;
                    var total_numbers = 0;
                    var total_spread = 0;
                    var total_pass = 0;
                    var totalSum = 0;
                    for (var i = 0; i < len; i++) {
                        if (data.result.content[i].settleMoney != undefined && data.result.content[i].settleMoney != null) {
                            total_numbers = total_numbers + data.result.content[i].settleMoney;
                        }
                        if (data.result.content[i].spreadNum != undefined && data.result.content[i].spreadNum != null) {
                            total_spread = total_spread + data.result.content[i].spreadNum;
                        }
                        if (data.result.content[i].passNum != undefined && data.result.content[i].passNum != null) {
                            total_pass = total_pass + data.result.content[i].passNum;
                        }
                        if (data.result.content[i].settleNum != undefined && data.result.content[i].settleNum != null) {
                            totalSum = totalSum + data.result.content[i].settleNum;
                        }
                    }

                    $scope.pageResultShikeDetails = data.result;
                    $scope.query.pageSizeShikeDetails = data.result.size;

                    $scope.pageResultShikeDetails.totalNumbers = total_numbers;
                    $scope.pageResultShikeDetails.totalSpread = total_spread;
                    $scope.pageResultShikeDetails.totalPass = total_pass;
                    $scope.pageResultShikeDetails.totalSum = totalSum;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('加载试客业绩明细数据失败');
            });
    };

    $scope.shikeSpreadDetailsData = function (page) {
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach($scope.outputModelAdm.outputData, function (value, key) {
            $scope.query.admIdList.push(value.id);
        });
        // $scope.query.userIdList = [];
        // angular.forEach($scope.outputModel.outputData, function (value, key) {
        //     $scope.query.userIdList.push(value.id);
        // });

        $http.post(ctx + '/business/achievement/shikeSpreadDetailsData', $scope.query)
            .success(function (data, status) {
                if (data.success) {

                    var len = data.result.content.length;
                    var total_numbers = 0;
                    var total_spread = 0;
                    var total_pass = 0;
                    var totalSum = 0;
                    for (var i = 0; i < len; i++) {
                        if (data.result.content[i].settleMoney != undefined && data.result.content[i].settleMoney != null) {
                            total_numbers = total_numbers + data.result.content[i].settleMoney;
                        }
                        if (data.result.content[i].spreadNum != undefined && data.result.content[i].spreadNum != null) {
                            total_spread = total_spread + data.result.content[i].spreadNum;
                        }
                        if (data.result.content[i].passNum != undefined && data.result.content[i].passNum != null) {
                            total_pass = total_pass + data.result.content[i].passNum;
                        }
                        if (data.result.content[i].settleNum != undefined && data.result.content[i].settleNum != null) {
                            totalSum = totalSum + data.result.content[i].settleNum;
                        }
                    }

                    $scope.pageResultShikeSpreadDetails = data.result;
                    $scope.query.pageSizeShikeSpreadDetails = data.result.size;

                    $scope.pageResultShikeSpreadDetails.totalNumbers = total_numbers;
                    $scope.pageResultShikeSpreadDetails.totalSpread = total_spread;
                    $scope.pageResultShikeSpreadDetails.totalPass = total_pass;
                    $scope.pageResultShikeSpreadDetails.totalSum = totalSum;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('加载试客跑量明细数据失败');
            });
    };

    $scope.ddbDetailsData = function (page) {
        $scope.query.page = page;

        $scope.query.admIdList = [];
        angular.forEach($scope.outputModelAdm.outputData, function (value, key) {
            $scope.query.admIdList.push(value.id);
        });
        // $scope.query.userIdList = [];
        // angular.forEach($scope.outputModel.outputData, function (value, key) {
        //     $scope.query.userIdList.push(value.id);
        // });

        $http.post(ctx + '/business/achievement/ddbDetailsData', $scope.query)
            .success(function (data, status) {
                if (data.success) {

                    var len = data.result.content.length;
                    var totalClickNumber = 0;
                    var totalDownloadNumber = 0;
                    var totalMoney = 0;

                    for (var i = 0; i < len; i++) {
                        if (data.result.content[i].clickNumber != undefined && data.result.content[i].clickNumber != null) {
                            totalClickNumber = totalClickNumber + data.result.content[i].clickNumber;
                        }
                        if (data.result.content[i].downloadNumber != undefined && data.result.content[i].downloadNumber != null) {
                            totalDownloadNumber = totalDownloadNumber + data.result.content[i].downloadNumber;
                        }
                        if (data.result.content[i].settleMoney != undefined && data.result.content[i].settleMoney != null) {
                            totalMoney = totalMoney + data.result.content[i].settleMoney;
                        }
                    }

                    $scope.pageResultDdbDetails = data.result;
                    $scope.query.pageSizeDdbDetails = data.result.size;

                    $scope.pageResultDdbDetails.totalClickNumber = totalClickNumber;
                    $scope.pageResultDdbDetails.totalDownloadNumber = totalDownloadNumber;
                    $scope.pageResultDdbDetails.totalMoney = totalMoney;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('加载多点宝业绩明细数据失败');
            });
    };

    $scope.userList = [];

    $scope.outputModel = {outputData: []};

    $scope.localLang = {
        selectAll: "全选",
        selectNone: "清空",
        reset: "重置",
        search: "搜索...",
        nothingSelected: "全部"         //default-label is deprecated and replaced with this.
    };

    $scope.getUserList = function () {
        $http.post(ctx + '/business/achievement/getUserListByAdmId', {})
            .success(function (data, status) {
                if (data.success) {
                    $scope.userList = data.result;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('获取客户下拉列表数据失败');
            });
    };

    $scope.bmList = [];

    $scope.outputModelAdm = {outputData: []};

    $scope.localLang = {
        selectAll: "全选",
        selectNone: "清空",
        reset: "重置",
        search: "搜索...",
        nothingSelected: "全部"         //default-label is deprecated and replaced with this.
    };

    $scope.getBmList = function () {
        $http.post(ctx + '/business/achievement/getBusinessManagerList', {})
            .success(function (data, status) {
                if (data.success) {
                    $scope.bmList = data.result;
                } else {
                    ngDialog.alert(data.message);
                }
            })
            .error(function (data, status) {
                ngDialog.alert('获取商务经理列表失败');
            });
    };

    $scope.export = function () {
        $scope.query.admIdList = [];
        angular.forEach($scope.outputModelAdm.outputData, function (value, key) {
            $scope.query.admIdList.push(value.id);
        });
        $scope.query.userIdList = [];
        angular.forEach($scope.outputModel.outputData, function (value, key) {
            $scope.query.userIdList.push(value.id);
        });

        window.open(ctx + '/business/achievement/export?admIds=' + $scope.query.admIdList.join(',')
            + '&admParam=' + $scope.query.admParam
            + '&ym=' + $scope.query.ym
        );
    };

    // $scope.getUserList();
    $scope.getBmList();
    $scope.achievementData(0);
});