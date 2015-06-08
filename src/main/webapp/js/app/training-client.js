var TH_CLIENT = angular.module('trainingHelperClient', ["ngResource"]);

TH_CLIENT.factory("TrainingClient", function($resource) {

    return $resource('/rest/training/:trainingId', {},
        {
            list: {method:'GET', url:"/rest/training/list", isArray: false }
        });

}).factory("LoginClient", function($resource) {

    return $resource('/rest/user/:op', {},
        {
            loginInfo: {method:'GET',params: {op : 'loginInfo'}, isArray: false}
        });
});