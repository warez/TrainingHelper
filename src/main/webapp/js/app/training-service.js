var TH_SERVICE = angular.module('trainingHelperService', ["ngResource"]);

TH_SERVICE.factory("TrainingService", function($resource) {

    return $resource('/rest/training/:trainingId', {},
        {
            getDocument: {method:'GET', url:"/rest/training/document", isArray: false},

            list: {method:'GET', url:"/rest/training/list", isArray: false }
        });

});