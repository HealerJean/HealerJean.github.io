var InviteCodeController = app.controller('InviteCodeController', function($rootScope,$scope,$http,ngDialog) {
    // adUrlFind('/business#/myInvite',window.location.origin);

    $scope.query = {};

    $scope.adChangePageSize=function () {
        $scope.inviteData(0);
    };

    $scope.inviteData = function(page){
        $scope.query.page = page;
        $http.post(ctx + '/business/myInvite/data',$scope.query)
            .success(function(data, status) {
                $scope.pageResult = data;
                $scope.query.pageSize=$scope.pageResult.size;
            })
            .error(function(data, status) {
                toastr['error']('加载邀请表失败', '提示');
            });
    };

    $scope.createInviteCode = function(){
        $http.post(ctx + '/business/myInvite/createInviteCode')
            .success(function(data, status) {
                if(data.success){
                    $scope.code = data.result;
                    toastr['info']('已经生成邀请码', '提示');
                }
            })
            .error(function(data, status) {
                toastr['error']('加载邀请表失败', '提示');
            });
    };

    $scope.inviteData(0);
});