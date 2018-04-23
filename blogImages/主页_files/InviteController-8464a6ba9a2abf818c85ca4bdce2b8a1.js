var InviteController = app.controller('InviteController', function($rootScope,$scope,$http,ngDialog) {
    // adUrlFind('/business#invite',window.location.origin);

    $scope.query = {};

    $scope.adChangePageSize=function () {
        $scope.inviteData(0);
    };

    $scope.inviteData = function(page){
        $scope.query.page = page;
        $http.post(ctx + '/business/invite/data',$scope.query)
            .success(function(data, status) {
                $scope.pageResult = data;
                $scope.query.pageSize=$scope.pageResult.size;
            })
            .error(function(data, status) {
                toastr['error']('加载邀请链接列表失败', '提示');
            });
    };

    $scope.create = function(){
        ngDialog.confirm('确定要创建新的邀请链接？').then(function () {
            $http.post(ctx + '/business/invite/create',{})
                .success(function(data, status) {
                    $scope.inviteData(0);
                    toastr['success']('创建成功', '提示');

                })
                .error(function(data, status) {
                    toastr['error']('创建新的邀请链接失败', '提示');
                });
        }, function () {
        });
    };


    $scope.copyLink = function(item){
        $scope.currItem = item;
        ngDialog.open({
            template: $('#copyTpl').html(),
            width:'650px',
            plain:true,
            cache:false,
            scope: $scope
        });
    };

    $scope.close = function(){
        ngDialog.closeAll();
    };


    $scope.processText = function (){
        $scope.textToCopy = $scope.currItem.inviteUrl;
    };

    $scope.success = function () {
        toastr['info']("成功复制到剪切板", '提示');
    };

    $scope.fail = function (err) {
        toastr['error']("复制失败,请手动复制地址", '提示');
    };

    $scope.showInviteUser = function(item){
        $scope.currItem = item;
        ngDialog.open({
            name:'detail',
            template: ctx + '/business/invite/user',
            width:'650px',
            scope: $scope,
            cache:false
        });
    };

    $scope.$on('ngDialog.opened', function (e, $dialog) {
        if($dialog.name == 'detail'){
            $scope.inviteUserData();
        }
    });

    $scope.inviteUserData = function(){
        $http.post(ctx + '/business/invite/user/data',{inviteCode:$scope.currItem.code})
            .success(function(data, status) {
                $scope.userResult = data;
            })
            .error(function(data, status) {
                ngDialog.alert('加载邀请用户数据失败');
            });
    };

    $scope.inviteData(0);
});