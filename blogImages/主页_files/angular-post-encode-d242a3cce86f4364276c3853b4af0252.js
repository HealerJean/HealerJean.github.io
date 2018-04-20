// Modifies $httpProvider for correct server communication (POST variable format)
angular.module('httpPostFix', [], function($httpProvider){
    // Use x-www-form-urlencoded Content-Type
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    $httpProvider.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest';

    // Override $http service's default transformRequest
    $httpProvider.defaults.transformRequest = [function(data){
        /**
         * The workhorse; converts an object to x-www-form-urlencoded serialization.
         * @param {Object} obj
         * @return {String}
         */
        var param = function(obj){
            var query = '';
            var name, value, fullSubName, subName, subValue, innerObj, i;

            for(name in obj){
                value = obj[name];

                if(value instanceof Array){
                    for(i=0; i<value.length; ++i){
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                }
                else if(value instanceof Object){
                    for(subName in value){
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                }
                else if(value !== undefined && value !== null){
                    query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
                }
            }

            return query.length ? query.substr(0, query.length - 1) : query;
        };

        return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
    }];


    $httpProvider.interceptors.push(function() {
        return {
            'response': function(response) {
                var sessionout = response.headers('sessionout');
                if(sessionout == 'true'){
                    alert('会话已过期,请重新登陆');
                    location.href="/";
                    return;
                }
                var permission = response.headers('nopermission');
                if(permission == 'true'){
                    alert('没有权限访问此资源');
                    return;
                }
                return response;
            }
        };
    });
});