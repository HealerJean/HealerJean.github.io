var app = angular.module('app',['ngAnimate'
    ,'ngRoute'
    ,"ngDialog"
    ,"ngMessages"
    ,"httpPostFix"
    ,"ui.select"
    ,'ui.calendar'
    ,'ui.bootstrap.datetimepicker'
    ,'angular-clipboard'
    ,"angularFileUpload"
    ,"ngSanitize"
    ,'isteven-multi-select'
    ,'daterangepicker'
]);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: ctx +'/business/home',
            controller:'HomeController'
        })
        .when('/report', {
            templateUrl: ctx +'/business/report',
            controller:'ReportController'
        })
        .when('/redeem', {
            templateUrl: ctx +'/business/redeem/apply',
            controller:'RedeemApplyController'
        })
        .when('/redeemShowOne/:code', {
            templateUrl: ctx +'/business/redeem',
            controller:'RedeemController'
        })
        .when('/redeem/query', {
            templateUrl: ctx +'/business/redeem',
            controller:'RedeemController'
        })
        .when('/ad', {
            templateUrl: ctx +'/business/ad',
            controller:'AdController'
        })
        .when('/invite', {
            templateUrl: ctx +'/business/invite',
            controller:'InviteController'
        })
        .when('/spread/cooperate', {
            templateUrl: ctx +'/business/spread/cooperate',
            controller:'SpreadCooperateController'
        })
        .when('/spread', {
            templateUrl: ctx +'/business/spread',
            controller:'SpreadController'
        })
        .when('/play/spread', {
            templateUrl: ctx +'/business/play/spread',
            controller:'PlaySpreadController'
        })
        .when('/money/apply', {
            templateUrl: ctx +'/business/special/money/apply',
            controller:'SpecialMoneyApplyController'
        })
        .when('/money/add', {
            templateUrl: ctx +'/business/special/money/apply/add',
            controller:'SpecialMoneyApplyAddController'
        })
        .when('/coupon/apply', {
            templateUrl: ctx +'/business/spread/coupon/apply',
            controller:'SpreadCouponApplyController'
        })
        .when('/coupon/add', {
            templateUrl: ctx +'/business/spread/coupon/apply/add',
            controller:'SpreadCouponApplyAddController'
        })
        .when('/achievement', {
            templateUrl: ctx +'/business/achievement',
            controller:'AchievementController'
        })
        // .when('/achievement/history', {
        //     templateUrl: ctx +'/business/achievement/history',
        //     controller:'AchievementController'
        // })
        .when('/myInvite', {
            templateUrl: ctx +'/business/myInvite',
            controller:'InviteCodeController'
        })
        .when('/supperTasker', {
            templateUrl: ctx +'/business/supperTasker',
            controller:'SupperTaskerController'
        })
        .otherwise({
            redirectTo: '/report'
        });
}]);



app.directive('paged',function () {
    return{
        restrict:'E',
        template: '<select class="adSelect" ng-change="adChangePageSize()" ng-model="query.pageSize" ng-options="option.value as option.name for option in adPageSize"></select>条',
        replace: true,
        link: function (scope, element, attrs) {
            var adFirstNum=parseInt(attrs.num);
            scope.adPageSize=[
                {name:adFirstNum,value:adFirstNum},
                {name:30,value:30},
                {name:50,value:50},
                {name:100,value:100},
                {name:200,value:200},
                {name:500,value:500}
            ];
        }
    }
});

app.directive('defineDialog',function () {
    return{
        restrict:'E',
        template:'<script type="text/template" id="customHeader"><div>'+'<h5>自定义列表项</h5><div class="divider"></div><div class="form-group adSelectSelf"><div class="adSelectItem">'+
        '<div class="col-md-3 adMidPadding" data-ng-repeat="item in customHeaderList">'+
        '<div class="margin_top_3">'+
        '<input type="checkbox" data-ng-if="item.isShow==1" data-ng-click="deleteSelectedItem(item)" checked="checked">'+
        '<input type="checkbox" data-ng-if="item.isShow==0" data-ng-click="addSelectedItem(item)">'+
        '{{item.colName}}'+
        '</div></div></div></div><div class="divider"></div>'+
        '<div class="adDialogBottom">'+
        '<a class="btn btn-primary" ng-click="saveCustomHeader()" style="margin-right: 10px;">确定</a>'+
        '<a class="btn btn-default" ng-click="adCloseDialog()">取消</a>'+
        '</div>'+
        '</div></script>',
        replace: true,
        link: function (scope, element, attrs) {
            scope.adCloseDialog=function () {
                $('.ngdialog-theme-default').remove();
            }
        }
    }
});

app.directive('pager', function(){
    return {
        restrict: 'E',
        scope: {
            page:'@',
            totalPages:'@',
            clickMethod:'='
        },
        template:'<div></div>',
        replace: true,
        link: function($scope, $element, $attrs) {
            function init(){
                laypage({
                    cont: $element,
                    pages: parseInt($attrs.totalPages),
                    curr: parseInt($attrs.page),
                    skip: true,
                    skin: '#0081ff',
                    prev: '‹', //若不显示，设置false即可
                    next: '›', //若不显示，设置false即可
                    jump: function(e, first){
                        if(!first){
                            $scope.clickMethod(parseInt(e.curr) - 1);
                        }
                    }
                });
            }
            init();

            $scope.$watch("totalPages", function (value) {
                if(value){
                    init();
                }
            });
        }
    }
});
app.directive('onRepeatFinishedRender', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    //这里element, 就是ng-repeat渲染的最后一个元素
                    scope.$emit('ngRepeatFinished', element);

                    $().piroBox({
                        my_speed: 400, //animation speed
                        bg_alpha: 0.3, //background opacity
                        slideShow : true, // true == slideshow on, false == slideshow off
                        slideSpeed : 4, //slideshow duration in seconds(3 to 6 Recommended)
                        close_all : '.piro_close,.piro_overlay'// add class .piro_overlay(with comma)if you want overlay click close piroBox
                    });
                });
            }
        }
    };
});

app.run(function($rootScope, $templateCache) {
    $rootScope.$on('$routeChangeStart', function(event, next, current) {
        if (typeof(current) !== 'undefined'){
            $templateCache.remove(current.templateUrl);
        }
    });
});

app.animation('.toggle', function() {
    return {
        leave : function(element, done) {
            done();
        }
    }
});

// angular.bootstrap(document, ['app']);
