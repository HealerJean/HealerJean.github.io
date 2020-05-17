<#assign ctx=Request.request.getContextPath()>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>HealerJean后台登录</title>
    <script type="text/javascript" src="${ctx}/assets/js/plugins/jquery/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/angularjs/base/angular.all.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/angularjs/postencode/angular-post-encode.js"></script>

    <!-- bootstarp--->
    <link href="${ctx}/assets/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" media="all" />
    <script type="text/javascript" src="${ctx}/assets/js/plugins/bootstrap/js/bootstrap.min.js"></script>
    <!--// bootstarp-css -->

    <#-- angularjs ng dialog -->
    <link rel="stylesheet" href="${ctx}/assets/js/angularjs/ngdialog/css/ngDialog-theme-default.min.css">
    <link rel="stylesheet" href="${ctx}/assets/js/angularjs/ngdialog/css/ngDialog.min.css">
    <script type="text/javascript" src="${ctx}/assets/js/angularjs/ngdialog/js/ngDialog.js"></script>



    <script type="text/javascript">
        var app = angular.module('app',[
            'ngAnimate'
            ,"ngMessages"
            ,"ngDialog"
            ,"httpPostFix"
        ]);


        var Controller = app.controller('Controller', function($scope,$http,ngDialog) {

            $scope.loadJobs = function(){
                $http.get('${ctx}/quartz/jobList')
                        .success(function(data, status) {
                            $scope.jobList = data;
                        })
                        .error(function(data, status) {
                            ngDialog.alert('操作失败');
                        });
            };

            $scope.createJob = function(){
                $scope.add = {};
                ngDialog.open({
                    template: $('#addJobTpl').html(),
                    width: '450px',
                    plain: true,
                    cache: false,
                    showClose: false,
                    scope: $scope
                });
            };

            $scope.editJob = function(item){
                $scope.edit = angular.copy(item);
                $scope.edit.jobCron = item.cron;
                ngDialog.open({
                    template: $('#editJobTpl').html(),
                    width: '450px',
                    plain: true,
                    cache: false,
                    showClose: false,
                    scope: $scope
                });
            };

            $scope.saveCreateJob = function(){
                $('#saveBtn').attr('disabled');
                $http.post('${ctx}/quartz/create',$scope.add)
                        .success(function(data, status) {
                            $('#saveBtn').removeAttr('disabled');
                            if(data.success){
                                ngDialog.close();
                                ngDialog.alert("创建job成功");
                                $scope.loadJobs();
                            } else {
                                ngDialog.alert(data.message);
                            }
                        })
                        .error(function(data, status) {
                            $('#saveBtn').removeAttr('disabled');
                            ngDialog.alert('操作失败!!!');
                        });
            };

            $scope.saveEditJob = function(){
                $('#updateBtn').attr('disabled');
                $http.post('${ctx}/quartz/update',$scope.edit)
                        .success(function(data, status) {
                            $('#saveBtn').removeAttr('disabled');
                            if(data.success){
                                ngDialog.close();
                                ngDialog.alert("更新job成功");
                                $scope.loadJobs();
                            } else {
                                ngDialog.alert(data.message);
                            }
                        })
                        .error(function(data, status) {
                            $('#updateBtn').removeAttr('disabled');
                            ngDialog.alert('操作失败!!!');
                        });
            };

            $scope.deleteJob = function(item){
                ngDialog.confirm('确定删除这个定时任务？').then(function(){
                    $scope.doing = true;
                    $http.post('${ctx}/quartz/delete',{jobId:item.jobId})
                            .success(function(data, status) {
                                if(data.success){
                                    ngDialog.alert('操作成功');
                                    $scope.loadJobs();
                                } else {
                                    ngDialog.alert(data.message);
                                }
                            })
                            .error(function(data, status) {
                                ngDialog.alert('操作失败!!!');
                            });
                });
            };

            $scope.pauseJob = function(item){
                ngDialog.confirm('确定暂停这个定时任务？').then(function(){
                    $scope.doing = true;
                    $http.post('${ctx}/quartz/pause',{jobId:item.jobId})
                            .success(function(data, status) {
                                if(data.success){
                                    ngDialog.alert('操作成功');
                                    $scope.loadJobs();
                                } else {
                                    ngDialog.alert(data.message);
                                }
                            })
                            .error(function(data, status) {
                                ngDialog.alert('操作失败!!!');
                            });
                });
            };

            $scope.resumeJob = function(item){
                ngDialog.confirm('确定恢复这个定时任务？').then(function(){
                    $scope.doing = true;
                    $http.post('${ctx}/quartz/resume',{jobId:item.jobId})
                            .success(function(data, status) {
                                if(data.success){
                                    ngDialog.alert('操作成功');
                                    $scope.loadJobs();
                                } else {
                                    ngDialog.alert(data.message);
                                }
                            })
                            .error(function(data, status) {
                                ngDialog.alert('操作失败!!!');
                            });
                });
            };

            $scope.loadJobs();
        });
    </script>


</head>
<body  ng-app="app" ng-controller="Controller">

<div class="row" >
    <div class="col-md-12">

        <div>
            <input type="button" value="创建" class="btn btn-primary" ng-click="createJob()"/>
        </div>


        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>jobId</th>
                <th>jobClass</th>
                <th>cron</th>
                <th>状态</th>
                <th>描述</th>
                <th>上次执行</th>
                <th>下次执行</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="item in jobList">
                <td ng-bind="item.jobId"></td>
                <td ng-bind="item.jobClass"></td>
                <td ng-bind="item.cron"></td>
                <td ng-bind="item.jobStatus"></td>
                <td ng-bind="item.jobDesc"></td>
                <td ng-bind="item.previousFireTime|date:'yyyy-MM-dd HH:mm:ss'"></td>
                <td ng-bind="item.nextFireTime|date:'yyyy-MM-dd HH:mm:ss'"></td>
                <td>
                    <a ng-if="item.jobStatus=='PAUSED'" ng-click="resumeJob(item)">恢复</a>
                    <a ng-if="item.jobStatus=='NORMAL'" ng-click="pauseJob(item)">暂停</a>
                    <a ng-click="editJob(item)">修改时间</a>
                    <a ng-click="deleteJob(item)">删除</a>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>

<script type="text/template" id="addJobTpl">


    <form method="post">
        <h3>所有字段都必填啊，我就不检查了</h3>
        <div class="form-group">
            <label for="exampleInputEmail1">jobId</label>
            <input type="text" class="form-control" ng-model="add.jobId"  >
        </div>
        <div class="form-group">
            <label for="exampleInputPassword1">jobCron</label>
            <input type="text" class="form-control" ng-model="add.jobCron" >
        </div>
        <div class="form-group">
            <label for="exampleInputPassword1">jobClass</label>
            <input type="text" class="form-control" ng-model="add.jobClass" >
        </div>
        <div class="form-group">
            <label for="exampleInputPassword1">任务描述</label>
            <input type="text" class="form-control" ng-model="add.jobDesc" >
        </div>
        <div class="form-group">
            <input type="button" class="btn btn-primary" id="saveBtn" value="保存" ng-click="saveCreateJob()"/>
        </div>
    </form>

</script>

<script type="text/template" id="editJobTpl">

    <form method="post">
        <h3>修改Job执行时间</h3>
        <div class="form-group">
            <label for="exampleInputEmail1">jobId</label>
            <input type="text" class="form-control" ng-model="edit.jobId"  >
        </div>
        <div class="form-group">
            <label for="exampleInputEmail1">jobCron</label>
            <input type="text" class="form-control" ng-model="edit.jobCron" >
        </div>
        <div class="form-group">
            <input type="button" class="btn btn-primary" id="updateBtn" value="保存" ng-click="saveEditJob()"/>
        </div>
    </form>

</script>
